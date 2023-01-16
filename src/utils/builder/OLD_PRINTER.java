package utils.builder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import utils.Database;
import utils.constants.Strings;
import utils.structures.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public final class OLD_PRINTER {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     *
     * @param database          application's database for movies and users
     * @param output            output node in .json
     * @param credentials       current user's credentials
     */
    public static void printHomePageData(final Database database,
                                         final ArrayNode output,
                                         final Credentials credentials) {

        ObjectNode      node = OBJECT_MAPPER.createObjectNode(),
                        currentUserNode = OBJECT_MAPPER.createObjectNode();
        ArrayNode       movieList = OBJECT_MAPPER.createArrayNode();

        printUserNode(currentUserNode, database.getLoggedUser());

        node.set("error", null);
        node.set("currentMoviesList", movieList);
        node.set("currentUser", currentUserNode);

        output.add(node);
    }

    /**
     *
     * @param database          application's database for movies and users
     * @param output            output node in .json
     * @param credentials       current user's credentials
     */
    public static void printMovieListData(final Database database,
                                          final ArrayNode output, final
                                          Credentials credentials) {

        ObjectNode      node = OBJECT_MAPPER.createObjectNode(),
                        currentUserNode = OBJECT_MAPPER.createObjectNode();
        ArrayNode       movieList = OBJECT_MAPPER.createArrayNode();

        printUserNode(currentUserNode, database.getLoggedUser());

        for (Movie movie : database.getMovies()) {
            if (!movie.getCountriesBanned().contains(credentials.getCountry())) {
                addMovieToArrayNode(movieList, movie);
            }
        }

        node.set("error", null);
        node.set("currentMoviesList", movieList);
        node.set("currentUser", currentUserNode);

        output.add(node);
    }

    /**
     *
     * @param database          application's database for movies and users
     * @param output            output node in .json
     * @param credentials       current user's credentials
     * @param startsWith        prefix string for movie search
     */
    public static void printMoviesBySearch(final Database database,
                                           final ArrayNode output,
                                           final Credentials credentials,
                                           final String startsWith) {

        ObjectNode      node = OBJECT_MAPPER.createObjectNode(),
                        currentUserNode = OBJECT_MAPPER.createObjectNode();
        ArrayNode       movieList = OBJECT_MAPPER.createArrayNode();

        printUserNode(currentUserNode, database.getLoggedUser());

        for (Movie movie : database.getMovies()) {
            if (!movie.getCountriesBanned().contains(credentials.getCountry())
                && movie.getName().startsWith(startsWith)) {
                    addMovieToArrayNode(movieList, movie);
            }
        }

        node.set("error", null);
        node.set("currentMoviesList", movieList);
        node.set("currentUser", currentUserNode);

        output.add(node);
    }

    /**
     *
     * @param database          application's database for movies and users
     * @param output            output node in .json
     * @param credentials       current user's credentials
     * @param filters           filters to be applied on movie list
     * @return                  the resulted filtered movie list
     */
    public static List<Movie> printMoviesByFilter(final Database database,
                                                  final ArrayNode output,
                                                  final Credentials credentials,
                                                  final Filters filters) {

        ObjectNode      node = OBJECT_MAPPER.createObjectNode(),
                        currentUserNode = OBJECT_MAPPER.createObjectNode();
        ArrayNode       movieList = OBJECT_MAPPER.createArrayNode();

        printUserNode(currentUserNode, database.getLoggedUser());

        List<Movie> selectedMovies = new ArrayList<>();
        for (Movie movie : database.getMovies()) {
            String country = database.getLoggedUser().getCredentials().getCountry();
            if (!movie.getCountriesBanned().contains(country)) {

                boolean containsActors = true;
                for (String actor : filters.getContains().getActors()) {
                    if (!movie.getActors().contains(actor)) {
                        containsActors = false;
                        break;
                    }
                }

                boolean containsGenre = true;
                for (String genre : filters.getContains().getGenre()) {
                    if (!movie.getGenres().contains(genre)) {
                        containsGenre = false;
                        break;
                    }
                }

                if (containsGenre && containsActors) {
                    selectedMovies.add(movie);
                }
            }
        }

        /*
          defibrillation
         */

        if (Objects.equals(filters.getSort().getDuration(), " ")
            && filters.getSort().getRating().equals(Strings.INCREASING)) {
            selectedMovies.sort((Comparator.comparingInt(Movie::getRating)));
        }

        if (filters.getSort().getRating().equals(Strings.DECREASING)) {
            selectedMovies.sort((Comparator.comparingInt(Movie::getRating).reversed()));
        }

        if (filters.getSort().getRating().equals(Strings.DECREASING)
            && filters.getSort().getDuration().equals(Strings.DECREASING)) {
            selectedMovies.sort((Comparator.comparingInt(Movie::getRating).reversed()));
            selectedMovies.sort((Comparator.comparingInt(Movie::getDuration).reversed()));
        }

        if (filters.getSort().getRating().equals(Strings.INCREASING)
            && filters.getSort().getDuration().equals(Strings.DECREASING)) {

            selectedMovies.sort((Comparator.comparingInt(Movie::getRating)));
            selectedMovies.sort((Comparator.comparingInt(Movie::getDuration).reversed()));
        }

        for (Movie movie : selectedMovies) {
            addMovieToArrayNode(movieList, movie);
        }

        node.set("error", null);
        node.set("currentMoviesList", movieList);
        node.set("currentUser", currentUserNode);

        output.add(node);

        return selectedMovies;
    }

    /**
     *
     * @param database          application's database for movies and users
     * @param output            output node in .json
     * @param credentials       current user's credentials
     * @param movieName         target movie name
     */
    public static void printMovieFromSeeDetails(final Database database,
                                                final ArrayNode output,
                                                final Credentials credentials,
                                                final String movieName) {

        ObjectNode      node = OBJECT_MAPPER.createObjectNode(),
                        currentUserNode = OBJECT_MAPPER.createObjectNode();
        ArrayNode       movieList = OBJECT_MAPPER.createArrayNode();

        printUserNode(currentUserNode, database.getLoggedUser());

        for (Movie movie : database.getMovies()) {
            if (!movie.getCountriesBanned().contains(credentials.getCountry())
                    && movie.getName().equals(movieName)) {
                    addMovieToArrayNode(movieList, movie);
                    break;
            }
        }

        node.set("error", null);
        node.set("currentMoviesList", movieList);
        node.set("currentUser", currentUserNode);

        output.add(node);
    }

    /**
     *
     * @param output        the output node
     */
    public static void printDefaultError(final ArrayNode output) {
        ObjectNode      node = OBJECT_MAPPER.createObjectNode();
        ArrayNode       movieList = OBJECT_MAPPER.createArrayNode();

        node.put("error", "Error");
        node.set("currentMoviesList", movieList);
        node.set("currentUser", null);

        output.add(node);
    }
    public static void printDefaultErrorWithCredentials(final Database database, final ArrayNode output) {
        ObjectNode      node = OBJECT_MAPPER.createObjectNode();
        ArrayNode       movieList = OBJECT_MAPPER.createArrayNode();

        node.put("error", "Error");
        node.set("currentMoviesList", movieList);
        if (database.getLoggedUser() == null)
            node.set("currentUser", null);
        else {
            ObjectNode currentUserNode = OBJECT_MAPPER.createObjectNode();
            printUserNode(currentUserNode, database.getLoggedUser());
            node.set("currentUser", currentUserNode);
        }

        output.add(node);
    }

    private static void addMovieToArrayNode(final ArrayNode moviesNode, final Movie movie) {
        ObjectNode      movieNode = OBJECT_MAPPER.createObjectNode();
        ArrayNode       genresNode = OBJECT_MAPPER.createArrayNode(),
                        actorsNode = OBJECT_MAPPER.createArrayNode(),
                        countriesNode = OBJECT_MAPPER.createArrayNode();

        for (String genre : movie.getGenres()) {
            genresNode.add(genre);
        }

        for (String actor : movie.getActors()) {
            actorsNode.add(actor);
        }

        for (String country : movie.getCountriesBanned()) {
            countriesNode.add(country);
        }

        movieNode.put("name", movie.getName())
                 .put("year", movie.getYear() + "")
                 .put("duration", movie.getDuration());

        movieNode.set("genres", genresNode);
        movieNode.set("actors", actorsNode);
        movieNode.set("countriesBanned", countriesNode);

        movieNode.put("numLikes", movie.getNumLikes());

        movieNode.put("rating", (movie.getNumRatings() == 0) ? 0.00
                                        : ((float) movie.getRating() / movie.getNumRatings()));

        movieNode.put("numRatings", movie.getNumRatings());

        moviesNode.add(movieNode);
    }

    private static void addNotificationToList(final ArrayNode notifications, final Notification notification) {
        ObjectNode notificationNode = OBJECT_MAPPER.createObjectNode();

        notificationNode.put("movieName", notification.getMovieName());
        notificationNode.put("message", notification.getMessage());

        notifications.add(notificationNode);
    }

    private static void printUserNode(final ObjectNode currentUserNode, final User user) {

        Credentials credentials = user.getCredentials();

        ArrayNode purchasedMovies = OBJECT_MAPPER.createArrayNode(),
                watchedMovies = OBJECT_MAPPER.createArrayNode(),
                likedMovies = OBJECT_MAPPER.createArrayNode(),
                ratedMovies = OBJECT_MAPPER.createArrayNode(),
                notifications = OBJECT_MAPPER.createArrayNode();

        ObjectNode credentialsNode = OBJECT_MAPPER.createObjectNode();

        for (Movie movie : user.getPurchasedMovies()) {
            addMovieToArrayNode(purchasedMovies, movie);
        }

        for (Movie movie : user.getWatchedMovies()) {
            addMovieToArrayNode(watchedMovies, movie);
        }

        for (Movie movie : user.getLikedMovies()) {
            addMovieToArrayNode(likedMovies, movie);
        }

        for (Movie movie : user.getRatedMovies()) {
            addMovieToArrayNode(ratedMovies, movie);
        }

        for (Notification notification : user.getNotifications()) {
            addNotificationToList(notifications, notification);
        }

        credentialsNode.put("name", credentials.getName())
                .put("password", credentials.getPassword())
                .put("accountType", credentials.getAccountType())
                .put("country", credentials.getCountry())
                .put("balance", credentials.getBalance() + "");


        currentUserNode.set("credentials", credentialsNode);
        currentUserNode.put("tokensCount", credentials.getTokens());
        currentUserNode.put("numFreePremiumMovies", credentials.getNumFreePremiumMovies());
        currentUserNode.set("purchasedMovies", purchasedMovies);
        currentUserNode.set("watchedMovies", watchedMovies);
        currentUserNode.set("likedMovies", likedMovies);
        currentUserNode.set("ratedMovies", ratedMovies);
        currentUserNode.set("notifications", notifications);

    }

    public static void printPremiumNotifications(final Database database,
                                                final ArrayNode output,
                                                final Credentials credentials) {

        ObjectNode      node = OBJECT_MAPPER.createObjectNode(),
                        currentUserNode = OBJECT_MAPPER.createObjectNode();

        printUserNode(currentUserNode, database.getLoggedUser());

        node.set("error", null);
        node.set("currentMoviesList", null);
        node.set("currentUser", currentUserNode);

        output.add(node);
    }

    private OLD_PRINTER() { }
}
