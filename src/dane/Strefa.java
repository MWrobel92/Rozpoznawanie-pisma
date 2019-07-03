package dane;

import algorytm.GeneratorWiazan;
import java.util.LinkedList;
import java.util.TreeSet;

/**
 * Strefa niepewnych (jednokierunkowych) wiązań, potencjalne "skrzyżowanie"
 * @author MWr
 */
public class Strefa {
    
    /** Dyski należące do strefy */
    private final TreeSet<Dysk> dyski;
    /** Wiązania należące do strefy */    
    private final LinkedList<Wiazanie> wiazania;
    
    /** Kreski wychodzące ze strefy */
    private final LinkedList<Lancuch> kreskiP;
    /** Kreski wchodzące do strefy */
    private final LinkedList<Lancuch> kreskiK;
    
    /** Gniazda wewnątrz strefy wchodzące do strefy */
    private final LinkedList<Gniazdo> gniazda;
    
    private final double MAX_KAT = Math.PI / 3.0;
    
    public Strefa(Wiazanie w) {
        dyski = new TreeSet<>();
        wiazania = new LinkedList<>();
        /** Łańcuchy, których POCZĄTEK jest wewnątrz strefy */
        kreskiP = new LinkedList<>();
        /** Łańcuchy, których KONIEC jest wewnątrz strefy */
        kreskiK = new LinkedList<>();
        
        gniazda = new LinkedList<>();
        
        wiazania.add(w);
        dyski.add(w.dysk1);
        dyski.add(w.dysk2);
    }
    
    public boolean zawieraDysk(Dysk d) {
        return dyski.contains(d);
    }
    
    public void dodajLancuch(Lancuch l, boolean wychodzacy) {
        if (wychodzacy) {
            kreskiP.add(l);
        } else {
            kreskiK.add(l);
        }
    }

    public int liczbaDyskow() {
        return dyski.size();
    }
    
    public int liczbaLancuchow() {
        return kreskiP.size() + kreskiK.size();
    }

    public void dodajWiazanie(Wiazanie w) {
        wiazania.add(w);
        dyski.add(w.dysk1);
        dyski.add(w.dysk2);
    }

    /**
     * Scala wizania i dyski z obu stref.
     * Uwaga! Nie scala kresek!
     * @param str2 
     */
    public void scal(Strefa str2) {
        wiazania.addAll(str2.wiazania);
        dyski.addAll(str2.dyski);
    }
    
    public void generujGniazda() {
        
        for (Lancuch l : kreskiP) {
            gniazda.add(new Gniazdo(l, true));
        }
        
        for (Lancuch l : kreskiK) {
            gniazda.add(new Gniazdo(l, false));
        }
    }
    
    /** Dokonaj "pogłębienia" gniazd, czyli przedłużenia o pasujące wiązania */
    public void poglebGniazda() {
        
        // Rozszerzamy gniazda "w głąb" strefy        
        LinkedList<Wiazanie> wiazaniaKopia = new LinkedList<>();
        wiazaniaKopia.addAll(wiazania);
        
        boolean kontynuuj = true;
        while (kontynuuj) {
            kontynuuj = false;
            
            // Dla każdego gniazda
            for (Gniazdo g : gniazda) {
                
                Wiazanie gw = g.ostatnieWiazanie();
                Wiazanie najlepsze = null;
                double najlepszyKat = 0.0;
                
                // Szukamy najlepszego spośród wiązań, które są do niego styczne
                for (Wiazanie w : wiazaniaKopia) {
                    
                    if (gw.dysk2.equals(w.dysk1)) {
                        
                        double kat = gw.katMiedzyWektorami(w, false);
                        if ((najlepsze == null) || (kat < najlepszyKat)) {
                            najlepszyKat = kat;
                            najlepsze = w;
                        }
                    }
                    else if (gw.dysk2.equals(w.dysk2)) {
                        
                        double kat = gw.katMiedzyWektorami(w, true);
                        if ((najlepsze == null) || (kat < najlepszyKat)) {
                            najlepszyKat = kat;
                            najlepsze = w;
                        }
                    }
                }
                
                // Sprawdzamy, czy najlepsze wiązanie może być przedłużeniem naszego gniazda
                if ((najlepsze != null) && (najlepszyKat < GeneratorWiazan.MAX_KAT)) {
                    
                    g.dodajWiazanie(najlepsze);
                    wiazaniaKopia.removeFirstOccurrence(najlepsze);
                    kontynuuj = true;
                    
                }
                
            }
           
        }
        
    }
    
