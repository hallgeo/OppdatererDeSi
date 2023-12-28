package oppdaterer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class OppdaterFrame extends JFrame
{

  private JPanel panel;
  private JButton startB;
  private JButton startDeskSimB;  
  private JButton avsluttB;    
  private JTextArea tekstTA;

  public static void main(String[] args)
  {
    for (String s:args)
    {
      if (s.equals("iprod=true"))
      {
        AnalyserKart1og2.startMappe = "";
        AnalyserKart1og2.destMappe = "";
      }
      System.out.println("OppdaterFrame argument: " + s);
    }
//    AnalyserKart1og2.startMappe = "";
    OppdaterFrame a = new OppdaterFrame();
    System.out.println("OppdaterFrame kjører");
    
    // start auto ved oppstart av program
    a.startTrad();
  }

  public OppdaterFrame()
  {
    super("DeskSim-oppdaterer");
    panel = new JPanel();
    panel.setLayout(null);
    add(panel);

    setLocation(100, 100);
    setSize(750, 400);

    KnappLytter kl = new KnappLytter();
    startB = new JButton("Søk oppdateringer");
    startB.setBounds(10, 10, 160, 40);
    startB.addActionListener(kl);
    panel.add(startB);
    startB.setVisible(false);
    
    startDeskSimB = new JButton("Start DeskSim");
    startDeskSimB.setBounds(130, 310, 160, 40);
    startDeskSimB.addActionListener(kl);
    panel.add(startDeskSimB);
    startDeskSimB.setVisible(false);    
    
    avsluttB = new JButton("Avslutt");
    avsluttB.setBounds(10, 310, 100, 40);
    avsluttB.addActionListener(kl);
    panel.add(avsluttB);    
    avsluttB.setVisible(false);       

    tekstTA = new JTextArea();
    JScrollPane scrollp = new JScrollPane(tekstTA);
    scrollp.setBounds(10, 60, 710, 240);
    panel.add(scrollp);

    setVisible(true);
    setDefaultCloseOperation(/*JFrame.DO_NOTHING_ON_CLOSE*/JFrame.EXIT_ON_CLOSE);
    setAlwaysOnTop(true);
  }

  private void startOppdt()
  {
    tekstTA.append("Starter oppdatering\n");    
    AnalyserKart1og2 analyserKart1og2 = new AnalyserKart1og2(this);
    analyserKart1og2.utfor();
    tekstTA.append("oppdatering FERDIG - velg Avslutt, steng dos / kommandovindu og start DeskSim på nytt\n");   
//    startDeskSimB.setVisible(true); // velger manuell restart av DeskSim etter oppdatering
    avsluttB.setVisible(true);
  }

  public void leggTilTA(String s)
  {
    tekstTA.append(s);
    tekstTA.setCaretPosition(tekstTA.getDocument().getLength());    
  }
  
  public void startTrad()
  {
    OppdtThread oppdtThread = new OppdtThread();
    oppdtThread.start();    
  }
  
  private class OppdtThread extends Thread
  {
    @Override
    public void run()
    {
      startOppdt();
    }
  }

  private class KnappLytter implements ActionListener
  {

    @Override
    public void actionPerformed(ActionEvent ae)
    {
      if (ae.getSource() == startB)
      {
        startTrad();
      }
      else if (ae.getSource() == startDeskSimB)
      {
        // velger manuell restart av DeskSim etter oppdatering
      }
      else if (ae.getSource() == avsluttB)
      {
        System.exit(0);
      }      
    }

  }

}
