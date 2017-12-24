import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
 
 
/**
 * Cette classe crée une fenêtre qui comporte deux boutons positionnés à des
 * coordonnées absolues, sans faire appel à un layout manager.
 *
 */
public class GUI  {

	
	Client clients;
	int check = 0 ;
	JFrame fenetre;
	
    public GUI(List<Borne> borneList, Client client) {
    	this.clients=client;
        // la création des objets graphiques est déléguée au thread de
        // diffusion d'événements de Swing
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // nouvelle fenêtre
                fenetre = new JFrame("Car Charger Finder");
                 
                // pas de layout manager pour cette fenêtre : 
                // on positionnera les composants à la min
                fenetre.setLayout(null);
                 
                // création du bandeau
                JPanel Bandeau = new Bandeau();
                
                
                //Description
                JPanel Description = new JPanel();
                JTextPane text = new JTextPane();
                text.setOpaque(false);
                StyledDocument doc = text.getStyledDocument();		
                MutableAttributeSet center = new SimpleAttributeSet();		
                StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
                doc.setParagraphAttributes(0, 0, center, true);
                Style defaut = text.getStyle("default");  
                Style style1 = text.addStyle("style1", defaut);
                StyleConstants.setForeground(style1, Color.GREEN);
                StyleConstants.setFontSize(style1, 25);
                StyleConstants.setFontFamily(style1, "Comic sans MS");
                Style style2 = text.addStyle("style2", defaut);
                StyleConstants.setForeground(style2, Color.BLACK);
                StyleConstants.setFontSize(style2, 16);
                StyleConstants.setFontFamily(style2, "Comic sans MS");
                
                String Titre  = "Bienvenue sur Car Charger Finder. \n ";
                String textdesc = "Le moyen pour vous, de trouver un emplacement à proximité \n pour recharger votre véhicule";
                StyledDocument sDoc = (StyledDocument)text.getDocument();
                try {
                      int pos = 0;
                      sDoc.insertString(pos, Titre, style1);pos+=Titre.length();
                      sDoc.insertString(pos, textdesc, style2);pos+=textdesc.length();

                } catch (BadLocationException e) { }
                text.setEditable(false);
                Description.add(text);
                
              //Création du panneau Maps
                JPanel PanMaps = new JPanel(new BorderLayout());
        	    
        		
                new JGoogleMapEditorPan();
				JGoogleMapEditorPan Maps = JGoogleMapEditorPan.showMap(borneList, client);
                PanMaps.add(Maps);
                Maps.setBounds(0,0,700,600);
                
                
                JButton Deconnexion = new JButton("Deconnexion" );
        	    Maps.add(Deconnexion);
        	    Deconnexion.setBounds(0, 0, 150, 30);
        	    

        	    Deconnexion.addActionListener(new ActionListener(){
        	        public void actionPerformed(ActionEvent event){
        	        	clients.disconnect();
        	        	fenetre.setVisible(false);
        	        	fenetre.dispose();
        	        }
        	    });
        	    
        	    
        	    
              //Creation du panneau inscription
                JPanel PanInscription = new JPanel(new BorderLayout());
        		JPanel PanLabel2 = new JPanel(new GridLayout(2,1));
        	    JPanel PanField2 = new JPanel(new GridLayout(2,1));
        	    JPanel PanButton2 = new JPanel();
        	    
        	    PanLabel2.add(new JLabel(" Email  " ));
        	    PanLabel2.add(new JLabel(" Password  " ));
        	    
        	    JTextField newlogin = new JTextField(20);
        	    JTextField newpassword = new JPasswordField(20);
        	    PanField2.add(newlogin);
        	    PanField2.add(newpassword);
        	    
        	    JButton Connexionai = new JButton("Connexion" );
        	    PanButton2.add(Connexionai);

        	    Connexionai.addActionListener(new ActionListener(){
        	        public void actionPerformed(ActionEvent event){
        	        	String res=clients.createLogin(newlogin.getText(), newpassword.getText());
        	        	if (res.equals("Creation OK")){
        	        		fenetre.remove(PanInscription);
            	        	fenetre.remove(Description);
            	        	fenetre.add(PanMaps);
            	        	fenetre.revalidate();   
            	        	fenetre.repaint();
        	        	}
        	        	else
        	        		JOptionPane.showMessageDialog(null, res, "Erreur", JOptionPane.WARNING_MESSAGE);        	        
        	        }
        	       });
        

        	    PanInscription.add(PanLabel2, BorderLayout.WEST);
        	    PanInscription.add(PanField2, BorderLayout.CENTER);
        	    PanInscription.add(PanButton2, BorderLayout.SOUTH);
                
        	    
        	    
        	    
                //Creation du panneau connexion
                JPanel PanConnect = new JPanel(new BorderLayout());
        		JPanel PanLabel = new JPanel(new GridLayout(2,1));
        	    JPanel PanField = new JPanel(new GridLayout(2,1));
        	    JPanel PanButton = new JPanel(new GridLayout(1,2));
        	    
        	    PanLabel.add(new JLabel(" Email  " ));
        	    PanLabel.add(new JLabel(" Password  " ));
        	    
        	    JTextField login = new JTextField(20);
        	    JTextField password = new JPasswordField(20);
        	    PanField.add(login);
        	    PanField.add(password);
        	    
        	    JButton Connexion = new JButton("Connexion" );
        	    JButton Inscription = new JButton("Pas encore inscrit ?" );
        	    PanButton.add(Connexion);
        	    PanButton.add(Inscription);
        	    Inscription.addActionListener(new ActionListener(){
        	        public void actionPerformed(ActionEvent event){
        	        	
        	        	fenetre.remove(PanConnect);    
        	        	fenetre.add(PanInscription);
        	        	fenetre.revalidate();
        	        	fenetre.repaint();
        	        }
        	      });

        	    Connexion.addActionListener(new ActionListener(){
        	        public void actionPerformed(ActionEvent event){
        	        	String connect=clients.connectLogin(login.getText(), password.getText());
        	        	if (connect.equals("Connexion réussie!")){
        					fenetre.remove(PanConnect);
        		        	fenetre.remove(Description);
        		        	fenetre.add(PanMaps);
        		        	fenetre.revalidate();
        		        	fenetre.repaint();
        				}
        				else 
        					JOptionPane.showMessageDialog(null, connect, "Erreur", JOptionPane.ERROR_MESSAGE);
        	        	}
        	        
        	      });
        	    
        	    PanConnect.add(PanLabel, BorderLayout.WEST);
        	    PanConnect.add(PanField, BorderLayout.CENTER);
        	    PanConnect.add(PanButton, BorderLayout.SOUTH);
                
        	    
        	    
        	  
        	  
                 
                // ajout du bandeau à la fenêtre
        	    fenetre.add(Description);
                fenetre.add(Bandeau);
                fenetre.add(PanConnect);
                 
                // positionnement et dimensionnement manuel
                Bandeau.setBounds(0, 0, 650, 100);
                Description.setBounds(0, 150, 600, 100);
                PanConnect.setBounds(100, 350, 450, 100);
                PanInscription.setBounds(100, 350, 450, 100);
                PanMaps.setBounds(0, 100, 650, 550);
                
          
                 
                // quitter le programme lorsqu'on ferme la fenêtre
                fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                 
                // dimensionnement en affichage de la fenêtre
                fenetre.setSize(650, 650);
                fenetre.setResizable(false);
                fenetre.setVisible(true);
            }
             
        });
        
        
    }
    
    public void rePaint(){
    	fenetre.repaint();
    }

	public void setMap(){
		check = 1;
	}

	
}