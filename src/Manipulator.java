/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.awt.geom.Point2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.AffineTransform;
import java.awt.Shape;
import java.awt.BasicStroke;
import java.awt.Point;
import java.awt.Color;
import java.awt.Graphics2D;

public class Manipulator
{
  public float theSize;
  public float theInnerSize;

  public Point2D.Float thePoint0;
  public Point2D.Float thePoint1;
  public Point2D.Float thePoint2;

  public Point2D.Float theInnerPoint0;
  public Point2D.Float theInnerPoint1;

  public Shape theResizor0;
  public Shape theResizor1;
  public Shape theResizor2;

  public Shape theInnerResizor0;
  public Shape theInnerResizor1;

  public double theDirection0 = -1;
  public double theDirection2 = -1;

  public double theDistance0 = -1;
  public double theDistance2 = -1;

  public Color theOuterColor = new Color(255,30,0,100);
  public Color theInnerColor = new Color(0,0,255,100);
  public Color thePointColor = new Color(0,255,0,100);

  public Color theStrokeColor = new Color(0,0,0,150);

  public BasicStroke theStroke = new BasicStroke(1f);

  public int       theMirrorer          = 1;
  public boolean[] theWhichManipulators = new boolean[]{true,true,true};

  public Manipulator theLinkedManipulator;

  public Manipulator( float aSize, Point2D.Float aPnt0, Point2D.Float aPnt1, Point2D.Float aPnt2 )
  {
    theSize = aSize;
    theInnerSize = (float)(theSize/2);

    thePoint0 = aPnt0;
    thePoint1 = aPnt1;
    thePoint2 = aPnt2;

    if( thePoint0 != null )
    {
      double dir = DirectionGetter.getDirection(thePoint1.x,thePoint1.y,thePoint0.x,thePoint0.y);
      double dis = DistanceGetter.getDistance(thePoint1.x,thePoint1.y,thePoint0.x,thePoint0.y);
      Point2D.Double pnt = LocationGetter.getLocation(thePoint1.x,thePoint1.y,dis/2,dir);
      theInnerPoint0 = new Point2D.Float((float)pnt.x,(float)pnt.y);

      theDirection0 = dir;
      theDistance0 = dis;
    }

    if( thePoint2 != null )
    {
      double dir = DirectionGetter.getDirection(thePoint1.x,thePoint1.y,thePoint2.x,thePoint2.y);
      double dis = DistanceGetter.getDistance(thePoint1.x,thePoint1.y,thePoint2.x,thePoint2.y);
      Point2D.Double pnt = LocationGetter.getLocation(thePoint1.x,thePoint1.y,dis/2,dir);
      theInnerPoint1 = new Point2D.Float((float)pnt.x,(float)pnt.y);

      theDirection2 = dir;
      theDistance2 = dis;
    }

    createResizors();
  }

  public void createResizors()
  {
    theResizor1 = new Ellipse2D.Float(thePoint1.x-theInnerSize,thePoint1.y-theInnerSize,theInnerSize*2,theInnerSize*2);

    if( thePoint0 != null )
    {
      theResizor0 = new Ellipse2D.Float(thePoint0.x-theSize,thePoint0.y-theSize,theSize*2,theSize*2);
      theInnerResizor0 = new Ellipse2D.Float(theInnerPoint0.x-theInnerSize,theInnerPoint0.y-theInnerSize,theInnerSize*2,theInnerSize*2);
    }

    if( thePoint2 != null )
    {
      theResizor2 = new Ellipse2D.Float(thePoint2.x-theSize,thePoint2.y-theSize,theSize*2,theSize*2);
      theInnerResizor1 = new Ellipse2D.Float(theInnerPoint1.x-theInnerSize,theInnerPoint1.y-theInnerSize,theInnerSize*2,theInnerSize*2);
    }
  }

  public void setSize( float aSize )
  {
    theSize = aSize;
    theInnerSize = (float)(theSize/2);
    createResizors();
  }

  public void setMirrored( boolean aBool )
  {
    theMirrorer = aBool ? -1 : 1;
  }

  public void setManipulators( boolean small, boolean mid, boolean big )
  {
    theWhichManipulators = new boolean[]{small,mid,big};
  }

  public void paint( Graphics2D aG2D )
  {
    aG2D.setStroke(theStroke);

    if( theWhichManipulators[0] )
    {
      aG2D.setColor(thePointColor);
      aG2D.fill(theResizor1);
      aG2D.setColor(theStrokeColor);
      aG2D.draw(theResizor1);
    }

    if( theInnerPoint0 != null && theWhichManipulators[1] )
    {
      aG2D.setColor(theInnerColor);
      aG2D.fill(theInnerResizor0);
      aG2D.setColor(theStrokeColor);
      aG2D.draw(theInnerResizor0);
    }
    if( theInnerPoint1 != null && theWhichManipulators[1] )
    {
      aG2D.setColor(theInnerColor);
      aG2D.fill(theInnerResizor1);
      aG2D.setColor(theStrokeColor);
      aG2D.draw(theInnerResizor1);
    }

    if( thePoint0 != null && theWhichManipulators[2] )
    {
      aG2D.setColor(theOuterColor);
      aG2D.fill(theResizor0);
      aG2D.setColor(theStrokeColor);
      aG2D.draw(theResizor0);
    }

    if( thePoint2 != null && theWhichManipulators[2] )
    {
      aG2D.setColor(theOuterColor);
      aG2D.fill(theResizor2);
      aG2D.setColor(theStrokeColor);
      aG2D.draw(theResizor2);
    }
  }

