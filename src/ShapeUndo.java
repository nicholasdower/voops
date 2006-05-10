/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.util.Vector;

public class ShapeUndo extends Undo
{
  static final long serialVersionUID = -5158556545209074452L;

  Vector theOldShapes;
  Vector theNewShapes;

  public ShapeUndo( DrawingArea anArea )
  {
    super(anArea);
  }

  public ShapeUndo( DrawingArea anArea, Vector someOldShapes, Vector someNewShapes )
  {
    super(anArea);
    setOldShapes(someOldShapes);
    setNewShapes(someNewShapes);
  }

  public void setOldShapes( Vector someOldShapes )
  {
    theOldShapes = new Vector();
    for( int i = 0 ; i < someOldShapes.size() ; i++ )
    {
      theOldShapes.add(((PaintableShape)someOldShapes.get(i)).get());
    }
  }

  public void setNewShapes( Vector someNewShapes )
  {
    theNewShapes = new Vector();
    for( int i = 0 ; i < someNewShapes.size() ; i++ )
    {
      theNewShapes.add(((PaintableShape)someNewShapes.get(i)).get());
    }
  }

  public void undo()
  {
    double oldZoom = getDrawingArea().getZoom();
    getDrawingArea().setZoom(getZoom());

    for( int i = 0 ; i < theNewShapes.size() ; i++ )
      getDrawingArea().getLayerManager().replaceShape((PaintableShape)theOldShapes.get(i),(PaintableShape)theNewShapes.get(i));
    Vector temp = theNewShapes;
    theNewShapes = theOldShapes;
    theOldShapes = temp;

    getDrawingArea().setZoom(oldZoom);
  }

  public void redo()
  {
    undo();
  }
}