package se.olofsson.hmsmarulken.cipherer.exceptions;

/**
 * Created on 2016-05-29.
 * @author Christoffer Olofsson
 */
public class CipherAlreadyExistException extends Exception
{
    public CipherAlreadyExistException(int leftRoller, int middleRoller, int rightRoller)
    {
        super("Cipherer (" + leftRoller + ", " + middleRoller + ", " + rightRoller + ") already exist.");
    }
}
