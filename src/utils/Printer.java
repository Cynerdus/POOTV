package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.POJONode;
import utils.constants.Strings;
import utils.structures.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class Printer {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    public static void printHomePageData(Database database, ArrayNode output, Credentials credentials) {

        ObjectNode      node = objectMapper.createObjectNode(),
                        currentUserNode = objectMapper.createObjectNode();
        ArrayNode       movieList = objectMapper.createArrayNode();

        printUserNode(currentUserNode, database.getLoggedUser());

        node.set("error", null);
        node.set("currentMoviesList", movieList);
        node.set("currentUser", currentUserNode);

        output.add(node);
    }

    public static void printMovieListData(Database database, ArrayNode output, Credentials credentials) {

        ObjectNode      node = objectMapper.createObjectNode(),
                        currentUserNode = objectMapper.createObjectNode();
        ArrayNode       movieList = objectMapper.createArrayNode();

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

    public static void printMoviesBySearch(Database database, ArrayNode output, Credentials credentials, String startsWith) {

        ObjectNode      node = objectMapper.createObjectNode(),
                        currentUserNode = objectMapper.createObjectNode();
        ArrayNode       movieList = objectMapper.createArrayNode();

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

    public static void printMoviesByFilter(Database database, ArrayNode output, Credentials credentials, Filters filters) {

        ObjectNode      node = objectMapper.createObjectNode(),
                        currentUserNode = objectMapper.createObjectNode();
        ArrayNode       movieList = objectMapper.createArrayNode();

        printUserNode(currentUserNode, database.getLoggedUser());

        List<Movie> selectedMovies = new ArrayList<>();

        if (filters.getContains() != null) {
            for (Movie movie : database.getMovies()) {
                if (!movie.getCountriesBanned().contains(database.getLoggedUser().getCredentials().getCountry())) {

                    boolean containsActors = true;
                    for (String actor : filters.getContains().getActors())
                        if (!movie.getActors().contains(actor)) {
                            containsActors = false;
                            break;
                        }

                    boolean containsGenre = true;
                    for (String genre : filters.getContains().getGenre())
                        if (!movie.getGenres().contains(genre)) {
                            containsGenre = false;
                            break;
                        }

                    if (containsGenre && containsActors)
                        selectedMovies.add(movie);
                }
            }
        } else {
            for (Movie movie : database.getMovies()) {
                if (!movie.getCountriesBanned().contains(database.getLoggedUser().getCredentials().getCountry())) {
                    selectedMovies.add(movie);
                }
            }
        }

        /*
          defibrillation
         */

        if (Objects.equals(filters.getSort().getDuration(), " ")
            && filters.getSort().getRating().matches(Strings.INCREASING)) {
            selectedMovies.sort((Comparator.comparingInt(Movie::getRating)));
        }

        if (filters.getSort().getRating().matches(Strings.DECREASING)
            && filters.getSort().getDuration().matches(Strings.DECREASING))
            selectedMovies.sort((Comparator.comparingInt(Movie::getRating).reversed()));
            selectedMovies.sort((Comparator.comparingInt(Movie::getDuration).reversed()));

        if (filters.getSort().getRating().matches(Strings.INCREASING)
            && filters.getSort().getDuration().matches(Strings.DECREASING)) {

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
    }

    public static void printMovieFromSeeDetails(final Database database, final ArrayNode output, final Credentials credentials, final String movieName) {
        ObjectNode      node = objectMapper.createObjectNode(),
                        currentUserNode = objectMapper.createObjectNode();
        ArrayNode       movieList = objectMapper.createArrayNode();

        printUserNode(currentUserNode, database.getLoggedUser());

        for (Movie movie : database.getMovies()) {
            if (!movie.getCountriesBanned().contains(credentials.getCountry())
                    && movie.getName().matches(movieName)) {
                    addMovieToArrayNode(movieList, movie);
                    break;
            }
        }

        node.set("error", null);
        node.set("currentMoviesList", movieList);
        node.set("currentUser", currentUserNode);

        output.add(node);
    }

    public static void printDefaultError(final ArrayNode output) {
        ObjectNode      node = objectMapper.createObjectNode();
        ArrayNode       movieList = objectMapper.createArrayNode();

        node.put("error", "Error");
        node.set("currentMoviesList", movieList);
        node.set("currentUser", null);

        output.add(node);
    }

    private static void addMovieToArrayNode(ArrayNode moviesNode, Movie movie) {
        ObjectNode      movieNode = objectMapper.createObjectNode();
        ArrayNode       genresNode = objectMapper.createArrayNode(),
                        actorsNode = objectMapper.createArrayNode(),
                        countriesNode = objectMapper.createArrayNode();

        for (String genre : movie.getGenres())
            genresNode.add(genre);

        for (String actor : movie.getActors())
            actorsNode.add(actor);

        for (String country : movie.getCountriesBanned())
            countriesNode.add(country);

        movieNode.put("name", movie.getName())
                 .put("year", movie.getYear())
                 .put("duration", movie.getDuration());

        movieNode.set("genres", genresNode);
        movieNode.set("actors", actorsNode);
        movieNode.set("countriesBanned", countriesNode);

        movieNode.put("numLikes", movie.getNumLikes());

        movieNode.put("rating", (movie.getNumRatings() == 0) ? 0.00 : ((float)movie.getRating() / movie.getNumRatings()));

        movieNode.put("numRatings", movie.getNumRatings());

        moviesNode.add(movieNode);
    }

    private static void printUserNode(final ObjectNode currentUserNode, final User user) {

        Credentials credentials = user.getCredentials();

        ArrayNode purchasedMovies = objectMapper.createArrayNode(),
                watchedMovies = objectMapper.createArrayNode(),
                likedMovies = objectMapper.createArrayNode(),
                ratedMovies = objectMapper.createArrayNode();

        ObjectNode credentialsNode = objectMapper.createObjectNode();

        for (Movie movie : user.getPurchasedMovies())
            addMovieToArrayNode(purchasedMovies, movie);

        for (Movie movie : user.getWatchedMovies())
            addMovieToArrayNode(watchedMovies, movie);

        for (Movie movie : user.getLikedMovies())
            addMovieToArrayNode(likedMovies, movie);

        for (Movie movie : user.getRatedMovies())
            addMovieToArrayNode(ratedMovies, movie);

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

    }
    private Printer() { }
}
