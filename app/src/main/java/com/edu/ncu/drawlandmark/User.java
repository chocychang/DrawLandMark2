package com.edu.ncu.drawlandmark;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String username="";
    public String useremail="su3";
    public String userUID="";
  //  public String parent_setting;
  // public String porfolio;
  //  public String profile_picture;

    public User(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String userUID){
        this.userUID = userUID;
    }

    public User(String useremail, String username){
        this.useremail = useremail;
        this.username = username;
    }

    public User(String userUID, String useremail, String username){
        this.userUID = userUID;
        this.useremail = useremail;
        this.username = username;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }
}