  public void link( Manipulator aManipulator )
  {
    theLinkedManipulator = aManipulator;
  }

  public void drag( Point2D.Float aPoint, double x, double y )
  {

    if( thePoint0 != null && aPoint.equals(thePoint0) )
    {
      thePoint0.x += x;
      thePoint0.y += y;

      double dir0 = DirectionGetter.getDirection(thePoint1.x,thePoint1.y,thePoint0.x,thePoint0.y);
      double dis0 = DistanceGetter.getDistance(thePoint1.x,thePoint1.y,thePoint0.x,thePoint0.y);

      Point2D.Double innerPnt0 = LocationGetter.getLocation(thePoint1.x,thePoint1.y,dis0/2,dir0);
      theInnerPoint0 = new Point2D.Float((float)innerPnt0.x,(float)innerPnt0.y);

      if( thePoint2 != null )
      {
        double newDir = theDirection2 + theMirrorer*(dir0 - theDirection0);
        double newDis = theDistance2*(dis0/theDistance0);

        Point2D.Double pnt2      = LocationGetter.getLocation(thePoint1.x,thePoint1.y,newDis,newDir);
        Point2D.Double innerPnt1 = LocationGetter.getLocation(thePoint1.x,thePoint1.y,newDis/2,newDir);

        thePoint2      = new Point2D.Float((float)pnt2.x,(float)pnt2.y);
        theInnerPoint1 = new Point2D.Float((float)innerPnt1.x,(float)innerPnt1.y);

        theDirection2 = newDir;
        theDistance2  = newDis;
      }
      else if( theLinkedManipulator != null )
      {
        double newDir = theLinkedManipulator.theDirection2 + theMirrorer*(dir0 - theDirection0);
        double newDis = theLinkedManipulator.theDistance2*(dis0/theDistance0);

        Point2D.Double pnt2      = LocationGetter.getLocation(theLinkedManipulator.thePoint1.x,theLinkedManipulator.thePoint1.y,newDis,newDir);
        Point2D.Double innerPnt1 = LocationGetter.getLocation(theLinkedManipulator.thePoint1.x,theLinkedManipulator.thePoint1.y,newDis/2,newDir);

        theLinkedManipulator.thePoint2      = new Point2D.Float((float)pnt2.x,(float)pnt2.y);
        theLinkedManipulator.theInnerPoint1 = new Point2D.Float((float)innerPnt1.x,(float)innerPnt1.y);

        theLinkedManipulator.theDirection2 = newDir;
        theLinkedManipulator.theDistance2  = newDis;

        theLinkedManipulator.createResizors();
      }

      theDirection0 = dir0;
      theDistance0  = dis0;
    }
    else if( thePoint2 != null && aPoint.equals(thePoint2) )
    {
      thePoint2.x += x;
      thePoint2.y += y;

      double dir2 = DirectionGetter.getDirection(thePoint1.x,thePoint1.y,thePoint2.x,thePoint2.y);
      double dis2 = DistanceGetter.getDistance(thePoint1.x,thePoint1.y,thePoint2.x,thePoint2.y);

      Point2D.Double innerPnt1 = LocationGetter.getLocation(thePoint1.x,thePoint1.y,dis2/2,dir2);
      theInnerPoint1 = new Point2D.Float((float)innerPnt1.x,(float)innerPnt1.y);

      if( thePoint0 != null )
      {
        double newDir = theDirection0 + theMirrorer*(dir2 - theDirection2);
        double newDis = theDistance0*(dis2/theDistance2);

        Point2D.Double pnt0      = LocationGetter.getLocation(thePoint1.x,thePoint1.y,newDis,newDir);
        Point2D.Double innerPnt0 = LocationGetter.getLocation(thePoint1.x,thePoint1.y,newDis/2,newDir);

        thePoint0      = new Point2D.Float((float)pnt0.x,(float)pnt0.y);
        theInnerPoint0 = new Point2D.Float((float)innerPnt0.x,(float)innerPnt0.y);

        theDirection0 = newDir;
        theDistance0  = newDis;
      }
      else if( theLinkedManipulator != null )
      {
        double newDir = theLinkedManipulator.theDirection0 + theMirrorer*(dir2 - theDirection2);
        double newDis = theLinkedManipulator.theDistance0*(dis2/theDistance2);

        Point2D.Double pnt0      = LocationGetter.getLocation(theLinkedManipulator.thePoint1.x,theLinkedManipulator.thePoint1.y,newDis,newDir);
        Point2D.Double innerPnt0 = LocationGetter.getLocation(theLinkedManipulator.thePoint1.x,theLinkedManipulator.thePoint1.y,newDis/2,newDir);

        theLinkedManipulator.thePoint0      = new Point2D.Float((float)pnt0.x,(float)pnt0.y);
        theLinkedManipulator.theInnerPoint0 = new Point2D.Float((float)innerPnt0.x,(float)innerPnt0.y);

        theLinkedManipulator.theDirection0 = newDir;
        theLinkedManipulator.theDistance0  = newDis;

        theLinkedManipulator.createResizors();
      }

      theDirection2 = dir2;
      theDistance2  = dis2;
    }
    else if( theInnerPoint0 != null && aPoint.equals(theInnerPoint0) )
    {
      theInnerPoint0.x += x;
      theInnerPoint0.y += y;

      double dir0 = DirectionGetter.getDirection(thePoint1.x,thePoint1.y,theInnerPoint0.x,theInnerPoint0.y);
      double dis0 = DistanceGetter.getDistance(thePoint1.x,thePoint1.y,theInnerPoint0.x,theInnerPoint0.y);

      Point2D.Double pnt0 = LocationGetter.getLocation(thePoint1.x,thePoint1.y,dis0*2,dir0);
      thePoint0 = new Point2D.Float((float)pnt0.x,(float)pnt0.y);

      theDirection0 = dir0;
      theDistance0  = dis0*2;
    }
    else if( theInnerPoint1 != null && aPoint.equals(theInnerPoint1) )
    {
      theInnerPoint1.x += x;
      theInnerPoint1.y += y;

      double dir2 = DirectionGetter.getDirection(thePoint1.x,thePoint1.y,theInnerPoint1.x,theInnerPoint1.y);
      double dis2 = DistanceGetter.getDistance(thePoint1.x,thePoint1.y,theInnerPoint1.x,theInnerPoint1.y);

      Point2D.Double pnt2 = LocationGetter.getLocation(thePoint1.x,thePoint1.y,dis2*2,dir2);
      thePoint2 = new Point2D.Float((float)pnt2.x,(float)pnt2.y);

      theDirection2 = dir2;
      theDistance2  = dis2*2;
    }
    else if( aPoint.equals(thePoint1) )
    {
      thePoint1.x += x;
      thePoint1.y += y;

      if( thePoint0 != null )
      {
        thePoint0.x += x;
        thePoint0.y += y;

        theInnerPoint0.x += x;
        theInnerPoint0.y += y;
      }

      if( thePoint2 != null )
      {
        thePoint2.x += x;
        thePoint2.y += y;

        theInnerPoint1.x += x;
        theInnerPoint1.y += y;
      }

      if( theLinkedManipulator != null )
      {
        theLinkedManipulator.thePoint1.x += x;
        theLinkedManipulator.thePoint1.y += y;

        if( theLinkedManipulator.thePoint0 != null )
        {
          theLinkedManipulator.thePoint0.x += x;
          theLinkedManipulator.thePoint0.y += y;

          theLinkedManipulator.theInnerPoint0.x += x;
          theLinkedManipulator.theInnerPoint0.y += y;
        }

        if( theLinkedManipulator.thePoint2 != null )
        {
          theLinkedManipulator.thePoint2.x += x;
          theLinkedManipulator.thePoint2.y += y;

          theLinkedManipulator.theInnerPoint1.x += x;
          theLinkedManipulator.theInnerPoint1.y += y;
        }
        theLinkedManipulator.createResizors();
      }
    }
    else
      return;

    createResizors();
  } 

