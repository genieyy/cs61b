package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static gitlet.Utils.*;
import static java.lang.System.exit;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File TempRepo=Utils.join(GITLET_DIR,"TempRepo");
    public static final File headfile=Utils.join(GITLET_DIR,"head");
    public static HashMap<String,Commit> commits=null;//save all commit
    public static Commit head;//recent commit
    public static HashMap<String,Blob>file2blobs=null;//addfile blob  deleted after new commit
    public static Commit InitCommit;//init commit

    /* TODO: fill in the rest of this class. */
    public static void setup() throws IOException {
        /*if(GITLET_DIR.exists()){
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            exit(0);

        }*/
        GITLET_DIR.mkdirs();

        headfile.createNewFile();
        TempRepo.createNewFile();//make file

        InitCommit=new Commit("initcommit");
        head=InitCommit;
        Utils.writeObject(headfile,head);


    }
    public static Commit commitbuild(String m) {
        head=Utils.readObject(headfile,Commit.class);
        file2blobs=(HashMap<String,Blob>)Utils.readObject(TempRepo, HashMap.class);
        Commit c=new Commit(head,file2blobs,m);
        commits.put(c.id,c);//保存新commits

        head=c;
        Utils.writeObject(headfile,head);//save head
        file2blobs.clear();
        Utils.writeObject(TempRepo,file2blobs);//save temprepo

        c.saveCommit();
        return c;
    }
    public static void addBlobs(String filename){
        File file=Utils.join(CWD,filename);
        if(!file.exists()){
            System.out.println("File does not exist.");
            exit(0);
        }
        file2blobs=(HashMap<String,Blob>)Utils.readObject(TempRepo, HashMap.class);
        for ( Blob blob : file2blobs.values()) {
            if (Utils.sha1(Utils.readContents(file)).equals(blob.id)) {
                return;
            }//temprepo have no this file
        }
        for(Blob blob : head.file2blobs.values()){
            if (Utils.sha1(Utils.readContents(file)).equals(blob.id)) {
                return;
            }//headcommit have no this file
        }
        file2blobs.put(filename,new Blob(filename));

        Utils.writeObject(TempRepo,file2blobs);//save temprepo
    }
    public static void rmfiles(String filename){
        File file=Utils.join(GITLET_DIR,filename);

        System.out.println("No reason to remove the file.");


    }

    public static void logcommits() {
        Commit p=head;
        do{
            if(p.fatherCm.size()>1){
                System.out.println("===\n" +
                        "commit"+" "+p.id+"\n" +
                        "Merge: "+p.fatherCm.get(0).id.substring(0,7)+" "+p.fatherCm.get(1).id.substring(0,7)+"\n"+p.time+"\n" +
                        "Merged development into master.");
            }
            else{
                System.out.println("===\n" +
                        "commit"+" "+p.id+"\n" +
                        p.time+"\n" +
                        "Merged development into master.");
            }

        }while(p.fatherCm!=null);
    }
}

