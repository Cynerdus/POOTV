package utils.facade;

import utils.constants.FeatureNames;
import utils.constants.Numbers;
import utils.constants.PageNames;
import utils.constants.Strings;
import utils.structures.Movie;
import utils.structures.User;

public class SeeDetails extends Page {

    public SeeDetails() {
        addLegalFeatures();
        addLegalPageSwitches();
    }

    /**
     *      available pages to switch to for See Details page
     */
    @Override
    protected void addLegalPageSwitches() {
        legalPageSwitches.add(PageNames.MOVIES);
        legalPageSwitches.add(PageNames.UPGRADES);
        legalPageSwitches.add(PageNames.LOGOUT);
        legalPageSwitches.add(PageNames.SEE_DETAILS);
    }

    /**
     *      available features for See Details page
     */
    @Override
    protected void addLegalFeatures() {
        legalFeatures.add(FeatureNames.WATCH);
        legalFeatures.add(FeatureNames.PURCHASE);
        legalFeatures.add(FeatureNames.LIKE);
        legalFeatures.add(FeatureNames.RATE);
        legalFeatures.add(FeatureNames.FILTER);
        legalFeatures.add(FeatureNames.SUBSCRIBE);
    }

    /**
     *
     * @param movie         movie to be watched
     * @param user          current user
     */
    public void addMovieToWatchList(final Movie movie, final User user) {
        if (user.getPurchasedMovies().contains(movie)) {
            user.addMovieToList(user.getWatchedMovies(), movie);
        }
    }

    /**
     *
     * @param movie         movie to be purchased
     * @param user          current user
     * @return              true for success
     *                      false otherwise
     */
    public boolean addMovieToPurchasedList(final Movie movie, final User user) {
        if (!user.getPurchasedMovies().contains(movie)) {

            if (user.getCredentials().getAccountType().equals(Strings.PREMIUM)
                && user.getCredentials().getNumFreePremiumMovies() > Numbers.ZERO) {
                user.getCredentials().decrementNumFreePremiumMovies();
            } else if (user.getCredentials().getTokens() >= Numbers.TWO) {
                user.getCredentials().subtractTokens(Numbers.TWO);
            }

            user.addMovieToList(user.getPurchasedMovies(), movie);
            return true;
        }

        return false;
    }

    /**
     *
     * @param movie         movie to be liked
     * @param user          current user
     */
    public void addMovieToLikedList(final Movie movie, final User user) {
        if (user.getPurchasedMovies().contains(movie)) {
            user.addMovieToList(user.getLikedMovies(), movie);
            movie.incrementNumLikes();
        }
    }

    /**
     *
     * @param movie         movie to be added to rated list
     * @param user          current user
     * @param rating        rate proposed
     * @return              true for success
     *                      false otherwise
     */
    public boolean addMovieToRatedList(final Movie movie, final User user, final int rating) {
        if (user.getPurchasedMovies().contains(movie)) {
            if (rating < Numbers.ZERO || rating > Numbers.FIVE) {
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
