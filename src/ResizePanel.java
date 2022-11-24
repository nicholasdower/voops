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
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.RenderingHints;
import java.awt.Cursor;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.Vector;

public class ResizePanel extends JPanel
{
  private Selectable       theTool;

  private double           theCurrentXFactor;
  private double           theCurrentYFactor;

  private double           theSize;
  private Rectangle2D.Double theRectangle;
  private Rectangle2D.Double theResizedRectangle;

  private static final double theResizorSize = 4;
  private Rectangle2D.Double[] theResizors;
  private int theCurrentResizor;

  private BufferedImage theImage;
  private Graphics2D    theGraphics;

  private BasicStroke theStroke;

  private Vector theListeners;

  private boolean shouldPreserve = false;

  public ResizePanel( Selectable aTool )
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
              case 0 : setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR)); break;
              case 1 : setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));  break;
              case 2 : setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR)); break;
              case 3 : setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));  break;
              case 4 : setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));  break;
              case 5 : setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR)); break;
              case 6 : setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));  break;
              case 7 : setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR)); break;
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
            setSizeFromMouse(e.getX(),e.getY());
        }
        public void mouseReleased( MouseEvent e )
        {
          if( theCurrentResizor >= 0 )
            ((Tool)theTool).commitUndo();
          theCurrentResizor = -1;
          theResizedRectangle = new Rectangle2D.Double();
          theResizedRectangle.setRect(theRectangle);
        }
      }
    );

    theStroke = new BasicStroke(2f);

    theListeners = new Vector();

    theResizors = new Rectangle2D.Double[8];
  }

  private void setUpImage()
  {
    theImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
    RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                   rh.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
    theGraphics = theImage.createGraphics();
    theGraphics.addRenderingHints(rh);

    theSize    = Math.min(((double)getHeight())/2,((double)getWidth())/2) - theStroke.getLineWidth()*2;

    double y = (((double)getHeight())-theSize)/2;
    double x = (((double)getWidth())-theSize)/2;

    theRectangle = new Rectangle2D.Double(x,y,theSize,theSize);
    theResizedRectangle = new Rectangle2D.Double(x,y,theSize,theSize);

    createResizors();
  }

  public void setShouldPreserve( boolean aShouldPreserve )
  {
    shouldPreserve = aShouldPreserve;
  }

  public boolean shouldPreserve() {
    return shouldPreserve;
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
      theGraphics.draw(theResizors[i]);

    g.drawImage(theImage,0,0,this);
  }

  private void createResizors()
  {
    theResizors[0] = new Rectangle2D.Double(theRectangle.x-(theResizorSize/2),theRectangle.y-(theResizorSize/2),theResizorSize,theResizorSize);
    theResizors[1] = new Rectangle2D.Double((theRectangle.x+theRectangle.width/2)-(theResizorSize/2),theRectangle.y-(theResizorSize/2),theResizorSize,theResizorSize);
    theResizors[2] = new Rectangle2D.Double((theRectangle.x+theRectangle.width)-(theResizorSize/2),theRectangle.y-(theResizorSize/2),theResizorSize,theResizorSize);
    theResizors[3] = new Rectangle2D.Double(theRectangle.x-(theResizorSize/2),(theRectangle.y+theRectangle.height/2)-(theResizorSize/2),theResizorSize,theResizorSize);
    theResizors[4] = new Rectangle2D.Double((theRectangle.x+theRectangle.width)-(theResizorSize/2),(theRectangle.y+theRectangle.height/2)-(theResizorSize/2),theResizorSize,theResizorSize);
    theResizors[5] = new Rectangle2D.Double(theRectangle.x-(theResizorSize/2),(theRectangle.y+theRectangle.height)-(theResizorSize/2),theResizorSize,theResizorSize);
    theResizors[6] = new Rectangle2D.Double((theRectangle.x+theRectangle.width/2)-(theResizorSize/2),(theRectangle.y+theRectangle.height)-(theResizorSize/2),theResizorSize,theResizorSize);
    theResizors[7] = new Rectangle2D.Double((theRectangle.x+theRectangle.width)-(theResizorSize/2),(theRectangle.y+theRectangle.height)-(theResizorSize/2),theResizorSize,theResizorSize);
  }

  private void setSizeFromMouse(int x, int y)
  {
    if( theCurrentResizor == -1 )
      return;

    Rectangle2D.Double oldResizedRectangle = new Rectangle2D.Double();
    oldResizedRectangle.setRect(theResizedRectangle);


    if( theCurrentResizor == 0 )
    {
      theResizedRectangle = new Rectangle2D.Double(x,y,(theResizedRectangle.x-x)+theResizedRectangle.width,(theResizedRectangle.y-y)+theResizedRectangle.height );
    }
    else if( theCurrentResizor == 1 )
    {
      theResizedRectangle = new Rectangle2D.Double(theResizedRectangle.x,y,theResizedRectangle.width,(theResizedRectangle.y-y)+theResizedRectangle.height );
    }
    else if( theCurrentResizor == 2 )
    {
      theResizedRectangle = new Rectangle2D.Double(theResizedRectangle.x,y,x-theResizedRectangle.x,(theResizedRectangle.y-y)+theResizedRectangle.height );
    }
    else if( theCurrentResizor == 3 )
    {
      theResizedRectangle = new Rectangle2D.Double(x,theResizedRectangle.y,(theResizedRectangle.x-x)+theResizedRectangle.width,theResizedRectangle.height );
    }
    else if( theCurrentResizor == 4 )
    {
      theResizedRectangle = new Rectangle2D.Double(theResizedRectangle.x,theResizedRectangle.y,x-theResizedRectangle.x,theResizedRectangle.height );
    }
    else if( theCurrentResizor == 5 )
    {
      theResizedRectangle = new Rectangle2D.Double(x,theResizedRectangle.y,(theResizedRectangle.x-x)+theResizedRectangle.width,y-theResizedRectangle.y );
    }
    else if( theCurrentResizor == 6 )
    {
      theResizedRectangle = new Rectangle2D.Double(theResizedRectangle.x,theResizedRectangle.y,theResizedRectangle.width,y-theResizedRectangle.y );
    }
    else if( theCurrentResizor == 7 )
    {
      theResizedRectangle = new Rectangle2D.Double(theResizedRectangle.x,theResizedRectangle.y,x-theResizedRectangle.x,y-theResizedRectangle.y );
    }

    if( theResizedRectangle.width <= 0 || theResizedRectangle.height <= 0 )
    {
      //theResizedRectangle.setRect(oldResizedRectangle.getBounds2D());
      theCurrentXFactor = 1;
      theCurrentXFactor = 1;
      return;
    }
    else
    {
      if( theResizedRectangle.width < 0 )
      {
        theResizedRectangle = new Rectangle2D.Double(theResizedRectangle.x+theResizedRectangle.width,theResizedRectangle.y,-theResizedRectangle.width, theResizedRectangle.height);
        if( theCurrentResizor == 3 )
          theCurrentResizor = 4;
        else if( theCurrentResizor == 4 )
          theCurrentResizor = 3;
        else if( theCurrentResizor == 0 )
          theCurrentResizor = 2;
        else if( theCurrentResizor == 2 )
          theCurrentResizor = 0;
        else if( theCurrentResizor == 5 )
          theCurrentResizor = 7;
        else if( theCurrentResizor == 7 )
          theCurrentResizor = 5;
      }
      if( theResizedRectangle.height < 0 )
      {
        theResizedRectangle = new Rectangle2D.Double(theResizedRectangle.x,theResizedRectangle.y+theResizedRectangle.height,theResizedRectangle.width,-theResizedRectangle.height);
        if( theCurrentResizor == 0 )
          theCurrentResizor = 5;
        else if( theCurrentResizor == 5 )
          theCurrentResizor = 0;
        else if( theCurrentResizor == 1 )
          theCurrentResizor = 6;
        else if( theCurrentResizor == 6 )
          theCurrentResizor = 1;
        else if( theCurrentResizor == 2 )
          theCurrentResizor = 7;
        else if( theCurrentResizor == 7 )
          theCurrentResizor = 2;
      }
    }

    theCurrentXFactor = theResizedRectangle.width/oldResizedRectangle.width;
    theCurrentYFactor = theResizedRectangle.height/oldResizedRectangle.height;

    if( shouldPreserve )
    {
      double min = Math.min(Math.abs(theCurrentXFactor),Math.abs(theCurrentYFactor));
      theCurrentXFactor = theCurrentXFactor<0?-min:min;
      theCurrentYFactor = theCurrentYFactor<0?-min:min;
    }
    notifyListeners();
  }

  public double[] getLastSize()
  {
    if( theResizedRectangle.width == 0 || theResizedRectangle.height == 0 )
      return null;

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
