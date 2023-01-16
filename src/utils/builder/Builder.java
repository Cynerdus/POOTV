package utils.builder;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import utils.structures.Movie;
import utils.structures.Notification;
import utils.structures.User;

import java.util.List;

public interface Builder {

    ObjectNode setError(ObjectNode node, String error);
    ObjectNode setCurrentMoviesList(ObjectNode node, ArrayNode list);
    ObjectNode setCurrentUser(ObjectNode node, ObjectNode userNode);

    ObjectNode setMovieDetails(ObjectNode movieNode, Movie movie);
    ArrayNode setMovieList(ArrayNode movieList, List<String> list);
    ObjectNode putMovieList(ObjectNode movieNode, ArrayNode movieList, String index);
    ArrayNode putMovieInList(ArrayNode moviesList, ObjectNode movieNode);

    ObjectNode setNotificationDetails(ObjectNode notificationNode, Notification notif);
    ArrayNode putNotificationInList(ArrayNode notificationList, ObjectNode notifNode);

    ObjectNode setUserDetails(ObjectNode userNode, User user);
    ArrayNode setUserMovieList(ArrayNode movieList, List<Movie> list);
    ArrayNode setNotificationList(ArrayNode notificationList, List<Notification> list);
    ObjectNode putUserList(ObjectNode userNode, ArrayNode listNode, String index);
}
