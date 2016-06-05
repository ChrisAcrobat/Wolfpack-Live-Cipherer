package se.olofsson.hmsmarulken.codecipher;

import se.olofsson.hmsmarulken.codecipher.exceptions.DuplicatesOfCipherableCharsException;
import se.olofsson.hmsmarulken.codecipher.exceptions.UnevenNumberOfCipherableCharsException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris_Acrobat on 2016-05-29.
 */
public class CipherableChars
{
    public final String chars;
    public static final String PASS_THROUGH = "åäö\n ".toUpperCase();

    public CipherableChars()
    {
        String chars = "";

        for(char c = 'A'; c < ('Z' + 1); c++)
        {
            chars += c;
        }

        for(int i = 0; i < 10; i++)
        {
            chars += i;
        }

        try
        {
            checkIfNumberOfCharsIsEven(chars);
            checkForDuplicates(chars.toCharArray());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            chars = null;
        }

        this.chars = chars;
    }

    /** Check if contains duplicates */
    private void checkForDuplicates(char[] chars) throws DuplicatesOfCipherableCharsException
    {
        List<Character> listOfChars = new ArrayList();
        for(char c: chars)
        {
            if(listOfChars.contains(c))
            {
                throw new DuplicatesOfCipherableCharsException();
            }
            else
            {
                listOfChars.add(c);
            }
        }
    }

    /** Check if number of chars is even */
    private void checkIfNumberOfCharsIsEven(String chars) throws UnevenNumberOfCipherableCharsException
    {
        if(chars.length()%2 == 1)
        {
            throw new UnevenNumberOfCipherableCharsException();
        }
    }

    public String getChars()
    {
        return chars;
    }
}
