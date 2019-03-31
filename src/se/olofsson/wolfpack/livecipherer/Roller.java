package se.olofsson.wolfpack.livecipherer;

import javax.swing.*;

public class Roller
{
    private final JSpinner JSPINNER;
    private final Roller NEXT_ROLLER;
    private final int OFFSET;
    private final int[] PRIME_LIST = new int['Z'-'A'+1];

    private boolean isValid = false;

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
        JSPINNER.addMouseWheelListener(e -> step(e.getWheelRotation()));
    }

    public Roller setValue(Character character){
        JSPINNER.setValue(character);
        return NEXT_ROLLER;
    }

    public void addPrime(int number){
        isValid = false;
        for(int index = 0; index < PRIME_LIST.length; index++){
            if(PRIME_LIST[index] == 0){
                PRIME_LIST[index] = number;
                isValid = true;
                break;
            }
        }
    }

    public boolean validate(){
        return isValid && PRIME_LIST[PRIME_LIST.length - 1] != 0;
    }

    public int getCipheredCharIndex(){
        Character currentCharacter = (Character) JSPINNER.getValue();
        int charIndex = currentCharacter - 'A';
        int prime = PRIME_LIST[(charIndex + OFFSET) % PRIME_LIST.length];
        return NEXT_ROLLER == null ? prime : NEXT_ROLLER.getCipheredCharIndex() * prime;
    }

    public void step(int steps){
        while(steps != 0){
            if(0 < steps){
                steps--;
                stepNext();
            }else{
                steps++;
                stepPrevious();
            }
        }
    }

    public void stepNext(){
        Character nextValue = (Character) JSPINNER.getPreviousValue();
        JSPINNER.setValue(nextValue);
        if(JSPINNER.getValue() != nextValue && NEXT_ROLLER != null){
            NEXT_ROLLER.stepNext();
        }
    }

    public void stepPrevious(){
        Character previousValue = (Character) JSPINNER.getNextValue();
        JSPINNER.setValue(previousValue);
        if(JSPINNER.getValue() != previousValue && NEXT_ROLLER != null){
            NEXT_ROLLER.stepPrevious();
        }
    }
}
