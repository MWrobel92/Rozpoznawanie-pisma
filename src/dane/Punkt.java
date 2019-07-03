package dane;

import algorytm.Matematyczne;

/**
 * Pojedynczy piksel
 * @author MWr
 */
public class Punkt implements Comparable, Cloneable {

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }
    
    public int x;
    public int y;
    
    public Punkt (int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public double odlegloscDo (Punkt p2) {
        double rx = getX() - p2.getX();
        double ry = getY() - p2.getY();
        return Math.sqrt(rx*rx+ry*ry);
    }
    
    /**
     * Oblicza kąt między odcinkami łączącymi dany punkt z punktami p1 i p2
     * @param p1
     * @param p2
     * @return 
     */
    public double obliczKat(Punkt p1, Punkt p2) {
        
        double x1 = p1.getX() - getX();
        double y1 = p1.getY() - getY();
        double x2 = p2.getX() - getX();
        double y2 = p2.getY() - getY();
        
        return Matematyczne.katMiedzyWektorami(x1, y1, x2, y2);  
        
    }
    
    @Override
    public int hashCode() {
        return getX() * 1000000 + getY();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Punkt other = (Punkt) obj;
        
        return ((x == other.getX()) && (y == other.getY()));
    }

    @Override
    public int compareTo(Object obj) {        
        final Punkt other = (Punkt) obj;
        if (getX() > other.getX()) {
            return 1;
        }
        else if (getX() < other.getX()) {
            return -1;
        }
        else {
            if (getY() > other.getY()) {
                return 1;
            }
            else if (getY() < other.getY()) {
                return -1;
            }
            else {
                return 0;
            }
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getX());
        sb.append(", ");
        sb.append(getY());
        return sb.toString();
    }
    
    @Override
    public Object clone() {
        return new Punkt(x, y);
    }
   
}
