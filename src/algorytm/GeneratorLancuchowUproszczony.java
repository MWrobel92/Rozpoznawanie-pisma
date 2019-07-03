package algorytm;

import dane.Strefa;

/**
 *
 * @author MWr
 */
public class GeneratorLancuchowUproszczony extends GeneratorLancuchow {
    
    @Override
    public void przetworzStrefy() {
        
        // Przeanalizuj strefy
        decyzje.clear();
        for (Strefa s : strefy) {
            // Wariant bardzo uproszczony - bez pogłębiania gniazd
            s.generujGniazda();
            decyzje.addAll(s.generujDecyzje(true));            
        }
    }
    
}
