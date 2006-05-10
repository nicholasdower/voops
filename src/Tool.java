/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Paint;
import javax.swing.JComponent;
import java.awt.Cursor;

public class Tool implements MouseListener, MouseMotionListener
{
  private int            theType;
  
  private DrawingArea    theDrawingArea;
  private PaintableShape theShape;

  private boolean        isStarted = false;

  private Undo theCurrentUndo;

  private Cursor theCursor;

  public static final double theMoveKeyValue = .3333333; 

  public Tool( DrawingArea aDrawingArea, int aType )
  {
    theDrawingArea = aDrawingArea;
    theDrawingArea.clearDrawingArea();
    theDrawingArea.addMouseListener(this);
    theDrawingArea.addMouseMotionListener(this);

    if( aType != PaintableShape.TYPE_FILL && aType != PaintableShape.TYPE_DRAW && aType != PaintableShape.TYPE_BOTH )
      theType = PaintableShape.TYPE_DRAW;
    else
      theType  = aType;

    theShape = new PaintableShape(theType);
  }

  public void setDrawingArea( DrawingArea anArea )
  {
    theDrawingArea = anArea;
  }

  protected void paintShape()
  {
    theDrawingArea.clearDrawingArea();
    theDrawingArea.paintShape(theShape);
    theDrawingArea.repaint();
  }

  protected void commit()
  {
    theDrawingArea.clearDrawingArea();
    if( theShape == null )
      return;
    
    if( theShape.getFillShape() == null )
      return;

    getDrawingArea().setUndoPoint(new CompleteUndo(getDrawingArea()));
    theDrawingArea.addShape(theShape.get());
 
    theDrawingArea.repaint();
  }

  public void commitUndo()
  {
    if( theCurrentUndo != null )
    {
      getDrawingArea().setUndoPoint(theCurrentUndo);
      theCurrentUndo = null;
    }
  }

  public void setUndo( Undo anUndo )
  {
    theCurrentUndo = anUndo;
  }

  public Undo getUndo()
  {
    return theCurrentUndo;
  }

  public void loseControl(){}

  public void setDrawPaint( Paint aPaint )
  {
    theShape.setDrawPaint(aPaint);
  }

  public void setFillPaint( Paint aPaint )
  {
    theShape.setFillPaint(aPaint);
  }

  public void setStroke( BasicStroke aStroke )
  {
    theShape.setStroke(aStroke);
  }

  public void setIsStarted( boolean anIsStarted )
  {
    isStarted = anIsStarted;
  }

  public void setCursor( Cursor aCursor )
  {
    theCursor = aCursor;
    if( theDrawingArea != null )
      theDrawingArea.setCursor(theCursor);
  }

  public Cursor getCursor()
  {
    return theCursor;
  }

  public PaintableShape getShape()
  {
    return theShape;
  }

  public DrawingArea getDrawingArea()
  {
    return theDrawingArea;
  }

  public JComponent getToolbar()
  {
    return null;
  }
 
  public double getMoveKeyValue()
  {
    return theMoveKeyValue*getDrawingArea().getZoom();
  }

  public int getType()
  {
    return theType;
  }

  public void setType( int aType )
  {
    theType = aType;
    theShape.setType(aType);
  }

  public boolean getIsStarted()
  {
    return isStarted;
  }

  public void mousePressed ( MouseEvent e ){}
  public void mouseClicked ( MouseEvent e ){}
  public void mouseReleased( MouseEvent e ){}
  public void mouseExited  ( MouseEvent e ){}
  public void mouseEntered ( MouseEvent e ){}

  public void mouseMoved   ( MouseEvent e ){}
  public void mouseDragged ( MouseEvent e ){}
}