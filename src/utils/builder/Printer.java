package utils.builder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import utils.Database;
import utils.constants.Strings;
import utils.structures.Filters;
import utils.structures.Movie;
import utils.structures.User;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Printer {

    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final Formatter formatter = new Formatter();
    private ObjectNode node1 = OBJECT_MAPPER.createObjectNode();
    private ObjectNode node2 = OBJECT_MAPPER.createObjectNode();

    private ArrayNode list1 = OBJECT_MAPPER.createArrayNode();
    private ArrayNode list2 = OBJECT_MAPPER.createArrayNode();

    public Printer() {}

    public void printHomePageData(final User loggedUser,
                                  final ArrayNode output) {

        resetNodes();

        node1 = formatter.setError(node1, null);
        node1 = formatter.setCurrentMoviesList(node1, OBJECT_MAPPER.createArrayNode());
        node1 = formatter.setCurrentUser(node1, printUserNode(loggedUser));

        output.add(node1);
    }

    public void printDefaultError(final ArrayNode output) {
        resetNodes();

        node1 = formatter.setError(node1, "Error");
        node1 = formatter.setCurrentMoviesList(node1, OBJECT_MAPPER.createArrayNode());
        node1 = formatter.setCurrentUser(node1, null);

        output.add(node1);
    }

    public void printDefaultErrorWithCredentials(final User loggedUser,
                                                 final ArrayNode output) {

        resetNodes();

        node1 = formatter.setError(node1, "Error");
        node1 = formatter.setCurrentMoviesList(node1, OBJECT_MAPPER.createArrayNode());
        node1 = formatter.setCurrentUser(node1, (loggedUser == null) ? null : printUserNode(loggedUser));

        output.add(node1);
    }

    public void printPremiumNotifications(final User loggedUser,
                                          final ArrayNode output) {

        resetNodes();

        node1 = formatter.setError(node1, null);
        node1 = formatter.setCurrentMoviesList(node1, null);
        node1 = formatter.setCurrentUser(node1, printUserNode(loggedUser));

        output.add(node1);
    }

    public void printMovieListData(final Database database,
                                   final ArrayNode output) {
        
        resetNodes();

        node1 = formatter.setError(node1, null);
        list1 = formatter.setUserMovieList(
                OBJECT_MAPPER.createArrayNode(),
                getEligibleMovies(database.getLoggedUser(),
                database.getMovies()));
        node1 = formatter.setCurrentMoviesList(node1, list1);
        node1 = formatter.setCurrentUser(node1, printUserNode(database.getLoggedUser()));

        output.add(node1);
    }

    public void printMoviesBySearch(final Database database,
                                    final ArrayNode output,
                                    final String startsWith) {

        resetNodes();

        node1 = formatter.setError(node1, null);
        list1 = formatter.setUserMovieList(
                OBJECT_MAPPER.createArrayNode(),
                getSearchMovies(database.getLoggedUser(),
                database.getMovies(), startsWith));
        node1 = formatter.setCurrentMoviesList(node1, list1);
        node1 = formatter.setCurrentUser(node1, printUserNode(database.getLoggedUser()));

        output.add(node1);
    }

    public void printMovieByFilter(final Database database,
                                   final ArrayNode output,
                                   final Filters filters) {

        resetNodes();

        node1 = formatter.setError(node1, null);
        list1 = formatter.setUserMovieList(
                OBJECT_MAPPER.createArrayNode(),
                getFilterMovies(database.getLoggedUser(),
                database.getMovies(), filters));
        node1 = formatter.setCurrentMoviesList(node1, list1);
        node1 = formatter.setCurrentUser(node1, printUserNode(database.getLoggedUser()));

        output.add(node1);
    }

    public void printMovieFromSeeDetails(final Database database,
                                         final ArrayNode output,
                                         final String name) {

        resetNodes();

        node1 = formatter.setError(node1, null);

        List<Movie> movie = new ArrayList<>();
        movie.add(getMovie(database.getLoggedUser(), database.getMovies(), name));
        list1 = formatter.setUserMovieList(OBJECT_MAPPER.createArrayNode(), movie);
        node1 = formatter.setCurrentMoviesList(node1, list1);
        node1 = formatter.setCurrentUser(node1, printUserNode(database.getLoggedUser()));

        output.add(node1);
    }

    private ObjectNode printUserNode(final User user) {
        node2 = formatter.setUserDetails(node2, user);

        node2 = formatter.putUserList(node2, formatter.setUserMovieList(
                OBJECT_MAPPER.createArrayNode(),
                user.getPurchasedMovies()), "purchasedMovies");

        node2 = formatter.putUserList(node2, formatter.setUserMovieList(
                OBJECT_MAPPER.createArrayNode(),
                user.getLikedMovies()), "likedMovies");

        node2 = formatter.putUserList(node2, formatter.setUserMovieList(
                OBJECT_MAPPER.createArrayNode(),
                user.getRatedMovies()), "ratedMovies");

        node2 = formatter.putUserList(node2, formatter.setUserMovieList(
                OBJECT_MAPPER.createArrayNode(),
                user.getWatchedMovies()), "watchedMovies");

        node2 = formatter.putUserList(node2, formatter.setNotificationList(
                OBJECT_MAPPER.createArrayNode(),
                user.getNotifications()), "notifications");

        return node2;
    }

    private List<Movie> getEligibleMovies(final User user, final List<Movie> movies) {
        List<Movie> newList = new ArrayList<>();

        for (Movie movie : movies) {
            if (!movie.getCountriesBanned().contains(user.getCredentials().getCountry())) {
                newList.add(movie);
            }
        }

        return newList;
    }

    private List<Movie> getSearchMovies(final User user,
                                        final List<Movie> movies,
                                        final String startsWith) {

        List<Movie> newList = new ArrayList<>();

        for (Movie movie : movies) {
            if (!movie.getCountriesBanned().contains(user.getCredentials().getCountry())
                && movie.getName().startsWith(startsWith)) {
                newList.add(movie);
            }
        }

        return newList;
    }

    public List<Movie> getFilterMovies(final User user,
                                        final List<Movie> movies,
                                        final Filters filters) {

        List<Movie> newList = new ArrayList<>();

        for (Movie movie : movies) {
            if (!movie.getCountriesBanned().contains(user.getCredentials().getCountry())) {
                boolean containsActors = true, containsGenre = true;

                for (String actor : filters.getContains().getActors()) {
                    if (!movie.getActors().contains(actor)) {
                        containsActors = false;
                        break;
                    }
                }

                for (String genre : filters.getContains().getGenre()) {
                    if (!movie.getGenres().contains(genre)) {
                        containsGenre = false;
                        break;
                    }
                }

                if (containsActors && containsGenre) {
                    newList.add(movie);
                }
            }
        }

        /*
          defibrillation
         */

        if (Objects.equals(filters.getSort().getDuration(), " ")
            && filters.getSort().getRating().equals(Strings.INCREASING)) {
            newList.sort((Comparator.comparingInt(Movie::getRating)));
        }

        if (filters.getSort().getRating().equals(Strings.DECREASING)) {
            newList.sort((Comparator.comparingInt(Movie::getRating).reversed()));
        }

        if (filters.getSort().getRating().equals(Strings.DECREASING)
            && filters.getSort().getDuration().equals(Strings.DECREASING)) {
            newList.sort((Comparator.comparingInt(Movie::getRating).reversed()));
            newList.sort((Comparator.comparingInt(Movie::getDuration).reversed()));
        }

        if (filters.getSort().getRating().equals(Strings.INCREASING)
            && filters.getSort().getDuration().equals(Strings.DECREASING)) {

            newList.sort((Comparator.comparingInt(Movie::getRating)));
            newList.sort((Comparator.comparingInt(Movie::getDuration).reversed()));
        }

        return newList;
    }

    private Movie getMovie(final User user, final List<Movie> movies, final String name) {
        for (Movie movie : movies) {
            if (!movie.getCountriesBanned().contains(user.getCredentials().getCountry())
                && movie.getName().equals(name)) {
                return movie;
            }
        }

        return null;
    }

    private void resetNodes() {
        node1 = OBJECT_MAPPER.createObjectNode();
        node2 = OBJECT_MAPPER.createObjectNode();

        list1 = OBJECT_MAPPER.createArrayNode();
        list2 = OBJECT_MAPPER.createArrayNode();
    }
}
