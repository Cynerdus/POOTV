package utils.facade.command;

import com.fasterxml.jackson.databind.node.ArrayNode;
import utils.builder.Printer;
import utils.constants.FeatureNames;
import utils.constants.Strings;
import utils.structures.Action;
import utils.structures.Movie;
import utils.structures.Notification;
import utils.structures.User;

import java.util.ArrayList;
import java.util.List;

public class Database implements Command {

    private utils.Database database;
    private ArrayNode outputData;

    private Action action;

    private final Printer printer = new Printer();

    /**
     *
     * @param outputData1       output node for JSON parsing
     * @param database1         general database
     */
    public void setData(final ArrayNode outputData1, final utils.Database database1) {
        outputData = outputData1;
        database = database1;
    }

    /**
     *
     * @param action        current action
     */
    public void setAction(final Action action) {
        this.action = action;
    }

    @Override
    public void process() {

    }

    /**
     *  command execution
     */
    @Override
    public void execute() {
        if (action.getFeature().matches(FeatureNames.ADD)) {
            if (database.getMovieByName(action.getAddedMovie().getName()) != null) {
                printer.printDefaultErrorWithCredentials(database.getLoggedUser(), outputData);
            }

            Movie newMovie = new Movie();
            newMovie.setName(action.getAddedMovie().getName());
            newMovie.setYear(action.getAddedMovie().getYear());
            newMovie.setDuration(action.getAddedMovie().getDuration());

            ArrayList<String> genres, actors, countriesBanned;
            genres = new ArrayList<>(action.getAddedMovie().getGenres());
            actors = new ArrayList<>(action.getAddedMovie().getActors());
            countriesBanned = new ArrayList<>(action.getAddedMovie().getCountriesBanned());

            newMovie.setGenres(genres);
            newMovie.setActors(actors);
            newMovie.setCountriesBanned(countriesBanned);

            database.addMovie(newMovie);
            notifyUsers(newMovie.getGenres(), newMovie.getName(), Strings.ADD);
        } else { /* delete */
            Movie movie = database.getMovieByName(action.getDeletedMovie());
            if (movie == null) {
                printer.printDefaultError(outputData);
            } else {
                database.getMovies().remove(movie);
            }
        }
    }

    private void notifyUsers(final List<String> genres,
                             final String movieName,
                             final String message) {

        List<User> userList = database.getUsers();
        for (User user : userList) {
            for (String genre : genres) {
                if (user.getSubscriptions().contains(genre)) {
                    Notification notification = new Notification();
                    notification.setMovieName(movieName);
                    notification.setMessage(message);
                    user.getNotifications().add(notification);
                }
            }
        }
    }
}
