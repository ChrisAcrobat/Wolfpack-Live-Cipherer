package se.olofsson.hmsmarulken.codecipher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main
{
    public static final String CIPHERABLE_CHARS = new CipherableChars().getChars();

    public static void main(String[] args)
    {
        Cipher cipher = new Cipher();

        int numberOfChars = CIPHERABLE_CHARS.length()/2;
        char[] ciphered = new char[CIPHERABLE_CHARS.length()];

        List charsLeft = new ArrayList<>();
        for(char _char : CIPHERABLE_CHARS.toCharArray())
        {
            charsLeft.add(_char);
        }

        Scanner scanner = new Scanner(System.in);

        for(int c = 0; c < numberOfChars; c++)
        {
            char charOrigin = (char) charsLeft.get(0);

            while(true)
            {
                char _char;

                if(c < numberOfChars - 1)   // If more than one choices are left.
                {
                    System.out.println("Press ciphered of: " + charOrigin + " (chars left: " + (numberOfChars - c - 1) + ")");
                    String line = scanner.nextLine();

                    if(line.length() != 1)  // If input is more than one char: Do not accept - throw away and try again.
                    {
                        continue;
                    }

                    _char = line.toUpperCase().charAt(0);

                    if(!charsLeft.contains(_char) || _char == charOrigin)  // Check if char is valid. If not: Do not accept - throw away and try again.
                    {
                        continue;
                    }
                    charsLeft.remove((Character) _char);
                    charsLeft.remove((Character) charOrigin);
                }
                else
                {
                    _char = (char) charsLeft.get(1);
                }

                int index = CIPHERABLE_CHARS.indexOf(_char);
                int indexOrigin = CIPHERABLE_CHARS.indexOf(charOrigin);

                ciphered[indexOrigin] = _char;
                ciphered[index] = charOrigin;
                break;
            }
        }

        System.out.println("ciphered: " + new String(ciphered));
    }
}
