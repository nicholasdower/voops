/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.awt.geom.Point2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Ellipse2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Rectangle2D;
import java.awt.Graphics2D;

public class SmoothPath
{
    private Point2D.Double  theStartPoint;
    private double theStartDirection;
    private double theStartDistance;

    private Point2D.Double thePoint0;
    private Point2D.Double thePoint1;
    private Point2D.Double thePoint2;

    private GeneralPath thePath;
    private GeneralPath theLastPath;

    private double thePadding;

    private double theLastDirection;

    private double theMinDistance;

    public SmoothPath( double aPadding, double aDistance )
    {
      thePadding     = aPadding;
      theMinDistance = aDistance;
    }
 
    public void setSettings( double aPadding, double aDistance )
    {
      thePadding     = aPadding;
      theMinDistance = aDistance;
    }

    public void start( double anX, double aY )
    {
	thePath = new GeneralPath();
        thePath.moveTo((float)anX,(float)aY);
        theStartPoint = new Point2D.Double(anX,aY);

        thePoint2 = new Point2D.Double(anX,aY);
        thePoint1 = null;
        thePoint0 = null;
    }

    public void addPoint( double anX, double aY )
    {
      addPoint(anX,aY,false);
    }
    public void addPoint( double anX, double aY, boolean noMatterWhat )
    {
      if( thePoint2 == null ) //No points
      {
        start(anX,aY);
        return;
      }

      if( DistanceGetter.getDistance(thePoint2,new Point2D.Double(anX,aY)) < theMinDistance && !noMatterWhat )
        return;

      thePoint0 = thePoint1;
      thePoint1 = thePoint2;
      thePoint2 = new Point2D.Double(anX,aY);
      
      if( thePoint0 == null )  //Only two points
      {
        theLastDirection  = DirectionGetter.getDirection(thePoint1,thePoint2);
        theStartDirection = DirectionGetter.getDirection(thePoint2,thePoint1);
        theStartDistance  = DistanceGetter.getDistance(thePoint1,thePoint2)*thePadding;
        return;
      }
      else
      {
        double A    = DirectionGetter.getDirection(thePoint0,thePoint1);
        double NOTA = DirectionGetter.getDirection(thePoint1,thePoint0);
        double B    = DirectionGetter.getDirection(thePoint1,thePoint2);
        double NOTB = DirectionGetter.getDirection(thePoint2,thePoint1);

        double controlDirection1 = AngleAverager.averageAngles(NOTA,NOTB,0.5);
        double controlDirection2 = AngleAverager.averageAngles(A,B,0.5);

        double         distance0 = DistanceGetter.getDistance(thePoint0,thePoint1);
        Point2D.Double control0  = LocationGetter.getLocation(thePoint0,thePadding*distance0,theLastDirection);

        double         distance1 = DistanceGetter.getDistance(thePoint1,thePoint2);
        Point2D.Double control1  = LocationGetter.getLocation(thePoint1,thePadding*distance0,controlDirection1);

        //thePath.append(new Ellipse2D.Double(control0.x-1,control0.y-1,2,2),false);
        //thePath.append(new Rectangle2D.Double(control1.x-1,control1.y-1,2,2),false);
        //thePath.append(new CubicCurve2D.Float((float)thePoint0.x,(float)thePoint0.y,(float)control0.x,(float)control0.y,(float)control1.x,(float)control1.y,(float)thePoint1.x,(float)thePoint1.y),false);
        thePath.curveTo((float)control0.x,(float)control0.y,(float)control1.x,(float)control1.y,(float)thePoint1.x,(float)thePoint1.y);
        theLastDirection = controlDirection2;
      }
    }

    public GeneralPath getPath()
    {
      return (GeneralPath)thePath.clone();
    }

    public Point2D.Double getStartPoint()
    {
      return theStartPoint;
    }

    public void closePath()
    {
      if( thePoint1 == null )
        return;

      double         distance0 = DistanceGetter.getDistance(thePoint1,theStartPoint);
      Point2D.Double control0  = LocationGetter.getLocation(thePoint1,thePadding*distance0,theLastDirection);

      double         direction1        = DirectionGetter.getDirection(theStartPoint,thePoint1);
      double         controlDirection1 = AngleAverager.averageAngles(direction1, theStartDirection, 0.5);
      Point2D.Double control1          = LocationGetter.getLocation(theStartPoint,thePadding*distance0,theStartDirection);

      thePath.curveTo((float)control0.x,(float)control0.y,(float)control1.x,(float)control1.y,(float)theStartPoint.x,(float)theStartPoint.y);
      thePath.closePath();
    }

    public GeneralPath getWithLastPointsCurved( int x, int y )
    {
      if( thePath == null || thePoint1 == null )
        return getWithLastPoints(x,y);

      GeneralPath gp = (GeneralPath)thePath.clone();

      double A    = DirectionGetter.getDirection(thePoint1,thePoint2);
      double NOTA = DirectionGetter.getDirection(thePoint2,thePoint1);
      double B    = DirectionGetter.getDirection(thePoint2,new Point2D.Double(x,y));
      double NOTB = DirectionGetter.getDirection(new Point2D.Double(x,y),thePoint2);

      double controlDirection1 = AngleAverager.averageAngles(NOTA,NOTB,0.5);
      double controlDirection2 = AngleAverager.averageAngles(A,B,0.5);

      double         distance0 = DistanceGetter.getDistance(thePoint1,thePoint2);
      Point2D.Double control0  = LocationGetter.getLocation(thePoint1,thePadding*distance0,theLastDirection);

      double         distance1 = DistanceGetter.getDistance(thePoint2,new Point2D.Double(x,y));
      Point2D.Double control1  = LocationGetter.getLocation(thePoint2,thePadding*distance0,controlDirection1);

      gp.curveTo((float)control0.x,(float)control0.y,(float)control1.x,(float)control1.y,(float)thePoint2.x,(float)thePoint2.y);

      distance0 = DistanceGetter.getDistance(thePoint2,new Point2D.Double(x,y));
      control0  = LocationGetter.getLocation(thePoint2,thePadding*distance0,controlDirection2);

      gp.quadTo((float)control0.x,(float)control0.y,x,y);
      return gp;
    }

    public GeneralPath getWithLastPoints( int x, int y )
    {
      GeneralPath tempPath = (GeneralPath)getPath();
      if( thePoint0 == null )
      {
        if( thePoint1 == null )
          tempPath.lineTo(x,y);
        else
        {
          tempPath.lineTo((int)(thePoint2.x),(int)(thePoint2.y));
          tempPath.lineTo(x,y);
        }
      }
      else
      {
          tempPath.lineTo((int)(thePoint2.x),(int)(thePoint2.y));
          tempPath.lineTo(x,y);
      }
      return tempPath;
    }
}