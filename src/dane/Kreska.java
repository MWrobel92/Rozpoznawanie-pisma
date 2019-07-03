package dane;

import algorytm.Matematyczne;
import static algorytm.Matematyczne.obliczWielomian;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Klasa reprezentująca jedną kreskę (fragment litery będący jednym "pociągnięciem ręki").
 * @author MWr
 */
public class Kreska implements Cloneable {
      
    private final static int LICZBA_PUNKTOW_CHARAKTERYSTYCZNYCH = 17;
    
    public LinkedList<Punkt> punkty;
    
    public ArrayList<PunktZmiennoprzecinkowy> punktyCharakterystyczne;
    
    /** Pełny wektor współczynników wielomianów */
    public ArrayList<Double> wynikAproksymacji;
    
    /** Długość wektora okrojonego (przed normalizacją) */
    public double d;
    
    /** Wektor krótszy znormalizowany */
    public ArrayList<Double> wektorKsztaltu;
    
    /** Konstruktor bezargumentowy */
    public Kreska() {
        punkty = new LinkedList<>();
        punktyCharakterystyczne = new ArrayList<>();
    }
    
    /** Konstruktor argumentowy
     * @param punkty Uporządkowany zbiór punktów 
     */
    public Kreska(Collection<Punkt> punkty) {
        this.punkty = new LinkedList<>();
        for (Punkt p : punkty) {
            this.punkty.add( (Punkt)p.clone() );
        }
        if (this.punkty.size() > 1) {
            wyznaczPunktyCharakterystyczne();
        } else {
            punktyCharakterystyczne = new ArrayList<>();
        }
    }

    public void wczytajZListyDyskow(Collection<Dysk> dyski) {
        this.punkty = new LinkedList<>();
        for (Dysk d : dyski) {
            this.punkty.add(new Punkt(d.pobierzX(), d.pobierzY()));
        }
        wyznaczPunktyCharakterystyczne();
    }
    
    public double wyznaczDlugosc() {
        double dlugosc = 0.0;
        for (int i=1; i < punkty.size(); ++i) {
            int rx = punkty.get(i).getX() - punkty.get(i-1).getX();
            int ry = punkty.get(i).getY() - punkty.get(i-1).getY();
            dlugosc += Matematyczne.pitagoras((double)rx, (double)ry);
        }
        return dlugosc;
    }
    
    public void wyznaczPunktyCharakterystyczne() {
        
        punktyCharakterystyczne = new ArrayList<>(5);        
        double dlugosc = wyznaczDlugosc();
        
        // Przepisanie począku
        punktyCharakterystyczne.add(new PunktZmiennoprzecinkowy(punkty.getFirst().getX(), punkty.getFirst().getY()));
        
        // Wyznaczenie środkowych punktów
        int liczbaOdcinkow = LICZBA_PUNKTOW_CHARAKTERYSTYCZNYCH - 1;
        ArrayList<Double> dlugosciPosrednie = new ArrayList<>(liczbaOdcinkow - 1);
        for (int i = 1; i < liczbaOdcinkow; ++i) {
            dlugosciPosrednie.add(dlugosc / (double)liczbaOdcinkow * (double)i);
        }
        
        double dotychczasowaDlugosc = 0.0;
        for (int i=1; i < punkty.size(); ++i) {
            int rx = punkty.get(i).getX() - punkty.get(i-1).getX();
            int ry = punkty.get(i).getY() - punkty.get(i-1).getY();
            double roznica = Matematyczne.pitagoras((double)rx, (double)ry);
            double nowaDlugosc = dotychczasowaDlugosc + roznica;
            
            for (double d : dlugosciPosrednie) {
                if ((d > dotychczasowaDlugosc) && (d <= nowaDlugosc)) {
                    double odlegloscOdPierwszego = d - dotychczasowaDlugosc;
                    double stosunek = odlegloscOdPierwszego / roznica;

                    double nx = (double)punkty.get(i).getX() * stosunek + (double)punkty.get(i-1).getX() * (1.0-stosunek);
                    double ny = (double)punkty.get(i).getY() * stosunek + (double)punkty.get(i-1).getY() * (1.0-stosunek);

                    punktyCharakterystyczne.add(new PunktZmiennoprzecinkowy(nx, ny));
                }
            }  
            
            dotychczasowaDlugosc = nowaDlugosc;
        }
                
        // Przepisanie końca
        punktyCharakterystyczne.add(new PunktZmiennoprzecinkowy(punkty.getLast().getX(), punkty.getLast().getY()));
        
        // Uruchomienie aproksymacji (kiedyś trzeba będzie przenieść gdzieś inddziej)
        aproksymuj();
    }
    
