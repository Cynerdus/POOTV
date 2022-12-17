package utils.facade;

import utils.constants.PageNames;

public class Logout extends Page {

    public Logout() {
        addLegalPageSwitches();
    }

    /**
     *      available page switched for Logout page
     */
    @Override
    protected void addLegalPageSwitches() {
        legalPageSwitches.add(PageNames.LOGIN);
        legalPageSwitches.add(PageNames.REGISTER);
    }
}
