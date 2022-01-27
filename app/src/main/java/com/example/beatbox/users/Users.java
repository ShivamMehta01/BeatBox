package com.example.beatbox.users;

import java.io.File;
import java.util.ArrayList;

public class Users {
    String name;
    String mail;
    String password;
    String id;
    public ArrayList<File> fav;

    public Users(String name,String mail, String password,String id,ArrayList<File> fav) {
        this.name=name;
        this.mail = mail;
        this.password = password;
        this.id = id;
        this.fav=fav;
    }

    public String getName()
    {
        return name;
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }

    public String getId() {
        return id;
    }

    public void setName(String name)
    {
        this.name=name;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<File> getFav() {
        return fav;
    }

    public void setFav(ArrayList<File> fav) {
        this.fav = fav;
    }

    // empty constrctor
    public Users(){
        this.id="";
        this.name="";
        this.mail="";
        this.password="";
        this.fav=new ArrayList<>();
    }

    // signup constructor
    public Users(String name,String mail,String password,ArrayList<File> fav)
    {
        this.name=name;
        this.mail=mail;
        this.password=password;
        this.fav=fav;
    }
}
