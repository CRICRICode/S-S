package game.Moves;

import game.Game;
import gui.Main;
import objects.Board.CasellaSpeciale;
import objects.pattern.Observer;
import objects.pattern.MovimentoStrategy;
import objects.Player.Giocatore;

import java.util.ArrayList;
import java.util.List;

public class CasellaSemplice implements MovimentoStrategy {

    private static List<Observer> observers = new ArrayList<>();
    private CercaCasella movimento = new CercaCasella();
    public static void addObserver(Observer o) { observers.add(o);}

    @Override
    public Giocatore muovi(Giocatore g, int caselle) throws Main.Eccezione {
        g.setPosizione(CercaCasella.casaSucc(g, caselle));
        if(g.isDoppioSei()){
            g.setDoppioSei(false);
            for (Observer o : observers) {
                o.update("Doppio 6: +12");
            }
            movimento.setMovimentoStrategy(CercaCasella.cercaCasella(g, caselle));
            movimento.sposta(g, caselle);
        }
        CasellaSpeciale cs=Game.getScacchiera().getCasella(g.getPosizione()).isTrue();
        if(cs!=null) {
            if (cs.equals(CasellaSpeciale.LOCANDA) || cs.equals(CasellaSpeciale.PANCA)) {
                String tmp = cs.equals(CasellaSpeciale.LOCANDA) ? "3 turni" : "1 turno";
                for (Observer o : observers) {
                    o.update("Casella corrente: " + cs + " fermo " + tmp);
                }
            }
        }
        if(g.getPosizione()==Game.getScacchiera().getNCaselle())
            throw new Main.Eccezione();

        return g;
    }
}
