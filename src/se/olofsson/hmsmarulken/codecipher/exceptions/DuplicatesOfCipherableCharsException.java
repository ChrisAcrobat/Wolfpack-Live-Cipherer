package se.olofsson.hmsmarulken.codecipher.exceptions;

/**
 * Created by Chris_Acrobat on 2016-05-29.
 */
public class DuplicatesOfCipherableCharsException extends Exception
{
    public DuplicatesOfCipherableCharsException()
    {
        super("Cipherable chars has duplicates.");
    }
}
