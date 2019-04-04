package se.olofsson.wolfpack.livecipherer;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class UpdateChecker extends Thread implements Runnable
{
    final private String CURRENT_RELEASE = "v0.2";

    final private JMenuBar JMENU_BAR;
    final private String LATEST_RELEASE_URL = "https://github.com/ChrisAcrobat/Wolfpack-Live-Cipherer/releases/latest/";

    public UpdateChecker(JMenuBar jMenuBar){
        JMENU_BAR = jMenuBar;
    }

    @Override
    public void run(){
        String latestVersion = getLatestVersion();
        if(CURRENT_RELEASE != latestVersion){
            System.out.println("Update available: " + latestVersion);
        }
    }

    private String getLatestVersion(){
        String latestVersion = "";
        try{
            URLConnection urlConnection = new URL(LATEST_RELEASE_URL).openConnection();
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            latestVersion = urlConnection.getURL().toString();
            inputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        String searchString = "/releases/tag/";
        int index = latestVersion.indexOf(searchString);
        return latestVersion.substring(index+searchString.length());
    }
}
