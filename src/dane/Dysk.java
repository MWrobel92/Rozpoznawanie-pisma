package dane;

import algorytm.Matematyczne;
import java.util.Objects;

/**
 * Element szkieletu - okrąg wpisany w obszar
 * @author MWr
 */
public class Dysk implements Comparable {
    
    /** Minimalny dopuszczalny kąt między promieniami do stycznych (w radianach) */
    public final static double MIN_KAT = (Math.PI * 0.7 );
    /** Maksymalna dopuszczalna różnica długości promieni */
    public final static double MAX_ROZNICA = 1.1;
    
    Punkt srodek;
    
    Punkt punktStyczny1;
    Punkt punktStyczny2;
    
    double promien;
    
    /** Współrzędne wektora łączącego punkty styczne */
    private final int wektorKierunkowyX;
    private final int wektorKierunkowyY;

    public Dysk(Punkt srodek, Punkt styczny1, Punkt styczny2) {
        this.srodek = srodek;
        this.punktStyczny1 = styczny1;
        this.punktStyczny2 = styczny2;
        
        this.promien = (srodek.odlegloscDo(punktStyczny1) + srodek.odlegloscDo(punktStyczny2)) / 2.0;
        
        this.wektorKierunkowyX = punktStyczny2.getX() - punktStyczny1.getX();
        this.wektorKierunkowyY = punktStyczny2.getY() - punktStyczny1.getY();
    }
    
    public int pobierzX() {
        return srodek.getX();
    } 
    
    public int pobierzY() {
        return srodek.getY();
    }
    
    public double pobierzR() {
        return promien;
    }
    
    public int pobierzX1() {
        return punktStyczny1.getX();
    } 
    
    public int pobierzY1() {
        return punktStyczny1.getY();
    }
    
    public int pobierzX2() {
        return punktStyczny2.getX();
    } 
    
    public int pobierzY2() {
        return punktStyczny2.getY();
    }
    
    public double pobierzKat() {
        return srodek.obliczKat(punktStyczny1, punktStyczny2);
    }
    
    /** 
     * Pobiera współrzędną X wektora normalnego (tego, wzdłuż którego należy szukać sąsiadów). 
     * @param odwroc Zamien zwrot wektora
     * @return 
     */
    int pobierzWektorX(boolean odwroc) {
        if (odwroc) {
            return -wektorKierunkowyY;
        } else {
            return wektorKierunkowyY;
        }
    }

    /** 
     * Pobiera współrzędną X wektora normalnego (tego, wzdłuż którego należy szukać sąsiadów). 
     * @param odwroc Zamien zwrot wektora
     * @return 
     */
    int pobierzWektorY(boolean odwroc) {
        if (odwroc) {
            return wektorKierunkowyX;
        } else {
            return -wektorKierunkowyX;
        }
    }
    
    public double odlegloscSrodkow(Dysk drugi) {
        return srodek.odlegloscDo(drugi.srodek);
    }
    
    /**
     * Wylicza kąt kiędzy wektorem kierunkowym a wektorem łączącym środek dysku z podanym punktem
     * @param x Współrzędna X środka sprawdzanego punktu
     * @param y Współrzędna Y środka sprawdzanego punktu
     * @param odwroc Zwrot wektora kierunkowego
     * @return Obliczony kąt
     */
    public double katMiedzyWektorami(int x, int y, boolean odwroc) {
        
        double x1 = x - srodek.getX();
        double y1 = y - srodek.getY();
        double x2 = (double)pobierzWektorX(odwroc);
        double y2 = (double)pobierzWektorY(odwroc);
        
        return Matematyczne.katMiedzyWektorami(x1, y1, x2, y2);        
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final Dysk other = (Dysk)obj;
        
        // Wariant uproszczony - porównujemy jedynie środki
        return srodek.equals(other.srodek);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.srodek);
        // hash = 67 * hash + Objects.hashCode(this.punktStyczny1);
        // hash = 67 * hash + Objects.hashCode(this.punktStyczny2);
        return hash;
    }

    @Override
    public int compareTo(Object obj) {        
        final Dysk other = (Dysk) obj;
        return srodek.compareTo(other.srodek);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("środek=[");
        sb.append(srodek.toString());
        sb.append("], styczny 1=[");
        sb.append(punktStyczny1.toString());
        sb.append("], styczny 2=[");
        sb.append(punktStyczny2.toString());
        sb.append("], promień=");
        sb.append(promien);
        sb.append(", kąt=");
        sb.append(pobierzKat());
        return sb.toString();
    }
}
