/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import javax.swing.JComponent;
import java.awt.Cursor;

public class PolygonPenTool extends Tool implements Curveable
{
  private SmoothPath  thePath;
  private GeneralPath theFinalPath;
  private boolean     shouldClose = true;

  private double thePadding   = 0.45;
  private double theThreshold = 14;

  private static SmoothCurveToolbar theToolbar = new SmoothCurveToolbar();

  public PolygonPenTool( DrawingArea aDrawingArea )
  {
    this(aDrawingArea,PaintableShape.TYPE_DRAW);
  }

  public PolygonPenTool( DrawingArea aDrawingArea, int aType )
  {
    super(aDrawingArea,aType);
    theToolbar.setTool(this);
    setCursor(OOPSCursors.POLYGON_PEN);
  }

  public void mouseDragged( MouseEvent e )
  {
    if( !getIsStarted() )
      return;

    drawCurve(e.getX(),e.getY());
  }

  public void mousePressed( MouseEvent e )
  {
    if( !getIsStarted() )
    {
      thePath = new SmoothPath(thePadding, theThreshold*getDrawingArea().getZoom());
      thePath.start(e.getX(),e.getY());

      setIsStarted(true);
    }
    else
    {
      setIsStarted(false);
      thePath = null;
      getDrawingArea().clearDrawingArea();
    }
  }

  public void mouseReleased( MouseEvent e )
  {
    if( !getIsStarted() )
      return;

    if( shouldClose )
    {
      thePath.addPoint(e.getX(),e.getY(),true);
      thePath.closePath();
      getShape().setShape(thePath.getPath());
    }
    else
    {
      getShape().setShape(theFinalPath);
    }
    commit();
    thePath = null;
    setIsStarted(false);
  }

  private void drawCurve(int x, int y)
  {
    theFinalPath = thePath.getWithLastPointsCurved(x,y);
    getShape().setShape(theFinalPath);
    thePath.addPoint(x,y);
    paintShape();
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
      thePath.setSettings(thePadding,theThreshold*getDrawingArea().getZoom());
  }
}