package hudson.plugins.phing.console;

import hudson.Extension;
import hudson.MarkupText;
import hudson.console.ConsoleAnnotationDescriptor;
import hudson.console.ConsoleAnnotator;
import hudson.console.ConsoleNote;
import java.util.regex.Pattern;

/**
 *   Marks the log line "TARGET:" that Phing uses to mark the beginning of the new target.
 *
 * @author Seiji Sogabe
 */
public class PhingTargetNote extends ConsoleNote {


    private static final Pattern PATTERN = Pattern.compile(".*(?=:)");
    
    @Override
    public ConsoleAnnotator<?> annotate(Object context, MarkupText text, int charPos) {
        if (!ENABLED) {
            return null;
        }

        MarkupText.SubText t = text.findToken(PATTERN);
        if (t == null) {
            return null;
        }
        t.addMarkup(0, t.length(), "<span class='phing-target'>", "</span>");
        return null;
    }

    @Extension
    public static final class DescriptorImpl extends ConsoleAnnotationDescriptor {
        public String getDisplayName() {
            return "Phing Target Note";
        }
    }

    private static final boolean ENABLED = !Boolean.getBoolean(PhingTargetNote.class.getName() + ".disabled");
}
