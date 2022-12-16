package utils.facade;

import utils.constants.PageNames;

public class Unauthenticated extends Page{

    public Unauthenticated() {
        addLegalPageSwitches();
    }

    @Override
    protected void addLegalPageSwitches() {
        legalPageSwitches.add(PageNames.LOGIN);
        legalPageSwitches.add(PageNames.REGISTER);
    }
}
