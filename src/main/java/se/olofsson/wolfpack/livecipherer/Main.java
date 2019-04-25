package se.olofsson.wolfpack.livecipherer;

import javax.swing.*;
import java.net.URL;
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
    }

    private static String getCurrentVersion(){
        // Source (modified): https://stackoverflow.com/a/1273432
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
}
