package se.olofsson.wolfpack.livecipherer;

import javax.swing.*;

public class Roller
{
    private final JSpinner JSPINNER;
    private final Roller NEXT_ROLLER;
    private final int OFFSET;
    private final int[] PRIME_LIST = new int[26];

    public Roller(JSpinner spnRoller, Roller nextRoller, int offset){
        JSPINNER = spnRoller;
        NEXT_ROLLER = nextRoller;
        OFFSET = offset;

        Character[] characterArray = new Character['Z'-'A'+3];
        for(char character = 'A'-1; character <= 'Z'+1; character++){
            characterArray['Z'+1-character] = character;
        }
        JSPINNER.setModel(new SpinnerListModel(characterArray));

        JSPINNER.setValue('A');
        JSPINNER.addChangeListener(e -> {
            if('Z'+1 == (Character) JSPINNER.getValue())
                JSPINNER.setValue('A');
            else if('A'-1 == (Character) JSPINNER.getValue())
                JSPINNER.setValue('Z');
        });
    }

    public void addPrime(int number){
        for(int index = 0; index < PRIME_LIST.length; index++){
            if(PRIME_LIST[index] == 0){
                PRIME_LIST[index] = number;
                break;
            }
        }
    }

    public int getCipheredCharIndex(){
        Character currentCharacter = (Character) JSPINNER.getValue();
        int charIndex = currentCharacter - 'A';
        int prime = PRIME_LIST[(charIndex + OFFSET) % 26];
        return NEXT_ROLLER == null ? prime : NEXT_ROLLER.getCipheredCharIndex() * prime;
    }
}
