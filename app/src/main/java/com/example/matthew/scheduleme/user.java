package com.example.matthew.scheduleme;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Matthew on 10/9/2016.
 */

public class user implements Serializable{
    public String name;
    public String password;
    HashMap<String,ArrayList<String>> sentInvites= new HashMap<String,ArrayList<String>>();
    HashMap<String,String> recievedInvites=new HashMap<String, String>();
    public ArrayList<String> friends;
    public List<String> events;
    public String getName(){return this.name;}
    public String getPassword() {return this.password;}
    public List<String> getEvents() {return events;}

    public HashMap<String, ArrayList<String>> getSentInvites() {
        return sentInvites;
    }

    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }

    public void setSentInvites(HashMap<String, ArrayList<String>> sentInvites) {
        this.sentInvites = sentInvites;
    }

    public ArrayList<String> getFriends() {return friends;}
    public void setName(String name) {
        this.name = name;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setEvents(List<String> events) {
        this.events = events;
    }
    public void addAFriend(String u) {friends.add(u);}
    public void setAllFriends(ArrayList<String> f) {friends = f;}
    public ArrayList<String> getAllFriends() {return this.friends;}

}