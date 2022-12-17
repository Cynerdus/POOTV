package utils.facade;

import utils.Database;
import utils.constants.FeatureNames;
import utils.constants.PageNames;
import utils.structures.Credentials;
import utils.structures.User;

public class Register extends Page {

    public Register() {
        addLegalFeatures();
        addLegalPageSwitches();
    }

    /**
     *      available features for Register page
     */
    @Override
    protected void addLegalFeatures() {
        legalFeatures.add(FeatureNames.REGISTER);
    }

    /**
     *      available pages to switch to for Register page
     */
    @Override
    protected void addLegalPageSwitches() {
        legalPageSwitches.add(PageNames.LOGIN);
    }

    /**
     *
     * @param database      database for movies and users
     * @param credentials   user credentials
     * @return              true if user can be found within database
     *                      false otherwise
     */
    public boolean checkCredentials(final Database database, final Credentials credentials) {
        return database.isUserWithNameRegistered(credentials.getName());
    }

    /**
     *
     * @param database          database for movies and users
     * @param credentials       new user credentials
     */
    public void addUserToDatabase(final Database database, final Credentials credentials) {
        User user = new User();
        user.setCredentials(credentials);
        database.addUser(user);
    }
}
