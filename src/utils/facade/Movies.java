package utils.facade;

import utils.constants.FeatureNames;
import utils.constants.PageNames;

public class Movies extends Page {

    public Movies() {
        addLegalFeatures();
        addLegalPageSwitches();
    }

    /**
     *      available page switches for Movies page
     */
    @Override
    protected void addLegalPageSwitches() {
        legalPageSwitches.add(PageNames.LOGOUT);
        legalPageSwitches.add(PageNames.SEE_DETAILS);
        legalPageSwitches.add(PageNames.UPGRADES);
    }

    /**
     *      available features for Movies page
     */
    @Override
    protected void addLegalFeatures() {
        legalFeatures.add(FeatureNames.SEARCH);
        legalFeatures.add(FeatureNames.FILTER);
    }
}
