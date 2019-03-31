package se.olofsson.wolfpack.livecipherer;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created on 2016-06-07.
 * @author Christoffer Olofsson
 */
public class TextFormatter extends Thread implements Runnable
{
    private final Roller RIGHT_ROLLER;
    private final JTextArea FIRST;
    private final JTextArea SECOND;

    private String first;
    private String second;
    private boolean busyWait = false;

    private final long SLEEP = 1000/15;

    public TextFormatter(Roller rightRoller, JTextArea first, JTextArea second)
    {
        RIGHT_ROLLER = rightRoller;
        FIRST = first;
        SECOND = second;

        this.first = first.getText();
        this.second = second.getText();

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
        for(String string : debugPrints){
            debugOutput(string.charAt(0), string.charAt(1), string.charAt(2), string.charAt(3), string.charAt(4), string.charAt(5));
        }
    }

    private void debugOutput(char leftRollerChar, char middleRollerChar, char rightRollerChar, char character, char target, char realTarget){
        RIGHT_ROLLER.setValue(rightRollerChar).setValue(middleRollerChar).setValue(leftRollerChar);
        char shifted = cipherCharacter(character);
        System.out.println("" + leftRollerChar + middleRollerChar + rightRollerChar + ":" + character + " => " + shifted + " (" + target + ", " + (shifted==target) + ")");
        RIGHT_ROLLER.stepNext();
        char shiftedReal = cipherCharacter(character);
        System.err.println("" + leftRollerChar + middleRollerChar + rightRollerChar + ":" + character + " => " + shiftedReal + " (" + realTarget + ", " + (shiftedReal==realTarget) + ")");
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
                }else if(!second.equals(SECOND.getText()))
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

            String text = FROM.getText().toUpperCase();
            // TODO: Remove unsupported characters?
            FROM.setText(text);

            if(text.length() < caretPosition)
            {
                caretPosition = text.length();
            }
            FROM.setCaretPosition(caretPosition);

            TO.setText(cipherMessage(text));
            TO.setCaretPosition(caretPosition);

            busyWait = false;
        });
    }

    private String cipherMessage(String message){
        return message;
    }

    private Character cipherCharacter(Character character){
        int number = RIGHT_ROLLER.getCipheredCharIndex();
        ArrayList<Character> list = new ArrayList<>();
        for(char c = 'A'; c <= 'Z'; c++){
            list.add(c);
        }
        HashMap<Character, Character> keyMap = new HashMap<>();
        while(list.size() > 0){
            Character item = list.get(number % list.size());
            list.remove(item);
            Character chr = list.get(number % list.size());
            list.remove(chr);
            keyMap.put(item, chr);
            keyMap.put(chr, item);
        }

        return keyMap.get(character);
    }
}
