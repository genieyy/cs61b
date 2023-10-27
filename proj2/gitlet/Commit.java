package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    public String message;
    public String id;
    public Commit fa;
    public Commit secfa;
    public String time;
    public HashMap<Blob,String> blobsf2ile=new HashMap<>();


    /* TODO: fill in the rest of this class. */
    public Commit(String m) throws ParseException {
        message=m;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date epochTime = dateFormat.parse("1970-01-01 00:00:00");
        SimpleDateFormat formatter= new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy");
        time= formatter.format(epochTime);
    }
    public Commit(Commit far,HashMap fblo,String m){
        message=m;
        SimpleDateFormat formatter= new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy");
        time= formatter.format(new Date(System.currentTimeMillis()));
        fa=far;
        Map<Blob,String> map =fblo;
        blobsf2ile.putAll(map);
        map =fa.blobsf2ile;
        for(Map.Entry<Blob,String> i:map.entrySet()){
            if(!blobsf2ile.containsKey(i.getKey())){
                blobsf2ile.put(i.getKey(),i.getValue());
            }
        }//compare with head,save the blobs of far different with self
        Removal r=Utils.readObject(Repository.RemovalFile, Removal.class);
        map =r.blobs;
        for(Map.Entry<Blob,String> i:map.entrySet()){
            blobsf2ile.remove(i.getKey());
        }// removal delete
        id= Utils.sha1(message,time,fa.toString(),secfa.toString(),blobsf2ile.toString());
    }
    public Commit(Commit far,Commit secfar,HashMap fblo,String m){
        message=m;
        SimpleDateFormat formatter= new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy");
        time= formatter.format(new Date(System.currentTimeMillis()));

        fa=far;
        secfa=secfar;
        //id final consider

        Map<Blob,String> map =fblo;
        blobsf2ile.putAll(map);
        map =fa.blobsf2ile;
        for(Map.Entry<Blob,String> i:map.entrySet()){
            if(!blobsf2ile.containsKey(i.getKey())){
                blobsf2ile.put(i.getKey(),i.getValue());
            }
        }//compare with fa,save the blobs of far different with self
        map =secfa.blobsf2ile;
        for(Map.Entry<Blob,String> i:map.entrySet()){
            if(!blobsf2ile.containsKey(i.getKey())){
                blobsf2ile.put(i.getKey(),i.getValue());
            }
        }//compare with secfa,save the blobs of secfar different with self
        Removal r=Utils.readObject(Repository.RemovalFile, Removal.class);
        map =r.blobs;
        for(Map.Entry<Blob,String> i:map.entrySet()){
            blobsf2ile.remove(i.getKey());
        }// removal delete
        id= Utils.sha1(message,time,fa.toString(),secfa.toString(),blobsf2ile.toString());
    }
    public void saveCommit(){
        File f=Utils.join(Repository.commits,this.id);
        Utils.writeObject(f,this);
    }
}


