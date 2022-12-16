package utils.facade;

import utils.Database;
import utils.constants.FeatureNames;
import utils.constants.PageNames;
import utils.structures.Credentials;
import utils.structures.User;

public class Register extends Page{

    private Credentials credentials;

    public Register() {
        addLegalFeatures();
        addLegalPageSwitches();
    }

    @Override
    protected void addLegalFeatures() {
        legalFeatures.add(FeatureNames.REGISTER);
    }

    @Override
    protected void addLegalPageSwitches() {
        legalPageSwitches.add(PageNames.LOGIN);
    }

    public boolean checkCredentials(Database database, Credentials credentials) {
        return database.isUserWithNameRegistered(credentials.getName());
    }

    public void addUserToDatabase(Database database, Credentials credentials) {
        User user = new User();
        user.setCredentials(credentials);
        database.addUser(user);
    }
}
