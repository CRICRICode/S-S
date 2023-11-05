package objects.pattern;

import gui.Main;
import objects.Player.Giocatore;

public interface MovimentoStrategy {
    Giocatore muovi(Giocatore g, int caselle) throws Main.Eccezione;
}
