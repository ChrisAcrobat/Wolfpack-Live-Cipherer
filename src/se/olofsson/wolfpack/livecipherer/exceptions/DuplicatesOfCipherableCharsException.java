package se.olofsson.wolfpack.livecipherer.exceptions;

/**
 * Created on 2016-05-29.
 * @author Christoffer Olofsson
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
