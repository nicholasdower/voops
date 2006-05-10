/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.awt.event.MouseEvent;
import java.awt.Point;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;
import java.awt.Cursor;

public class EllipseTool extends Tool implements AspectRatioable
{
  private Point theStartPoint;

  private boolean shouldPreserveRatio = false;

  private double  theAspectRatio = 1;

  private static EllipseToolbar theToolbar = new EllipseToolbar();

  private double theStart = 0;
  private double theAngle = 2*Math.PI;
  private int    theArcType  = Arc2D.PIE;

  public EllipseTool( DrawingArea aDrawingArea )
  {
    this(aDrawingArea, PaintableShape.TYPE_DRAW );
  }

  public EllipseTool( DrawingArea aDrawingArea, int aType )
  {
    super(aDrawingArea, aType);
    theToolbar.setTool(this);
    setCursor(OOPSCursors.ELLIPSE);
  }

  public void mouseDragged( MouseEvent e )
  {
    if( !getIsStarted() )
      return;

    drawOvalFill(e.getX(),e.getY());
  }

  public void mousePressed( MouseEvent e )
  {
    if( !getIsStarted() )
    {
      theStartPoint = new Point(e.getX(),e.getY());
      setIsStarted(true);
    }
    else
    {
      setIsStarted(false);
      theStartPoint = null;
      getDrawingArea().clearDrawingArea();
    }
  }

  public void mouseReleased( MouseEvent e )
  {
    if( !getIsStarted() )
      return;

    commit();
    theStartPoint = null;
    setIsStarted(false);
  }

  

