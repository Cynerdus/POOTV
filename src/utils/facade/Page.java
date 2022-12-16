package utils.facade;

import java.util.ArrayList;
import java.util.List;

public abstract class Page{

    private Boolean active = false;

    protected List<String> legalFeatures = new ArrayList<>();
    protected List<String> legalPageSwitches = new ArrayList<>();

    public void activeSwitch() {
        active = !active;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isFeatureLegal(final String feature) {
        return legalFeatures.contains(feature);
    }

    public boolean isPageSwitchLegal(String pageSwitch) {
        return legalPageSwitches.contains(pageSwitch);
    }

    protected void addLegalFeatures() { }
    protected void addLegalPageSwitches() { }
}
