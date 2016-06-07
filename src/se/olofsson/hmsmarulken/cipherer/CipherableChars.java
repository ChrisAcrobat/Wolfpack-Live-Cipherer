package se.olofsson.hmsmarulken.cipherer;

import se.olofsson.hmsmarulken.cipherer.exceptions.DuplicatesOfCipherableCharsException;
import se.olofsson.hmsmarulken.cipherer.exceptions.UnevenNumberOfCipherableCharsException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2016-05-29.
 * @author Christoffer Olofsson
 */
public class CipherableChars
{
    public final String chars;
    public static final String PASS_THROUGH = "åäö\n ".toUpperCase();

    public CipherableChars(List<Character> supportedCharacters)
    {
        String chars = "";

        for(char c = 'A'; c <= 'Z'; c++)
        {
            chars += c;
            supportedCharacters.add(c);
        }

        for(int i = 0; i < 10; i++)
        {
            chars += i;
            supportedCharacters.add((String.valueOf(i)).charAt(0));
        }

        for(char _char : PASS_THROUGH.toCharArray())
        {
            supportedCharacters.add(_char);
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
                throw new DuplicatesOfCipherableCharsException(c);
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
