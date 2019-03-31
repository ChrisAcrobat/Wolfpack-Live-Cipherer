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
                "AAA-HEJ I STUGAN-HEJ I STUGAN",
                "BBB-HEJ I STUGAN-HEJ I STUGAN",
                "AAA-HELLO WORLD-HELLO WORLD",
                "BBB-HELLO WORLD-HELLO WORLD"
        };
        for(String debugPrint : debugPrints){
            debugOutput(debugPrint);
        }
    }

    private void debugOutput(String debugPrint){
        RIGHT_ROLLER.setValue(debugPrint.charAt(0)).setValue(debugPrint.charAt(1)).setValue(debugPrint.charAt(2));
        String[] data = debugPrint.split("-");
        String shifted = cipherMessage(data[1]);
        System.out.println(data[0] + ": " + data[1] + " => " + shifted + " (" + (shifted==data[2]) + ")");
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
        boolean first = true;
        String cipherMessage = "";
        for(String subMessage : message.split("\n", -1)){
            if(!first){
                cipherMessage += "\n";
            }else{
                first = false;
            }
            int skipped = 0;
            for(Character character : subMessage.toCharArray()){
                if('A' <= character && character <= 'Z'){
                    RIGHT_ROLLER.stepNext();
                    cipherMessage += cipherCharacter(character);
                }else{
                    skipped++;
                    if('0' <= character && character <= '9'){
                        cipherMessage += character;
                    }else{
                        cipherMessage += ' ';
                    }
                }
            }
            RIGHT_ROLLER.step(-subMessage.length() + skipped);
        }
        return cipherMessage;
    }

    private Character cipherCharacter(Character character){
        int number = RIGHT_ROLLER.getCipheredCharIndex();
        ArrayList<Character> charactersToBeMapped = new ArrayList<>();
        for(char c = 'A'; c <= 'Z'; c++){
            charactersToBeMapped.add(c);
        }
        HashMap<Character, Character> keyMap = new HashMap<>();
        while(0 < charactersToBeMapped.size()){
            Character item = charactersToBeMapped.get(number % charactersToBeMapped.size());
            charactersToBeMapped.remove(item);
            Character chr = charactersToBeMapped.get(number % charactersToBeMapped.size());
            charactersToBeMapped.remove(chr);
            keyMap.put(item, chr);
            keyMap.put(chr, item);
        }
        return keyMap.get(character);
    }
}
