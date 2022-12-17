package utils.structures;

public class Credentials {

    private String name;
    private String password;
    private String accountType;
    private String country;
    private int balance;
    private int tokens;
    private int numFreePremiumMovies = 15;

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

    public void addBalance(final int balance) {
        this.balance += balance;
    }

    public void subtractBalance(final int balance) {
        this.balance -= balance;
    }
    public int getTokens() {
        return tokens;
    }

    public void setTokens(final int tokens) {
        this.tokens = tokens;
    }

    public void addTokens(final int tokens) {
        this.tokens += tokens;
    }

    public void subtractTokens(final int tokens) {
        this.tokens -= tokens;
    }

    public int getNumFreePremiumMovies() {
        return numFreePremiumMovies;
    }

    public void setNumFreePremiumMovies(final int numFreePremiumMovies) {
        this.numFreePremiumMovies = numFreePremiumMovies;
    }

    public void decrementNumFreePremiumMovies() {
        this.numFreePremiumMovies--;
    }

    public void incrementNumFreePremiumMovies() {
        this.numFreePremiumMovies++;
    }
}
