package objects.pattern;

import objects.Rules.Regole;
import objects.Dices.Dadi;
import objects.Dices.Doppi;
import objects.Dices.Singolo;

public class SingletonDadi {

    private static Dadi istanza = null;

    private SingletonDadi(){}

    public static void reset(){ istanza = null;}

    public static synchronized Dadi getIstanza(){
        if(istanza == null){
            istanza = SingletonRegole.getIstanza().contains(Regole.DEFAULT)? new Singolo(): new Doppi();
            }
        return istanza;
    }
}
