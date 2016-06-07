package se.olofsson.hmsmarulken.cipherer;

import se.olofsson.hmsmarulken.cipherer.exceptions.CipherAlreadyExistException;

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
