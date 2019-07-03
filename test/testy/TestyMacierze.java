package testy;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import algorytm.Matematyczne;
import static algorytm.Matematyczne.obliczWielomian;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author MWr
 */
public class TestyMacierze {
    
    public TestyMacierze() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // The methods must be annotated with annotation @Test. For example:
    
    @Test
    public void wyznaczniki() {
    
        // Testowanie wyznacznika 3x3
        double przewidywanyWynik = 74.5;
        double wynik = Matematyczne.wyznacznikMacierzy3Rzędu(
                 3, 4, 3.5,
                 5, 6,   7,
                12, 1,   3);
        
        System.out.println(wynik);
        assertTrue(przewidywanyWynik == wynik);
        
        // Testowanie wyznacznika 4x4
        ArrayList<Double> kolumna1 = new ArrayList<>(Arrays.asList( 0.0, 5.0, 5.0,-2.0));
        ArrayList<Double> kolumna2 = new ArrayList<>(Arrays.asList(-1.0, 1.0,-4.0,-3.0));
        ArrayList<Double> kolumna3 = new ArrayList<>(Arrays.asList( 0.0,-2.0, 1.0, 4.0));
        ArrayList<Double> kolumna4 = new ArrayList<>(Arrays.asList( 3.0, 0.0,-5.0, 0.0));
        przewidywanyWynik = 203.0;
        
        wynik = Matematyczne.wyznacznikMacierzy4Rzedu(kolumna1, kolumna2, kolumna3, kolumna4);
        System.out.println(wynik);
        assertTrue(przewidywanyWynik == wynik);
        
        kolumna1 = new ArrayList<>(Arrays.asList( 0.0, 1.0, 2.0, 7.0));
        kolumna2 = new ArrayList<>(Arrays.asList( 1.0, 2.0, 3.0, 4.0));
        kolumna3 = new ArrayList<>(Arrays.asList( 5.0, 6.0, 7.0, 8.0));
        kolumna4 = new ArrayList<>(Arrays.asList(-1.0, 1.0,-1.0, 1.0));
        przewidywanyWynik = -64.0;
        
        wynik = Matematyczne.wyznacznikMacierzy4Rzedu(kolumna1, kolumna2, kolumna3, kolumna4);
        System.out.println(wynik);
        assertTrue(przewidywanyWynik == wynik);
    }
    
    @Test
    public void rownanie() {
    
        ArrayList<List<Double>> macierz = new ArrayList<>(4);
        macierz.add(new ArrayList<>(Arrays.asList( 0.0, 1.0, 2.0, 7.0)));
        macierz.add(new ArrayList<>(Arrays.asList( 1.0, 2.0, 3.0, 4.0)));
        macierz.add(new ArrayList<>(Arrays.asList( 5.0, 6.0, 7.0, 8.0)));
        macierz.add(new ArrayList<>(Arrays.asList(-1.0, 1.0,-1.0, 1.0)));
        
        ArrayList<Double> kolumnaY = new ArrayList<>(Arrays.asList(-15.2,-15.8,-17.2,-9.8));      
        
        ArrayList<Double> wyniki = Matematyczne.ukladRownan4Rzedu(macierz, kolumnaY);
        System.out.println(wyniki);
        
        // Sprawdzamy w zaokrągleniu
        assertTrue(Math.round(wyniki.get(0) -  2.0) == 0);
        assertTrue(Math.round(wyniki.get(1) -  0.0) == 0);
        assertTrue(Math.round(wyniki.get(2) +  3.0) == 0);
        assertTrue(Math.round(wyniki.get(3) -  0.2) == 0);
        
    }
    
    public void aproksymacja(double y1, double y2, double y3, double y4, double y5) {
    
        ArrayList<Double> kolumnaX = new ArrayList<>(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0)); 
        ArrayList<Double> kolumnaY = new ArrayList<>(Arrays.asList( y1,  y2,  y3,  y4,  y5));      
        
        ArrayList<Double> wyniki = Matematyczne.aproksymacjaW3(kolumnaX, kolumnaY);
        System.out.println(wyniki);
        
        double a = wyniki.get(0);
        double b = wyniki.get(1);
        double c = wyniki.get(2);
        double d = wyniki.get(3);
        
        ArrayList<Double> Y1 = new ArrayList<>(5);
        for (int i = 0; i < 5; ++i) {
            Y1.add(obliczWielomian(a, b, c, d, kolumnaX.get(i)));            
        }
       
        System.out.println(Y1);
        
        for (int i = 0; i < 5; ++i) {
            double blad = Y1.get(i) - kolumnaY.get(i);
            assertTrue(Math.round(blad) == 0);            
        }
    }
    
    @Test
    public void aproksymacja() {
        aproksymacja( 17.5, 29.0, 55.5,103.0,177.5);
        aproksymacja(  8.0,  3.0,  0.0, -1.0,  0.0);
        aproksymacja(  5.0,  3.0,  1.0, -1.0, -3.0);
        aproksymacja(  1.0,  4.0,  9.0, 16.0, 25.0);
    }
}
