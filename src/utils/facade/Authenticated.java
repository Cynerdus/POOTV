package utils.facade;

import utils.constants.PageNames;

public class Authenticated extends Page {

    public Authenticated() {
        addLegalPageSwitches();
    }

    /**
     *      available page switches for Authenticated page
     */
    @Override
    protected void addLegalPageSwitches() {
        legalPageSwitches.add(PageNames.MOVIES);
        legalPageSwitches.add(PageNames.UPGRADES);
        legalPageSwitches.add(PageNames.LOGOUT);
    }
}
