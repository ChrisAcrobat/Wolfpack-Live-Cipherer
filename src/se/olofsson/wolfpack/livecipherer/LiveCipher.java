package se.olofsson.wolfpack.livecipherer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created on 2016-06-05.
 * @author Christoffer Olofsson
 */
public class LiveCipher extends JFrame
{
    private final Cipherer CIPHERER;
    private final int ABOUT_BUTTON = 0;
    private final int NEW_CIPHER_BUTTON = 1;

    private JPanel pnlRoot;
    private JTextArea txtUpper;
    private JTextArea txtLower;
    private JComboBox cbxRollerLeft;
    private JComboBox cbxRollerMiddle;
    private JComboBox cbxRollerRight;
    private JMenuBar jMenuBar;

    private int leftRoller = 0;
    private int middleRoller = 0;
    private int rightRoller = 0;

    private boolean changingRoller = false;

    public LiveCipher()
    {
        setContentPane(pnlRoot);
        setIconImage(new ImageIcon(this.getClass().getClassLoader().getResource("submarine-icon.png")).getImage());

        // Add menu-bar
        jMenuBar = new JMenuBar();
        {
            jMenuBar.setVisible(false);
            this.setJMenuBar(jMenuBar);

            // Add utility-dropdown
            JMenu utility = new JMenu("Utility");
            {
                JMenuItem newCipher = new JMenuItem("Create new cipher");
                newCipher.addMouseListener(new MouseListener(){
                    @Override
                    public void mouseClicked(MouseEvent mouseEvent)
                    {
                        //
                    }

                    @Override
                    public void mousePressed(MouseEvent mouseEvent)
                    {
                        //
                    }

                    @Override
                    public void mouseReleased(MouseEvent mouseEvent)
                    {
                        if(SwingUtilities.isLeftMouseButton(mouseEvent))
                        {
                            buttonPressed(NEW_CIPHER_BUTTON);
                        }
                    }

                    @Override
                    public void mouseEntered(MouseEvent mouseEvent)
                    {
                        //
                    }

                    @Override
                    public void mouseExited(MouseEvent mouseEvent)
                    {
                        //
                    }
                });
                utility.add(newCipher);

                jMenuBar.add(utility);
            }

            // Add about-button
            JMenu about = new JMenu("About");
            {
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

        cbxRollerLeft.addActionListener(e -> rollerLeftChanged());
        cbxRollerMiddle.addActionListener(e -> rollerMiddleChanged());
        cbxRollerRight.addActionListener(e -> rollerRightChanged());
    }

    private void rollerChanged()
    {
        changingRoller = true;

        int[] newSettings = CIPHERER.setRollers(leftRoller, middleRoller, rightRoller);
        leftRoller = newSettings[0];
        middleRoller = newSettings[1];
        rightRoller = newSettings[2];

        setTitle("HMS Marulken: Live Cipherer (" + leftRoller + ", " + middleRoller + ", " + rightRoller + ")");

        cbxRollerLeft.removeAllItems();
        cbxRollerMiddle.removeAllItems();
        cbxRollerRight.removeAllItems();

        for(int roller = 0; roller < 3; roller++)
        {
            Integer[] positions = CIPHERER.getRollerPositions(roller);

            for(int p : positions)
            {
                switch(roller)
                {
                    case 0:
                        cbxRollerLeft.addItem(p);
                        break;

                    case  1:
                        cbxRollerMiddle.addItem(p);
                        break;

                    case 2:
                        cbxRollerRight.addItem(p);
                        break;
                }
            }
        }

        cbxRollerLeft.setSelectedItem(leftRoller);
        cbxRollerMiddle.setSelectedItem(middleRoller);
        cbxRollerRight.setSelectedItem(rightRoller);

        txtLower.setText(CIPHERER.cipherMessage(txtUpper.getText()));

        changingRoller = false;
    }

    private void rollerLeftChanged()
    {
        if(!changingRoller)
        {
            leftRoller = (Integer) cbxRollerLeft.getSelectedItem();
            rollerChanged();
        }
    }

    private void rollerMiddleChanged()
    {
        if(!changingRoller)
        {
            middleRoller = (Integer) cbxRollerMiddle.getSelectedItem();
            rollerChanged();
        }
    }

    private void rollerRightChanged()
    {
        if(!changingRoller)
        {
            rightRoller = (Integer) cbxRollerRight.getSelectedItem();
            rollerChanged();
        }
    }

    private void mouseEvent(AWTEvent event)
    {
        if(event instanceof MouseEvent)
        {
            MouseEvent mouseEvent = (MouseEvent) event;

            if(SwingUtilities.isRightMouseButton(mouseEvent) && mouseEvent.isPopupTrigger())
            {
                jMenuBar.setVisible(!jMenuBar.isVisible());
            }
        }
    }

    private void buttonPressed(int button)
    {
        switch(button)
        {
            case ABOUT_BUTTON:
                new About();
                break;

            case NEW_CIPHER_BUTTON:
                new CreateNewCipher();
                break;
        }
    }
}
