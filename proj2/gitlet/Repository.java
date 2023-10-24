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
    public static final File objects = join(GITLET_DIR, "objects");
    public static final File commits = join(objects,"commits");
    public static final File blobs = join(objects,"blobs");
    public static final File refs=Utils.join(GITLET_DIR,"refs");//heads->master
    public static final File heads=Utils.join(refs,"heads");//master,other
    public static final File Temp=Utils.join(GITLET_DIR,"Temp");//for git add
    public static File Head=Utils.join(GITLET_DIR,"Head");//recent commit
    public static Commit head=readObject(Head,Commit.class);
    public static HashMap<String,Blob>file2blobs;//addfile blob  deleted after new commit
    public static Commit InitCommit;//init commit

    /* TODO: fill in the rest of this class. */
    public static void setup() throws IOException {
        /*if(GITLET_DIR.exists()){
            System.out.print("A Gitlet version-control system already exists in the current directory.");
            exit(0);

        }*/
        GITLET_DIR.mkdirs();
        objects.mkdirs();
        commits.mkdirs();
        blobs.mkdirs();
        refs.mkdirs();
        Temp.mkdirs();//make directory

        InitCommit=new Commit("initcommit");
        File master=join(heads,"master");
        Utils.writeObject(Head,InitCommit);
        Utils.writeObject(master,InitCommit);
        head=InitCommit;
    }
    public static Commit commitbuild(String m) {

        head=Utils.readObject(Head,Commit.class);
        Commit c=new Commit(head,file2blobs,m);
        head=c;
        Utils.writeObject(Head,head);//save head
        file2blobs.clear();
        Utils.writeObject(Temp,file2blobs);//save temprepo

        c.saveCommit();
        return c;
    }
    public static void addBlobs(String filename){
        File file=Utils.join(CWD,filename);
        if(!file.exists()){
            System.out.println("File does not exist.");
            exit(0);
        }

        for ( Blob blob : file2blobs.values()) {
            if (Utils.sha1(Utils.readObject(file,String.class)).equals(blob.id)) {
                return;
            }//temprepo have no this file
        }
        for(Blob blob : head.file2blobs.values()){
            if (Utils.sha1(Utils.readObject(file,String.class)).equals(blob.id)) {
                return;
            }//headcommit have no this file
        }
        file2blobs.put(filename,new Blob(filename));

        Utils.writeObject(Temp,file2blobs);//save temprepo
    }
    public static void rmfiles(String filename){
        File file=Utils.join(GITLET_DIR,filename);

        System.out.println("No reason to remove the file.");


    }

    public static void logcommits() {
        Commit p=Head;
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

