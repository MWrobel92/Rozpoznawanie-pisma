package dane;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;

/**
 *
 * @author MWr
 */
public class Obszar {
    
    private final TreeSet<Punkt> zbiorPikseli;
    private final TreeSet<Punkt> pikseleKrawedzi;
    private final TreeSet<Punkt> pikseleWnetrza;
    
    public Obszar() {
        zbiorPikseli = new TreeSet<>();
        pikseleKrawedzi = new TreeSet<>();
        pikseleWnetrza = new TreeSet<>();
    }
    
    
    public boolean dodajPiksel(int x, int y) {
        return zbiorPikseli.add(new Punkt(x, y));
    }
    
    public void znajdzKrawedzieNaZewnatrz() {
        
        for (Punkt px : zbiorPikseli) {
            
            pikseleWnetrza.add(px);
            
            // Sprawdzenie, ile pikseli ze zbioru występuje w 4-sąsiedztwie tego punktu
            
            if (!zbiorPikseli.contains(new Punkt(px.x-1, px.y))) {
                pikseleKrawedzi.add(new Punkt(px.getX()-1, px.getY()));
            }
            
            if (!zbiorPikseli.contains(new Punkt(px.x+1, px.y))) {
                pikseleKrawedzi.add(new Punkt(px.getX()+1, px.getY()));
            }
            
            if (!zbiorPikseli.contains(new Punkt(px.x, px.y-1))) {
                pikseleKrawedzi.add(new Punkt(px.getX(), px.getY()-1));
            }
            
            if (!zbiorPikseli.contains(new Punkt(px.x, px.y+1))) {
                pikseleKrawedzi.add(new Punkt(px.getX(), px.getY()+1));
            }
        }
    }
    
    public void znajdzKrawedzieWewnatrz() {
        
        for (Punkt px : zbiorPikseli) {
            
            // Sprawdzenie, ile pikseli ze zbioru występuje w 4-sąsiedztwie tego punktu
            int liczbaSasiadow = 0;
            if (zbiorPikseli.contains(new Punkt(px.getX()-1, px.getY()))) ++liczbaSasiadow;
            if (zbiorPikseli.contains(new Punkt(px.getX()+1, px.getY()))) ++liczbaSasiadow;
            if (zbiorPikseli.contains(new Punkt(px.getX(), px.getY()-1))) ++liczbaSasiadow;
            if (zbiorPikseli.contains(new Punkt(px.getX(), px.getY()+1))) ++liczbaSasiadow;
            
            // Dodanie piksela, jeśli ma w 4-sąsiedztwie białe piksele
            if (liczbaSasiadow < 4) {
                pikseleKrawedzi.add(px);
            }
            else {
                pikseleWnetrza.add(px);
            }
        }
    }
    
    public TreeSet<Dysk> znajdzDyski() {
        
        LinkedList<Dysk> wszystkieDyski = new LinkedList<>();
        
        // Szukanie wszystkich możliwych dysków
        for (Punkt srodek : pikseleWnetrza) {
            
            // Wyszukanie najbliższego piksela krawędzi
            Punkt styczny1 = pikseleKrawedzi.first();
            double min = srodek.odlegloscDo(styczny1);
            
            for (Punkt pk : pikseleKrawedzi) {
                if (srodek.odlegloscDo(pk) < min) {
                    min = srodek.odlegloscDo(pk);
                    styczny1 = pk;
                }
            }
            
            // Wyszukanie najbliższego piksela spośród tych, które są "w miarę naprzeciwko"
            Punkt styczny2 = null;
            double min2 = Double.MAX_VALUE;
            for (Punkt pk : pikseleKrawedzi) {
                
                double kat = srodek.obliczKat(styczny1, pk);
                if (kat < Dysk.MIN_KAT) {
                    continue;
                }
                if (srodek.odlegloscDo(pk) < min2) {
                    min2 = srodek.odlegloscDo(pk);
                    styczny2 = pk;
                }
            }
            
            if (styczny2 != null) {
                
                // Sprawdzenie różnicy odległości
                double roznicaOdleglosci = Math.abs(min - min2);
                boolean roznicaOk = (roznicaOdleglosci < Dysk.MAX_ROZNICA);
                
                // Sprawdzenie, czy to nie jest punkt "przyklejony" do jednej krawędzi
                boolean p1 = pikseleKrawedzi.contains(new Punkt(srodek.getX()-1, srodek.getY())); // lewy
                boolean p2 = pikseleKrawedzi.contains(new Punkt(srodek.getX(), srodek.getY()-1)); // góra
                boolean p3 = pikseleKrawedzi.contains(new Punkt(srodek.getX()+1, srodek.getY())); // prawy
                boolean p4 = pikseleKrawedzi.contains(new Punkt(srodek.getX(), srodek.getY()+1)); // dół
                // Nie może być tak, że sąsiedzi punkt środkowego np. z góry i z lewej należą do krawędzi
                boolean sasiedziOk = !((p1 && p2) || (p2 && p3)|| (p3 && p4)|| (p4 && p1));
                
                if (roznicaOk && sasiedziOk) {
                    
                    // Znaleźliśmy dysk
                    Dysk nowy = new Dysk(srodek, styczny1, styczny2);
                    wszystkieDyski.add(nowy);
                }
            }
        }
    
        // Selekcja dysków
        TreeSet<Dysk> dyskiZatwierdzone = new TreeSet<>();
        
        // Posortowanie dysków malejąco wg długości promienia
        wszystkieDyski.sort((Dysk d1, Dysk d2) -> {
            if (d1.promien < d2.promien) {
                return 1;
            } else if (d1.promien > d2.promien) {
                return -1;
            } else {
                return 0;
            }
        });
        
        // Dopóki lista nie jest pusta
        while (!wszystkieDyski.isEmpty()) {
            
            // Przeniesienie największego do zatwierdzonych
            Dysk najwiekszy = wszystkieDyski.pollFirst();
            dyskiZatwierdzone.add(najwiekszy);
            
            // Usunięcie z listy dysków, których środki są w promieniu ostatnio zatwierdzonego
            Iterator<Dysk> iterator = wszystkieDyski.iterator();
            Dysk tymczasowy;
            while (iterator.hasNext()) {
                tymczasowy = iterator.next();
                if (tymczasowy.srodek.odlegloscDo(najwiekszy.srodek) < najwiekszy.promien) {
                    iterator.remove();
                }
            }
        }
        
        return dyskiZatwierdzone;
    }
 
    
    public TreeSet<Punkt> pobierzKrawedzie () {
        return (TreeSet<Punkt>)pikseleKrawedzi.clone();
    }
    
    public TreeSet<Punkt> pobierzWnetrze () {
        return (TreeSet<Punkt>)pikseleWnetrza.clone();
    }
   
}
