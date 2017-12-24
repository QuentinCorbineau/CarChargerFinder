import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
 
/**
 * Afficher une carte GoogleMap dans un JEditorPane
 * http://www.fobec.com/java/922/afficher-une-carte-avec-api-google-map.html
 * @author fobec 2010
 */
public class JGoogleMapEditorPan extends JEditorPane {
 
    private int zoomFactor = 13;
    private String ApiKey = "AIzaSyBa5XaphM3DaC46w0LR_JPBvgjclGFduXo";
    private String roadmap = "roadmap";
    public final String viewTerrain = "terrain";
    public final String viewSatellite = "satellite";
    public final String viewHybrid = "hybrid";
    public final String viewRoadmap = "roadmap";
    
    //dimensions for the map and markers in pixel (and coordinate distances)
    final static int MAP_WIDTH = 650 ;
    final static int MAP_HEIGHT = 550;
    final static int MAP_WIDTH_COORDINATES = 1020*MAP_WIDTH/600 ;
    final static int MAP_HEIGHT_COORDINATES = 730*MAP_HEIGHT/600;
    final static int MARKER_WIDTH = 20;
    final static int MARKER_HEIGHT = 20;
    
    //images for the markers
    static ImageIcon markerGreen = new ImageIcon(new ImageIcon("Images/greenMarker.png").getImage().getScaledInstance(MARKER_WIDTH,MARKER_HEIGHT, 20));
	static ImageIcon markerRed = new ImageIcon(new ImageIcon("Images/redMarker.png").getImage().getScaledInstance(MARKER_WIDTH,MARKER_HEIGHT,20));
	
	static JLabel [] tabMarker;
    /**
     * Constructeur: initialisation du EditorKit
     */
    public JGoogleMapEditorPan() {
        HTMLEditorKit kit = new HTMLEditorKit();
        HTMLDocument htmlDoc = (HTMLDocument) kit.createDefaultDocument();
        this.setEditable(false);
        this.setContentType("text/html");
        this.setEditorKit(kit);
        this.setDocument(htmlDoc);
    }
 
    /**
     * Fixer le zoom
     * @param zoom valeur de 0 à 21
     */
    public void setZoom(int zoom) {
        this.zoomFactor = zoom;
    }
 
    /**
     * Fixer la clé de developpeur
     * @param key APIKey fourni par Google
     */
    public void setApiKey(String key) {
        this.ApiKey = key;
    }
 
    /**
     * Fixer le type de vue
     * @param roadMap
     */
    public void setRoadmap(String roadMap) {
        this.roadmap = roadMap;
    }
 
    /**
     * Afficher la carte d'après des coordonnées GPS
     * @param latitude
     * @param longitude
     * @param width
     * @param height
     * @throws Exception erreur si la APIKey est non renseignée
     */
    public void showCoordinate(String latitude, String longitude, int width, int height,List<Borne> borneList) throws Exception {
        this.setMap(latitude, longitude, width, height,borneList);
    }
 
    /**
     * Afficher la carte d'après une ville et son pays
     * @param city
     * @param country
     * @param width
     * @param height
     * @throws Exception erreur si la APIKey est non renseignée
     */
    public void showLocation(String city, String country, int width, int height,List<Borne> borneList) throws Exception {
        this.setMap(city, country, width, height,borneList);
    }
    
    
 
    /**
     * Assembler l'url et Générer le code HTML
     * @param x
     * @param y
     * @param width
     * @param height
     * @throws Exception
     */
    private void setMap(String x, String y, Integer width, Integer height, List<Borne> borneList) throws Exception {
        if (this.ApiKey.isEmpty()) {
            throw new Exception("Developper API Key not set !!!!");
        }
 
       
        String url = "http://maps.google.com/maps/api/staticmap?";
        url += "center=" + x + "," + y;
        url += "&zoom=" + this.zoomFactor;
        url += "&size=" + width.toString() + "x" + height.toString();
        url += "&maptype=" + this.roadmap;     
        url += "&sensor=false";
        url += "&key=" + this.ApiKey;
 
        String html = "<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'>";
        html += "<html><head></head><body>";
        html += "<img src='" + url + "'>";
        html += "</body></html>";
        this.setText(html);
    }
 
