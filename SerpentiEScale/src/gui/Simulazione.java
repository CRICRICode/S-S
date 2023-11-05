package gui;

import objects.Player.Giocatore;
import objects.Player.Giocatori;
import game.Game;
import game.Moves.*;
import objects.Board.Casella;
import objects.Board.CasellaSpeciale;
import objects.Board.Scacchiera;
import objects.Snake.Serpenti;
import objects.Stairs.Scale;
import objects.pattern.Observer;
import objects.pattern.SingletonListaGiocatori;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;
//@SuppressWarnings("ALL")
public class Simulazione extends JFrame {
    private boolean manuale;
    private Game gioco;
    private JButton start, lanciaDadi, muovi, leggenda;
    private JTextArea info, dadi, giocatoreCorrente;
    private JTextArea [] infoPartecipanti;
    private Casella [] casella;
    private JTextArea[][] tabellone;
    private int righe = Scacchiera.getRighe(), colonne = Scacchiera.getColonne();



    class ObsCarte extends Thread {

        public void run(){
            Osservatore o=new Osservatore();
            CasellaPesca.addObserver(o);
        }
        class Osservatore implements Observer{
            @Override
            public void update(Object o) {
                info.setText((String) o);
                info.setVisible(true);

            }
        }

    }
    class ObsCase extends Thread {
        public void run() {
            Osservatore o = new Osservatore();
            gioco.setColore(o);
        }

        class Osservatore implements Observer {
            @Override
            public void update(Object o) {
                Object[] obj = (Object[]) o;
                int position = (Integer) obj[0];
                int i = position / colonne;
                int j = position % colonne;
                boolean flag = (boolean) obj[1];

                if (flag) {
                    tabellone[i][j].setBackground(Color.green);
                } else {
                    //inizio<fine
                    if ((Integer) obj[2] < (Integer) obj[0]) {
                        for (int x = (Integer) obj[2] + 1; x <= (Integer) obj[0]; x++) {
                            i = x / colonne;
                            j = x % colonne;
                            tabellone[i][j].setBackground(Color.yellow);
                        }
                    } else {
                        for (int x = (Integer) obj[0]; x < (Integer) obj[2]; x++) {
                            i = x / colonne;
                            j = x % colonne;
                            tabellone[i][j].setBackground(Color.red);
                        }
                    }
                }
            }
        }
    }


