package dane;

import java.util.LinkedList;

/**
 * Klasa reprezentująca łączenie kreski i strefy.
 * Jest skierowane zawsze do wewnątrz strefy. Na początku zawiera tylko jedno
 * wiązanie, potem można do niego dołączać kolejne wchodząc w głąb strefy.
 * @author MWr
 */
public class Gniazdo {
    
    Lancuch lancuch;  
    boolean poczatek;
    
    LinkedList<Dysk> dyski;
    
    /**
     * Konstruktor
     * @param lancuch
     * @param poczatek True, jeśli gniazdo dotyczy łańcucha wychodzącego ze strefy
     */
    public Gniazdo(Lancuch lancuch, boolean poczatek) {
        
        this.lancuch = lancuch;
        this.poczatek = poczatek;
        
        if (poczatek) {
            dyski = lancuch.pobierzPoczatek();
        } else {
            dyski = lancuch.pobierzKoniec();
        }
        
    }    

    public Wiazanie ostatnieWiazanie() {
        return new Wiazanie(dyski.get(dyski.size()-2), dyski.getLast());
    }

    public void dodajWiazanie(Wiazanie nowe) {
        
        Dysk nowyDysk = null;
        
        if (nowe.dysk1.equals(dyski.getLast())) {
            nowyDysk = nowe.dysk2;
        } else if (nowe.dysk2.equals(dyski.getLast())) {
            nowyDysk = nowe.dysk1;
        }
        
        if (nowyDysk != null) {
            dyski.add(nowyDysk);
        }

    }
    
    /** Dopisuje dyski z gniazda do łańcucha */
    public void wydluzLancuch() {
        
        for (int i = 2; i < dyski.size(); ++i) {
            
            if (poczatek) {
                lancuch.dodajDyskNaPoczatku(dyski.get(i));
            } else {
                lancuch.dodajDyskNaKoncu(dyski.get(i));
            }
            
        }
        
    }
    
}
