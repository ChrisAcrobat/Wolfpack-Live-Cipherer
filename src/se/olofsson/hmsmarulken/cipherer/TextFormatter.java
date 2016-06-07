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

    private void cipher(JTextArea from, JTextArea to)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                int caretPosition = from.getCaretPosition();

                String text = from.getText();

                text = text.toUpperCase();
                text = CIPHERER.removeUnsupportedCharacters(text);

                from.setText(text);
                from.setCaretPosition(caretPosition);

                caretPosition = to.getCaretPosition();
                text = CIPHERER.cipherMessage(text);

                to.setText(text);
                to.setCaretPosition(caretPosition);

                busyWait = false;
            }
        });
    }
}
