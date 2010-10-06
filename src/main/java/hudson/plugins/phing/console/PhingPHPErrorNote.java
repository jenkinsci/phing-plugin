package hudson.plugins.phing.console;

import hudson.Extension;
import hudson.MarkupText;
import hudson.console.ConsoleAnnotationDescriptor;
import hudson.console.ConsoleAnnotator;
import hudson.console.ConsoleNote;

/**
 *
 * @author Seiji Sogabe
 */
public class PhingPHPErrorNote extends ConsoleNote {


    public PhingPHPErrorNote() {
        //
    }

    @Override
    public ConsoleAnnotator<?> annotate(Object context, MarkupText text, int charPos) {
        if (!ENABLED) {
            return null;
        }

        if (text.getText().contains("Notice")) {
            text.addMarkup(0, text.length(), "<span class='phing-phperror-notice'>", "</span>");
            return null;
        }

        if (text.getText().contains("Warning error")) {
            text.addMarkup(0, text.length(), "<span class='phing-phperror-warning'>", "</span>");
            return null;
        }

        if (text.getText().contains("Parse error")) {
            text.addMarkup(0, text.length(), "<span class='phing-phperror-parse'>", "</span>");
            return null;
        }

        if (text.getText().contains("Fatal error")) {
            text.addMarkup(0, text.length(), "<span class='phing-phperror-fatal'>", "</span>");
            return null;
        }

        return null;
    }

    @Extension
    public static final class DescriptorImpl extends ConsoleAnnotationDescriptor {

        public String getDisplayName() {
            return "PHP Error Note";
        }
    }

    public static boolean ENABLED = !Boolean.getBoolean(PhingPHPErrorNote.class.getName() + ".disabled");
}
