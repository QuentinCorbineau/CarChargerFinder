import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.Icon;
import javax.swing.JLabel;

public class Borne{
	
    private String xCoord;
    private String yCoord;
   // private boolean available=true;
    private int nbrPlaces=4;
    private int nbrAvail=3;
    private JLabel marker=new JLabel();
    
    protected Borne(String x, String y, int places, int nb) throws RemoteException{
        super();
        this.xCoord = x;
        this.yCoord = y;
        this.nbrPlaces=places;
        this.nbrAvail=nb;
    }
    
    public String getX() {
        return xCoord;
    }
 /*   public void setX(String x) {
        this.xCoord = x;
    }*/
    public int getNbrePlaces(){
    	return nbrPlaces;
    }
    public int getNbreAvailable() {
        return nbrAvail;
    }
    public void setNbreAvailable(int nbreAvail) {
        this.nbrAvail = nbreAvail;
    }
    public void setMarker(Icon icon) {
        this.marker.setIcon(icon);
    }
    public JLabel getMarker() {
        return marker;
    }
    public String getY() {
        return yCoord;
    }
  /*  public void setY(String y) {
        this.yCoord = y;
    }*/
 
    public String toString(){
        return "Borne [X-Coordinate=;" + xCoord
                + "; Y-Coordinate=;" + yCoord + ";Places en total : ;"+nbrPlaces+";Bornes availables : ;" + nbrAvail +";]";
    }
}
