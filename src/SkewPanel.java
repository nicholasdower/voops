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
import java.awt.Cursor;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.RenderingHints;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.Vector;

public class SkewPanel extends JPanel
{
  private Selectable       theTool;

  private double           theCurrentXFactor;
  private double           theCurrentYFactor;

  private double             theSize;
  private Point2D.Double     theLastPoint;
  private double             theCurrentWidth;
  private double             theCurrentHeight;
  private Rectangle2D.Double theRectangle;

  private double theResizorSize;
  private Rectangle2D.Double[] theResizors;
  private int theCurrentResizor;

  private BufferedImage theImage;
  private Graphics2D    theGraphics;

  private BasicStroke theStroke;
  private BasicStroke theLightStroke;

  private Vector theListeners;

  public SkewPanel( Selectable aTool )
  {
    theTool = aTool;

    this.addMouseMotionListener
    (
      new MouseMotionAdapter()
      {
        public void mouseDragged( MouseEvent e )
        {
          setSizeFromMouse(e.getX(),e.getY());
        }

        public void mouseMoved( MouseEvent e )
        {
          theCurrentResizor = -1;
          for( int i = 0 ; i < theResizors.length ; i++ )
          {
            if( theResizors[i] != null && theResizors[i].contains(e.getX(), e.getY()) )
            {
              theCurrentResizor = i;
              break;
            }
          }

          if( theCurrentResizor >= 0 )
          {
            switch(theCurrentResizor)
            {
              case 0 : setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR)); break;
              case 1 : setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR)); break;
              case 2 : setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR)); break;
              case 3 : setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR)); break;
            }
          }
          else
          {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
          }
        }
      }
    );

    this.addMouseListener
    (
      new MouseAdapter()
      {
        public void mousePressed( MouseEvent e )
        {
          theCurrentResizor = -1;
          for( int i = 0 ; i < theResizors.length ; i++ )
          {
            if( theResizors[i] != null && theResizors[i].contains(e.getX(), e.getY()) )
            {
              theCurrentResizor = i;
              break;
            }
          }
          if( theCurrentResizor >= 0 )
          {
            theCurrentWidth  = theRectangle.width;
            theCurrentHeight = theRectangle.height;
            theLastPoint = new Point2D.Double(e.getX(),e.getY());
            setSizeFromMouse(e.getX(),e.getY());
          }
        }
        public void mouseReleased( MouseEvent e )
        {
          theCurrentResizor = -1;
          theLastPoint = null;
          ((Tool)theTool).commitUndo();
        }
      }
    );
  
    theListeners = new Vector();

    theResizors = new Rectangle2D.Double[4];
  }

  private void setUpImage()
  {
    theImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
    RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                   rh.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
    theGraphics = theImage.createGraphics();
    theGraphics.addRenderingHints(rh);

    theSize    = Math.min(((double)getHeight())/2,((double)getWidth())/2);

    double y = (((double)getHeight())-theSize)/2;
    double x = (((double)getWidth())-theSize)/2;

    theRectangle = new Rectangle2D.Double(x,y,theSize,theSize);

    theResizorSize = theSize/20;

    createResizors();

    theStroke = new BasicStroke((float)(theSize/20));
    theLightStroke = new BasicStroke((float)(theSize/60) , BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, (float)(theSize/60) < 1 ? 1 : (float)(theSize/60), new float[]{(float)(theSize/60)},0);
  }

  public void paint( Graphics g )
  {
    if( theImage == null || theImage.getWidth() != getWidth() || theImage.getHeight() != getHeight() )
      setUpImage();

    theGraphics.setColor(getBackground());
    theGraphics.fillRect(0,0,getWidth(),getHeight());

    theGraphics.setStroke(theStroke);
    theGraphics.setPaint(Color.white);
    theGraphics.fill(theRectangle);
    theGraphics.setPaint(Color.black);
    theGraphics.draw(theRectangle);

    theGraphics.setPaint(Color.red);
    for( int i = 0 ; i < theResizors.length ; i++ )
    {
      theGraphics.draw(theResizors[i]);
      theGraphics.fill(theResizors[i]);
    }

    theGraphics.setPaint(new Color(0,0,0,180));
    theGraphics.setStroke(theLightStroke);
    theGraphics.draw(new Line2D.Double(theRectangle.x+theRectangle.width,theRectangle.y,theRectangle.x+theRectangle.width+theSize/4,theRectangle.y));
    theGraphics.draw(new Line2D.Double(theRectangle.x+theRectangle.width+theSize/4,theRectangle.y, theRectangle.x+theRectangle.width, theRectangle.y+theRectangle.height));
    theGraphics.draw(new Line2D.Double(theRectangle.x,theRectangle.y+theRectangle.height,theRectangle.x-theSize/4,theRectangle.y+theRectangle.height));
    theGraphics.draw(new Line2D.Double(theRectangle.x-theSize/4,theRectangle.y+theRectangle.height,theRectangle.x,theRectangle.y));
    theGraphics.draw(new Line2D.Double(theRectangle.x,theRectangle.y,theRectangle.x,theRectangle.y-theSize/4));
    theGraphics.draw(new Line2D.Double(theRectangle.x,theRectangle.y-theSize/4, theRectangle.x+theRectangle.width,theRectangle.y));
    theGraphics.draw(new Line2D.Double(theRectangle.x+theRectangle.width,theRectangle.y+theRectangle.height,theRectangle.x+theRectangle.width,theRectangle.y+theRectangle.height+theSize/4));
    theGraphics.draw(new Line2D.Double(theRectangle.x+theRectangle.width,theRectangle.y+theRectangle.height+theSize/4, theRectangle.x,theRectangle.y+theRectangle.height));

    g.drawImage(theImage,0,0,this);
  }

  private void createResizors()
  {
    theResizors[0] = new Rectangle2D.Double((theRectangle.x+theRectangle.width/2)-(theResizorSize/2),theRectangle.y-(theResizorSize/2),theResizorSize,theResizorSize);
    theResizors[1] = new Rectangle2D.Double(theRectangle.x-(theResizorSize/2),(theRectangle.y+theRectangle.height/2)-(theResizorSize/2),theResizorSize,theResizorSize);
    theResizors[2] = new Rectangle2D.Double((theRectangle.x+theRectangle.width)-(theResizorSize/2),(theRectangle.y+theRectangle.height/2)-(theResizorSize/2),theResizorSize,theResizorSize);
    theResizors[3] = new Rectangle2D.Double((theRectangle.x+theRectangle.width/2)-(theResizorSize/2),(theRectangle.y+theRectangle.height)-(theResizorSize/2),theResizorSize,theResizorSize);
  }

  private void setSizeFromMouse(int x, int y)
  {
    if( theCurrentResizor == -1 )
      return;

    double xDist = Math.abs(theLastPoint.x-x);
    double yDist = Math.abs(theLastPoint.y-y);

    if( theCurrentResizor == 0 || theCurrentResizor == 3 )
    {
      theCurrentXFactor = xDist/theCurrentWidth;
      theCurrentYFactor = 0;
      double startX = Math.min(theRectangle.x,x-theRectangle.width/2);
      theCurrentWidth = (theRectangle.x+theRectangle.width)- startX;
      theCurrentHeight = theRectangle.height;
    }
    else
    {
      theCurrentXFactor = 0;
      theCurrentYFactor = yDist/theCurrentHeight;
      double startY = Math.min(theRectangle.y,y-theRectangle.height/2);
      theCurrentHeight = (theRectangle.y+theRectangle.height)- startY;
      theCurrentWidth = theRectangle.width;
    }

    if( theCurrentResizor == 0 )
    {
      if( x > theLastPoint.x )
        theCurrentXFactor *= -1;
    }
    else if( theCurrentResizor == 1 )
    {
      if( y > theLastPoint.y )
        theCurrentYFactor *= -1;
    }
    else if( theCurrentResizor == 2 )
    {
      if( y < theLastPoint.y )
        theCurrentYFactor *= -1;
    }
    else if( theCurrentResizor == 3 )
    {
      if( x < theLastPoint.x )
        theCurrentXFactor *= -1;
    }

    theLastPoint = new Point2D.Double(x,y);

    notifyListeners();
  }

  public double[] getLastSkew()
  {
    return new double[]{theCurrentXFactor,theCurrentYFactor,theCurrentResizor};
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