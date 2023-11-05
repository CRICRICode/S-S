package game.Moves;

import game.Game;
import gui.Main;
import objects.Board.Casella;
import objects.Board.CasellaSpeciale;
import objects.Board.Scacchiera;
import objects.pattern.MovimentoStrategy;
import objects.Player.Giocatore;

public class CercaCasella {
    private MovimentoStrategy movimento;

    public void setMovimentoStrategy(MovimentoStrategy movimento) {
        this.movimento = movimento;
    }

    public static int casaSucc(Giocatore g, int caselle) {
        int tmp = g.getPosizione()+ caselle, nc = Game.getScacchiera().getNCaselle();
        return tmp>nc? nc-(tmp%nc) : tmp;
    }

    static public MovimentoStrategy cercaCasella(Giocatore g, int caselle) throws Main.Eccezione {
        Scacchiera scacchiera = Game.getScacchiera();

        int cSucc = casaSucc(g, caselle);

        Casella succ = scacchiera.getCasella(cSucc);
        CasellaSpeciale spec = succ.isTrue();
        if (spec == null) {
            return new CasellaSemplice();
        }

        switch (spec) {
            default:
                System.out.println("Casella semplice");
                return new CasellaSemplice();

            case INIZIO_SC:     //Essendo che la scala e il serpente funzionano in modo quasi analogo faccio un merge dei case
            case TESTA_SE:
                System.out.println("Casella Scala o Serpente");
                return new CasellaSS();

            case DADI:
                g.setDoppioSei(false);
                System.out.println("Casella Dadi");
                return new CasellaDadi();

            case MOLLA:
                g.setDoppioSei(false);
                System.out.println("Casella Molla");
                return new CasellaMolla();

            case PESCA:
                g.setDoppioSei(false);
                System.out.println("Casella Pesca");
                return new CasellaPesca();

            case LOCANDA:
                g.setTurniFermo(3);
                System.out.println("Casella Fermo 3 turni");
                break;

            case PANCA:
                g.setTurniFermo(1);
                System.out.println("Casella Fermo 1 turno");
                break;


        }

        return new CasellaSemplice();
    }

    public Giocatore sposta(Giocatore g, int c) throws Main.Eccezione {
        return movimento.muovi(g, c);
    }


}
