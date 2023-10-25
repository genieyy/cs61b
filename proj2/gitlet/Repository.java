package gitlet;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;

import static gitlet.Utils.*;
import static java.lang.System.exit;

// TODO: any imports you need here

/**
 * Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 * @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /**
     * The current working directory.
     */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /**
     * The .gitlet directory.
     */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File objects = join(GITLET_DIR, "objects");
    public static final File commits = join(objects, "commits");
    public static final File blobs = join(objects, "blobs");
    public static final File refs = Utils.join(GITLET_DIR, "refs");//heads->master
    public static final File heads = Utils.join(refs, "heads");//master,other
    public static File Head = Utils.join(GITLET_DIR, "Head");//recent commit
    public static Commit head;
    public final static File TempFile = join(Repository.GITLET_DIR, "Temp");
    public final static File RemovalFile = join(Repository.GITLET_DIR, "Removal");
    public static HashMap<Blob, String> B;//temparary blobs
    public static Commit InitCommit;//init commit

    /* TODO: fill in the rest of this class. */
    public static void setup() throws IOException, ParseException {
        /*if(GITLET_DIR.exists()){
            System.out.print("A Gitlet version-control system already exists in the current directory.");
            exit(0);

        }*/
        GITLET_DIR.mkdirs();
        objects.mkdirs();
        commits.mkdirs();
        blobs.mkdirs();
        refs.mkdirs();
        heads.mkdirs();
        Temp T = new Temp(B);
        //make directory
        Utils.writeObject(TempFile, T);
        InitCommit = new Commit("initcommit");
        File master = join(heads, "master");
        Utils.writeObject(Head, InitCommit);
        Utils.writeObject(master, InitCommit);

    }

    public static Commit commitbuild(String m) {

        head = Utils.readObject(Head, Commit.class);
        B = readObject(TempFile, Temp.class).blobs;//read temprepo
        if(B.size()==0){
            System.out.print("No changes added to the commit.");
            return null;
        }
        Commit c = new Commit(head, B, m);
        head = c;
        Utils.writeObject(Head, head);//save head
        B.clear();//temprepo clear
        Temp t = new Temp(B);
        t.saveTemp();//save temprepo
        c.saveCommit();
        return c;
    }

    public static void addBlobs(String filename) {
        File file = Utils.join(CWD, filename);
        if (!file.exists()) {
            System.out.println("File does not exist.");
            exit(0);
        }
        B = readObject(TempFile, Temp.class).blobs;//read temprepo
        if(B.containsKey(filename))B.put(new Blob(filename),filename);//same name replace

        if (B.containsKey(Utils.sha1(Utils.readObject(file, String.class)))){
                return;
        }//temprepo have no this file

        head = readObject(Head, Commit.class);
        if (head.blobsf2ile.containsKey(Utils.sha1(Utils.readObject(file, String.class)))){
            return;
        }//headcommit have no this file

        B.put(new Blob(filename), filename);
        Temp t = new Temp(B);
        t.saveTemp();//save temprepo
    }

    public static void rmfiles(String filename) {
        File file = Utils.join(GITLET_DIR, filename);
        head=readObject(Head,Commit.class);
        Temp t=readObject(TempFile,Temp.class);
        if(!t.blobs.containsValue(sha1(readContentsAsString(file)))
                &&!head.file2blobs.containsValue(sha1(readContentsAsString(file)))){
            System.out.println("No reason to remove the file.");
        }
        if(t.blobs.containsValue(sha1(readContentsAsString(file)))){
            t.blobs.remove(filename);
        }
        if(head.file2blobs.containsValue(sha1(readContentsAsString(file)))){
            head.file2blobs.remove()
        }

        file.delete();




    }

    public static void logcommits() {
        Commit p = head;
        if (p.secfa != null) {
            System.out.println("===\n" +
                    "commit" + " " + p.id + "\n" +
                    "Merge: " + p.fa.id.substring(0, 7) + " " + p.secfa.id.substring(0, 7) + "\n" + p.time + "\n" +
                    "Merged development into master.");
        } else {
            System.out.println("===\n" +
                    "commit" + " " + p.id + "\n" +
                    p.time + "\n" +
                    "Merged development into master.");
        }
    }
}