    public double porownaj(Kreska druga) {
        double wynik = 0.0;
        
        // Sprawdzamy, czy dla każdego punktu naszej kreski istnieje jakiś odpowiednik w drugiej
        double limitOdleglosci = wyznaczDlugosc() / LICZBA_PUNKTOW_CHARAKTERYSTYCZNYCH;
        for (PunktZmiennoprzecinkowy p1 : punktyCharakterystyczne) {
            
            for (PunktZmiennoprzecinkowy p2 : druga.punktyCharakterystyczne) {
                
                double odleglosc = p1.odlegloscDo(p2);
                if (odleglosc < limitOdleglosci) {
                    wynik += 1.0;
                    break;
                }
            }
            
        }
        
        //...i to samo w drugą stronę
        limitOdleglosci = druga.wyznaczDlugosc() / LICZBA_PUNKTOW_CHARAKTERYSTYCZNYCH;
        for (PunktZmiennoprzecinkowy p1 : druga.punktyCharakterystyczne) {
            
            for (PunktZmiennoprzecinkowy p2 : punktyCharakterystyczne) {
                
                double odleglosc = p1.odlegloscDo(p2);
                if (odleglosc < limitOdleglosci) {
                    wynik += 1.0; // - odleglosc / limitOdleglosci;
                    break;
                }
            }
            
        }
        
        return wynik / (LICZBA_PUNKTOW_CHARAKTERYSTYCZNYCH * 2);
    }
    
    public void aproksymuj() {
        
        //Przygotowanie list punktów
        LinkedList<Double> t = new LinkedList<>();
        LinkedList<Double> x = new LinkedList<>();
        LinkedList<Double> y = new LinkedList<>();
        
        double odstep = 1.0 / (double)LICZBA_PUNKTOW_CHARAKTERYSTYCZNYCH;
        double tt = 0.0;
        
        for (PunktZmiennoprzecinkowy p : punktyCharakterystyczne) {
            t.add(tt);
            x.add(p.x);
            y.add(p.y);
            tt += odstep;
        }    
        
        List<Double> aproksymacjaX = Matematyczne.aproksymacjaW3(t, x);
        List<Double> aproksymacjaY = Matematyczne.aproksymacjaW3(t, y);
        
        wynikAproksymacji = new ArrayList<>(8);
        wynikAproksymacji.addAll(aproksymacjaX);
        wynikAproksymacji.addAll(aproksymacjaY);
        
        double a1 = aproksymacjaX.get(1); double b1 = aproksymacjaY.get(1);
        double a2 = aproksymacjaX.get(2); double b2 = aproksymacjaY.get(2);
        double a3 = aproksymacjaX.get(3); double b3 = aproksymacjaY.get(3);
        
        // Normalizacja skróconego wektora
        d = Math.sqrt(a1*a1 + a2*a2 + a3*a3 + b1*b1 + b2*b2 + b3*b3);
        wektorKsztaltu = new ArrayList<>(Arrays.asList(a1/d, a2/d, a3/d, b1/d, b2/d, b3/d));
        
    }
    
    public double pobierzAproksymowanyX(double t) {
        return obliczWielomian(wynikAproksymacji.get(0), wynikAproksymacji.get(1), 
                wynikAproksymacji.get(2), wynikAproksymacji.get(3), t);
    }
    
    public double pobierzAproksymowanyY(double t) {
        return obliczWielomian(wynikAproksymacji.get(4), wynikAproksymacji.get(5), 
                wynikAproksymacji.get(6), wynikAproksymacji.get(7), t);
    }
    
    @Override
    public Object clone() {
        Kreska nowa = new Kreska(punkty);
        return nowa;
    }
    
    @Override
    public String toString() {        
        StringBuilder sb = new StringBuilder();
        for (Punkt d : punkty) {
            sb.append(d.getX());
            sb.append(',');
            sb.append(d.getY());
            sb.append(';');
        }
        return sb.toString();
    }
}
