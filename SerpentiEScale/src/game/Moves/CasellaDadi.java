package game.Moves;

import game.Game;
import gui.Main;
import objects.Rules.Regole;
import objects.Dices.Dadi;
import objects.pattern.Observer;
import objects.pattern.SingletonDadi;
import objects.pattern.SingletonRegole;
import objects.pattern.MovimentoStrategy;
import objects.Player.Giocatore;

import java.util.ArrayList;
import java.util.List;

public class CasellaDadi implements MovimentoStrategy {

    private static List<Observer> observers = new ArrayList<>();
    private CercaCasella movimento = new CercaCasella();
    public static void addObserver(Observer ob) {observers.add(ob);}

    public CasellaDadi(){};


    @Override
    public Giocatore muovi(Giocatore player, int caselle) throws Main.Eccezione {

        player.setPosizione(CercaCasella.casaSucc(player, caselle));  //sposto il giocatore (numero casella, non indice)


        Dadi d = SingletonDadi.getIstanza();
        int[] ris = (SingletonRegole.getIstanza().contains(Regole.C) && player.getPosizione() > Game.getScacchiera().getNCaselle() - 7) ?
                new int[] { d.lanciaDadi()[0] } : d.lanciaDadi();
        int nCaselle = (ris.length == 1) ? ris[0] : ris[0] + ris[1];
        System.out.println("casella dadi: avanti di " + nCaselle);


        if (SingletonRegole.getIstanza().contains(Regole.D) && ris.length == 2 && ris[0] == 6) {  //Se ci sta la regola del doppio 6
            player.setDoppioSei(true);
        }

        String msg = (ris.length == 1) ? "Casella dadi, nuovo lancio: " + ris[0] : "Casella dadi, nuovo lancio: " + ris[0] + "-" + ris[1];
        observers.forEach(o -> o.update(msg));

        movimento.setMovimentoStrategy(CercaCasella.cercaCasella(player, nCaselle));
        movimento.sposta(player, nCaselle);

        if (player.getPosizione() == Game.getScacchiera().getNCaselle()) {
            throw new Main.Eccezione();
        }

        return player;
    }

}