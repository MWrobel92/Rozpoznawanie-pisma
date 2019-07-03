/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfejs;

import algorytm.Matematyczne;
import dane.Dysk;
import dane.Kreska;
import dane.Punkt;
import dane.Wiazanie;
import java.util.LinkedList;

/**
 *
 * @author MWr
 */
public class Brudnopis {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
       
        Punkt p1 =  new Punkt(  0,  0);
        Punkt p2 =  new Punkt(  0, 10);
        Punkt p3 =  new Punkt(  0, 20);
        
        // double wynik = KrzywiznaMengera.oblicz(p1, p2, p3);
        // System.out.println(wynik);
        
        Dysk d1 = new Dysk(p2, p1, p3);
        
        Punkt p4 =  new Punkt(  50,  0);
        Punkt p5 =  new Punkt(  50, 10);
        Punkt p6 =  new Punkt(  50, 20);
        
        Dysk d2 = new Dysk(p5, p6, p4);
        Dysk d3 = new Dysk(p5, p4, p6);
        
        Wiazanie w1 = new Wiazanie(d1, d2);
        Wiazanie w2 = new Wiazanie(d1, d3);
        
        // System.out.println(w1.toString());
        // System.out.println(w2.toString());
        
        LinkedList<Punkt> punkty1 = new LinkedList<>();
        LinkedList<Punkt> punkty2 = new LinkedList<>();
        
        punkty1.add(new Punkt(6, 1));
        punkty1.add(new Punkt(2, 0));
        punkty1.add(new Punkt(0, 2));
        punkty1.add(new Punkt(1, 5));
        punkty1.add(new Punkt(4, 5));
        punkty1.add(new Punkt(6, 3));
        
        punkty2.add(new Punkt(4, 5));
        punkty2.add(new Punkt(6, 3));
        punkty2.add(new Punkt(6, 1));
        punkty2.add(new Punkt(2, 0));
        punkty2.add(new Punkt(0, 2));
        punkty2.add(new Punkt(1, 5));
        
        Kreska k1 = new Kreska(punkty1);
        Kreska k2 = new Kreska(punkty2);
        
        double por1 = k1.porownaj(k1);
        double por2 = k1.porownaj(k2);
        
        System.out.println(por1);
        System.out.println(por2);
    }
    
}
