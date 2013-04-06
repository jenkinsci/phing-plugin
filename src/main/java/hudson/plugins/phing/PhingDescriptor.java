/*
 * The MIT License
 * 
 * Copyright (c) 2008-2013, Jenkins project, Seiji Sogabe
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

import hudson.CopyOnWrite;
import hudson.model.Descriptor;
import hudson.model.Descriptor.FormException;
import hudson.tasks.Builder;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.StaplerRequest;

/**
 * Descriptor for Phing.
 * 
 * @author Seiji Sogabe
 */
public final class PhingDescriptor extends Descriptor<Builder> {

    @CopyOnWrite
    @Deprecated
    private volatile PhingInstallation[] installations = new PhingInstallation[0];

    @Deprecated
    PhingInstallation[] getOldInstallations() {
        return installations;
    }
    
    @Deprecated
    void clearOldInstallationsAndSave() {
        installations = null;
        save();
    }
    
    public PhingDescriptor() {
        super(PhingBuilder.class);
        load();
    }

    @Override
    public String getHelpFile() {
        return "/plugin/phing/help.html";
    }

    @Override
    public String getDisplayName() {
        return Messages.Phing_DisplayName();
    }

    public PhingInstallation[] getInstallations() {
        return PhingInstallation.getInstallations();
    }

    @Override
    public PhingBuilder newInstance(final StaplerRequest req, final JSONObject formData) throws FormException {
        return req.bindJSON(PhingBuilder.class, formData);
    }

}
