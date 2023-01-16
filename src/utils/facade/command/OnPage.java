package utils.facade.command;

import com.fasterxml.jackson.databind.node.ArrayNode;
import utils.Database;
import utils.builder.Printer;
import utils.constants.FeatureNames;
import utils.constants.PageNames;
import utils.facade.*;
import utils.structures.Action;
import utils.structures.Credentials;
import utils.structures.Movie;

import java.util.List;

public class OnPage implements Command{

    private Database database;
    private ArrayNode outputData;
    private Action action;

    private String active = "";

    private String feature = "";

    private final Login login = new Login();
    private final Register register = new Register();
    private final Unauthenticated unauthenticated = new Unauthenticated();
    private final Movies movies = new Movies();
    private final SeeDetails seeDetails = new SeeDetails();
    private final Upgrades upgrades = new Upgrades();
    private final Authenticated authenticated = new Authenticated();

    private final Printer printer = new Printer();

    private Boolean eligible = false;

    public void setData(final ArrayNode outputData, final Database database) {
        this.outputData = outputData;
        this.database = database;
    }

    public void setActivePage(final String page) {
        active = page;
    }

    public void setAction(final Action action) {
        this.action = action;
        feature = action.getFeature();
    }

    @Override
    public void process() {
        eligible = switch (active) {
            case PageNames.UNAUTHENTICATED -> unauthenticated.isFeatureLegal(feature);
            case PageNames.LOGIN -> login.isFeatureLegal(feature);
            case PageNames.REGISTER -> register.isFeatureLegal(feature);
            case PageNames.AUTHENTICATED -> authenticated.isFeatureLegal(feature);
            case PageNames.MOVIES -> movies.isFeatureLegal(feature);
            case PageNames.UPGRADES -> upgrades.isFeatureLegal(feature);
            case PageNames.SEE_DETAILS -> seeDetails.isFeatureLegal(feature);
            default -> false;
        };
    }

    @Override
    public void execute() {
        if (!eligible) {
            printer.printDefaultError(outputData);
            return;
        }

        switch (feature) {
            case FeatureNames.LOGIN -> executeLogin();
            case FeatureNames.REGISTER -> executeRegister();
            case FeatureNames.PURCHASE -> executePurchase();
            case FeatureNames.WATCH -> executeWatch();
            case FeatureNames.LIKE -> executeLike();
            case FeatureNames.RATE -> executeRate();
            case FeatureNames.SEARCH -> executeSearch();
            case FeatureNames.FILTER -> executeFilter();
            case FeatureNames.BUY_PREMIUM_ACCOUNT -> executeBuyPremium();
            case FeatureNames.BUY_TOKENS -> executeBuyTokens();
            case FeatureNames.SUBSCRIBE -> executeSubscribe();
            default -> printer.printDefaultError(outputData);
        }
    }

    private void executeLogin() {
        if (login.checkCredentials(database, action.getCredentials())) {
            database.setLoggedUser(database.getUserWithCredentials(action.getCredentials()));
            printer.printHomePageData(database.getLoggedUser(), outputData);

            active = PageNames.AUTHENTICATED;
            return;
        }

        active = PageNames.UNAUTHENTICATED;
        printer.printDefaultError(outputData);
    }

    private void executeRegister() {
        if (!register.checkCredentials(database, action.getCredentials())) {
            register.addUserToDatabase(database, action.getCredentials());

            database.setLoggedUser(database.getUserWithCredentials(action.getCredentials()));
            printer.printHomePageData(database.getLoggedUser(), outputData);

            active = PageNames.AUTHENTICATED;
            return;
        }

        active = PageNames.UNAUTHENTICATED;
        printer.printDefaultError(outputData);
    }

    private void executePurchase() {
        Movie movie = (database.getCurrentMovieOnScreen() != null)
                ? database.getCurrentMovieOnScreen()
                : (action.getMovie() != null)
                ? getMovieFromDatabase(action.getMovie()) : null;

        if (movie != null && !database.getLoggedUser().getPurchasedMovies().contains(movie)) {
            boolean rc = seeDetails.addMovieToPurchasedList(movie, database.getLoggedUser());
            if (rc) {
                printer.printMovieFromSeeDetails(database, outputData, movie.getName());
            } else {
                printer.printDefaultError(outputData);
            }

            return;
        }
        printer.printDefaultError(outputData);

    }

    private void executeWatch() {
        Movie movie = (database.getCurrentMovieOnScreen() != null)
                ? database.getCurrentMovieOnScreen()
                : (action.getMovie() != null)
                ? getMovieFromDatabase(action.getMovie()) : null;

        if (movie != null && database.getLoggedUser().getPurchasedMovies().contains(movie)) {
            seeDetails.addMovieToWatchList(movie, database.getLoggedUser());
            printer.printMovieFromSeeDetails(database, outputData, movie.getName());

            return;
        }

        printer.printDefaultError(outputData);
    }

    private void executeLike() {
        Movie movie = (database.getCurrentMovieOnScreen() != null)
                ? database.getCurrentMovieOnScreen()
                : (action.getMovie() != null)
                ? getMovieFromDatabase(action.getMovie()) : null;

        if (movie != null && database.getLoggedUser().getPurchasedMovies().contains(movie)) {
            seeDetails.addMovieToLikedList(movie, database.getLoggedUser());
            printer.printMovieFromSeeDetails(database, outputData, movie.getName());

            return;
        }
        printer.printDefaultError(outputData);

    }

    private void executeRate() {
        Movie movie = (database.getCurrentMovieOnScreen() != null)
                ? database.getCurrentMovieOnScreen()
                : (action.getMovie() != null)
                ? getMovieFromDatabase(action.getMovie()) : null;

        if (movie != null && database.getLoggedUser().getPurchasedMovies().contains(movie)) {
            boolean rc = seeDetails.addMovieToRatedList(movie,
                    database.getLoggedUser(),
                    action.getRate());
            if (rc) {
                printer.printMovieFromSeeDetails(database, outputData, movie.getName());
            } else {
                printer.printDefaultError(outputData);
            }

            return;
        }
        printer.printDefaultError(outputData);

    }

    private void executeSearch() {
        printer.printMoviesBySearch(database, outputData, action.getStartsWith());
    }

    private void executeFilter() {
        List<Movie> list;
        list = printer.getFilterMovies(database.getLoggedUser(),
                        database.getMovies(), action.getFilters());
        printer.printMovieByFilter(database, outputData, action.getFilters());

        database.setCurrentlyFilteredMovies(list);
    }

    private void executeBuyPremium() {
        upgrades.buyPremium(database.getLoggedUser());
    }

    private void executeBuyTokens() {
        upgrades.buyTokens(action.getCount(), database.getLoggedUser());
    }

    private void executeSubscribe() {
        if (database.getLoggedUser().getSubscriptions().contains(action.getSubscribedGenre())) {
            printer.printDefaultError(outputData);
        } else {
            database.getLoggedUser().getSubscriptions().add(action.getSubscribedGenre());
        }
    }

    private Movie getMovieFromDatabase(final String name) {
        for (Movie movie : database.getMovies()) {
            if (movie.getName().equals(name)) {
                return movie;
            }
        }
        return null;
    }

    public String getActivePage() {
        return active;
    }
}
