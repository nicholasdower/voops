/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.awt.Graphics;

public class GIFPreviewPanel extends JLabel implements Runnable
{
  private Thread theThread;
  private int    theDelay;
  private Vector theImages;
  private int    theCurrentImage;

  private int theImageWidth;
  private int theImageHeight;

  private BufferedImage theImage;

  public GIFPreviewPanel( Vector someImages, int delay )
  {
    theDelay        = delay;
    theCurrentImage = 0;
    theImages       = someImages;
    theImageWidth   = (int)( ((BufferedImage)theImages.get(0)).getWidth() /2 );
    theImageHeight  = (int)( ((BufferedImage)theImages.get(0)).getHeight()/2 );
  }

  public void stop()
  {
    theThread = null;
  }

  public void start()
  {
    if(theThread == null)
    {
      theThread = new Thread(this, "GIF Preview");
      theThread.start();
    }
  }
 
  public void paint( Graphics g )
  {
    g.setColor(getBackground());
    g.fillRect(0,0,getWidth(),getHeight());
    g.drawImage(theImage,(int)(getWidth()/2)-theImageWidth,(int)(getHeight()/2)-theImageHeight,this);
  }

  public void update()
  {
    theImage = (BufferedImage)theImages.get((theCurrentImage++)%theImages.size());
    repaint();
  }

  public void run()
  {
    Thread myThread = Thread.currentThread();
    while( theThread == myThread )
    {
      update();
      try
      {
        Thread.sleep(theDelay);
      }
      catch( InterruptedException ie )
      {
        System.out.println(ie);
      }
    }
  }
}