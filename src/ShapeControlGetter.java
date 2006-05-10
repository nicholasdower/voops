/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.awt.Shape;
import java.awt.geom.PathIterator;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.Vector;

public class ShapeControlGetter
{
  private ShapeControlGetter(){}

  public static Vector getControls( Shape aShape )
  {
    PathIterator iterator = aShape.getPathIterator(null);

    GeneralPath newPath = new GeneralPath(iterator.getWindingRule());

    float[] points = new float[6];
    Vector controls = new Vector();
    int type;
    while( !iterator.isDone() )
    {
      type = iterator.currentSegment(points);
      switch(type)
      {
        case PathIterator.SEG_CLOSE   :
        {
          break;
        }
        case PathIterator.SEG_CUBICTO :
        {
          controls.add(new Point2D.Float(points[0],points[1]));
          controls.add(new Point2D.Float(points[2],points[3]));
          break;
        }
        case PathIterator.SEG_LINETO  :
        {
          break;
        }
        case PathIterator.SEG_MOVETO  :
        {
          break;
        }
        case PathIterator.SEG_QUADTO  :
        {
          break;
        }
      }
      iterator.next();
    }
    return controls;
  }
}