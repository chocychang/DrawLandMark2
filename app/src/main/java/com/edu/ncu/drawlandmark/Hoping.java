package com.edu.ncu.drawlandmark;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties

public class Hoping {

    public String hopingID;
    public String hoperName;
    public String hoperWord;

    public Hoping(){

    }

    public Hoping(String hopingID, String hoperName, String hoperWord){
        this.hopingID = hopingID;
        this.hoperName = hoperName;
        this.hoperWord = hoperWord;
    }

    public Hoping(String hopingID){
        this.hopingID = hopingID;
    }

}
