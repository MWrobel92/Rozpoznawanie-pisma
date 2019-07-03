/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testy;

import dane.Kreska;
import dane.Punkt;
import java.util.LinkedList;
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
public class TestyKreski {
    
    public TestyKreski() {
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

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void aproksymacja() {
        LinkedList<Punkt> punkty = new LinkedList<>();
        punkty.add(new Punkt(2, 4));
        punkty.add(new Punkt(3, 4));
        punkty.add(new Punkt(4, 4));
        punkty.add(new Punkt(5, 4));
        punkty.add(new Punkt(6, 4));
        
        Kreska kreska = new Kreska(punkty);
        
        double d = kreska.d;
        List<Double> wektorA = kreska.wynikAproksymacji;
        List<Double> wektorB = kreska.wektorKsztaltu;
        
        System.out.println(d);
        System.out.println(wektorA);
        System.out.println(wektorB);
        
        assertEquals(wektorA.get(1), wektorB.get(0) * d, 0.001);
    }
}