    /** 
     * Wybiera, które gniazda mogą był połączone
     * @param trybUproszczony Dopuszcza wiązania, które nie kończą się w tym samym punkcie
     * @return 
     */
    public LinkedList<Decyzja> generujDecyzje(boolean trybUproszczony) {
                
        // Generujemy potencjalne połączenia między gniazdami
        LinkedList<Decyzja> potencjalnePolaczeniaGniazd = new LinkedList<>();
        
        for (int i = 0; i < gniazda.size(); ++i) {
            Gniazdo g1 = gniazda.get(i);
            
            for (int j = i+1; j < gniazda.size(); ++j) {
                Gniazdo g2 = gniazda.get(j);
                                
                if (trybUproszczony) {
                    
                    // Sprawdzamy kąty niepogłębionych gniazd i wektora między nimi
                    Wiazanie nowe = new Wiazanie(g1.ostatnieWiazanie().dysk2, g2.ostatnieWiazanie().dysk2);
                    
                    double kat1 = g1.ostatnieWiazanie().katMiedzyWektorami(nowe, false);
                    double kat2 = g2.ostatnieWiazanie().katMiedzyWektorami(nowe, true);
                    
                    if ((kat1 < MAX_KAT) && (kat2 < MAX_KAT)) {
                        potencjalnePolaczeniaGniazd.add(new Decyzja(g1, g2));
                    }
                    
                } else {
                    
                    // Sprawdzamy, czy gniazda kończą się w tym samym punkcie i kąt między nimi
                    if (g1.dyski.getLast().equals(g2.dyski.getLast())) {
                
                        double kat = g1.ostatnieWiazanie().katMiedzyWektorami(g2.ostatnieWiazanie(), true);
                        if (kat < MAX_KAT) {
                            potencjalnePolaczeniaGniazd.add(new Decyzja(g1, g2));
                        }
                
                    }
                }
                           
            }
        }
        
        // Dokonujemy selekcji wiązań, aby pozostały tylko te, które się nie wykluczają
        potencjalnePolaczeniaGniazd.sort((Decyzja d1, Decyzja d2) -> {
            if (d1.PobierzKat() < d2.PobierzKat()) {
                return -1;
            } else if (d1.PobierzKat() > d2.PobierzKat()) {
                return 1;
            } else {
                return 0;
            }
        });
        
        for (int i = 0; i < potencjalnePolaczeniaGniazd.size(); ++i) {            
            Decyzja d1 = potencjalnePolaczeniaGniazd.get(i);
            
            int j = i+1;
            while(j < potencjalnePolaczeniaGniazd.size()) {
                Decyzja d2 = potencjalnePolaczeniaGniazd.get(j);
                
                if ((d1.lancuch1 == d2.lancuch1) || (d1.lancuch1 == d2.lancuch2)
                        || (d1.lancuch2 == d2.lancuch1) || (d1.lancuch2 == d2.lancuch2)) {                    
                    potencjalnePolaczeniaGniazd.remove(j);                    
                } else {
                    ++j;
                }
            }
            
        }        
        
        return potencjalnePolaczeniaGniazd;
    }

    public void przedluzLancuchy() {
        
        for (Gniazdo g : gniazda) {
            g.wydluzLancuch();
        }
    
    }
    
}
