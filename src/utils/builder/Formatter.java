package utils.builder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import utils.structures.Movie;
import utils.structures.Notification;
import utils.structures.User;

import java.util.ArrayList;
import java.util.List;

public class Formatter implements Builder {

    private ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public ObjectNode setError(ObjectNode node, String error) {
        node.put("error", error);

        return node;
    }

    @Override
    public ObjectNode setCurrentMoviesList(ObjectNode node, ArrayNode list) {
        node.set("currentMoviesList", list);

        return node;
    }

    @Override
    public ObjectNode setCurrentUser(ObjectNode node, ObjectNode userNode) {
        node.set("currentUser", userNode);

        return node;
    }

    @Override
    public ObjectNode setMovieDetails(ObjectNode movieNode, Movie movie) {
        movieNode.put("name", movie.getName())
                .put("year", movie.getYear() + "")
                .put("duration", movie.getDuration());

        movieNode.put("numLikes", movie.getNumLikes());

        movieNode.put("rating", (movie.getNumRatings() == 0) ? 0.00
                : ((float) movie.getRating() / movie.getNumRatings()));

        movieNode.put("numRatings", movie.getNumRatings());

        return movieNode;
    }

    @Override
    public ArrayNode setMovieList(ArrayNode movieList, List<String> list) {
        for (String item : list) {
            movieList.add(item);
        }

        return movieList;
    }

    @Override
    public ObjectNode putMovieList(ObjectNode movieNode, ArrayNode movieList, String index) {
        movieNode.set(index, movieList);

        return movieNode;
    }

    @Override
    public ArrayNode putMovieInList(ArrayNode moviesList, ObjectNode movieNode) {
        moviesList.add(movieNode);

        return moviesList;
    }

    @Override
    public ObjectNode setNotificationDetails(ObjectNode notificationNode, Notification notif) {
        notificationNode.put("movieName", notif.getMovieName());
        notificationNode.put("message", notif.getMessage());

        return notificationNode;
    }

    @Override
    public ArrayNode putNotificationInList(ArrayNode notificationList, ObjectNode notifNode) {
        notificationList.add(notifNode);

        return notificationList;
    }

    @Override
    public ObjectNode setUserDetails(ObjectNode userNode, User user) {
        ObjectNode credentials = OBJECT_MAPPER.createObjectNode();

        credentials.put("name", user.getCredentials().getName())
                .put("password", user.getCredentials().getPassword())
                .put("accountType", user.getCredentials().getAccountType())
                .put("country", user.getCredentials().getCountry())
                .put("balance", user.getCredentials().getBalance()+ "");

        userNode.set("credentials", credentials);
        userNode.put("tokensCount", user.getCredentials().getTokens());
        userNode.put("numFreePremiumMovies", user.getCredentials().getNumFreePremiumMovies());

        return userNode;
    }

    @Override
    public ArrayNode setUserMovieList(ArrayNode movieList, List<Movie> list) {
        for (Movie movie : list) {
            ObjectNode movieNode = OBJECT_MAPPER.createObjectNode();
            ArrayNode strings;
            setMovieDetails(movieNode, movie);

            strings = setMovieList(OBJECT_MAPPER.createArrayNode(), movie.getGenres());
            putMovieList(movieNode, strings, "genres");

            strings = setMovieList(OBJECT_MAPPER.createArrayNode(), movie.getActors());
            putMovieList(movieNode, strings, "actors");

            strings = setMovieList(OBJECT_MAPPER.createArrayNode(), movie.getCountriesBanned());
            putMovieList(movieNode, strings, "countriesBanned");

            movieList.add(movieNode);
        }

        return movieList;
    }

    @Override
    public ArrayNode setNotificationList(ArrayNode notificationList, List<Notification> list) {
        for (Notification notif : list) {
            notificationList.add(setNotificationDetails(OBJECT_MAPPER.createObjectNode(), notif));
        }

        return notificationList;
    }

    @Override
    public ObjectNode putUserList(ObjectNode userNode, ArrayNode listNode, String index) {
        userNode.set(index, listNode);

        return userNode;
    }
}
