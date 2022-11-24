/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.awt.geom.Point2D;
import java.lang.Math;

public class AngleAverager
{
  private final static double PI       = 3.141592;
  private final static double ANG90    = PI/2;
  private final static double ANG180   = PI;
  private final static double ANG360   = 2*PI;
  private final static double ANG270   = (PI/2)*3;

  public static double averageAngles(double anAngle0, double anAngle1, double aPercentage )
  {
    anAngle0 %= ANG360;
    anAngle1 %= ANG360;

    if( anAngle0 == anAngle1 )
      return anAngle0;

    double maxAngle = Math.max(anAngle0, anAngle1);
    double minAngle = Math.min(anAngle0, anAngle1);

    double average;

    if( maxAngle == anAngle0 )
    {
      if( (maxAngle-minAngle) > ANG180 )
        average = (maxAngle + ((ANG360-maxAngle)+minAngle)*(1-aPercentage))%ANG360;
      else
        average = minAngle + (maxAngle-minAngle)*aPercentage;
    }
    else
    {
      if( (maxAngle-minAngle) > ANG180 )
        average = (maxAngle + ((ANG360-maxAngle)+minAngle)*aPercentage)%ANG360;
      else
        average = minAngle + (maxAngle-minAngle)*(1-aPercentage);
    }

    return average;
  }
}