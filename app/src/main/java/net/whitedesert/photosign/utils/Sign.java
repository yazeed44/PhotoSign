package net.whitedesert.photosign.utils;

/**
 * Created by yazeed44 on 8/9/14.
 */
public class Sign {

    private String path;
    private int rawId;
    private String name;

    public void setPath(String path){
        this.path = path;
    }
    public void setRawId(int rawId){this.rawId = rawId;}
    public void setName(String name){this.name = name;}
    public String getPath(){
        return this.path;
    }
    public int getRawId(){return this.rawId;}
    public String getName(){return this.name;}
}
