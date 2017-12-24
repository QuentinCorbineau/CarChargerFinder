import java.io.BufferedReader;

//import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ReadCSVFile_BufferedReader {

	//Delimiters used in the CSV file
    private static final String COMMA_DELIMITER = ",";
    
    public static void putDataInList(List<Borne> borneList)
    {
        BufferedReader br = null;
        try
        {
        	URL scoresPage = new URL("http://opendata.regionpaca.fr/donnees.html?type=109&no_cache=1&tx_ausyopendata_pi1%5Bdataset%5D=1395&tx_ausyopendata_pi1%5Bdatafile%5D=2527&tx_ausyopendata_pi1%5Baction%5D=download&tx_ausyopendata_pi1%5Bcontroller%5D=Dataset&cHash=acde6117d7c09e68fb9ef40629ea1015");
			InputStream is = scoresPage.openStream() ;
			BufferedReader in = new BufferedReader( new InputStreamReader(is));
			String inputLine;
					
            //Read to skip the header
            in.readLine();
            
        	while ((inputLine = in.readLine()) != null){
				String[] borneDetails = inputLine.split(COMMA_DELIMITER);
				
                String x=new String();
                String y=new String();
                if(borneDetails.length > 0 )
                {
                	//Save the borne details in Borne object
                	y=borneDetails[7];
                	x=borneDetails[8];
                    
                	if(x.equals("")||x.charAt(0)!='4'){
                		x=borneDetails[10];
                		y=borneDetails[9];
                	}
                	Borne borne = new Borne(y,x,2,4);
                    borneList.add(borne);
                }
			}
			in.close();
			}
			catch (MalformedURLException e) {
			System.out.println("Prb ds l ’URL ");}
			catch(IOException e)
        	{
				System.out.println("Prb lors de la fermeture du BufferedReader");
				e.printStackTrace();
        	}
			catch (Exception e) {
			System.out.println(" Prb lors de la lecture ");}
    }
}
