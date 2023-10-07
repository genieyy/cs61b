package deque;

import java.util.Arrays;
import java.util.Iterator;

public class ArrayDeque<T>{
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
        if(array.length==size){
            resize(size*2);
        }
        size+=1;
        T[] a=(T[])new Object[size];
        System.arraycopy(array,0,a,1,size);
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
        return Arrays.stream(array).iterator();
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
