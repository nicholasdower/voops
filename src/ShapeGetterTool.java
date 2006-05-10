/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.awt.event.MouseEvent;
import java.util.Vector;
import java.awt.Cursor;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;

public class ShapeGetterTool extends Tool
{
  private boolean shouldFill   = true;
  private boolean shouldStroke = false;
  private boolean strokeColor  = false;

  private OOPS theOOPS;

  private static ShapeGetterToolbar theToolbar = new ShapeGetterToolbar();

  public ShapeGetterTool( DrawingArea aDrawingArea, OOPS anOOPS )
  {
    super(aDrawingArea, PaintableShape.TYPE_FILL);
    theOOPS = anOOPS;
    setCursor(OOPSCursors.GETTER);
    theToolbar.setTool(this);
  }

  public void mouseReleased( MouseEvent e )
  {
    PaintableShape shape = getDrawingArea().getFirstShapeAt(new Point2D.Double(e.getX(),e.getY()));
    if( shape != null )
    {
      double oldZoom = getDrawingArea().getZoom();
      getDrawingArea().setZoom(1);
      if( shouldFill )
      {
        theOOPS.setFillPaint(shape.getFillPaint());
      }
      if( shouldStroke )
      {
        theOOPS.setStroke(shape.getStroke());
      }
      if( strokeColor )
      {
        theOOPS.setDrawPaint(shape.getDrawPaint());
      }
      getDrawingArea().setZoom(oldZoom);
    }
    getDrawingArea().repaint();
  }

  public void setFillType( boolean fill, boolean stroke, boolean color )
  {
    shouldFill   = fill;
    shouldStroke = stroke;
    strokeColor  = color;
  }

  public JComponent getToolbar()
  {
    return theToolbar;
  }
}