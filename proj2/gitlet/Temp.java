package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;

import static gitlet.Utils.join;
import static gitlet.Utils.writeObject;

public class Temp implements Serializable{
    public HashMap<String,String>blobs=new HashMap<>();

    public Temp(HashMap<String,String>B){
        blobs=B;
    }
    public void  saveTemp(){
        writeObject(Repository.TempFile, this);
    }
}
