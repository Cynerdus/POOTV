package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.POJONode;
import utils.structures.Credentials;
import utils.structures.Genres;
import utils.structures.Movie;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Formatter;

public class Printer {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    public static void printHomePageData(ArrayNode output, Credentials credentials) {

        ObjectNode      node = objectMapper.createObjectNode(),
                        currentUserNode = objectMapper.createObjectNode();
        ArrayNode       movieList = objectMapper.createArrayNode();

        printUserNode(currentUserNode, credentials);

        node.set("error", null);
        node.set("currentMoviesList", movieList);
        node.set("currentUser", currentUserNode);

        output.add(node);
    }

    public static void printMovieListData(Database database, ArrayNode output, Credentials credentials) {

        ObjectNode      node = objectMapper.createObjectNode(),
                        currentUserNode = objectMapper.createObjectNode();
        ArrayNode       movieList = objectMapper.createArrayNode();

        printUserNode(currentUserNode, credentials);

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

        movieNode.put("numLikes", 0)
                 .put("rating", 0.00)
                 .put("numRatings", 0);

        moviesNode.add(movieNode);
    }

    private static void printUserNode(final ObjectNode currentUserNode, final Credentials credentials) {
        ArrayNode       purchasedMovies = objectMapper.createArrayNode(),
                        watchedMovies = objectMapper.createArrayNode(),
                        likedMovies = objectMapper.createArrayNode(),
                        ratedMovies = objectMapper.createArrayNode();

        ObjectNode      credentialsNode = objectMapper.createObjectNode();

        credentialsNode.put("name", credentials.getName())
                .put("password", credentials.getPassword())
                .put("accountType", credentials.getAccountType())
                .put("country", credentials.getCountry())
                .put("balance", credentials.getBalance() + "");

        currentUserNode.set("credentials", credentialsNode);
        currentUserNode.put("tokensCount", 0);
        currentUserNode.put("numFreePremiumMovies", 15);
        currentUserNode.set("purchasedMovies", purchasedMovies);
        currentUserNode.set("watchedMovies", watchedMovies);
        currentUserNode.set("likedMovies", likedMovies);
        currentUserNode.set("ratedMovies", ratedMovies);
    }

    private Printer() { }
}
