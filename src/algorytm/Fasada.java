/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorytm;

import dane.Dysk;
import dane.Kreska;
import dane.ZbiorKresek;
import dane.Lancuch;
import dane.Obszar;
import dane.Punkt;
import dane.PunktZmiennoprzecinkowy;
import dane.Wiazanie;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

/**
 *
 * @author Michał
 */
public class Fasada {

    /** Obraz w odcieniach szarości */
    private ObrazOdcienieSzarosci szary;
    
    /** Obraz binarny */
    private ObrazBinarny binarny;
    
    /** Lista obszarów */
    private List<Obszar> obszary;
    
    /** Znalezione dyski */
    private TreeSet<Dysk> dyski;
    
    /** Znalezione wiązania */
    private List<Wiazanie> wiazania;
    
    /** Znalezione łańcuchy */
    private List<Lancuch> lancuchy;
        
    public void przygotujObraz(BufferedImage skan) {
                
        // Konwersja obrazu do tablicy bajtów
        szary = new ObrazOdcienieSzarosci(skan);
                
        // Progowanie
        short prog = szary.metodaOtsu();        
        binarny = szary.obrazWyjsciowy(prog);
        
        // Segmentacja
        obszary = GeneratorObszarow.generujObszary(binarny);
        for(Obszar o : obszary) {
            o.znajdzKrawedzieNaZewnatrz();
        }
        
        // Wyszukiwanie dysków
        dyski = new TreeSet<>();
        for(Obszar o : obszary) {
            dyski.addAll(o.znajdzDyski());
        }
        
        // Wyszukiwanie wiązań
        GeneratorWiazan gw = new GeneratorWiazan();
        wiazania = gw.generuj(dyski);
        
        // Wyszukiwanie łańcuchów
        GeneratorLancuchow gl = new GeneratorLancuchowUproszczony();
        lancuchy = gl.generuj(wiazania);
        
    }

    public BufferedImage pobierzObrazBinarny() {
        return binarny.obrazPowiekszony(1);
    }
      
    public BufferedImage pobierzObrazPowiekszony(int skala) {
                
        BufferedImage obraz = binarny.obrazPowiekszony(skala);
        
        Graphics2D g = obraz.createGraphics();
        
        for(Obszar o : obszary) {
            
            // Malowanie krawędzi
            g.setColor(Color.gray);
            for (Punkt px : o.pobierzKrawedzie()) {                
                g.fillRect(px.getX()*skala, px.getY()*skala, skala, skala);
            }
            
            // Malowanie dysków
            g.setColor(Color.yellow);
            for (Dysk d : dyski) {    
                // Środek
                g.fillRect(d.pobierzX()*skala, d.pobierzY()*skala, skala, skala);
                // Okrąg
                double srodekXd = (double)d.pobierzX()*(double)skala + ((double)skala/2.0);
                double srodekYd = (double)d.pobierzY()*(double)skala + ((double)skala/2.0);
                
                int x = (int)Math.round(srodekXd - d.pobierzR()*(double)skala);
                int y = (int)Math.round(srodekYd - d.pobierzR()*(double)skala);
                int rr = (int)Math.round(d.pobierzR()*(double)skala*2.0);
                g.drawOval(x, y, rr, rr);
            }
            
            // Malowanie wiązań
            for (Wiazanie w : wiazania) {
                
                if (w.dwustronne()) {
                    g.setColor(Color.green);
                } else {
                    g.setColor(Color.red);
                }
                
                int przesuniecie = (int)Math.round((double)skala/2.0);
                
                int x1 = w.x1() * skala + przesuniecie;
                int y1 = w.y1() * skala + przesuniecie;
                int x2 = w.x2() * skala + przesuniecie;
                int y2 = w.y2() * skala + przesuniecie;

                g.drawLine(x1, y1, x2, y2);
            }
            
        }
        
        return obraz;
    }
    
