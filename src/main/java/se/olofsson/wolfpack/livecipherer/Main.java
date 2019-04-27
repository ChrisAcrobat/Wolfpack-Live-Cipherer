package se.olofsson.wolfpack.livecipherer;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * Created on 2016-05-29.
 * @author Christoffer Olofsson
 */
public class Main
{
    public static final String CURRENT_VERSION = getCurrentVersion();

    public static void main(String[] args)
    {
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception e){
            e.printStackTrace();
        }

        new LiveCipher().setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        sendUsageAnalytics(String.join("\n", args));
    }

    private static String getCurrentVersion(){
        // Source by ZZ Coder (modified): https://stackoverflow.com/a/1273432
        String currentVersion = null;
        try{
            Class clazz = Main.class;
            String className = clazz.getSimpleName() + ".class";
            String classPath = clazz.getResource(className).toString();
            if (!classPath.startsWith("jar")){
                return null;
            }
            String manifestPath = classPath.substring(0, classPath.lastIndexOf("!") + 1) + "/META-INF/MANIFEST.MF";
            Manifest manifest = new Manifest(new URL(manifestPath).openStream());
            Attributes attr = manifest.getMainAttributes();
            currentVersion = attr.getValue("LiveCipherer-Version");
        }catch(Exception e){}
        return currentVersion;
    }

    private static void sendUsageAnalytics(String args){
        String baseURL = "https://docs.google.com/forms/d/e/1FAIpQLSdDQVz-MDe3pVMWO1VIt_s1c-dibNGUiNra-IpN9FeorLge3A/formResponse?usp=pp_url";
        try{
            String encodedVersion = URLEncoder.encode(CURRENT_VERSION == null ? "null" : CURRENT_VERSION, "UTF-8");
            String encodedArgs = URLEncoder.encode(args, "UTF-8");
            String fullUrl = baseURL + "&entry.762130873=" + encodedVersion + "&entry.175483917=" + encodedArgs + "&submit=Submit";
            new URL(fullUrl).openConnection().getInputStream();
        }catch(IOException e){}
    }
}
