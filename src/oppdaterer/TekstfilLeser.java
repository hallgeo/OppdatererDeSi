
package oppdaterer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class TekstfilLeser
{

  public static String lesFil(String filnavn)
  {
    StringBuilder sb = new StringBuilder();

    BufferedReader inntekst; //tekstfila som skal leses

    try
    { //åpner fila som skal leses
//      inntekst = new BufferedReader(new FileReader(filnavn));
        inntekst = new BufferedReader(new InputStreamReader(new FileInputStream(filnavn), StandardCharsets.UTF_8));      
    } catch (FileNotFoundException e)
    {
      System.out.println("Tekstfilleser: Finner ikke fil " + filnavn);
      return null;
    }

    try
    {
      //leser tegn inntil filslutt
      String innlinje = null;
      do
      {
        innlinje = inntekst.readLine(); //leser en linje
        if (innlinje != null) //null betyr filslutt
        {
          sb.append(innlinje);
        }
      } while (innlinje != null);

      //Alt er lest. Lukker fila.
      inntekst.close();

      return sb.toString();
    } catch (IOException ioe)
    {
      return null;
    }
  }

  public static void copyFile(File source, File dest) throws IOException
  {
    InputStream is = null;
    OutputStream os = null;
    try
    {
      is = new FileInputStream(source);
      os = new FileOutputStream(dest);
      byte[] buffer = new byte[1024];
      int length;
      while ((length = is.read(buffer)) > 0)
      {
        os.write(buffer, 0, length);
      }
    } finally
    {
      is.close();
      os.close();
    }
  }
}