    public BufferedImage pobierzObrazPowiekszony2(int skala) {
                
        BufferedImage obraz = binarny.obrazPowiekszony(skala);
        
        Graphics2D g = obraz.createGraphics();
        
        int przesuniecie = (int)Math.round((double)skala/2.0);
                    
        int liczbaNamalowanych = 0;
        for (Kreska k : pobierzKreski().kreski) {  
            
            //Ustawienie koloru
            switch (liczbaNamalowanych % 6) {
                case 0: g.setColor(Color.magenta); break;
                case 1: g.setColor(Color.green);   break;
                case 2: g.setColor(Color.blue);    break;
                case 3: g.setColor(Color.red);     break;
                case 4: g.setColor(Color.yellow);  break;
                case 5: g.setColor(Color.cyan);    break;
            }            
            ++liczbaNamalowanych;
            
            // Narysowanie punktów charakterystycznych
            for (PunktZmiennoprzecinkowy p1 : k.punktyCharakterystyczne) {
                g.drawRect((int)Math.round(p1.x * skala), (int)Math.round(p1.y * skala), skala, skala);
            }
            
            // Narysowanie kreski aproksymowanej
            int liczbaPunktow = 10;
            double odstep = 1.0 / (double)liczbaPunktow;
            
            double x_poprz = k.pobierzAproksymowanyX(0.0);
            double y_poprz = k.pobierzAproksymowanyY(0.0);
        
            for (double t = odstep; t <= 1.0; t += odstep) {
                double x_akt = k.pobierzAproksymowanyX(t);
                double y_akt = k.pobierzAproksymowanyY(t);
                
                int x1 = (int)Math.round(x_poprz * skala) + przesuniecie;
                int y1 = (int)Math.round(y_poprz * skala) + przesuniecie;
                int x2 = (int)Math.round(x_akt * skala) + przesuniecie;
                int y2 = (int)Math.round(y_akt * skala) + przesuniecie;
                g.drawLine(x1, y1, x2, y2);
                
                x_poprz = x_akt;
                y_poprz = y_akt;
            }  
            /*
            if (!k.punktyCharakterystyczne.isEmpty()) {
                // Piksel początkowy
                PunktZmiennoprzecinkowy p1 = k.punktyCharakterystyczne.get(0);
                g.drawRect((int)Math.round(p1.x * skala), (int)Math.round(p1.y * skala), skala, skala);
            }
            
            for (int i=1; i < k.punktyCharakterystyczne.size(); ++i) {
                
                PunktZmiennoprzecinkowy p1 = k.punktyCharakterystyczne.get(i-1);
                PunktZmiennoprzecinkowy p2 = k.punktyCharakterystyczne.get(i);
                
                // Wiązanie
                int x1 = (int)Math.round(p1.x * skala) + przesuniecie;
                int y1 = (int)Math.round(p1.y * skala) + przesuniecie;
                int x2 = (int)Math.round(p2.x * skala) + przesuniecie;
                int y2 = (int)Math.round(p2.y * skala) + przesuniecie;
                g.drawLine(x1, y1, x2, y2);
                
                // Piksel końcowy
                g.fillRect((int)Math.round(p2.x * skala), (int)Math.round(p2.y * skala), skala, skala);
            } 
            */
        }
        
        return obraz;
    }
    
    public BufferedImage pobierzObrazPowiekszonyNaniesKreski(int skala, 
            ZbiorKresek kreski) {
        
        BufferedImage obraz = binarny.obrazPowiekszony(skala);        
        Graphics2D g = obraz.createGraphics();        
        int przesuniecie = (int)Math.round((double)skala/2.0);
            
        // Namalowanie kresek
        g.setColor(Color.yellow);
        
        int s = kreski.kreski.size();
        
        for(Kreska k : kreski.kreski) {
            
            // Zmiana koloru, jeśli to jest ostatnia kreska
            if (s > 1) { --s; } else { g.setColor(Color.blue); }
            
            if (!k.punkty.isEmpty()) {
                // Piksel początkowy
                Punkt p1 = k.punkty.getFirst();
                g.fillRect(p1.getX()*skala, p1.getY()*skala, skala, skala);
            }
            
            for (int i=1; i < k.punkty.size(); ++i) {
                
                Punkt p1 = k.punkty.get(i-1);
                Punkt p2 = k.punkty.get(i);
                
                // Wiązanie
                int x1 = p1.getX() * skala + przesuniecie;
                int y1 = p1.getY() * skala + przesuniecie;
                int x2 = p2.getX() * skala + przesuniecie;
                int y2 = p2.getY() * skala + przesuniecie;
                g.drawLine(x1, y1, x2, y2);
                
                // Piksel końcowy
                g.fillRect(p2.getX()*skala, p2.getY()*skala, skala, skala);
            }          
            
        }
        
        return obraz;
    }

    public List<Obszar> pobierzObszary() {
        return obszary;
    }

    public TreeSet<Dysk> pobierzDyski() {
        return dyski;
    }
    
    public ZbiorKresek pobierzKreski() {
        LinkedList<Kreska> kreski = new LinkedList<>();
        for (Lancuch l : lancuchy) {
            kreski.add(l.zwrocKreske());
        }
        return new ZbiorKresek(kreski);
    }
    
    /*
    public String wypiszKreski() {
        StringBuilder sb = new StringBuilder();
        for (Lancuch l : lancuchy) {
            for (Dysk d : l.dyski) {
                sb.append(d.pobierzX());
                sb.append(',');
                sb.append(d.pobierzY());
                sb.append(';');
            }
            sb.append('\n');
        }
        return sb.toString();
    }
    */
}
