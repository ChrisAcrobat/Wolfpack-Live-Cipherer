package se.olofsson.wolfpack.livecipherer;

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
    private final Cipherer CIPHERER;
    private final int ABOUT_BUTTON = 0;
    private final Roller ROLLER_1;
    private final Roller ROLLER_2;
    private final Roller ROLLER_3;
    private final HashMap<Character, Character> KEY_MAP = new HashMap<>();

    private JPanel pnlRoot;
    private JTextArea txtUpper;
    private JTextArea txtLower;
    private JSpinner spnRollerLeft;
    private JSpinner spnRollerMiddle;
    private JSpinner spnRollerRight;
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
                                    Desktop.getDesktop().browse(new java.net.URI("https://github.com/ChrisAcrobat/Wolfpack-Live-Cipherer/releases/latest/"));
                                }catch(IOException e){
                                    e.printStackTrace();
                                }catch(URISyntaxException e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        //
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        //
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        //
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        //
                    }
                });
                jMenuBar.add(gitHub);
            }

            // Add about-button
            JMenu about = new JMenu("About");{
                about.addMouseListener(new MouseListener(){
                    @Override
                    public void mouseClicked(MouseEvent mouseEvent)
                    {
                        if(SwingUtilities.isLeftMouseButton(mouseEvent))
                        {
                            buttonPressed(ABOUT_BUTTON);
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        //
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        //
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        //
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        //
                    }
                });
                jMenuBar.add(about);
            }
        }

        // Initiate rollers
        ROLLER_3 = new Roller(spnRollerLeft, null, 3);
        ROLLER_2 = new Roller(spnRollerMiddle, ROLLER_3, 2);
        ROLLER_1 = new Roller(spnRollerRight, ROLLER_2, 1);
        primeWheels();
        generateCurrentCharMapping();
        CIPHERER = new Cipherer();
        CIPHERER.setRollers(leftRoller, middleRoller, rightRoller);
        new TextFormatter(CIPHERER, txtUpper, txtLower).start();
        rollerChanged();

        // Finalize frame
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Add event handlers
        Toolkit.getDefaultToolkit().addAWTEventListener(e -> {mouseEvent(e);}, AWTEvent.MOUSE_EVENT_MASK);

        String[] debugPrints = {
                "AAAACO",
                "AAABZR",
                "AAACAY",
                "AABBRC",
                "AACCBB",
                "ABCABN",
                "ABCEQF",
                "BCDEVX",
                "AAADIG",
                "AAAEQK",
                "AAAFLV",
                "AAAGXD",
                "AAAHWS",
                "ZZZZIB",
                "BBBCAP",
                "CCCBRA",
                "FFFFWA",
                "BFGAQZ",
                "HEJATK",
                "VILAQZ",
                "MUSAZZ",
                "BBBBYR",
                "CABABP",
                "CABSLJ",
                "AAAZBP",
                "AAARJB",
                "ZZZBFZ",
                "CCCABD",
                "CCCRSY",
                "CCCPFN",
                "TABBDQ",
                "TAKBIP",
                "YENBEJ",
                "TOBBUH",
                "TORBPJ",
                "LOKEDK",
                "NEOOMA",
                "NEOEVV",
                "NEORKU",
                "LUNAQI",
                "TOBAKO",
                "CAABOR"
        };
        for (String string : debugPrints){
            debugOutput(string.charAt(0), string.charAt(1), string.charAt(2), string.charAt(3), string.charAt(4));
        }
    }

    private void debugOutput(char leftRollerChar, char middleRollerChar, char rightRollerChar, char character, char target){
        spnRollerLeft.setValue(leftRollerChar);
        spnRollerMiddle.setValue(middleRollerChar);
        spnRollerRight.setValue(rightRollerChar);
        generateCurrentCharMapping();
        char shifted = KEY_MAP.get(character);
        System.out.println("" + leftRollerChar + middleRollerChar + rightRollerChar + ":" + character + " => " + shifted + " (" + target + ", " + (shifted==target) + ")");
    }

    private void primeWheels(){
        ArrayList<Integer> primeList = new ArrayList<>();
        int prime = 2;
        int rollerIndex = 0;
        while(primeList.size() < 78){   // A->Z (x3)
            boolean primeFound = true;
            for(int previousPrime : primeList){
                if(prime % previousPrime != 0){
                    continue;
                }
                primeFound = false;
                break;
            }
            if(primeFound){
                primeList.add(prime);
                Roller roller = null;
                switch(rollerIndex%3){
                    case 0: roller = ROLLER_1; break;
                    case 1: roller = ROLLER_2; break;
                    case 2: roller = ROLLER_3; break;
                }
                if(roller != null){
                    roller.addPrime(prime);
                }
                rollerIndex++;
            }
            prime++;
        }
    }

    private void generateCurrentCharMapping(){
        int number = ROLLER_1.getCipheredCharIndex();
        ArrayList<Character> list = new ArrayList<>();
        for(char i = 'A'; i <= 'Z'; i++){
            list.add(i);
        }
        KEY_MAP.clear();
        while(list.size() > 0){
            Character item = list.get(number % list.size());
            list.remove(item);
            Character chr = list.get(number % list.size());
            list.remove(chr);
            KEY_MAP.put(item, chr);
            KEY_MAP.put(chr, item);
        }
    }

    private void rollerChanged(){
        changingRoller = true;

        int[] newSettings = CIPHERER.setRollers(leftRoller, middleRoller, rightRoller);
        leftRoller = newSettings[0];
        middleRoller = newSettings[1];
        rightRoller = newSettings[2];

    //    setTitle("Wolfpack: Live Cipherer");  // TODO: Display public key?

    //    spnRollerLeft.removeAllItems();
    //    spnRollerMiddle.removeAllItems();
    //    spnRollerRight.removeAllItems();

        for(int roller = 0; roller < 3; roller++)
        {
            Integer[] positions = CIPHERER.getRollerPositions(roller);

            for(int p : positions)
            {
                switch(roller)
                {
                    case 0:
    //                    spnRollerLeft.addItem(p);
                        break;

                    case  1:
    //                    spnRollerMiddle.addItem(p);
                        break;

                    case 2:
    //                    spnRollerRight.addItem(p);
                        break;
                }
            }
        }

    //    spnRollerLeft.setSelectedItem(leftRoller);
    //    spnRollerMiddle.setSelectedItem(middleRoller);
    //    spnRollerRight.setSelectedItem(rightRoller);

        txtLower.setText(CIPHERER.cipherMessage(txtUpper.getText()));

        changingRoller = false;
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

    private void buttonPressed(int button){
        switch(button)
        {
            case ABOUT_BUTTON:
                new About();
                break;
        }
    }
}
