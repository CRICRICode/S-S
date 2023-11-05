package objects.Board;

import objects.pattern.Observer;

import java.util.ArrayList;
import java.util.List;

public class Casella {
    private int id;
    private CasellaSpeciale tipo;
    private boolean speciale = false;
    private int serpenteOScala;
    private static List<Observer> obs=new ArrayList<>();

    public static void addObserver(Observer o){
        obs.add(o);
    }

    public Casella(int id) {
        this.id = id;
    }

    public void casellaSpeciale(CasellaSpeciale tipo) {
        this.tipo = tipo;
        speciale = true;
    }

    public CasellaSpeciale isTrue(){
        if(!speciale) return null;
        return tipo;
    }

    public int getId() {
        return id;
    }
    public void setSerpenteOScala(int s){
        serpenteOScala = s;
    }

    public int getSerpenteOScala() {
        return serpenteOScala;
    }

}
