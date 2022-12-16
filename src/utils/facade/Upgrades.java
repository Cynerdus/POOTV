package utils.facade;

import utils.constants.PageNames;

public class Upgrades extends Page{

    @Override
    protected void addLegalPageSwitches() {
        legalPageSwitches.add(PageNames.MOVIES);
        legalPageSwitches.add(PageNames.SEE_DETAILS);
        legalPageSwitches.add(PageNames.LOGOUT);
    }
}
