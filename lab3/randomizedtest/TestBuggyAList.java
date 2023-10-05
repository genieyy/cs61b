package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import timingtest.AList;

import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove(){
        AListNoResizing<Integer>Alst=new AListNoResizing<>();
        BuggyAList<Integer>Blst=new BuggyAList<>();
        Alst.addLast(4);
        Blst.addLast(4);
        Alst.addLast(5);
        Blst.addLast(5);
        Alst.addLast(6);
        Blst.addLast(6);

        int i,j;
        i=Alst.removeLast();
        j=Blst.removeLast();
        assertEquals(i,j);

        i=Alst.removeLast();
        j=Blst.removeLast();
        assertEquals(i,j);

        i=Alst.removeLast();
        j=Blst.removeLast();
        assertEquals(i,j);


  }
  @Test
  public void randomizedTest(){
    AListNoResizing<Integer> L = new AListNoResizing<>();
    BuggyAList<Integer> B = new BuggyAList<>();
    int N = 5000;
    for (int i = 0; i < N; i += 1) {
      int operationNumber = StdRandom.uniform(0, 4);
      if (operationNumber == 0) {
        // addLast
        int randVal = StdRandom.uniform(0, 100);
        L.addLast(randVal);
        B.addLast(randVal);



      } else if (operationNumber == 1) {
        // size
        int lsize = L.size();
        int bsize = B.size();
        assertEquals(lsize,bsize);

      }
      else if (operationNumber == 2) {
          // size
          int lsize = L.size();
          int bsize = B.size();
          assertEquals(lsize,bsize);
          if(lsize>0){
              int l=L.getLast();
              int b=B.getLast();
              assertEquals(l,b);

          }
      }
      else if (operationNumber == 3) {
          // size
          int lsize = L.size();
          int bsize = B.size();
          assertEquals(lsize,bsize);
          if(lsize>0){
              int l=L.removeLast();
              int b=B.removeLast();
              assertEquals(l,b);

          }

      }
    }
  }
}
