package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>{
    private Node<T> firstsentinel;
    private int size;
    private Node<T> lastsentinel;
    private class Node<T> {
        public T value;
        public Node<T> last;
        public Node<T> next;
        public Node(T v) {
            value = v;
            next = null;
            last = null;
        }
        public Node(T v, Node<T> l, Node<T> n) {
            value = v;
            last = l;
            next = n;
        }
    }
    public LinkedListDeque(){
        firstsentinel=new Node<T>(null,null,null);
        lastsentinel=new Node<T>(null,firstsentinel,firstsentinel);
        firstsentinel.next=lastsentinel;
        firstsentinel.last=lastsentinel;
        size=0;
    }

    public void addFirst(T v){
        Node<T>item=new Node<>(v,firstsentinel,firstsentinel.next);
        firstsentinel.next.last=item;
        firstsentinel.next=item;
        size+=1;
    }
    public void addLast(T v){
        Node<T>item=new Node<>(v,lastsentinel.last,lastsentinel);
        lastsentinel.last.next=item;
        lastsentinel.last=item;
        size+=1;
    }

    public boolean isEmpty(){
        return size==0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        Node<T> p=firstsentinel;
        while(p.next!=lastsentinel){
            System.out.print(p.next.value.toString());
            p=p.next;
        }
    }

    public T removeFirst(){
        if(size()==0)return null;
        T x=firstsentinel.next.value;
        firstsentinel=new Node<>(firstsentinel.value,lastsentinel,firstsentinel.next.next);
        firstsentinel.next.last=firstsentinel;
        size-=1;
        return x;
    }

    public T removeLast(){
        if(size()==0)return null;
        T x=lastsentinel.last.value;
        lastsentinel.last=lastsentinel.last.last;
        lastsentinel.last.next=lastsentinel;
        size-=1;
        return x;
    }
    public T get(int index){
        if(index>size()-1)return null;
        Node<T> p=firstsentinel;
        for(int i=0;i<=index;++i){
            p=p.next;
        }
        return p.value;
    }
    public T getRecursive(int index){
        if(index>size()-1)return null;
        if(index==0)return this.firstsentinel.next.value;
        else{
            LinkedListDeque<T> r=new LinkedListDeque<>();
            r.firstsentinel=firstsentinel.next;
            r.size=size()-1;
            r.lastsentinel=lastsentinel;
            return r.getRecursive(index-1);
        }

    }
    public Iterator<T> iterator(){
        return this.iterator();
    }

    public boolean equals(Object o){
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof LinkedListDeque ls)){
            return false;
        }


        if (((LinkedListDeque<?>) o).size() != this.size()){
            return false;
        }
        for(int i = 0; i < ((LinkedListDeque<?>) o).size(); i += 1){
            T itemFromObj =  ((LinkedListDeque<T>) o).get(i);
            T itemFromThis = this.get(i);
            if (!itemFromObj.equals(itemFromThis)){
                return false;
            }
        }
        return true;
    }

    @Override
    public void resize(int v) {

    }

}
