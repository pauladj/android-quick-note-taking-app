package com.example.proyecto1.utilities;

public class ActiveUser {

    private static ActiveUser myActiveUsername;
    private static String activeUsername;

    /**
     * Get one instance of this class
     * @return ActiveUser
     */
    public static ActiveUser getMyActiveUsername(){
        if (myActiveUsername == null){
            myActiveUsername = new ActiveUser();
        }
        return myActiveUsername;
    }

    /**
     * Get the active username
     * @return active username
     */
    public String getUsername(){

        return activeUsername;
    }

    /**
     * Set the active username
     * @param username
     */
    public void setUsername(String username){
        activeUsername = username;
    }
}
