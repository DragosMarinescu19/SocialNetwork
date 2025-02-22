package Repository.Pagination;

public class Page<E> {
    private Iterable<E> elementsOnPage;
    private int totalElementsCount;

    public Page(Iterable<E> elementsOnPage, int totalElementsCount) {
        this.elementsOnPage = elementsOnPage;
        this.totalElementsCount = totalElementsCount;
    }

    public Iterable<E> getElementsOnPage() {
        return elementsOnPage;
    }

    public int getTotalElementCount() {
        return totalElementsCount;
    }
}