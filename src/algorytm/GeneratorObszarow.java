package algorytm;

import dane.Obszar;
import dane.Punkt;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Klasa odpowiedzialna za generowanie obszarów na podstawie obrazu binarnego
 * @author MWr
 */
public class GeneratorObszarow {
       
    public static ArrayList<Obszar> generujObszary (ObrazBinarny obraz) {
        
        ArrayList<Obszar> obszary = new ArrayList<>();
        
        int szerokoscObrazu = obraz.szerokosc;
        int wysokoscObrazu = obraz.wysokosc;
        
        boolean[][] piksele = obraz.zwrocKopiePikseli();
        
        for (int i = 0; i < szerokoscObrazu; ++i) {
            for (int j = 0; j < wysokoscObrazu; ++j) {
                
                if (piksele[i][j]) {
                    
                    Obszar nowy = new Obszar();
                    
                    // Dodawanie pikseli metodą "pożaru prerii"
                    LinkedList<Punkt> stos = new LinkedList<>();
                    
                    stos.add(new Punkt(i, j));
                    piksele[i][j] = false;
                    
                    while (!stos.isEmpty()) {
                        Punkt aktualny = stos.pollLast();
                        
                        // Sprawdzenie, czy w 4-sąsiedtwie są jakieś aktywne piksele
                        if (aktualny.getX() < (szerokoscObrazu - 1)) {
                            if (piksele[aktualny.getX() + 1][aktualny.getY()]) {
                                piksele[aktualny.getX() + 1][aktualny.getY()] = false;
                                stos.add(new Punkt(aktualny.getX() + 1, aktualny.getY()));
                            }
                        }
                        
                        if (aktualny.getX() > 0) {
                            if (piksele[aktualny.getX() - 1][aktualny.getY()]) {
                                piksele[aktualny.getX() - 1][aktualny.getY()] = false;
                                stos.add(new Punkt(aktualny.getX() - 1, aktualny.getY()));
                            }
                        }
                        
                        if (aktualny.getY() < (wysokoscObrazu - 1)) {
                            if (piksele[aktualny.getX()][aktualny.getY() + 1]) {
                                piksele[aktualny.getX()][aktualny.getY() + 1] = false;
                                stos.add(new Punkt(aktualny.getX(), aktualny.getY() + 1));
                            }
                        }
                        
                        if (aktualny.getY() > 0) {
                            if (piksele[aktualny.getX()][aktualny.getY() - 1]) {
                                piksele[aktualny.getX()][aktualny.getY() - 1] = false;
                                stos.add(new Punkt(aktualny.getX(), aktualny.getY() - 1));
                            }
                        }
                        
                        // Dodanie piksela ze stosu do zbioru
                        nowy.dodajPiksel(aktualny.getX(), aktualny.getY());
                    }
                    
                    obszary.add(nowy);
                    
                }
            }
        } 
        
        return obszary;        
    }
}
