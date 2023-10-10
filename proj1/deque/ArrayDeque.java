package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>{
    private T[] array;
    private int size;
    public ArrayDeque(){
        size=0;
        array=(T[])new Object[10];
    }
    public void resize(int capability){
        T[]a=(T[])new Object[capability];
        System.arraycopy(array,0,a,0,size);
        array=a;
    }
    public void addFirst(T item){
        if(array.length<=size){
            resize((size+1)*2);
        }

        T[] a=(T[])new Object[size+1];
        System.arraycopy(array,0,a,1,size);
        size+=1;
        a[0]=item;
        array=a;
    }
    public void addLast(T item){
        if(array.length==size){
            resize(size*2);
        }
        array[size]=item;
        size+=1;
    }
    public boolean isEmpty(){
        return size==0;
    }
    public int size(){
        return size;
    }
    public void printDeque(){
        for(int i=0;i<size;++i){
            System.out.print(array[i].toString());
        }
    }
    public T removeFirst(){
        T x=array[0];
        T[] a=(T[])new Object[size-1];
        System.arraycopy(array,0,a,0,size-1);
        array=a;
        return x;
    }
    public T removeLast(){
        T x=array[size-1];
        size-=1;
        return x;
    }
    public T get(int index){
        if(index>size-1)return null;
        return array[index];
    }
    public Iterator<T> iterator(){
        return new Iterator<T>() {
            int loc=0;
            @Override
            public boolean hasNext() {
                if(loc==size()-1)return false;
                return true;
            }
            @Override
            public T next() {
                if(hasNext())return get(loc++);
                else return null;
            }
        };
    }
    public boolean equals(Object o){
        if(o instanceof ArrayDeque){
            if(((ArrayDeque<?>) o).size()==size){
                for(int i=0;i<size;++i){
                    if(((ArrayDeque<?>) o).array[i]!=array[i])return false;
                }
                return true;
            }
        }
        return false;
    }


}
