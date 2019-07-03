/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dane;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * @author MWr
 */
public class ZbiorKresek {
    
    public LinkedList<Kreska> kreski;
    
    public ZbiorKresek() {
        kreski = new LinkedList<>();
    }
    
    public ZbiorKresek(Collection<Kreska> kreski) {
        this.kreski = new LinkedList<>();
        for (Kreska k : kreski) {
            this.kreski.add( (Kreska)k.clone() );
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Kreska d : kreski) {
            sb.append(d.toString());
            sb.append('\n');
        }
        return sb.toString();
    }
    
    public double porownaj(ZbiorKresek drugi) {
        
        double wynik = 0.0;
        
        int liczbaKresek1 = kreski.size();
        int liczbaKresek2 = drugi.kreski.size();
        
        // Utworzenie macierzy skojarzeń
        // ArrayList<ArrayList<Double>> macierz = new ArrayList<>(liczbaKresek1);
        
        for (int i = 0; i < liczbaKresek1; ++i) {   
            double najlepszaWartoscWiersza = 0.0;
            // ArrayList<Double> wiersz = new ArrayList<>(liczbaKresek2);
            Kreska k = kreski.get(i);
            for (int j = 0; j < liczbaKresek2; ++j) {
                double wartosc = k.porownaj(drugi.kreski.get(j));
                // wiersz.add(wartosc);
                if (wartosc > najlepszaWartoscWiersza) {
                    najlepszaWartoscWiersza = wartosc;
                }
            }
            wynik += najlepszaWartoscWiersza;
            // macierz.add(wiersz);
        }        
        
        return wynik / (double)Math.max(liczbaKresek1, liczbaKresek2);
    } 
    
}
