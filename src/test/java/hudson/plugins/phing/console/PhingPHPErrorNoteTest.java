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

import hudson.MarkupText;
import hudson.console.ConsoleAnnotator;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for PhingPHPErrorNote.
 * @author Seiji Sogabe
 */
public class PhingPHPErrorNoteTest {

    private PhingPHPErrorNote target;
    
    @Before
    public void setUp() {
        target = new PhingPHPErrorNote();
    }

    /**
     * Test of annotate method, of class PhingPHPErrorNote.
     */
    @Test
    public void testAnnotate_Notice() {
        Object context = new Object();
        MarkupText text = new MarkupText("Notice: notice");

        ConsoleAnnotator<?> ca = target.annotate(context, text, 0);

        Assert.assertNull(ca);
        Assert.assertEquals("<span class='phing-phperror-notice'>Notice: notice</span>", text.toString(true));
    }

    /**
     * Test of annotate method, of class PhingPHPErrorNote.
     */
    @Test
    public void testAnnotate_Warning() {
        Object context = new Object();
        MarkupText text = new MarkupText("Warning error: warning");

        ConsoleAnnotator<?> ca = target.annotate(context, text, 0);

        Assert.assertNull(ca);
        Assert.assertEquals("<span class='phing-phperror-warning'>Warning error: warning</span>", text.toString(true));
    }

    /**
     * Test of annotate method, of class PhingPHPErrorNote.
     */
    @Test
    public void testAnnotate_Parse() {
        Object context = new Object();
        MarkupText text = new MarkupText("Parse error: parse");

        ConsoleAnnotator<?> ca = target.annotate(context, text, 0);

        Assert.assertNull(ca);
        Assert.assertEquals("<span class='phing-phperror-parse'>Parse error: parse</span>", text.toString(true));
    }

    /**
     * Test of annotate method, of class PhingPHPErrorNote.
     */
    @Test
    public void testAnnotate_Fatal() {
        Object context = new Object();
        MarkupText text = new MarkupText("Fatal error: fatal");

        ConsoleAnnotator<?> ca = target.annotate(context, text, 0);

        Assert.assertNull(ca);
        Assert.assertEquals("<span class='phing-phperror-fatal'>Fatal error: fatal</span>", text.toString(true));
    }

    /**
     * Test of annotate method, of class PhingPHPErrorNote.
     */
    @Test
    public void testAnnotate_NoMessage() {
        Object context = new Object();
        MarkupText text = new MarkupText("PHP Error");

        ConsoleAnnotator<?> ca = target.annotate(context, text, 0);

        Assert.assertNull(ca);
        Assert.assertEquals("PHP Error", text.toString(true));
    }

}