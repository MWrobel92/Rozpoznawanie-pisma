package dane;

/**
 * Scalenie dwóch kresek (lub doklejenie czegoś do pierwszej)
 * @author MWr
 */
public class Decyzja {
    
    Lancuch lancuch1;
    Lancuch lancuch2;
    
    /** True, zwrot dany łańcuch WYCHODZI ze strefy łączenia */
    boolean zwrot1;
    boolean zwrot2;
    private double kat;
    
    public Decyzja(Gniazdo g1, Gniazdo g2) {
        lancuch1 = g1.lancuch;
        lancuch2 = g2.lancuch;
        
        zwrot1 = g1.poczatek;
        zwrot2 = g2.poczatek;
        
        kat = g1.ostatnieWiazanie().katMiedzyWektorami(g2.ostatnieWiazanie(), true);
    }
    
    public double PobierzKat() {
        return kat;
    }

    public Lancuch scal() {
        
        Lancuch usuniety = null;
        
        if (lancuch1 != lancuch2) {
            boolean doklejamyZPrzodu = zwrot1;
            boolean odwracamyL2 = wystepujeZmianaZwrotu();

            if (doklejamyZPrzodu) {
                lancuch1.doklejLancuchNaPoczatku(lancuch2, odwracamyL2);
            } else {
                lancuch1.doklejLancuchNaKoncu(lancuch2, odwracamyL2);
            }
            
            usuniety = lancuch2;
        }
    
        return usuniety;
    }

    /**
     * Modyfikuje łańcuchy, biorąc pod uwagę decyzję podjętą poprzednio
     * @param poprzedniaDecyzja 
     */
    public void modyfikuj(Decyzja poprzedniaDecyzja) {
        
        if (lancuch1 == poprzedniaDecyzja.lancuch2) {
            lancuch1 = poprzedniaDecyzja.lancuch1;
            
            // Czy trzeba zmienić zwrot?
            if (poprzedniaDecyzja.wystepujeZmianaZwrotu()) {
                zwrot1 = !zwrot1;
            }
        }
        if (lancuch2 == poprzedniaDecyzja.lancuch2) {
            lancuch2 = poprzedniaDecyzja.lancuch1;
            
            // Czy trzeba zmienić zwrot?
            if (poprzedniaDecyzja.wystepujeZmianaZwrotu()) {
                zwrot2 = !zwrot2;
            }
        }        
        
    }

    boolean wystepujeZmianaZwrotu() {
        return (zwrot1 == zwrot2);
    }
}
