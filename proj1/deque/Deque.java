package deque;

public interface Deque<T> {
    public void addFirst(T v);

    public void addLast(T item);

    public boolean isEmpty();

    public int size();

    public T removeFirst();

    public T removeLast();

    public T get(int index);

    public void printDeque();

}
