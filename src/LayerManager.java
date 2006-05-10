/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.util.Vector;
import java.awt.geom.Point2D;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

public class LayerManager implements Serializable
{
  static final long serialVersionUID = -2945217551459092754L;

  private Vector theLayers;
  private int    theCurrentLayer = 0;

  private boolean isUndoing = false;

  private transient DrawingArea theDrawingArea;

  Vector theUndos;
  Vector theRedos;
 
  public LayerManager( DrawingArea anArea )
  {
    theDrawingArea = anArea;
    theLayers = new Vector();
    theLayers.add(new Layer("Untitled"));
    theUndos = new Vector();
    theRedos = new Vector();
  }

  public void addLayer()
  {
    setUndoPoint(new CompleteUndo(theDrawingArea));
    theCurrentLayer++;
    theLayers.add(new Layer("Untitled"));
  }

  public Vector getLayers()
  {
    return theLayers;
  }

  public void setDrawingArea( DrawingArea anArea )
  {
    theDrawingArea = anArea;
    if( theUndos != null )
      for( int i = 0 ; i < theUndos.size() ; i++ )
        ((Undo)theUndos.get(i)).setDrawingArea(theDrawingArea);
    if( theRedos != null )
      for( int i = 0 ; i < theRedos.size() ; i++ )
        ((Undo)theRedos.get(i)).setDrawingArea(theDrawingArea);
  }

  public void setLayers( Vector someLayers )
  {
    theLayers = new Vector();
    for( int i = 0 ; i < someLayers.size() ; i++ )
      theLayers.add(((Layer)someLayers.get(i)).clone());
  }

  public void removeLayer( Layer aLayer )
  {
    setUndoPoint(new CompleteUndo(theDrawingArea));

    Layer currentLayer = (Layer)theLayers.get(theCurrentLayer);
    int index = theLayers.indexOf(aLayer);
    theLayers.remove(aLayer);
    if( currentLayer.equals(aLayer) && theCurrentLayer > theLayers.size()-1)
    {
      theCurrentLayer -= 1;
      if( theCurrentLayer < 0 )
        theCurrentLayer = 0;
    }
    else if( currentLayer.equals(aLayer) )
    {
      
    }
    else
    {
      theCurrentLayer = theLayers.indexOf(currentLayer);
    }

    if( theLayers.size() == 0 )
      theLayers.add(new Layer("Untitled"));
  }
 
  public void setLayerVisible( Layer aLayer, boolean anIsVisible )
  {
    aLayer.setVisible(anIsVisible);
  }

  public void setLayerName( Layer aLayer, String aName )
  {
    aLayer.setName(aName);
  }

  public int getCurrentLayerIndex()
  {
    return theCurrentLayer;
  }

  public void setCurrentLayer( Layer aLayer )
  {
    setUndoPoint(new CompleteUndo(theDrawingArea));
    theCurrentLayer = theLayers.indexOf(aLayer);
  }

  public void setCurrentLayer( int aLayer )
  {
    theCurrentLayer = aLayer;
  }

  public void merge( Layer aLayer )
  {
    setUndoPoint(new CompleteUndo(theDrawingArea));
    Layer combinedLayer = (Layer)theLayers.get(theLayers.indexOf(aLayer)-1);
    Vector shapes = aLayer.getShapes();
    for( int i = 0 ; i < shapes.size() ; i++ )
      combinedLayer.addShape((PaintableShape)shapes.get(i));
    if( theCurrentLayer == theLayers.indexOf(aLayer) || theCurrentLayer == theLayers.size()-1 )
      theCurrentLayer -= 1;

    theLayers.remove(aLayer);
  }

  public void moveLayerUp( Layer aLayer )
  {
    setUndoPoint(new CompleteUndo(theDrawingArea));
    int index = theLayers.indexOf(aLayer);
    Object currentLayer = theLayers.get(theCurrentLayer);
    if( index == theLayers.size()-1 )
      return;
    theLayers.remove(aLayer);
    theLayers.insertElementAt(aLayer,index+1);
    theCurrentLayer = theLayers.indexOf(currentLayer);
  }

