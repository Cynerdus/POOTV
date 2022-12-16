package utils.facade;

import utils.Printer;
import utils.constants.FeatureNames;
import utils.constants.PageNames;
import utils.structures.Movie;

public class Movies extends Page{

    public Movies() {
        addLegalFeatures();
        addLegalPageSwitches();
    }

    @Override
    protected void addLegalPageSwitches() {
        legalPageSwitches.add(PageNames.LOGOUT);
        legalPageSwitches.add(PageNames.SEE_DETAILS);
        legalPageSwitches.add(PageNames.UPGRADES);
    }

    @Override
    protected void addLegalFeatures() {
        legalFeatures.add(FeatureNames.SEARCH);
        legalFeatures.add(FeatureNames.FILTER);
    }
}
