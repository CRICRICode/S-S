package gui;

import objects.Player.Giocatore;
import game.Game;
import objects.Rules.Regole;
import objects.Snake.Serpenti;
import objects.Stairs.Scale;
import objects.pattern.SingletonCarte;
import objects.pattern.SingletonDadi;
import objects.pattern.SingletonListaGiocatori;
import objects.pattern.SingletonRegole;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class Impostazioni extends JFrame {

    private JTextArea titolo;
    private JCheckBox DadoSingolo, DoppiDadi, CasellaPremio, CasellaPesca, CasellaSosta, CarteSpeciali, LanciaDado, DoppioSei;
    private JTextField righe, colonne;
    private JButton aggiungiGiocatore, salva, carica, auto, man;
    private ButtonGroup dadi = new ButtonGroup();
    private JRadioButton uno, due;
    private ArrayList<Integer> speciali =new ArrayList<>();
    private ArrayList<Giocatore> giocatori=new ArrayList<>();
    private ArrayList<Serpenti> serpenti=new ArrayList<>();
    private ArrayList<Regole> regole=new ArrayList<>();
    private ArrayList<Scale> scale=new ArrayList<>();
    private File fileSalvataggio;

    public Impostazioni() {
        setTitle("Impostazioni simulazione");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Listener listener = new Listener();

        // Utilizziamo un GridLayout per semplificare il layout
        setLayout(new GridLayout(0, 2));

        Font titleFont = new Font("Arial", Font.BOLD, 30);
        Font textFont = new Font("Helvetica", Font.BOLD, 10);

        // Etichette per le righe e le colonne
        addLabelAndTextField("Righe:", "10");
        addLabelAndTextField("Colonne:", "10");

        // Caselle di controllo per le opzioni
        DadoSingolo = addCheckBox(" Un solo dado*");
        DoppioSei = addCheckBox(" Doppio 6*");
        CasellaSosta = addCheckBox(" Caselle sosta");
        CasellaPremio = addCheckBox("Caselle premio");
        CasellaPesca = addCheckBox(" Caselle \"pesca una carta\"");
        CarteSpeciali = addCheckBox(" Ulteriori carte - Richiede \"pesca una carta\"");

        // Pulsanti di opzione per il numero di dadi
        due = addRadioButton("2 Dadi", due);
        due.setSelected(true);
        uno = addRadioButton("1 Dado", uno);

        // Pulsanti di esecuzione
        aggiungiGiocatore = new JButton("🤖 Aggiungi un giocatore");
        salva = new JButton("💾 Salva Configurazione");
        carica = new JButton("📂 Carica Configurazione");
        auto = new JButton("📜 Avvia simulazione automatica");
        man = new JButton("🤚 Avvia simulazione manuale");

        aggiungiGiocatore.addActionListener(listener);
        salva.addActionListener(listener);
        carica.addActionListener(listener);
        auto.addActionListener(listener);
        man.addActionListener(listener);

        add(salva);
        add(carica);
        add(auto);
        add(man);
        add(aggiungiGiocatore);

        JTextArea info = new JTextArea("ATTENZIONE: Le regole con * l'asterisco * richiedono 2 dadi");
        info.setFont(textFont);
        info.setForeground(Color.RED);
        JTextArea spazio = new JTextArea("    ");
        addComponent(info, 10, 2, 1);

        pack();
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null); // dovrebbe centrare la finestra sullo schermo;
    }

    // Metodo per aggiungere etichette e caselle di testo
    private void addLabelAndTextField(String labelText, String defaultValue) {
        JLabel label = new JLabel(labelText);
        JTextField textField = new JTextField(defaultValue);
        add(label);
        add(textField);

        // Assegna il textField ai campi righe o colonne in base all'etichetta
        if (labelText.equals("Righe:")) {
            righe = textField;
        } else if (labelText.equals("Colonne:")) {
            colonne = textField;
        }
    }

    // Metodo per aggiungere caselle di controllo
    private JCheckBox addCheckBox(String labelText) {
        JCheckBox checkBox = new JCheckBox(labelText);
        add(checkBox);
        return checkBox;
    }


    private void addComponent(JComponent component, int row, int column, int columnSpan) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = column;
        constraints.gridy = row;
        constraints.gridwidth = columnSpan;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(component, constraints);
    }

    // Metodo di utilità per aggiungere etichetta e campo di testo
    private void addField(String labelText, JTextField textField, String defaultValue) {
        JTextArea label = new JTextArea(labelText);
        label.setEditable(false);
        label.setFont(new Font("Helvetica", Font.TYPE1_FONT, 12));
        add(label);

        textField = new JTextField(defaultValue);
        textField.setPreferredSize(new Dimension(100, 30));
        add(textField);
    }

    // Metodo di utilità per aggiungere casella di controllo
    private JCheckBox addCheckBox(String labelText, JCheckBox checkBox) {
        checkBox = new JCheckBox(labelText);
        add(checkBox);
        return checkBox;
    }

    // Metodo di utilità per aggiungere pulsante di opzione
    private JRadioButton addRadioButton(String labelText, JRadioButton radioButton) {
        radioButton = new JRadioButton(labelText);
        dadi.add(radioButton);
        add(radioButton);
        return radioButton;
    }

    // Metodo di utilità per aggiungere pulsanti
    private void addButtons(String button1Text, String button2Text) {
        JButton button1 = new JButton(button1Text);
        button1.addActionListener(new Listener());
        add(button1);

        JButton button2 = new JButton(button2Text);
        button2.addActionListener(new Listener());
        add(button2);
    }

    class Listener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == auto) {
                auto();
            } else if (e.getSource() == carica) {
                importaImpostazioni();
            } else if (e.getSource() == salva) {
                save();
            } else if (e.getSource() == aggiungiGiocatore) {
                addGiocatore();
            } else if (e.getSource() == man) {
                man();
            }
        } //ActionPerformed

        private void addGiocatore() {
            giocatori.add(new Giocatore());
            JOptionPane.showMessageDialog(null, "Inserito un nuovo giocatore");
        } //aggiungiGiocatore

        private void save() {
            JFileChooser chooser = new JFileChooser();
            try {
                if (fileSalvataggio != null) {
                    int ans = JOptionPane.showConfirmDialog(null, "Sovrascrivere " + fileSalvataggio.getAbsolutePath() + " ?");
                    if (ans == 0) { // Risposta "Sì"
                        scrivi(fileSalvataggio.getAbsolutePath());
                    } else {
                        JOptionPane.showMessageDialog(null, "Nessun salvataggio");
                    }
                    return;
                }
                if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    fileSalvataggio = chooser.getSelectedFile();
                    setTitle("Scale e Serpenti" + " - " + fileSalvataggio.getName());
                }
                if (fileSalvataggio != null) {
                    scrivi(fileSalvataggio.getAbsolutePath());
                    JOptionPane.showMessageDialog(null, "File salvato con successo!");
                } else {
                    JOptionPane.showMessageDialog(null, "Nessun Salvataggio");
                }
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        } //Salvataggio

        private void scrivi(String nomeFile) throws IOException {
            PrintWriter pw=new PrintWriter( new FileWriter(nomeFile));
            //prima riga: nRighe nColonne
            pw.println("Inizio:");
            String tmp=righe.getText()+" "+colonne.getText();
            pw.println(tmp);
            pw.println("Serpenti:");
            for(Serpenti s: serpenti){
                //coda-testa
                tmp=s.getTesta()+" "+s.getCoda();
                pw.println(tmp);
            }
            pw.println("/Serpenti");

            pw.println("Scale:");
            for(Scale s: scale){
                //inizio-fine
                tmp=s.getInizio()+" "+s.getFine();
                pw.println(tmp);

            }
            pw.println("/Scale");

            tmp=uno.isSelected()? "1":"2";
            pw.println("Dadi "+tmp);

            pw.println("Regole");
            if(DadoSingolo.isSelected())
                pw.println("C");
            if(DoppioSei.isSelected())
                pw.println("D");
            if(CasellaSosta.isSelected())
                pw.println("E");
            if(CasellaPremio.isSelected())
                pw.println("F");
            if(CasellaPesca.isSelected())
                pw.println("G");
            if(CarteSpeciali.isSelected())
                pw.println("H");

            pw.println("/Regole");

            pw.close();
        }//Scrittura sul file

        private void importaImpostazioni() {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();
                if (selectedFile.exists()) {
                    fileSalvataggio = selectedFile;
                    setTitle("Scale e Serpenti - " + fileSalvataggio.getName());
                    try {
                        reset(fileSalvataggio.getAbsolutePath());
                        JOptionPane.showMessageDialog(null, "File caricato con successo!");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "File inesistente!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Nessuna apertura!");
            }
        }//Importa impostazioni

        private void reset(String nomeFile) throws IOException {
            try (BufferedReader br = new BufferedReader(new FileReader(nomeFile))) {
                if (!br.readLine().equals("Inizio:")) {
                    throw new IOException();
                }

                String[] dimensioni = br.readLine().split(" ");
                righe.setText(dimensioni[0]);
                colonne.setText(dimensioni[1]);

                String dadiLine = br.readLine();
                int nDadi = Integer.parseInt(dadiLine.split(" ")[1]);
                uno.setSelected(nDadi == 1);
                due.setSelected(nDadi == 2);

                String linea;
                while (!(linea = br.readLine()).equals("/Regole")) {
                    switch (linea) {
                        case "B":
                            regole.add(Regole.DEFAULT);
                            break;
                        case "C":
                            regole.add(Regole.C);
                            DadoSingolo.setSelected(true);
                            break;
                        case "D":
                            regole.add(Regole.D);
                            DoppioSei.setSelected(true);
                            break;
                        case "E":
                            regole.add(Regole.E);
                            CasellaSosta.setSelected(true);
                            break;
                        case "F":
                            regole.add(Regole.F);
                            CasellaPremio.setSelected(true);
                            break;
                        case "G":
                            regole.add(Regole.G);
                            CasellaPesca.setSelected(true);
                            break;
                        case "H":
                            regole.add(Regole.H);
                            CarteSpeciali.setSelected(true);
                            break;
                    }
                }
            }
        } //Reset delle impostazioni

        private void auto() {
            int nCaselleTot = Integer.parseInt(righe.getText()) * Integer.parseInt(colonne.getText());

            int nCaselleSpeciali = 1; // include la casella 100
            if (CasellaSosta.isSelected()) {
                nCaselleSpeciali += 3;
            }
            if (CasellaPremio.isSelected()) {
                nCaselleSpeciali += 3;
            }
            if (CasellaPesca.isSelected()) {
                nCaselleSpeciali += 3;
            }
            if (serpenti.isEmpty() && scale.isEmpty()) {
                nCaselleSpeciali += 20;
            } else {
                nCaselleSpeciali += speciali.size();
            }

            // Verifica ulteriori condizioni
            if (nCaselleTot <= nCaselleSpeciali) {
                JOptionPane.showMessageDialog(null, "Attenzione! La matrice è troppo piccola per le caselle speciali.");
                return;
            }

            if (giocatori.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Inserire almeno un giocatore.");
                return;
            }

            if ((DadoSingolo.isSelected() || DoppioSei.isSelected()) && uno.isSelected()) {
                JOptionPane.showMessageDialog(null, "Le regole selezionate sono incompatibili. Si prega di riprovare.");
                return;
            }

            if (CarteSpeciali.isSelected() && !CarteSpeciali.isSelected()) {
                JOptionPane.showMessageDialog(null, "Per utilizzare la regola H, è necessaria la regola G.");
                return;
            }

            int num = Integer.parseInt(righe.getText()) * Integer.parseInt(colonne.getText());
            for (Serpenti s : serpenti) {
                if (s.getCoda() > num) {
                    JOptionPane.showMessageDialog(null, "Attenzione, serpenti non validi. Si prega di reinserire scale e serpenti.");
                    serpenti = new ArrayList<>();
                    serpenti = new ArrayList<>();
                    speciali = new ArrayList();
                    return;
                }
            }
            for (Scale s : scale) {
                if (s.getFine() > num) {
                    JOptionPane.showMessageDialog(null, "Attenzione, scale non valide. Si prega di reinserire scale e serpenti.");
                    scale = new ArrayList<>();
                    serpenti = new ArrayList<>();
                    speciali = new ArrayList();
                    return;
                }
            }

            // Creazione degli ID dei giocatori
            List<String> playerIds = new ArrayList<>();
            for (int i = 1; i <= giocatori.size(); i++) {
                playerIds.add("Player" + i);
            }

            // Creazione delle regole
            List<Regole> regole = new ArrayList<Regole>();
            if (DadoSingolo.isSelected()) {
                regole.add(Regole.C);
            }
            if (DoppioSei.isSelected()) {
                regole.add(Regole.D);
            }
            if (CasellaSosta.isSelected()) {
                regole.add(Regole.E);
            }
            if (CasellaPremio.isSelected()) {
                regole.add(Regole.F);
            }
            if (CasellaPesca.isSelected()) {
                regole.add(Regole.G);
            }
            if (CarteSpeciali.isSelected()) {
                regole.add(Regole.H);
            }

            // Inizializzazione del gioco
            new SingletonRegole(regole);
            new SingletonListaGiocatori(giocatori);

            int nDadi = uno.isSelected() ? 1 : 2;
            if (nDadi == 1) {
                regole.add(Regole.DEFAULT);
            }

            Game gioco = new Game(Integer.parseInt(righe.getText()), Integer.parseInt(colonne.getText()));
            gioco.initScacchiera();
            new Simulazione(Game.getScacchiera().getCaselle(), gioco, false);

            // Reset delle variabili
            giocatori = new ArrayList<>();
            scale = new ArrayList<>();
            serpenti = new ArrayList<>();
            regole = new ArrayList<>();
            SingletonDadi.reset();
            SingletonCarte.reset();
        } //Simulazione atuomatica

        private void man() {
            int nCaselleTot = Integer.parseInt(righe.getText()) * Integer.parseInt(colonne.getText());
            if (nCaselleTot < 20 && serpenti.isEmpty() && scale.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Attenzione! Per giocare su questa matrice usare scale e serpenti personalizzati");
                return;
            } else if (nCaselleTot != 100) {
                int nCaselleSpeciali = 1; //include casella 100
                if (CasellaSosta.isSelected())
                    nCaselleSpeciali += 3;
                if (CasellaPremio.isSelected())
                    nCaselleSpeciali += 3;
                if (CasellaPesca.isSelected())
                    nCaselleSpeciali += 3;
                if (serpenti.isEmpty() && scale.isEmpty())
                    nCaselleSpeciali += 20;
                else {
                    nCaselleSpeciali += speciali.size();
                }
                if (nCaselleTot <= nCaselleSpeciali) {
                    JOptionPane.showMessageDialog(null, "Attenzione! Matrice troppo piccola");
                    return;
                }
            }
            if (giocatori.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Inserire almeno un giocatore");
            } else if ((DadoSingolo.isSelected() || DoppioSei.isSelected()) && uno.isSelected()) {
                JOptionPane.showMessageDialog(null, "Sono state selezionate regole incompatibili, riprovare");
            } else if (CarteSpeciali.isSelected() && !CasellaPesca.isSelected()) {
                JOptionPane.showMessageDialog(null, "Per usare la regola H è richiesta la regola G");
            } else {

                int num = Integer.parseInt(righe.getText()) * Integer.parseInt(colonne.getText());
                for (Serpenti s : serpenti) {
                    if (s.getCoda() > num) {
                        JOptionPane.showMessageDialog(null, "Attenzione, serpenti non validi. Reinserire scale e serpenti");
                        serpenti = new ArrayList<>();
                        scale = new ArrayList<>();
                        speciali = new ArrayList<>();
                        return;
                    }
                }
                for (Scale s : scale) {
                    if (s.getFine() > num) {
                        JOptionPane.showMessageDialog(null, "Attenzione, scale non valide. Reinserire scale e serpenti");
                        scale = new ArrayList<>();
                        serpenti = new ArrayList<>();
                        speciali = new ArrayList<>();
                        return;
                    }
                }

                if (DadoSingolo.isSelected()) {
                    regole.add(Regole.C);
                }
                if (DoppioSei.isSelected()) {
                    regole.add(Regole.D);
                }
                if (CasellaSosta.isSelected()) {
                    regole.add(Regole.E);
                }
                if (CasellaPremio.isSelected()) {
                    regole.add(Regole.F);
                }
                if (CasellaPesca.isSelected()) {
                    regole.add(Regole.G);
                }
                if (CarteSpeciali.isSelected()) {
                    regole.add(Regole.H);
                }
                new SingletonRegole(regole);

                new SingletonListaGiocatori(giocatori);
                int nDadi = uno.isSelected() ? 1 : 2;
                if (nDadi == 1)
                    regole.add(Regole.DEFAULT);
                Game gioco = new Game(Integer.parseInt(righe.getText()),
                        Integer.parseInt(colonne.getText()));
                gioco.initScacchiera();
                new Simulazione(Game.getScacchiera().getCaselle(), gioco , true);
                giocatori = new ArrayList<>();
                scale = new ArrayList<>();
                serpenti = new ArrayList<>();
                regole = new ArrayList<>();
                SingletonDadi.reset();
                SingletonCarte.reset();
            }
        }
    }
}
