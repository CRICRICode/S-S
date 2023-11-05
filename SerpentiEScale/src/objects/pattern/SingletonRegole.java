package objects.pattern;

import objects.Rules.Regole;

import java.util.List;

public class SingletonRegole {

    private static List<Regole> istanza=null;

    private SingletonRegole(){}

    public SingletonRegole(List<Regole> istanza){
        this.istanza = istanza;
    }

    public static synchronized List<Regole> getIstanza(){
        return istanza;
    }
}
