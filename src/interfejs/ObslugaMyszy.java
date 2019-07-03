/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfejs;

import algorytm.Fasada;
import dane.Dysk;
import dane.ZbiorKresek;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.TreeSet;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author MWr
 */
public class ObslugaMyszy implements MouseListener, KeyListener {
    
    private final int SKALA;
    
    private final JTextArea konsola;
    
    private JPanel panelObrazu;
    private Fasada modul;
    
    private RecznyGenerator reczny;
    
    LinkedList<Dysk> dyski;
    
    boolean rysujKreski;
    

    ObslugaMyszy(int skala, JTextArea konsola) {
        this.konsola = konsola;
        this.SKALA = skala;
        dyski = new LinkedList<>();
        reczny = new RecznyGenerator();
        rysujKreski = false;    
        panelObrazu = null;
        modul = null;
    }
    
    public void zaladujDyski(TreeSet<Dysk> doZaladowania) {
        dyski.clear();
        dyski.addAll(doZaladowania);
    }
    
    public ZbiorKresek pobierzKreski() {
        return reczny.pobierzKreski();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX() / SKALA;
        int y = e.getY() / SKALA;
        
        StringBuilder sb = new StringBuilder("Klik: X = " + x + ", Y = " + y + ".\n");        
        
        // Wyszukanie dysku o podanych współrzędnych
        Dysk dysk = null;
        for (Dysk d : dyski) {
            if ((d.pobierzX() == x) && (d.pobierzY() == y)) {
                dysk = d;
                break;
            }
        }
        
        if (dysk != null) {
            sb.append(dysk.toString());
            sb.append('\n');
        }
        
        konsola.setText(konsola.getText() + sb.toString());
        
        // Dodanie punktu (jeśli potrzeba)
        if (rysujKreski) {
            reczny.dodajPunkt(x, y);            
            rysujObraz();            
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (rysujKreski) {
        int kod = e.getKeyCode();
            switch (kod) {
                case KeyEvent.VK_ENTER:
                    reczny.zatwierdzKreske();
                    break;
                case KeyEvent.VK_ESCAPE:
                    reczny.anulujKreske();
                    break;
                case KeyEvent.VK_SPACE:
                    ZbiorKresek z1 = reczny.pobierzKreski();
                    ZbiorKresek z2 = modul.pobierzKreski();
                    double wynik = z1.porownaj(z2);
                    konsola.setText(konsola.getText() + "Wynik porównania: " + wynik + "\n");
            }
            rysujObraz();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }

    void wlacz(JPanel panelObrazu, Fasada modul) {
        rysujKreski = true;
        this.panelObrazu = panelObrazu;
        this.modul = modul;
    }
    
    void wylacz() {
        rysujKreski = false;
    }

    private void rysujObraz() {
        JLabel obraz = new JLabel("",
            new ImageIcon(modul.pobierzObrazPowiekszonyNaniesKreski(SKALA, reczny.pobierzKreski())),
            JLabel.CENTER
        );
        obraz.addMouseListener(this);
        panelObrazu.removeAll();
        panelObrazu.add(obraz);                
        panelObrazu.revalidate();
    }

    
}
