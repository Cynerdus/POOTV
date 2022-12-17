package utils.facade;

import utils.Database;
import utils.constants.FeatureNames;
import utils.constants.PageNames;
import utils.structures.Credentials;

public class Login extends Page {

    public Login() {
        addLegalFeatures();
        addLegalPageSwitches();
    }

    /**
     *      available features for Login page
     */
    @Override
    protected void addLegalFeatures() {
        legalFeatures.add(FeatureNames.LOGIN);
    }

    /**
     *      available page switches for Login page
     */
    @Override
    protected void addLegalPageSwitches() {
        legalPageSwitches.add(PageNames.REGISTER);
    }

    /**
     *
     * @param database          database for movies and users
     * @param credentials       user's credentials
     * @return                  true if the user can be found within the database
     *                          false otherwise
     */
    public boolean checkCredentials(final Database database, final Credentials credentials) {
        return database.isUserWithCredentialsRegistered(credentials);
    }
}
