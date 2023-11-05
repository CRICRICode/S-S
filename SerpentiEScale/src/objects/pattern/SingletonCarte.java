package objects.pattern;

import objects.Rules.Regole;
import objects.Cards.Carte;

import java.util.Iterator;

public class SingletonCarte implements Iterable<Carte> {

    private static Carte[] mazzo = new Carte[40];
    private static SingletonCarte singletonCarte = new SingletonCarte();

    private SingletonCarte() {
        reset();
    }

    public static SingletonCarte getIstanza() {
        return singletonCarte;
    }

    public static void reset() {
        for (int i = 0; i < mazzo.length; i++) {
            mazzo[i] = null;
        }
    }

    @Override
    public Iterator<Carte> iterator() {
        return new CarteIterator();
    }

    private class CarteIterator implements Iterator<Carte> {

        private int corrente = -1;

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public Carte next() {
            if (corrente == mazzo.length - 1) {
                corrente = -1;
            }
            corrente++;
            if (mazzo[corrente] == null) {
                double num = Math.random();
                if (SingletonRegole.getIstanza().contains(Regole.H)) {
                    if (num < 0.2)
                        mazzo[corrente] = Carte.PANCHINA;
                    else if (num < 0.4)
                        mazzo[corrente] = Carte.LOCANDA;
                    else if (num < 0.6)
                        mazzo[corrente] = Carte.DADI;
                    else if (num < 0.8)
                        mazzo[corrente] = Carte.MOLLA;
                    else
                        mazzo[corrente] = Carte.DIVIETO_SOSTA;
                } else {
                    if (num < 0.25)
                        mazzo[corrente] = Carte.PANCHINA;
                    else if (num < 0.5)
                        mazzo[corrente] = Carte.LOCANDA;
                    else if (num < 0.75)
                        mazzo[corrente] = Carte.DADI;
                    else
                        mazzo[corrente] = Carte.MOLLA;
                }
            }
            return mazzo[corrente];
        }

        @Override
        public void remove() {
        }
    }
}


