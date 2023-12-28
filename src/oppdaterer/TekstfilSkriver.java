
package oppdaterer;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;


public class TekstfilSkriver 
{
    public static void skrivFil(String filename, String text) 
    {
        
      BufferedWriter bufferedWriter = null;
      byte[] bytes = new byte[1024];
      Writer fstream;
      try 
      {
          fstream = new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8);
          bufferedWriter = new BufferedWriter(/*new FileWriter(filename)*/fstream);
          bufferedWriter.write(text);         
      } 
      catch (FileNotFoundException ex) 
      {
          ex.printStackTrace();
      } 
      catch (IOException ex) 
      {
          ex.printStackTrace();
      } 
      finally 
      {
        try 
        {
            if (bufferedWriter != null) 
            {
                bufferedWriter.flush();
                bufferedWriter.close();
            }
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
        }
      }
    }

}