package utils;

import utils.structures.Credentials;
import utils.structures.Movie;
import utils.structures.User;

import java.security.Key;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private final List<User> users = new ArrayList<>();
    private final List<Movie> movies = new ArrayList<>();

    private User loggedUser = null;
    private Movie currentMovieOnScreen = null;
    private List<Movie> currentlyFilteredMovies = null;

    private List<String> pageStack = new ArrayList<>();

    public Database() {

    }

    /**
     *
     * @return          logged user
     */
    public User getLoggedUser() {
        return loggedUser;
    }

    /**
     *
     * @param loggedUser        logged in user
     */
    public void setLoggedUser(final User loggedUser) {
        this.loggedUser = loggedUser;
    }

    /**
     *
     * @param usersList     list of users
     */
    public void addUsers(final List<User> usersList) {
        for (User user : usersList) {
            addUser(user);
        }
    }

    /**
     *
     * @return          list of users
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     *
     * @param moviesList            list of movies
     */
    public void addMovies(final List<Movie> moviesList) {
        for (Movie movie : moviesList) {
            addMovie(movie);
        }
    }

    /**
     *
     * @return          list of movies
     */
    public List<Movie> getMovies() {
        return movies;
    }

    /**
     *
     * @param user          user to be added
     */
    public void addUser(final User user) {
        users.add(user);
    }

    /**
     *
     * @param user          user to be deleted
     */
    public void deleteUser(final User user) {
        users.remove(user);
    }

    /**
     *
     * @param movie         movie to be added
     */
    public void addMovie(final Movie movie) {
        movies.add(movie);
    }

    /**
     *
     * @param movie     movie to be deleted
     */
    public void deleteMovie(final Movie movie) {
        movies.remove(movie);
    }

    /**
     *
     * @param user      user
     * @return          true if user is registered
     *                  false otherwise
     */
    public boolean isUserRegistered(final User user) {
        return users.contains(user);
    }

    /**
     *
     * @param credentials       user's credentials
     * @return                  true if there is a user registere with credentials
     *                          false otherwise
     */
    public boolean isUserWithCredentialsRegistered(final Credentials credentials) {
        for (User user : users) {
            if (user.getCredentials().getName().matches(credentials.getName())
                && user.getCredentials().getPassword().matches(credentials.getPassword())) {
                return true;
            }
        }

        return false;
    }

    /**
     *
     * @param name          username
     * @return              true if there is a user registered under name
     *                      false otherwise
     */
    public boolean isUserWithNameRegistered(final String name) {
        for (User user : users) {
            if (user.getCredentials().getName().matches(name)) {
                return true;
            }
        }

        return false;
    }

    /**
     *
     * @param credentials       user's credentials
     * @return                  true if there is a match
     *                          false otherwise
     */
    public User getUserWithCredentials(final Credentials credentials) {
        for (User user : users) {
            if (user.getCredentials().getName().matches(credentials.getName())
                    && user.getCredentials().getPassword().matches(credentials.getPassword())) {
                return user;
            }
        }

        return null;
    }

    /**
     *
     * @return      current movie displayer
     */
    public Movie getCurrentMovieOnScreen() {
        return currentMovieOnScreen;
    }

    /**
     *
     * @param currentMovieOnScreen      current movie displayer
     */
    public void setCurrentMovieOnScreen(final Movie currentMovieOnScreen) {
        this.currentMovieOnScreen = currentMovieOnScreen;
    }

    /**
     *
     * @return      the list of filtered movies
     */
    public List<Movie> getCurrentlyFilteredMovies() {
        return currentlyFilteredMovies;
    }

    /**
     *
     * @param currentlyFilteredMovies       list of filtered movies
     */
    public void setCurrentlyFilteredMovies(final List<Movie> currentlyFilteredMovies) {
        this.currentlyFilteredMovies = currentlyFilteredMovies;
    }

    /**
     *
     * @param name      movie name
     * @return          true if movie is in the filtered list
     *                  false otherwise
     */
    public Movie getElementFromFilteredMovies(final String name) {
        for (Movie movie : currentlyFilteredMovies) {
            if (movie.getName().equals(name)) {
                return movie;
            }
        }
        return null;
    }

    public Movie getMovieByName(final String name) {
        for (Movie movie : movies) {
            if (movie.getName().equals(name)) {
                return movie;
            }
        }
        return null;
    }

    public List<String> getPageStack() {
        return pageStack;
    }

    public void setPageStack(List<String> pageStack) {
        this.pageStack = pageStack;
    }
}
