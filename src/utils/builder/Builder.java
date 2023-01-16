package utils.builder;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import utils.structures.Movie;
import utils.structures.Notification;
import utils.structures.User;

import java.util.List;

public interface Builder {

    ObjectNode setError(final ObjectNode node, final String error);
    ObjectNode setCurrentMoviesList(final ObjectNode node, final ArrayNode list);
    ObjectNode setCurrentUser(final ObjectNode node, final ObjectNode userNode);

    ObjectNode setMovieDetails(final ObjectNode movieNode, final Movie movie);
    ArrayNode setMovieList(final ArrayNode movieList, final List<String> list);
    ObjectNode putMovieList(final ObjectNode movieNode, final ArrayNode movieList, final String index);
    ArrayNode putMovieInList(final ArrayNode moviesList, final ObjectNode movieNode);

    ObjectNode setNotificationDetails(final ObjectNode notificationNode, final Notification notif);
    ArrayNode putNotificationInList(final ArrayNode notificationList, final ObjectNode notifNode);

    ObjectNode setUserDetails(final ObjectNode userNode, final User user);
    ArrayNode setUserMovieList(final ArrayNode movieList, final List<Movie> list);
    ArrayNode setNotificationList(final ArrayNode notificationList, final List<Notification> list);
    ObjectNode putUserList(final ObjectNode userNode, final ArrayNode listNode, final String index);
}
