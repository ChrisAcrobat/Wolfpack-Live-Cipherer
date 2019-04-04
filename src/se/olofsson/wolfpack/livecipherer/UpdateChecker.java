package se.olofsson.wolfpack.livecipherer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
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
            // Add link to update
            JMenu link = new JMenu("New update is available!");
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
                            }catch(IOException e){
                                e.printStackTrace();
                            }catch(URISyntaxException e){
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
            JMENU_BAR.add(Box.createHorizontalGlue());
            JMENU_BAR.add(link);
            JMENU_BAR.setVisible(true);
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
