package gitlet;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static gitlet.Utils.*;
import static java.lang.System.exit;
import static java.util.Collections.sort;

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
    public static File CurBranch=Utils.join(GITLET_DIR,"CurBranch");
    public static Commit head;
    public final static File TempFile = join(Repository.GITLET_DIR, "Temp");
    public final static File RemovalFile = join(Repository.GITLET_DIR, "Removal");
    public static HashMap<String,Blob> B;//temparary blobs
    public static Commit InitCommit;//init commit
    public static File master = join(heads, "master");

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
        writeObject(RemovalFile,new Removal(new HashMap<>()));
        writeObject(TempFile,new Temp(new HashMap<>()));
        InitCommit = new Commit("init commit");
        InitCommit.saveCommit();
        writeObject(CurBranch,"master");
        Utils.writeObject(Head, InitCommit);
        Utils.writeObject(master, InitCommit);

    }

    public static void commitbuild(String m) {

        head = Utils.readObject(Head, Commit.class);
        B = readObject(TempFile, Temp.class).blobs;//read temprepo
        Removal r=readObject(RemovalFile,Removal.class);
        if (B.size() == 0&&r.blobs.size()==0) {
            System.out.print("No changes added to the commit.");
        }

        Commit c = new Commit(head, m);
        head = c;
        writeObject(Head, head);//save head
        writeObject(master,head);
        B.clear();//temprepo clear
        Temp t = new Temp(B);
        t.saveTemp();//save temprepo
        c.saveCommit();

    }
    public static void addBlobs(String filename) {
        File file = Utils.join(CWD, filename);
        if (!file.exists()) {
            System.out.println("File does not exist.");
            exit(0);
        }
        if(TempFile.exists()==false)writeObject(TempFile,B);
        B = readObject(TempFile, Temp.class).blobs;//read temprepo
        if(B!=null){
            if (B.containsValue(Utils.sha1(Utils.readContentsAsString(file)))){
                Temp t = new Temp(B);
                t.saveTemp();//save temprepo
                return;
            }//temprepo have no this file
        }

        head = readObject(Head, Commit.class);
        if(head.file2blobs!=null){
            if (head.file2blobs.containsValue(Utils.sha1(Utils.readContentsAsString(file)))){
                writeObject(Head,head);//save temprepo
                return;
            }//headcommit have no this file
        }
        if(B==null)B=new HashMap<>();
        Blob b=new Blob(filename);
        b.saveBlob();
        B.put(filename,b);
        Temp t = new Temp(B);
        t.saveTemp();//save temprepo
        writeObject(Head,head);
    }

    public static void rmfiles(String filename) {
        File file = join(CWD, filename);
        head=readObject(Head,Commit.class);
        Temp t=readObject(TempFile,Temp.class);
        if(t.blobs.containsKey(filename)){
            t.blobs.remove(filename);
            t.saveTemp();
        }
        if(head.file2blobs.containsKey(filename)){
            Removal r=readObject(RemovalFile, Removal.class);
            r.blobs.put(filename,new Blob(filename));
            r.saveRemoval();
            file.delete();
        }
        if(!t.blobs.containsKey(filename)&&!head.file2blobs.containsKey(filename)){
            System.out.println("No reason to remove the file.");
        }

    }

    public static void logcommits() {
        head=readObject(Head,Commit.class);
        Commit p = head;
        do {
            if (p.secfa != null) {
                System.out.println("===\n" +
                        "commit" + " " + p.id + "\n" +
                        "Merge: " + p.fa.id.substring(0, 7) + " " + p.secfa.id.substring(0, 7) + "\n" +"Date: "+p.time + "\n" +
                        "Merged development into master."+"\n");
            } else {
                System.out.println("===\n" +
                        "commit" + " " + p.id + "\n" +"Date: "+
                        p.time + "\n" +p.message+"\n");
            }
            p=p.fa;

        }while(p!=null);
    }

    public static void global_log() {
        List<String> coms=Utils.plainFilenamesIn(commits);
        for(String s:coms){
            Commit c=readObject(join(commits,s), Commit.class);
            System.out.println(c.message);
        }
    }

    public static void find_mesg(String mes) {
        List<String> coms=Utils.plainFilenamesIn(commits);
        for(String s:coms){
            Commit c=readObject(join(commits,s), Commit.class);
            if(c.message.equals(mes)){
                System.out.println(c.id);
            }
        }
    }

    public static void printstatus() {
        System.out.println("=== Branches ===");
        List<String>br=plainFilenamesIn(heads);
        head=readObject(Head,Commit.class);
        sort(br);
        for(int i=0;i<br.size();++i){
            if(readObject(CurBranch,String.class).equals(br.get(i))){
                System.out.println("*"+br.get(i));
            }
            else{
                System.out.println(readObject(join(heads,br.get(i)),Commit.class));
                System.out.println(head);
                System.out.println(head.equals(readObject(join(heads,br.get(i)),Commit.class)));
                System.out.println(br.get(i));
            }
        }
        System.out.println();

        System.out.println("=== Staged Files ===");
        Temp t=readObject(TempFile,Temp.class);

        for (String string : t.blobs.keySet()) {
            System.out.println(string);
        }
        System.out.println();

        System.out.println("=== Removed Files ===");
        Removal r=readObject(RemovalFile, Removal.class);
        for(String s:r.blobs.keySet()){
            System.out.println(s);
        }
        System.out.println();

        System.out.println("=== Modifications Not Staged For Commit ===");

        System.out.println();

        System.out.println("=== Untracked Files ===");
        System.out.println();


    }

    public static void checkoutheadfile(String filename) {
        head=readObject(Head, Commit.class);
        File file=join(CWD,filename);
        if(head.file2blobs.containsKey(filename)){
            writeContents(file,head.file2blobs.get(filename));
        }
        else{
            System.out.println("File does not exist in that commit.");
            return;
        }
    }


    public static void checkoutcommitfile() {
    }

    public static void checkoutbranchfile() {
    }
}

