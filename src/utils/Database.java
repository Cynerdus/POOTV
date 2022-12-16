package utils;

import utils.structures.Credentials;
import utils.structures.Movie;
import utils.structures.User;

import java.util.ArrayList;
import java.util.List;

public class Database {

    private final List<User> users = new ArrayList<>();
    private final List<Movie> movies = new ArrayList<>();

    private User loggedUser;

    public Database() {

    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public void addUsers(final List<User> users) {
        for (User user : users)
            addUser(user);
    }

    public List<User> getUsers() {
        return users;
    }

    public void addMovies(final List<Movie> movies) {
        for (Movie movie : movies)
            addMovie(movie);
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void addUser(final User user) {
        users.add(user);
    }

    public void deleteUser(final User user) {
        users.remove(user);
    }

    public void addMovie(final Movie movie) {
        movies.add(movie);
    }

    public void deleteMovie(final Movie movie) {
        movies.remove(movie);
    }

    public boolean isUserRegistered(final User user) {
        return users.contains(user);
    }

    public boolean isUserWithCredentialsRegistered(final Credentials credentials) {
        for (User user : users) {
            if (user.getCredentials().getName().matches(credentials.getName())
                && user.getCredentials().getPassword().matches(credentials.getPassword()))
                    return true;
        }

        return false;
    }

    public boolean isUserWithNameRegistered(final String name) {
        for (User user : users) {
            if (user.getCredentials().getName().matches(name))
                return true;
        }

        return false;
    }

    public User getUserWithCredentials(final Credentials credentials) {
        for (User user : users) {
            if (user.getCredentials().getName().matches(credentials.getName())
                    && user.getCredentials().getPassword().matches(credentials.getPassword()))
                        return user;
        }

        return null;
    }
}
