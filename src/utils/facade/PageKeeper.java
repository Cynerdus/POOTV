package utils.facade;

import com.fasterxml.jackson.databind.node.ArrayNode;
import utils.Database;
import utils.constants.FeatureNames;
import utils.structures.Action;
import utils.Printer;
import utils.constants.PageNames;
import utils.constants.Strings;
import utils.structures.Credentials;
import utils.structures.Movie;

public class PageKeeper {

    private final Database database;
    private final ArrayNode outputData;

    private final Login login;
    private final Register register;
    private final Unauthenticated unauthenticated;
    private final Movies movies;
    private final SeeDetails seeDetails;
    private final Upgrades upgrades;
    private final Authenticated authenticated;

    public PageKeeper(final Database database, final ArrayNode outputData) {
        this.database = database;
        this.outputData = outputData;

        login =             new Login();
        register =          new Register();
        unauthenticated =   new Unauthenticated();
        movies =            new Movies();
        seeDetails =        new SeeDetails();
        upgrades =          new Upgrades();
        authenticated =     new Authenticated();

        switchActivityOnUnauthenticated();
        database.setLoggedUser(null);
    }

    public void processPageAction(final Action action) {

        String activePageName = getActivePageName();
        switch (activePageName) {
            case PageNames.LOGIN            -> processActionOnLoginPage(action);
            case PageNames.REGISTER         -> processActionOnRegisterPage(action);
            case PageNames.AUTHENTICATED    -> processActionOnAuthenticatedPage(action);
            case PageNames.MOVIES           -> processActionOnMoviesPage(action);
            case PageNames.LOGOUT,
                 PageNames.UNAUTHENTICATED  -> processActionOnUnauthenticatedPage(action);
            default                         -> throwInternalError();
        }
    }

    private String getActivePageName() {
        if (login.isActive())           return PageNames.LOGIN;
        if (register.isActive())        return PageNames.REGISTER;
        if (unauthenticated.isActive()) return PageNames.UNAUTHENTICATED;
        if (movies.isActive())          return PageNames.MOVIES;
        if (seeDetails.isActive())      return PageNames.SEE_DETAILS;
        if (upgrades.isActive())        return PageNames.UPGRADES;
        if (authenticated.isActive())   return PageNames.AUTHENTICATED;
        return "";
    }

    private void processActionOnUnauthenticatedPage(final Action action) {
        if (unauthenticated.isPageSwitchLegal(action.getPage())) {
            switchActivityOnUnauthenticated();

            switch (action.getPage()) {
                case PageNames.LOGIN    -> switchActivityOnLogin();
                case PageNames.REGISTER -> switchActivityOnRegister();
                default                 -> {switchActivityOnUnauthenticated();
                                            Printer.printDefaultError(outputData);}
            }
        } else {
            Printer.printDefaultError(outputData);
        }
    }

    private void processActionOnLoginPage(final Action action) {
        if (login.isPageSwitchLegal(action.getPage())) {
            switchActivityOnLogin();

            if (action.getPage().matches(PageNames.REGISTER)) {
                switchActivityOnRegister();
            } else {
                switchActivityOnUnauthenticated();
                switchActivityOnLogin();
                Printer.printDefaultError(outputData);
            }

            return;
        }

        if (login.isFeatureLegal(action.getFeature())) {
            if (login.checkCredentials(database, action.getCredentials())) {
                switchActivityOnLogin();
                switchActivityOnAuthenticated();
                database.setLoggedUser(database.getUserWithCredentials(action.getCredentials()));
                Printer.printHomePageData(outputData, getFullCredentials(action));
            } else {
                switchActivityOnLogin();
                switchActivityOnUnauthenticated();
                Printer.printDefaultError(outputData);
            }

            return;
        }

        Printer.printDefaultError(outputData);
    }

    private void processActionOnRegisterPage(final Action action) {
        if (register.isPageSwitchLegal(action.getPage())) {
            switchActivityOnRegister();

            if (action.getPage().matches(PageNames.LOGIN)) {
                switchActivityOnLogin();
            } else {
                switchActivityOnUnauthenticated();
                switchActivityOnRegister();
                Printer.printDefaultError(outputData);
            }

            return;
        }

        if (register.isFeatureLegal(action.getFeature())) {
            if (!register.checkCredentials(database, action.getCredentials())) {
                register.addUserToDatabase(database, action.getCredentials());
                switchActivityOnRegister();
                switchActivityOnAuthenticated();
                database.setLoggedUser(database.getUserWithCredentials(action.getCredentials()));
                Printer.printHomePageData(outputData, getFullCredentials(action));
            } else {
                Printer.printDefaultError(outputData);
            }

            return;
        }

        Printer.printDefaultError(outputData);
    }

    private void processActionOnAuthenticatedPage(final Action action) {
        if (authenticated.isPageSwitchLegal(action.getPage())) {
            switchActivityOnAuthenticated();

            switch (action.getPage()) {
                case PageNames.MOVIES   ->  switchActivityOnMovies(action);
                case PageNames.UPGRADES ->  switchActivityOnRegister();
                case PageNames.LOGOUT   ->  switchActivityOnUnauthenticated();
                default                 -> Printer.printDefaultError(outputData);
            }
        } else {
            Printer.printDefaultError(outputData);
        }
    }

    private void processActionOnMoviesPage(final Action action) {
        if (movies.isPageSwitchLegal(action.getPage())) {
            switchActivityOnMovies();

            switch (action.getPage()) {
                case PageNames.UPGRADES ->  switchActivityOnRegister();
                case PageNames.LOGOUT   ->  switchActivityOnUnauthenticated();
                default                 -> Printer.printDefaultError(outputData);
            }
        }

        if (movies.isFeatureLegal(action.getFeature())) {
            switch (action.getFeature()) {
                case FeatureNames.SEARCH ->
            }
        }
    }

    private void processActionOnUpgradesPage(final Action action) {

    }

    private void throwInternalError() {

    }

    private Credentials getFullCredentials(Action action) {
        return database.getUserWithCredentials(action.getCredentials()).getCredentials();
    }

    private void switchActivityOnLogin() {
        login.activeSwitch();
    }

    private void switchActivityOnRegister() {
        register.activeSwitch();
    }

    private void switchActivityOnUnauthenticated() {
        unauthenticated.activeSwitch();
    }

    private void switchActivityOnAuthenticated() {
        authenticated.activeSwitch();
    }

    private void switchActivityOnMovies(Action action) {
        movies.activeSwitch();
        Printer.printMovieListData(database, outputData, database.getLoggedUser().getCredentials());
    }

    private void switchActivityOnMovies() {
        movies.activeSwitch();
    }

    private void switchActivityOnSeeDetails() {
        seeDetails.activeSwitch();
    }

    private void switchActivityOnUpgrades() {
        upgrades.activeSwitch();
    }
}
