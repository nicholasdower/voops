/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.awt.Shape;
import java.awt.geom.PathIterator;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

public class ShapeToCubicCurver
{
  private ShapeToCubicCurver(){}

  public static Shape convert( Shape aShape )
  {
    PathIterator iterator = aShape.getPathIterator(null);

    GeneralPath newPath = new GeneralPath(iterator.getWindingRule());

    float[] points = new float[6];
    float[] lastPoint = null;
    float[] firstPoint = null;
    int type;
    while( !iterator.isDone() )
    {
      type = iterator.currentSegment(points);
      switch(type)
      {
        case PathIterator.SEG_CLOSE   :
        {
          if( firstPoint == null )
            break;

          if( lastPoint[0] != firstPoint[0] || lastPoint[0] != firstPoint[0] )
          {
            double direction = DirectionGetter.getDirection(lastPoint[0],lastPoint[1],firstPoint[0],firstPoint[1]);
            double distance  = DistanceGetter.getDistance(lastPoint[0],lastPoint[1],firstPoint[0],firstPoint[1]);

            Point2D.Double pnt0 = LocationGetter.getLocation(lastPoint[0],lastPoint[1],distance/4,direction);
            Point2D.Double pnt1 = LocationGetter.getLocation(lastPoint[0],lastPoint[1],(3*distance)/4,direction);

            newPath.curveTo((float)pnt0.x,(float)pnt0.y,(float)pnt1.x,(float)pnt1.y,firstPoint[0],firstPoint[1]);
          }
          lastPoint = new float[]{firstPoint[0],firstPoint[1]};
          firstPoint = null;

          newPath.closePath();

          break;
        }
        case PathIterator.SEG_CUBICTO :
        {
          lastPoint = new float[]{points[4],points[5]};

          newPath.curveTo(points[0],points[1],points[2],points[3],points[4],points[5]);
          break;
        }
        case PathIterator.SEG_LINETO  :
        {
          double direction = DirectionGetter.getDirection(lastPoint[0],lastPoint[1],points[0],points[1]);
          double distance  = DistanceGetter.getDistance(lastPoint[0],lastPoint[1],points[0],points[1]);

          Point2D.Double pnt0 = LocationGetter.getLocation(lastPoint[0],lastPoint[1],distance/4,direction);
          Point2D.Double pnt1 = LocationGetter.getLocation(lastPoint[0],lastPoint[1],(3*distance)/4,direction);

          newPath.curveTo((float)pnt0.x,(float)pnt0.y,(float)pnt1.x,(float)pnt1.y,points[0],points[1]);

          lastPoint = new float[]{points[0],points[1]};

          break;
        }
        case PathIterator.SEG_MOVETO  :
        {
          firstPoint = new float[]{points[0],points[1]};

          lastPoint = new float[]{points[0],points[1]};

          newPath.moveTo(points[0],points[1]);
          break;
        }
        case PathIterator.SEG_QUADTO  :
        {
          double direction0 = DirectionGetter.getDirection(lastPoint[0],lastPoint[1],points[0],points[1]);
          double distance0  = DistanceGetter.getDistance(lastPoint[0],lastPoint[1],points[0],points[1]);

          double direction1 = DirectionGetter.getDirection(points[2],points[3],points[0],points[1]);
          double distance1  = DistanceGetter.getDistance(points[2],points[3],points[0],points[1]);

          Point2D.Double pnt0 = LocationGetter.getLocation(lastPoint[0],lastPoint[1],2*distance0/3,direction0);
          Point2D.Double pnt1 = LocationGetter.getLocation(points[2],points[3],2*distance1/3,direction1);

          newPath.curveTo((float)pnt0.x,(float)pnt0.y,(float)pnt1.x,(float)pnt1.y,points[2],points[3]);
          lastPoint = new float[]{points[2],points[3]};

          break;
        }
      }
      iterator.next();
    }
    return newPath;
  }
}