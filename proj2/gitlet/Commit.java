package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable{
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    public String id;
    public LinkedList<Commit> fatherCm=new LinkedList<>();
    public String time;
    public HashMap<String,Blob> file2blobs=new HashMap<>();


    /* TODO: fill in the rest of this class. */
    public Commit(String m){
        message=m;
    }
    public Commit(Commit fa,HashMap fblo,String m){
        message=m;
        SimpleDateFormat formatter= new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy");
        time= formatter.format(new Date(System.currentTimeMillis()));

        fatherCm.add(fa);
        //id final consider

        Map<String,Blob> map =fblo;
        for(Map.Entry<String,Blob> i:map.entrySet()){
            file2blobs.put(i.getKey(),i.getValue());
        }
        id= Utils.sha1(message,time,fatherCm.toString(),file2blobs.toString());
    }
    public void saveCommit(){
        File f=Utils.join(Repository.commits,this.id);
        Utils.writeObject(f,this);
    }
}


