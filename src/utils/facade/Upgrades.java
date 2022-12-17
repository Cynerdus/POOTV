package utils.facade;

import utils.constants.FeatureNames;
import utils.constants.Numbers;
import utils.constants.PageNames;
import utils.constants.Strings;
import utils.structures.User;

public class Upgrades extends Page {

    public Upgrades() {
        addLegalPageSwitches();
        addLegalFeatures();
    }

    /**
     *      available pages to switch to for Upgrade page
     */
    @Override
    protected void addLegalPageSwitches() {
        legalPageSwitches.add(PageNames.MOVIES);
        legalPageSwitches.add(PageNames.SEE_DETAILS);
        legalPageSwitches.add(PageNames.LOGOUT);
    }

    /**
     *      available features for Upgrade page
     */
    @Override
    protected void addLegalFeatures() {
        legalFeatures.add(FeatureNames.BUY_TOKENS);
        legalFeatures.add(FeatureNames.BUY_PREMIUM_ACCOUNT);
    }

    /**
     *
     * @param tokens        tokens to be bought
     * @param user          user to buy
     * @return              true if the transaction was successful
     *                      false otherwise
     */
    public boolean buyTokens(final int tokens, final User user) {
        if (user.getCredentials().getBalance() >= tokens) {
            user.getCredentials().subtractBalance(tokens);
            user.getCredentials().addTokens(tokens);
            return true;
        }

        return false;
    }

    /**
     *
     * @param user      user to buy premium
     * @return          true if transaction was successful
     *                  false otherwise
     */
    public boolean buyPremium(final User user) {
        if (user.getCredentials().getBalance() >= Numbers.TEN) {
            user.getCredentials().subtractTokens(Numbers.TEN);
            user.getCredentials().setAccountType(Strings.PREMIUM);
            return true;
        }

        return false;
    }
}
