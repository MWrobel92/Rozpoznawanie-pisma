/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dane;

/**
 *
 * @author MWr
 */
public class PunktZmiennoprzecinkowy {
    
    public double x;
    public double y;
    
    public PunktZmiennoprzecinkowy(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public double odlegloscDo (PunktZmiennoprzecinkowy p2) {
        double rx = x - p2.x;
        double ry = y - p2.y;
        return Math.sqrt(rx*rx+ry*ry);
    }
}
