/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.awt.geom.Point2D;
import java.lang.Math;

public class DistanceGetter
{
  public static double getDistance(Point2D.Double aPoint0, Point2D.Double aPoint1)
  {
    return getDistance(aPoint0.x,aPoint0.y,aPoint1.x,aPoint1.y);
  }
  public static double getDistance(double x0, double y0, double x1, double y1)
  {
    return Math.pow( Math.pow(Math.abs(x0-x1),2)+Math.pow(Math.abs(y0-y1),2), 0.5 );
  }
}