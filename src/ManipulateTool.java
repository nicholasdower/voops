/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.awt.event.MouseEvent;
import java.util.Vector;
import java.awt.Cursor;
import java.awt.Shape;
import java.awt.Color;
import java.awt.Point;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.AffineTransform;
import javax.swing.JComponent;

public class ManipulateTool extends Tool
{
  private boolean shouldRedraw   = true;
  private float   theSize = 5.5f;

  private PaintableShape theCurrentShape;
  private ManipulationSet theManipulationSet;

  private Point2D.Float theCurrentPoint;

  private Point theDragPoint;

  private boolean   theIsMirrored = false;
  private boolean[] theWhichManipulators = new boolean[]{true,true,true};
  private int       theWindingRule;

  private static ManipulateToolbar theToolbar = new ManipulateToolbar();

  public ManipulateTool( DrawingArea aDrawingArea )
  {
    super(aDrawingArea, PaintableShape.TYPE_FILL);
    setCursor(OOPSCursors.MOVE_UP);
    theToolbar.setTool(this);
  }

  public void setWindRule( int aRule )
  {
    theWindingRule = aRule;
    if( theManipulationSet != null )
    {
      theManipulationSet.setWindRule(theWindingRule);
      if( shouldRedraw )
        theCurrentShape.setShape(theManipulationSet.recreateShape());
      else
        theCurrentShape.setFillShape(theManipulationSet.recreateShape());

      getDrawingArea().clearDrawingArea();
      drawControls();
      getDrawingArea().repaint();
    }
  }

  public void setMirrored( boolean aBool )
  {
    theIsMirrored = aBool;
    if( theManipulationSet != null )
      theManipulationSet.setMirrored(aBool);
  }

  public void setManipulators( boolean small, boolean mid, boolean big )
  {
    theWhichManipulators = new boolean[]{small,mid,big};
    if( theManipulationSet != null )
    {
      theManipulationSet.setManipulators(theWhichManipulators[0],theWhichManipulators[1],theWhichManipulators[2]);

      getDrawingArea().clearDrawingArea();
      drawControls();
      getDrawingArea().repaint();
    }
  }

  public void mouseClicked( MouseEvent e )
  {
    if( e.getClickCount() == 2 )
    {
      theCurrentShape = getDrawingArea().getFirstShapeAt(new Point2D.Double(e.getX(),e.getY()));
      if( theCurrentShape != null )
      {
        getDrawingArea().setUndoPoint(new CompleteUndo(getDrawingArea()));

        PaintableShape holder = theCurrentShape.get();
        getDrawingArea().replaceShape(theCurrentShape,holder);
        theCurrentShape = holder;

        Shape newShape = ShapeToCubicCurver.convert(theCurrentShape.getFillShape());
        if( shouldRedraw )
          theCurrentShape.setShape(newShape);
        else
          theCurrentShape.setFillShape(newShape);

        theManipulationSet = new ManipulationSet(newShape,(float)(theSize));
        theManipulationSet.setMirrored(theIsMirrored);
        theManipulationSet.setManipulators(theWhichManipulators[0],theWhichManipulators[1],theWhichManipulators[2]);

        theWindingRule = theManipulationSet.getWindRule();
        theToolbar.setWindRule(theWindingRule);

        getDrawingArea().clearDrawingArea();
        drawControls();
      }
      else
      {
//Put add a point code here
      }
      getDrawingArea().repaint();
    }
  }

  public void mousePressed( MouseEvent e )
  {
    if( theManipulationSet == null )
      return;

    theDragPoint = new Point(e.getX(),e.getY());
    theCurrentPoint = theManipulationSet.getPoint(theDragPoint);
  }

  public void mouseReleased( MouseEvent e )
  {
    theDragPoint = null;
    theCurrentPoint = null;
  }

  public void mouseDragged( MouseEvent e )
  {
    if( theManipulationSet == null || theCurrentPoint == null )
      return;

    theManipulationSet.drag(theCurrentPoint,e.getX()-theDragPoint.x,e.getY()-theDragPoint.y);

    if( shouldRedraw )
      theCurrentShape.setShape(theManipulationSet.recreateShape());
    else
      theCurrentShape.setFillShape(theManipulationSet.recreateShape());

    theDragPoint = new Point(e.getX(),e.getY());
    getDrawingArea().clearDrawingArea();
    drawControls();
    getDrawingArea().repaint();
  }

  private void drawControls()
  {
    theManipulationSet.paint((Graphics2D)getDrawingArea().getDrawingGraphics());
  }

  public void loseControl()
  {
    theToolbar.resetWindRule();
  }

  public JComponent getToolbar()
  {
     return theToolbar;
  }
}
