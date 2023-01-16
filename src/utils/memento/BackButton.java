package utils.memento;

import java.util.ArrayList;
import java.util.List;

public class BackButton {
    private final List<Memento> pageQueue = new ArrayList<>();

    /**
     *
     * @param pageName      page to be added to queue
     */
    public void addPage(final Memento pageName) {
        pageQueue.add(pageName);
    }

    /**
     *
     * @param index     index for page / state
     * @return          the page
     */
    public Memento getPage(final int index) {
        return pageQueue.get(index);
    }

    /**
     *  removes last element of pageQueue
     */
    public void removeLastPage() {
        if (pageQueue.size() > 0) {
            pageQueue.remove(pageQueue.size() - 1);
        }
    }

    /**
     *
     * @return      size of page queue
     */
    public int getPageQueueSize() {
        return pageQueue.size();
    }

    /**
     *
     * @return      last page name or null
     */
    public String getLastPage() {
        if (pageQueue.size() > 1) {
            return pageQueue.get(pageQueue.size() - 1).getState();
        }

        return "";
    }
}
