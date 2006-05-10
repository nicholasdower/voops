/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.util.Vector;

public class TransformUndo extends Undo
{
  static final long serialVersionUID = 8820321931600591065L;

  private Vector          theShapes;
  private AffineTransform theTransform;

  public TransformUndo( DrawingArea anArea, Vector someShapes )
  {
    super(anArea);
    theShapes = someShapes;
    theTransform = new AffineTransform();
  }
 
  public void transform( AffineTransform aTransform )
  {
    theTransform.concatenate(aTransform);
  }

  public void undo()
  {
    double oldZoom = getDrawingArea().getZoom();
    getDrawingArea().setZoom(getZoom());
    try
    {
      theTransform = theTransform.createInverse();
    }
    catch( NoninvertibleTransformException nite )
    {
      System.out.println(nite);
      return;
    }
    for( int i = 0 ; i < theShapes.size() ; i++ )
      ((PaintableShape)theShapes.get(i)).transform(theTransform);

    getDrawingArea().setZoom(oldZoom);
  }

  public void redo()
  {
    undo();
  }
}