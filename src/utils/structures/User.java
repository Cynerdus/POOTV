package utils.structures;

import java.util.ArrayList;
import java.util.List;

public class User {

    private Credentials credentials;

    private List<Movie> watchedMovies = new ArrayList<>();
    private List<Movie> purchasedMovies = new ArrayList<>();
    private List<Movie> likedMovies = new ArrayList<>();
    private List<Movie> ratedMovies = new ArrayList<>();
    private List<Notification> notifications = new ArrayList<>();

    private List<String> subscriptions = new ArrayList<>();

    private List<Integer> ratedMoviesRates = new ArrayList<>();

    public User() { }

    /**
     *
     * @return          user information
     */
    public Credentials getCredentials() {
        return credentials;
    }

    /**
     *
     * @param credentials       user information
     */
    public void setCredentials(final Credentials credentials) {
        this.credentials = credentials;
    }

    /**
     *
     * @return          watched movies list
     */
    public List<Movie> getWatchedMovies() {
        return watchedMovies;
    }

    /**
     *
     * @param watchedMovies     watched movies list
     */
    public void setWatchedMovies(final List<Movie> watchedMovies) {
        this.watchedMovies = watchedMovies;
    }

    /**
     *
     * @return          purchased movies list
     */
    public List<Movie> getPurchasedMovies() {
        return purchasedMovies;
    }

    /**
     *
     * @param purchasedMovies       purchased movies list
     */
    public void setPurchasedMovies(final List<Movie> purchasedMovies) {
        this.purchasedMovies = purchasedMovies;
    }

    /**
     *
     * @return          liked movies list
     */
    public List<Movie> getLikedMovies() {
        return likedMovies;
    }

    /**
     *
     * @param likedMovies       liked movies list
     */
    public void setLikedMovies(final List<Movie> likedMovies) {
        this.likedMovies = likedMovies;
    }

    /**
     *
     * @return          rated movies list
     */
    public List<Movie> getRatedMovies() {
        return ratedMovies;
    }

    /**
     *
     * @param ratedMovies       rated movies list
     */
    public void setRatedMovies(final List<Movie> ratedMovies) {
        this.ratedMovies = ratedMovies;
    }

    /**
     *
     * @param movieList     general list
     * @param movie         movie to be added
     */
    public void addMovieToList(final List<Movie> movieList, final Movie movie) {
        movieList.add(movie);
    }

    /**
     *
     * @return      notification list
     */
    public List<Notification> getNotifications() {
        return notifications;
    }

    /**
     *
     * @param notifications     notification queue
     */
    public void setNotifications(final List<Notification> notifications) {
        this.notifications = notifications;
    }

    /**
     *
     * @return      user subscriptions
     */
    public List<String> getSubscriptions() {
        return subscriptions;
    }

    /**
     *
     * @param subscriptions     user subscriptions
     */
    public void setSubscriptions(final List<String> subscriptions) {
        this.subscriptions = subscriptions;
    }

    /**
     *
     * @return      registered movies paired with list of rates
     */
    public List<Integer> getRatedMoviesRates() {
        return ratedMoviesRates;
    }

    /**
     *
     * @param ratedMoviesRates      registered rates paired with list of movies
     */
    public void setRatedMoviesRates(final List<Integer> ratedMoviesRates) {
        this.ratedMoviesRates = ratedMoviesRates;
    }
}
