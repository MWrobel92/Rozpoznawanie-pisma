/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfejs;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import algorytm.Fasada;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

/**
 * Klasa odpowiedzialna za interfejs głównego okna programu.
 * @author Michał Wróbel
 */
public class OknoProgramu extends JPanel implements ActionListener {
    
    private final int SKALA = 7;
    
    // Elementy składowe odpowiadające za okno
    private JFrame frame;
        
    // Kontrolki
    private final JMenu menuPlik;
    private final JMenuItem menuPlikWczytaj;
    private final JMenuItem menuPlikZapiszKreski;
    private final JMenuItem menuPlikZapiszKreski2;
    
    private final JMenu menuObraz;
    private final JMenuItem menuObrazZamien;
    
    private final JSplitPane podzialka;
    
    private final JTextArea konsola;
    private final JScrollPane panelKonsoli; 
    
    private final JPanel panelObrazu;    
    private final JScrollPane panelObrazuZewnetrzny;
    
    // Element wykonujący algorytm
    private final Fasada modul;
    private final ObslugaMyszy obslugaMyszy;
    
    int nrObrazu = 0;
 
    private void przygotujIPokazGUI() {        
        
        // Podstawowe ustawienia okna
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame = new JFrame("Rozpoznawanie pisma");
        
        // Przygotowanie głównego panelu
        OknoProgramu newContentPane = this;
        newContentPane.setOpaque(true); 
        frame.setContentPane(newContentPane);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Wyświetlenie okna 
        frame.pack();
        frame.setSize(600, 400);
        frame.setVisible(true);
    }
    
    /**
     * Konstruktor.
     */
    public OknoProgramu() {
                
        modul = new Fasada();
        
        // Deklaracje kontenerów GUI
        JMenuBar pasekMenu;
        
        // Pasek menu
        pasekMenu = new JMenuBar();
        menuPlik = new JMenu();
        
        menuPlik.setText("Plik");
        menuPlikWczytaj = new JMenuItem();
        menuPlikWczytaj.setText("Otwórz");
        menuPlik.add(menuPlikWczytaj);   
        menuPlikZapiszKreski = new JMenuItem();
        menuPlikZapiszKreski.setText("Zapisz kreski");
        menuPlikZapiszKreski.setEnabled(false);
        menuPlik.add(menuPlikZapiszKreski);   
        menuPlikZapiszKreski2 = new JMenuItem();
        menuPlikZapiszKreski2.setText("Zapisz kreski naniesione ręcznie");
        menuPlikZapiszKreski2.setEnabled(false);
        menuPlik.add(menuPlikZapiszKreski2);  
        pasekMenu.add(menuPlik);
        
        menuObraz = new JMenu();
        menuObraz.setText("Obraz");
        menuObrazZamien = new JMenuItem();
        menuObrazZamien.setText("Przełącz");
        menuObrazZamien.setEnabled(false);
        menuObraz.add(menuObrazZamien);   
        pasekMenu.add(menuObraz);
        
        // Panel główny   
        panelObrazu = new JPanel(new BorderLayout());
        
        panelObrazuZewnetrzny = new JScrollPane(panelObrazu);
        panelObrazuZewnetrzny.setPreferredSize(null);
        panelObrazuZewnetrzny.getViewport().setOpaque(false);
        
        konsola = new JTextArea();
        konsola.setEditable(false);
        
        panelKonsoli = new JScrollPane(konsola);
        panelKonsoli.setPreferredSize(null);
        panelKonsoli.getViewport().setOpaque(false);
        
        podzialka = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelObrazuZewnetrzny, panelKonsoli);
        podzialka.setDividerLocation(400);
        
        // Kontroler obsługi kliknięć
        obslugaMyszy = new ObslugaMyszy(SKALA, konsola);
        panelObrazu.setFocusable(true);
        panelObrazu.addKeyListener(obslugaMyszy);
        
        // Okno        
        setLayout(new BorderLayout());
        add(pasekMenu, BorderLayout.NORTH);
        add(podzialka, BorderLayout.CENTER);
        
        // Ustawienie listenera i komend akcji
        ActionListener listener = this;
        
