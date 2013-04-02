/*
 * The MIT License
 *
 * Copyright (c) 2008-2013, Jenkins project, Seiji Sogabe
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package hudson.plugins.phing;

import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.Util;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Computer;
import hudson.model.Descriptor;
import hudson.plugins.phing.console.PhingConsoleAnnotator;
import hudson.tasks.Builder;
import hudson.util.ArgumentListBuilder;
import hudson.util.VariableResolver;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;
import java.util.Set;
import jenkins.model.Jenkins;

import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Phing Builder Plugin.
 *
 * @author Seiji Sogabe
 */
public final class PhingBuilder extends Builder {

    @Extension
    public static final PhingDescriptor DESCRIPTOR = new PhingDescriptor();

    /**
     * Optional build script.
     */
    private final String buildFile;

    /**
     * Identifies {@link PhingInstallation} to be used.
     */
    private final String name;

    /**
     * List of Phing targets to be invoked.
     * If not specified, use "build.xml".
     */
    private final String targets;

    /**
     * Optional properties to be passed to Phing.
     * Follow {@link Properties} syntax.
     */
    private final String properties;

    /**
     * Whether uses ModuleRoot as working directory or not.
     * @since 0.9
     */
    private final boolean useModuleRoot;

    /**
     * Additional options to be passed to Phing.
     * @since 0.12
     */
    private final String options;

    public String getBuildFile() {
        return buildFile;
    }

    public String getName() {
        return name;
    }

    public String getTargets() {
        return targets;
    }

    public String getProperties() {
        return properties;
    }

    public boolean isUseModuleRoot() {
        return useModuleRoot;
    }

    public String getOptions() {
        return options;
    }

    @DataBoundConstructor
    public PhingBuilder(String name, String buildFile, String targets, String properties,
            boolean useModuleRoot, String options) {
        super();
        this.name = Util.fixEmptyAndTrim(name);
        this.buildFile = Util.fixEmptyAndTrim(buildFile);
        this.targets = Util.fixEmptyAndTrim(targets);
        this.properties = Util.fixEmptyAndTrim(properties);
        this.useModuleRoot = useModuleRoot;
        this.options = Util.fixEmptyAndTrim(options);
    }

    public PhingInstallation getPhing(EnvVars env, BuildListener listener) 
            throws IOException, InterruptedException {
        PhingInstallation.DescriptorImpl desc = getPhingInstallationDescriptor();
        for (PhingInstallation inst : desc.getInstallations()) {
            if (name != null && name.equals(inst.getName())) {
                PhingInstallation pi = inst.forNode(Computer.currentComputer().getNode(), listener);
                return pi.forEnvironment(env);
            }
        }
        return null;
    }

    private PhingInstallation.DescriptorImpl getPhingInstallationDescriptor() {
        return (PhingInstallation.DescriptorImpl) Jenkins.getInstance().getDescriptor(PhingInstallation.class);
    }

    @Override
    public Descriptor<Builder> getDescriptor() {
        return DESCRIPTOR;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
            throws InterruptedException, IOException {

        ArgumentListBuilder args = new ArgumentListBuilder();
        EnvVars env = build.getEnvironment(listener);

        PhingInstallation pi = getPhing(env, listener);
        
        // PHP Command
        if (pi != null) {
            String phpCommand = pi.getPhpCommand();
            if (phpCommand != null) {
                env.put("PHP_COMMAND", phpCommand);
            }
        }
        
        // Phing Command
        if (pi == null) {
            args.add(PhingInstallation.getExecName(launcher));
        } else {
            args.add(pi.getExecutable(launcher));
        }

        VariableResolver<String> vr = build.getBuildVariableResolver();

        String script = (buildFile == null) ? "build.xml" : buildFile;
        FilePath buildScript = lookingForBuildScript(build, env.expand(script), listener);
        if (buildScript == null) {
            listener.getLogger().println(Messages.Phing_NotFoundABuildScript(script));
            return false;
        }

        args.add("-buildfile", buildScript.getRemote());

        // Targets
        String expandedTargets = Util.replaceMacro(env.expand(targets), vr);
        if (expandedTargets != null) {
            args.addTokenized(expandedTargets.replaceAll("[\t\r\n]+", " "));
        }

        Set<String> sensitiveVars = build.getSensitiveBuildVariables();
        args.addKeyValuePairs("-D", build.getBuildVariables(), sensitiveVars);
        args.addKeyValuePairsFromPropertyString("-D", env.expand(properties), vr, sensitiveVars);

        String expandedOptions = Util.replaceMacro(env.expand(options), vr);
        if (expandedOptions == null || !expandedOptions.contains("-logger ")) {
            // avoid printing esc sequence
            args.add("-logger", "phing.listener.DefaultLogger");
        }
        if (expandedOptions != null) {
            args.addTokenized(expandedOptions.replaceAll("[\t\r\n]", " "));
        }

        // Environment variables
        if (pi != null && pi.getHome() != null) {
            env.put("PHING_HOME", pi.getHome());
            env.put("PHING_CLASSPATH", pi.getHome() + File.separator + "classes");
        }

        if (!launcher.isUnix()) {
            args = args.toWindowsCommand();
        }

        // Working Directory
        // since 0.9
        FilePath working = useModuleRoot ? build.getModuleRoot() : buildScript.getParent();
        listener.getLogger().println(Messages.Phing_WorkingDirectory(working));

        final long startTime = System.currentTimeMillis();
        try {
            PhingConsoleAnnotator pca = new PhingConsoleAnnotator(listener.getLogger(), build.getCharset());
            int result;
            try {
                result = launcher.launch().cmds(args).envs(env).stdout(pca).pwd(working).join();
            } finally {
                pca.forceEol();
            }
            return result == 0;
        } catch (final IOException e) {
            Util.displayIOException(e, listener);
            final long processingTime = System.currentTimeMillis() - startTime;
            final String errorMessage = buildErrorMessage(pi, processingTime);
            e.printStackTrace(listener.fatalError(errorMessage));
            return false;
        }
    }

    private FilePath lookingForBuildScript(AbstractBuild<?, ?> build, String script, BuildListener listener)
            throws IOException, InterruptedException {

        PrintStream logger = listener.getLogger();

        FilePath buildScriptPath = build.getModuleRoot().child(script);
        logger.println("looking for '" + buildScriptPath.getRemote() + "' ... ");
        if (buildScriptPath.exists()) {
            return buildScriptPath;
        }

        FilePath workspace = build.getWorkspace();
        if (workspace != null) {
            buildScriptPath = workspace.child(script);
            logger.println("looking for '" + buildScriptPath.getRemote() + "' ... ");
            if (buildScriptPath.exists()) {
                return buildScriptPath;
            }
        }

        buildScriptPath = new FilePath(new File(script));
        logger.println("looking for '" + buildScriptPath.getRemote() + "' ... ");
        if (buildScriptPath.exists()) {
            return buildScriptPath;
        }

        // build script not Found
        return null;
    }

    private String buildErrorMessage(final PhingInstallation pi, final long processingTime) {
        final StringBuffer msg = new StringBuffer();
        msg.append(Messages.Phing_ExecFailed());
        if (pi == null && processingTime < 1000) {
            PhingInstallation[] installations = getPhingInstallationDescriptor().getInstallations();
            if (installations.length == 0) {
                msg.append(Messages.Phing_GlocalConfigNeeded());
            } else {
                msg.append(Messages.Phing_ProjectConfigNeeded());
            }
        }
        return msg.toString();
    }
}
