/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import java.awt.Point;
import javax.swing.JComponent;
import java.awt.Cursor;

public class SmoothPolygonTool extends Tool implements Curveable
{
  private SmoothPath  thePath;
  private GeneralPath theFinalPath;
  private Point       theLastPoint;

  private double thePadding   = 0.45;
  private double theThreshold = 14;

  private static SmoothPolygonToolbar theToolbar = new SmoothPolygonToolbar();

  private boolean     isInited = false;
  private boolean     isLosingControl = false;
  private boolean     shouldClose = true;

  public SmoothPolygonTool( DrawingArea aDrawingArea )
  {
    this(aDrawingArea,PaintableShape.TYPE_DRAW);
  }

  public SmoothPolygonTool( DrawingArea aDrawingArea, int aType )
  {
    super(aDrawingArea,aType);
    theToolbar.setTool(this);
    setCursor(OOPSCursors.SMOOTH_POLYGON);
  }

  public void mouseDragged( MouseEvent e )
  {
    if( !getIsStarted() )
      return;

    getShape().setShape(thePath.getWithLastPointsCurved(e.getX(),e.getY()));
    paintShape();
  }

  public void mousePressed( MouseEvent e )
  {
    if( getIsStarted() )
    {
      setIsStarted(false);
      getDrawingArea().clearDrawingArea();
      thePath        = null;
      isInited = false;
      return;
    }
    setIsStarted(true);
    if( isInited )
    {
      if( e.getClickCount() == 2 )
      {
        loseControl();
        isLosingControl = true;
        isInited = false;
        setIsStarted(false);
      }
    }
    else
    {
      thePath = new SmoothPath(thePadding, theThreshold);
      thePath.start(e.getX(),e.getY());
      isInited = true;
    }
  }

  public void mouseReleased( MouseEvent e )
  {
    if( isLosingControl )
    {
      isLosingControl = false;
      return;
    }

    if( !getIsStarted() )
      return;

    if( isInited )
    {
      drawCurve(e.getX(),e.getY());
      setIsStarted(false);
    }
  }

  public void loseControl()
  {
    if( thePath == null )
      return;

    if( shouldClose )
    {
      thePath.addPoint(thePath.getStartPoint().x,thePath.getStartPoint().y,true);
      thePath.closePath();
      getShape().setShape(thePath.getPath());
    }

    commit();
    thePath = null;
    setIsStarted(false);
  }

  private void drawCurve(int x, int y)
  {
    theFinalPath = thePath.getWithLastPointsCurved(x,y);
    getShape().setShape(theFinalPath);
    thePath.addPoint(x,y,true);
    paintShape();
  }

  public void setShouldClose(boolean aShouldClose)
  {
    shouldClose = aShouldClose;
  }

  public JComponent getToolbar()
  {
    return theToolbar;
  }

  public void setCurveSettings( double aPadding, double aThreshold )
  {
    thePadding   = aPadding;
    theThreshold = aThreshold;
    if( thePath != null )
      thePath.setSettings(thePadding,theThreshold);
  }
}