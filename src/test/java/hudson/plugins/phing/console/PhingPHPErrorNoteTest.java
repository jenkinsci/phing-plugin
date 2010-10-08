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
 * Test class for PhingPHPErrorNote.
 * @author Seiji Sogabe
 */
public class PhingPHPErrorNoteTest {

    private PhingPHPErrorNote target;
    
    public PhingPHPErrorNoteTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        target = new PhingPHPErrorNote();
    }

    @After
    public void tearDown() {
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