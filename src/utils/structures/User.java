package utils.structures;

import utils.structures.Credentials;

import java.util.ArrayList;
import java.util.List;

public class User {

    private Credentials credentials;

    private List<Movie> watchedMovies = new ArrayList<>();
    private List<Movie> purchasedMovies = new ArrayList<>();
    private List<Movie> likedMovies = new ArrayList<>();
    private List<Movie> ratedMovies = new ArrayList<>();

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

    public List<Movie> getWatchedMovies() {
        return watchedMovies;
    }

    public void setWatchedMovies(final List<Movie> watchedMovies) {
        this.watchedMovies = watchedMovies;
    }

    public List<Movie> getPurchasedMovies() {
        return purchasedMovies;
    }

    public void setPurchasedMovies(final List<Movie> purchasedMovies) {
        this.purchasedMovies = purchasedMovies;
    }

    public List<Movie> getLikedMovies() {
        return likedMovies;
    }

    public void setLikedMovies(final List<Movie> likedMovies) {
        this.likedMovies = likedMovies;
    }

    public List<Movie> getRatedMovies() {
        return ratedMovies;
    }

    public void setRatedMovies(final List<Movie> ratedMovies) {
        this.ratedMovies = ratedMovies;
    }

    public void addMovieToList(final List<Movie> movieList, final Movie movie) {
        movieList.add(movie);
    }
}
