/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.util.Vector;

public class HSChooser extends JPanel
{
  private BufferedImage theImage;
  private Color         theColor = Color.black;
  private Point         theLocation;

  private Vector theChangeListeners;

  public HSChooser()
  {
    this.addMouseListener
    (
      new MouseAdapter()
      {
        public void mousePressed( MouseEvent e )
        {
          setPointerLocation(e.getX(),e.getY());
          notifyListeners();
        }
      }
    );
    this.addMouseMotionListener
    (
      new MouseMotionAdapter()
      {
        public void mouseDragged( MouseEvent e )
        {
          setPointerLocation(e.getX(),e.getY());
          notifyListeners();
        }
      }
    );

    theChangeListeners = new Vector();
  }

  private void updateImage()
  {
    theImage = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_RGB);
    float hStep = 359f/((float)getHeight());
    float sStep = 100f/((float)getWidth());

    for( int i = 0 ; i < getWidth() ; i++ )
    {
      for( int j = 0 ; j < getHeight() ; j++ )
      {
        theImage.setRGB(i,(getHeight()-1)-j,HSLRGBConverter.HSLtoRGB(((float)j)*hStep,((float)i)*sStep,50));
      }
    }

    setColor(theColor);
  }

  public void paint( Graphics g )
  {
    if( theImage == null || theImage.getWidth() != getWidth() || theImage.getHeight() != getHeight() )
      updateImage();

    g.drawImage(theImage,0,0,this);

    if( theLocation == null )
      return;
    g.setColor(Color.black);
    g.fillRect(theLocation.x-1,theLocation.y-6,3,5);
    g.fillRect(theLocation.x-6,theLocation.y-1,5,3);
    g.fillRect(theLocation.x+2,theLocation.y-1,5,3);
    g.fillRect(theLocation.x-1,theLocation.y+2,3,5);
  }

  public void setColor( Color aColor )
  {
    theColor = aColor;
    if( theImage == null )
      return;

    float[] hsl = HSLRGBConverter.RGBtoHSL(aColor.getRGB());

    float hStep = 359f/((float)getHeight());
    float sStep = 100f/((float)getWidth());

    if( hsl[0] == 0 )
      setPointerLocation((int)(hsl[1]/sStep),getHeight());
    else if( hsl[0] == 359 )
      setPointerLocation((int)(hsl[1]/sStep),0);
    else
      setPointerLocation((int)(hsl[1]/sStep),getHeight()-(int)(hsl[0]/hStep));
  }

  public void setHS( float[] anHS)
  {
    int x = (int)((anHS[1]/100f)*((float)getWidth()));
    int y = (int)((anHS[0]/359f)*((float)getHeight()));
    setPointerLocation(x,y);
  }

  public float[] getHS()
  {
    float h = (  ((float)theLocation.y)/((float)getHeight())  ) * 359;
          h = theLocation.y == getHeight()-1 ? 359 : h;
    float s = (  ((float)theLocation.x)/((float)getWidth())  ) * 100;
          s = theLocation.x == getWidth()-1 ? 100 : s;
    return new float[]{359-h,s};
  }

  public Color getColor()
  {
    return theColor;
  }

  private void setPointerLocation( int x, int y )
  {
    if( theImage == null )
      return;

    if( y >= getHeight() )
      y = getHeight()-1;
    if( y < 0 )
      y = 0;

    if( x >= getWidth() )
      x = getWidth()-1;
    if( x < 0 )
      x = 0;

    Color color = new Color(theImage.getRGB(x,y));
    theLocation = new Point(x,y);
    theColor = color;
    repaint();
  }

  public void addChangeListener( ChangeListener aListener )
  {
    theChangeListeners.add(aListener);
  }

  public void removeChangeListener( ChangeListener aListener )
  {
    theChangeListeners.remove(aListener);
  }

  private void notifyListeners()
  {
    for( int i = 0 ; i < theChangeListeners.size() ; i++ )
    {
      ((ChangeListener)theChangeListeners.get(i)).stateChanged(new ChangeEvent(this));
    }
  }
}