package com.example.matthew.scheduleme;

import java.io.Serializable;

/**
 * Created by Matthew on 10/9/2016.
 */

public class user implements Serializable{
    public String name;
    public String password;
    public String profession;
    public int id;

    public String getName(){return this.name;}
    public String getPassword() {return this.password;}
    public String getProfession() {return this.profession;}
    public int getId() {
        return this.id;
    }

}
