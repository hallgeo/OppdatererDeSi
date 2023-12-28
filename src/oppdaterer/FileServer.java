/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oppdaterer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DeskSim22
 */
public class FileServer
{
  public FileServer()
  {
    byte[] ba = {12,34,15};
    boolean run = true;
    int numForb = 0;
    
    try
    {
      System.out.println("Starter filserver for DeskSim oppdateringer");
      ServerSocket serverSocket = new ServerSocket(5079);
      while (run)
      {
        Socket socket = serverSocket.accept();
        ForbindelseHandterer fh = new ForbindelseHandterer(socket, numForb);
        fh.start();
        numForb++;
      }
//      System.out.println("accepted");
//      OutputStream ostr = socket.getOutputStream();   
//      InputStream istr = socket.getInputStream();
//      istr.read();
//      
//      ostr.write(ba, 0, ba.length);
//              
      serverSocket.close();
      System.out.println("ferdig");
      

      
      
    } catch (IOException ex)
    {
      Logger.getLogger(FileServer.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  private class ForbindelseHandterer extends Thread
  {
    private Socket socket;
    private int num;
    
    public ForbindelseHandterer(Socket socket, int num)
    {
      this.socket = socket;
      this.num = num;
    }
    
    @Override
    public void run()
    {
      System.out.println("Forbindelsetråd starter: " + num);
      InputStream istr;
      OutputStream ostr;
      try
      {
        istr = socket.getInputStream();
        String fil = mottaFilInfo(istr);
        ostr = socket.getOutputStream();   
        sendFil(ostr, /*"e:/DeskSimFiles/" + */fil);
        ostr.close();
        socket.close();
      } catch (IOException ex)
      {
        Logger.getLogger(FileServer.class.getName()).log(Level.SEVERE, null, ex);
      }
      System.out.println("Forbindelsetråd stopper: " + num);
    }
  }
  
  private String mottaFilInfo(InputStream istr) throws IOException 
  {
    boolean run = true;
    byte[] ba = new byte[1024];
    StringBuilder sb = new StringBuilder();
    int i = istr.read(ba, 0, ba.length);
    String s = new String(ba, 0, i, StandardCharsets.UTF_8);     
    sb.append(s);
    System.out.println("mottar: " + i + " string: " + sb.toString());
    return sb.toString();
  }
  
  public void sendFil(OutputStream ostr, String fil)
  {   
    byte[] ba = new byte[1024];
    try
    {
      String startBane = "e:/desksimfiles/";
      
      FileInputStream fistr = new FileInputStream(startBane + fil);

      boolean kjor = true;
      System.out.println("starter å sende: " + fil);
      while (kjor)
      {
        int i = fistr.read(ba, 0, ba.length);
        
        if (i < ba.length)
        {
          for (int k = i; k < ba.length; k++)
          {
            ba[k] = -1;
          }
          kjor = false;
        }
//        
//        String s = new String(ba, StandardCharsets.UTF_8);
//        System.out.println("s: " + s);
        
        if (i < ba.length) // siste
        {
          ostr.write(ba, 0, i);
        }
        else // normal
        {
          ostr.write(ba, 0, ba.length);
        }
        
      }
//      fistr.read(ba, 0, ba.length);
     
      
      fistr.close();
      System.out.println("ferdig sendfil");
      
      // TODO code application logic here
    } 
    catch (Exception ex)
    {
      System.out.println("File not found: " + fil); 
//      Logger.getLogger(FileTest1.class.getName()).log(Level.SEVERE, null, ex);
    }
        
  }
  
  public static void main(String[] args)
  {
    new FileServer();
  }
}
