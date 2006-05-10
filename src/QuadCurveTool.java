/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.awt.event.MouseEvent;
import java.awt.Point;
import java.awt.Color;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.Cursor;
import javax.swing.JComponent;

public class QuadCurveTool extends Tool
{
  private final static double PI       = 3.141592;
  private final static double ANG90    = PI/2;
  private final static double ANG180   = PI;
  private final static double ANG360   = 2*PI;

  private Line2D.Double theLinePath;

  private Point theStartPoint;
  private Point theEndPoint;

  private double[] theLineLengths;
  private double[] theLineAngles;

  private double[] theCurveLengths;
  private double[] theCurveAngles;

  private boolean isDrawingLine;

  private static QuadCurveToolbar theToolbar = new QuadCurveToolbar();

  public QuadCurveTool( DrawingArea aDrawingArea )
  {
    this(aDrawingArea,PaintableShape.TYPE_DRAW);
  }

  public QuadCurveTool( DrawingArea aDrawingArea, int aType )
  {
    super(aDrawingArea,aType);

    isDrawingLine = true;

    theToolbar.setTool(this);

    setCursor(OOPSCursors.QUAD_CURVE);
  }

  public void mouseDragged( MouseEvent e )
  {
    if( !getIsStarted() )
      return;

    if( isDrawingLine )
      drawLine(e.getX(),e.getY());
    else
      drawCurve(e.getX(),e.getY());
  }

