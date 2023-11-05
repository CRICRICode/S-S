package objects.Snake;

import java.util.Random;

//La struttura dei metodi è uguale a quella delle Scale
public class Serpenti {
    private int testa;
    private int coda;

    public Serpenti(int nCaselle) {
        Random random = new Random();
        coda = generateRandomExcluding(random, 1, nCaselle, -1);
        testa = generateRandomExcluding(random, 1, coda, -1);
    }

    private int generateRandomExcluding(Random random, int min, int max, int excludedValue) {
        int randomValue;
        do {
            randomValue = random.nextInt(max - min) + min;
        } while (randomValue == excludedValue);
        return randomValue;
    }


    public int getTesta() {
        return testa;
    }

    public int getCoda() {
        return coda;
    }


}