  public boolean holds( Point2D.Float aPoint )
  {
    if( aPoint.equals(thePoint0) )
      return true;
    else if( aPoint.equals(thePoint1) )
      return true;
    else if( aPoint.equals(thePoint2) )
      return true;
    else if( aPoint.equals(theInnerPoint0) )
      return true;
    else if( aPoint.equals(theInnerPoint1) )
      return true;

    return false;
  } 

  public Point2D.Float getPoint( Point aPoint )
  {
    if( theResizor0 != null && theWhichManipulators[2] )
      if( theResizor0.contains(aPoint.x,aPoint.y) )
        return thePoint0;

    if( theResizor1 != null && theWhichManipulators[0] )
      if( theResizor1.contains(aPoint.x,aPoint.y) )
        return thePoint1;

    if( theResizor2 != null && theWhichManipulators[2] )
      if( theResizor2.contains(aPoint.x,aPoint.y) )
        return thePoint2;

    if( theInnerResizor0 != null && theWhichManipulators[1] )
      if( theInnerResizor0.contains(aPoint.x,aPoint.y) )
        return theInnerPoint0;

    if( theInnerResizor1 != null && theWhichManipulators[1] )
      if( theInnerResizor1.contains(aPoint.x,aPoint.y) )
        return theInnerPoint1;

    return null;
  }
}