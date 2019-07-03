package algorytm;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Klasa reprezentująca obraz w odcieniach szarości jako prostą tablicę pikseli
 * @author Michał
 */
public class ObrazOdcienieSzarosci {
    
    int wysokosc;
    int szerokosc;
    short[][] piksel;
    
    /**
     * Konstruktor tworzący obraz (tablicę pikseli) w odcieniach szarości na podstawie obrazu kolorowego
     * @param obrazKolorowy 
     */
    public ObrazOdcienieSzarosci(BufferedImage obrazKolorowy) {
        
        szerokosc = obrazKolorowy.getWidth();
        wysokosc = obrazKolorowy.getHeight();        
        
        piksel = new short[szerokosc][wysokosc];        
        
        for (int i = 0; i < szerokosc; ++i) {
            for (int j = 0; j < wysokosc; ++j) {
            
                // Konwersja piksela
                int rgb = obrazKolorowy.getRGB(i, j);
                Color c = new Color(rgb);
                int nowykolor = ( c.getBlue() + c.getGreen() + c.getRed() ) / 3;
                piksel[i][j] = (short)nowykolor;                
            } 
        }          
    }
    
    /**
     * Wyznacza wartość progową całego obrazu za pomocą metody Otsu
     * @return 
     */
    public short metodaOtsu() {
        return metodaOtsu(0, 0, szerokosc, wysokosc);
    }
    
    /**
     * Wyznacza wartość progową fragmentu obrazu za pomocą metody Otsu
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return Wyznaczony próg
     */
    public short metodaOtsu(int x1, int y1, int x2, int y2) {
        
        int[] histogram = new int[256];
        
        for (int h : histogram) { h = 0; } // Wyzerowanie tablicy
        
        // Wykonanie histogramu
        for (int i = x1; i < x2; ++i) {
            for (int j = y1; j < y2; ++j) {
                histogram[piksel[i][j]] += 1;
            } 
        }  
                
        int total = (x2-x1) * (y2-y1); // wysokość * szerokość

        float sum = 0;
        for (int t=0 ; t<256 ; t++) sum += t * histogram[t];

        float sumB = 0;
        int wB = 0;
        int wF = 0;

        float varMax = 0;
        short prog = 0;

        for (short t=0 ; t<256 ; t++) {
           wB += histogram[t];               // Weight Background
           if (wB == 0) continue;

           wF = total - wB;                 // Weight Foreground
           if (wF == 0) break;

           sumB += (float) (t * histogram[t]);

           float mB = sumB / wB;            // Mean Background
           float mF = (sum - sumB) / wF;    // Mean Foreground

           // Calculate Between Class Variance
           float varBetween = (float)wB * (float)wF * (mB - mF) * (mB - mF);

           // Check if new maximum found
           if (varBetween > varMax) {
              varMax = varBetween;
              prog = t;
           }
        }
        
        return prog;
    }
    
    /**
     * Wyznacza wartość progową każdego piksela za pomocą metody Otsu w wersji lokalnej
     * @return 
     */
    public short[][] progDynamiczny() {
        
        short[][] macierzProgow = new short[szerokosc][wysokosc];
        
        int szerokoscKafelka = szerokosc / 10; // Wartość przykładowa
        int x1, x2, y1, y2;
               
        // Przygotowanie tablicy progow        
        for (int x = 0; x < szerokosc; x += 1) {
            for (int y = 0; y < wysokosc; y += 1) {
                
                x1 = Math.max(x-szerokoscKafelka, 0);
                x2 = Math.min(x+szerokoscKafelka, szerokosc);
                y1 = Math.max(y-szerokoscKafelka, 0);
                y2 = Math.min(y+szerokoscKafelka, wysokosc);
                
                macierzProgow[x][y] = metodaOtsu(x1, y1, x2, y2);
                
            }
        }
         
        return macierzProgow;
    }
    
    public ObrazBinarny obrazWyjsciowy(short prog) {
        
        ObrazBinarny wynik = new ObrazBinarny(szerokosc, wysokosc);
        
        for (int i = 0; i < szerokosc; ++i) {
            for (int j = 0; j < wysokosc; ++j) {
                
                // Konwersja piksela
                wynik.piksel[i][j] = (piksel[i][j] < prog);
                
            } 
        } 
        
        return wynik;
        
    }
    
    public ObrazBinarny obrazWyjsciowy(short[][] prog) {
                
        ObrazBinarny wynik = new ObrazBinarny(szerokosc, wysokosc);
        
        for (int i = 0; i < szerokosc; ++i) {
            for (int j = 0; j < wysokosc; ++j) {
                
                // Konwersja piksela
                wynik.piksel[i][j] = (piksel[i][j] < prog[i][j]);
                
            } 
        } 
        
        return wynik;
                
    }
}
