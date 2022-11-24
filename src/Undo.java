/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.io.Serializable;

public abstract class Undo implements Serializable
{
  static final long serialVersionUID = 6493499330783348886L;

  private transient DrawingArea theDrawingArea;
  private double      theZoomFactor;


  public Undo( DrawingArea anArea )
  {
    theDrawingArea = anArea;
    theZoomFactor  = theDrawingArea.getZoom();
  }

  public void undo()
  {

  }

  public void redo()
  {

  }

  public void setDrawingArea( DrawingArea anArea )
  {
    theDrawingArea = anArea;
  }

  public DrawingArea getDrawingArea()
  {
    return theDrawingArea;
  }

  public double getZoom()
  {
    return theZoomFactor;
  }
}