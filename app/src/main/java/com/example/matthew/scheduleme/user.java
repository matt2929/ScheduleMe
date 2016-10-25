package com.example.matthew.scheduleme;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Matthew on 10/9/2016.
 */

public class user implements Serializable{
    public String name;
    public String password;
    public String profession;
    public int id;
    public ArrayList<user> friends;

    public String getName(){return this.name;}
    public String getPassword() {return this.password;}
    public String getProfession() {return this.profession;}
    public int getId() {
        return this.id;
    }
    public void addAFriend(user u) {friends.add(u);}
    public void setAllFriends(ArrayList<user> f) {friends = f;}
    public ArrayList<user> getAllFriends() {return this.friends;}
    public void setName(String s) {name = s;}
}
