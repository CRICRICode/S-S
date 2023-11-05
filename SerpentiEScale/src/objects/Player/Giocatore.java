package objects.Player;

import java.util.Objects;

public class Giocatore {
    private static int prossimoId = 1; // Variabile statica per tenere traccia dell'ID successivo
    private int id;
    private int turniFermo = 0;
    private int sosta = 0;
    private int posizione;
    private boolean doppioSei = false;


    public Giocatore() {
        this.id = prossimoId; // Assegna l'ID successivo al giocatore appena creato
        prossimoId++; // Incrementa l'ID successivo per il prossimo giocatore
    }
    public Giocatore(int id, int posizione) {
        this.id = id;
        this.posizione = posizione;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Giocatore giocatore = (Giocatore) o;
        return Objects.equals(id,giocatore.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public int getId() {
        return id;
    }

    public int getPosizione() {
        return posizione;
    }

    public void setPosizione(int posizione) {
        this.posizione = posizione;
    }

    public int getTurniFermo() {
        return turniFermo;
    }

    public void setTurniFermo(int turniFermo) {
        this.turniFermo = turniFermo;
    }

    public int getSostaFree() {
        return sosta;
    }

    public void setSostaFree(int sosta) {
        this.sosta = sosta;
    }

    public boolean isDoppioSei() {
        return doppioSei;
    }

    public void setDoppioSei(boolean doppioSei) {
        this.doppioSei = doppioSei;
    }
}


