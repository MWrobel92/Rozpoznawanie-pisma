package algorytm;

import java.awt.image.BufferedImage;
import java.util.LinkedList;

/**
 *
 * @author Michał
 */
public class ObrazBinarny {
    
    int wysokosc;
    int szerokosc;
    boolean[][] piksel;
    
    private final int bialy = 16777215; // #FFFFFF
    
    public ObrazBinarny(int s, int w) {
        
        wysokosc = w;
        szerokosc = s;
        
        piksel = new boolean[szerokosc][wysokosc];
    }
    
    /**
     * 
     * @param x
     * @param y
     * @param domyslne To zostanie zwrócone, jeśli piksel jest poza obszarem.
     * @return 
     */
    boolean pobierzPiksel(int x, int y, boolean domyslne) {
        
        if ((x < 0) || (y < 0) || (x >= szerokosc) || (y >= wysokosc)) {
            return domyslne;
        } else {
            return piksel[x][y];
        }
        
    }
    
    /**
     * Wykonuje erozję obrazu elementaem strukturalnym 3x3 i zwraca nowy obraz
     * @return 
     */
    public ObrazBinarny erozja() {
        
        ObrazBinarny nowy = new ObrazBinarny(szerokosc, wysokosc);
        
        for (int i = 0; i < szerokosc; ++i) {
            for (int j = 0; j < wysokosc; ++j) {
                
                boolean n = pobierzPiksel(i, j, true);
                
                // Sąsiedztwo czterospójne
                n &= pobierzPiksel(i-1, j, true);
                n &= pobierzPiksel(i+1, j, true);
                n &= pobierzPiksel(i, j-j, true);
                n &= pobierzPiksel(i, j+1, true);
                
                // Sąsiedztwo ośmiospójne
                // n &= pobierzPiksel(i-1, j-1, true);
                // n &= pobierzPiksel(i+1, j-1, true);
                // n &= pobierzPiksel(i-1, j+1, true);
                // n &= pobierzPiksel(i+1, j+1, true);

                
                nowy.piksel[i][j] = n;
                
            } 
        } 
        
        return nowy;
    }
    
    public ObrazBinarny dylatacja() {
        
        ObrazBinarny nowy = new ObrazBinarny(szerokosc, wysokosc);
        
        for (int i = 0; i < szerokosc; ++i) {
            for (int j = 0; j < wysokosc; ++j) {
                
                boolean n = pobierzPiksel(i, j, false);
                
                // Sąsiedztwo czterospójne
                n |= pobierzPiksel(i-1, j, false);
                n |= pobierzPiksel(i+1, j, false);
                n |= pobierzPiksel(i, j-j, false);
                n |= pobierzPiksel(i, j+1, false);
                                
                nowy.piksel[i][j] = n;
                
            } 
        } 
        
        return nowy;
    }
    
    /**
     * Dylatacja elementem strukturalnym typu pozioma linia
     * @param n Liczba pikseli w jednym "ramieniu" linii
     * @return 
     */
    public ObrazBinarny dylatacjaPozioma (int n) {
        
        ObrazBinarny nowy = new ObrazBinarny(szerokosc, wysokosc);
        
        for (int i = 0; i < szerokosc; ++i) {
            for (int j = 0; j < wysokosc; ++j) {

                boolean npx = false;
                
                for (int k = (i-n); k < (i+n); ++k) {                
                    npx |= pobierzPiksel(k, j, false);
                }
                                
                nowy.piksel[i][j] = npx;
                
            } 
        } 
        
        return nowy;
    }
    
    /**
     * Szkieletyzacja obrazu za pomocą szybkiego algorytmu równoległego (Zhang, Suen, 1984)
     * @return Nowy obraz
     */
    public ObrazBinarny szkieletyzacja () {
        
        ObrazBinarny nowyObraz = new ObrazBinarny(szerokosc, wysokosc);
        ObrazBinarny tymczasowy = new ObrazBinarny(szerokosc, wysokosc);
        
        // Zmienne pomocnicze
        int i, j;
        // boolean n;
        
        // Przepisanie obrazu do nowego, wyczyszczenie tymczasowego
        for (i = 0; i < szerokosc; ++i) {
            for (j = 0; j < wysokosc; ++j) {
                nowyObraz.piksel[i][j] = piksel[i][j];
                tymczasowy.piksel[i][j] = false;
            } 
        } 
        
        // Właściwa szkieletyzacja
        int doUsuniecia = 1;
        boolean parzysty = false;
        
        while (doUsuniecia > 0) {
            
            doUsuniecia = 0;
            
            // Typowanie pikseli do usunięcia
            for (i = 0; i < szerokosc; ++i) {
                for (j = 0; j < wysokosc; ++j) {
                    
                    if (nowyObraz.sprawdzPiksel(i, j, parzysty)) {
                        tymczasowy.piksel[i][j] = true;
                        doUsuniecia++;
                    }
                } 
            } 
            
            // Usunięcie wytypowanych pikseli z obrazu (odejmowanie macierzy)
            for (i = 0; i < szerokosc; ++i) {
                for (j = 0; j < wysokosc; ++j) {  
                    if (tymczasowy.piksel[i][j]) {
                        nowyObraz.piksel[i][j] = false;
                        tymczasowy.piksel[i][j] = false;
                    }
                } 
            } 
            
            parzysty = !parzysty;
        }
        
        return nowyObraz;
    }
    
