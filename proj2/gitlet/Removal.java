package gitlet;

import java.io.Serializable;
import java.util.HashMap;

import static gitlet.Utils.writeObject;

public class Removal implements Serializable {
    public HashMap<Blob,String>blobs;
    public Removal(HashMap<Blob,String>B){
        blobs=new HashMap<>(B);
    }
    public void RemovalSave(){
        writeObject(Repository.RemovalFile,this);
    }

}
