/*
 * The MIT License
 *
 * Copyright (c) 2011, Seiji Sogabe
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
package hudson.plugins.phing;

import org.htmlunit.html.HtmlButton;
import org.htmlunit.html.HtmlCheckBoxInput;
import org.htmlunit.html.HtmlForm;
import hudson.model.Descriptor;
import hudson.model.FreeStyleProject;
import hudson.tasks.Builder;
import hudson.util.DescribableList;
import static org.codehaus.groovy.runtime.DefaultGroovyMethods.last;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.JenkinsRule.WebClient;

/**
 * Job Config submit Test
 *
 * @author Seiji Sogabe
 */
public class PhingBuilderConfigSubmitTest {

    @Rule
    public JenkinsRule j = new JenkinsRule();
    
    private WebClient webClient;

    @Before
    public void setUp() throws Exception {
        webClient = j.createWebClient();
        webClient.setCssEnabled(false);
        webClient.setThrowExceptionOnFailingStatusCode(false);
    }

    @Test
    public void testConfigsubmit() throws Exception {
        FreeStyleProject p = j.createFreeStyleProject();
        PhingBuilder builder = new PhingBuilder("Default", "build.xml", "install", null, true, null);
        p.getBuildersList().add(builder);

        HtmlForm form = webClient.goTo(p.getUrl() + "/configure").getFormByName("config");
        HtmlCheckBoxInput useModuleRootCheckBox = form.getInputByName("_.useModuleRoot");

        assertThat(useModuleRootCheckBox.isChecked(), is(true));

        useModuleRootCheckBox.setChecked(false);
        form.submit((HtmlButton)last(form.getHtmlElementsByTagName("button")));

        DescribableList<Builder, Descriptor<Builder>> builders = p.getBuildersList();
        PhingBuilder b = builders.get(PhingBuilder.class);
        
        assertThat(b.isUseModuleRoot(), is(false));
    }
}
