package utils.facade;

import utils.Printer;
import utils.constants.FeatureNames;
import utils.constants.PageNames;
import utils.constants.Strings;
import utils.structures.Movie;
import utils.structures.User;

public class SeeDetails extends Page{

    public SeeDetails() {
        addLegalFeatures();
        addLegalPageSwitches();
    }

    @Override
    protected void addLegalPageSwitches() {
        legalPageSwitches.add(PageNames.MOVIES);
        legalPageSwitches.add(PageNames.UPGRADES);
        legalPageSwitches.add(PageNames.LOGOUT);
        legalPageSwitches.add(PageNames.SEE_DETAILS);
    }

    @Override
    protected void addLegalFeatures() {
        legalFeatures.add(FeatureNames.WATCH);
        legalFeatures.add(FeatureNames.PURCHASE);
        legalFeatures.add(FeatureNames.LIKE);
        legalFeatures.add(FeatureNames.RATE);
        legalFeatures.add(FeatureNames.FILTER);
    }

    public void addMovieToWatchList(final Movie movie, final User user) {
        if (user.getPurchasedMovies().contains(movie))
            user.addMovieToList(user.getWatchedMovies(), movie);
    }

    public boolean addMovieToPurchasedList(final Movie movie, final User user) {
        if (!user.getPurchasedMovies().contains(movie)) {

            if (user.getCredentials().getAccountType().equals(Strings.PREMIUM)
                && user.getCredentials().getNumFreePremiumMovies() > 0)
                user.getCredentials().decrementNumFreePremiumMovies();
            else if (user.getCredentials().getTokens() >= 2) {
                user.getCredentials().subtractTokens(2);
            }

            user.addMovieToList(user.getPurchasedMovies(), movie);
            return true;
        }

        return false;
    }

    public void addMovieToLikedList(final Movie movie, final User user) {
        if (user.getPurchasedMovies().contains(movie)) {
            user.addMovieToList(user.getLikedMovies(), movie);
            movie.incrementNumLikes();
        }
    }

    public boolean addMovieToRatedList(final Movie movie, final User user, final int rating) {
        if (user.getPurchasedMovies().contains(movie)) {
            if (rating < 0 || rating > 5) {
                return false;
            }

            user.addMovieToList(user.getRatedMovies(), movie);
            movie.setRating(movie.getRating() + rating);
            movie.incrementNumRatings();
            return true;
        }
        return false;
    }
}
