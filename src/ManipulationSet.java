/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.util.Vector;
import java.awt.geom.PathIterator;
import java.awt.Shape;
import java.awt.Point;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.GeneralPath;

public class ManipulationSet
{
  private Vector theManipulators;
  private Shape  theShape;

  private int theWindingRule;

  private float theSize;

  public ManipulationSet( Shape aShape, float aSize )
  {
    theSize = aSize;
    theShape = aShape;
    createManipulators();
  }
 
  public int getWindRule()
  {
    return theWindingRule;
  }

  public void setWindRule( int aRule )
  {
    theWindingRule = aRule;
  }

  public void setSize( float aSize )
  {
    theSize = aSize;
    for( int i = 0 ; i < theManipulators.size() ; i++ )
      ((Manipulator)theManipulators.get(i)).setSize(theSize);
  }

  public void setMirrored( boolean aBool )
  {
    for( int i = 0 ; i < theManipulators.size() ; i++ )
      ((Manipulator)theManipulators.get(i)).setMirrored(aBool);
  }

  public void setManipulators( boolean small, boolean mid, boolean big )
  {
    for( int i = 0 ; i < theManipulators.size() ; i++ )
      ((Manipulator)theManipulators.get(i)).setManipulators(small,mid,big);
  }

  public void createManipulators()
  {
    theManipulators = new Vector();

    PathIterator iterator = theShape.getPathIterator(null);
    theWindingRule = iterator.getWindingRule();

    float[] points = new float[6];
    float[] lastPoint = null;
    float[] lastControl = null;
    Manipulator currentManipulator;
    Manipulator firstManipulator = null;
    int type;
    while( !iterator.isDone() )
    {
      type = iterator.currentSegment(points);
      switch(type)
      {
        case PathIterator.SEG_CLOSE   :
        {
          if( lastControl != null && lastPoint != null )
            theManipulators.add(new Manipulator(theSize,new Point2D.Float(lastControl[0],lastControl[1]), new Point2D.Float(lastPoint[0],lastPoint[1]), null ));

          if( firstManipulator != null  && theManipulators.size() > 0 )
          {
            lastControl = null;
            lastPoint = null;

            firstManipulator.link((Manipulator)theManipulators.get(theManipulators.size()-1));
            ((Manipulator)theManipulators.get(theManipulators.size()-1)).link(firstManipulator);
            firstManipulator = null;
          }
          break;
        }
        case PathIterator.SEG_CUBICTO :
        {
          if( lastControl == null )
            currentManipulator = new Manipulator(theSize,null, new Point2D.Float(lastPoint[0],lastPoint[1]), new Point2D.Float(points[0],points[1]) );
          else
            currentManipulator = new Manipulator(theSize,new Point2D.Float(lastControl[0],lastControl[1]), new Point2D.Float(lastPoint[0],lastPoint[1]), new Point2D.Float(points[0],points[1]) );

          theManipulators.add(currentManipulator);
          if( firstManipulator == null )
            firstManipulator = currentManipulator;

          lastPoint = new float[]{points[4],points[5]};
          lastControl = new float[]{points[2],points[3]};
          break;
        }
        case PathIterator.SEG_LINETO  :
        {
          break;
        }
        case PathIterator.SEG_MOVETO  :
        {
          if( lastControl != null && lastPoint != null )
            theManipulators.add(new Manipulator(theSize,new Point2D.Float(lastControl[0],lastControl[1]), new Point2D.Float(lastPoint[0],lastPoint[1]), null ));
          
          firstManipulator = null;

          lastPoint = new float[]{points[0],points[1]};
          lastControl = null;

          break;
        }
        case PathIterator.SEG_QUADTO  :
        {
          break;
        }
      }
      iterator.next();
    }
    if( lastControl != null && lastPoint != null )
      theManipulators.add(new Manipulator(theSize,new Point2D.Float(lastControl[0],lastControl[1]), new Point2D.Float(lastPoint[0],lastPoint[1]), null ));
  }

  public void drag( Point2D.Float aPoint, double x, double y )
  {
    for( int i = 0 ; i < theManipulators.size() ; i++ )
    {
      if( ((Manipulator)theManipulators.get(i)).holds(aPoint) )
      {
        ((Manipulator)theManipulators.get(i)).drag(aPoint,x,y);
        break;
      }
    }
  }

  public Shape recreateShape()
  {
    PathIterator iterator = theShape.getPathIterator(null);

    GeneralPath newPath = new GeneralPath(theWindingRule);

    Vector controlPoints = new Vector();
    Vector endPoints = new Vector();
    Manipulator currentManipulator;
    for( int i = 0 ; i < theManipulators.size() ; i++ )
    {
      currentManipulator = (Manipulator)theManipulators.get(i);
      if( currentManipulator.thePoint0 != null )
        controlPoints.add(currentManipulator.thePoint0);
      if( currentManipulator.thePoint2 != null )
        controlPoints.add(currentManipulator.thePoint2);
      endPoints.add(currentManipulator.thePoint1);
    }

    float[] points = new float[6];
    Point2D.Float control0;
    Point2D.Float control1;
    Point2D.Float endPoint;
    int type;
    while( !iterator.isDone() )
    {
      type = iterator.currentSegment(points);
      switch(type)
      {
        case PathIterator.SEG_CLOSE   :
        {
          newPath.closePath();
          break;
        }
        case PathIterator.SEG_CUBICTO :
        {
          control0 = ((Point2D.Float)controlPoints.remove(0));
          control1 = ((Point2D.Float)controlPoints.remove(0));
          endPoint = ((Point2D.Float)endPoints.remove(0));
          newPath.curveTo(control0.x,control0.y,control1.x,control1.y,endPoint.x,endPoint.y);
          break;
        }
        case PathIterator.SEG_LINETO  :
        {
          newPath.lineTo(points[0],points[1]);
          break;
        }
        case PathIterator.SEG_MOVETO  :
        {
          endPoint = ((Point2D.Float)endPoints.remove(0));
          newPath.moveTo(endPoint.x,endPoint.y);
          break;
        }
        case PathIterator.SEG_QUADTO  :
        {
          newPath.quadTo(points[0],points[1],points[2],points[3]);
          break;
        }
      }
      iterator.next();
    }
    theShape = newPath;
    return theShape;
  }

  public Point2D.Float getPoint( Point aPoint )
  {
    Point2D.Float pnt = null;

    for( int i = 0 ; i < theManipulators.size() ; i++ )
    {
      pnt = ((Manipulator)theManipulators.get(i)).getPoint(aPoint);
      if( pnt != null )
        return pnt;
    }

    return null;
  }

  public void paint( Graphics2D aG2D )
  {
    for( int i = 0 ; i < theManipulators.size() ; i++ )
      ((Manipulator)theManipulators.get(i)).paint(aG2D);
  }
}