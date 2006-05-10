/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.util.Vector;

public class TranslateUndo extends Undo
{
  static final long serialVersionUID = 8544876608425999412L;

  private Vector theShapes;
  private double theXFactor;
  private double theYFactor;

  public TranslateUndo( DrawingArea anArea, Vector someShapes )
  {
    super(anArea);
    theShapes = (Vector)someShapes.clone();
  }

  public void translate( double anXFactor, double aYFactor )
  {
    theXFactor += anXFactor;
    theYFactor += aYFactor;
  }
 
  public void undo()
  {
    double oldZoom = getDrawingArea().getZoom();
    getDrawingArea().setZoom(getZoom());

    for( int i = 0 ; i < theShapes.size() ; i++ )
      ((PaintableShape)theShapes.get(i)).translate(-theXFactor,-theYFactor,true);

    theXFactor *= -1;
    theYFactor *= -1;

    getDrawingArea().setZoom(oldZoom);
  }

  public void redo()
  {
    undo();
  }
}