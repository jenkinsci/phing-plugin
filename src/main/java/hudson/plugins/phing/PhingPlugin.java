package hudson.plugins.phing;

import hudson.Plugin;
import hudson.tasks.BuildStep;

/**
 * Plugin for Phing, whcih is a project build system for PHP.
 *
 * @author Seiji Sogabe
 * @plugin
 */
public final class PhingPlugin extends Plugin {

    @Override
    public void start() throws Exception {
        BuildStep.BUILDERS.add(PhingBuilder.DESCRIPTOR);
    }

}
