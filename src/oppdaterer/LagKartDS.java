/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package oppdaterer;

import java.io.File;

/**
 *
 * @author DeskSim6
 */
public class LagKartDS
{
  String s;
  StringBuilder sb;
  public LagKartDS()
  {
    sb = new StringBuilder();
//    itererMappeOld("c:/jmesim/dist");
    itererMappeDenne(".");   
//    itererMappe("c:/jmesim/dist");    
    System.out.println("sb: " + sb.toString());
    TekstfilSkriver.skrivFil("kart1.txt", sb.toString());
  }
  
  
  private void itererMappeDenne(String pathMappe)
  {
    File startFile = new File(pathMappe);   
    File[] alleFiler = startFile.listFiles();
    
    for (File fil: alleFiler)
    {
      int in = fil.getAbsolutePath().indexOf(".\\");
   
      String relativPath = fil.getAbsolutePath().substring(in+2);
      
//      System.out.println("file: " + fil.getAbsolutePath() + " rel: " + relativPath);     
      if (fil.isDirectory())
      {
        itererMappeDenne(fil.getAbsolutePath());
      }
      else
      {
        sb.append("fil," + relativPath + "," + fil.lastModified() + ";\n");        
      }
    }    
  }  
  
  private void itererMappeDist(String pathMappe)
  {
    File startFile = new File(pathMappe);   
    File[] alleFiler = startFile.listFiles();
    
    for (File fil: alleFiler)
    {
//      System.out.println("fil: " + fil.getAbsolutePath());
      int in = fil.getAbsolutePath().indexOf("dist");
      String relativPath = fil.getAbsolutePath().substring(in+5);
      if (fil.isDirectory())
      {
//        System.out.println("DIR: " + fil.getName());
        sb.append("dir," + relativPath + "," + fil.lastModified() + ";\n");
//        System.out.println("dir: " + fil.getName() + " " + fil.lastModified());
        itererMappeDist(fil.getAbsolutePath());
      }
      else
      {
        sb.append("fil," + relativPath + "," + fil.lastModified() + ";\n");        
//        System.out.println("file: " + relativPath + " " + fil.lastModified());
      }
    }    
  }
  
  public static void main(String[] args)
  {
    new LagKartDS();
  }
}
