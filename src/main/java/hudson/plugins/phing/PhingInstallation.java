/*
 * The MIT License
 * 
 * Copyright (c) 2008-2011, Jenkins project, Seiji Sogabe
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
import hudson.Launcher;
import hudson.Util;
import hudson.model.EnvironmentSpecific;
import hudson.model.Node;
import hudson.model.TaskListener;
import hudson.remoting.Callable;
import hudson.slaves.NodeSpecific;
import hudson.tools.ToolDescriptor;
import hudson.tools.ToolInstallation;
import hudson.tools.ToolProperty;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import jenkins.model.Jenkins;

import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Phing Installation.
 *
 * @author Seiji Sogabe
 */
public final class PhingInstallation extends ToolInstallation 
    implements EnvironmentSpecific<PhingInstallation>, NodeSpecific<PhingInstallation>, Serializable {

    private static final String PHING_EXEC_NAME_FOR_UNIX = "phing";

    private static final String PHING_EXEC_NAME_FOR_WINDOWS = "phing.bat";

    private static final long serialVersionUID = 1L;

    /**
     * Phing Installation name.
     */
    private final String name;

    /**
     * PHING_HOME where phing has been installed.
     */
    private final String phingHome;

    /**
     * PHP command.
     */
    private final String phpCommand;

    public static String getExecName(final Launcher launcher) {
        String execName;
        if (launcher.isUnix()) {
            execName = PHING_EXEC_NAME_FOR_UNIX;
        } else {
            execName = PHING_EXEC_NAME_FOR_WINDOWS;
        }
        return execName;
    }

    @Override
    public String getHome() {
        return phingHome;
    }

    @Deprecated
    public String getPhingHome() {
        return phingHome;
    }

    public String getPhpCommand() {
        return phpCommand;
    }

    @DataBoundConstructor
    public PhingInstallation(final String phpCommand, final String name, final String phingHome, List<? extends ToolProperty<?>> properties) {
        super(name, phingHome);
        this.name = Util.fixEmptyAndTrim(name);
        this.phingHome = Util.fixEmptyAndTrim(phingHome);
        this.phpCommand = Util.fixEmptyAndTrim(phpCommand);
    }

    public String getExecutable(final Launcher launcher) throws IOException, InterruptedException {
        final String execName = getExecName(launcher);
        return launcher.getChannel().call(new Callable<String, IOException>() {
            private static final long serialVersionUID = 1L;

            public String call() throws IOException {
                final File exe = new File(new File(getPhingHome(), "bin"), execName);
                if (exe.exists()) {
                    return exe.getPath();
                }
                return null;
            }
        });
    }

    @Override
    public PhingInstallation forEnvironment(EnvVars ev) {
        return new PhingInstallation(null, getName(), ev.expand(phingHome), getProperties().toList());
    }

    public PhingInstallation forNode(Node node, TaskListener log) throws IOException, InterruptedException {
          return new PhingInstallation(null, getName(), translateFor(node, log), getProperties().toList());
    }

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) Jenkins.getInstance().getDescriptor(PhingInstallation.class);
    }
 
    @Extension
    public static class DescriptorImpl extends ToolDescriptor<PhingInstallation> {

        public DescriptorImpl() {
            super();
            load();
        }

        @Override
        public String getDisplayName() {
            return "Phing";
        }
        
        
        
    }
}
