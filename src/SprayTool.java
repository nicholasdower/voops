/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.event.MouseEvent;
import java.lang.Runnable;
import java.lang.Thread;
import java.awt.geom.Point2D;
import java.util.Random;

public class SprayTool extends Tool implements Runnable
{
  private Point  thePoint;
  private double theRadius    = 10;
  private Random theRandomGenerator;

  private static final double ANG360 = 3.14*2;

  private Thread theSprayThread;

  public SprayTool( DrawingArea aDrawingArea )
  {
    super(aDrawingArea,PaintableShape.TYPE_FILL);
    theRandomGenerator = new Random();
  }

  public void setRadius( double aRadius )
  {
    theRadius = aRadius;
  }

  public void run()
  {
    Thread myThread = Thread.currentThread();

    while( theSprayThread == myThread )
    {
      drawBead();

      try
      {
        Thread.sleep(1);
      }
      catch( InterruptedException ie )
      {
        System.out.println(ie);
      }
    }
  }

  public void mouseDragged( MouseEvent e )
  {
    thePoint = new Point(e.getX(),e.getY());
  }

  public void mousePressed( MouseEvent e )
  {
    thePoint = new Point(e.getX(),e.getY());
     
    theSprayThread = new Thread(this,"Spray");
    theSprayThread.start();
  }

  public void mouseReleased( MouseEvent e )
  {
    theSprayThread = null;
  }

  private void drawBead()
  {
    Point2D.Double aPoint = LocationGetter.getLocation(thePoint.x,thePoint.y,theRandomGenerator.nextDouble()*theRadius,theRandomGenerator.nextDouble()*ANG360);
    getShape().setShape(new Ellipse2D.Double(aPoint.x,aPoint.y,1,1));
    commit();
  }
}