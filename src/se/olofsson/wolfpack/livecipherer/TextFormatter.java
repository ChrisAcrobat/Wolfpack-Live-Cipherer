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
    private final JCheckBox USE_PRIVATE_KEY;
    private final JTextArea FIRST;
    private final JTextArea SECOND;

    private String first;
    private String second;
    private boolean busyWait = false;

    private final long SLEEP = 1000/15;

    public TextFormatter(Roller rightRoller, JCheckBox usePrivateKey, JTextArea first, JTextArea second)
    {
        RIGHT_ROLLER = rightRoller;
        USE_PRIVATE_KEY = usePrivateKey;
        FIRST = first;
        SECOND = second;

        this.first = first.getText();
        this.second = second.getText();
    }

    /**
     * @param debugPrint Input example: AAA-CLEARTEXT-YJZYTUOIY<br>
     * AAA -> Key<br>
     * CLEARTEXT -> Cleartext message<br>
     * YJZYTUOIY -> Ingame Enigma encryption
     * @return Boolean if cipher worked properly.
     */
    private boolean debugOutput(String debugPrint){
        RIGHT_ROLLER.setState(debugPrint.charAt(2)).setState(debugPrint.charAt(1)).setState(debugPrint.charAt(0));
        String[] data = debugPrint.split("-");
        String shifted = cipherMessage(data[1]);
        String reversed = cipherMessage(shifted);
        String other = cipherMessage(data[2]);
        boolean test_1 = shifted.equals(data[2]);
        boolean test_2 = reversed.equals(data[1]);
        boolean test_3 = other.equals(data[1]);
        if(!test_1 || !test_2 || !test_3){
            System.out.println(data[0] + ":      " + data[1] + "\nTarget:   " + data[2]);
        }
        if(!test_1){
            System.out.println("Ciphered: " + shifted);
        }
        if(!test_2){
            System.out.println("Reversed: " + reversed);
        }
        if(!test_3){
            System.out.println("Other:    " + other);
        }
        if(!test_1 || !test_2 || !test_3){
            System.out.println();
        }

        return test_1 && test_2 && test_3;
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
        String state = RIGHT_ROLLER.getState();
        for(String subMessage : message.split("\n", -1)){
            if(!first){
                cipherMessage += "\n";
            }else{
                first = false;
            }
            for(Character character : subMessage.toCharArray()){
                if(cipherMessage.length() == 3 && USE_PRIVATE_KEY.isSelected()){
                    RIGHT_ROLLER.setState(cipherMessage);
                }
                if('A' <= character && character <= 'Z'){
                    RIGHT_ROLLER.stepNext();
                    cipherMessage += cipherCharacter(character);
                }else{
                    if('0' <= character && character <= '9'){
                        cipherMessage += character;
                    }else{
                        cipherMessage += ' ';
                    }
                }
            }
            RIGHT_ROLLER.setState(state);
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
            Character cipheredCharacter_1 = charactersToBeMapped.get(number % charactersToBeMapped.size());
            charactersToBeMapped.remove(cipheredCharacter_1);
            Character cipheredCharacter_2 = charactersToBeMapped.get(number % charactersToBeMapped.size());
            charactersToBeMapped.remove(cipheredCharacter_2);
            keyMap.put(cipheredCharacter_1, cipheredCharacter_2);
            keyMap.put(cipheredCharacter_2, cipheredCharacter_1);
        }
        return keyMap.get(character);
    }
}
