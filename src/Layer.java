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

public class Layer implements Serializable
{
  static final long serialVersionUID = 2064455641174855717L;

  private Vector  theShapes;
  private String  theName;
  private boolean isVisible;

  public Layer( String aName )
  {
    theShapes  = new Vector();
    theName    = aName;
    isVisible  = true;
  }

  public Layer( String aName, Vector someShapes, boolean anIsVisible )
  {
    theShapes  = someShapes;
    theName    = aName;
    isVisible  = anIsVisible;
  }

  public void addShape( PaintableShape aShape )
  {
    theShapes.add(aShape);
  }

  public boolean isVisible()
  {
    return isVisible;
  }

  public void setVisible( boolean aBool )
  {
    isVisible = aBool;
  }
 
  public String getName()
  {
    return theName;
  }

  public void setName( String aName )
  {
    theName = aName;
  }

  public Vector getShapes()
  {
    return theShapes;
  }

  public void paintShapes( Graphics2D aGraphics )
  {
    for( int i = 0 ; i < theShapes.size() ; i++ )
    {
      ((PaintableShape)theShapes.get(i)).paint(aGraphics);
    }
  }

  public void replaceShape( PaintableShape anOldShape, PaintableShape aNewShape )
  {
    int index = theShapes.indexOf(anOldShape);
    if( index >= 0 )
    {
      theShapes.remove(anOldShape);
      theShapes.insertElementAt(aNewShape,index);
    }
  }

  public void insertShape( PaintableShape anOldShape, PaintableShape aNewShape )
  {
    int index = theShapes.indexOf(anOldShape);
    if( index >= 0 )
    {
      theShapes.insertElementAt(aNewShape,index+1);
    }
  }

  public int indexOf( PaintableShape aShape )
  {
    return theShapes.indexOf(aShape);
  }

  public void insertShape( PaintableShape aNewShape, int anIndex )
  {
    theShapes.insertElementAt(aNewShape,anIndex);
  }

  public void moveToFront( Vector someShapes )
  {
    Vector indexes = new Vector();
    int index;
    for( int i = 0 ; i < someShapes.size() ; i++ )
    {
      index = theShapes.indexOf(someShapes.get(i));
      int loc = 0;
      while( loc < indexes.size() && index > ((Integer)indexes.get(loc)).intValue() ){loc++;}
      indexes.insertElementAt(new Integer(index),loc);
    }

    int loc;
    Object currentShape;
    for( int i = 0 ; i < indexes.size() ; i++ )
    {
      loc = ((Integer)indexes.get(i)).intValue()-i;
      currentShape = theShapes.remove(loc);
      theShapes.insertElementAt(currentShape,theShapes.size());
    }
  }

  public void moveToBack( Vector someShapes )
  {
    Vector indexes = new Vector();
    int index;
    for( int i = 0 ; i < someShapes.size() ; i++ )
    {
      index = theShapes.indexOf(someShapes.get(i));
      int loc = 0;
      while( loc < indexes.size() && index > ((Integer)indexes.get(loc)).intValue() ){loc++;}
      indexes.insertElementAt(new Integer(index),loc);
    }

    int loc;
    Object currentShape;
    for( int i = (indexes.size()-1) ; i >= 0  ; i-- )
    {
      loc = ((Integer)indexes.get(i)).intValue() + ((indexes.size()-1)-i);
      currentShape = theShapes.remove(loc);
      theShapes.insertElementAt(currentShape,0);
    }
  }

  public void moveUp( Vector someShapes )
  {
    Vector indexes = new Vector();
    int index;
    for( int i = 0 ; i < someShapes.size() ; i++ )
    {
      index = theShapes.indexOf(someShapes.get(i));
      if( index < (theShapes.size()-1) )
      {
        int loc = 0;
        while( loc < indexes.size() && index > ((Integer)indexes.get(loc)).intValue() ){loc++;}
        indexes.insertElementAt(new Integer(index),loc);
      }
    }

    Object currentShape;
    int loc;
    for( int i = (indexes.size()-1) ; i >= 0 ; i-- )
    {
      loc = ((Integer)indexes.get(i)).intValue();
      if( loc < (theShapes.size()-1) && !someShapes.contains(theShapes.get(loc+1)) )
      {
        currentShape = theShapes.remove(loc);
        theShapes.insertElementAt(currentShape,loc+1);
      }
    } 
  }

  public void moveBack( Vector someShapes )
  {
    Vector indexes = new Vector();
    int index;
    for( int i = 0 ; i < someShapes.size() ; i++ )
    {
      index = theShapes.indexOf(someShapes.get(i));
      if( index > 0 )
      {
        int loc = 0;
        while( loc < indexes.size() && index > ((Integer)indexes.get(loc)).intValue() ){loc++;}
        indexes.insertElementAt(new Integer(index),loc);
      }
    }

    Object currentShape;
    int loc;
    for( int i = 0 ; i < indexes.size() ; i++ )
    {
      loc = ((Integer)indexes.get(i)).intValue();
      if( loc > 0 && !someShapes.contains(theShapes.get(loc-1)) )
      {
        currentShape = theShapes.remove(loc);
        theShapes.insertElementAt(currentShape,loc-1);
      }
    } 
  }

  public void removeShape( Object aShape )
  {
    theShapes.remove(aShape);
  }

  public PaintableShape getFirstShapeAt( Point2D.Double aPoint )
  {
    for( int i = (theShapes.size()-1) ; i >= 0 ; i-- )
    {
      if( ((PaintableShape)theShapes.get(i)).intersects(aPoint.x,aPoint.y,1,1) )
      {
        return (PaintableShape)theShapes.get(i);
      }
    }
    return null;
  }

  public void scale( double anXFactor, double aYFactor )
  {
    for( int i = 0 ; i < theShapes.size() ; i++ )
      ((PaintableShape)theShapes.get(i)).scale(anXFactor,aYFactor);
  }

  public void zoom( double aFactor )
  {
    for( int i = 0 ; i < theShapes.size() ; i++ )
      ((PaintableShape)theShapes.get(i)).setZoom(aFactor);
  } 

  public void clear()
  {
    theShapes = new Vector();
  }

  public Object clone()
  {
    return new Layer(theName,(Vector)(theShapes.clone()),isVisible);
  }

  private void writeObject( ObjectOutputStream out) throws IOException
  {
    out.writeObject(theShapes);
    out.writeObject(theName);

    out.writeBoolean(isVisible);
  }
  private void readObject( ObjectInputStream in) throws IOException, ClassNotFoundException
  {
    theShapes = (Vector)in.readObject();
    theName   = (String)in.readObject();
    isVisible = in.readBoolean();
  }
}