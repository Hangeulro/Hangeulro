package kr.edcan.neologism.model;

import java.util.ArrayList;

/**
 * Created by JunseokOh on 2016. 9. 28..
 */
public class MyDic {
    public String owner, dicname, sub;
    public ArrayList<String> favorite;

    public MyDic(String owner, String dicname, String sub, ArrayList<String> favorite) {
        this.owner = owner;
        this.dicname = dicname;
        this.sub = sub;
        this.favorite = favorite;
    }

    public String getOwner() {
        return owner;
    }

    public String getDicname() {
        return dicname;
    }

    public String getSub() {
        return sub;
    }

    public ArrayList<String> getFavorite() {
        return favorite;
    }
}
