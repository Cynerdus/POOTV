package utils.facade;

import com.fasterxml.jackson.databind.node.ArrayNode;
import utils.Database;
import utils.constants.FeatureNames;
import utils.constants.Strings;
import utils.structures.*;
import utils.Printer;
import utils.constants.PageNames;

import java.util.ArrayList;
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

    /**
     *
     * @param action        command Action for every step
     */
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

    public void processDatabaseModification(final Action action) {
        if (action.getFeature().matches(FeatureNames.ADD)) {
            if (database.getMovieByName(action.getAddedMovie().getName()) != null) {
                Printer.printDefaultErrorWithCredentials(database, outputData);
            }

            Movie newMovie = new Movie();
            newMovie.setName(action.getAddedMovie().getName());
            newMovie.setYear(action.getAddedMovie().getYear());
            newMovie.setDuration(action.getAddedMovie().getDuration());

            ArrayList<String> genres, actors, countriesBanned;
            genres = new ArrayList<>(action.getAddedMovie().getGenres());
            actors = new ArrayList<>(action.getAddedMovie().getActors());
            countriesBanned = new ArrayList<>(action.getAddedMovie().getCountriesBanned());

            newMovie.setGenres(genres);
            newMovie.setActors(actors);
            newMovie.setCountriesBanned(countriesBanned);

            database.addMovie(newMovie);
            notifyUsers(newMovie.getGenres(), newMovie.getName(), Strings.ADD);
        }
    }

    private void notifyUsers(final List<String> genres, final String movieName, final String message) {
        List<User> userList = database.getUsers();
        for (User user : userList) {
            for (String genre : genres) {
                if (user.getSubscriptions().contains(genre)) {
                    Notification notification = new Notification();
                    notification.setMovieName(movieName);
                    notification.setMessage(message);
                    user.getNotifications().add(notification);
                }
            }
        }
    }

    private String getActivePageName() {
        if (login.isActive()) {
            return PageNames.LOGIN;
        }
        if (register.isActive()) {
            return PageNames.REGISTER;
        }
        if (unauthenticated.isActive()) {
            return PageNames.UNAUTHENTICATED;
        }
        if (movies.isActive()) {
            return PageNames.MOVIES;
        }
        if (seeDetails.isActive()) {
            return PageNames.SEE_DETAILS;
        }
        if (upgrades.isActive()) {
            return PageNames.UPGRADES;
        }
        if (authenticated.isActive()) {
            return PageNames.AUTHENTICATED;
        }
        return "";
    }

    private void processActionOnUnauthenticatedPage(final Action action) {
        if (unauthenticated.isPageSwitchLegal(action.getPage())) {
            switchActivityOnUnauthenticated();

            boolean error = false;
            switch (action.getPage()) {
                case PageNames.LOGIN    -> switchActivityOnLogin();
                case PageNames.REGISTER -> switchActivityOnRegister();
                default                 -> switchActivityOnUnauthenticated(error);
            }

            if (!error) {
                return;
            }
        }

        Printer.printDefaultError(outputData);
    }

    private void processActionOnLoginPage(final Action action) {
        if (login.isPageSwitchLegal(action.getPage())) {
            switchActivityOnLogin();

            boolean error = false;
            if (action.getPage().equals(PageNames.REGISTER)) {
                switchActivityOnRegister();
            } else {
                switchActivityOnUnauthenticated(error);
                switchActivityOnLogin();
            }

            if (!error) {
                return;
            }
        }

        if (login.checkCredentials(database, action.getCredentials())) {

            switchActivityOnLogin();
            switchActivityOnAuthenticated();

            database.setLoggedUser(database.getUserWithCredentials(action.getCredentials()));
            Printer.printHomePageData(database, outputData, getFullCredentials(action));

            return;
        } else {
            switchActivityOnLogin();
            switchActivityOnUnauthenticated();
        }

        Printer.printDefaultError(outputData);
    }

    private void processActionOnRegisterPage(final Action action) {
        if (register.isPageSwitchLegal(action.getPage())) {
            switchActivityOnRegister();

            boolean error = false;
            if (action.getPage().equals(PageNames.LOGIN)) {
                switchActivityOnLogin();
            } else {
                switchActivityOnUnauthenticated(error);
                switchActivityOnRegister();
            }

            if (!error) {
                return;
            }
        }

        if (!register.checkCredentials(database, action.getCredentials())) {
            register.addUserToDatabase(database, action.getCredentials());
            switchActivityOnRegister();
            switchActivityOnAuthenticated();

            database.setLoggedUser(database.getUserWithCredentials(action.getCredentials()));
            Printer.printHomePageData(database, outputData, getFullCredentials(action));

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

            return;
        }

        Printer.printDefaultError(outputData);
    }

    private void processActionOnMoviesPage(final Action action) {
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
            switch (action.getFeature()) {
                case FeatureNames.SEARCH    -> Printer.printMoviesBySearch(
                                                                    database,
                                                                    outputData,
                                                                    getLoggedUserCredentials(),
                                                                    action.getStartsWith());
                case FeatureNames.FILTER    -> {
                                                List<Movie> list;
                                                list = Printer.printMoviesByFilter(
                                                                    database,
                                                                    outputData,
                                                                    getLoggedUserCredentials(),
                                                                    action.getFilters());
                                                database.setCurrentlyFilteredMovies(list); }
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

            rc = switch (action.getFeature()) {
                case FeatureNames.BUY_TOKENS            -> upgrades.buyTokens(
                                                                        action.getCount(),
                                                                        database.getLoggedUser());
                case FeatureNames.BUY_PREMIUM_ACCOUNT   -> upgrades.buyPremium(
                                                                        database.getLoggedUser());
                default                                 -> false;
            };

            if (!rc) {
                Printer.printDefaultError(outputData);
            }

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
            case FeatureNames.SUBSCRIBE -> activateSubscribeFeature(action);
            case FeatureNames.FILTER    -> {
                                                List<Movie> list;
                                                list = Printer.printMoviesByFilter(database,
                                                                    outputData,
                                                                    getLoggedUserCredentials(),
                                                                    action.getFilters());
                                            database.setCurrentlyFilteredMovies(list); }
            default                     -> Printer.printDefaultError(outputData);
        }
    }

    private void throwInternalError() {
    }

    private Credentials getFullCredentials(final Action action) {
        return database.getUserWithCredentials(action.getCredentials()).getCredentials();
    }

    private Credentials getLoggedUserCredentials() {
        return database.getLoggedUser().getCredentials();
    }

    private Movie getMovieFromDatabase(final String name) {
        for (Movie movie : database.getMovies()) {
            if (movie.getName().equals(name)) {
                return movie;
            }
        }
        return null;
    }

    private void activateWatchFeature(final Action action) {
        Movie movie = (database.getCurrentMovieOnScreen() != null)
                ? database.getCurrentMovieOnScreen()
                : (action.getMovie() != null)
                ? getMovieFromDatabase(action.getMovie()) : null;

        if (movie != null && database.getLoggedUser().getPurchasedMovies().contains(movie)) {
            seeDetails.addMovieToWatchList(movie, database.getLoggedUser());

            Credentials credentials = database.getLoggedUser().getCredentials();
            Printer.printMovieFromSeeDetails(database, outputData, credentials, movie.getName());
        } else {
            Printer.printDefaultError(outputData);
        }
    }

    private void activatePurchaseFeature(final Action action) {
        Movie movie = (database.getCurrentMovieOnScreen() != null)
                ? database.getCurrentMovieOnScreen()
                : (action.getMovie() != null)
                ? getMovieFromDatabase(action.getMovie()) : null;

        if (movie != null && !database.getLoggedUser().getPurchasedMovies().contains(movie)) {
            boolean rc = seeDetails.addMovieToPurchasedList(movie, database.getLoggedUser());
            if (rc) {
                Credentials credentials = database.getLoggedUser().getCredentials();
                Printer.printMovieFromSeeDetails(database,
                                                outputData,
                                                credentials,
                                                movie.getName());
            } else {
                Printer.printDefaultError(outputData);
            }
        } else {
            Printer.printDefaultError(outputData);
        }
    }

    private void activateLikeFeature(final Action action) {
        Movie movie = (database.getCurrentMovieOnScreen() != null)
                    ? database.getCurrentMovieOnScreen()
                    : (action.getMovie() != null)
                    ? getMovieFromDatabase(action.getMovie()) : null;

        if (movie != null && database.getLoggedUser().getPurchasedMovies().contains(movie)) {
            seeDetails.addMovieToLikedList(movie, database.getLoggedUser());

            Credentials credentials = database.getLoggedUser().getCredentials();
            Printer.printMovieFromSeeDetails(database, outputData, credentials, movie.getName());
        } else {
            Printer.printDefaultError(outputData);
        }

    }

    private void activateRateFeature(final Action action) {
        Movie movie = (database.getCurrentMovieOnScreen() != null)
                    ? database.getCurrentMovieOnScreen()
                    : (action.getMovie() != null)
                    ? getMovieFromDatabase(action.getMovie()) : null;

        if (movie != null && database.getLoggedUser().getPurchasedMovies().contains(movie)) {
            Credentials credentials = database.getLoggedUser().getCredentials();

            boolean rc = seeDetails.addMovieToRatedList(movie,
                                                        database.getLoggedUser(),
                                                        action.getRate());
            if (rc) {
                Printer.printMovieFromSeeDetails(database,
                                                outputData,
                                                credentials,
                                                movie.getName());
            } else {
                Printer.printDefaultError(outputData);
            }
        } else {
            Printer.printDefaultError(outputData);
        }
    }

    public void activateSubscribeFeature(Action action) {
        if (database.getLoggedUser().getSubscriptions().contains(action.getSubscribedGenre())) {
            Printer.printDefaultError(outputData);
        } else {
            database.getLoggedUser().getSubscriptions().add(action.getSubscribedGenre());
        }
    }

    private void switchActivityOnLogin() {
        login.activeSwitch();
    }

    private void switchActivityOnRegister() {
        register.activeSwitch();
    }

    private void switchActivityOnUnauthenticated() {
        unauthenticated.activeSwitch();
        database.setLoggedUser(null);
    }
    private void switchActivityOnUnauthenticated(boolean error) {
        unauthenticated.activeSwitch();
        database.setLoggedUser(null);
        error = true;
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
        if (movie != null && !movie.getCountriesBanned()
            .contains(database.getLoggedUser().getCredentials().getCountry())) {

            Credentials credentials = database.getLoggedUser().getCredentials();
            Printer.printMovieFromSeeDetails(database, outputData, credentials, movieName);
        } else {
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
