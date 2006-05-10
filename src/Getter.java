/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.awt.Color;
import java.awt.Frame;
import java.awt.Dialog;
import java.awt.Component;
import java.awt.Container;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.Robot;
import java.awt.Point;
import java.awt.MouseInfo;
import java.awt.event.InputEvent;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.awt.Graphics;

public class Getter extends JDialog implements MouseListener,MouseMotionListener
{
  public final static int ZOOM_TWO       = 2;
  public final static int ZOOM_FOUR      = 4;
  public final static int ZOOM_EIGHT     = 8;
  public final static int ZOOM_SIXTEEN   = 16;
  public final static int ZOOM_THIRTYTWO = 32;

  private static Color theInnerColor = new Color(255,255,255,100);
  private static Color theOuterColor = new Color(0,0,0,100);

  private Color theColor;
  private boolean theIsExiting  = false;
  private boolean theIsEntering = false;

  private int theWidth;
  private int theHeight;

  private int theImageWidth;
  private int theImageHeight;

  private Robot theRobot;
  private Point theLocation;

  private Image         theImage;
  private BufferedImage thePaintImage;
  private Graphics      theGraphics;

  private Rectangle thePointer;
  
  private int theZoom;

  public Getter( Frame aFrame )
  {
    this( aFrame, ZOOM_EIGHT, 200, 200 );
  }

  public Getter( Frame aFrame, int aZoom )
  {
    this( aFrame, aZoom, 200, 200 );
  }

  public Getter( Frame aFrame, int aZoom, int aWidth, int aHeight )
  {
    super(aFrame, "Getter", true);
    if( aZoom != ZOOM_TWO && aZoom != ZOOM_FOUR && aZoom != ZOOM_EIGHT && aZoom != ZOOM_SIXTEEN && aZoom != ZOOM_THIRTYTWO )
      theZoom = ZOOM_EIGHT;
    else
      theZoom = aZoom;

    theWidth  = aWidth;
    theHeight = aHeight;

    if( theWidth != theHeight )
    {
      theWidth = Math.max(theWidth,theHeight);
      theHeight = theWidth;
    }

    if( theWidth % theZoom != 0 )
    {
      theWidth -= theWidth % theZoom; 
      theHeight = theWidth;
    }

    theImageWidth  = (int)(theWidth/theZoom);
    theImageHeight = (int)(theHeight/theZoom);
 
    thePaintImage = new BufferedImage(theWidth,theHeight,BufferedImage.TYPE_INT_ARGB);
    theGraphics = thePaintImage.getGraphics();

    thePointer = new Rectangle(  (((int)(theWidth/2)) - ((int)theZoom/2)), (((int)(theHeight/2)) - ((int)theZoom/2)), theZoom, theZoom );

    this.addWindowListener
    (
      new WindowAdapter()
      {
        public void windowOpened( WindowEvent e )
        {
          startGetter();
        }
      }
    );

    this.addMouseListener(this);
    this.addMouseMotionListener(this);
  }

  public void paint( Graphics g )
  {
    if( theImage != null )
    {
      theGraphics.drawImage(theImage,0,0,null);

      theGraphics.setColor(theOuterColor);
      theGraphics.drawRect(thePointer.x,thePointer.y,thePointer.width,thePointer.height);

      theGraphics.setColor(theInnerColor);
      theGraphics.drawRect(thePointer.x+1,thePointer.y+1,thePointer.width-2,thePointer.height-2);

      g.drawImage(thePaintImage,getInsets().left+1,getInsets().top,null);
    }
    else
    {
      g.setColor(getBackground());
      g.fillRect(0,0,getWidth(),getHeight());
    }
  }

  public Color showGetDialog( Component aParent, Point currentLocation )
  {
    theLocation = currentLocation;
    try
    {
      theRobot = new Robot();
    }
    catch( Exception e )
    {
      return null;
    }
    
    theColor = Color.red;

    this.pack();
    this.setSize(theWidth+getInsets().left + getInsets().right,theHeight + getInsets().top + getInsets().bottom);
    this.setLocationRelativeTo(aParent);
    this.show();
    this.dispose();
    return theColor;
  }

  public Color getColor()
  {
    return theColor;
  }

  private void startGetter()
  {
    Point thisPoint            = this.getLocationOnScreen();

    theRobot.mouseMove(thisPoint.x + 5, thisPoint.y + 30);
    theRobot.mousePress(InputEvent.BUTTON1_MASK);
    theRobot.mouseMove(theLocation.x,theLocation.y);
  }

  public void mousePressed(  MouseEvent e ){}
  public void mouseReleased( MouseEvent e )
  {
    theColor = theRobot.getPixelColor(this.getLocationOnScreen().x + e.getX(),this.getLocationOnScreen().y + e.getY());
    this.setVisible(false);
  }
  public void mouseEntered(  MouseEvent e ){}
  public void mouseExited(   MouseEvent e ){}
  public void mouseClicked(  MouseEvent e ){}

  public void mouseDragged( MouseEvent e )
  {
    Point currentMouseLocation = getLocationOnScreen();
    currentMouseLocation.x += e.getX();
    currentMouseLocation.y += e.getY();
    int x = currentMouseLocation.x-(int)(theImageWidth/2);
    int y = currentMouseLocation.y-(int)(theImageHeight/2);

    theImage = (theRobot.createScreenCapture(new Rectangle(x,y,theImageWidth,theImageHeight))).getScaledInstance(theWidth,theHeight,Image.SCALE_FAST);
    repaint();
  }
  public void mouseMoved( MouseEvent e ){}
}