    public BufferedImage obrazWyjsciowy() {
        
        BufferedImage wynik = new BufferedImage(szerokosc, wysokosc, BufferedImage.TYPE_INT_RGB);
        
        for (int i = 0; i < szerokosc; ++i) {
            for (int j = 0; j < wysokosc; ++j) {
                
                // Konwersja piksela
                if (piksel[i][j]) {
                    wynik.setRGB(i, j, 0); // Czarny                    
                }
                else {
                    wynik.setRGB(i, j, 0xFFFFFF); // Biały
                }
                
            } 
        } 
        
        return wynik;
        
    }
    
    public BufferedImage obrazPowiekszony(int skala) {
        
        BufferedImage wynik = new BufferedImage(szerokosc*skala, wysokosc*skala, BufferedImage.TYPE_INT_RGB);
        
        for (int i = 0; i < szerokosc; ++i) {
            for (int j = 0; j < wysokosc; ++j) {
                
                for (int k = i*skala; k < (i+1)*skala; ++k) {
                    for (int l = j*skala; l < (j+1)*skala; ++l) {
                        
                        // Konwersja piksela
                        if (piksel[i][j]) {
                            wynik.setRGB(k, l, 0); // Czarny                    
                        }
                        else {
                            wynik.setRGB(k, l, 0xFFFFFF); // Biały
                        }
                    }
                }
                
            } 
        } 
        
        return wynik;
        
    }
    
    /**
     * Funkcja sprawdzająca, czy dany piksel należy usunąć
     * @param x
     * @param y
     * @return 
     */
    private boolean sprawdzPiksel(int x, int y, boolean parzysty) {
        
        boolean wynik = false;
        
        boolean a, b, c, d;
        
        if (piksel[x][y]) {
            
            // Utworzenie listy sąsiedztwa
            LinkedList<Boolean> sasiedzi = new LinkedList<>();
            
            sasiedzi.add(pobierzPiksel(x,   y-1, false));
            sasiedzi.add(pobierzPiksel(x+1, y-1, false));
            sasiedzi.add(pobierzPiksel(x+1, y,   false));
            sasiedzi.add(pobierzPiksel(x+1, y+1, false));
            sasiedzi.add(pobierzPiksel(x,   y+1, false));
            sasiedzi.add(pobierzPiksel(x-1, y+1, false));
            sasiedzi.add(pobierzPiksel(x-1, y  , false));
            sasiedzi.add(pobierzPiksel(x-1, y-1, false));
            
            // Wyznaczenie liczby sąsiadów
            int liczbaSasiadow = 0;
            for (Boolean s : sasiedzi) {
                if (s) {
                    liczbaSasiadow++;
                }
            }            
            a = ((liczbaSasiadow > 1) && (liczbaSasiadow < 7));
            
            // Wyznaczenie liczby przejść 0 -> 1
            int liczbaPrzejsc = 0;
            boolean poprzedni = sasiedzi.get(7);
            for (Boolean s : sasiedzi) {
                if (!poprzedni && s) {
                    liczbaPrzejsc++;
                }
                poprzedni = s;
            }            
            b = (liczbaPrzejsc == 1);
            
            // Sprawdzenie dodatkowych warunków
            
            if (parzysty) {
                c = !(sasiedzi.get(0) && sasiedzi.get(2) && sasiedzi.get(6));
                d = !(sasiedzi.get(0) && sasiedzi.get(4) && sasiedzi.get(6));
            } else {
                c = !(sasiedzi.get(0) && sasiedzi.get(2) && sasiedzi.get(4));
                d = !(sasiedzi.get(2) && sasiedzi.get(4) && sasiedzi.get(6));
            }
                   
            wynik = (a && b && c && d);
            
        }
        
        return wynik;
    }

    boolean[][] zwrocKopiePikseli() {
        
        boolean[][] nowe = new boolean[szerokosc][wysokosc];
        
        for (int i = 0; i < szerokosc; ++i) {
            for (int j = 0; j < wysokosc; ++j) {
                nowe[i][j] = piksel[i][j];
            }
        } 
        
        return nowe;
    }
}
