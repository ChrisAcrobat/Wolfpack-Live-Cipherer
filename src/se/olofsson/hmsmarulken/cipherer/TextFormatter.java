package se.olofsson.hmsmarulken.cipherer;

import javax.swing.*;

/**
 * Created on 2016-06-07.
 * @author Christoffer Olofsson
 */
public class TextFormatter extends Thread implements Runnable
{
    private final Cipherer CIPHERER;
    private final JTextArea FIRST;
    private final JTextArea SECOND;

    private String first;
    private String second;
    private boolean busyWait = false;

    private final long SLEEP = 1000/15;

    public TextFormatter(Cipherer cipherer, JTextArea first, JTextArea second)
    {
        CIPHERER = cipherer;

        FIRST = first;
        SECOND = second;

        this.first = first.getText();
        this.second = second.getText();
    }

    @Override
    public void run()
    {
        while(true)
        {
            if(!busyWait)
            {
                if(!first.equals(FIRST.getText()))
                {
                    busyWait = true;
                    cipher(FIRST, SECOND);
                    first = FIRST.getText();

                    continue;
                }

                if(!second.equals(SECOND.getText()))
                {
                    busyWait = true;
                    cipher(SECOND, FIRST);
                    second = SECOND.getText();
                }
            }

            try
            {
                sleep(SLEEP);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void cipher(final JTextArea FROM, final JTextArea TO)
    {
        SwingUtilities.invokeLater(() -> {
            int caretPosition = FROM.getCaretPosition();

            String text = FROM.getText();

            text = text.toUpperCase();
            text = CIPHERER.removeUnsupportedCharacters(text);
            FROM.setText(text);

            if(text.length() < caretPosition)
            {
                caretPosition = text.length();
            }
            FROM.setCaretPosition(caretPosition);

            text = CIPHERER.cipherMessage(text);
            TO.setText(text);
            TO.setCaretPosition(caretPosition);

            busyWait = false;
        });
    }
}
