package utils.facade;

import utils.constants.FeatureNames;
import utils.constants.PageNames;
import utils.constants.Strings;
import utils.structures.User;

public class Upgrades extends Page{

    public Upgrades() {
        addLegalPageSwitches();
        addLegalFeatures();
    }

    @Override
    protected void addLegalPageSwitches() {
        legalPageSwitches.add(PageNames.MOVIES);
        legalPageSwitches.add(PageNames.SEE_DETAILS);
        legalPageSwitches.add(PageNames.LOGOUT);
    }

    @Override
    protected void addLegalFeatures() {
        legalFeatures.add(FeatureNames.BUY_TOKENS);
        legalFeatures.add(FeatureNames.BUY_PREMIUM_ACCOUNT);
    }

    public void buyTokens(final int tokens, final User user) {
        user.getCredentials().subtractBalance(tokens);
        user.getCredentials().addTokens(tokens);
    }

    public void buyPremium(final User user) {
        user.getCredentials().subtractTokens(10);
        user.getCredentials().setAccountType(Strings.PREMIUM);
    }
}
