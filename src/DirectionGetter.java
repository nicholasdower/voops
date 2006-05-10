/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.awt.geom.Point2D;
import java.lang.Math;

public class DirectionGetter
{
  private final static double PI       = 3.141592;
  private final static double ANG90    = PI/2;
  private final static double ANG180   = PI;
  private final static double ANG360   = 2*PI;
  private final static double ANG270   = (PI/2)*3;

  public static double getDirection(Point2D.Double aPoint0, Point2D.Double aPoint1)
  {
    return getDirection(aPoint0.x,aPoint0.y,aPoint1.x,aPoint1.y);
  }
  public static double getDirection(double x0, double y0, double x1, double y1)
  {
    if( x0 == x1 && y0 == y1 )
      return -1;

    if( y0 == y1 )
    {
      if( x0 > x1 )
      {
        return ANG180;
      }
      else if( x0 < x1 )
      {
        return 0;
      }
      else
        return -1;
    }

    if( y0 < y1 )    //  180 - 360
    {
      if( x0 < x1 )      //  270 - 360 
      {
        return Math.atan(Math.abs(x0-x1)/Math.abs(y0-y1)) + ANG270;
      }
      else                             //  180 - 270
        return Math.atan(Math.abs(y0-y1)/Math.abs(x0-x1)) + ANG180;
    }
    else                           //  0 - 180
    {
      if( x0 < x1 )      //  0 - 90
      {
        return Math.atan(Math.abs(y0-y1)/Math.abs(x0-x1));
      }
      else                             //  90 - 180
        return Math.atan(Math.abs(x0-x1)/Math.abs(y0-y1)) + ANG90;
    }
  }
}