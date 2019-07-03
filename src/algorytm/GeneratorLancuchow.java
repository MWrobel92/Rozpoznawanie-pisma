package algorytm;

import dane.Decyzja;
import dane.Lancuch;
import dane.Strefa;
import dane.Wiazanie;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author MWr
 */
public class GeneratorLancuchow {
    
    LinkedList<Wiazanie> wiazaniaDwukierunkowe;
    LinkedList<Wiazanie> wiazaniaJednokierunkowe;
    
    LinkedList<Lancuch> lancuchy;
    LinkedList<Strefa> strefy;
    LinkedList<Decyzja> decyzje;
    
    private boolean polaczStrefy;
    
    public GeneratorLancuchow()  {
        wiazaniaDwukierunkowe = new LinkedList<>();
        wiazaniaJednokierunkowe = new LinkedList<>();
        
        lancuchy = new LinkedList<>();
        strefy = new LinkedList<>();
        decyzje = new LinkedList<>();
        
        polaczStrefy = true;
    }
    
    public void wczytajWiazania(Collection<Wiazanie> wiazania) {
        
        // Podział wiązań na dwukierunkowe i jednokierunkowe
        wiazaniaDwukierunkowe.clear();
        wiazaniaJednokierunkowe.clear();
        
        for(Wiazanie w : wiazania) {
            if (w.dwustronne()) {
                wiazaniaDwukierunkowe.add(w);
            } else {
                wiazaniaJednokierunkowe.add(w);
            }
        }
        
    }
    
    private void generujLancuchy() {      
        
        // Wygenerowanie łańcuchów na podstawia wiązań dwukierunkowych (pewnych)        
        lancuchy.clear();
            
        for(Wiazanie w : wiazaniaDwukierunkowe) {
            
            // Sprawdzamy, czy wiązanie można dokleić do istniejących łańcuchów
            Lancuch lan1 = null;
            Lancuch lan2 = null;
            
            boolean lan1przod = true;
            boolean lan2przod = true;
            
            for (Lancuch l : lancuchy) {
                
                if (l.maNaPoczatku(w.x1(), w.y1())) {
                    lan1 = l;
                    lan1przod = true;
                } else if (l.maNaKoncu(w.x1(), w.y1())) {
                    lan1 = l;
                    lan1przod = false;
                } else if (l.maNaPoczatku(w.x2(), w.y2())) {
                    lan2 = l;
                    lan2przod = true;
                } else if (l.maNaKoncu(w.x2(), w.y2())) {
                    lan2 = l;
                    lan2przod = false;
                }
            }
            
            if ((lan1 == null) && (lan2 == null)) {
                
                // Nie można dokleić wiązania do istniejących łańcuchów
                // Trzeba utworzyć nowy łańcuch                
                lancuchy.add(new Lancuch(w));
                
            }
            else if ((lan1 != null) && (lan2 == null)) {
                
                // Wiązanie można dokleić do łańcucha 1
                if (lan1przod) {
                    lan1.dodajDyskNaPoczatku(w.dysk2);
                } else {
                    lan1.dodajDyskNaKoncu(w.dysk2);
                }
            }
            else if ((lan1 == null) && (lan2 != null)) {
                
                // Wiązanie można dokleić do wiązania 1
                if (lan2przod) {
                    lan2.dodajDyskNaPoczatku(w.dysk1);
                } else {
                    lan2.dodajDyskNaKoncu(w.dysk1);
                }
            } else if ((lan1 != null) && (lan2 != null)){
                
                // L2 odwracamy wtedy, gdy wiązanie łączy dwa końce albo dwa początki
                boolean trzebaOdwrocicL2 = (lan1przod == lan2przod);
                
                // Wiązanie jest pomiędzy dwoma istniejącymi łańcuchami
                if (lan1przod) {                    
                    lan1.doklejLancuchNaPoczatku(lan2, trzebaOdwrocicL2);
                } else {
                    lan1.doklejLancuchNaKoncu(lan2, trzebaOdwrocicL2);
                }
                
                lancuchy.removeFirstOccurrence(lan2);
            }
            
        }
    }
    
    public LinkedList<Strefa> generujStrefy() {
        
        // Generujemy listę stref na podstawie wiązań jednokierunkowych
        strefy.clear();
        
        for(Wiazanie w : wiazaniaJednokierunkowe) {
            
            // Sprawdzamy, czy wiązanie można dokleić do istniejących stref
            Strefa str1 = null;
            Strefa str2 = null;
                        
            for (Strefa s : strefy) {
                
                if (s.zawieraDysk(w.dysk1)) {
                    str1 = s;
                } else if (s.zawieraDysk(w.dysk2)) {
                    str2 = s;
                }
            }
            
            if ((str1 == null) && (str2 == null)) {
                
                // Nie można dokleić wiązania do istniejących stref
                // Trzeba utworzyć nową strefę                
                strefy.add(new Strefa(w));
                
            }
            else if ((str1 != null) && (str2 == null)) {
                
                // Wiązanie można dokleić do strefy 1
                str1.dodajWiazanie(w);
            }
            else if ((str1 == null) && (str2 != null)) {
                
                // Wiązanie można dokleić do wiązania 1
                str2.dodajWiazanie(w);
                
            } else if ((str1 != null) && (str2 != null)){
                
                // Wiązanie jest pomiędzy dwoma istniejącymi strefami
                str1.dodajWiazanie(w);
                if (str1 != str2) {
                    
                    str1.scal(str2);
                    strefy.removeFirstOccurrence(str2);
                    
                }
            }
            
        }
        
        // Przypisujemy łańcuchy do stref
        for (Lancuch l : lancuchy) {
            
            for (Strefa s : strefy) {
                
                if (s.zawieraDysk(l.pobierzPierwszyDysk())) {
                    s.dodajLancuch(l, true);
                }
                
                if (s.zawieraDysk(l.pobierzOstatniDysk())) {
                    s.dodajLancuch(l, false);
                }
            }
            
        }
        
        return strefy;
    }
    
    /** Analizuje strefy skrzyżowań, wybiera obszary do połączenia */
    public void przetworzStrefy() {
        
        // Przeanalizuj strefy
        decyzje.clear();
        for (Strefa s : strefy) {
            s.generujGniazda();
            s.poglebGniazda();
            decyzje.addAll(s.generujDecyzje(false));            
            s.przedluzLancuchy();
        }
    }
    
    public void wykonajDecyzje() {
        // Połącz łańcuchy zgodnie z ustalonymi decyzjami
        for (int i = 0; i < decyzje.size(); ++i) {
            
            Decyzja dec = decyzje.get(i); 
            Lancuch usuniety = dec.scal();
            
            if (usuniety != null) {
                // Modyfikacja pozostałych decyzji
                for (int j = (i+1); j < decyzje.size(); ++j) {
                    decyzje.get(j).modyfikuj(dec);
                }
                
                // Usunięcie scalonego łańcucha z listy
                lancuchy.removeFirstOccurrence(usuniety);
            }       
            
        }
    }

    public List<Lancuch> generuj(List<Wiazanie> wiazania) {
        
        wczytajWiazania(wiazania);
        generujLancuchy();
        generujStrefy();
        
        if (polaczStrefy) {
            przetworzStrefy();
            wykonajDecyzje();
        }
        
        return lancuchy;
    }
}
