package utils;

import java.util.ArrayList;

public class Input {

    private ArrayList<User> users;

    private ArrayList<Movie> movies;

    private ArrayList<Action> actions;

    public Input() { }

    /**
     *
     * @return      list of users
     */
    public ArrayList<User> getUsers() {
        return users;
    }

    /**
     *
     * @param users     list of users
     */
    public void setUsers(final ArrayList<User> users) {
        this.users = users;
    }

    /**
     *
     * @return      list of available movies
     */
    public ArrayList<Movie> getMovies() {
        return movies;
    }

    /**
     *
     * @param movies        list of available movies
     */
    public void setMovies(final ArrayList<Movie> movies) {
        this.movies = movies;
    }

    /**
     *
     * @return      list of commands
     */
    public ArrayList<Action> getActions() {
        return actions;
    }

    /**
     *
     * @param actions       list of commands
     */
    public void setActions(final ArrayList<Action> actions) {
        this.actions = actions;
    }
}
