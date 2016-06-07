package se.olofsson.hmsmarulken.cipherer.exceptions;

/**
 * Created by Chris_Acrobat on 2016-05-29.
 */
public class DuplicatesOfCipherableCharsException extends Exception
{
    public DuplicatesOfCipherableCharsException()
    {
        super("Cipherable chars has duplicates.");
    }

    public DuplicatesOfCipherableCharsException(char _char)
    {
        super("Cipherable chars has duplicates: " + _char);
    }
}
