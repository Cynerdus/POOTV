package utils.builder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import utils.structures.Movie;
import utils.structures.Notification;
import utils.structures.User;

import java.util.List;

public class Formatter implements Builder {

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     *
     * @param node      general node
     * @param error     error to parse
     * @return          updated node
     */
    @Override
    public ObjectNode setError(final ObjectNode node, final String error) {
        node.put("error", error);

        return node;
    }

    /**
     *
     * @param node          general node
     * @param list          movie list node
     * @return              updated node
     */
    @Override
    public ObjectNode setCurrentMoviesList(final ObjectNode node,
                                           final ArrayNode list) {

        node.set("currentMoviesList", list);

        return node;
    }

    /**
     *
     * @param node          general node
     * @param userNode      user node
     * @return              updated general node
     */
    @Override
    public ObjectNode setCurrentUser(final ObjectNode node, final ObjectNode userNode) {
        node.set("currentUser", userNode);

        return node;
    }

    /**
     *
     * @param movieNode     movie node
     * @param movie         movie to set details
     * @return              updated movie node
     */
    @Override
    public ObjectNode setMovieDetails(final ObjectNode movieNode, final Movie movie) {
        movieNode.put("name", movie.getName())
                .put("year", movie.getYear() + "")
                .put("duration", movie.getDuration());

        movieNode.put("numLikes", movie.getNumLikes());

        movieNode.put("rating", (movie.getNumRatings() == 0) ? 0.00
                : ((float) movie.getRating() / movie.getNumRatings()));

        movieNode.put("numRatings", movie.getNumRatings());

        return movieNode;
    }

    /**
     *
     * @param movieList     movie list node
     * @param list          genres / actors / countries banned list
     * @return              updated list node
     */
    @Override
    public ArrayNode setMovieList(final ArrayNode movieList, final List<String> list) {
        for (String item : list) {
            movieList.add(item);
        }

        return movieList;
    }

    /**
     *
     * @param movieNode     movie node
     * @param movieList     movie list
     * @param index         string to name the JSON node
     * @return              updated node
     */
    @Override
    public ObjectNode putMovieList(final ObjectNode movieNode,
                                   final ArrayNode movieList,
                                   final String index) {
        movieNode.set(index, movieList);

        return movieNode;
    }

    /**
     *
     * @param moviesList        movie list node
     * @param movieNode         movie node
     * @return                  updated list node
     */
    @Override
    public ArrayNode putMovieInList(final ArrayNode moviesList,
                                    final ObjectNode movieNode) {
        moviesList.add(movieNode);

        return moviesList;
    }

    /**
     *
     * @param notificationNode      notification node
     * @param notif                 notification
     * @return                      updated node
     */
    @Override
    public ObjectNode setNotificationDetails(final ObjectNode notificationNode,
                                             final Notification notif) {
        notificationNode.put("movieName", notif.getMovieName());
        notificationNode.put("message", notif.getMessage());

        return notificationNode;
    }

    /**
     *
     * @param notificationList      notification list node
     * @param notifNode             notification node
     * @return                      updated node
     */
    @Override
    public ArrayNode putNotificationInList(final ArrayNode notificationList,
                                           final ObjectNode notifNode) {
        notificationList.add(notifNode);

        return notificationList;
    }

    /**
     *
     * @param userNode      user node
     * @param user          current user
     * @return              updated node
     */
    @Override
    public ObjectNode setUserDetails(final ObjectNode userNode,
                                     final User user) {

        ObjectNode credentials = objectMapper.createObjectNode();

        credentials.put("name", user.getCredentials().getName())
                .put("password", user.getCredentials().getPassword())
                .put("accountType", user.getCredentials().getAccountType())
                .put("country", user.getCredentials().getCountry())
                .put("balance", user.getCredentials().getBalance() + "");

        userNode.set("credentials", credentials);
        userNode.put("tokensCount", user.getCredentials().getTokens());
        userNode.put("numFreePremiumMovies", user.getCredentials().getNumFreePremiumMovies());

        return userNode;
    }

    /**
     *
     * @param movieList         movie list node
     * @param list              current list
     * @return                  updated node
     */
    @Override
    public ArrayNode setUserMovieList(final ArrayNode movieList,
                                      final List<Movie> list) {

        for (Movie movie : list) {
            ObjectNode movieNode = objectMapper.createObjectNode();
            ArrayNode strings;
            setMovieDetails(movieNode, movie);

            strings = setMovieList(objectMapper.createArrayNode(), movie.getGenres());
            putMovieList(movieNode, strings, "genres");

            strings = setMovieList(objectMapper.createArrayNode(), movie.getActors());
            putMovieList(movieNode, strings, "actors");

            strings = setMovieList(objectMapper.createArrayNode(), movie.getCountriesBanned());
            putMovieList(movieNode, strings, "countriesBanned");

            movieList.add(movieNode);
        }

        return movieList;
    }

    /**
     *
     * @param notificationList      node for notification list
     * @param list                  current list
     * @return                      updated node list
     */
    @Override
    public ArrayNode setNotificationList(final ArrayNode notificationList,
                                         final List<Notification> list) {
        for (Notification notif : list) {
            notificationList.add(setNotificationDetails(objectMapper.createObjectNode(), notif));
        }

        return notificationList;
    }

    /**
     *
     * @param userNode      user node
     * @param listNode      general list node
     * @param index         string to name the JSON node
     * @return              modified user node
     */
    @Override
    public ObjectNode putUserList(final ObjectNode userNode,
                                  final ArrayNode listNode,
                                  final String index) {

        userNode.set(index, listNode);

        return userNode;
    }
}
