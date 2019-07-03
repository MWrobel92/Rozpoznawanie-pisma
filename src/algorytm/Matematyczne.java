/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorytm;

import dane.Punkt;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author MWr
 */
public class Matematyczne {
    
    public static double pitagoras(double x, double y) {
        return Math.sqrt(x*x + y*y);
    }
    
    public static double krzywiznaMengera(Punkt p1, Punkt p2, Punkt p3) {
        
        // Obliczenie długości boków
        double a = pitagoras(p1.getX() - p2.getX(), p1.getY() - p2.getY());
        double b = pitagoras(p2.getX() - p3.getX(), p2.getY() - p3.getY());
        double c = pitagoras(p3.getX() - p1.getX(), p3.getY() - p1.getY());
        
        // Obliczenie pola powierzchni trójkąta
        double p = (a+b+c)/2.0;
        double s = Math.sqrt(p*(p-a)*(p-b)*(p-c));
        
        // Obliczenie krzywizny
        return (4*s)/(a*b*c);
    }
    
    public static double katMiedzyWektorami (double x1, double y1, double x2, double y2) {
        
        double cosinus = (x1 * x2 + y1 * y2) / ( Math.sqrt(x1 * x1 + y1 * y1) * Math.sqrt(x2 * x2 + y2 * y2) );

        // Elininacja błędów zaokrąglenia
        cosinus = Math.min(cosinus, 1.0);
        cosinus = Math.max(cosinus, -1.0);

        double kat = Math.acos( cosinus );
        
        return kat;
    }
    
    public double bladSredniokwadratowy(List<Double> wektorX, List<Double> wektorY, List<Double> wielomian) {
        
        int liczbaPunktow = Math.min(wektorX.size(), wektorY.size());
        int stopienWielomianu = wielomian.size();
        
        double blad = 0.0;
        for (int i = 0; i < liczbaPunktow; ++i) {
            
            double x = wektorX.get(i);
            double y = wektorY.get(i);
            
            double wynik = 0.0;
            for(int j = 0; j < stopienWielomianu; ++j) {
                wynik += wielomian.get(j) * Math.pow(x, j);
            }
            
            blad += (wynik - y) * (wynik - y);            
        }
        
        return blad / (double)liczbaPunktow;
    }
    
    public static double obliczWielomian(double a, double b, double c, double d, double x) {
        return a + b*x + c*x*x + d*x*x*x;
    } 
        
    public static double wyznacznikMacierzy3Rzędu(double a11, double a12, double a13, 
            double a21, double a22, double a23, double a31, double a32, double a33) {
        
        return a11*a22*a33 + a12*a23*a31 + a13*a21*a32 - a13*a22*a31 - a11*a23*a32 - a12*a21*a33;
        
    } 
    
    /** Oblicza wyznacznik macierzy 4 rzędu dla danych podanych KOLUMNAMI! */
    public static double wyznacznikMacierzy4Rzedu(List<Double> kol1, List<Double> kol2, List<Double> kol3, List<Double> kol4) {
       
        double w1 = kol1.get(0) * wyznacznikMacierzy3Rzędu(
                kol2.get(1), kol3.get(1), kol4.get(1),
                kol2.get(2), kol3.get(2), kol4.get(2),
                kol2.get(3), kol3.get(3), kol4.get(3) );
        
        double w2 = kol1.get(1) * wyznacznikMacierzy3Rzędu(
                kol2.get(0), kol3.get(0), kol4.get(0),
                kol2.get(2), kol3.get(2), kol4.get(2),
                kol2.get(3), kol3.get(3), kol4.get(3) );
        
        double w3 = kol1.get(2) * wyznacznikMacierzy3Rzędu(
                kol2.get(0), kol3.get(0), kol4.get(0),
                kol2.get(1), kol3.get(1), kol4.get(1),
                kol2.get(3), kol3.get(3), kol4.get(3) );
        
        double w4 = kol1.get(3) * wyznacznikMacierzy3Rzędu(
                kol2.get(0), kol3.get(0), kol4.get(0),
                kol2.get(1), kol3.get(1), kol4.get(1),
                kol2.get(2), kol3.get(2), kol4.get(2) );
        
        return w1 - w2 + w3 - w4;
    }
    
    public static ArrayList<Double> ukladRownan4Rzedu(List<List<Double>> macierz, List<Double> wyniki) {
        
        // Wyznaczniki macierzy
        double w0 = wyznacznikMacierzy4Rzedu(macierz.get(0), macierz.get(1), macierz.get(2), macierz.get(3));
        double w1 = wyznacznikMacierzy4Rzedu(        wyniki, macierz.get(1), macierz.get(2), macierz.get(3));
        double w2 = wyznacznikMacierzy4Rzedu(macierz.get(0),         wyniki, macierz.get(2), macierz.get(3));
        double w3 = wyznacznikMacierzy4Rzedu(macierz.get(0), macierz.get(1),         wyniki, macierz.get(3));
        double w4 = wyznacznikMacierzy4Rzedu(macierz.get(0), macierz.get(1), macierz.get(2),         wyniki);
        
        return new ArrayList<>(Arrays.asList(w1/w0, w2/w0, w3/w0, w4/w0));
    }
    
    public static ArrayList<Double> aproksymacjaW3(List<Double> x, List<Double> y) {
    
        // Wyznaczenie liczb do macierzy
        double sx1 = 0.0;    double sy1 = 0.0;
        double sx2 = 0.0;    double sy2 = 0.0;
        double sx3 = 0.0;    double sy3 = 0.0;
        double sx4 = 0.0;    double sy4 = 0.0;
        double sx5 = 0.0;
        double sx6 = 0.0;
        
        int liczbaPunktow = Math.min(x.size(), y.size());
        double sx0 = liczbaPunktow;
        
        for (int i = 0; i < liczbaPunktow; ++i) {
            double xx = x.get(i);
            double yy = y.get(i);
            
            sx1 += (xx);                sy1 += (yy);
            sx2 += (xx*xx);             sy2 += (yy*xx);
            sx3 += (xx*xx*xx);          sy3 += (yy*xx*xx);
            sx4 += (xx*xx*xx*xx);       sy4 += (yy*xx*xx*xx);
            sx5 += (xx*xx*xx*xx*xx);
            sx6 += (xx*xx*xx*xx*xx*xx);
         
        }
        
        // Uwaga: macierz ułożona jest KOLUMNAMI!          
        ArrayList<List<Double>> macierz = new ArrayList<>(4);
        macierz.add(new ArrayList<>(Arrays.asList(sx0, sx1, sx2, sx3)));
        macierz.add(new ArrayList<>(Arrays.asList(sx1, sx2, sx3, sx4)));
        macierz.add(new ArrayList<>(Arrays.asList(sx2, sx3, sx4, sx5)));
        macierz.add(new ArrayList<>(Arrays.asList(sx3, sx4, sx5, sx6)));
        
        ArrayList<Double> kolumnaY = new ArrayList<>(Arrays.asList(sy1, sy2, sy3, sy4));        
        return Matematyczne.ukladRownan4Rzedu(macierz, kolumnaY);

    }
    
}
