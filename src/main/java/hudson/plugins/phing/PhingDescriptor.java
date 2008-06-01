package hudson.plugins.phing;

import hudson.CopyOnWrite;
import hudson.StructuredForm;
import hudson.model.Descriptor;
import hudson.tasks.Builder;
import hudson.util.FormFieldValidator;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

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
    public boolean configure(final StaplerRequest req) throws FormException {
        final List<PhingInstallation> list =
                req.bindJSONToList(PhingInstallation.class, StructuredForm.get(
                        req).get("phing"));
        installations = list.toArray(new PhingInstallation[list.size()]);
        save();
        return true;
    }

    @Override
    public void convert(final Map<String, Object> oldPropertyBag) {
        if (oldPropertyBag.containsKey("installations")) {
            installations =
                    (PhingInstallation[]) oldPropertyBag.get("installations");
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
    public PhingBuilder newInstance(final StaplerRequest req,
            final JSONObject formData) throws FormException {
        return req.bindJSON(PhingBuilder.class, formData);
    }

    public void doCheckPhingHome(final StaplerRequest req,
            final StaplerResponse res) throws IOException, ServletException {
        new FormFieldValidator(req, res, true) {
            @Override
            protected void check() throws IOException, ServletException {
                final File f = getFileParameter("value");
                if ("".equals(f.getPath().trim())) {
                    error(Messages.Phing_PhingHomeRequired());
                    return;
                }
                if (!f.isDirectory()) {
                    error(Messages.Phing_NotAPHPCommand(f));
                    return;
                }
                final File phing =
                        new File(f, "bin" + File.separator + "phing.php");
                if (!phing.exists()) {
                    error(Messages.Phing_NotAPhingDirectory(f));
                    return;
                }
                ok();
            }
        }.process();
    }

    public void doCheckPhpCommand(final StaplerRequest req,
            final StaplerResponse res) throws IOException, ServletException {
        new FormFieldValidator(req, res, true) {
            @Override
            protected void check() throws IOException, ServletException {
                final File f = getFileParameter("value");
                if ("".equals(f.getPath().trim())) {
                    ok();
                    return;
                }
                if (!f.exists()) {
                    error(Messages.Phing_NotAPHPCommand(f));
                    return;
                }
                ok();
            }
        }.process();
    }

}
