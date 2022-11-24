/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.awt.event.MouseEvent;
import java.awt.Point;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Line2D;
import java.awt.Cursor;

public class CubicCurveTool extends Tool
{
  private Point theStartPoint;
  private Point theControlPoint;
  private Point theEndPoint;

  private boolean isDrawingLine;
  private boolean isDrawingCurve;

  public CubicCurveTool( DrawingArea aDrawingArea )
  {
    this(aDrawingArea, PaintableShape.TYPE_DRAW );
  }

  public CubicCurveTool( DrawingArea aDrawingArea, int aType )
  {
    super(aDrawingArea, aType );

    isDrawingLine  = true;
    isDrawingCurve = false;
    setCursor(OOPSCursors.CUBIC_CURVE);
  }

  public void mouseDragged( MouseEvent e )
  {
    if( !getIsStarted() )
      return;

    if( isDrawingLine )
      drawLine(e.getX(),e.getY());
    else
    {
      if( isDrawingCurve )
        drawCurve(e.getX(),e.getY());
      else
        drawCubicCurve(e.getX(),e.getY());
    }
  }

  public void mousePressed( MouseEvent e )
  {
    if( isDrawingLine )
    {
      if( !getIsStarted() )
      {
        theStartPoint = new Point(e.getX(),e.getY());
        setIsStarted(true);
      }
      else
      {
        setIsStarted(false);
        getDrawingArea().clearDrawingArea();
        isDrawingCurve = false;
        isDrawingLine = true;
      }
    }
    else
    {
      if( isDrawingCurve )
      {
        if( !getIsStarted() )
        {
          drawCurve(e.getX(),e.getY());
          setIsStarted(true);
        }
        else
        {
          setIsStarted(false);
          getDrawingArea().clearDrawingArea();
          isDrawingCurve = false;
          isDrawingLine = true;
        }
      }
      else
      {
        if( !getIsStarted() )
        {
          drawCubicCurve(e.getX(),e.getY());
          setIsStarted(true);
        }
        else
        {
          setIsStarted(false);
          getDrawingArea().clearDrawingArea();
          isDrawingCurve = false;
          isDrawingLine = true;
        }
      }
    }
  }
  public void mouseReleased( MouseEvent e )
  {
    if( !isDrawingLine )
    {
      if( isDrawingCurve )
      {
        if( !getIsStarted() )
          return;

        theControlPoint = new Point(e.getX(),e.getY());
        isDrawingCurve = false;
        setIsStarted(false);
      }
      else
      {
        if( !getIsStarted() )
          return;

        isDrawingCurve = false;
        isDrawingLine = true;
        commit();
        setIsStarted(false);
      }
    }
    else
    {
      if( !getIsStarted() )
        return;

      isDrawingLine  = false;
      isDrawingCurve = true;
      theEndPoint    = new Point(e.getX(),e.getY());
      setIsStarted(false);
    }
  }

  private void drawLine(int x, int y)
  {
    getShape().setShape(new Line2D.Double(theStartPoint.x,theStartPoint.y,x,y));
    paintShape();
  }

  private void drawCurve(int x, int y)
  {
    getShape().setShape(new QuadCurve2D.Double(theStartPoint.x,theStartPoint.y,x,y,theEndPoint.x,theEndPoint.y));
    paintShape();
  }

  private void drawCubicCurve(int x, int y)
  {
    getShape().setShape(new CubicCurve2D.Double(theStartPoint.x,theStartPoint.y,theControlPoint.x,theControlPoint.y,x,y,theEndPoint.x,theEndPoint.y));
    paintShape();
  }
}