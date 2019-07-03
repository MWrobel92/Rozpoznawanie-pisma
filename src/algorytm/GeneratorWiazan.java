package algorytm;

import dane.Dysk;
import dane.Wiazanie;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Klasa generująca zbiór wiązań na podstawie zbioru dysków
 * @author MWr
 */
public class GeneratorWiazan {
    
    public final static double MAX_WIELOKROTNOSC_R = 2.5;    
    public final static double MAX_KAT = (Math.PI * 0.2);
       
    public LinkedList<Wiazanie> generuj (Collection<Dysk> dyski) {
        
        LinkedList<Wiazanie> wiazania = new LinkedList<>();
        
        for(Dysk d1 : dyski) {
            
            Dysk potencjalnySasiadA = null;
            double odlegloscA = Double.MAX_VALUE;
            
            Dysk potencjalnySasiadB = null;
            double odlegloscB = Double.MAX_VALUE;
            
            for(Dysk d2 : dyski) {
                
                // Sprawdzanie, czy dyski d1 i d2 mogą być powiązane
                boolean wynikWstepny = true;
        
                if (d1.equals(d2)) {
                    wynikWstepny = false;
                }

                double odleglosc = d1.odlegloscSrodkow(d2);

                if (odleglosc > (d1.pobierzR() * MAX_WIELOKROTNOSC_R)) {
                    wynikWstepny = false;
                }
        
                if (wynikWstepny) {
                    
                    double katA = d1.katMiedzyWektorami(d2.pobierzX(), d2.pobierzY(), true);
                    double katB = d1.katMiedzyWektorami(d2.pobierzX(), d2.pobierzY(), false);
                                
                    if (katA < MAX_KAT) {
                        if ((potencjalnySasiadA == null) || (odleglosc < odlegloscA)) {                             
                            potencjalnySasiadA = d2;
                            odlegloscA = odleglosc;
                        }                            
                    }                    
                    else if (katB < MAX_KAT) {
                        if ((potencjalnySasiadB == null) || (odleglosc < odlegloscB)) {                             
                            potencjalnySasiadB = d2;
                            odlegloscB = odleglosc;
                        }                            
                    }
                }
                    
            }
            
            // Dodaj ewentualne wiązania
            if (potencjalnySasiadA != null) { 
                
                boolean byloPrzeciwne = false;
                for (Wiazanie w : wiazania) {
                    if (w.jestPrzeciwneDo(d1, potencjalnySasiadA)) {
                        byloPrzeciwne = true;
                        w.ustawDwustronnie();
                    }
                }
                if (!byloPrzeciwne) {
                    wiazania.add(new Wiazanie(d1, potencjalnySasiadA));
                }
            }
            
            if (potencjalnySasiadB != null) {
                
                boolean byloPrzeciwne = false;
                for (Wiazanie w : wiazania) {
                    if (w.jestPrzeciwneDo(d1, potencjalnySasiadB)) {
                        byloPrzeciwne = true;
                        w.ustawDwustronnie();
                    }
                }
                if (!byloPrzeciwne) {
                    wiazania.add(new Wiazanie(d1, potencjalnySasiadB));
                }
            }
        }
        
        return wiazania;
    }
    
}
