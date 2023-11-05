package objects.Stairs;

import java.util.Random;

//La struttura dei metodi è uguale a quella dei Serpenti
public class Scale {
    private int inizio;
    private int fine;

    public Scale(int nCaselle) {
        Random random = new Random();
        fine = random.nextInt(nCaselle - 2) + 2;  // Genera un numero casuale tra 2 e nCaselle - 1
        inizio = random.nextInt(fine - 2) + 1;      // Genera un numero casuale tra 2 e fine - 1
    }

    public int getInizio() {
        return inizio;
    }

    public int getFine() {
        return fine;
    }

}


