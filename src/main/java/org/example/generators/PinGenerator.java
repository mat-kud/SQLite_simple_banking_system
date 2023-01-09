package org.example.generators;

import java.util.Random;

public class PinGenerator {
    private final Random random = new Random();

    public String generatePinNumber(){
        int low = 1000;
        int high = 10000;
        return  String.valueOf(random.nextInt(high-low) + low);
    }
}