    /**
     * Exemple : JGoogleMapEditorPan dans un JFrame
     */
    public static JGoogleMapEditorPan showMap(List<Borne>borneList, Client client) {
        JGoogleMapEditorPan googleMap = new JGoogleMapEditorPan();
        tabMarker =   new JLabel[borneList.size()];
        
        int i=0;
        try {
           // googleMap.setApiKey("AIzaSyBa5XaphM3DaC46w0LR_JPBvgjclGFduXo"); //elle est déjà rentrée au début
            //  googleMap.setRoadmap(googleMap.viewHybrid);
        	googleMap.setZoom(13);
        	
        	//temporairement
            
            for(Borne bor:borneList){
            	if(i<=10){
            		bor.setNbreAvailable(0);
            		i++;
            	}
            }
 
            /**
            Afficher la ville de Gardanne
             */
           // googleMap.showLocation("gardanne", "france", MAP_WIDTH, MAP_HEIGHT, borneList);
            /**
             * Afficher Nice en fonction ses coordonnées GPS : 43.7102° N, 7.2620° E
             */
            String x="43.7102";
            String y="7.2620";
              googleMap.showCoordinate(x, y,MAP_WIDTH, MAP_HEIGHT, borneList);
              
              //creating mouseadapter to listen on Markers
              MouseAdapter ma=new MouseAdapter(){ 
                  public void mouseClicked(MouseEvent me) { 
                	  for(Borne bor : borneList)
                      {   
                		
                    	if (bor.getMarker()==me.getComponent()){
                    		int choix = JOptionPane.showConfirmDialog (googleMap,
                					"Il y a "+bor.getNbreAvailable()+" places.\nVoulez-vous occuper (Oui) ou libérer (Non) une borne ?");
                			if (choix==0){
                				if (bor.getNbreAvailable()==0)
                					JOptionPane.showMessageDialog (googleMap,
                        					"Vous ne pouvez pas occuper de place!\nIl n'y a plus de place libre !");
                				else{
                					bor.setNbreAvailable(bor.getNbreAvailable()-1);
                					if (bor.getNbreAvailable()==0)
                						bor.setMarker(markerRed);
                					JOptionPane.showMessageDialog (googleMap,
                        					"Vous occupez une place!");
                				}
                			}
                			else if (choix==1)
                				if (bor.getNbreAvailable()==bor.getNbrePlaces())
                					JOptionPane.showMessageDialog (googleMap,
                        					"Vous ne pouvez pas libérez de place!\nElles sont déjà toutes libres!");
                				else{
                					bor.setNbreAvailable(bor.getNbreAvailable()+1);
                					if (bor.getNbreAvailable()==1)
                						bor.setMarker(markerGreen);
                					JOptionPane.showMessageDialog (googleMap,
                        					"Vous avez libéré une place!");
                				}
                    	}
                      }
                	  client.sendListBornes();
                    }
                  }; 
              //showing markers with icons
              //Adding markers of all the locations with car chargers
              String xMark =new String();
              String yMark =new String();
              int xInt;
              int yInt;
              String[] xSplit;
      		String[] ySplit;
      		xSplit= x.split("\\.");
      		ySplit= y.split("\\.");
      		xInt=Integer.parseInt(xSplit[0])*10000+Integer.parseInt(xSplit[1]);
      		yInt=Integer.parseInt(ySplit[0])*10000+Integer.parseInt(ySplit[1]);
      		
              for(Borne bor : borneList)
              {              	
            	xMark=bor.getY().substring(0,7);
            	yMark=bor.getX().substring(0,6);
            	int xMarkInt;
            	int yMarkInt;
            	xSplit= xMark.split("\\.");
              	ySplit= yMark.split("\\.");
              	xMarkInt=Integer.parseInt(xSplit[0])*10000+Integer.parseInt(xSplit[1]);
              	yMarkInt=Integer.parseInt(ySplit[0])*10000+Integer.parseInt(ySplit[1]);
              	if (xMarkInt>=xInt-MAP_HEIGHT_COORDINATES/2&&xMarkInt<=xInt+MAP_HEIGHT_COORDINATES/2&&yMarkInt>=yInt-MAP_WIDTH_COORDINATES/2&&yMarkInt<=yInt+MAP_WIDTH_COORDINATES/2){
              		if (bor.getNbreAvailable()>0){
              			bor.setMarker(markerGreen);
              		}
              		else{
              			bor.setMarker(markerRed);
              		}
              		bor.getMarker().setSize(MARKER_WIDTH,MARKER_HEIGHT);
              		bor.getMarker().setLocation(MAP_WIDTH/2+MAP_WIDTH*(yMarkInt-yInt)/MAP_WIDTH_COORDINATES-MARKER_WIDTH/2, MAP_HEIGHT/2+MAP_HEIGHT*(-xMarkInt+xInt)/MAP_HEIGHT_COORDINATES-MARKER_HEIGHT+1);
              		
              		bor.getMarker().addMouseListener(ma);
              		googleMap.add(bor.getMarker());
              	}
              }
                   
        } catch (Exception ex) {
            Logger.getLogger(JGoogleMapEditorPan.class.getName()).log(Level.SEVERE, null, ex);
        }
        googleMap.setPreferredSize(new Dimension(MAP_WIDTH,MAP_HEIGHT));
        return googleMap;
        
    }
}