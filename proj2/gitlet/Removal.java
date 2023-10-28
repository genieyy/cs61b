package gitlet;

import java.io.Serializable;
import java.util.HashMap;

import static gitlet.Utils.writeObject;

public class Removal implements Serializable {
    public HashMap<String,Blob>blobs;
    public Removal(HashMap<String,Blob>B){
        blobs=new HashMap<>(B);
    }
    public void saveRemoval(){
        writeObject(Repository.RemovalFile,this);
    }

}
