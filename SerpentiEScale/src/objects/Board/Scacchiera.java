package objects.Board;

import objects.Rules.Regole;
import objects.Snake.Serpenti;
import objects.Stairs.Scale;
import objects.pattern.SingletonRegole;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Scacchiera {
    private static int righe=10;
    private static int colonne=10;
    private static int nCaselle =100;
    private Casella[] caselle;
    private int nSpecialiPerTipo =3;
    private static List<Serpenti> serpenti;
    private static List<Scale> scale;

    public static int getColonne() {
        return colonne;
    }

    public static int getRighe() {
        return righe;
    }

    public Scacchiera(int nRighe, int nColonne, int nCaselle) {
        righe=nRighe;
        colonne=nColonne;
        Scacchiera.nCaselle =nCaselle;
        caselle = new Casella[nCaselle];
        for(int i=0; i<nCaselle; i++){
            caselle[i] = new Casella(i);
        }
        scale = new ArrayList<>();
        serpenti = new ArrayList<>();
    }

    public static int getNCaselle() {
        return nCaselle;
    }

    public Casella[] getCaselle() {
        return caselle;
    }

    public static List<Serpenti> getSerpenti() {
        return serpenti;
    }

    public static List<Scale> getScale() {
        return scale;
    }

    public void initScacchiera(int nSerpenti, int nScale) {
        for (int i = 0; i < nSerpenti; i++) {
            Serpenti s = new Serpenti(nCaselle);
            setupCaselleSpeciali(s.getCoda() - 1, s.getTesta() - 1, CasellaSpeciale.TESTA_SE, CasellaSpeciale.CODA_SE);
            serpenti.add(s);
        }

        for (int i = 0; i < nScale; i++) {
            Scale s = new Scale(nCaselle);
            setupCaselleSpeciali(s.getInizio() - 1, s.getFine() - 1, CasellaSpeciale.INIZIO_SC, CasellaSpeciale.FINE_SC);
            scale.add(s);
        }
    }

    private void setupCaselleSpeciali(int casella1, int casella2, CasellaSpeciale speciale1, CasellaSpeciale speciale2) {
        if (caselle[casella1].isTrue() == null && caselle[casella2].isTrue() == null) {
            caselle[casella1].casellaSpeciale(speciale1);
            caselle[casella1].setSerpenteOScala(casella2);
            caselle[casella2].casellaSpeciale(speciale2);
        }
    }

    public Casella getCasella(int n){
        return caselle[n-1];
    }

    public void caselleSpeciali() {
        setupCaselleSpecialiPerRegola(Regole.F, CasellaSpeciale.DADI, CasellaSpeciale.MOLLA);
        setupCaselleSpecialiPerRegola(Regole.E, CasellaSpeciale.PANCA, CasellaSpeciale.LOCANDA);
        setupCaselleSpecialiPerRegola(Regole.G, CasellaSpeciale.PESCA);
    }

    private void setupCaselleSpecialiPerRegola(Regole regola, CasellaSpeciale... speciali) {
        if (SingletonRegole.getIstanza().contains(regola)) {
            for (int i = 0; i < nSpecialiPerTipo; i++) {
                for (CasellaSpeciale speciale : speciali) {
                    toSpeciale(speciale, !speciale.equals(CasellaSpeciale.PESCA));
                }
            }
        }
    }

    private void toSpeciale(CasellaSpeciale speciale, boolean flag){
        if(flag){
            int num=new Random().nextInt(nCaselle -1-12);
            while(getCasella(num+1).isTrue()!=null){
                num=new Random().nextInt(nCaselle -1-12);
            }
            caselle[num].casellaSpeciale(speciale);
        }
        else {
            int num = new Random().nextInt(nCaselle - 1);
            while (getCasella(num + 1).isTrue() != null) {
                num = new Random().nextInt(nCaselle - 1);
            }
            caselle[num].casellaSpeciale(speciale);
        }
    }

}
