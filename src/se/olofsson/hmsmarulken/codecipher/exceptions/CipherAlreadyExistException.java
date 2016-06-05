package se.olofsson.hmsmarulken.codecipher.exceptions;

/**
 * Created by Chris_Acrobat on 2016-05-29.
 */
public class CipherAlreadyExistException extends Exception
{
    public CipherAlreadyExistException(int leftRoller, int middleRoller, int rightRoller)
    {
        super("Cipher (" + leftRoller + ", " + middleRoller + ", " + rightRoller + ") already exist.");
    }
}
