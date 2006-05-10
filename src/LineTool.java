/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.awt.Point;
import java.awt.geom.Area;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import java.awt.Cursor;
import java.awt.Toolkit;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class LineTool extends Tool implements LineAngleLimitable
{
  private Line2D.Double theLinePath;
  private Point2D.Double theStartPoint;

  private static SingleLineToolbar theToolbar = new SingleLineToolbar();
  
  private double[] theLengths;
  private double[] theAngles;

  public LineTool( DrawingArea aDrawingArea )
  {
    this(aDrawingArea,PaintableShape.TYPE_DRAW);
  }

  public LineTool( DrawingArea aDrawingArea, int aType )
  {
    super(aDrawingArea,aType);
    theToolbar.setTool(this);
    setCursor(OOPSCursors.LINE);
  }

  public void mouseDragged( MouseEvent e )
  {
    if( !getIsStarted() )
      return;

    drawLine(e.getX(),e.getY());
  }

  public void mousePressed( MouseEvent e )
  {
    if( !getIsStarted() )
    {
      setIsStarted(true);
      theStartPoint = new Point2D.Double(e.getX(),e.getY());
    }
    else
    {
      setIsStarted(false);
      theLinePath = null;
      getDrawingArea().clearDrawingArea();
    }
  }

  public void mouseReleased( MouseEvent e )
  {
    if( !getIsStarted() )
      return;

    commit();
    getDrawingArea().repaint();
    theLinePath = null;
    setIsStarted(false);
  }

  private void drawLine( double x, double y )
  {
    Point2D.Double limitedPoint = new Point2D.Double(x,y);
    double limitedDistance = -1;
    double limitedDirection = -1;
    double distance = DistanceGetter.getDistance(theStartPoint.x,theStartPoint.y,limitedPoint.x,limitedPoint.y);
    if( theLengths != null )
    {
      for( int i = 0 ; i < theLengths.length ; i++ )
      {
        if( distance < theLengths[i]*getDrawingArea().getZoom() )
        {
          limitedDistance = theLengths[i];
          break;
        }
      }
      if( limitedDistance < 0 )
        limitedDistance = theLengths[theLengths.length-1];

      if( theAngles == null )
      {
        double direction = DirectionGetter.getDirection(theStartPoint.x,theStartPoint.y,limitedPoint.x,limitedPoint.y);
        limitedPoint = LocationGetter.getLocation(theStartPoint.x,theStartPoint.y,limitedDistance*getDrawingArea().getZoom(),direction);
      }
    }
    else
      limitedDistance = distance;

    double direction = Math.toDegrees(DirectionGetter.getDirection(theStartPoint.x,theStartPoint.y,limitedPoint.x,limitedPoint.y));
    if( theAngles != null )
    {
      double diff = 361;
      double curDiff;
      int where = -1;
      for( int i = 0 ; i < theAngles.length ; i++ )
      {
        curDiff = Math.abs(theAngles[i]-direction);
        if( curDiff > 180 )
          curDiff = 360 - curDiff;
        if( curDiff < diff )
        {
          diff = curDiff;
          where = i;
        }
      }
      limitedDirection = Math.toRadians(theAngles[where]);

      if( theLengths == null )
      {
        limitedPoint = LocationGetter.getLocation(theStartPoint.x,theStartPoint.y,distance,limitedDirection);
      }
      else
      {
        limitedPoint = LocationGetter.getLocation(theStartPoint.x,theStartPoint.y,limitedDistance*getDrawingArea().getZoom(),limitedDirection);
      }
      limitedDirection = Math.toDegrees(limitedDirection);
    }
    else
      limitedDirection = direction;

    theLinePath = new Line2D.Double(theStartPoint.x,theStartPoint.y,limitedPoint.x,limitedPoint.y);
    getShape().setShape(theLinePath);
    paintShape();
    theToolbar.setStatus(limitedDistance,limitedDirection);
  }

  public JComponent getToolbar()
  {
    theToolbar.resetStatus();
    return theToolbar;
  }

  public void setLengths( double[] someLengths )
  {
    if( someLengths == null )
    {
      theLengths = null;
      return;
    }

    theLengths = new double[someLengths.length];
    double temp;
    int    where;
    int    index = someLengths.length-1;
    for( int j = 0 ; j < someLengths.length ; j++ )
    {
      temp = -1;
      where = -1;
      for( int i = 0 ; i < someLengths.length ; i++ )
      {
        if( someLengths[i] > temp )
        {
          temp = someLengths[i];
          where = i;
        }
      }
      someLengths[where] = -1;
      theLengths[index--] = temp;
    }
  }

  public void setAngles( double[] someAngles )
  {
    if( someAngles == null )
    {
      theAngles = null;
      return;
    }

    theAngles = new double[someAngles.length];
    double temp;
    int    where;
    int    index = someAngles.length-1;
    for( int j = 0 ; j < someAngles.length ; j++ )
    {
      temp = -1;
      where = -1;
      for( int i = 0 ; i < someAngles.length ; i++ )
      {
        if( someAngles[i] > temp )
        {
          temp = someAngles[i];
          where = i;
        }
      }
      someAngles[where] = -1;
      theAngles[index--] = temp;
    }
  }

  public void add(double anX0, double aY0, double anX1, double aY1)
  {
    anX0 *= getDrawingArea().getZoom();
    aY0  *= getDrawingArea().getZoom();
    anX1 *= getDrawingArea().getZoom();
    aY1  *= getDrawingArea().getZoom();

    getShape().setShape(new Line2D.Double(anX0,aY0,anX1,aY1));
    paintShape();
    getShape().setShape(new Line2D.Double(anX0,aY0,anX1,aY1));
    commit();
  }
  public void addRay(double anX0, double aY0, double direction, double length)
  {
    anX0   *= getDrawingArea().getZoom();
    aY0    *= getDrawingArea().getZoom();
    length *= getDrawingArea().getZoom();

    Point2D.Double location = LocationGetter.getLocation(anX0,aY0,length,Math.toRadians(direction));
    getShape().setShape(new Line2D.Double(anX0,aY0,location.x,location.y));
    paintShape();
    getShape().setShape(new Line2D.Double(anX0,aY0,location.x,location.y));
    commit();
  }
}