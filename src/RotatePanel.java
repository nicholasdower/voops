/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import javax.swing.JPanel;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.RenderingHints;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.Vector;

public class RotatePanel extends JPanel
{
  private final static double PI       = 3.141592;
  private final static double ANG90    = PI/2;
  private final static double ANG180   = PI;
  private final static double ANG360   = 2*PI;
  private final static double ANG270   = (PI/2)*3;

  private double           theRotation = 0;
  private double           theCurrentDirection = 0;
  private Point2D.Double   theCenter;
  private Point2D.Double   thePoint;
  private double           theSize;
  private Ellipse2D.Double theCircle;
  private Arc2D.Double     theArc;
  private Ellipse2D.Double theInnerCircle;
  private GeneralPath      theSpinner;

  private double theArcStart     = 0;
  private double theArcExtent    = 0;
  private int    theArcDirection = 0;

  private BufferedImage theImage;
  private Graphics2D    theGraphics;

  private BasicStroke theStroke;

  private Vector theListeners;

  private boolean isResizing = false;

  public RotatePanel()
  {
    this.addMouseMotionListener
    (
      new MouseMotionAdapter()
      {
        public void mouseDragged( MouseEvent e )
        {
          setRotationFromMouse(e.getX(),e.getY());
        }
      }
    );

    this.addMouseListener
    (
      new MouseAdapter()
      {
        public void mousePressed( MouseEvent e )
        {
          isResizing = true;
          theArcStart = theCurrentDirection;
          setRotationFromMouse(e.getX(),e.getY());
        }

        public void mouseReleased( MouseEvent e )
        {
          theArcDirection = 0;
          isResizing = false;
          notifyListeners();
        }
      }
    );

    theStroke = new BasicStroke(2f);

    theListeners = new Vector();

  }

  private void setUpImage()
  {
    theImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
    RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                   rh.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
    theGraphics = theImage.createGraphics();
    theGraphics.addRenderingHints(rh);

    theCenter  = new Point2D.Double(((double)getWidth())/2, ((double)getHeight())/2);
    theSize    = Math.min(getHeight(),getWidth()) - theStroke.getLineWidth()*2;
    thePoint   = LocationGetter.getLocation(theCenter,theSize/2 - theStroke.getLineWidth(),theCurrentDirection);

    double y = (((double)getHeight())-theSize)/2;
    double x = (((double)getWidth())-theSize)/2;

    theCircle  = new Ellipse2D.Double(x,y,theSize,theSize);

    float spinnerSize = ((float)theSize)/20;

    theInnerCircle    = new Ellipse2D.Double(theCenter.x-(double)spinnerSize*1.5,theCenter.y-(double)spinnerSize*1.5,spinnerSize*3,spinnerSize*3);

    theSpinner = new GeneralPath();
    theSpinner.moveTo((float)theCenter.x,(float)theCenter.y);
    theSpinner.lineTo((float)theCenter.x,(float)theCenter.y-spinnerSize);
    theSpinner.lineTo(((float)theCenter.x+((float)theSize)/2)-spinnerSize*3,(float)theCenter.y-spinnerSize);
    theSpinner.lineTo(((float)theCenter.x+((float)theSize)/2)-spinnerSize*3,(float)theCenter.y-spinnerSize*2);
    theSpinner.lineTo(((float)theCenter.x+(float)theSize/2)-theStroke.getLineWidth(),(float)theCenter.y);
    theSpinner.lineTo(((float)theCenter.x+((float)theSize)/2)-spinnerSize*3,(float)theCenter.y+spinnerSize*2);
    theSpinner.lineTo(((float)theCenter.x+((float)theSize)/2)-spinnerSize*3,(float)theCenter.y+spinnerSize);
    theSpinner.lineTo((float)theCenter.x,(float)theCenter.y+spinnerSize);
    theSpinner.closePath();

    theSpinner = (GeneralPath)AffineTransform.getRotateInstance(-theCurrentDirection,theCenter.x,theCenter.y).createTransformedShape(theSpinner);

    theArc = new Arc2D.Double(x,y,theSize,theSize,Math.toDegrees(theArcStart),theArcExtent,Arc2D.PIE);
  }

