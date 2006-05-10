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

public class FillTool extends Tool
{
  private boolean shouldFill   = true;
  private boolean shouldStroke = false;
  private boolean strokeColor  = false;

  private static FillToolbar theToolbar = new FillToolbar();

  public FillTool( DrawingArea aDrawingArea )
  {
    super(aDrawingArea, PaintableShape.TYPE_FILL);
    setCursor(OOPSCursors.FILL);
    theToolbar.setTool(this);
  }

  public void mouseReleased( MouseEvent e )
  {
    PaintableShape shape = getDrawingArea().getFirstShapeAt(new Point2D.Double(e.getX(),e.getY()));
    if( shape != null )
    {
      getDrawingArea().setUndoPoint(new FillUndo(getDrawingArea(),shape));
      if( shouldStroke )
      {
        shape.setStroke(getShape().getStroke());

        Rectangle2D.Double bounds = new Rectangle2D.Double();
          bounds.setRect(shape.getFillShape().getBounds2D());

        if( bounds.width != 0 && bounds.height != 0 ) 
          shape.setShape(getShape().getFillShape());
      }
      if( shouldFill )
      {
        try
        {
          if( getShape().getFillPaint().getClass().equals(Class.forName("TransformableTexturePaint")) )
          {
            shape.setFillPaint((TransformableTexturePaint)((TransformableTexturePaint)getShape().getFillPaint()).clone());
            if( !e.isShiftDown() )
            {
              int kind = ((TransformableTexturePaint)getShape().getFillPaint()).getKind();
              if( kind == TransformableTexturePaint.KIND_RADIAL || kind == TransformableTexturePaint.KIND_RECTANGULAR )
              {
                TransformableTexturePaint paint = (TransformableTexturePaint)((TransformableTexturePaint)getShape().getFillPaint()).clone();
  
                Rectangle2D.Double rect = new Rectangle2D.Double();
                rect.setRect(shape.getFillShape().getBounds2D());

                double height = Math.abs(e.getY()-rect.y);
                       height = Math.max(height, Math.abs(e.getY()-(rect.y+rect.height)));
  
                double width = Math.abs(e.getX()-rect.x);
                       width = Math.max(width, Math.abs(e.getX()-(rect.x+rect.width)));

                rect = new Rectangle2D.Double(e.getX()-width,e.getY()-height,width*2,height*2);

                ((TransformableTexturePaint)shape.getFillPaint()).setAnchor(rect);
              }
            }
          }
          else
            shape.setFillPaint(getShape().getFillPaint());
        }
        catch( ClassNotFoundException cnfe )
        {

        }
      }
      if( strokeColor )
        shape.setDrawPaint(getShape().getDrawPaint());
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