package game;
import game.Moves.CercaCasella;
import gui.Main;
import objects.Board.Scacchiera;
import objects.Dices.Dadi;
import objects.Rules.Regole;
import objects.pattern.Observer;
import objects.pattern.SingletonDadi;
import objects.pattern.SingletonListaGiocatori;
import objects.pattern.SingletonRegole;
import objects.Player.Giocatore;
import objects.Player.Giocatori;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Game {
    private static Scacchiera scacchiera;
    private boolean checkVincita = false;
    private ArrayList<Observer> obs = new ArrayList<>();
    private ArrayList<Observer> obsDadi = new ArrayList<>();
    private ArrayList<Observer> obsGiocatori = new ArrayList<>();
    private ArrayList<Observer> obsWin = new ArrayList<>();
    private ArrayList<Observer> obsMsg = new ArrayList<>();
    private Observer colore;
    private GiocoAutomatico giocoAuto;
    private GiocoManuale giocoMan;
    private List<Regole> regole;
    private Giocatori giocatori;
    private Iterator<Giocatore> gIterator;
    private int nScale = 5, nSerpenti = 5, nCaselle, nRighe, nColonne;
    public static Semaphore semLancio = new Semaphore(0);
    public static Semaphore semMovimento = new Semaphore(0);
    public void addObserver(Observer o) {
        obs.add(o);
    }
    public void addObserverGiocatore(Observer o) {
        obsGiocatori.add(o);
    }
    public void addObserverMsg(Observer o) {
        obsMsg.add(o);
    }
    public void addObserverDadi(Observer o) {
        obsDadi.add(o);
    }
    public void addObserverWin(Observer o) {
        obsWin.add(o);
    }
    public void setColore(Observer o) {
        colore = o;
    }

    public Game(int nRighe, int nColonne) {
        this.nRighe = nRighe;
        this.nColonne = nColonne;
        this.nCaselle = nColonne * nRighe;
        this.giocatori = SingletonListaGiocatori.getIstanza();
        this.regole = SingletonRegole.getIstanza();
        gIterator = giocatori.iterator();
    }

    public Game(){
        this.nRighe = 10;
        this.nColonne = 10;
        this.nCaselle = nColonne * nRighe;
        this.giocatori = SingletonListaGiocatori.getIstanza();
        this.regole = SingletonRegole.getIstanza();
        gIterator = giocatori.iterator();
    }

    public static Scacchiera getScacchiera() {
        return scacchiera;
    }

    public void initScacchiera() {
        scacchiera = new Scacchiera(nRighe, nColonne, nCaselle);
        scacchiera.initScacchiera(nSerpenti, nScale);
        scacchiera.caselleSpeciali();
    }

    public void avviaPartita() {
        giocoAuto = new GiocoAutomatico();
        giocoAuto.start();
    }

    public void avviaManuale() {
        giocoMan =new GiocoManuale();
        giocoMan.start();
    }

    public void stop(){
        if(giocoAuto ==null && giocoMan ==null)
            return;
        if(giocoAuto !=null)
            giocoAuto.stop();
        else
            giocoMan.stop();
    }

    class GiocoManuale extends Thread {

        public GiocoManuale() {
        }

        public void run() {
            CercaCasella movimento = new CercaCasella();
            Giocatore g = null;

            while (!checkVincita) {
                g = gIterator.next();
                System.out.println("Gioca: " + g.getId());
                notificaOsservatoriPlayer(g.getId());

                if (regole.contains(Regole.E) && g.getTurniFermo() > 0) {
                    if (gestisciRegolaHSosta(g)) {
                        continue;
                    } else {
                        g.setTurniFermo(g.getTurniFermo() - 1);
                        System.out.println(g.getId() + " bloccato");
                        notificaOsservatoriMsg(g.getId() + " bloccato");
                        try {
                            sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                }

                attesaLancioDadi();

                Object[] color = impostaColoreCasellaPrecedente(g);
                Dadi d = SingletonDadi.getIstanza();
                int[] ris = lancioDadi(d);
                notificaOsservatoriDadi(ris);

                if (gestisciRegolaDoppioSei(g, ris)) {
                    int nCaselle = calcolaNumeroCaselle(ris);
                    avanzamentoGiocatore(g, movimento, nCaselle, color);
                }
            }
        }

        private boolean gestisciRegolaHSosta(Giocatore player) {
            if (regole.contains(Regole.H) && player.getSostaFree() > 0 && player.getTurniFermo() == 1) {
                player.setSostaFree(player.getSostaFree() - 1);
                player.setTurniFermo(player.getTurniFermo() - 1);
                System.out.println("Carta sosta evita di bloccarsi");
                notificaOsservatoriMsg("Carta sosta evita di bloccarsi");
                return true;
            }
            return false;
        }

        private void attesaLancioDadi() {
            try {
                semLancio.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private Object[] impostaColoreCasellaPrecedente(Giocatore player) {
            Object[] color = new Object[2];
            if (player.getPosizione() != 0) {
                color[0] = player.getPosizione() - 1;
                color[1] = true;
                colore.update(color);
            }
            return color;
        }

        private int[] lancioDadi(Dadi d) {
            int[] ris = d.lanciaDadi();
            String dadi = ris.length == 2 ? ris[0] + "-" + ris[1] : "" + ris[0];
            System.out.println("Dadi: " + dadi);
            return ris;
        }

        private boolean gestisciRegolaDoppioSei(Giocatore player, int[] ris) {
            if (regole.contains(Regole.D) && ris.length == 2 && ris[0] == ris[1] && ris[0] == 6) {
                player.setDoppioSei(true);
            }
            return true;
        }

        private int calcolaNumeroCaselle(int[] ris) {
            return ris.length == 1 ? ris[0] : ris[0] + ris[1];
        }

        private void avanzamentoGiocatore(Giocatore player, CercaCasella avanzamento, int nCaselle, Object[] color) {
            try {
                semMovimento.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                avanzamento.setMovimentoStrategy(CercaCasella.cercaCasella(player, nCaselle));
                avanzamento.sposta(player, nCaselle);
                System.out.println("Casella corrente " + player.getPosizione());

                impostaColoreCasella(player, color);

                notificaOsservatori(null);

                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (Main.Eccezione e) {
                checkVincita = true;
                int tmp;
                if (color[0] != null)
                    tmp = (Integer) color[0];
                else
                    tmp = -1;
                color = new Object[3];
                color[0] = player.getPosizione() - 1;
                color[1] = false;
                color[2] = tmp;
                colore.update(color);
                notificaOsservatori(null);
                System.out.println("Ha vinto il giocatore: " + player.getId());
                notificaOsservatoriVincitore(player.getId());
            }
        }

        // Metodi per notificare gli osservatori
        private void notificaOsservatoriPlayer(int playerId) {
            for (Observer o : obsGiocatori) {
                o.update(playerId);
            }
        }

        private void notificaOsservatoriMsg(String message) {
            for (Observer o : obsMsg) {
                o.update(message);
            }
        }

        private void notificaOsservatoriDadi(int[] ris) {
            for (Observer o : obsDadi) {
                o.update(ris);
            }
        }

        private void impostaColoreCasella(Giocatore player, Object[] color) {
            int tmp;
            if (color[0] != null)
                tmp = (Integer) color[0];
            else
                tmp = -1;
            color = new Object[3];
            color[0] = player.getPosizione() - 1;
            color[1] = false;
            color[2] = tmp;
            colore.update(color);
        }

        private void notificaOsservatori(Object data) {
            for (Observer o : obs) {
                o.update(data);
            }
        }

        private void notificaOsservatoriVincitore(int playerId) {
            for (Observer o : obsWin) {
                o.update(playerId);
            }
        }
    }

    class GiocoAutomatico extends Thread{
        public GiocoAutomatico(){}

        public void run(){
            CercaCasella avanzamento = new CercaCasella();


            Giocatore player = null;
            while (!checkVincita) {
                player = gIterator.next();
                System.out.println("Giocatore: " + player.getId());
                for(Observer o: obsGiocatori){
                    o.update(player.getId());
                }
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (regole.contains(Regole.E) && player.getTurniFermo() > 0) {
                    if (regole.contains(Regole.H) && player.getSostaFree() > 0 && player.getTurniFermo() == 1) {
                        player.setSostaFree(player.getSostaFree() - 1);
                        player.setTurniFermo(player.getTurniFermo() - 1);
                        System.out.println("Carta sosta evita di bloccarsi");
                        for(Observer o: obsMsg){o.update("Carta sosta evita di bloccarsi");}
                    } else {
                        player.setTurniFermo(player.getTurniFermo() - 1);
                        System.out.println(player.getId() + " bloccato");
                        for(Observer o: obsMsg){o.update(player.getId() + " bloccato");}
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                }
                Object[] color=new Object[2];
                if(player.getPosizione()!=0) {
                    color[0] = player.getPosizione() - 1;
                    color[1] = true;
                    colore.update(color);
                }
                Dadi d;
                int[] ris;

                if (regole.contains(Regole.C) && player.getPosizione() > scacchiera.getNCaselle() - 7 && player.getPosizione() < scacchiera.getNCaselle()) {
                    d = SingletonDadi.getIstanza();
                    ris = new int[1];
                    ris[0] = d.lanciaDadi()[0];
                    System.out.println("un dado :" + ris[0]);
                } else {
                    d = SingletonDadi.getIstanza();
                    ris = d.lanciaDadi();
                    String dadi = ris.length == 2 ? ris[0] + "-" + ris[1] : "" + ris[0];
                    System.out.println("dadi: " + dadi);

                }
                for(Observer o: obsDadi){
                    o.update(ris);
                }
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (regole.contains(Regole.D) && ris.length == 2 && ris[0] == ris[1] && ris[0] == 6)
                    player.setDoppioSei(true);

                int nCaselle = ris.length == 1 ? ris[0] : ris[0] + ris[1];

                try {
                    avanzamento.setMovimentoStrategy(CercaCasella.cercaCasella(player, nCaselle));
                    player=avanzamento.sposta(player, nCaselle);
                    System.out.println("Posizione casella: " + player.getPosizione());

                    int tmp;
                    if(color[0]!=null)
                        tmp=(Integer) color[0];
                    else
                        tmp=-1;
                    color=new Object[3];
                    color[0]=player.getPosizione()-1;
                    color[1]=false;
                    color[2]=tmp;

                    colore.update(color);
                    for(Observer o: obs){
                        o.update(null);
                    }
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        this.interrupt();
                    }
                } catch (Main.Eccezione e) {
                    checkVincita = true;
                    int tmp;
                    if(color[0]!=null)
                        tmp=(Integer) color[0];
                    else
                        tmp=-1;
                    color=new Object[3];
                    color[0]=player.getPosizione()-1;
                    color[1]=false;
                    color[2]=tmp;

                    colore.update(color);
                    for(Observer o: obs){
                        o.update(null);
                    }
                    System.out.println("Ha vinto " + player.getId());
                    for(Observer o: obsWin){
                        o.update(player.getId());
                    }
                }

            }
        }
    }

}