  public void paint( Graphics g )
  {
    if( theImage == null || theImage.getWidth() != getWidth() || theImage.getHeight() != getHeight() )
      setUpImage();

    theGraphics.setColor(getBackground());
    theGraphics.fillRect(0,0,getWidth(),getHeight());

    theGraphics.setStroke(theStroke);
    theGraphics.setPaint(Color.white);
    theGraphics.fill(theCircle);
    theGraphics.setPaint(new Color(100,100,255));
    theGraphics.fill(theArc);
    theGraphics.setPaint(Color.red);
    theGraphics.fill(theSpinner);
    theGraphics.setPaint(Color.black);
    theGraphics.draw(theSpinner);
    theGraphics.draw(theCircle);
    theGraphics.setPaint(Color.white);
    theGraphics.fill(theInnerCircle);
    theGraphics.setPaint(Color.black);
    theGraphics.draw(theInnerCircle);

    g.drawImage(theImage,0,0,this);
  }

  private void setRotationFromMouse(double x, double y)
  {
    double oldDirection = theCurrentDirection;

    theCurrentDirection = DirectionGetter.getDirection(theCenter,new Point2D.Double(x,y));
    thePoint   = LocationGetter.getLocation(theCenter,theSize/2,theCurrentDirection);

    theRotation = -(theCurrentDirection-oldDirection);

    theSpinner = (GeneralPath)AffineTransform.getRotateInstance(theRotation,theCenter.x,theCenter.y).createTransformedShape(theSpinner);


    if( theArcDirection == 0 )
    {
      theArcExtent = Math.toDegrees(theCurrentDirection-theArcStart);
      if( theArcExtent%360 == 0 )
      {
        theArc.setAngleStart(Math.toDegrees(theArcStart));
        theArc.setAngleExtent(theArcExtent);
        repaint();
        return;
      }

      if( theArcExtent > 0 && theArcExtent > 180 )
        theArcDirection = -1;
      else if( theArcExtent < 0 && theArcExtent < -180 )
        theArcDirection = 1;
      else if( theArcExtent < 0 )
        theArcDirection = -1;
      else if( theArcExtent > 0 )
        theArcDirection = 1;
    }
    else
    {
      if( oldDirection > theArcStart && theCurrentDirection < theArcStart )
      {
        if( Math.abs(oldDirection-theCurrentDirection) < ANG180 )
        {
          if( theArcDirection == 1 )
            theArcDirection *= -1;
        }
      }
      else if( oldDirection < theArcStart && theCurrentDirection > theArcStart )
      {
        if( Math.abs(oldDirection-theCurrentDirection) < ANG180 )
        {
          if( theArcDirection == -1 )
            theArcDirection *= -1;
        }
      }

      if( oldDirection == theArcStart || theCurrentDirection == theArcStart )
        theArcDirection = 0;
    }
    if( theArcDirection == -1 )
    {
      if( theCurrentDirection > theArcStart )
        theArcExtent = Math.toDegrees(theCurrentDirection-theArcStart)-360;
      else
        theArcExtent = Math.toDegrees(theCurrentDirection-theArcStart);
    }
    else  if( theArcDirection == 1 )
    {
      if( theCurrentDirection > theArcStart )
        theArcExtent = Math.toDegrees(theCurrentDirection-theArcStart);
      else
        theArcExtent = 360 + Math.toDegrees(theCurrentDirection-theArcStart);
    }

    if( theArcExtent%360 == 0 )
      theArcDirection = 0;

    theArc.setAngleStart(Math.toDegrees(theArcStart));
    theArc.setAngleExtent(theArcExtent);

    repaint();

    notifyListeners();
  }

  public double getLastRotation()
  {
    return theRotation;
  }

  public double getDelta()
  {
    return theArcExtent;
  }

  public double getDirection()
  {
    return theCurrentDirection;
  }

  public boolean isResizing()
  {
    return isResizing;
  }

  public void setRotation( double aRotation )
  {
    theArcDirection = 0;
    theArcStart = theCurrentDirection;
    Point2D.Double pnt = LocationGetter.getLocation(theCenter,1,aRotation);
    setRotationFromMouse(pnt.x,pnt.y);
    theArcDirection = 0;
    isResizing = true;
    notifyListeners();
    isResizing = false;
    notifyListeners();
  }

  private void notifyListeners()
  {
    for( int i = 0 ; i < theListeners.size() ; i++ )
      ((ChangeListener)theListeners.get(i)).stateChanged(new ChangeEvent(this));
  }

  public void addChangeListener( ChangeListener aListener )
  {
    theListeners.add(aListener);
  }

  public void removeChangeListener( ChangeListener aListener )
  {
    theListeners.remove(aListener);
  }
}