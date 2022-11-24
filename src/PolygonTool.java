/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.GeneralPath;
import javax.swing.JComponent;
import java.awt.Cursor;

public class PolygonTool extends Tool implements LineAngleLimitable
{
  private GeneralPath thePath;
  private GeneralPath theCurrentPath;

  private Point2D.Double theLastPoint;
  private Point2D.Double theCurrentPoint;

  private double[] theLengths;
  private double[] theAngles;

  private static PolygonToolbar theToolbar = new PolygonToolbar();

  private boolean isInited = false;

  private boolean  shouldClose = true;

  public PolygonTool( DrawingArea aDrawingArea )
  {
    this(aDrawingArea,PaintableShape.TYPE_DRAW);
  }

  public PolygonTool( DrawingArea aDrawingArea, int aType )
  {
    super(aDrawingArea,aType);
    theToolbar.setTool(this);
    setCursor(OOPSCursors.POLYGON);
  }

  public void mouseDragged( MouseEvent e )
  {
    if( !getIsStarted() )
      return;

    drawLine(e.getX(),e.getY());
  }

  public void mouseReleased( MouseEvent e )
  {
    if( !getIsStarted() )
      return;

    if( isInited )
    {
      thePath = (GeneralPath)theCurrentPath.clone();
      theLastPoint = new Point2D.Double(theCurrentPoint.x,theCurrentPoint.y);
      setIsStarted(false);
    }
  }

  public void mousePressed( MouseEvent e )
  {
    if( getIsStarted() )
    {
      setIsStarted(false);
      getDrawingArea().clearDrawingArea();
      thePath        = null;
      theCurrentPath = null;
      isInited       = false;
      theToolbar.resetStatus();
      return;
    }
    if( isInited )
    {
      setIsStarted(true);
      if( e.getClickCount() == 2 )
      {
        drawLine(e.getX(),e.getY());

        if( shouldClose )
          thePath.closePath();
        getShape().setShape(thePath);
        commit();
        thePath        = null;
        theCurrentPath = null;
        isInited       = false;
        setIsStarted(false);
        theToolbar.resetStatus();
      }
      else
        drawLine(e.getX(),e.getY());
    }
    else
    {
      isInited = true;
      drawLine(e.getX(),e.getY());
      setIsStarted(true);
    }
  }

  public void loseControl()
  {
    if( thePath == null )
      return;

    if( shouldClose )
      thePath.closePath();
    getShape().setShape(thePath);
    commit();
    thePath        = null;
    theCurrentPath = null;
    isInited       = false;
    setIsStarted(false);
  }

  private void drawLine(int x, int y)
  {
    if( thePath == null )
    {
      thePath = new GeneralPath();
      thePath.moveTo(x,y);

      theCurrentPoint = new Point2D.Double(x,y);
      theLastPoint    = new Point2D.Double(x,y);
    }
    else
    {
      theCurrentPath = (GeneralPath)thePath.clone();

      Point2D.Double limitedPoint = new Point2D.Double(x,y);
      double limitedDistance = -1;
      double limitedDirection = -1;
      double distance = DistanceGetter.getDistance(theLastPoint.x,theLastPoint.y,limitedPoint.x,limitedPoint.y);
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
          double direction = DirectionGetter.getDirection(theLastPoint.x,theLastPoint.y,limitedPoint.x,limitedPoint.y);
          limitedPoint = LocationGetter.getLocation(theLastPoint.x,theLastPoint.y,limitedDistance*getDrawingArea().getZoom(),direction);
        }
      }
      else
        limitedDistance = distance;

      double direction = Math.toDegrees(DirectionGetter.getDirection(theLastPoint.x,theLastPoint.y,limitedPoint.x,limitedPoint.y));
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
          limitedPoint = LocationGetter.getLocation(theLastPoint.x,theLastPoint.y,distance,limitedDirection);
        }
        else
        {
          limitedPoint = LocationGetter.getLocation(theLastPoint.x,theLastPoint.y,limitedDistance*getDrawingArea().getZoom(),limitedDirection);
        }
        limitedDirection = Math.toDegrees(limitedDirection);
      }
      else
        limitedDirection = direction;

      theCurrentPath.lineTo((float)limitedPoint.x,(float)limitedPoint.y);
      getShape().setShape(theCurrentPath);
      paintShape();
      theCurrentPoint = new Point2D.Double(limitedPoint.x,limitedPoint.y);
      theToolbar.setStatus(limitedDistance,limitedDirection);
    }
  }

  public void setShouldClose(boolean aShouldClose)
  {
    shouldClose = aShouldClose;
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

  public JComponent getToolbar()
  {
    theToolbar.resetStatus();
    return theToolbar;
  }
}