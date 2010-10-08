package hudson.plugins.phing.console;

import hudson.MarkupText;
import hudson.console.ConsoleAnnotator;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test class for PhingOutcomeNoteTest.
 *
 * @author Seiji Sogabe
 */
public class PhingOutcomeNoteTest {

    private PhingOutcomeNote target;
    
    public PhingOutcomeNoteTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        target = new PhingOutcomeNote();
    }

    @After
    public void tearDown() {
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