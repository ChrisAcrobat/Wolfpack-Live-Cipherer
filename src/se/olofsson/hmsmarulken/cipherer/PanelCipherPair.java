package se.olofsson.hmsmarulken.cipherer;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

/**
 * Created by Chris_Acrobat on 2016-06-16.
 */
public class PanelCipherPair extends JPanel
{
    private boolean zigZag = true;
    private final char ORIGIN_CHAR;
    private String oldValue = "";

    private JTextField txtCipheredChar;

    PanelCipherPair(char _char)
    {
        super();

        ORIGIN_CHAR = _char;

        txtCipheredChar = new JTextField(2);
        txtCipheredChar.setHorizontalAlignment(JTextField.CENTER);
        txtCipheredChar.setFont(new Font("Consolas", Font.PLAIN, 12));
        txtCipheredChar.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                charUpdated();
            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
                //
            }

            @Override
            public void changedUpdate(DocumentEvent e)
            {
                //
            }
        });

        add(new JLabel(ORIGIN_CHAR + ": "));
        add(txtCipheredChar);
    }

    private void charUpdated()
    {
        zigZag = !zigZag;
        if(zigZag)
        {
            return;
        }

        String input = txtCipheredChar.getText().toUpperCase();

        if(input.equals(String.valueOf(ORIGIN_CHAR)))
        {
            input = String.valueOf(oldValue);
        }
        else if(0 < input.length())
        {
            char firstChar = input.charAt(0);

            input = input.replaceAll(String.valueOf(oldValue), "");

            if(input.length() == 0)
            {
                input = String.valueOf(firstChar);
            }

            oldValue = String.valueOf(input.charAt(0));
        }

        final String OUTPUT = input;
        SwingUtilities.invokeLater(() -> txtCipheredChar.setText(OUTPUT));
    }
}
