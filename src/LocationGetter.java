/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.awt.geom.Point2D;
import java.lang.Math;

public class LocationGetter
{
  private final static double PI       = 3.141592;
  private final static double ANG90    = PI/2;
  private final static double ANG180   = PI;
  private final static double ANG360   = 2*PI;
  private final static double ANG270   = (PI/2)*3;

  public static Point2D.Double getLocation(Point2D.Double aPoint, double aDistance, double anAngle )
  {
    return getLocation(aPoint.x,aPoint.y,aDistance,anAngle);
  }

  public static Point2D.Double getLocation(double anX, double aY, double aDistance, double anAngle )
  {
    if( aDistance == 0 )
      return new Point2D.Double(anX,aY);

    anAngle = anAngle%ANG360;
 
    double x;
    double y;

    x = Math.cos(anAngle)*aDistance;
    y = Math.sin(anAngle)*aDistance;

    return new Point2D.Double(anX+x,aY-y);
  }
}