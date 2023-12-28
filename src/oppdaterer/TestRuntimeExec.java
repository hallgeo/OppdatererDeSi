/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oppdaterer;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DeskSim22
 */
public class TestRuntimeExec
{
  public static void main(String[] args)
  {
    System.out.println("test");
    try
    {
      String[] sa = {"dir"}; 
//      Process p = Runtime.getRuntime().exec("java -cp LastNed.jar lastned.TestFrame");      
      Process p = Runtime.getRuntime().exec("cmd /c rename kart2.txt kart3.txt");    
p = Runtime.getRuntime().exec("cmd /c rename kart3.txt kart2.txt");         
    } catch (Exception ex )
    {
      System.out.println("feil");
      Logger.getLogger(TestRuntimeExec.class.getName()).log(Level.SEVERE, null, ex);
    }
    System.out.println("test2");
  }
}
