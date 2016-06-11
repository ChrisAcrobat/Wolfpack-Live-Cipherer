package se.olofsson.hmsmarulken.cipherer;

import javax.swing.*;

/**
 * Created by Chris_Acrobat on 2016-06-11.
 */
public class About extends JFrame
{
    private JPanel pnlRoot;

    public About()
    {
        setTitle("About");
        setContentPane(pnlRoot);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
