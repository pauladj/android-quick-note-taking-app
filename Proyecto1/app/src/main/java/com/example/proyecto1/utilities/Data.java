package com.example.proyecto1.utilities;

public class Data {

    private static Data myData;
    private String activeUsername;
    private String filesPath;

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

    /**
     * Get the path of where files are saved (internal storage)
     * @return
     */
    public String getFilesPath(){
        return filesPath;
    }

    /**
     * Set the path of where files are saved (internal storage)
     * @param path
     */
    public void setFilesPath(String path){
        filesPath = path;
    }
}
