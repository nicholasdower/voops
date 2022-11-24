/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import javax.swing.JComponent;
import java.awt.Cursor;

public class PenTool extends Tool implements Curveable
{
  private SmoothPath  thePath;
  private GeneralPath theFinalPath;

  private int thePointCount = 0;

  private double thePadding   = 0.45;
  private double theThreshold = 14;

  private static SmoothCurveToolbar theToolbar = new SmoothCurveToolbar();

  public PenTool( DrawingArea aDrawingArea )
  {
    this(aDrawingArea,PaintableShape.TYPE_DRAW);
  }

  public PenTool( DrawingArea aDrawingArea, int aType )
  {
    super(aDrawingArea,aType);
    theToolbar.setTool(this);
    setCursor(OOPSCursors.PEN);
  }

  public void mouseDragged( MouseEvent e )
  {
    if( !getIsStarted() )
      return;

    if( (++thePointCount)%1== 0 )
      drawCurve(e.getX(),e.getY());
  }

  public void mousePressed( MouseEvent e )
  {
    if( !getIsStarted() )
    {
      setIsStarted(true);
      thePath = new SmoothPath(thePadding, theThreshold*getDrawingArea().getZoom());
      thePath.start(e.getX(),e.getY());
    }
    else
    {
      thePath = null;
      setIsStarted(false);
      thePointCount = 0;
      getDrawingArea().clearDrawingArea();
    }
  }

  public void mouseReleased( MouseEvent e )
  {
    if( !getIsStarted() )
      return;

    getShape().setShape(theFinalPath);
    commit();
    thePath = null;
    thePointCount = 0;
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