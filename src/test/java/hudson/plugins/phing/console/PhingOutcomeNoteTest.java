package hudson.plugins.phing.console;

import hudson.MarkupText;
import hudson.console.ConsoleAnnotator;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for PhingOutcomeNoteTest.
 *
 * @author Seiji Sogabe
 */
public class PhingOutcomeNoteTest {

    private PhingOutcomeNote target;
    
    @Before
    public void setUp() {
        target = new PhingOutcomeNote();
    }

    /**
     * Test of annotate method, of class PhingOutcomeNote.
     */
    @Test
    public void testAnnotate_FAILED() {
        Object context = new Object();
        MarkupText text = new MarkupText("BUILD FAILED");

        ConsoleAnnotator<?> ca = target.annotate(context, text, 0);

        Assert.assertNull(ca);
        Assert.assertEquals("<span class='phing-outcome-failed'>BUILD FAILED</span>", text.toString(true));
    }

    /**
     * Test of annotate method, of class PhingOutcomeNote.
     */
    @Test
    public void testAnnotate_FINISHED() {
        Object context = new Object();
        MarkupText text = new MarkupText("BUILD FINISHED");

        ConsoleAnnotator<?> ca = target.annotate(context, text, 0);

        Assert.assertNull(ca);
        Assert.assertEquals("<span class='phing-outcome-finished'>BUILD FINISHED</span>", text.toString(true));
    }

    /**
     * Test of annotate method, of class PhingOutcomeNote.
     */
    @Test
    public void testAnnotate_NOMESSAGE() {
        Object context = new Object();
        MarkupText text = new MarkupText("NO MESSAGE FOUND");

        ConsoleAnnotator<?> ca = target.annotate(context, text, 0);

        Assert.assertNull(ca);
        Assert.assertEquals("NO MESSAGE FOUND", text.toString(true));
    }


}