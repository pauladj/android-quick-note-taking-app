package com.example.proyecto1.utilities;

public class Data {

    private static Data myData;
    private static String activeUsername;

    /**
     * Get one instance of this class
     * @return Data
     */
    public static Data getMyData(){
        if (myData == null){
            myData = new Data();
        }
        return myData;
    }

    /**
     * Get the active username
     * @return active username
     */
    public String getActiveUsername(){
        return activeUsername;
    }

    /**
     * Set the active username
     * @param username
     */
    public void setActiveUsername(String username){
        activeUsername = username;
    }
}
