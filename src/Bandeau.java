import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;


public class Bandeau extends JPanel {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void paintComponent(Graphics g) {
	  try {
	      
	      //Pour une image de fond
	      Image imgfond = ImageIO.read(new File("Images/bandeau-final.png"));
	      g.drawImage(imgfond, 0, 0, this);
	    } 
	  catch (IOException e) {
	      e.printStackTrace();
	    } 

}
  

	
}