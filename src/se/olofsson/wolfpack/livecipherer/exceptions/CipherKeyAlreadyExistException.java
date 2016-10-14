package se.olofsson.wolfpack.livecipherer.exceptions;

/**
 * Created on 2016-05-29.
 * @author Christoffer Olofsson
 */
public class CipherKeyAlreadyExistException extends Exception
{
    public CipherKeyAlreadyExistException(int leftRoller, int middleRoller, int rightRoller)
    {
        super("Cipherer (" + leftRoller + ", " + middleRoller + ", " + rightRoller + ") already exist.");
    }
}
