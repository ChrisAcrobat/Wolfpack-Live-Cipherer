package se.olofsson.wolfpack.livecipherer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class UpdateChecker extends Thread implements Runnable
{
    private final LiveCipher LIVE_CIPHER;
    private final String LATEST_RELEASE_URL = "https://github.com/ChrisAcrobat/Wolfpack-Live-Cipherer/releases/latest/";

    public UpdateChecker(LiveCipher liveCipher){
        LIVE_CIPHER = liveCipher;
    }

    @Override
    public void run(){
        String latestVersion = getLatestVersion();
        if(!latestVersion.equals(Main.CURRENT_VERSION)){
            // Add link to update
            JMenu link = new JMenu("Update " + latestVersion + " is available!");
            link.setForeground(new Color(0x28, 0xA7, 0x45));
            link.addMouseListener(new MouseListener(){
                @Override
                public void mouseClicked(MouseEvent mouseEvent)
                {
                    if(SwingUtilities.isLeftMouseButton(mouseEvent))
                    {
                        if(Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                            try{
                                Desktop.getDesktop().browse(new java.net.URI(LATEST_RELEASE_URL));
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
                @Override public void mousePressed(MouseEvent e){}
                @Override public void mouseReleased(MouseEvent e){}
                @Override public void mouseEntered(MouseEvent e){}
                @Override public void mouseExited(MouseEvent e){}
            });
            LIVE_CIPHER.addLinkToNewRelease(link);
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
