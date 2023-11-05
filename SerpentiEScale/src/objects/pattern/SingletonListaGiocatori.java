package objects.pattern;

import objects.Player.Giocatore;
import objects.Player.Giocatori;

import java.util.List;

public class SingletonListaGiocatori {
    private static Giocatori istanza= null; //dichiaro la mia istanza a null

    private SingletonListaGiocatori(){} //impedisco l'istanza di oggetti da parte di classi esterne

    public SingletonListaGiocatori(List<Giocatore> istanza) {this.istanza = new Giocatori(istanza);}

    public static synchronized Giocatori getIstanza(){    //Metodo per accedere al singleton
        return istanza;
    }

}
