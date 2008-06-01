package hudson.plugins.phing;

import hudson.Launcher;
import hudson.Util;
import hudson.remoting.Callable;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Phing Installation.
 *
 * @author Seiji Sogabe
 */
public final class PhingInstallation implements Serializable {

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

    public String getName() {
        return name;
    }

    public String getPhingHome() {
        return phingHome;
    }

    public String getPhpCommand() {
        return phpCommand;
    }

    @DataBoundConstructor
    public PhingInstallation(final String phpCommand, final String name, final String phingHome) {
        this.name = Util.fixEmptyAndTrim(name);
        this.phingHome = Util.fixEmptyAndTrim(phingHome);
        this.phpCommand = Util.fixEmptyAndTrim(phpCommand);
    }

    public String getExecutable(final Launcher launcher) throws IOException,
            InterruptedException {
        return launcher.getChannel().call(new Callable<String, IOException>() {
            private static final long serialVersionUID = 1L;

            public String call() throws IOException {
                final File exe = getExeFile(launcher);
                if (exe.exists()) {
                    return exe.getPath();
                }
                return null;
            }
        });
    }

    private File getExeFile(final Launcher launcher) {
        return new File(getPhingHome(), "bin" + File.separator + getExecName(launcher));
    }

}
