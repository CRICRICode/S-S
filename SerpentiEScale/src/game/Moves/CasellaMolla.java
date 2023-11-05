package game.Moves;

import gui.Main;
import objects.Player.Giocatore;
import game.Game;
import objects.pattern.Observer;
import objects.pattern.MovimentoStrategy;

import java.util.ArrayList;
import java.util.List;

public class CasellaMolla implements MovimentoStrategy {
    private static List<Observer> observers = new ArrayList<>();
    private CercaCasella movimento = new CercaCasella();
    public static void addObserver(Observer o) { observers.add(o);}
    public CasellaMolla(){}

    @Override
    public Giocatore muovi(Giocatore player, int caselle) throws Main.Eccezione {
        player.setPosizione(CercaCasella.casaSucc(player, caselle));
        observers.forEach(o -> o.update("MOLLA: " + (player.getPosizione() + caselle)));
        movimento.setMovimentoStrategy(CercaCasella.cercaCasella(player, caselle));
        movimento.sposta(player, caselle);

        if (player.getPosizione() == Game.getScacchiera().getNCaselle()) {
            throw new Main.Eccezione();
        }

        return player;
    }

}
