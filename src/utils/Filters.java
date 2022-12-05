package utils;

public class Filters {

    private Sort sort;
    private Contains contains;

    public Filters() { }

    /**
     *
     * @return      sort option
     */
    public Sort getSort() {
        return sort;
    }

    /**
     *
     * @param sort      option
     */
    public void setSort(final Sort sort) {
        this.sort = sort;
    }

    /**
     *
     * @return      filter requirements
     */
    public Contains getContains() {
        return contains;
    }

    /**
     *
     * @param contains      filter requirements
     */
    public void setContains(final Contains contains) {
        this.contains = contains;
    }
}
