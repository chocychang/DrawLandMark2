package com.edu.ncu.drawlandmark;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Porfolio_info {

    public String porfolio_number;


    public Porfolio_info(){

    }


    public Porfolio_info(String porfolio_number){
        this.porfolio_number = porfolio_number;
    }
}