  public void mousePressed( MouseEvent e )
  {
    if( isDrawingLine )
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
        theEndPoint   = null;
        isDrawingLine = true;
      }
    }
    else
    {
      if( !getIsStarted() )
      {
        drawCurve(e.getX(),e.getY());
        setIsStarted(true);
      }
      else
      {
        setIsStarted(false);
        getDrawingArea().clearDrawingArea();
        theStartPoint = null;
        theEndPoint   = null;
        isDrawingLine = true;
      }
    }
  }

  public void mouseReleased( MouseEvent e )
  {
    if( !isDrawingLine )
    {
      if( !getIsStarted() )
        return;

      commit();
      theStartPoint = null;
      theEndPoint   = null;
      isDrawingLine = true;
      setIsStarted(false);
    }
    else
    {
      if( !getIsStarted() )
        return;

      isDrawingLine = false;
      theEndPoint   = new Point((int)theLinePath.x2,(int)theLinePath.y2);
      setIsStarted(false);
    }
  }

  public void setLineLengths( double[] someLengths )
  {
    if( someLengths == null )
    {
      theLineLengths = null;
      return;
    }

    theLineLengths = new double[someLengths.length];
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
      theLineLengths[index--] = temp;
    }
  }

  public void setLineAngles( double[] someAngles )
  {
    if( someAngles == null )
    {
      theLineAngles = null;
      return;
    }

    theLineAngles = new double[someAngles.length];
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
      theLineAngles[index--] = temp;
    }
  }

  public void setCurveLengths( double[] someLengths )
  {
    if( someLengths == null )
    {
      theCurveLengths = null;
      return;
    }

    theCurveLengths = new double[someLengths.length];
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
      theCurveLengths[index--] = temp;
    }
  }

  public void setCurveAngles( double[] someAngles )
  {
    if( someAngles == null )
    {
      theCurveAngles = null;
      return;
    }

    theCurveAngles = new double[someAngles.length];
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
      theCurveAngles[index--] = temp;
    }
  }

  private void drawLine(int x, int y)
  {
    Point2D.Double limitedPoint = new Point2D.Double(x,y);
    double limitedDistance = -1;
    double limitedDirection = -1;
    double distance = DistanceGetter.getDistance(theStartPoint.x,theStartPoint.y,limitedPoint.x,limitedPoint.y);
    if( theLineLengths != null )
    {
      for( int i = 0 ; i < theLineLengths.length ; i++ )
      {
        if( distance < theLineLengths[i]*getDrawingArea().getZoom() )
        {
          limitedDistance = theLineLengths[i];
          break;
        }
      }
      if( limitedDistance < 0 )
        limitedDistance = theLineLengths[theLineLengths.length-1];

      if( theLineAngles == null )
      {
        double direction = DirectionGetter.getDirection(theStartPoint.x,theStartPoint.y,limitedPoint.x,limitedPoint.y);
        limitedPoint = LocationGetter.getLocation(theStartPoint.x,theStartPoint.y,limitedDistance*getDrawingArea().getZoom(),direction);
      }
    }
    else
      limitedDistance = distance;

    double direction = Math.toDegrees(DirectionGetter.getDirection(theStartPoint.x,theStartPoint.y,limitedPoint.x,limitedPoint.y));
    if( theLineAngles != null )
    {
      double diff = 361;
      double curDiff;
      int where = -1;
      for( int i = 0 ; i < theLineAngles.length ; i++ )
      {
        curDiff = Math.abs(theLineAngles[i]-direction);
        if( curDiff > 180 )
          curDiff = 360 - curDiff;
        if( curDiff < diff )
        {
          diff = curDiff;
          where = i;
        }
      }
      limitedDirection = Math.toRadians(theLineAngles[where]);

      if( theLineLengths == null )
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

  private void drawCurve(int x, int y)
  {
    Point2D.Double limitedPoint = new Point2D.Double(x,y);
    double limitedDistance = -1;
    double limitedDirection = -1;

    double dis = DistanceGetter.getDistance(theStartPoint.x,theStartPoint.y,theEndPoint.x,theEndPoint.y);
    double dir = DirectionGetter.getDirection(theStartPoint.x,theStartPoint.y,theEndPoint.x,theEndPoint.y);
    Point2D.Double midPoint = LocationGetter.getLocation(theStartPoint.x,theStartPoint.y,dis/2,dir);

    double distance = DistanceGetter.getDistance(midPoint.x,midPoint.y,limitedPoint.x,limitedPoint.y);
    if( theCurveLengths != null )
    {
      for( int i = 0 ; i < theCurveLengths.length ; i++ )
      {
        if( distance < theCurveLengths[i]*getDrawingArea().getZoom() )
        {
          limitedDistance = theCurveLengths[i];
          break;
        }
      }
      if( limitedDistance < 0 )
        limitedDistance = theCurveLengths[theCurveLengths.length-1];

      if( theCurveAngles == null )
      {
        double direction = DirectionGetter.getDirection(midPoint.x,midPoint.y,limitedPoint.x,limitedPoint.y);
        limitedPoint = LocationGetter.getLocation(midPoint.x,midPoint.y,limitedDistance*getDrawingArea().getZoom(),direction);
      }
    }
    else
      limitedDistance = distance;

    double direction = Math.toDegrees(DirectionGetter.getDirection(midPoint.x,midPoint.y,limitedPoint.x,limitedPoint.y));
    if( theCurveAngles != null )
    {
      double diff = 361;
      double curDiff;
      int where = -1;
      for( int i = 0 ; i < theCurveAngles.length ; i++ )
      {
        curDiff = Math.abs(theCurveAngles[i]-direction);
        if( curDiff > 180 )
          curDiff = 360 - curDiff;
        if( curDiff < diff )
        {
          diff = curDiff;
          where = i;
        }
      }
      limitedDirection = (Math.toRadians(theCurveAngles[where]) + dir)%ANG360;

      if( theCurveLengths == null )
      {
        limitedPoint = LocationGetter.getLocation(midPoint.x,midPoint.y,distance,limitedDirection);
      }
      else
      {
        limitedPoint = LocationGetter.getLocation(midPoint.x,midPoint.y,limitedDistance*getDrawingArea().getZoom(),limitedDirection);
      }
      limitedDirection = theCurveAngles[where];
    }
    else
      limitedDirection = (360 +(direction - Math.toDegrees(dir)))%360;

    getShape().setShape(QuadCurveGetter.getQuadCurve2D(theStartPoint.x,theStartPoint.y,limitedPoint.x,limitedPoint.y,theEndPoint.x,theEndPoint.y));
    paintShape();
    theToolbar.setStatus(limitedDistance,limitedDirection);
  }

  public JComponent getToolbar()
  {
    theToolbar.resetStatus();
    return theToolbar;
  }
}