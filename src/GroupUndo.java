/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.util.Vector;

public class GroupUndo extends Undo
{
  static final long serialVersionUID = -2136591649657319274L;

  private Vector theShapes;
  private Vector theGroups;
  private int    theCurrentLayer;

  public GroupUndo( DrawingArea anArea )
  {
    super(anArea);
    theShapes = getDrawingArea().getLayerManager().getAllShapes();
    theGroups = new Vector();
    for( int i = 0 ; i < theShapes.size() ; i++ )
      for( int j = 0 ; j < ((Vector)theShapes.get(i)).size() ; j++ )
        theGroups.add(((PaintableShape)((Vector)theShapes.get(i)).get(j)).getGroup());
  }

  public void undo()
  {
    Vector temp = new Vector();
    for( int i = 0 ; i < theShapes.size() ; i++ )
      for( int j = 0 ; j < ((Vector)theShapes.get(i)).size() ; j++ )
        temp.add(((PaintableShape)((Vector)theShapes.get(i)).get(j)).getGroup());

    for( int i = 0 ; i < theShapes.size() ; i++ )
      for( int j = 0 ; j < ((Vector)theShapes.get(i)).size() ; j++ )
        ((PaintableShape)((Vector)theShapes.get(i)).get(j)).setGroup((Vector)theGroups.get(i));

    theGroups = temp;
  }

  public void redo()
  {
    undo();
  }
}