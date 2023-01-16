package utils.facade;

import com.fasterxml.jackson.databind.node.ArrayNode;
import utils.Database;
import utils.builder.Printer;
import utils.constants.Strings;
import utils.facade.command.ChangePage;
import utils.facade.command.OnPage;
import utils.memento.BackButton;
import utils.memento.Originator;
import utils.structures.*;
import utils.constants.PageNames;

import java.util.*;

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
    private String activePage = "";

    private final Originator originator = new Originator();
    private final BackButton backButton = new BackButton();

    private Printer printer = new Printer();

    private ChangePage changePage = new ChangePage();
    private OnPage onPage = new OnPage();
    private utils.facade.command.Database dabatase = new utils.facade.command.Database();

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

        activePage = PageNames.UNAUTHENTICATED;

        changePage.setData(outputData, database);
        onPage.setData(outputData, database);
        dabatase.setData(outputData, database);

        database.setLoggedUser(null);
    }

    /**
     *
     * @param action        command Action for every step
     */
    public void processPageAction(final Action action) {

        switch (action.getType()) {
            case Strings.CHANGE_PAGE -> {
                changePage.setSeeDetailsMovie(action.getMovie());
                changePage.setTransition(activePage, action.getPage());
                changePage.process();
                changePage.execute();
                activePage = changePage.getActivePage();

                if (!Objects.equals(backButton.getLastPage(), activePage)) {
                    savePageToQueue(activePage);
                }
            }

            case Strings.ON_PAGE -> {
                onPage.setActivePage(activePage);
                onPage.setAction(action);
                onPage.process();
                onPage.execute();

                if (!onPage.getActivePage().equals(activePage)) {
                    activePage = onPage.getActivePage();
                    savePageToQueue(activePage);
                }
            }

            case Strings.BACK -> {
                processBackButton();
            }

            case Strings.DATABASE -> {
                dabatase.setAction(action);
                dabatase.execute();
            }

            default -> { }
        }
    }

    public void savePageToQueue(String page) {
        originator.setState(page);
        backButton.addPage(originator.saveToMemento());
    }

    public void processBackButton() {
        String backPage = "";
        int size = backButton.getPageQueueSize();

        if (size > 1) {
            originator.getStateFromMemento(backButton.getPage(size - 2));
            backPage = originator.getState();

            if (backPage.equals(PageNames.LOGIN) || backPage.equals(PageNames.REGISTER)) {
                printer.printDefaultError(outputData);
                return;
            }

            backButton.removeLastPage();
        } else {
            backPage = PageNames.UNAUTHENTICATED;
        }

        changePage.setTransition(activePage, backPage);
        changePage.setSeeDetailsMovie("");
        changePage.process();
        changePage.execute();
        activePage = backPage;
    }

    public void processRecommendationsForPremium() {
        if (database.getLoggedUser() != null
            && database.getLoggedUser().getCredentials().getAccountType().equals(Strings.PREMIUM)) {

            List<Movie> likedMovies = database.getLoggedUser().getLikedMovies();
            Hashtable<String, Integer> genreLikes = new Hashtable<>();
            for (Movie movie : likedMovies) {
                for (String genre : movie.getGenres()) {
                    if (!genreLikes.containsKey(genre)) {
                        genreLikes.put(genre, 1);
                    } else {
                        genreLikes.put(genre, genreLikes.get(genre) + 1);
                    }
                }
            }

            String bestGenre = "";
            int maxLikes = 0;

            Enumeration<String> e = genreLikes.keys();
            while (e.hasMoreElements()) {
                String genre = e.nextElement();
                if (genreLikes.get(genre) > maxLikes) {
                    maxLikes = genreLikes.get(genre);
                    bestGenre = genre;
                }
            }

            Notification notification = new Notification();
            notification.setMovieName("No recommendation");
            notification.setMessage("Recommendation");

            List<Movie> databaseMovies = new ArrayList<>(database.getMovies());
            databaseMovies.sort((Comparator.comparingInt(Movie::getNumLikes).reversed()));
            for (Movie movie : databaseMovies) {
                if (movie.getGenres().contains(bestGenre)) {
                    notification.setMovieName(movie.getName());
                    notification.setMessage("Recommendation");
                    break;
                }
            }

            database.getLoggedUser().getNotifications().add(notification);
            printer.printPremiumNotifications(database.getLoggedUser(), outputData);
        }
    }

}
