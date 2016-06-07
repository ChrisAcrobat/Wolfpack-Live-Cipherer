package se.olofsson.hmsmarulken.cipherer.exceptions;

/**
 * Created by Chris_Acrobat on 2016-05-31.
 */
public class UnevenNumberOfCipherableCharsException extends Exception
{
    public UnevenNumberOfCipherableCharsException()
    {
        super("Uneven number of cipherable chars.");
    }
}
