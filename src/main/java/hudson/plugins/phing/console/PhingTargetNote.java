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
package hudson.plugins.phing.console;

import hudson.Extension;
import hudson.MarkupText;
import hudson.console.ConsoleAnnotationDescriptor;
import hudson.console.ConsoleAnnotator;
import hudson.console.ConsoleNote;
import java.util.regex.Pattern;

/**
 * Marks the log line "TARGET:" that Phing uses to mark the beginning of the new target.
 *
 * @author Seiji Sogabe
 */
public class PhingTargetNote extends ConsoleNote {

    private static final Pattern PATTERN = Pattern.compile("(?<=\\>\\s).*(?=:)");
    
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
