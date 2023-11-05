package game.Moves;

import gui.Main;
import objects.Player.Giocatore;
import game.Game;
import objects.Board.Casella;
import objects.Board.CasellaSpeciale;
import objects.Board.Scacchiera;
import objects.pattern.Observer;
import objects.pattern.MovimentoStrategy;

import java.util.ArrayList;
import java.util.List;

public class CasellaSS implements MovimentoStrategy {
    private static List<Observer> observers = new ArrayList<>();
    private CercaCasella movimento = new CercaCasella();

    public static void addObserver(Observer o) { observers.add(o);}
    @Override
    public Giocatore muovi(Giocatore player, int caselle) throws Main.Eccezione {
        Scacchiera scacchiera = Game.getScacchiera();
        player.setPosizione(CercaCasella.casaSucc(player, caselle));
        Casella c = scacchiera.getCasella(player.getPosizione());
        CasellaSpeciale cs = c.isTrue();

        observers.forEach(o -> o.update("Casella " + (c.getId() + 1) + " " + cs.name() + " si muove fino alla casella: " + (c.getSerpenteOScala() + 1)));

        if (cs == CasellaSpeciale.INIZIO_SC || cs == CasellaSpeciale.TESTA_SE) {
            player.setPosizione(c.getSerpenteOScala() + 1);
        }

        if (player.isDoppioSei()) {
            player.setDoppioSei(false);
            movimento.setMovimentoStrategy(CercaCasella.cercaCasella(player, caselle));
            movimento.sposta(player, caselle);
        }

        if (player.getPosizione() == Game.getScacchiera().getNCaselle()) {
            throw new Main.Eccezione();
        }

        return player;
    }

}
