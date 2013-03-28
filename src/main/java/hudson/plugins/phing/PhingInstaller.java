package hudson.plugins.phing;

import hudson.Extension;
import hudson.tools.DownloadFromUrlInstaller;
import hudson.tools.ToolInstallation;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * <p>[概 要] PhingInstallerクラス。</p>
 * <p>[詳 細] </p>
 * <p>[備 考] </p>
 * <p>[環 境] JavaSE 6.0, Camel 2.7.2</p>
 * <p>Copyright (c) NTT COMWARE 2012</p>
 * @author NTT COMWARE Seiji Sogabe
 */
public class PhingInstaller extends DownloadFromUrlInstaller {

    @DataBoundConstructor
    public PhingInstaller(String id) {
        super(id);
    }

    public static class DescriptionImpl extends DownloadFromUrlInstaller.DescriptorImpl<PhingInstaller> {

        @Override
        public String getDisplayName() {
            return "Install from www.phing.info";
        }

        @Override
        public boolean isApplicable(Class<? extends ToolInstallation> toolType) {
            return toolType == PhingInstallation.class;
        }
        
        
    }
    
}
