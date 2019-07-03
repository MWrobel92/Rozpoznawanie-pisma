package dane;

import java.util.LinkedList;

/**
 * Kreska przedstawiona jako łańcuch dysków.
 * @author MWr
 */
public class Lancuch {
    
    public final LinkedList<Dysk> dyski;
    
    public Lancuch(Wiazanie pierwszeWiazanie) {
        dyski = new LinkedList<>();
        dyski.add(pierwszeWiazanie.dysk1);
        dyski.add(pierwszeWiazanie.dysk2);
    }
    
    public boolean maNaPoczatku(int x, int y) {
        return ( (x == x1()) && (y == y1() ));
    }
    
    public boolean maNaKoncu(int x, int y) {
        return ( (x == x2()) && (y == y2() ));
    }
    
    public void dodajDyskNaPoczatku(Dysk nowy) {
        dyski.addFirst(nowy);
    }
        
    public void dodajDyskNaKoncu(Dysk nowy) {
        dyski.addLast(nowy);
    }

    public void doklejLancuchNaPoczatku(Lancuch lancuch, boolean odwracamy) {
        
        if (!odwracamy) {            
            // Doklejamy łańcuch w niezmienionej kolejności
            dyski.addAll(0, lancuch.dyski);
            
        } else {            
            // Doklejamy odwrócony łańcuch
            for (Dysk d : lancuch.dyski) {
                dyski.addFirst(d);
            }            
        }
    }

    public void doklejLancuchNaKoncu(Lancuch lancuch, boolean odwracamy) {
        
        if (!odwracamy) {            
            // Doklejamy łańcuch w niezmienionej kolejności
            dyski.addAll(lancuch.dyski);
            
        } else {            
            // Doklejamy odwrócony łańcuch
            int dlugosc = dyski.size();
            for (Dysk d : lancuch.dyski) {
                dyski.add(dlugosc, d);
            }
        }
            
    }
    
    public int x1() {
        return dyski.getFirst().pobierzX();
    }
    
    public int y1() {
        return dyski.getFirst().pobierzY();
    }
    
    public int x2() {
        return dyski.getLast().pobierzX();
    }
    
    public int y2() {
        return dyski.getLast().pobierzY();
    }

    public Dysk pobierzPierwszyDysk() {
        return dyski.getFirst();
    }

    public Dysk pobierzOstatniDysk() {
        return dyski.getLast();
    }

    Wiazanie pobierzPierwszeWiazanie() {
        Wiazanie w = new Wiazanie(dyski.get(1), dyski.getFirst());
        w.ustawDwustronnie();
        return w;
    }

    Wiazanie pobierzOstatnieWiazanie() {
        Wiazanie w = new Wiazanie(dyski.get(dyski.size()-2), dyski.getLast());
        w.ustawDwustronnie();
        return w;
    }

    LinkedList<Dysk> pobierzPoczatek() {
        LinkedList<Dysk> lista = new LinkedList<>();
        lista.add(dyski.get(1));
        lista.add(dyski.getFirst());
        return lista;
    }

    LinkedList<Dysk> pobierzKoniec() {
        LinkedList<Dysk> lista = new LinkedList<>();
        lista.add(dyski.get(dyski.size()-2));
        lista.add(dyski.getLast());
        return lista;
    }
    
    public Kreska zwrocKreske() {
        Kreska kreska = new Kreska();
        kreska.wczytajZListyDyskow(dyski);
        return kreska;
    }

}
