/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.awt.event.MouseEvent;
import java.awt.Point;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JComponent;
import java.awt.Cursor;

public class RoundRectangleTool extends Tool implements AspectRatioable
{
  private Point theStartPoint;
  private double   theArcWidth    = 20;
  private double   theArcHeight   = 20;

  private boolean shouldPreserveRatio = false;

  private double  theAspectRatio = 1;

  private static RoundRectangleToolbar theToolbar = new RoundRectangleToolbar();

  public RoundRectangleTool( DrawingArea aDrawingArea )
  {
    this(aDrawingArea, PaintableShape.TYPE_DRAW);
  }

  public RoundRectangleTool( DrawingArea aDrawingArea, int aType )
  {
    super(aDrawingArea, aType);
    theToolbar.setTool(this);
    setCursor(OOPSCursors.ROUND_RECTANGLE);
  }

  public void mouseDragged( MouseEvent e )
  {
    if( !getIsStarted() )
      return;

    drawRoundRect(e.getX(),e.getY());
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
      getDrawingArea().clearDrawingArea();
      theStartPoint = null;
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

  private void drawRoundRect(int x, int y)
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
          getShape().setShape(new RoundRectangle2D.Double(theStartPoint.x-width,theStartPoint.y-height,width,height,theArcWidth*getDrawingArea().getZoom(),theArcHeight*getDrawingArea().getZoom()));
          theToolbar.setStatus(width,height);
        }
        else
        {
          getShape().setShape(new RoundRectangle2D.Double(x,y,theStartPoint.x-x,theStartPoint.y-y,theArcWidth*getDrawingArea().getZoom(),theArcHeight*getDrawingArea().getZoom()));
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
          getShape().setShape(new RoundRectangle2D.Double(theStartPoint.x-width,theStartPoint.y,width,height,theArcWidth*getDrawingArea().getZoom(),theArcHeight*getDrawingArea().getZoom()));
          theToolbar.setStatus(width,height);
        }
        else
        {
          getShape().setShape(new RoundRectangle2D.Double(x,theStartPoint.y,theStartPoint.x-x,y-theStartPoint.y,theArcWidth*getDrawingArea().getZoom(),theArcHeight*getDrawingArea().getZoom()));
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
          getShape().setShape(new RoundRectangle2D.Double(theStartPoint.x,theStartPoint.y-height,width,height,theArcWidth*getDrawingArea().getZoom(),theArcHeight*getDrawingArea().getZoom()));
          theToolbar.setStatus(width,height);
        }
        else
        {
          getShape().setShape(new RoundRectangle2D.Double(theStartPoint.x,y,x-theStartPoint.x,theStartPoint.y-y,theArcWidth*getDrawingArea().getZoom(),theArcHeight*getDrawingArea().getZoom()));
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
          getShape().setShape(new RoundRectangle2D.Double(theStartPoint.x,theStartPoint.y,width,height,theArcWidth*getDrawingArea().getZoom(),theArcHeight*getDrawingArea().getZoom()));
          theToolbar.setStatus(width,height);
        }
        else
        {
          getShape().setShape(new RoundRectangle2D.Double(theStartPoint.x,theStartPoint.y,x-theStartPoint.x,y-theStartPoint.y,theArcWidth*getDrawingArea().getZoom(),theArcHeight*getDrawingArea().getZoom()));
          theToolbar.setStatus(x-theStartPoint.x,y-theStartPoint.y);
        }
      }
    }

    paintShape();
  }

  public void setArcWidth( double aWidth )
  {
    theArcWidth = aWidth;
  }

  public void setArcHeight( double aHeight )
  {
    theArcHeight = aHeight;
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

    getShape().setShape(new RoundRectangle2D.Double(anX,aY,aWidth,aHeight,theArcWidth*getDrawingArea().getZoom(),theArcHeight*getDrawingArea().getZoom()));
    paintShape();
    getShape().setShape(new RoundRectangle2D.Double(anX,aY,aWidth,aHeight,theArcWidth*getDrawingArea().getZoom(),theArcHeight*getDrawingArea().getZoom()));
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