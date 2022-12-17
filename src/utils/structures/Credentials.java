package utils.structures;

import utils.constants.Numbers;

public class Credentials {

    private String name;
    private String password;
    private String accountType;
    private String country;
    private int balance;
    private int tokens;
    private int numFreePremiumMovies = Numbers.FIFTEEN;

    public Credentials() { }

    /**
     *
     * @return      User name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name      of the User
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     *
     * @return      the User password
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password      User password
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     *
     * @return      account type
     */
    public String getAccountType() {
        return accountType;
    }

    /**
     *
     * @param accountType       type of account
     */
    public void setAccountType(final String accountType) {
        this.accountType = accountType;
    }

    /**
     *
     * @return      the registered country
     */
    public String getCountry() {
        return country;
    }

    /**
     *
     * @param country       registered country
     */
    public void setCountry(final String country) {
        this.country = country;
    }

    /**
     *
     * @return      current available currency
     */
    public int getBalance() {
        return balance;
    }

    /**
     *
     * @param balance       available currency
     */
    public void setBalance(final int balance) {
        this.balance = balance;
    }

    /**
     *
     * @param plusBalance       currency
     */
    public void addBalance(final int plusBalance) {
        this.balance += plusBalance;
    }

    /**
     *
     * @param subBalance       currency
     */
    public void subtractBalance(final int subBalance) {
        this.balance -= subBalance;
    }

    /**
     *
     * @return      number of tokens
     */
    public int getTokens() {
        return tokens;
    }

    /**
     *
     * @param tokens        number of tokens
     */
    public void setTokens(final int tokens) {
        this.tokens = tokens;
    }

    /**
     *
     * @param plusTokens        number of tokens to be added
     */
    public void addTokens(final int plusTokens) {
        this.tokens += plusTokens;
    }

    /**
     *
     * @param subTokens        number of tokens to be subtracted
     */
    public void subtractTokens(final int subTokens) {
        this.tokens -= subTokens;
    }

    /**
     *
     * @return      number of free movies
     */
    public int getNumFreePremiumMovies() {
        return numFreePremiumMovies;
    }

    /**
     *
     * @param numFreePremiumMovies      number of free movies
     */
    public void setNumFreePremiumMovies(final int numFreePremiumMovies) {
        this.numFreePremiumMovies = numFreePremiumMovies;
    }

    /**
     *      decrements the number of free movies on premium account
     */
    public void decrementNumFreePremiumMovies() {
        this.numFreePremiumMovies--;
    }

    /**
     *      increments the number of free movies on premium account
     */
    public void incrementNumFreePremiumMovies() {
        this.numFreePremiumMovies++;
    }
}
