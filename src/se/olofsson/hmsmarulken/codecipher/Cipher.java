package se.olofsson.hmsmarulken.codecipher;

import se.olofsson.hmsmarulken.codecipher.exceptions.CipherAlreadyExistException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chris_Acrobat on 2016-06-03.
 */
public class Cipher
{
    Map<Integer, Map<Integer, Map<Integer, String>>> knownCiphers = new HashMap<>();

    Cipher()
    {
        try
        {
            addCipher(3, 6, 6, "RF4YWB08TPZVQX6JMA2I3LENDKG5SUC1O9H7");
        }
        catch(CipherAlreadyExistException e)
        {
            e.printStackTrace();
        }
    }

    public boolean addCipher(int leftRoller, int middleRoller, int rightRoller, String cipher) throws CipherAlreadyExistException
    {
        Map leftRollerMap = knownCiphers.get(leftRoller);
        if(leftRollerMap == null)
        {
            leftRollerMap = new HashMap<>();
            knownCiphers.put(leftRoller, leftRollerMap);
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
        return knownCiphers.get(leftRoller).get(middleRoller).get(rightRoller);
    }
}
