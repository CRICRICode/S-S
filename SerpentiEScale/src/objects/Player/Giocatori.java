package objects.Player;

import java.util.Iterator;
import java.util.List;

public class Giocatori implements Iterable<Giocatore> {
    private int nDefault =5; //numero default di giocatori massimi
    private Giocatore [] giocatori = new Giocatore[nDefault];
    private int nGiocatori=0;

    public Giocatori(List<Giocatore> giocatori){
        giocatori.toArray(this.giocatori);
        nGiocatori=giocatori.size();
    }

    public int getnGiocatori() {
        return nGiocatori;
    }

   public boolean add(Giocatore g){
        for(int i=0; i<5; i++){
            if(giocatori[i]==null){
                giocatori[i] = g;
                nGiocatori++;
                return true;
            }
        }return false;
   }


   private class GiocatoriIterator implements Iterator<Giocatore>{
        private int cur=-1; //giocatore corrente (current)

        @Override
       public boolean hasNext(){ return true;}

       @Override
       public Giocatore next(){
            if(cur== nDefault -1 || giocatori[cur+1]==null){
                cur=-1;
            }
           cur++;
            return giocatori[cur];
       }
    }

    @Override
    public Iterator<Giocatore> iterator() {
        return new GiocatoriIterator();
    }


}


