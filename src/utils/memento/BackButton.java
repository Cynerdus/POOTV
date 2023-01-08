package utils.memento;

import java.util.ArrayList;
import java.util.List;

public class BackButton {
    private final List<Memento> pageQueue = new ArrayList<>();

    public void addPage(Memento pageName) {
        pageQueue.add(pageName);
    }

    public Memento getPage(int index) {
        return pageQueue.get(index);
    }

    public void removeLastPage() {
        if (pageQueue.size() > 0) {
            pageQueue.remove(pageQueue.size() - 1);
        }
    }

    public int getPageQueueSize() {
        return pageQueue.size();
    }
}
