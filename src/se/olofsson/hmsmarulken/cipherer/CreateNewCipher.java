package se.olofsson.hmsmarulken.cipherer;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Chris_Acrobat on 2016-06-11.
 */
public class CreateNewCipher extends JFrame
{
    private JPanel pnlRoot;
    private JTextField txtNewCipher;
    private JPanel pnlChars;

    public CreateNewCipher()
    {
        setTitle("Create new cipher");
        setContentPane(pnlRoot);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        final char[] CHARS = Main.CIPHERABLE_CHARS.toCharArray();
        for(int c = 0; c < CHARS.length/2; c++)
        {
            char _char = CHARS[c];
            PanelCipherPair panelCipherPair = new PanelCipherPair(_char);
            pnlChars.add(panelCipherPair);
        }

        txtNewCipher.setHorizontalAlignment(JTextField.CENTER);

        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public String getNewCipher()
    {
        int numberOfChars = Main.CIPHERABLE_CHARS.length()/2;
        char[] ciphered = new char[Main.CIPHERABLE_CHARS.length()];

        List charsLeft = new ArrayList<>();
        for(char _char : Main.CIPHERABLE_CHARS.toCharArray())
        {
            charsLeft.add(_char);
        }

        Scanner scanner = new Scanner(System.in);

        for(int c = 0; c < numberOfChars; c++)
        {
            char charOrigin = (char) charsLeft.get(0);

            while(true)
            {
                char _char;

                if(c < numberOfChars - 1)   // If more than one choices are left.
                {
                    System.out.println("Press ciphered of: " + charOrigin + " (chars left: " + (numberOfChars - c - 1) + ")");
                    String line = scanner.nextLine();

                    if(line.length() != 1)  // If input is more than one char: Do not accept - throw away and try again.
                    {
                        continue;
                    }

                    _char = line.toUpperCase().charAt(0);

                    if(!charsLeft.contains(_char) || _char == charOrigin)  // Check if char is valid. If not: Do not accept - throw away and try again.
                    {
                        continue;
                    }
                    charsLeft.remove((Character) _char);
                    charsLeft.remove((Character) charOrigin);
                }
                else
                {
                    _char = (char) charsLeft.get(1);
                }

                int index = Main.CIPHERABLE_CHARS.indexOf(_char);
                int indexOrigin = Main.CIPHERABLE_CHARS.indexOf(charOrigin);

                ciphered[indexOrigin] = _char;
                ciphered[index] = charOrigin;
                break;
            }
        }

        String cipher = new String(ciphered);

        System.out.println("cipher: " + cipher);
        return cipher;
    }
}
