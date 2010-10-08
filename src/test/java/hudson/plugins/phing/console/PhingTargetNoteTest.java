package hudson.plugins.phing.console;

import hudson.MarkupText;
import hudson.console.ConsoleAnnotator;
import java.io.IOException;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test class for PhingTargetNote
 * 
 * @author Seiji Sogabe
 */
public class PhingTargetNoteTest {

    private PhingTargetNote target;

    public PhingTargetNoteTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        target = new PhingTargetNote();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of annotate method, of class PhingTargetNote.
     */
    @Test
    public void testAnnotate() throws IOException {
        Object context = new Object();
        MarkupText text = new MarkupText("phing > build:");

        ConsoleAnnotator<?> ca = target.annotate(context, text, 0);

        Assert.assertNull(ca);
        Assert.assertEquals("<span class='phing-target'>phing > build</span>:", text.toString(true));
    }

    /**
     * Test of annotate method, of class PhingTargetNote.
     */
    @Test
    public void testAnnotateNoTarget() throws IOException {
        Object context = new Object();
        MarkupText text = new MarkupText("phing ");

        ConsoleAnnotator<?> ca = target.annotate(context, text, 0);

        Assert.assertNull(ca);
        Assert.assertEquals("phing ", text.toString(true));
    }

}