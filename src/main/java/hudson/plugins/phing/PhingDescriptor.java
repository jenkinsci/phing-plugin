package hudson.plugins.phing;

import hudson.CopyOnWrite;
import hudson.model.Descriptor;
import hudson.model.Hudson;
import hudson.tasks.Builder;

import hudson.util.FormValidation;
import java.io.File;
import java.util.List;
import java.util.Map;


import net.sf.json.JSONObject;

import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

/**
 * Descriptor for Phing.
 * 
 * @author Seiji Sogabe
 */
public final class PhingDescriptor extends Descriptor<Builder> {

    @CopyOnWrite
    private volatile PhingInstallation[] installations =
            new PhingInstallation[0];

    public PhingDescriptor() {
        super(PhingBuilder.class);
        load();
    }

    @Override
    public boolean configure(final StaplerRequest req, final JSONObject formData) throws FormException {
        final List<PhingInstallation> list = req.bindJSONToList(PhingInstallation.class,
                formData.get("phing"));
        installations = list.toArray(new PhingInstallation[list.size()]);
        save();
        return true;
    }

    @Override
    public void convert(final Map<String, Object> oldPropertyBag) {
        if (oldPropertyBag.containsKey("installations")) {
            installations = (PhingInstallation[]) oldPropertyBag.get("installations");
        }
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
        return installations;
    }

    @Override
    public PhingBuilder newInstance(final StaplerRequest req, final JSONObject formData) throws FormException {
        return req.bindJSON(PhingBuilder.class, formData);
    }

    public FormValidation doCheckPhingHome(@QueryParameter File value) {
        if (!Hudson.getInstance().hasPermission(Hudson.ADMINISTER)) {
            return FormValidation.ok();
        }

        if ("".equals(value.getPath().trim())) {
            return FormValidation.error(Messages.Phing_PhingHomeRequired());
        }

        if (!value.isDirectory()) {
            return FormValidation.error(Messages.Phing_NotAPHPCommand(value));
        }

        final File phing = new File(value, "bin" + File.separator + "phing.php");
        if (!phing.exists()) {
            return FormValidation.error(Messages.Phing_NotAPhingDirectory(value));
        }

        return FormValidation.ok();
    }

    public FormValidation doCheckPhpCommand(@QueryParameter File value) {
        if (!Hudson.getInstance().hasPermission(Hudson.ADMINISTER)) {
            return FormValidation.ok();
        }
        
        if ("".equals(value.getPath().trim())) {
            return FormValidation.ok();
        }

        if (!value.exists()) {
            return FormValidation.error(Messages.Phing_NotAPHPCommand(value));
        }

        if (value.isDirectory()) {
            return FormValidation.error(Messages.Phing_DirectoryNotAllowed(value));
        }

        return FormValidation.ok();
    }
}
