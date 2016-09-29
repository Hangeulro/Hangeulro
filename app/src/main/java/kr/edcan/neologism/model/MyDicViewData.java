package kr.edcan.neologism.model;

import java.util.ArrayList;

/**
 * Created by JunseokOh on 2016. 9. 28..
 */
public class MyDicViewData {
    public String dicname, subtxt;
    public ArrayList<Word> wordlist;

    public MyDicViewData(String dicname, String subtxt, ArrayList<Word> wordlist) {
        this.dicname = dicname;
        this.subtxt = subtxt;
        this.wordlist = wordlist;
    }

    public String getDicname() {
        return dicname;
    }

    public String getSubtxt() {
        return subtxt;
    }

    public ArrayList<Word> getWordlist() {
        return wordlist;
    }
}