package com.wesgue.signinexample.Databases.Models;

/**
 * Created by Wesley Gue on 10/14/2016.

 * Stores Session ID from the server
 */

public class Session {

    // private variables
    private int id;
    private String session_server_id;


    // Empty Constructor
    public Session() {

    }

    //Constructor
    public Session(int id, String sessionServerId) {
        this.id = id;
        this.session_server_id = sessionServerId;
    }

    //Constructor
    public Session(String sessionServerId) {
        this.session_server_id = sessionServerId;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSession_server_id() {
        return session_server_id;
    }

    public void setSession_server_id(String session_server_id) {
        this.session_server_id = session_server_id;
    }
}

