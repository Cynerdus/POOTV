package utils.facade;

import utils.Database;
import utils.constants.FeatureNames;
import utils.constants.PageNames;
import utils.structures.Credentials;

import javax.xml.crypto.Data;

public class Login extends Page{

    public Login() {
        addLegalFeatures();
        addLegalPageSwitches();
    }

    @Override
    protected void addLegalFeatures() {
        legalFeatures.add(FeatureNames.LOGIN);
    }

    @Override
    protected void addLegalPageSwitches() {
        legalPageSwitches.add(PageNames.REGISTER);
    }

    public boolean checkCredentials(Database database, Credentials credentials) {
        return database.isUserWithCredentialsRegistered(credentials);
    }
}
