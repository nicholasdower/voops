/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.util.Vector;

public class CompleteUndo extends Undo
{
  static final long serialVersionUID = 9160707368431110928L;

  private Vector theLayers;
  private int    theCurrentLayer;

  public CompleteUndo( DrawingArea anArea )
  {
    super(anArea);
    theCurrentLayer = getDrawingArea().getLayerManager().getCurrentLayerIndex();
    Vector layers = getDrawingArea().getLayerManager().getLayers();
    theLayers = new Vector();
    for( int i = 0 ; i < layers.size() ; i++ )
      theLayers.add(((Layer)layers.get(i)).clone());
  }

  public void undo()
  {
    double oldZoom = getDrawingArea().getZoom();
    getDrawingArea().setZoom(getZoom());

    int currentLayer = getDrawingArea().getLayerManager().getCurrentLayerIndex();
    Vector layers = getDrawingArea().getLayerManager().getLayers();
    Vector oldLayers = new Vector();
    for( int i = 0 ; i < layers.size() ; i++ )
      oldLayers.add(((Layer)layers.get(i)).clone());

    getDrawingArea().getLayerManager().setLayers(theLayers);
    getDrawingArea().getLayerManager().setCurrentLayer(theCurrentLayer);

    theLayers       = oldLayers;
    theCurrentLayer = currentLayer;

    getDrawingArea().setZoom(oldZoom);
  }

  public void redo()
  {
    undo();
  }
}