package com.example.matthew.scheduleme;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.util.ObjectIdMap;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Matthew on 10/9/2016.
 */

public class user implements Serializable {
    public String name = "";
    @JsonIgnoreProperties
    public String _id;
    @JsonIgnoreProperties
    public String User;
    @JsonIgnoreProperties
    public String user;
    String schedule;
    ArrayList<ArrayList<String>> friends = new ArrayList<ArrayList<String>>();
    ArrayList<sentInvite> sentInvites = new ArrayList<sentInvite>();
    ArrayList<recievedInvite> recievedInvites = new ArrayList<recievedInvite>();

    public String getName() {
        return this.name;
    }

    public ArrayList<recievedInvite> getRecievedInvites() {
        return recievedInvites;
    }

    public ArrayList<sentInvite> getSentInvites() {
        return sentInvites;
    }

    public void setRecievedInvites(ArrayList<recievedInvite> recievedInvites) {
        this.recievedInvites = recievedInvites;
    }

    public void setSentInvites(ArrayList<sentInvite> sentInvites) {
        this.sentInvites = sentInvites;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFriends(ArrayList<ArrayList<String>> friends) {
        this.friends = friends;
    }

    public ArrayList<ArrayList<String>> getFriends() {
        return friends;
    }

    public String get_id() {
        return _id;
    }

    public String getSchedule() {
        return schedule;
    }

    public String getUser() {
        return User;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public void setUser(String user) {
        User = user;
    }



    public static class sentInvite implements Serializable {
        public String name = "";
        public String date = "";
        public ArrayList<ArrayList<String>> friendsAndAccepted;
        public String duration = "";
        public ArrayList<ArrayList<String>> getFriendsAccepted() {
            return friendsAndAccepted;
        }
        public String getDate() {
            return date;
        }
        public String getDuration() {
            return duration;
        }
        public String getName() {
            return name;
        }

        public ArrayList<ArrayList<String>> getFriendsAndAccepted() {
            return friendsAndAccepted;
        }

        public void setFriendsAndAccepted(ArrayList<ArrayList<String>> friendsAndAccepted) {
            this.friendsAndAccepted = friendsAndAccepted;
        }

        public void setDate(String date) {
            this.date = date;
        }
        public void setDuration(String duration) {
            this.duration = duration;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class recievedInvite implements Serializable {
        public String name = "";
        public String date = "";
        public String whoInvitedMe = "";
        public String duration = "";

        public String getWhoInvitedMe() {
            return whoInvitedMe;
        }

        public void setWhoInvitedMe(String whoInvitedMe) {
            this.whoInvitedMe = whoInvitedMe;
        }

        public String getDate() {
            return date;
        }
        public String getDuration() {
            return duration;
        }
        public String getName() {
            return name;
        }
        public void setDate(String date) {
            this.date = date;
        }
        public void setDuration(String duration) {
            this.duration = duration;
        }
        public void setName(String name) {
            this.name = name;
        }
    }
}