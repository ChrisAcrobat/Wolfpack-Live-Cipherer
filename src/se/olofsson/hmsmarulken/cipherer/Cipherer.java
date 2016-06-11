package se.olofsson.hmsmarulken.cipherer;

import se.olofsson.hmsmarulken.cipherer.exceptions.CipherKeyAlreadyExistException;
import se.olofsson.hmsmarulken.cipherer.exceptions.DuplicatesOfCipherableCharsException;
import se.olofsson.hmsmarulken.cipherer.exceptions.UnevenNumberOfCipherableCharsException;

import java.util.*;

/**
 * Created on 2016-06-03.
 * @author Christoffer Olofsson
 */
public class Cipherer
{
    private final Map<Integer, Map<Integer, Map<Integer, String>>> KNOWN_CIPHERS = new HashMap<>();

    private int leftRoller = 0;
    private int middleRoller = 0;
    private int rightRoller = 0;

    Cipherer()
    {
        try
        {
            addCipher(0, 0, 0, "BADCFEHGJILKNMPORQTSVUXWZY1032547698");
            addCipher(3, 6, 6, "RF4YWB08TPZVQX6JMA2I3LENDKG5SUC1O9H7");
            addCipher(6, 1, 0, "QT0FYDSXKNI84J91A6GBWZUHEVCP57M2R3LO");
        }
        catch(CipherKeyAlreadyExistException e)
        {
            e.printStackTrace();
        }
    }

    public boolean addCipher(int leftRoller, int middleRoller, int rightRoller, String cipher) throws CipherKeyAlreadyExistException
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
            throw new CipherKeyAlreadyExistException(leftRoller, middleRoller, rightRoller);
        }

        middleRollerMap.put(rightRoller, cipher.toUpperCase());

        return true;
    }

    public String getCipher(int leftRoller, int middleRoller, int rightRoller)
    {
        return KNOWN_CIPHERS.get(leftRoller).get(middleRoller).get(rightRoller);
    }

    public Integer[] getRollerPositions(int roller)
    {
        Set<Integer> sets = null;
        Integer[] positions = new Integer[]{};

        switch(roller)
        {
            case 0:
                sets = KNOWN_CIPHERS.keySet();
                break;

            case  1:
                if(KNOWN_CIPHERS.containsKey(leftRoller))
                {
                    sets = KNOWN_CIPHERS.get(leftRoller).keySet();
                }
                break;

            case 2:
                if(KNOWN_CIPHERS.containsKey(leftRoller))
                {
                    Map<Integer, Map<Integer, String>> r = KNOWN_CIPHERS.get(leftRoller);

                    if(r.containsKey(middleRoller))
                    {
                        sets = r.get(middleRoller).keySet();
                    }
                }
                break;
        }

        if(sets == null)
        {
            positions = new Integer[]{0};
        }
        else
        {
            positions = sets.toArray(positions);
            Arrays.sort(positions);
        }

        return positions;
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

    public int[] setRollers(int leftRoller, int middleRoller, int rightRoller)
    {
        Integer[] test = new Integer[]{};

        if(!KNOWN_CIPHERS.containsKey(leftRoller))
        {
            leftRoller = KNOWN_CIPHERS.keySet().toArray(test)[0];
        }

        Map<Integer, Map<Integer, String>> r1 = KNOWN_CIPHERS.get(leftRoller);

        if(!r1.containsKey(middleRoller))
        {
            middleRoller = r1.keySet().toArray(test)[0];
        }

        Map<Integer, String> r2 = r1.get(middleRoller);

        if(!r2.containsKey(rightRoller))
        {
            rightRoller = r2.keySet().toArray(test)[0];
        }

        this.leftRoller = leftRoller;
        this.middleRoller = middleRoller;
        this.rightRoller = rightRoller;

        return new int[]{leftRoller, middleRoller, rightRoller};
    }

    public String cipherMessage(String message)
    {
        return cipherMessage(message, getCipher(leftRoller, middleRoller, rightRoller));
    }

    public String cipherMessage(String message, String cipher)
    {
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

    public String validateCipher(String cipher)
    {
        try
        {
            CipherableChars.validate(cipher);
        }
        catch(Exception e)
        {
            return "Invalid cipher";
        }

        if(Main.CIPHERABLE_CHARS.length() != cipher.length())
        {
            return "Invalid cipher - to long or short";
        }

        for(char _char : Main.CIPHERABLE_CHARS.toCharArray())
        {
            int index = cipher.indexOf(_char);

            if(index < 0)
            {
                return "Invalid cipher - unsupported character: " + _char;
            }
        }

        return "";
    }
}
