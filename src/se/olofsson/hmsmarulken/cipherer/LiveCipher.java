package se.olofsson.hmsmarulken.cipherer;

import javax.swing.*;

/**
 * Created on 2016-06-05.
 * @author Christoffer Olofsson
 */
public class LiveCipher extends JFrame
{
    private final Cipherer CIPHERER;

    private JPanel lblRoot;
    private JTextArea txtUpper;
    private JTextArea txtLower;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JComboBox comboBox3;

    int leftRoller = 3;
    int middleRoller = 6;
    int rightRoller = 6;

    public LiveCipher()
    {
        setContentPane(lblRoot);

        CIPHERER = new Cipherer();
        CIPHERER.setRollers(leftRoller, middleRoller, rightRoller);
        new TextFormatter(CIPHERER, txtUpper, txtLower).start();

        pack();
        setLocationRelativeTo(null);
        setTitle("HMS Marulken: Live Cipherer {" + leftRoller + ", " + middleRoller + ", " + rightRoller + "}");
        setVisible(true);
    }
}
