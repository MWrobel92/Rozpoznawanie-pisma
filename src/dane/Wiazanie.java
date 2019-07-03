package dane;

import algorytm.Matematyczne;

/**
 * Wiązanie łączące dwa dyski
 * @author MWr
 */
public class Wiazanie {
    
    public Dysk dysk1;
    public Dysk dysk2;
    
    /** Przyjmuje wartość "true" w przypadku, gdy punkt punkt styczny 1 dysku 1 
     jest w jednej krawędzi z punktem stycznym 2 dysku 2 */
    boolean odwrotnie;
    
    /** Przyjmuje wartość "true", jeśli dyski są wzajemnie w swoim "kręgu zainteresowania */
    boolean dwustronne;
    
    public Wiazanie (Dysk d1, Dysk d2) {
        dysk1 = d1;
        dysk2 = d2;
        dwustronne = false;
        
        double odl11 = dysk1.punktStyczny1.odlegloscDo(dysk2.punktStyczny1);
        double odl12 = dysk1.punktStyczny1.odlegloscDo(dysk2.punktStyczny2);
        double odl21 = dysk1.punktStyczny2.odlegloscDo(dysk2.punktStyczny1);
        double odl22 = dysk1.punktStyczny2.odlegloscDo(dysk2.punktStyczny2);
        
        odwrotnie = ((odl12 + odl21) < (odl11 + odl22));
    }
    
    public int x1() {
        return dysk1.pobierzX();
    }
    
    public int y1() {
        return dysk1.pobierzY();
    }
    
    public int x2() {
        return dysk2.pobierzX();
    }
    
    public int y2() {
        return dysk2.pobierzY();
    }
    
    public double katMiedzyWektorami(Wiazanie drugiWektor, boolean odwrocDrugi) {
        int x1 = x2() - x1();
        int y1 = y2() - y1();
        int x2 = drugiWektor.x2() - drugiWektor.x1();
        int y2 = drugiWektor.y2() - drugiWektor.y1();
        
        if (odwrocDrugi) {
            x2 = -x2;
            y2 = -y2;
        }
        
        return Matematyczne.katMiedzyWektorami(x1, y1, x2, y2);
    } 
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("dysk 1 = [");
        sb.append(dysk1.toString());
        sb.append("], dysk 2=[");
        sb.append(dysk2.toString());
        sb.append("], ");
        if (dwustronne) {
            sb.append("dwustronne, ");
        } else {
            sb.append("jednostronne, ");
        }
        if (odwrotnie) {
            sb.append("odwrotne");
        } else {
            sb.append("zwykłe");
        }
        return sb.toString();
    }

    public boolean jestPrzeciwneDo(Dysk d1, Dysk potencjalnySasiadA) {        
        return (dysk1.equals(potencjalnySasiadA) && dysk2.equals(d1));
    }

    public void ustawDwustronnie() {
        dwustronne = true;
    }

    public boolean dwustronne() {
        return dwustronne;
    }
}
