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
    public static String cur;
    public static Commit head;
    public final static File TempFile = join(Repository.GITLET_DIR, "Temp");
    public final static File RemovalFile = join(Repository.GITLET_DIR, "Removal");
    public static HashMap<String,String> B;//temparary blobs
    public static Commit InitCommit;//init commit
    public static File master = join(heads, "master");

    /* TODO: fill in the rest of this class. */
    public static void setup(){
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
        try {
            InitCommit = new Commit("initial commit");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        InitCommit.saveCommit();
        writeObject(CurBranch,"master");
        Utils.writeObject(Head, InitCommit);
        Utils.writeObject(master, InitCommit);
        cur="master";

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
        cur=readObject(CurBranch,String.class);
        writeObject(join(heads,cur),head);//save branch head
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
                cur=readObject(CurBranch,String.class);
                writeObject(join(heads,cur),head);
                return;
            }//headcommit have no this file
        }
        if(B==null)B=new HashMap<>();
        Blob b=new Blob(filename);
        b.saveBlob();
        B.put(filename,b.id);
        Temp t = new Temp(B);
        t.saveTemp();//save temprepo
        writeObject(Head,head);
        cur=readObject(CurBranch,String.class);
        writeObject(join(heads,cur),head);
    }

    public static void rmfiles(String filename) {
        File file = join(CWD, filename);
        head=readObject(Head,Commit.class);
        Temp t=readObject(TempFile,Temp.class);
        if(t.blobs.containsKey(filename)){

            File f=join(blobs,t.blobs.get(filename));
            f.delete();
            t.blobs.remove(filename);
            t.saveTemp();
        }
        if(head.file2blobs.containsKey(filename)){
            File f=join(blobs,head.file2blobs.get(filename));
            f.delete();
            Removal r=readObject(RemovalFile, Removal.class);
            r.blobs.put(filename,new Blob(filename).id);
            r.saveRemoval();
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
            Blob b=readObject(join(blobs,head.file2blobs.get(filename)), Blob.class);
            writeContents(file,b.content);
        }
        else{
            System.out.println("File does not exist in that commit.");
            return;
        }
    }


    public static void checkoutcommitfile(String id,String filename) {
        File f=join(commits,id);
        if(!f.exists()){
            System.out.println("No commit with that id exists.");
            return;
        }
        Commit c=readObject(join(commits,id), Commit.class);
        if(!c.file2blobs.containsKey(filename)){
            System.out.println("File does not exist in that commit.");
            return;
        }
        Blob b=readObject(join(blobs,c.file2blobs.get(filename)), Blob.class);
        writeContents(join(CWD,filename),b.content);

    }

    public static void checkoutbranchfile(String branch) {
        File f=join(heads,branch);
        if(!f.exists()){
            System.out.println("No such branch exists.");
            return;
        }

        cur=readObject(CurBranch, String.class);
        if(branch.equals(cur)){
            System.out.println("No need to checkout the current branch.");
            return;
        }

        Commit c=readObject(f, Commit.class);
        head=readObject(Head, Commit.class);

        List<String> otfiles=Utils.plainFilenamesIn(CWD);
        for (String s:otfiles) {
            if(c.file2blobs.containsKey(s)&&head.file2blobs.containsKey(s)) {
                System.out.println("There is an untracked file in the way; " +
                        "delete it, or add and commit it first.");
                return;
            }
        }//unchecked files

        for(String s:c.file2blobs.keySet()){
            Blob b=readObject(join(blobs,c.file2blobs.get(s)), Blob.class);
            writeContents(join(CWD,s),b.content);
        }//write files of the branch head

        Temp t=readObject(TempFile,Temp.class);
        t.blobs.clear();
        t.saveTemp();//clear temp

        Removal r=readObject(RemovalFile, Removal.class);
        r.blobs.clear();
        r.saveRemoval();//clear removal

        head=c;
        writeObject(Head,head);//save head
    }

    public static void create_branch(String branch) {
        File f=join(heads,branch);
        if(f.exists()){
            System.out.println("A branch with that name already exists.");
            return;
        }
        head=readObject(Head, Commit.class);
        writeObject(f,head);
    }

    public static void rm_branch(String branch) {
        List<String> bs=Utils.plainFilenamesIn(heads);
        if(!bs.contains(branch)){
            System.out.println("A branch with that name does not exist.");
            return;
        }
        cur=readObject(CurBranch, String.class);
        if(branch.equals(cur)){
            System.out.println("Cannot remove the current branch.");
            return;
        }
        File f=join(heads,branch);
        f.delete();
    }

    public static void reset(String commitid) {
        List<String> cs=Utils.plainFilenamesIn(commits);
        if(!cs.contains(commitid)){
            System.out.println("No commit with that id exists.");
            return;
        }

        Commit c=readObject(join(commits,commitid), Commit.class);
        head=readObject(Head, Commit.class);
        List<String> otfiles=Utils.plainFilenamesIn(CWD);

        for (String s:otfiles) {
            if(c.file2blobs.containsKey(s)&&!head.file2blobs.containsKey(s)) {
                System.out.println("There is an untracked file in the way; " +
                        "delete it, or add and commit it first.");
                return;
            }
        }//unchecked files

        Temp t=readObject(TempFile,Temp.class);
        t.blobs.clear();
        t.saveTemp();//clear temp

        Removal r=readObject(RemovalFile, Removal.class);
        r.blobs.clear();
        r.saveRemoval();//clear removal

        head=c;
        writeObject(Head,head);
        cur=readObject(CurBranch,String.class);
        writeObject(join(heads,cur),head);//renew head to this commit
    }

    public static void Merge(String branch) {
        head=readObject(Head, Commit.class);
        Commit c=readObject(join(heads,branch), Commit.class);
        Commit p=head;
        int brcount=0,curcount=0;
        while(p!=null){
            if(p.equals(c)){
                System.out.println("Given branch is an ancestor of the current branch.");
                return;
            }
            curcount++;
            p=p.fa;
        }
        Commit q=c;
        while(q!=null){
            if(q.equals(head)){
                checkoutbranchfile(branch);
                System.out.println("Current branch fast-forwarded.");
                return;
            }
            brcount++;
            q=q.fa;
        }

        p=head;q=c;
        if(curcount>brcount){
            for(int i=0;i<curcount-brcount;i++){
                p=p.fa;
            }
        }
        else if(curcount<brcount){
            for(int i=0;i<brcount-curcount;i++){
                q=q.fa;
            }
        }

        while(!p.fa.equals(c.fa)){
            p=p.fa;
            q=q.fa;
        }

        Commit sa=p.fa;//first same ancestor,p,q are its children
        for(String s:sa.file2blobs.keySet()){
            if(c.file2blobs.containsKey(s)&&head.file2blobs.containsKey(s)){
                if(c.file2blobs.get(s).equals(sa.file2blobs.get(s))&&
                        !head.file2blobs.get(s).equals(sa.file2blobs.get(s))){
                    //keep unchanged

                }
                else if(!c.file2blobs.get(s).equals(sa.file2blobs.get(s))&&
                        head.file2blobs.get(s).equals(sa.file2blobs.get(s))){
                    checkoutcommitfile(c.id,s);
                    addBlobs(s);
                }
                else if(!c.file2blobs.get(s).equals(sa.file2blobs.get(s))&&
                        !head.file2blobs.get(s).equals(sa.file2blobs.get(s))){
                    if(!c.file2blobs.get(s).equals(head.file2blobs.get(s))){
                        System.out.print("Encountered a merge conflict.");
                        Blob b=readObject(join(blobs,head.file2blobs.get(s)), Blob.class);
                        Blob bc=readObject(join(blobs,c.file2blobs.get(s)), Blob.class);
                        writeObject(join(CWD,b.id),"<<<<<<< HEAD\n" +readContents(join(CWD,b.id))+
                                "\n" +
                                "=======\n" +readContents(join(CWD,bc.id))+
                                "\n" +
                                ">>>>>>>");
                        addBlobs(s);
                        cur=readObject(CurBranch, String.class);
                        commitbuild("Merged "+branch+" into "+cur+".");
                    }
                }
            }
            if(!c.file2blobs.containsKey(s)&&head.file2blobs.containsKey(s)){
                if(head.file2blobs.get(s).equals(sa.file2blobs.get(s))){
                    rmfiles(s);
                }
            }
            if(c.file2blobs.containsKey(s)&&!head.file2blobs.containsKey(s)) {
                if (c.file2blobs.get(s).equals(sa.file2blobs.get(s))) {
                    //remain absent
                }
            }
        }

        for(String s:c.file2blobs.keySet()){
            if(!sa.file2blobs.containsKey(s)){
                if(!head.file2blobs.containsKey(s)){
                    checkoutcommitfile(c.id,s);
                    addBlobs(s);
                }
                else{
                    System.out.print("Encountered a merge conflict.");
                    Blob b=readObject(join(blobs,head.file2blobs.get(s)), Blob.class);
                    Blob bc=readObject(join(blobs,c.file2blobs.get(s)), Blob.class);
                    writeObject(join(CWD,b.id),"<<<<<<< HEAD\n" +readContents(join(CWD,b.id))+
                            "\n" +
                            "=======\n" +readContents(join(CWD,bc.id))+
                            "\n" +
                            ">>>>>>>");
                    addBlobs(s);
                    cur=readObject(CurBranch, String.class);
                    commitbuild("Merged "+branch+" into "+cur+".");
                }
            }
        }


    }
}

