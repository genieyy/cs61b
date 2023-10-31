package gitlet;

import java.io.Serializable;

public class Blob implements Serializable {
    public String id;
    public byte[] content;
    public Blob(String filename){
        content=Utils.readContents(Utils.join(Repository.CWD,filename));
        id= Utils.sha1(content);//根据文件名找到文件，然后读取内容计算id

    }
    public void saveBlob(){
        Utils.writeObject(Utils.join(Repository.blobs,id),this);
    }
}