  public void moveLayerDown( Layer aLayer )
  {
    setUndoPoint(new CompleteUndo(theDrawingArea));
    int index = theLayers.indexOf(aLayer);
    Object currentLayer = theLayers.get(theCurrentLayer);
    if( index > 0 )
    {
      theLayers.remove(aLayer);
      theLayers.insertElementAt(aLayer,index-1);
      theCurrentLayer = theLayers.indexOf(currentLayer);
    }
  }

  public Vector getShapes()
  {
    return ((Layer)theLayers.get(theCurrentLayer)).getShapes();
  }

  public Vector getAllShapes()
  {
    Vector shapes = new Vector();
    for( int i = 0 ; i < theLayers.size() ; i++ )
      shapes.add(((Layer)theLayers.get(i)).getShapes());
    return shapes;
  }  

  public int indexOf( PaintableShape aShape )
  {
    return ((Layer)theLayers.get(theCurrentLayer)).indexOf(aShape);
  }

  public void addShape( PaintableShape aShape )
  {
    ((Layer)theLayers.get(theCurrentLayer)).addShape(aShape);
  }

  public void replaceShape( PaintableShape anOldShape, PaintableShape aNewShape )
  {
    ((Layer)theLayers.get(theCurrentLayer)).replaceShape(anOldShape,aNewShape);
  }

  public void insertShape( PaintableShape anOldShape, PaintableShape aNewShape )
  {
    ((Layer)theLayers.get(theCurrentLayer)).insertShape(anOldShape,aNewShape );
  }

  public void insertShape( PaintableShape aNewShape, int anIndex )
  {
    ((Layer)theLayers.get(theCurrentLayer)).insertShape(aNewShape,anIndex);
  }

  public void moveUpALayer( Vector someShapes )
  {
    if( theCurrentLayer == theLayers.size()-1 )
    {
      theLayers.add(new Layer("Untitled"));
    }

    Vector shapes = (Vector)someShapes.clone();

    int index = -1;
    int size = shapes.size();
    for( int i = 0 ; i < size ; i++ )
    {
      index = 0;
      int curIndex = ((Layer)theLayers.get(theCurrentLayer)).indexOf((PaintableShape)shapes.get(0));
      for( int j = 0 ; j < shapes.size() ; j++ )
      {
        if( ((Layer)theLayers.get(theCurrentLayer)).indexOf((PaintableShape)shapes.get(j)) > curIndex )
        {
          curIndex = ((Layer)theLayers.get(theCurrentLayer)).indexOf((PaintableShape)shapes.get(j));
          index = j;
        }
      }
      ((Layer)theLayers.get(theCurrentLayer)).removeShape((PaintableShape)shapes.get(index));
      ((Layer)theLayers.get(theCurrentLayer+1)).insertShape((PaintableShape)shapes.get(index),0);
      shapes.remove(index);
    }
  }

  public void moveBackALayer( Vector someShapes )
  {
    if( theCurrentLayer == 0 )
    {
      theCurrentLayer++;
      theLayers.insertElementAt(new Layer("Untitled"),0);
    }

    Vector shapes = (Vector)someShapes.clone();

    int index = -1;
    int size = shapes.size();
    for( int i = 0 ; i < size ; i++ )
    {
      index = 0;
      int curIndex = ((Layer)theLayers.get(theCurrentLayer)).indexOf((PaintableShape)shapes.get(0));
      for( int j = 0 ; j < shapes.size() ; j++ )
      {
        if( ((Layer)theLayers.get(theCurrentLayer)).indexOf((PaintableShape)shapes.get(j)) < curIndex )
        {
          curIndex = ((Layer)theLayers.get(theCurrentLayer)).indexOf((PaintableShape)shapes.get(j));
          index = j;
        }
      }
      ((Layer)theLayers.get(theCurrentLayer)).removeShape((PaintableShape)shapes.get(index));
      ((Layer)theLayers.get(theCurrentLayer-1)).addShape((PaintableShape)shapes.get(index));
      shapes.remove(index);
    }
  }

