package utils.facade;

import java.util.ArrayList;
import java.util.List;

public abstract class Page {

    private Boolean active = false;

    protected List<String> legalFeatures = new ArrayList<>();
    protected List<String> legalPageSwitches = new ArrayList<>();

    /**
     *      switches on and off the active label
     */
    public void activeSwitch() {
        active = !active;
    }

    /**
     *
     * @return      true if the page is active at the moment
     *              false otherwise
     */
    public boolean isActive() {
        return active;
    }

    /**
     *
     * @param feature           feature to be checked
     * @return                  true if the page can process the feature
     *                          false otherwise
     */
    public boolean isFeatureLegal(final String feature) {
        return legalFeatures.contains(feature);
    }

    /**
     *
     * @param pageSwitch        page to be checked
     * @return                  true if it is possible to switch to paceSwitch
     *                          false otherwise
     */
    public boolean isPageSwitchLegal(final String pageSwitch) {
        return legalPageSwitches.contains(pageSwitch);
    }

    protected void addLegalFeatures() { }
    protected void addLegalPageSwitches() { }
}
