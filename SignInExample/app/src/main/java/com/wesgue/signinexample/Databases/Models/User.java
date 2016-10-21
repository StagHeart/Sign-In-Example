package com.wesgue.signinexample.Databases.Models;

/**
 * Created by Wesley Gue on 10/12/2016.
 *
 *  Stores User from the Server
 */

public class User {

    // private variables
    private int id;
    private String user_id;
    private String email;
    private String session_id;


    // Empty Constructor
    public User(){

    }


    //Constructor
    public User(int id, String userId, String email,String sessionId){
        this.id = id;
        this.user_id = userId;
        this.email = email;
        this.session_id = sessionId;

    }


    //Constructor
    public User(String userId, String email, String sessionId ){
        this.user_id = userId;
        this.email = email;
        this.session_id = sessionId;
    }


    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }
}
