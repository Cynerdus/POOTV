package utils.facade;

import utils.constants.PageNames;

public class Authenticated extends Page{

    public Authenticated() {
        addLegalPageSwitches();
    }

    @Override
    protected void addLegalPageSwitches() {
        legalPageSwitches.add(PageNames.MOVIES);
        legalPageSwitches.add(PageNames.UPGRADES);
        legalPageSwitches.add(PageNames.LOGOUT);
    }
}
