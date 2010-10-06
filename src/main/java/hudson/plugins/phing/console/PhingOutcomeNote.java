package hudson.plugins.phing.console;

import hudson.Extension;
import hudson.MarkupText;
import hudson.console.ConsoleAnnotationDescriptor;
import hudson.console.ConsoleAnnotator;
import hudson.console.ConsoleNote;

/**
 *
 * @author SAeiji Sogabe
 */
public class PhingOutcomeNote extends ConsoleNote {

    @Override
    public ConsoleAnnotator<?> annotate(Object context, MarkupText text, int charpos) {
        if(!ENABLED) {
            return null;
        }
        
        if (text.getText().contains("FAILED")) {
            text.addMarkup(0, text.length(), "<span class='phing-outcome-failed'>", "</span>");
            return null;
        }
        if (text.getText().contains("FINISHED")) {
            text.addMarkup(0, text.length(), "<span class='phing-outcome-finished'>", "</span>");
        }
        return null;
    }

    @Extension
    public static final class DescriptorImpl extends ConsoleAnnotationDescriptor {

        public String getDisplayName() {
            return "PHP Outcome Note";
        }
    }

    private static final boolean ENABLED = !Boolean.getBoolean(PhingOutcomeNote.class.getName() + ".disabled");
}