    private void leggenda() {
        JDialog leggendaDialog = new JDialog(this, "Legenda", true);
        leggendaDialog.setSize(245, 240);
        leggendaDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        leggendaDialog.setLayout(new BorderLayout());

        JTextArea legendaText = new JTextArea(
                """
                        P: Panchina
                        L: Locanda
                        M: Molla
                        TSE: Testa Serpente
                        CSE: Coda Serpente
                        ISC: Inizio Scala
                        FSC: Fine Scala
                        D: Dadi
                        C: Prendi una carta"""
        );
        legendaText.setEditable(false);
        leggendaDialog.add(legendaText, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        leggendaDialog.pack();
        leggendaDialog.setVisible(true);
    }

    public Simulazione(Casella[] scacchiera, Game gioco, boolean manuale) {
        Observer o = new ObsInfo();
        gioco.addObserver(o);

        ObsGiocatore oG = new ObsGiocatore();
        gioco.addObserverGiocatore(oG);
        ObsDadi oD = new ObsDadi();
        gioco.addObserverDadi(oD);
        ObsWin oW = new ObsWin();
        gioco.addObserverWin(oW);


        ObsCC oCC = new ObsCC();
        CasellaSemplice.addObserver(oCC);
        CasellaMolla.addObserver(oCC);
        CasellaDadi.addObserver(oCC);
        CasellaSS.addObserver(oCC);
        gioco.addObserverMsg(oCC);

        this.manuale = manuale;
        ActionListener listener = new Listener();
        this.casella = scacchiera;
        this.gioco = gioco;


        Giocatori giocatori = SingletonListaGiocatori.getIstanza();
        this.setTitle("Scale e Serpenti");
        setResizable(true);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                gioco.stop();
            }
        });
        this.setVisible(true);

        infoPartecipanti = new JTextArea[giocatori.getnGiocatori()];
        Iterator<Giocatore> itg = giocatori.iterator();
        for (int i = 0; i < giocatori.getnGiocatori(); i++) {
            Giocatore g = itg.next();
            infoPartecipanti[i] = new JTextArea("Giocatore: " + g.getId() + " - Casella Corrente: 0");
            infoPartecipanti[i].setBounds(50, i * 30 + 200, 390, 20);
            infoPartecipanti[i].setEditable(false);
            this.getContentPane().add(infoPartecipanti[i]);
        }

        int index = 0;
        for (Scale s : Scacchiera.getScale()) {
            JTextArea info = new JTextArea("Inizio Scala: " + s.getInizio() + " - Fine Scala: " + s.getFine());
            info.setEditable(false);
            info.setBounds(1000, index * 30 + 100, 500, 20);
            this.getContentPane().add(info);
            index++;
        }
        for (Serpenti s : Scacchiera.getSerpenti()) {
            JTextArea info = new JTextArea("Testa Serpente: " + s.getCoda() + " - Coda Serpente: " + s.getTesta()); //Nell'implementazione c'è stata una piccola confusione quindi la testa e la coda e viceversa
            info.setEditable(false);
            info.setBounds(1000, index * 30 + 100, 500, 20);
            this.getContentPane().add(info);
            index++;
        }

        tabellone = new JTextArea[righe][colonne];
        for (int i = 0; i < righe; i++) {
            for (int j = 0; j < colonne; j++) {
                int elemento = i * colonne + j;
                CasellaSpeciale c = casella[elemento].isTrue();
                String tmp = c == null ? (elemento + 1) + "" : leggenda(c);

                tabellone[i][j] = new JTextArea(tmp);
                tabellone[i][j].setEditable(false);
                tabellone[i][j].setBounds(450 + j * 40, 100 + i * 40, 30, 30);
                this.getContentPane().add(tabellone[i][j]);
                tabellone[i][j] = tabellone[i][j];
            }
        }


        info = new JTextArea("");
        info.setEditable(false);
        info.setVisible(true);
        info.setBounds(560, 530, 300, 20);
        this.getContentPane().add(info);

        giocatoreCorrente = new JTextArea("Giocatore: ");
        giocatoreCorrente.setEditable(false);
        giocatoreCorrente.setVisible(true);
        giocatoreCorrente.setBounds(500, 550, 200, 20);
        this.getContentPane().add(giocatoreCorrente);

        dadi = new JTextArea(" ");
        dadi.setEditable(false);
        dadi.setBounds(750, 550, 300, 20);
        this.getContentPane().add(dadi);

            start = new JButton("Avvia");
            start.setBounds(600, 600, 100, 20);
            start.addActionListener(listener);
            this.getContentPane().add(start);

            lanciaDadi = new JButton("Lancia i dadi");
            lanciaDadi.setBounds(430, 600, 120, 20);
            lanciaDadi.addActionListener(listener);
            this.getContentPane().add(lanciaDadi);
            lanciaDadi.setVisible(manuale);

            muovi = new JButton("Muovi");
            muovi.setBounds(750, 600, 100, 20);
            muovi.addActionListener(listener);
            muovi.setVisible(manuale);
            this.getContentPane().add(muovi);

            leggenda = new JButton("Leggenda");
            leggenda.setBounds(600, 20, 100, 20);
            leggenda.addActionListener(listener);
            this.getContentPane().add(leggenda);


        JTextArea risolutore=new JTextArea("");
        risolutore.setBounds(700,500,100,50);
        risolutore.setEditable(false);
        this.getContentPane().add(risolutore);

        setPreferredSize(new Dimension(1350, 720));
        pack();
        setLocationRelativeTo(null);
    }




    class ObsGiocatore implements Observer{

        @Override
        public void update(Object o) {
            info.setVisible(false);
            if (o instanceof Integer) {
                aggiornaTurno( o.toString());
            }

        }
    }

    class ObsInfo implements Observer{
        @Override
        public void update(Object o){
            getInfoGiocatori();
        }
    }

    class ObsDadi implements Observer{

        @Override
        public void update(Object o) {
            lancio((int[]) o);
        }
    }
    static class ObsWin implements Observer {

        @Override
        public void update(Object o) {
                JOptionPane.showMessageDialog(null, "Ha vinto il giocatore: " + o.toString() + "!");

        }
    }
    class ObsCC implements Observer{
        @Override
        public void update(Object o) {
            info.setText((String) o);
            info.setVisible(true);
        }
    }//Observer per Caselle e Carte

    public void lancio(int[] n) {
        dadi.setText("Tiro: " + n[0] + (n.length == 2 ? "-" + n[1] : ""));
    }

    public void getInfoGiocatori() {
        Giocatori giocatori=SingletonListaGiocatori.getIstanza();
        Iterator<Giocatore> it=giocatori.iterator();
        int index=0;
        for(int i=0;i<giocatori.getnGiocatori();i++){
            Giocatore g=it.next();
            infoPartecipanti[index].setText("Nome: "+g.getId()+" - Casella Corrente: "+g.getPosizione());//+" - Turni Fermo: "+g.getTurniFermo());
            index++;
        }
    }

    public void aggiornaTurno(String pl) {
        for (int i=0; i<righe; i++) {
            for (int j=0; j<colonne; j++) {
                tabellone[i][j].setBackground(Color.white);
            }
        }
        giocatoreCorrente.setVisible(true);
        giocatoreCorrente.setText("Gioca: " + pl);
        dadi.setText("Tiro:  - ");
    }

    class Listener implements ActionListener {

        private boolean flag = false;
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == start && !manuale) {
                gioco.avviaPartita();
                new ObsCarte().start();
                new ObsCase().start();
            } else if (e.getSource() == start && manuale) {
                gioco.avviaManuale();
                new ObsCarte().start();
                new ObsCase().start();
            } else if (e.getSource() == lanciaDadi) {
                if (!flag) {
                    Game.semLancio.release();
                    flag=!flag;
                }
            } else if (e.getSource() == muovi) {
                if (flag) {
                    Game.semMovimento.release();
                    flag = !flag;
                }
            } else if (e.getSource() == leggenda){
                leggenda();
            }

        }
    }

    public String leggenda(CasellaSpeciale c){
        return switch (c) {
            default -> " ";
            case PANCA -> "P";
            case LOCANDA -> "L";
            case MOLLA -> "M";
            case TESTA_SE -> "TSE";
            case INIZIO_SC -> "ISC";
            case DADI -> "D";
            case FINE_SC -> "FSC";
            case CODA_SE -> "CSE";
        };
    }


}
