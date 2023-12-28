package oppdaterer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
//import java.io.OutputStreamWriter;
//import java.io.UnsupportedEncodingException;
import java.net.Socket;
//import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
//import org.apache.hc.client5.http.classic.HttpClient;
//import org.apache.hc.client5.http.classic.methods.HttpGet;
//import org.apache.hc.client5.http.impl.classic.HttpClients;
//import org.apache.hc.core5.http.ClassicHttpResponse;

/**
 *
 * @author DeskSim6
 */
public class AnalyserKart1og2
{

  String kartMin;
  String kartNy;
  ArrayList<String> slettesL;
  ArrayList<String> leggesTilL;
  ArrayList<String> modL;

  private OppdaterFrame oppdaterFrame;
  String utelatMappe = "lib\\";
  boolean assetsSkalOppdateres = true;
  boolean modHarAssets = false;
  public static String startMappe = "DeskSimOppdtTest/"; // installasjonsmappe, i prod normalt tom fordi man er inni dist allerede
  public static String destMappe = "dist/"; // distmappe i installasjonsmappe, i prod normalt tom fordi man er inni dist allerede

  public AnalyserKart1og2(OppdaterFrame oppdaterFrame)
  {
    this.oppdaterFrame = oppdaterFrame;
    lastNedKart();
    analyser();
  }

  private void lastNedKart()
  {
    System.out.println("Hent kart");
//    lastNedKopierFil("kart2.txt", "kart2.txt");
    lastNedFilNy("kart2.txt", startMappe + "kart2.txt");
  }
  
  private void analyser()
  {
    slettesL = new ArrayList<>();
    leggesTilL = new ArrayList<>();
    modL = new ArrayList<>();

    kartMin = TekstfilLeser.lesFil(startMappe + "kart1.txt");
    String[] saMin = kartMin.split(";");

    String kartNa = TekstfilLeser.lesFil(startMappe + "kart2.txt");
    String[] saNy = kartNa.split(";");
    
//    for (String s: saNy)
//    {
//      System.out.println(s);
//    }
//    System.exit(0);

//    int max = Math.max(saMin.length, saNy.length);
//    int i = 0;
    int iMin = 0;
    int iNy = 0;

//    max = 10; // temp verdi
    while (iMin < saMin.length && iNy < saNy.length)
    {
      // min
      String sMin = saMin[iMin];
      String[] SMinArray = sMin.split(",");

      // ny
      String sNy = saNy[iNy];
      String[] SNyArray = sNy.split(",");

      System.out.println("sMin: " + SMinArray[1] + " sNy: " + SNyArray[1] + " iMin: " + iMin + " iNy: " + iNy);

      if (!SMinArray[1].equals(SNyArray[1])) // avvik
      {
        // let etter fil i ny
        boolean minFilFinnesINy = sokFilNavn(saNy, SMinArray[1], iNy);
        if (minFilFinnesINy) // fil finnes fortsatt i ny, da er denne en ny fil som er lagt til
        {
          System.out.println("Avvik funnet: fil lagt til, hopp til neste i ny");
          leggesTilL.add(SNyArray[1]);
          iNy++;
        }
        else // fil finnes ikke i ny, da er denne slettet
        {
          System.out.println("Avvik funnet: fil slettet, hopp til neste min");
          slettesL.add(SMinArray[1]);
          iMin++;
        }
      }
      else // navn er like - sjekk endringstidspunkt
      {
        if (!SMinArray[2].equals(SNyArray[2]))
        {
          System.out.println("Endring funnet: fil modifiseres, iterer");
          modL.add(SMinArray[1]);
        }
        iMin++;
        iNy++;
      }
//      i++;
    }

    // en av listene er ferdig
    while (iMin < saMin.length)
    {
      // min
      String sMin = saMin[iMin];
      String[] SMinArray = sMin.split(",");

      System.out.println("Elemtenter gjenstår når nyListe er ferdig: fil slettet, hopp til neste min");
      slettesL.add(SMinArray[1]);

      iMin++;
    }

    while (iNy < saNy.length)
    {
      // ny
      String sNy = saNy[iNy];
      String[] SNyArray = sNy.split(",");
      System.out.println("Nytt element etter at minliste er ferdig: fil lagt til, hopp til neste i ny");
      leggesTilL.add(SNyArray[1]);

      iNy++;
    }
  }
  
