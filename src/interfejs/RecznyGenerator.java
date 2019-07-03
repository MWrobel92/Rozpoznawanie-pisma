package interfejs;

import dane.Kreska;
import dane.ZbiorKresek;
import dane.Punkt;
import java.util.LinkedList;

/**
 * Ręczny generator kresek.
 * @author MWr
 */
public class RecznyGenerator {
    
    LinkedList<Punkt> tworzonaKreska;
    LinkedList<Kreska> gotoweKreski;
    
    public RecznyGenerator() {
        tworzonaKreska = new LinkedList<>();
        gotoweKreski = new LinkedList<>();
    }
    
    public void dodajPunkt(int x, int y) {
        tworzonaKreska.add(new Punkt(x, y));
    }
    
    public void zatwierdzKreske() {
        if (tworzonaKreska.size() > 0) {
            gotoweKreski.add(new Kreska(tworzonaKreska));
            tworzonaKreska = new LinkedList<>();
        }
    }
    
    public void anulujKreske() {
        tworzonaKreska.clear();
    }
    
    ZbiorKresek pobierzKreski() {        
        ZbiorKresek zbior = new ZbiorKresek(gotoweKreski);
        zbior.kreski.add(new Kreska(tworzonaKreska));
        return zbior;
    }
    
}
