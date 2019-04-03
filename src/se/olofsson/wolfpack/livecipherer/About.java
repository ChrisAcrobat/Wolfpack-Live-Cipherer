package se.olofsson.wolfpack.livecipherer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

/**
 * Created by Chris_Acrobat on 2016-06-11.
 */
public class About extends JFrame
{
    private JPanel pnlRoot;
    private JButton elegantthemesAtIconArchiveComButton;

    public About()
    {
        setTitle("About");
        setContentPane(pnlRoot);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

        elegantthemesAtIconArchiveComButton.addActionListener(e -> About.this.openSourceToSubmarineIcon());
    }

    private void openSourceToSubmarineIcon()
    {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;

        if(desktop != null && desktop.isSupported(Desktop.Action.BROWSE))
        {
            try
            {
                desktop.browse(URI.create("http://www.iconarchive.com/show/beautiful-flat-one-color-icons-by-elegantthemes/submarine-icon.html"));
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}