        menuPlikWczytaj.setActionCommand("PlikWczytaj");
        menuPlikWczytaj.addActionListener(listener); 
        menuPlikZapiszKreski.setActionCommand("PlikZapiszKreski");
        menuPlikZapiszKreski.addActionListener(listener);  
        menuPlikZapiszKreski2.setActionCommand("PlikZapiszKreski2");
        menuPlikZapiszKreski2.addActionListener(listener); 
        menuObrazZamien.setActionCommand("ObrazZamien");
        menuObrazZamien.addActionListener(listener);
    }
    
    /**
     * Fukncja uruchamiająca okno.
     */
    public void start() {
        
        javax.swing.SwingUtilities.invokeLater(() -> {
            przygotujIPokazGUI();
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        switch (e.getActionCommand())
        {
            case "PlikWczytaj":
                wczytajPlik();
                break;
            case "ObrazZamien":
                JLabel picLabel1;
                
                nrObrazu = (nrObrazu + 1)%3;
                switch (nrObrazu) {
                    case 0:
                        // Widok kresek
                        obslugaMyszy.wylacz();
                        picLabel1 = new JLabel("", new ImageIcon(modul.pobierzObrazPowiekszony2(SKALA)), JLabel.CENTER);
                        break;
                    case 1:
                        // Widok dysków
                        picLabel1 = new JLabel("", new ImageIcon(modul.pobierzObrazPowiekszony(SKALA)), JLabel.CENTER);
                        break;
                    default:
                        // Generator kresek
                        obslugaMyszy.wlacz(panelObrazu, modul);
                        picLabel1 = new JLabel("", 
                            new ImageIcon(modul.pobierzObrazPowiekszonyNaniesKreski(SKALA, obslugaMyszy.pobierzKreski())),
                            JLabel.CENTER
                        );
                        break;
                }
                picLabel1.addMouseListener(obslugaMyszy);
                panelObrazu.removeAll();
                panelObrazu.add(picLabel1);                
                panelObrazu.revalidate();
                break;      
            case "PlikZapiszKreski" :
                String tresc = modul.pobierzKreski().toString();
                zapiszPlik(tresc);
                break;
            case "PlikZapiszKreski2" :
                String tresc2 = obslugaMyszy.pobierzKreski().toString();
                zapiszPlik(tresc2);
                break;
        }
    }
    
    private boolean wczytajPlik () {   
        
        boolean plikZostalWczytany = false;  
        
        JFileChooser oknoObslugiPliku = new JFileChooser();        
        FileFilter filtrJpg = new FileNameExtensionFilter("Obraz JPEG", "jpg", "jpeg");
        FileFilter filtrPng = new FileNameExtensionFilter("Obraz PNG", "png");
        oknoObslugiPliku.addChoosableFileFilter(filtrJpg);
        oknoObslugiPliku.addChoosableFileFilter(filtrPng);
        
        int wynikOperacji = oknoObslugiPliku.showOpenDialog(this);
        
        if (wynikOperacji == JFileChooser.APPROVE_OPTION) {
            
            // Wybrano plik do wczytania, można przejść do akcji
            File wybranyPlik = oknoObslugiPliku.getSelectedFile();
            
            try {
                
                BufferedImage skan = ImageIO.read(wybranyPlik);
                String nazwaPliku = wybranyPlik.getName();
                String[] tab = nazwaPliku.split("\\.");
                uruchomNowyWatek(skan);
                
                
            } catch (IOException ex) {
                
                //
                
            }
        }
        
        return plikZostalWczytany;
    }

    private void zapiszPlik(String tresc) {
                
        JFileChooser oknoObslugiPliku = new JFileChooser();        
        int wynikOperacji = oknoObslugiPliku.showSaveDialog(this);
        
        if (wynikOperacji == JFileChooser.APPROVE_OPTION) {
            
            // Wybrano plik do wczytania, można przejść do akcji
            File wybranyPlik = oknoObslugiPliku.getSelectedFile();
            
            try {                
                BufferedWriter strumienWyjsciowy = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(wybranyPlik), "UTF-8")
                );         

                strumienWyjsciowy.write(tresc);
                strumienWyjsciowy.close();
                
                
            } catch (IOException ex) {
                
                //
                
            }
        }
        
    }
    
    public void uruchomNowyWatek(BufferedImage skan) {
        
        Thread watek = new Thread(() -> {
            long czas1 = System.currentTimeMillis();
            modul.przygotujObraz(skan);
            long czas2 = System.currentTimeMillis();
            konsola.setText("Czas generowania: " + Double.toString((czas2-czas1)/1000.0) + " s\n");
            JLabel picLabel1 = new JLabel("", new ImageIcon(modul.pobierzObrazPowiekszony2(SKALA)), JLabel.CENTER);
            picLabel1.addMouseListener(obslugaMyszy);
            panelObrazu.removeAll();
            panelObrazu.add(picLabel1);
            OknoProgramu.this.revalidate();
            menuPlikZapiszKreski.setEnabled(true);
            menuPlikZapiszKreski2.setEnabled(true);
            menuObrazZamien.setEnabled(true);
        });       
        
        watek.start();        
        
    }
    
}
