/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.awt.Shape;
import java.awt.geom.PathIterator;
import java.awt.geom.GeneralPath;

public class ShapeIO
{
  private static final int PATH_DONE = 394123;

  public static void writeShape( ObjectOutputStream anOOS, Shape aShape ) throws java.io.IOException
  {
    PathIterator iterator = aShape.getPathIterator(null);

    anOOS.writeInt(iterator.getWindingRule());

    float[] points = new float[6];
    int type;
    while( !iterator.isDone() )
    {
      type = iterator.currentSegment(points);
      switch(type)
      {
        case PathIterator.SEG_CLOSE   : anOOS.writeInt(PathIterator.SEG_CLOSE);                                break;
        case PathIterator.SEG_CUBICTO : anOOS.writeInt(PathIterator.SEG_CUBICTO); writePoints(anOOS,points,6); break;
        case PathIterator.SEG_LINETO  : anOOS.writeInt(PathIterator.SEG_LINETO);  writePoints(anOOS,points,2); break;
        case PathIterator.SEG_MOVETO  : anOOS.writeInt(PathIterator.SEG_MOVETO);  writePoints(anOOS,points,2); break;
        case PathIterator.SEG_QUADTO  : anOOS.writeInt(PathIterator.SEG_QUADTO);  writePoints(anOOS,points,4); break;
      }
      iterator.next();
    }
    anOOS.writeInt(PATH_DONE);
  }

  private static void writePoints( ObjectOutputStream anOOS, float[] somePoints, int aSize ) throws java.io.IOException
  {
    for( int i = 0 ; i < aSize ; i++ )
      anOOS.writeFloat(somePoints[i]);
  }

  private static float[] readPoints( ObjectInputStream anOIS, int aSize ) throws java.io.IOException
  {
    float[] points = new float[aSize];
    for( int i = 0 ; i < aSize ; i++ )
      points[i] = anOIS.readFloat();
    return points;
  }

  public static Shape readShape( ObjectInputStream anOIS ) throws java.io.IOException
  {
    GeneralPath path = new GeneralPath(anOIS.readInt());
    int type = anOIS.readInt();
    float[] points;
    while( type != PATH_DONE )
    {
      switch(type)
      {
        case PathIterator.SEG_CLOSE   :                               
                                        path.closePath();               
                                        break;
        case PathIterator.SEG_CUBICTO : points = readPoints(anOIS,6);
                                        path.curveTo(points[0],points[1],points[2],points[3],points[4],points[5]);
                                        break;
        case PathIterator.SEG_LINETO  : points = readPoints(anOIS,2);
                                        path.lineTo(points[0],points[1]);
                                        break;
        case PathIterator.SEG_MOVETO  : points = readPoints(anOIS,2);
                                        path.moveTo(points[0],points[1]);
                                        break;
        case PathIterator.SEG_QUADTO  : points = readPoints(anOIS,4);
                                        path.quadTo(points[0],points[1],points[2],points[3]);
                                        break;
      }
      type = anOIS.readInt();
    }
    return path;
  }
}