  public boolean utfor()
  {
    System.out.println("Analyse ferdig, utfører endringer");
    boolean altGjfort = true;
    for (String sSle : slettesL)
    {
      if (sSle.substring(0, utelatMappe.length()).equals(utelatMappe))
      {
        System.out.println("UTELAT");
      }
      System.out.println("SLETT: " + sSle);
      skrivTilTA("sletter: " + sSle);
      
      slettFil(sSle);
    }

    for (String sLeg : leggesTilL)
    {
      System.out.println("Starter nedlasting NY fil: " + sLeg);
      skrivTilTA("legg til starter nedlasting: " + sLeg);      
//      simulerLastNedKopierFil(sLeg, sLeg);
//      lastNedKopierFil("dist/" + sLeg, /*"dist/"*/destMappe + sLeg);
      boolean b = lastNedFilNy("dist/" + sLeg, startMappe + /*"dist/"*/destMappe + sLeg);
      if (!b)
      {
        skrivTilTA("filnedlasting ikke fullfort, fil kanskje ikke funnet på server: " + sLeg);  
        altGjfort = false;
      }      
    }

    for (String sLeg : modL)
    {
      if (sLeg.substring(0, utelatMappe.length()).equals(utelatMappe))
      {
        System.out.println("utelat: " + sLeg);
      }
      else
      {
        System.out.println("Slett for MOD: " + sLeg);
        skrivTilTA("slett for mod: " + sLeg);          
        slettFil(sLeg);
      }
      
      if (sLeg.contains("assets.jar"))
      {
        modHarAssets = true;
      }
    }

    for (String sLeg : modL)
    {
      if (sLeg.substring(0, utelatMappe.length()).equals(utelatMappe))
      {
        System.out.println("utelat: " + sLeg);
      }
      else
      {
        System.out.println("Starter nedlasting for MOD: " + sLeg);
        skrivTilTA("legg til for mod starter nedlasting: " + sLeg);         
        //      simulerLastNedKopierFil(sLeg, sLeg);
//        lastNedKopierFil("dist/" + sLeg, /*"dist/"*/destMappe + sLeg);
        boolean b = lastNedFilNy("dist/" + sLeg, startMappe +/*"dist/"*/destMappe + sLeg);
        if (!b)
        {
          skrivTilTA("filnedlasting ikke fullfort, fil kanskje ikke funnet på server: " + sLeg); 
          altGjfort = false;
        }
      }
    }

    // assets
    if (assetsSkalOppdateres && modHarAssets)
    {
      System.out.println("Sletter assets for mod");
      skrivTilTA("sletter assets for mod");      
      slettFil(/*"dist/lib/assets.jar"*/destMappe + "lib/assets.jar");

      System.out.println("Laster ned assets for mod");
      skrivTilTA("laster ned assets for mod - OBS nedlasting av assets tar tid, ofte flere minutter, VENT");         
//      lastNedKopierFil("dist/lib/assets.jar", /*"dist/lib/assets.jar"*/destMappe + "lib/assets.jar");
      boolean b = lastNedFilNy("dist/lib/assets.jar", startMappe + /*"dist/lib/assets.jar"*/destMappe + "lib/assets.jar");
      if (!b)
      {
        skrivTilTA("filnedlasting ikke fullfort, fil kanskje ikke funnet på server: "); 
        altGjfort = false;
      }      
    }
    
    // ferdig med seltting og nedlastinger
    System.out.println("Endringer ferdig");
    if (altGjfort)
    {
      System.out.println("Alle nedlastinger gjennomført");  
      File f1 = new File(startMappe + "kart1.txt");
      File f2 = new File(startMappe + "kart2.txt"); 
      System.out.println("kart1: " + f1.exists() + " kart2: " + f2.exists());
      boolean del = f1.delete();
      boolean ren = f2.renameTo(f1);
      System.out.println("kart1 slettet: " + del + " kart2 endret navn til kart1: " + ren);  
     
//    try
//    {      
//      // bytt kart fil
//      Process p = null;
//      p = Runtime.getRuntime().exec("cmd /c del kart1.txt");       
//      p = Runtime.getRuntime().exec("cmd /c rename kart2.txt kart1.txt"); 
//      System.out.println("Endrer navn kart2 og kart1");
//    } catch (IOException ex)
//    {
//      Logger.getLogger(AnalyserKart1og2.class.getName()).log(Level.SEVERE, null, ex);
//    }
    }
    else
    {
      System.out.println("Feil ved nedlastinger underveis, versjonskart ikke oppdatert");
    }
    return true;
  }

