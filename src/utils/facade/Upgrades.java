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

    public boolean buyTokens(final int tokens, final User user) {
        if (user.getCredentials().getBalance() >= tokens) {
            user.getCredentials().subtractBalance(tokens);
            user.getCredentials().addTokens(tokens);
            return true;
        }

        return false;
    }

    public boolean buyPremium(final User user) {
        if (user.getCredentials().getBalance() >= 10) {
            user.getCredentials().subtractTokens(10);
            user.getCredentials().setAccountType(Strings.PREMIUM);
            return true;
        }

        return false;
    }
}