  private void drawOvalFill(int x, int y)
  {
    if( theStartPoint.x > x )
    {
      if( theStartPoint.y > y )
      {
        if( shouldPreserveRatio )
        {
          double aspectRatio = ((double)(theStartPoint.x-x))/((double)(theStartPoint.y-y));
          double width  = 0;
          double height = 0;
          if( aspectRatio < theAspectRatio )
          {
            width  = theStartPoint.x-x;
            height = width/theAspectRatio;
          }
          else
          {
            height = theStartPoint.y-y;
            width  = theAspectRatio*height;
          }

          if( theAngle >= 360 )
            getShape().setShape(new Ellipse2D.Double(theStartPoint.x-width,theStartPoint.y-height,width,height));
          else
            getShape().setShape(new Arc2D.Double(theStartPoint.x-width,theStartPoint.y-height,width,height,theStart,theAngle,theArcType));

          theToolbar.setStatus(width,height);
        }
        else
        {
          if( theAngle >= 360 )
            getShape().setShape(new Ellipse2D.Double(x,y,theStartPoint.x-x,theStartPoint.y-y));
          else
            getShape().setShape(new Arc2D.Double(x,y,theStartPoint.x-x,theStartPoint.y-y,theStart,theAngle,theArcType));

          theToolbar.setStatus(theStartPoint.x-x,theStartPoint.y-y);
        }
      }
      else
      {
        if( shouldPreserveRatio )
        {
          double aspectRatio = ((double)(theStartPoint.x-x))/((double)(y-theStartPoint.y));
          double width  = 0;
          double height = 0;
          if( aspectRatio < theAspectRatio )
          {
            width  = theStartPoint.x-x;
            height = width/theAspectRatio;
          }
          else
          {
            height = y-theStartPoint.y;
            width  = theAspectRatio*height;
          }

          if( theAngle >= 360 )
            getShape().setShape(new Ellipse2D.Double(theStartPoint.x-width,theStartPoint.y,width,height));
          else
            getShape().setShape(new Arc2D.Double(theStartPoint.x-width,theStartPoint.y,width,height,theStart,theAngle,theArcType));

          theToolbar.setStatus(width,height);
        }
        else
        {
          if( theAngle >= 360 ) 
            getShape().setShape(new Ellipse2D.Double(x,theStartPoint.y,theStartPoint.x-x,y-theStartPoint.y));
          else
            getShape().setShape(new Arc2D.Double(x,theStartPoint.y,theStartPoint.x-x,y-theStartPoint.y,theStart,theAngle,theArcType));

          theToolbar.setStatus(theStartPoint.x-x,y-theStartPoint.y);
        }
      }
    }
    else
    {
      if( theStartPoint.y > y )
      {
        if( shouldPreserveRatio )
        {
          double aspectRatio = ((double)(x-theStartPoint.x))/((double)(theStartPoint.y-y));
          double width  = 0;
          double height = 0;
          if( aspectRatio < theAspectRatio )
          {
            width  = x-theStartPoint.x;
            height = width/theAspectRatio;
          }
          else
          {
            height = theStartPoint.y-y;
            width  = theAspectRatio*height;
          }

          if( theAngle >= 360 ) 
            getShape().setShape(new Ellipse2D.Double(theStartPoint.x,theStartPoint.y-height,width,height));
          else
            getShape().setShape(new Arc2D.Double(theStartPoint.x,theStartPoint.y-height,width,height,theStart,theAngle,theArcType));

          theToolbar.setStatus(width,height);
        }
        else
        {
          if( theAngle >= 360 )
            getShape().setShape(new Ellipse2D.Double(theStartPoint.x,y,x-theStartPoint.x,theStartPoint.y-y));
          else
            getShape().setShape(new Arc2D.Double(theStartPoint.x,y,x-theStartPoint.x,theStartPoint.y-y,theStart,theAngle,theArcType));

          theToolbar.setStatus(x-theStartPoint.x,theStartPoint.y-y);
        }
      }
      else
      {
        if( shouldPreserveRatio )
        {
          double aspectRatio = ((double)(x-theStartPoint.x))/((double)(y-theStartPoint.y));
          double width  = 0;
          double height = 0;
          if( aspectRatio < theAspectRatio )
          {
            width  = x-theStartPoint.x;
            height = width/theAspectRatio;
          }
          else
          {
            height = y-theStartPoint.y;
            width  = theAspectRatio*height;
          }

          if( theAngle >= 360 )
            getShape().setShape(new Ellipse2D.Double(theStartPoint.x,theStartPoint.y,width,height));
          else
            getShape().setShape(new Arc2D.Double(theStartPoint.x,theStartPoint.y,width,height,theStart,theAngle,theArcType));

          theToolbar.setStatus(width,height);
        }
        else
        {
          if( theAngle >= 360 )
            getShape().setShape(new Ellipse2D.Double(theStartPoint.x,theStartPoint.y,x-theStartPoint.x,y-theStartPoint.y));
          else
            getShape().setShape(new Arc2D.Double(theStartPoint.x,theStartPoint.y,x-theStartPoint.x,y-theStartPoint.y,theStart,theAngle,theArcType));

          theToolbar.setStatus(x-theStartPoint.x,y-theStartPoint.y);
        }
      }
    }

    paintShape();
  }

  public void setAngle( double aStart, double anAngle )
  {
    theStart = aStart;
    theAngle = anAngle;
  }

  public void setArcType( int aType )
  {
    theArcType = aType;
  }

  public JComponent getToolbar()
  {
    theToolbar.resetStatus();
    return theToolbar;
  }

  public void add(double anX, double aY, double aWidth, double aHeight)
  {
    anX     *= getDrawingArea().getZoom();
    aY      *= getDrawingArea().getZoom();
    aWidth  *= getDrawingArea().getZoom();
    aHeight *= getDrawingArea().getZoom();

    if( theAngle >= 360 )
    {
      getShape().setShape(new Ellipse2D.Double(anX,aY,aWidth,aHeight));
      paintShape();
      getShape().setShape(new Ellipse2D.Double(anX,aY,aWidth,aHeight));
    }
    else
    {
      getShape().setShape(new Arc2D.Double(anX,aY,aWidth,aHeight,theStart,theAngle,theArcType));
      paintShape();
      getShape().setShape(new Arc2D.Double(anX,aY,aWidth,aHeight,theStart,theAngle,theArcType));
    }

    commit();
    theStartPoint = null;
    setIsStarted(false);
  }

  public void shouldPreserveRatio( boolean aShouldPreserveRatio )
  {
    shouldPreserveRatio = aShouldPreserveRatio;
  }

  public void setAspectRatio( double anAspectRatio )
  {
    theAspectRatio = anAspectRatio;
  }
}