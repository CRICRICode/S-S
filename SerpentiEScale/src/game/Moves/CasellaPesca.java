package game.Moves;

import gui.Main;
import objects.Player.Giocatore;
import game.Game;
import objects.Rules.Regole;
import objects.Cards.Carte;
import objects.Dices.Dadi;
import objects.pattern.Observer;
import objects.pattern.SingletonCarte;
import objects.pattern.SingletonDadi;
import objects.pattern.SingletonRegole;
import objects.pattern.MovimentoStrategy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CasellaPesca implements MovimentoStrategy {

    private static List<Observer> observers = new ArrayList<>();
    public static void addObserver(Observer o) { observers.add(o);}

    @Override
    public Giocatore muovi(Giocatore player, int caselle) throws Main.Eccezione {
        player.setPosizione(CercaCasella.casaSucc(player, caselle));

        SingletonCarte carte = SingletonCarte.getIstanza();
        Iterator<Carte> it = carte.iterator();
        Carte carta = it.next();
        CercaCasella sposta = new CercaCasella();

        switch (carta) {
            case DADI:
                Dadi d = SingletonDadi.getIstanza();
                int[] ris = d.lanciaDadi();
                String msg = (ris.length == 1) ? "Ha pescato la carta: Dadi, nuovo lancio: " + ris[0] : "Ha pescato la carta: Dadi, nuovo lancio: " + ris[0] + "-" + ris[1];
                observers.forEach(o -> o.update(msg));
                int nCaselle = (ris.length == 1) ? ris[0] : ris[0] + ris[1];
                System.out.println("+" + nCaselle + " caselle");
                sposta.setMovimentoStrategy(CercaCasella.cercaCasella(player, nCaselle));
                sposta.sposta(player, nCaselle);
                break;
            case MOLLA:
                sposta.setMovimentoStrategy(CercaCasella.cercaCasella(player, caselle));
                sposta.sposta(player, caselle);
                break;
            case LOCANDA:
                player.setTurniFermo(3);
                observers.forEach(o -> o.update("Ha pescato la carta: Locanda, fermo 3 turni"));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case PANCHINA:
                if (SingletonRegole.getIstanza().contains(Regole.H) && player.getSostaFree() > 0) {
                    player.setSostaFree(player.getSostaFree() - 1);
                    observers.forEach(o -> o.update("Ha pescato la carta: Panchina, evitata grazie a carta sosta"));
                } else {
                    player.setTurniFermo(player.getTurniFermo() + 1);
                    observers.forEach(o -> o.update("Ha pescato la carta: Panchina, fermo un turno"));
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case DIVIETO_SOSTA:
                observers.forEach(o -> o.update("Carta pescata: Divieto di Sosta"));
                player.setSostaFree(player.getSostaFree() + 1);
                break;
        }

        if (player.getPosizione() == Game.getScacchiera().getNCaselle()) {
            throw new Main.Eccezione();
        }

        return player;
    }

}
