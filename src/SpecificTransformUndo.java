/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.util.Vector;

public class SpecificTransformUndo extends Undo
{
  static final long serialVersionUID = -1521510272124793624L;

  private Vector          theShapes;
  private Vector          theTransforms;

  public SpecificTransformUndo( DrawingArea anArea, Vector someShapes )
  {
    super(anArea);
    theShapes = someShapes;
    theTransforms = new Vector();
    for( int i = 0 ; i < theShapes.size() ; i++ )
    {
      theTransforms.add(new AffineTransform());
    }
  }

  public void transform( AffineTransform aTransform, PaintableShape aShape )
  {
    ((AffineTransform)theTransforms.get(theShapes.indexOf(aShape))).concatenate(aTransform);
  }

  public void undo()
  {
    double oldZoom = getDrawingArea().getZoom();
    getDrawingArea().setZoom(getZoom());

    try
    {
      for( int i = 0 ; i < theShapes.size() ; i++ )
      {
        theTransforms.set(i,(((AffineTransform)theTransforms.get(i)).createInverse()));
      }
    }
    catch( NoninvertibleTransformException nite )
    {
      System.out.println(nite);
      return;
    }
    for( int i = 0 ; i < theShapes.size() ; i++ )
      ((PaintableShape)theShapes.get(i)).transform(((AffineTransform)theTransforms.get(i)));

    getDrawingArea().setZoom(oldZoom);
  }

  public void redo()
  {
    undo();
  }
}