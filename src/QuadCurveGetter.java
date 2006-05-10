/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.awt.geom.QuadCurve2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.Shape;

public class QuadCurveGetter
{
  private final static double PI       = 3.141592;
  private final static double ANG90    = PI/2;
  private final static double ANG180   = PI;
  private final static double ANG360   = 2*PI;
  private final static double ANG270   = (PI/2)*3;

  public static Shape getQuadCurve2D(double x0, double y0, double x1, double y1, double x2, double y2)
  {
    double baseDir   = DirectionGetter.getDirection(x0,y0,x2,y2);
    double opBaseDir = DirectionGetter.getDirection(x2,y2,x0,y0);
    double dir       = DirectionGetter.getDirection(x0,y0,x1,y1);

    double startDir;
    double endDir;

    if( baseDir >= 0 && baseDir < ANG180 )
    {
      if( dir >= baseDir && dir < (baseDir+ANG180) )
      {
        startDir = DirectionGetter.getDirection(x1,y1,x0,y0);
        endDir   = DirectionGetter.getDirection(x1,y1,x2,y2);
      }
      else
      {
        startDir   = DirectionGetter.getDirection(x1,y1,x2,y2);
        endDir = DirectionGetter.getDirection(x1,y1,x0,y0);

        double temp;
        temp = x0; 
        x0   = x2;
        x2   = temp;
        temp = y0; 
        y0   = y2;
        y2   = temp;
      }
    }
    else
    {
      if( dir >= baseDir || dir <= (baseDir+ANG180)%ANG360 )
      {
        startDir = DirectionGetter.getDirection(x1,y1,x0,y0);
        endDir   = DirectionGetter.getDirection(x1,y1,x2,y2);
      }
      else
      {
        startDir   = DirectionGetter.getDirection(x1,y1,x2,y2);
        endDir = DirectionGetter.getDirection(x1,y1,x0,y0);

        double temp;
        temp = x0; 
        x0   = x2;
        x2   = temp;
        temp = y0; 
        y0   = y2;
        y2   = temp;
      }
    }
//System.out.println(Math.toDegrees(startDir) + "\t" + Math.toDegrees(endDir));

    double angle    = Math.abs(startDir-endDir);
           angle    = angle > ANG180 ? Math.abs(ANG360-angle) : angle;

    if( angle < .1 || startDir == baseDir || startDir == opBaseDir)
    {
      GeneralPath path = new GeneralPath(); 
      //System.out.println("Zero");
      path.moveTo((float)x0,(float)y0);
      path.lineTo((float)x1,(float)y1);
      path.lineTo((float)x2,(float)y2);

      return path;
    }

    double sideAngles = (ANG180-angle)/2;
    double maxDir     = (endDir + sideAngles)%ANG360;
    double extendDir  = (maxDir+ANG90)%360;

    Point2D.Double     control;
    Point2D.Double     max     = new Point2D.Double(x1,y1);
    QuadCurve2D.Double currentCurve;
    double extendDist = .1;
 
    for( int i = 1 ; i < 100000 ; i++ )
    {
      control = LocationGetter.getLocation(x1,y1,i*extendDist,extendDir);
      currentCurve = new QuadCurve2D.Double((float)x0,(float)y0,(float)control.x,(float)control.y,(float)x2,(float)y2);

      if(  currentCurve.contains(max) )
      {
        return currentCurve;
      }
    }

    GeneralPath path = new GeneralPath(); 
    System.out.println("Created Path");
    path.moveTo((float)x0,(float)y0);
    path.lineTo((float)x1,(float)y1);
    path.lineTo((float)x2,(float)y2);
    return path;
  }
}