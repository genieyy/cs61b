package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static gitlet.Utils.readObject;
import static gitlet.Utils.writeObject;

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
    public HashMap<String,Blob> file2blobs=new HashMap<>();


    /* TODO: fill in the rest of this class. */
    public Commit(){}

    public Commit(String m) throws ParseException {
        message=m;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date epochTime = dateFormat.parse("1970-01-01 00:00:00");
        SimpleDateFormat formatter= new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy Z", Locale.ENGLISH);
        time= formatter.format(epochTime);

        id= Utils.sha1(message,time,file2blobs.toString());
    }
    public Commit(Commit far,String m){
        message=m;
        SimpleDateFormat formatter= new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy Z", Locale.ENGLISH);
        time= formatter.format(new Date(System.currentTimeMillis()));
        fa=far;
        HashMap<String,Blob>fblo=readObject(Repository.TempFile,Temp.class).blobs;
        Map<String,Blob> map =fblo;
        file2blobs.putAll(map);
        fblo.clear();
        Temp t=new Temp(fblo);
        writeObject(Repository.TempFile,t);

        map =fa.file2blobs;
        for(Map.Entry<String,Blob> i:map.entrySet()){
            if(!file2blobs.containsKey(i.getKey())){
                file2blobs.put(i.getKey(),i.getValue());
            }
        }//compare with head,save the blobs of far different with self


        Removal r= readObject(Repository.RemovalFile, Removal.class);
        if(r!=null){
            map =r.blobs;
            for(Map.Entry<String,Blob> i:map.entrySet()){
                file2blobs.remove(i.getKey());
            }// removal delete
            r.blobs.clear();
            writeObject(Repository.RemovalFile,r);
        }
        if(r==null)r=new Removal(new HashMap<>());
        writeObject(Repository.RemovalFile, r);

        id= Utils.sha1(message,time,fa.toString(),file2blobs.toString());
    }
    public Commit(Commit far,Commit secfar,String m){
        message=m;
        SimpleDateFormat formatter= new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy Z", Locale.ENGLISH);
        time= formatter.format(new Date(System.currentTimeMillis()));
        fa=far;
        HashMap<String,Blob>fblo=readObject(Repository.TempFile,Temp.class).blobs;
        Map<String,Blob> map =fblo;
        file2blobs.putAll(map);
        fblo.clear();
        Temp t=new Temp(fblo);
        writeObject(Repository.TempFile,t);

        map =fa.file2blobs;
        for(Map.Entry<String,Blob> i:map.entrySet()){
            if(!file2blobs.containsKey(i.getKey())){
                file2blobs.put(i.getKey(),i.getValue());
            }
        }//compare with head,save the blobs of far different with self
        map =secfa.file2blobs;
        for(Map.Entry<String,Blob> i:map.entrySet()){
            if(!file2blobs.containsKey(i.getKey())){
                file2blobs.put(i.getKey(),i.getValue());
            }
        }//compare with head,save the blobs of far different with self

        Removal r= readObject(Repository.RemovalFile, Removal.class);
        if(r!=null){
            map =r.blobs;
            for(Map.Entry<String,Blob> i:map.entrySet()){
                file2blobs.remove(i.getKey());
            }// removal delete
        }
        if(r==null)r=new Removal(new HashMap<>());
        writeObject(Repository.RemovalFile, r);

        id= Utils.sha1(message,time,fa.toString(),secfa.toString(),file2blobs.toString());
    }
    public void saveCommit(){
        File f=Utils.join(Repository.commits,this.id);
        Utils.writeObject(f,this);
    }
}