  public void moveUp( Vector someShapes )
  {
    ((Layer)theLayers.get(theCurrentLayer)).moveUp(someShapes);
  }

  public void moveBack( Vector someShapes )
  {
    ((Layer)theLayers.get(theCurrentLayer)).moveBack(someShapes);
  }

  public void moveToFront( Vector someShapes )
  {
    ((Layer)theLayers.get(theCurrentLayer)).moveToFront(someShapes);
  }

  public void moveToBack( Vector someShapes )
  {
    ((Layer)theLayers.get(theCurrentLayer)).moveToBack(someShapes);
  }

  public void removeShape( Object aShape )
  {
    ((Layer)theLayers.get(theCurrentLayer)).removeShape(aShape);
  }

  public PaintableShape getFirstShapeAt( Point2D.Double aPoint )
  {
    return ((Layer)theLayers.get(theCurrentLayer)).getFirstShapeAt(aPoint);
  }

  public void paintShapesBelow( Graphics2D aGraphics )
  {
    for( int i = 0 ; i <= theCurrentLayer ; i++ )
    {
      if( ((Layer)theLayers.get(i)).isVisible() )
      {
        ((Layer)theLayers.get(i)).paintShapes(aGraphics);
      }
    }
  }

  public void paintShapesAbove( Graphics2D aGraphics )
  {
    for( int i = theCurrentLayer+1 ; i < theLayers.size() ; i++ )
    {
      if( ((Layer)theLayers.get(i)).isVisible() )
      {
        ((Layer)theLayers.get(i)).paintShapes(aGraphics);
      }
    }
  }

  public Vector getIntersectedShapes( PaintableShape aShape )
  {
    Vector intersectedShapes = new Vector();

    Vector shapes = ((Layer)theLayers.get(theCurrentLayer)).getShapes();
    for( int j = 0 ; j < shapes.size() ; j++ )
    {
      if( ((PaintableShape)shapes.get(j)).intersects(aShape) )
        intersectedShapes.add(shapes.get(j));
    }

    if( intersectedShapes.size() == 0 )
      return null;
    else
      return intersectedShapes;
  }

  public void scale( double anXFactor, double aYFactor )
  {
    for( int i = 0 ; i < theLayers.size() ; i++ )
      ((Layer)theLayers.get(i)).scale(anXFactor,aYFactor);
  } 

  public void zoom( double aFactor )
  {
    for( int i = 0 ; i < theLayers.size() ; i++ )
      ((Layer)theLayers.get(i)).zoom(aFactor);
  } 

  public void clearCurrent()
  {
    ((Layer)theLayers.get(theCurrentLayer)).clear();
  }

  public void setUndoPoint( Undo anUndo  )
  {
    if( isUndoing )
      return;
    theRedos = new Vector();
    theUndos.add(anUndo);
  }

  public void undo()
  {
    if( theUndos == null || theUndos.size() <= 0 )
      return;

    isUndoing = true;
    ((Undo)theUndos.get(theUndos.size()-1)).undo();
    theRedos.add(theUndos.remove(theUndos.size()-1));
    isUndoing = false;
  }

  public void redo()
  {
    if( theRedos == null || theRedos.size() <= 0 )
      return;

    isUndoing = true;
    ((Undo)theRedos.get(theRedos.size()-1)).redo();
    theUndos.add(theRedos.remove(theRedos.size()-1));
    isUndoing = false;
  }

  private Vector getUndos()
  {
    return theUndos;
  }

  private Vector getRedos()
  {
    return theRedos;
  }

  private void writeObject( ObjectOutputStream out) throws IOException
  {
    out.writeObject(theLayers);
    out.writeInt(theCurrentLayer);

    out.writeBoolean(isUndoing);

    out.writeObject(theUndos);
    out.writeObject(theRedos);
  }
  private void readObject( ObjectInputStream in) throws IOException, ClassNotFoundException
  {
    theLayers = (Vector)in.readObject();
    theCurrentLayer = in.readInt();
    isUndoing = in.readBoolean();
    theUndos = (Vector)in.readObject();
    theRedos = (Vector)in.readObject();
  }
}