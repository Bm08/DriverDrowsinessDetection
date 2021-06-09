package com.project.driverdrowsinessdetectionsystem;

import com.google.firebase.database.IgnoreExtraProperties;

public class User {
    public String name;
    public String mobile;

    //Default constructor required for calls to
    //DataSnapshot.getValue(User.class)
    public User() {

    }

    public User(String name, String mobile) {
        this.name = name;
        this.mobile = mobile;
    }

}
