package se.olofsson.hmsmarulken.cipherer;

import se.olofsson.hmsmarulken.cipherer.exceptions.CipherAlreadyExistException;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created on 2016-06-03.
 * @author Christoffer Olofsson
 */
public class Cipherer
{
    private final Map<Integer, Map<Integer, Map<Integer, String>>> KNOWN_CIPHERS = new HashMap<>();

    int leftRoller;
    int middleRoller;
    int rightRoller;

    Cipherer()
    {
        try
        {
            addCipher(0, 0, 0, "BADCFEHGJILKNMPORQTSVUXWZY1032547698");
            addCipher(3, 6, 6, "RF4YWB08TPZVQX6JMA2I3LENDKG5SUC1O9H7");
            addCipher(6, 1, 0, "QT0FYDSXKNI84J91A6GBWZUHEVCP57M2R3LO");
        }
        catch(CipherAlreadyExistException e)
        {
            e.printStackTrace();
        }
    }

    public boolean addCipher(int leftRoller, int middleRoller, int rightRoller, String cipher) throws CipherAlreadyExistException
    {
        Map leftRollerMap = KNOWN_CIPHERS.get(leftRoller);
        if(leftRollerMap == null)
        {
            leftRollerMap = new HashMap<>();
            KNOWN_CIPHERS.put(leftRoller, leftRollerMap);
        }

        Map middleRollerMap = (Map) leftRollerMap.get(middleRoller);
        if(middleRollerMap == null)
        {
            middleRollerMap = new HashMap<>();
            leftRollerMap.put(middleRoller, middleRollerMap);
        }

        String storedCipher = (String) middleRollerMap.get(rightRoller);
        if(storedCipher != null)
        {
            throw new CipherAlreadyExistException(leftRoller, middleRoller, rightRoller);
        }

        middleRollerMap.put(rightRoller, cipher.toUpperCase());

        return true;
    }

    public String getCipher(int leftRoller, int middleRoller, int rightRoller)
    {
        return KNOWN_CIPHERS.get(leftRoller).get(middleRoller).get(rightRoller);
    }

    public String removeUnsupportedCharacters(String text)
    {
        Set<Character> uniqueChars = new LinkedHashSet<>();

        for(char _char : text.toCharArray())
        {
            uniqueChars.add(_char);
        }

        for(char _char : uniqueChars)
        {
            if(!Main.SUPPORTED_CHARACTERS.contains(_char))
            {
                text = text.replaceAll("\\Q" + _char + "\\E", "");
            }
        }

        return text;
    }

    public void setRollers(int leftRoller, int middleRoller, int rightRoller)
    {
        this.leftRoller = leftRoller;
        this.middleRoller = middleRoller;
        this.rightRoller = rightRoller;
    }

    public String cipherMessage(String message)
    {
        String cipher = getCipher(leftRoller, middleRoller, rightRoller);

        char[] chars = message.toCharArray();

        for(int c = 0; c < chars.length; c++)
        {
            char _char = chars[c];

            int index = cipher.indexOf(_char);

            if(0 <= index)
            {
                chars[c] = Main.CIPHERABLE_CHARS.charAt(index);
            }
        }

        return new String(chars);
    }
}