  private boolean sokFilNavn(String[] sokIA, String filnavn, int startIndex)
  {
    for (int i = startIndex; i < sokIA.length; i++)
    {
      String[] sa1 = sokIA[i].split(",");
//      if (sa1[0].equals("dir"))
//      {
//        break;
//      }

      if (sa1[1].equals(filnavn))
      {
        return true;
      }
    }

    return false;
  }
  
  private void skrivTilTA(String s)
  {
    if (oppdaterFrame != null)
    {
      oppdaterFrame.leggTilTA(s + "\n");
    }    
  }
  
  private void skrivTilTASammeLinje(String s)
  {
    if (oppdaterFrame != null)
    {
      oppdaterFrame.leggTilTA(s);
    }    
  }  

  private void simulerLastNedKopierFil(String filSrc, String filDst)
  {
    Path src = Paths.get("c:/jmesim/dist/" + filSrc);
    Path dst = Paths.get(startMappe + /*"dist/"*/destMappe + filDst);

    try
    {
      Files.createDirectories(dst.getParent());
      Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException ex)
    {
      Logger.getLogger(AnalyserKart1og2.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

//  private void lastNedKopierFil(String filSrc, String filDst)
//  {
//    String encodedFilePath = null;
//
//    try
//    {
//      encodedFilePath = URLEncoder.encode(filSrc, StandardCharsets.UTF_8.toString());
//    } catch (UnsupportedEncodingException ex)
//    {
//      Logger.getLogger(FileDownloader.class.getName()).log(Level.SEVERE, null, ex);
//    }
//
////    String apiUrl = "http://52.169.201.122:8080/get-file?filePath=" + encodedFilePath;
//    String apiUrl = "https://desksim.lokforerskolen.no/api/file/GetFile?filePath=" + encodedFilePath;
//    
////    String apiUrl = "http://52.169.201.122:8080/get-file?filePath=" + filSrc;//A_Scenarios%2Fsc_atc_DATC_til_FATC_bkj.xml";
//
//    apiUrl = apiUrl.replaceAll("\\\\", "/");
//    // Specify the local file path where you want to save the downloaded file
////    String localFilePath = "DeskSimMin\\dist\\" + filDst;
//    String localFilePath = startMappe + filDst;    
//
//    //
////    Path dst = Paths.get("DeskSimMin/" + filDst);
//    Path dst = Paths.get(localFilePath);      
//    if (localFilePath.contains("/")) // har med mappe
//    {
//      try
//      {
//        Files.createDirectories(dst.getParent());
//        //
//      } catch (IOException ex)
//      {
//        Logger.getLogger(AnalyserKart1og2.class.getName()).log(Level.SEVERE, null, ex);
//      }
//    }
//
//    HttpClient httpClient = HttpClients.createDefault();
//    HttpGet httpGet = new HttpGet(apiUrl);
//    try (ClassicHttpResponse response = (ClassicHttpResponse) httpClient.execute(httpGet))
//    {
//      if (response.getCode() == 200)
//      {
//        try (InputStream inputStream = response.getEntity().getContent(); OutputStream outputStream = new FileOutputStream(localFilePath))
//        { 
//          byte[] buffer = new byte[1024];
//          int bytesRead;
//          int lastNedTeller = 0;
//          int linjer = 0;
//          while ((bytesRead = inputStream.read(buffer)) != -1)
//          {
//            outputStream.write(buffer, 0, bytesRead);
//            if (lastNedTeller % 2000 == 0)
//            {
//              System.out.print("*");
//              skrivTilTASammeLinje("*");
//              if (linjer == 100)
//              {
//                System.out.println("");
//                skrivTilTA("");     
//                linjer = 0;
//              }
//              linjer++;
//            }
//            lastNedTeller++;
//          }
//          System.out.println("Fil lastet ned: " + localFilePath);
//          skrivTilTA("");
//        }
//      }
//      else
//      {
//        System.err.println("HTTP error! Status: " + response.getCode());
//      }
//
//    } catch (IOException e)
//    {
//      e.printStackTrace();
//    }
//  }
  
  public boolean lastNedFilNy(String filInn, String filUt)
  {
    byte[] ba = new byte[1024];
    boolean run = true;
    int k = 0;
    boolean nedlastingOk = true;
    try
    {
      Socket socket = new Socket("52.169.201.122", 5079);

//      Socket socket = new Socket("localhost", 5079);
      InputStream istr = socket.getInputStream();
      OutputStream ostr = socket.getOutputStream();
      
      // endre skråstrek i filbane
      filInn = filInn.replaceAll("\\\\", "/");
      
      // opprett mappe dersom ikke finnes
      Path dst = Paths.get(filUt);      
      if (filUt.contains("/")) // har med mappe
      {
        try
        {
          Files.createDirectories(dst.getParent());
          //
        } catch (IOException ex)
        {
          Logger.getLogger(AnalyserKart1og2.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      
      FileOutputStream fostr = new FileOutputStream(filUt); 

      // filinfo
      String filNavn = filInn;
      byte[] filA = filNavn.getBytes(StandardCharsets.UTF_8); 
      ostr.write(filA);

      while (run)
      {
        int i = istr.read(ba, 0, ba.length);
        if (i == -1)
        {
          run  = false;
          if (k == 0)
          {
            nedlastingOk = false;
            System.out.println("Ingen bytes lest, sannsynligvis file not found på server");
          }
        }
//        System.out.println("I: " + i);
        if (run)
        {
//          for (byte b: ba)
//          {
//            System.out.println("b: " + b + " i: " + i);
//          }
//          String s = new String(ba,StandardCharsets.UTF_8);
//          System.out.println("s: " + s);

          if (i < ba.length) // siste
          {
            fostr.write(ba, 0, i);
          }
          else // normal
          {
            fostr.write(ba, 0, ba.length);
          }         
        }
        
        if (k % 1000 == 0)
        {
          System.out.println("k: " + k);
        }
        
        k++;
      }   
      System.out.println("ferdig");
      
//      for (byte b: ba)
//      {
//        System.out.println("b: " + b);
//      }
      
      socket.close();
      fostr.close();
      istr.close();
      ostr.close();
    } 
    catch (IOException ex)
    {
      Logger.getLogger(AnalyserKart1og2.class.getName()).log(Level.SEVERE, null, ex);
      nedlastingOk = false;
    }
    
    return nedlastingOk;
  }  

  private void slettFil(String fil)
  {
    File f = new File(startMappe + /*"dist/"*/destMappe + fil);           //file to be delete  
    f.delete();
  }

  public static void main(String[] args)
  {
    new AnalyserKart1og2(null);
  }
}
