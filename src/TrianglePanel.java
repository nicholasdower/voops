/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;

public class TrianglePanel extends JPanel
{
  private int theY = -1;
  private float theLum = 1;
  private int theHeight;

  public TrianglePanel()
  {
    this.addComponentListener
    (
      new ComponentListener()
      {
        public void componentResized(ComponentEvent e)
        {
          setLuminance(getLuminance());
          theHeight = getHeight();
        }
        public void componentShown(ComponentEvent e){}
        public void componentMoved(ComponentEvent e){}
        public void componentHidden(ComponentEvent e){}
      }
    );
  }

  public void paint( Graphics g )
  {
    if( theY < 0 )
      setLuminance(theLum);
    g.setColor(getBackground());
    g.fillRect(0,0,getWidth(),getHeight());
    g.setColor(Color.black);
    g.fillRect(0,theY-1,1,1);
    g.fillRect(1,theY-2,1,3);
    g.fillRect(2,theY-3,1,5);
    g.fillRect(3,theY-4,1,7);
  }

  public void setPointer( int y )
  {
    if( y < 4 )
      theY = 4;
    else if( y > getHeight()-3 )
      theY = getHeight()-3;
    else
      theY = y;

    theLum = ((float)(theY-4))/((float)(theHeight-7));

    repaint();
  }

  public float getLuminance()
  {
    return 1f-theLum;
  }

  public void setLuminance( float aLum )
  {
    aLum = 1-aLum;
    theLum = aLum;
    theY = (int)(aLum*((float)(getHeight()-7))) + 4;
    repaint();
  }

  public void setColor( Color aColor )
  {
    setLuminance(HSLRGBConverter.RGBtoHSL(aColor.getRGB())[2]/100f);
  }
}