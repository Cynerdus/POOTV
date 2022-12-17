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

import java.util.List;

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
            case PageNames.UPGRADES         -> processActionOnUpgradesPage(action);
            case PageNames.SEE_DETAILS      -> processActionOnSeeDetailsPage(action);
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
                Printer.printHomePageData(database, outputData, getFullCredentials(action));
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
                Printer.printHomePageData(database, outputData, getFullCredentials(action));
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
                case PageNames.MOVIES       -> switchActivityOnMovies(action);
                case PageNames.UPGRADES     -> switchActivityOnUpgrades();
                case PageNames.SEE_DETAILS  -> switchActivityOnSeeDetails(action.getMovie());
                case PageNames.LOGOUT       -> switchActivityOnUnauthenticated();
                default                     -> Printer.printDefaultError(outputData);
            }
        } else {
            Printer.printDefaultError(outputData);
        }
    }

    private void processActionOnMoviesPage(final Action action) {
        System.out.println("movies");
        if (movies.isPageSwitchLegal(action.getPage())) {
            switchActivityOnMovies();

            switch (action.getPage()) {
                case PageNames.UPGRADES     -> switchActivityOnUpgrades();
                case PageNames.SEE_DETAILS  -> switchActivityOnSeeDetails(action.getMovie());
                case PageNames.LOGOUT       -> switchActivityOnUnauthenticated();
                default                     -> Printer.printDefaultError(outputData);
            }

            return;
        }

        if (movies.isFeatureLegal(action.getFeature())) {
            System.out.println("movie features: " + action.getFeature());
            switch (action.getFeature()) {
                case FeatureNames.SEARCH    -> Printer.printMoviesBySearch(database, outputData, getLoggedUserCredentials(), action.getStartsWith());
                case FeatureNames.FILTER
                        -> {
                    System.out.println("feature filter");
                    List<Movie> list = Printer.printMoviesByFilter(database, outputData, getLoggedUserCredentials(), action.getFilters());
                    database.setCurrentlyFilteredMovies(list);
                }
                default                     -> Printer.printDefaultError(outputData);
            }

            return;
        }

        Printer.printDefaultError(outputData);
    }

    private void processActionOnUpgradesPage(final Action action) {

        if (upgrades.isPageSwitchLegal(action.getPage())) {
            switchActivityOnUpgrades();

            switch (action.getPage()) {
                case PageNames.MOVIES       -> switchActivityOnMovies(action);
                case PageNames.SEE_DETAILS  -> switchActivityOnSeeDetails(action.getMovie());
                case PageNames.LOGOUT       -> switchActivityOnUnauthenticated();
                default                     -> Printer.printDefaultError(outputData);
            }

            return;
        }

        if (upgrades.isFeatureLegal(action.getFeature())) {
            boolean rc;

            switch (action.getFeature()) {
                case FeatureNames.BUY_TOKENS            -> rc = upgrades.buyTokens(action.getCount(), database.getLoggedUser());
                case FeatureNames.BUY_PREMIUM_ACCOUNT   -> rc = upgrades.buyPremium(database.getLoggedUser());
                default                                 -> rc = false;
            }

            if (!rc)
                Printer.printDefaultError(outputData);

            return;
        }

        Printer.printDefaultError(outputData);
    }

    private void processActionOnSeeDetailsPage(final Action action) {
        if (seeDetails.isPageSwitchLegal(action.getPage())) {
            switchActivityOnSeeDetails();

            switch (action.getPage()) {
                case PageNames.MOVIES       -> switchActivityOnMovies(action);
                case PageNames.UPGRADES     -> switchActivityOnUpgrades();
                case PageNames.SEE_DETAILS  -> switchActivityOnSeeDetails(action.getMovie());
                case PageNames.LOGOUT       -> switchActivityOnUnauthenticated();
                default                     -> Printer.printDefaultError(outputData);
            }

            return;
        }

        switch (action.getFeature()) {
            case FeatureNames.WATCH     -> activateWatchFeature(action);
            case FeatureNames.PURCHASE  -> activatePurchaseFeature(action);
            case FeatureNames.LIKE      -> activateLikeFeature(action);
            case FeatureNames.RATE      -> activateRateFeature(action);
            case FeatureNames.FILTER
                    -> {
                System.out.println("feature filter");
                List<Movie> list = Printer.printMoviesByFilter(database, outputData, getLoggedUserCredentials(), action.getFilters());
                database.setCurrentlyFilteredMovies(list);
            }
            default                     -> Printer.printDefaultError(outputData);
        }
    }

    private void throwInternalError() {
        System.out.println("err");
    }

    private Credentials getFullCredentials(Action action) {
        return database.getUserWithCredentials(action.getCredentials()).getCredentials();
    }

    private Credentials getLoggedUserCredentials() {
        return database.getLoggedUser().getCredentials();
    }

    private Movie getMovieFromDatabase(final String name) {
        for (Movie movie : database.getMovies()) {
            if (movie.getName().equals(name))
                return movie;
        }
        return null;
    }

    private void activateWatchFeature(final Action action) {
        Movie movie = (database.getCurrentMovieOnScreen() != null) ? database.getCurrentMovieOnScreen()
                : (action.getMovie() != null) ? getMovieFromDatabase(action.getMovie()) : null;

        if (movie != null && database.getLoggedUser().getPurchasedMovies().contains(movie)) {
            seeDetails.addMovieToWatchList(movie, database.getLoggedUser());
            Printer.printMovieFromSeeDetails(database, outputData, database.getLoggedUser().getCredentials(), movie.getName());
        } else
            Printer.printDefaultError(outputData);
    }

    private void activatePurchaseFeature(final Action action) {
        Movie movie = (database.getCurrentMovieOnScreen() != null) ? database.getCurrentMovieOnScreen()
                : (action.getMovie() != null) ? getMovieFromDatabase(action.getMovie()) : null;

        if (movie != null && !database.getLoggedUser().getPurchasedMovies().contains(movie)) {
            boolean rc = seeDetails.addMovieToPurchasedList(movie, database.getLoggedUser());
            if (rc)
                Printer.printMovieFromSeeDetails(database, outputData, database.getLoggedUser().getCredentials(), movie.getName());
            else
                Printer.printDefaultError(outputData);
        } else
            Printer.printDefaultError(outputData);
    }

    private void activateLikeFeature(final Action action) {
        Movie movie = (database.getCurrentMovieOnScreen() != null) ? database.getCurrentMovieOnScreen()
                : (action.getMovie() != null) ? getMovieFromDatabase(action.getMovie()) : null;

        if (movie != null && database.getLoggedUser().getPurchasedMovies().contains(movie)) {
            seeDetails.addMovieToLikedList(movie, database.getLoggedUser());
            Printer.printMovieFromSeeDetails(database, outputData, database.getLoggedUser().getCredentials(), movie.getName());
        } else
            Printer.printDefaultError(outputData);

    }

    private void activateRateFeature(final Action action) {
        Movie movie = (database.getCurrentMovieOnScreen() != null) ? database.getCurrentMovieOnScreen()
                : (action.getMovie() != null) ? getMovieFromDatabase(action.getMovie()) : null;

        if (movie != null && database.getLoggedUser().getPurchasedMovies().contains(movie)) {
            boolean rc = seeDetails.addMovieToRatedList(movie, database.getLoggedUser(), action.getRate());
            if (rc)
                Printer.printMovieFromSeeDetails(database, outputData, database.getLoggedUser().getCredentials(), movie.getName());
            else
                Printer.printDefaultError(outputData);
        } else
            Printer.printDefaultError(outputData);
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

    private void switchActivityOnMovies(final Action action) {
        movies.activeSwitch();
        Printer.printMovieListData(database, outputData, getLoggedUserCredentials());
    }

    private void switchActivityOnMovies() {
        movies.activeSwitch();
    }

    private void switchActivityOnSeeDetails(final String movieName) {
        Movie movie = (database.getCurrentlyFilteredMovies() != null)
                    ? database.getElementFromFilteredMovies(movieName)
                    : getMovieFromDatabase(movieName);

        seeDetails.activeSwitch();
        database.setCurrentMovieOnScreen(movie);
        if (movie != null && !movie.getCountriesBanned().contains(database.getLoggedUser().getCredentials().getCountry())) {
            Printer.printMovieFromSeeDetails(database, outputData, database.getLoggedUser().getCredentials(), movieName);
        }
        else {
            database.setCurrentMovieOnScreen(null);
            Printer.printDefaultError(outputData);
        }
    }

    private void switchActivityOnSeeDetails() {
        seeDetails.activeSwitch();
    }

    private void switchActivityOnUpgrades() {
        upgrades.activeSwitch();
    }
}
