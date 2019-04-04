package se.olofsson.wolfpack.livecipherer;

import com.sun.deploy.util.UpdateCheck;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created on 2016-06-05.
 * @author Christoffer Olofsson
 */
public class LiveCipher extends JFrame
{
    private final int ABOUT_BUTTON = 0;

    private JPanel pnlRoot;
    private JTextArea txtUpper;
    private JTextArea txtLower;
    private JSpinner spnRollerLeft;
    private JSpinner spnRollerMiddle;
    private JSpinner spnRollerRight;
    private JCheckBox cbxPrivateKey;
    private JMenuBar jMenuBar;

    private int leftRoller = 0;
    private int middleRoller = 0;
    private int rightRoller = 0;

    private boolean changingRoller = false;

    public LiveCipher(){
        setContentPane(pnlRoot);
        setTitle("Wolfpack: Live Cipherer");
        setIconImage(new ImageIcon(this.getClass().getClassLoader().getResource("submarine-icon.png")).getImage());

        // Add menu-bar
        jMenuBar = new JMenuBar();{
            jMenuBar.setVisible(false);
            this.setJMenuBar(jMenuBar);

            // Add GitHub-button
            JMenu gitHub = new JMenu("GitHub");{
                gitHub.addMouseListener(new MouseListener(){
                    @Override
                    public void mouseClicked(MouseEvent mouseEvent)
                    {
                        if(SwingUtilities.isLeftMouseButton(mouseEvent))
                        {
                            if(Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                                try{
                                    Desktop.getDesktop().browse(new java.net.URI("https://github.com/ChrisAcrobat/Wolfpack-Live-Cipherer/"));
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
                jMenuBar.add(gitHub);
            }

            // Add GitHub-button
            JMenu submarineIcon = new JMenu("Submarine-icon by Elegantthemes");{
                submarineIcon.addMouseListener(new MouseListener(){
                    @Override
                    public void mouseClicked(MouseEvent mouseEvent)
                    {
                        if(SwingUtilities.isLeftMouseButton(mouseEvent))
                        {
                            if(Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                                try{
                                    Desktop.getDesktop().browse(new java.net.URI("http://www.iconarchive.com/show/beautiful-flat-one-color-icons-by-elegantthemes/submarine-icon.html"));
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
                jMenuBar.add(submarineIcon);
            }
        }

        // Check for new release.
        new UpdateChecker(jMenuBar).start();

        // Initiate rollers
        Roller roller3 = new Roller(spnRollerLeft, null, 3);
        Roller roller2 = new Roller(spnRollerMiddle, roller3, 2);
        Roller roller1 = new Roller(spnRollerRight, roller2, 1);

        Roller[] rollerList = new Roller[]{roller1, roller2, roller3};
        primeWheels(rollerList);
        new TextFormatter(roller1, cbxPrivateKey, txtUpper, txtLower).start();

        // Finalize frame
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Add event handlers
        Toolkit.getDefaultToolkit().addAWTEventListener(this::mouseEvent, AWTEvent.MOUSE_EVENT_MASK);
    }

    private void primeWheels(Roller[] rollerList){
        int[] primeList = new int[]{2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307, 311, 313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397};
        for(int index = 0; index < primeList.length; index++){
            rollerList[index%3].addPrime(primeList[index]);
        }
        for(Roller roller : rollerList){
            if(!roller.validate()){
                System.err.println(roller + ": NOT VALID!");
            }
        }
    }

    private void mouseEvent(AWTEvent event){
        if(event instanceof MouseEvent)
        {
            MouseEvent mouseEvent = (MouseEvent) event;

            if(SwingUtilities.isRightMouseButton(mouseEvent) && mouseEvent.isPopupTrigger())
            {
                jMenuBar.setVisible(!jMenuBar.isVisible());
            }
        }
    }
}
