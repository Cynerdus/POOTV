package utils.facade.command;

import com.fasterxml.jackson.databind.node.ArrayNode;
import utils.Database;
import utils.builder.Printer;
import utils.constants.PageNames;
import utils.facade.*;
import utils.memento.BackButton;
import utils.structures.Movie;

public class ChangePage implements Command{

    private Database database;
    private ArrayNode outputData;

    private String current = "";
    private String next = "";
    private String active = "";

    private final Login login = new Login();
    private final Register register = new Register();
    private final Unauthenticated unauthenticated = new Unauthenticated();
    private final Movies movies = new Movies();
    private final SeeDetails seeDetails = new SeeDetails();
    private final Upgrades upgrades = new Upgrades();
    private final Authenticated authenticated = new Authenticated();

    private final Printer printer = new Printer();

    private Boolean eligible = false;

    private String movieName = "";

    public void setData(final ArrayNode outputData, final Database database) {
        this.outputData = outputData;
        this.database = database;
    }

    public void setTransition(final String current, final String next) {
        this.current = current;
        this.next = next;
    }

    @Override
    public void process() {
        eligible = switch (current) {
            case PageNames.UNAUTHENTICATED -> unauthenticated.isPageSwitchLegal(next);
            case PageNames.LOGIN -> login.isPageSwitchLegal(next);
            case PageNames.REGISTER -> register.isPageSwitchLegal(next);
            case PageNames.AUTHENTICATED -> authenticated.isPageSwitchLegal(next);
            case PageNames.MOVIES -> movies.isPageSwitchLegal(next);
            case PageNames.UPGRADES -> upgrades.isPageSwitchLegal(next);
            case PageNames.SEE_DETAILS -> seeDetails.isPageSwitchLegal(next);
            default -> false;
        };

        next = (next.equals(PageNames.LOGOUT) && eligible) ? PageNames.UNAUTHENTICATED : next;

        active = (eligible) ? next
                : (current.equals(PageNames.LOGIN)
                || current.equals(PageNames.REGISTER))
                ? PageNames.UNAUTHENTICATED : current;
    }

    @Override
    public void execute() {
        if (!eligible) {
            printer.printDefaultError(outputData);

            if (current.equals(PageNames.LOGIN) || current.equals(PageNames.REGISTER)) {
                database.setLoggedUser(null);
            }
            return;
        }

        activatePageSpecials();
    }

    private void activatePageSpecials() {
        if (active.equals(PageNames.MOVIES)) {
            printer.printMovieListData(database, outputData);
        } else if (active.equals(PageNames.SEE_DETAILS)) {
            Movie movie = (database.getCurrentlyFilteredMovies() != null)
                    ? database.getElementFromFilteredMovies(movieName)
                    : getMovieFromDatabase(movieName);

            database.setCurrentMovieOnScreen(movie);
            if (movie != null && !movie.getCountriesBanned()
                .contains(database.getLoggedUser().getCredentials().getCountry())) {

                printer.printMovieFromSeeDetails(database, outputData, movieName);
            } else {
                database.setCurrentMovieOnScreen(null);
                printer.printDefaultError(outputData);
            }
        }
    }

    public String getActivePage() {
        return active;
    }

    public void setSeeDetailsMovie(final String movieName) {
        this.movieName = movieName;
    }

    private Movie getMovieFromDatabase(final String name) {
        for (Movie movie : database.getMovies()) {
            if (movie.getName().equals(name)) {
                return movie;
            }
        }
        return null;
    }
}
