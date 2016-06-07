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
    private JComboBox cbxRollerLeft;
    private JComboBox cbxRollerMiddle;
    private JComboBox cbxRollerRight;

    private int leftRoller = 0;
    private int middleRoller = 0;
    private int rightRoller = 0;

    private boolean changingRoller = false;

    public LiveCipher()
    {
        setContentPane(lblRoot);

        CIPHERER = new Cipherer();
        CIPHERER.setRollers(leftRoller, middleRoller, rightRoller);
        new TextFormatter(CIPHERER, txtUpper, txtLower).start();

        rollerChanged();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

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
}
