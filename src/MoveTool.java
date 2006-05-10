/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Point;
import java.awt.Cursor;
import java.awt.Shape;
import java.awt.Paint;
import java.awt.geom.Area;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.util.Vector;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;
import java.awt.geom.AffineTransform;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;

public class MoveTool extends Tool implements Selectable
{
  private Point theLastPoint;

  private Vector theSelectedShapes;

  private Rectangle2D.Double theSelectedShapeCovers;
  private Rectangle2D.Double theOldCover;
  private Rectangle2D.Double[] theResizors;
  private final double theResizorSize = 3;
  private int theCurrentResizor;
  private boolean isResizing = false;

  private PaintableShape theLastAdded;

  private static final BasicStroke theCoverStroke = new BasicStroke(1);

  private AffineTransform theTransform;

  private KeyListener theDeleteListener; 

  private SelectionToolbar theSelectionToolbar;
 
  private Point2D.Double theRotationCenter;

  public MoveTool( DrawingArea aDrawingArea )
  {
    this(aDrawingArea,PaintableShape.TYPE_FILL);
  }

  public MoveTool( DrawingArea aDrawingArea, int aType )
  {
    super(aDrawingArea, aType);

    theDeleteListener = new KeyListener()
    {
      public void keyReleased( KeyEvent e )
      {
        if( e.getKeyCode() == KeyEvent.VK_DELETE )
          delete();

        if( e.getKeyCode() == KeyEvent.VK_LEFT )
          commitUndo();
        if( e.getKeyCode() == KeyEvent.VK_RIGHT )
          commitUndo();
        if( e.getKeyCode() == KeyEvent.VK_UP )
          commitUndo();
        if( e.getKeyCode() == KeyEvent.VK_DOWN )
          commitUndo();
      }
      public void keyTyped( KeyEvent e ){}
      public void keyPressed( KeyEvent e )
      {
        double xTranslation = 0;
        double yTranslation = 0;
        if( e.getKeyCode() == KeyEvent.VK_LEFT )
        {
          xTranslation = -getMoveKeyValue();
        }
        else if( e.getKeyCode() == KeyEvent.VK_RIGHT )
        {
          xTranslation = getMoveKeyValue();
        }
        else if( e.getKeyCode() == KeyEvent.VK_UP )
        {
          yTranslation = -getMoveKeyValue();
        }
        else if( e.getKeyCode() == KeyEvent.VK_DOWN )
        {
          yTranslation = getMoveKeyValue();
        }

        if( ( xTranslation != 0 || yTranslation != 0 ) && theSelectedShapes != null )
        {
          getDrawingArea().clearDrawingArea();
          if( theSelectedShapes.size() > 0 )
          {
            if( getUndo() == null )
            {
              setUndo(new TranslateUndo(getDrawingArea(),theSelectedShapes));
            }
            ((TranslateUndo)getUndo()).translate(xTranslation,yTranslation);

            for( int i = 0 ; i < theSelectedShapes.size() ; i++ )
            {
              ((PaintableShape)theSelectedShapes.get(i)).translate(xTranslation,yTranslation,true);
            }
            translateCover(xTranslation,yTranslation);
            paintCover();
          }
        }
      }
    };
    getDrawingArea().addKeyListener(theDeleteListener);
    //getDrawingArea().grabFocus();
    getDrawingArea().requestFocusInWindow();

    theSelectionToolbar = new SelectionToolbar(this);

    setCursor(OOPSCursors.MOVE_UP);
    theSelectedShapeCovers = new Rectangle2D.Double();
    theResizors = new Rectangle2D.Double[8];
    for( int i = 0 ; i < 8 ; i ++ )
      theResizors[i] = new Rectangle2D.Double(0,0,theResizorSize*2,theResizorSize*2);
  }

  public void mouseMoved( MouseEvent e )
  {
    if( !getIsStarted() && theSelectedShapes != null )
    {
      if( theSelectedShapes.size() > 0 )
      {
        theCurrentResizor = -1;
        for( int i = 0 ; i < theResizors.length ; i++ )
        {
          if( theResizors[i] != null && theResizors[i].contains(e.getX(), e.getY()) )
          {
            theCurrentResizor = i;
            break;
          }
        }
 
        if( theCurrentResizor >= 0 )
        {
          switch(theCurrentResizor)
          {
            case 0 : getDrawingArea().setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR)); break;
            case 1 : getDrawingArea().setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));  break;
            case 2 : getDrawingArea().setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR)); break;
            case 3 : getDrawingArea().setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));  break;
            case 4 : getDrawingArea().setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));  break;
            case 5 : getDrawingArea().setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR)); break;
            case 6 : getDrawingArea().setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));  break;
            case 7 : getDrawingArea().setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR)); break;
          }
        }
        else if( !theSelectedShapeCovers.contains(e.getX(), e.getY()) )
        {
          getDrawingArea().setCursor(OOPSCursors.MOVE_UP);
        }

      }
    }
  }

  public void mouseDragged( MouseEvent e )
  {
    if( !getIsStarted() )
      return;

    if( isResizing )
    {
      resizeFromResizors(e.getX(),e.getY());
      return;
    }
    getDrawingArea().clearDrawingArea();
    if( theSelectedShapes.size() > 0 )
    {
      if( getUndo() == null )
      {
        setUndo(new TranslateUndo(getDrawingArea(),theSelectedShapes));
      }
      ((TranslateUndo)getUndo()).translate(e.getX()-theLastPoint.x,e.getY()-theLastPoint.y);

      //getDrawingArea().clearDrawingArea();
      for( int i = 0 ; i < theSelectedShapes.size() ; i++ )
      {
        ((PaintableShape)theSelectedShapes.get(i)).translate(e.getX()-theLastPoint.x,e.getY()-theLastPoint.y,true);
      }
      translateCover(e.getX()-theLastPoint.x,e.getY()-theLastPoint.y);
      paintCover();

      theLastPoint = new Point(e.getX(),e.getY());
      //getDrawingArea().paintShape(getShape());
    }
  }

  public void mouseClicked( MouseEvent e )
  {
      if( theSelectedShapes == null )
      {
        theSelectedShapes      = new Vector();
      }

      theLastPoint = new Point(e.getX(),e.getY());
      Vector shapes = getDrawingArea().getShapes();
 
      boolean found = false;
      for( int i = (shapes.size()-1) ; i >= 0 ; i-- )
      {
        if( ((PaintableShape)shapes.get(i)).intersects(e.getX(),e.getY(),1,1) )
        {
          if( theLastAdded != (PaintableShape)shapes.get(i) )
          {
            if( theSelectedShapes.contains((PaintableShape)shapes.get(i)) )
            {
              if( e.isShiftDown() )
              {
                Vector group = ((PaintableShape)shapes.get(i)).getGroup();
                int index;
                if( group != null )
                {
                  for( int j = 0 ; j < group.size() ; j++ )
                  { 
                    index = theSelectedShapes.indexOf(group.get(j));
                    theSelectedShapes.remove(group.get(j));
                  }
                }
                else
                { 
                  index = theSelectedShapes.indexOf(shapes.get(i));
                  theSelectedShapes.remove(shapes.get(i));
                }
                setCoverRect();
              }
            }
          }
          theLastAdded = null;

          found = true;
          break;
        }
      }
      if( found )
      {
        getDrawingArea().clearDrawingArea();
        paintCover();
        setIsStarted(false);
      }
      else
      { 
        getDrawingArea().clearDrawingArea();
        setIsStarted(false);
        theSelectedShapes      = new Vector();
      }
    //}
    //else
    //{
    //  theLastPoint       = null;
    //  theLastAdded = null;
    //  getDrawingArea().clearDrawingArea();
    //}
  }

  public void mousePressed( MouseEvent e )
  {
    setCursor(OOPSCursors.MOVE_DOWN);
    if( !getIsStarted() )
    {
      //getDrawingArea().clearDrawingArea();
      if( theSelectedShapes == null )
      {
        theSelectedShapes      = new Vector();
      }
      else
      {
        theCurrentResizor = -1;
        for( int i = 0 ; i < theResizors.length ; i++ )
        {
          if( theResizors[i] != null && theResizors[i].contains(e.getX(), e.getY()) )
          {
            theCurrentResizor = i;
            break;
          }
        }
 
        if( theCurrentResizor >= 0 )
        {
          isResizing = true;
          setIsStarted(true);
          return;
        }
      }
      isResizing = false;

      theLastPoint = new Point(e.getX(),e.getY());
      Vector shapes = getDrawingArea().getShapes();
 
      boolean found = false;
      for( int i = (shapes.size()-1) ; i >= 0 ; i-- )
      {
        if( ((PaintableShape)shapes.get(i)).intersects(e.getX(),e.getY(),1,1) )
        {
          if( theSelectedShapes.contains((PaintableShape)shapes.get(i)) )
          {
            if( !e.isShiftDown() )
            {
              theSelectedShapes      = new Vector();
              theLastAdded = (PaintableShape)shapes.get(i);
              theSelectedShapes.add(theLastAdded);

              Vector group = theLastAdded.getGroup();
              if( group != null && e.getClickCount() != 2 )
              {
                for( int j = 0 ; j < group.size() ; j++ )
                {
                  if( !theSelectedShapes.contains((PaintableShape)group.get(j)) && !((PaintableShape)group.get(j)).equals(theLastAdded) )
                  {
                    theSelectedShapes.add((PaintableShape)group.get(j));
                  }
                }
              } 
            }
          }
          else
          {
            if( !e.isShiftDown() )
            {
              theSelectedShapes      = new Vector();
              theLastAdded = (PaintableShape)shapes.get(i);
              theSelectedShapes.add(theLastAdded); 

              Vector group = theLastAdded.getGroup();
              if( group != null )
              {
                for( int j = 0 ; j < group.size() ; j++ )
                {
                  if( !theSelectedShapes.contains((PaintableShape)group.get(j)) && !((PaintableShape)group.get(j)).equals(theLastAdded) )
                  {
                    theSelectedShapes.add((PaintableShape)group.get(j));
                  }
                }
              } 
            }
            else
            {
              theLastAdded = (PaintableShape)shapes.get(i);
              theSelectedShapes.add(theLastAdded); 

              Vector group = theLastAdded.getGroup();
              if( group != null )
              {
                for( int j = 0 ; j < group.size() ; j++ )
                {
                  if( !theSelectedShapes.contains((PaintableShape)group.get(j)) && !((PaintableShape)group.get(j)).equals(theLastAdded) )
                  {
                    theSelectedShapes.add((PaintableShape)group.get(j));
                  }
                }
              } 
            }       
          }

          found = true;
          break;
        }
      }
      if( found )
      {
        getDrawingArea().clearDrawingArea();
        setCoverRect();
        paintCover();
        setIsStarted(true);
      }
      else
      { 
        getDrawingArea().clearDrawingArea();
        setIsStarted(false);
        theSelectedShapes      = new Vector();
      }
    }
    else
    {
      setIsStarted(false);
      isResizing = false;
      theLastPoint       = null;
      getDrawingArea().clearDrawingArea();
    }

  }

  public void mouseReleased( MouseEvent e )
  {
    setCursor(OOPSCursors.MOVE_UP);
    if( !getIsStarted() )
      return;

    if( isResizing )
    {

    }
    commitUndo();
    //getDrawingArea().clearDrawingArea();
    theLastPoint       = null;
    setIsStarted(false);
  }

  public void delete()
  {
    if( theSelectedShapes != null )
    {
      getDrawingArea().setUndoPoint(new CompleteUndo(getDrawingArea()));
      for( int i = 0 ; i < theSelectedShapes.size() ; i++ )
      {
        getDrawingArea().removeShape(theSelectedShapes.get(i));
      }
      getDrawingArea().clearDrawingArea();
      getDrawingArea().repaint();
      theSelectedShapes = null;
    }
  }

  public void loseControl()
  {
    getDrawingArea().removeKeyListener(theDeleteListener);
  }

  public void setSelectDrawPaint( Paint aPaint )
  {
    super.setDrawPaint(aPaint);
  }
  public void setSelectFillPaint( Paint aPaint )
  {
    super.setFillPaint(aPaint);
  }
  public void setSelectStroke( BasicStroke aStroke )
  {
    super.setStroke(aStroke);
  }

  public void setDrawPaint( Paint aPaint ){}
  public void setFillPaint( Paint aPaint ){}
  public void setStroke( BasicStroke aStroke ){}

  public void rotate( double aTheta )
  {
    if( theSelectedShapes == null || theSelectedShapes.size() == 0)
      return;

    getDrawingArea().clearDrawingArea();

    Area combination = new Area();
    for( int i = 0 ; i < theSelectedShapes.size() ; i++ )
    {
      combination.add(new Area(((PaintableShape)theSelectedShapes.get(i)).getArea()));
    }

    Rectangle2D.Double bounds = new Rectangle2D.Double();
    bounds.setRect(combination.getBounds2D());

    if( theRotationCenter == null )
      theRotationCenter = new Point2D.Double(bounds.x+bounds.width/2,bounds.y+bounds.height/2);

    AffineTransform transform = AffineTransform.getRotateInstance(aTheta,theRotationCenter.x,theRotationCenter.y);

    if( getUndo() == null )
    {
      setUndo(new TransformUndo(getDrawingArea(),theSelectedShapes));
    }
    ((TransformUndo)getUndo()).transform(transform);

    for( int i = 0 ; i < theSelectedShapes.size() ; i++ )
    {
      ((PaintableShape)theSelectedShapes.get(i)).transform(transform);
    }
    setCoverRect();
    paintCover();
    getDrawingArea().repaint();
  }

  public void resetRotation()
  {
    commitUndo();
    theRotationCenter = null;
  }

  public void resize( double anXFactor, double aYFactor, double aResizor )
  {
    if( theSelectedShapes == null || theSelectedShapes.size() == 0)
      return;

    getDrawingArea().clearDrawingArea();

    Area combination = new Area();
    for( int i = 0 ; i < theSelectedShapes.size() ; i++ )
    {
      combination.add(new Area(((PaintableShape)theSelectedShapes.get(i)).getArea()));
    }

    Rectangle2D.Double combinedBounds = new Rectangle2D.Double();
    combinedBounds.setRect(combination.getBounds2D());
    Rectangle2D.Double newCombinedBounds = null;
    
    double width  = combinedBounds.width*anXFactor;
    double height = combinedBounds.height*aYFactor;
    double x,y;
    if( aResizor == 0 || aResizor == 1 || aResizor == 3)
    {
      x = (combinedBounds.x+combinedBounds.width) - width;
      y = (combinedBounds.y+combinedBounds.height) - height;
      newCombinedBounds = new Rectangle2D.Double(x,y,width,height);
    }
    else if( aResizor == 2 || aResizor == 4 )
    {
      x = combinedBounds.x;
      y = (combinedBounds.y+combinedBounds.height) - height;
      newCombinedBounds = new Rectangle2D.Double(x,y,width,height);
    }
    else if( aResizor == 5 || aResizor == 6 )
    {
      x = (combinedBounds.x+combinedBounds.width) - width;
      y = combinedBounds.y;
      newCombinedBounds = new Rectangle2D.Double(x,y,width,height);
    }
    else if( aResizor == 7 )
    {
      x = combinedBounds.x;
      y = combinedBounds.y;
      newCombinedBounds = new Rectangle2D.Double(x,y,width,height);
    }
    else
    {
      System.out.println("Error");
      return;
    }
    if( newCombinedBounds.width == 0 || newCombinedBounds.width == Double.NEGATIVE_INFINITY || newCombinedBounds.width == Double.POSITIVE_INFINITY  )
      return;
    else if( newCombinedBounds.height == 0 || newCombinedBounds.height == Double.NEGATIVE_INFINITY || newCombinedBounds.height == Double.POSITIVE_INFINITY  )
      return;

    if( getUndo() == null )
    {
      setUndo(new SpecificTransformUndo(getDrawingArea(),theSelectedShapes));
    }

    AffineTransform resizeTransform = AffineTransform.getScaleInstance(anXFactor,aYFactor);
    AffineTransform translateTransform = new AffineTransform();
    double xTranslation, yTranslation;
    Rectangle2D.Double oldBounds     = new Rectangle2D.Double();
    Rectangle2D.Double currentBounds = new Rectangle2D.Double();
    for( int i = 0 ; i < theSelectedShapes.size() ; i++ )
    {
      oldBounds.setRect(((PaintableShape)theSelectedShapes.get(i)).getArea().getBounds2D());
      xTranslation = (oldBounds.x-combinedBounds.x)/combinedBounds.width;
      yTranslation = (oldBounds.y-combinedBounds.y)/combinedBounds.height;

      xTranslation = newCombinedBounds.x + xTranslation*newCombinedBounds.width;
      yTranslation = newCombinedBounds.y + yTranslation*newCombinedBounds.height;

      ((PaintableShape)theSelectedShapes.get(i)).transform(resizeTransform);
      ((SpecificTransformUndo)getUndo()).transform(resizeTransform,(PaintableShape)theSelectedShapes.get(i));
      currentBounds.setRect(((PaintableShape)theSelectedShapes.get(i)).getArea().getBounds2D());

      translateTransform.setToTranslation(xTranslation-currentBounds.x, yTranslation-currentBounds.y);

      ((PaintableShape)theSelectedShapes.get(i)).transform(translateTransform);
      ((SpecificTransformUndo)getUndo()).transform(translateTransform,(PaintableShape)theSelectedShapes.get(i));
    }
    setCoverRect();
    paintCover();
    getDrawingArea().repaint();
  }

  public void skew( double anXFactor, double aYFactor, double aResizor )
  {
    if( theSelectedShapes == null || theSelectedShapes.size() == 0)
      return;

    getDrawingArea().clearDrawingArea();

    AffineTransform shearTransform = AffineTransform.getShearInstance(anXFactor,aYFactor);
    AffineTransform translateTransform = new AffineTransform();

    Rectangle2D.Double oldBounds = new Rectangle2D.Double();

    Area combination = new Area();
    for( int i = 0 ; i < theSelectedShapes.size() ; i++ )
    {
      combination.add(new Area(((PaintableShape)theSelectedShapes.get(i)).getArea()));
    }

    oldBounds.setRect(combination.getBounds2D());

    if( aResizor == 0 )
    { 
      Point2D pnt = shearTransform.transform(new Point2D.Double(oldBounds.x,oldBounds.y+oldBounds.height),null);
      translateTransform.setToTranslation(oldBounds.x-pnt.getX(), 0);
    }
    else if( aResizor == 1 )
    { 
      Point2D pnt = shearTransform.transform(new Point2D.Double(oldBounds.x+oldBounds.width,oldBounds.y),null);
      translateTransform.setToTranslation(0, oldBounds.y-pnt.getY());
    }
    else if( aResizor == 2 )
    { 
      Point2D pnt = shearTransform.transform(new Point2D.Double(oldBounds.x,oldBounds.y),null);
      translateTransform.setToTranslation(0, oldBounds.y-pnt.getY());
    }
    else if( aResizor == 3 )
    { 
      Point2D pnt = shearTransform.transform(new Point2D.Double(oldBounds.x,oldBounds.y),null);
      translateTransform.setToTranslation(oldBounds.x-pnt.getX(), 0);
    }

    if( getUndo() == null )
    {
      setUndo(new TransformUndo(getDrawingArea(),theSelectedShapes));
    }
    ((TransformUndo)getUndo()).transform(shearTransform);
    ((TransformUndo)getUndo()).transform(translateTransform);

    for( int i = 0 ; i < theSelectedShapes.size() ; i++ )
    {
      ((PaintableShape)theSelectedShapes.get(i)).transform(shearTransform);

      ((PaintableShape)theSelectedShapes.get(i)).transform(translateTransform);
    }
    setCoverRect();
    paintCover();
    getDrawingArea().repaint();
  }

  public void subtract()
  {
    if( theSelectedShapes == null || theSelectedShapes.size() == 0)
      return;

    getDrawingArea().setUndoPoint(new CompleteUndo(getDrawingArea()));
    getDrawingArea().clearDrawingArea();

    Area combination = new Area();
    for( int i = 0 ; i < theSelectedShapes.size() ; i++ )
    {
      combination.add(new Area(((PaintableShape)theSelectedShapes.get(i)).getArea()));
    }

    Vector shapes = getDrawingArea().getShapes();
    PaintableShape subtracted;
    for( int i = 0 ; i < shapes.size() ; i++ )
    {
      if( !theSelectedShapes.contains((PaintableShape)shapes.get(i)) )
      {
        subtracted = ((PaintableShape)shapes.get(i)).get();
        subtracted.subtract(combination);
        getDrawingArea().replaceShape((PaintableShape)shapes.get(i),subtracted);
      }
    }
    
    setCoverRect();
    paintCover();
    getDrawingArea().repaint();
  }
  public void intersect()
  {
    if( theSelectedShapes == null || theSelectedShapes.size() == 0)
      return;

    getDrawingArea().setUndoPoint(new CompleteUndo(getDrawingArea()));
    getDrawingArea().clearDrawingArea();

    Area combination = new Area();
    for( int i = 0 ; i < theSelectedShapes.size() ; i++ )
    {
      combination.add(new Area(((PaintableShape)theSelectedShapes.get(i)).getArea()));
    }

    Vector shapes = getDrawingArea().getShapes();
    Area temp;
    Rectangle2D.Double bounds = new Rectangle2D.Double();
    PaintableShape intersected;
    for( int i = 0 ; i < shapes.size() ; i++ )
    {
      if( !theSelectedShapes.contains((PaintableShape)shapes.get(i)) )
      {
        temp = new Area(((PaintableShape)shapes.get(i)).getFillShape());
        temp.add(new Area(((PaintableShape)shapes.get(i)).getDrawShape()));
        temp.intersect(combination);
        bounds.setRect(temp.getBounds2D());
        if( bounds.width > 0 && bounds.height > 0 )
        {
          intersected = ((PaintableShape)shapes.get(i)).get();
          intersected.intersect(combination);
          getDrawingArea().replaceShape((PaintableShape)shapes.get(i),intersected);
        }
      }
    }
    
    setCoverRect();
    paintCover();
    getDrawingArea().repaint();
  }

  public void redraw()
  {
    if( theSelectedShapes == null || theSelectedShapes.size() == 0)
      return;

    getDrawingArea().setUndoPoint(new CompleteUndo(getDrawingArea()));
    getDrawingArea().clearDrawingArea();

    PaintableShape     newShape;
    Rectangle2D.Double theBounds = new Rectangle2D.Double();
    for( int i = 0 ; i < theSelectedShapes.size() ; i++ )
    {
      newShape = ((PaintableShape)theSelectedShapes.get(i)).get();
      theBounds.setRect(newShape.getFillShape().getBounds2D());
      if( theBounds.width > 0 && theBounds.height > 0 )
      {
        newShape.setShape(newShape.getFillShape());
        getDrawingArea().replaceShape((PaintableShape)theSelectedShapes.get(i),newShape);
        theSelectedShapes.set(i,newShape);
      }
    }

    getDrawingArea().repaint();
  }

  public void combine()
  {
    if( theSelectedShapes == null || theSelectedShapes.size() == 0)
      return;

    getDrawingArea().setUndoPoint(new CompleteUndo(getDrawingArea()));
    getDrawingArea().clearDrawingArea();

    Area drawCombination = new Area();
    Area fillCombination = new Area();
    for( int i = 0 ; i < theSelectedShapes.size() ; i++ )
    {
      fillCombination.add(new Area(((PaintableShape)theSelectedShapes.get(i)).getFillShape()));
      drawCombination.add(new Area(((PaintableShape)theSelectedShapes.get(i)).getDrawShape()));
    }

    PaintableShape newShape = ((PaintableShape)theSelectedShapes.get(0)).get();
    newShape.setFillShape(fillCombination);
    newShape.resetFill();
    newShape.setDrawShape(drawCombination);
    for( int i = 0 ; i < theSelectedShapes.size() ; i++ )
    {
      getDrawingArea().removeShape(theSelectedShapes.get(i));
    }

    getDrawingArea().addShape(newShape);
    getDrawingArea().repaint();
    theSelectedShapes = null;
  }

  public void group()
  {
    if( theSelectedShapes == null )
      return;

    getDrawingArea().setUndoPoint(new GroupUndo(getDrawingArea()));

    Vector group = new Vector();
    for( int i = 0 ; i < theSelectedShapes.size() ; i++ )
    {
      group.add((PaintableShape)theSelectedShapes.get(i));
    }
    for( int i = 0 ; i < theSelectedShapes.size() ; i++ )
    {
      ((PaintableShape)theSelectedShapes.get(i)).setGroup(group);
    }
  }

  public void deGroup()
  {
    if( theSelectedShapes == null )
      return;

    getDrawingArea().setUndoPoint(new GroupUndo(getDrawingArea()));

    for( int i = 0 ; i < theSelectedShapes.size() ; i++ )
    {
      ((PaintableShape)theSelectedShapes.get(i)).setGroup(null);
    }

    getDrawingArea().clearDrawingArea();
    setIsStarted(false);
    theSelectedShapes      = new Vector();
  }

  public void moveToBack()
  {
    if( theSelectedShapes != null )
    {
      getDrawingArea().setUndoPoint(new CompleteUndo(getDrawingArea()));
      getDrawingArea().moveToBack(theSelectedShapes);
      getDrawingArea().repaint();
    }
  }

  public void moveToFront()
  {
    if( theSelectedShapes != null )
    {
      getDrawingArea().setUndoPoint(new CompleteUndo(getDrawingArea()));
      getDrawingArea().moveToFront(theSelectedShapes);
      getDrawingArea().repaint();
    }
  }

  public void moveBack()
  {
    if( theSelectedShapes != null )
    {
      getDrawingArea().setUndoPoint(new CompleteUndo(getDrawingArea()));
      getDrawingArea().moveBack(theSelectedShapes);
      getDrawingArea().repaint();
    }
  }

  public void moveUp()
  {
    if( theSelectedShapes != null )
    {
      getDrawingArea().setUndoPoint(new CompleteUndo(getDrawingArea()));
      getDrawingArea().moveUp(theSelectedShapes);
      getDrawingArea().repaint();
    }
  }

  public Vector getSelected()
  {
    if( theSelectedShapes == null || theSelectedShapes.size() == 0 )
      return null;

    Vector clonedShapes = new Vector();
    Vector holder = (Vector)theSelectedShapes.clone();
    while( holder.size() > 0 )
    {
      int index = -1;
      int found = 0;
      for( int i = 0 ; i < holder.size() ; i++ )
      {
        if( getDrawingArea().getLayerManager().indexOf((PaintableShape)holder.get(i)) > index )
        {
          found = i;
          index = getDrawingArea().getLayerManager().indexOf((PaintableShape)holder.get(i));
        }
      }
      clonedShapes.insertElementAt(holder.remove(found),0);
    }
    return clonedShapes;
  }

  public void moveBackALayer()
  {
    if( theSelectedShapes != null )
    {
      getDrawingArea().setUndoPoint(new CompleteUndo(getDrawingArea()));
      getDrawingArea().moveBackALayer(theSelectedShapes);
      theSelectedShapes = null;
      theSelectedShapeCovers = null;
      getDrawingArea().repaint();
    }
  }
  public void moveUpALayer()
  {
    if( theSelectedShapes != null )
    {
      getDrawingArea().setUndoPoint(new CompleteUndo(getDrawingArea()));
      getDrawingArea().moveUpALayer(theSelectedShapes);
      theSelectedShapes = null;
      getDrawingArea().clearDrawingArea();
      getDrawingArea().repaint();
    }
  }

  public void flipHorizontal()
  {
    if( theSelectedShapes == null || theSelectedShapes.size() == 0)
      return;

    getDrawingArea().clearDrawingArea();

    Area combination = new Area();
    for( int i = 0 ; i < theSelectedShapes.size() ; i++ )
    {
      combination.add(new Area(((PaintableShape)theSelectedShapes.get(i)).getArea()));
    }

    Rectangle2D.Double combinedBounds = new Rectangle2D.Double();
    combinedBounds.setRect(combination.getBounds2D());

    double centerX = combinedBounds.x + combinedBounds.width/2;

    if( getUndo() == null )
    {
      getDrawingArea().setUndoPoint(new CompleteUndo(getDrawingArea()));
    }

    AffineTransform resizeTransform = AffineTransform.getScaleInstance(-1,1);
    AffineTransform translateTransform = new AffineTransform();
    double xDiff;
    double newXDiff;
    double xTranslation;
    Rectangle2D.Double oldBounds     = new Rectangle2D.Double();
    Rectangle2D.Double currentBounds = new Rectangle2D.Double();
    for( int i = 0 ; i < theSelectedShapes.size() ; i++ )
    {
      oldBounds.setRect(((PaintableShape)theSelectedShapes.get(i)).getArea().getBounds2D());
      xDiff = -((oldBounds.x + oldBounds.width/2)-centerX);

      PaintableShape holder = ((PaintableShape)theSelectedShapes.get(i)).get();

      holder.transform(resizeTransform);

      currentBounds.setRect(holder.getArea().getBounds2D());
      newXDiff = (currentBounds.x + currentBounds.width/2)-centerX;

      xTranslation = xDiff - newXDiff;
      translateTransform.setToTranslation(xTranslation, 0);

      holder.transform(translateTransform);

      getDrawingArea().replaceShape(((PaintableShape)theSelectedShapes.get(i)),holder);
      theSelectedShapes.set(i,holder);
    }
    setCoverRect();
    paintCover();
    getDrawingArea().repaint();
  }

  public void flipVertical()
  {
    if( theSelectedShapes == null || theSelectedShapes.size() == 0)
      return;

    getDrawingArea().clearDrawingArea();

    Area combination = new Area();
    for( int i = 0 ; i < theSelectedShapes.size() ; i++ )
    {
      combination.add(new Area(((PaintableShape)theSelectedShapes.get(i)).getArea()));
    }

    Rectangle2D.Double combinedBounds = new Rectangle2D.Double();
    combinedBounds.setRect(combination.getBounds2D());

    double centerY = combinedBounds.y + combinedBounds.height/2;

    if( getUndo() == null )
    {
      getDrawingArea().setUndoPoint(new CompleteUndo(getDrawingArea()));
    }

    AffineTransform resizeTransform = AffineTransform.getScaleInstance(1,-1);
    AffineTransform translateTransform = new AffineTransform();
    double yDiff;
    double newYDiff;
    double yTranslation;
    Rectangle2D.Double oldBounds     = new Rectangle2D.Double();
    Rectangle2D.Double currentBounds = new Rectangle2D.Double();
    for( int i = 0 ; i < theSelectedShapes.size() ; i++ )
    {
      oldBounds.setRect(((PaintableShape)theSelectedShapes.get(i)).getArea().getBounds2D());
      yDiff = -((oldBounds.y + oldBounds.height/2)-centerY);

      PaintableShape holder = ((PaintableShape)theSelectedShapes.get(i)).get();

      holder.transform(resizeTransform);

      currentBounds.setRect(holder.getArea().getBounds2D());
      newYDiff = (currentBounds.y + currentBounds.height/2)-centerY;

      yTranslation = yDiff - newYDiff;
      translateTransform.setToTranslation(0,yTranslation);

      holder.transform(translateTransform);

      getDrawingArea().replaceShape(((PaintableShape)theSelectedShapes.get(i)),holder);
      theSelectedShapes.set(i,holder);
    }
    setCoverRect();
    paintCover();
    getDrawingArea().repaint();
  }

  public void align( int aType )
  {
    if( theSelectedShapes == null || theSelectedShapes.size() <= 1 )
      return;

    getDrawingArea().clearDrawingArea();

    Rectangle2D.Double[] bounds = new Rectangle2D.Double[theSelectedShapes.size()];
    for( int i = 0 ; i < bounds.length ; i++ )
    {
      bounds[i] = new Rectangle2D.Double();
      bounds[i].setRect(((PaintableShape)theSelectedShapes.get(i)).getArea().getBounds2D());
    }

    SpecificTransformUndo undo = new SpecificTransformUndo(getDrawingArea(),theSelectedShapes);
    setUndo(undo);
    AffineTransform translateTransform = new AffineTransform();

    if( aType == AlignTypes.RIGHT )
    {
      double x;
      for( int i = 1 ; i < theSelectedShapes.size() ; i++ )
      {
        x = (bounds[0].x + bounds[0].width)-(bounds[i].x + bounds[i].width);
        translateTransform.setToTranslation(x,0);
        ((PaintableShape)theSelectedShapes.get(i)).transform(translateTransform);
        undo.transform(translateTransform,(PaintableShape)theSelectedShapes.get(i));
      }
    }
    else if( aType == AlignTypes.LEFT )
    {
      double x;
      for( int i = 1 ; i < theSelectedShapes.size() ; i++ )
      {
        x = (bounds[0].x)-(bounds[i].x);
        translateTransform.setToTranslation(x,0);
        ((PaintableShape)theSelectedShapes.get(i)).transform(translateTransform);
        undo.transform(translateTransform,(PaintableShape)theSelectedShapes.get(i));
      }
    }
    else if( aType == AlignTypes.TOP )
    {
      double y;
      for( int i = 1 ; i < theSelectedShapes.size() ; i++ )
      {
        y = (bounds[0].y)-(bounds[i].y);
        translateTransform.setToTranslation(0,y);
        ((PaintableShape)theSelectedShapes.get(i)).transform(translateTransform);
        undo.transform(translateTransform,(PaintableShape)theSelectedShapes.get(i));
      }
    }
    else if( aType == AlignTypes.BOTTOM )
    {
      double y;
      for( int i = 1 ; i < theSelectedShapes.size() ; i++ )
      {
        y = (bounds[0].y+bounds[0].height)-(bounds[i].y + bounds[i].height);
        translateTransform.setToTranslation(0,y);
        ((PaintableShape)theSelectedShapes.get(i)).transform(translateTransform);
        undo.transform(translateTransform,(PaintableShape)theSelectedShapes.get(i));
      }
    }
    else if( aType == AlignTypes.CENTER_VERTICAL )
    {
      double x;
      for( int i = 1 ; i < theSelectedShapes.size() ; i++ )
      {
        x = (bounds[0].x+bounds[0].width/2)-(bounds[i].x+bounds[i].width/2);
        translateTransform.setToTranslation(x,0);
        ((PaintableShape)theSelectedShapes.get(i)).transform(translateTransform);
        undo.transform(translateTransform,(PaintableShape)theSelectedShapes.get(i));
      }
    }
    else if( aType == AlignTypes.CENTER_HORIZONTAL )
    {
      double y;
      for( int i = 1 ; i < theSelectedShapes.size() ; i++ )
      {
        y = (bounds[0].y+bounds[0].height/2)-(bounds[i].y+bounds[i].height/2);
        translateTransform.setToTranslation(0,y);
        ((PaintableShape)theSelectedShapes.get(i)).transform(translateTransform);
        undo.transform(translateTransform,(PaintableShape)theSelectedShapes.get(i));
      }
    }
    else if( aType == AlignTypes.CENTERS )
    {
      double x;
      double y;
      for( int i = 1 ; i < theSelectedShapes.size() ; i++ )
      {
        y = (bounds[0].y+bounds[0].height/2)-(bounds[i].y+bounds[i].height/2);
        x = (bounds[0].x+bounds[0].width/2)-(bounds[i].x+bounds[i].width/2);
        translateTransform.setToTranslation(x,y);
        ((PaintableShape)theSelectedShapes.get(i)).transform(translateTransform);
        undo.transform(translateTransform,(PaintableShape)theSelectedShapes.get(i));
      }
    }
    commitUndo();
    getDrawingArea().repaint();
  }

  public void setCoverRect()
  {
    if( theSelectedShapes == null )
      return;

    if( theSelectedShapes.size() == 0 )
    {
      theSelectedShapeCovers.width  = 0;
      theSelectedShapeCovers.height = 0;
    }
    else
    {
      Area combination = new Area();
      for( int i = 0 ; i < theSelectedShapes.size() ; i++ )
      {
        combination.add(new Area(((PaintableShape)theSelectedShapes.get(i)).getArea()));
      }

      theSelectedShapeCovers.setRect(combination.getBounds2D());
    }
    theResizors[0].x = theSelectedShapeCovers.x-theResizorSize;
    theResizors[0].y = theSelectedShapeCovers.y-theResizorSize;
    theResizors[1].x = (theSelectedShapeCovers.x+theSelectedShapeCovers.width/2)-theResizorSize;
    theResizors[1].y = theSelectedShapeCovers.y-theResizorSize;
    theResizors[2].x = (theSelectedShapeCovers.x+theSelectedShapeCovers.width)-theResizorSize;
    theResizors[2].y = theSelectedShapeCovers.y-theResizorSize;
    theResizors[3].x = theSelectedShapeCovers.x-theResizorSize;
    theResizors[3].y = (theSelectedShapeCovers.y+theSelectedShapeCovers.height/2)-theResizorSize;
    theResizors[4].x = (theSelectedShapeCovers.x+theSelectedShapeCovers.width)-theResizorSize;
    theResizors[4].y = (theSelectedShapeCovers.y+theSelectedShapeCovers.height/2)-theResizorSize;
    theResizors[5].x = theSelectedShapeCovers.x-theResizorSize;
    theResizors[5].y = (theSelectedShapeCovers.y+theSelectedShapeCovers.height)-theResizorSize;
    theResizors[6].x = (theSelectedShapeCovers.x+theSelectedShapeCovers.width/2)-theResizorSize;
    theResizors[6].y = (theSelectedShapeCovers.y+theSelectedShapeCovers.height)-theResizorSize;
    theResizors[7].x = (theSelectedShapeCovers.x+theSelectedShapeCovers.width)-theResizorSize;
    theResizors[7].y = (theSelectedShapeCovers.y+theSelectedShapeCovers.height)-theResizorSize;
  }

  private void translateCover( double x, double y)
  {
    theSelectedShapeCovers.x += x;
    theSelectedShapeCovers.y += y;
    for( int i = 0 ; i < 8 ; i ++ )
    {
      theResizors[i].x += x;
      theResizors[i].y += y;
    }
  }

  private void paintCover()
  {
    getDrawingArea().getDrawingGraphics().setStroke(theCoverStroke);

    if( theSelectedShapes.size() == 0 )
      return;

    getDrawingArea().getDrawingGraphics().setColor(Color.black);
    getDrawingArea().getDrawingGraphics().draw(theSelectedShapeCovers);
    getDrawingArea().getDrawingGraphics().setColor(Color.white);
    getDrawingArea().getDrawingGraphics().draw(new Rectangle2D.Double(theSelectedShapeCovers.x-theCoverStroke.getLineWidth(),theSelectedShapeCovers.y-theCoverStroke.getLineWidth(),theSelectedShapeCovers.width+2*theCoverStroke.getLineWidth(),theSelectedShapeCovers.height+2*theCoverStroke.getLineWidth()));

    getDrawingArea().getDrawingGraphics().setColor(Color.black);
    for( int i = 0 ; i < 8 ; i ++ )
      getDrawingArea().getDrawingGraphics().fill(theResizors[i]);

    getDrawingArea().getDrawingGraphics().setColor(Color.white);
    for( int i = 0 ; i < 8 ; i ++ )
      getDrawingArea().getDrawingGraphics().draw(theResizors[i]);
  }

  private void resizeFromResizors(int x, int y)
  {
    Rectangle2D.Double oldRect = new Rectangle2D.Double();
    oldRect.setRect(theSelectedShapeCovers);
    if( theCurrentResizor == 0 )
    {
      theSelectedShapeCovers = new Rectangle2D.Double(x,y,(theSelectedShapeCovers.x-x)+theSelectedShapeCovers.width,(theSelectedShapeCovers.y-y)+theSelectedShapeCovers.height );
    }
    else if( theCurrentResizor == 1 )
    {
      theSelectedShapeCovers = new Rectangle2D.Double(theSelectedShapeCovers.x,y,theSelectedShapeCovers.width,(theSelectedShapeCovers.y-y)+theSelectedShapeCovers.height );
    }
    else if( theCurrentResizor == 2 )
    {
      theSelectedShapeCovers = new Rectangle2D.Double(theSelectedShapeCovers.x,y,x-theSelectedShapeCovers.x,(theSelectedShapeCovers.y-y)+theSelectedShapeCovers.height );
    }
    else if( theCurrentResizor == 3 )
    {
      theSelectedShapeCovers = new Rectangle2D.Double(x,theSelectedShapeCovers.y,(theSelectedShapeCovers.x-x)+theSelectedShapeCovers.width,theSelectedShapeCovers.height );
    }
    else if( theCurrentResizor == 4 )
    {
      theSelectedShapeCovers = new Rectangle2D.Double(theSelectedShapeCovers.x,theSelectedShapeCovers.y,x-theSelectedShapeCovers.x,theSelectedShapeCovers.height );
    }
    else if( theCurrentResizor == 5 )
    {
      theSelectedShapeCovers = new Rectangle2D.Double(x,theSelectedShapeCovers.y,(theSelectedShapeCovers.x-x)+theSelectedShapeCovers.width,y-theSelectedShapeCovers.y );
    }
    else if( theCurrentResizor == 6 )
    {
      theSelectedShapeCovers = new Rectangle2D.Double(theSelectedShapeCovers.x,theSelectedShapeCovers.y,theSelectedShapeCovers.width,y-theSelectedShapeCovers.y );
    }
    else if( theCurrentResizor == 7 )
    {
      theSelectedShapeCovers = new Rectangle2D.Double(theSelectedShapeCovers.x,theSelectedShapeCovers.y,x-theSelectedShapeCovers.x,y-theSelectedShapeCovers.y );
    }

    if( theSelectedShapeCovers.width < 0 )
    {
      theSelectedShapeCovers = new Rectangle2D.Double(theSelectedShapeCovers.x+theSelectedShapeCovers.width,theSelectedShapeCovers.y,-theSelectedShapeCovers.width, theSelectedShapeCovers.height);
      if( theCurrentResizor == 3 )
        theCurrentResizor = 4;
      else if( theCurrentResizor == 4 )
        theCurrentResizor = 3;
      else if( theCurrentResizor == 0 )
        theCurrentResizor = 2;
      else if( theCurrentResizor == 2 )
        theCurrentResizor = 0;
      else if( theCurrentResizor == 5 )
        theCurrentResizor = 7;
      else if( theCurrentResizor == 7 )
        theCurrentResizor = 5;
    }
    if( theSelectedShapeCovers.height < 0 )
    {
      theSelectedShapeCovers = new Rectangle2D.Double(theSelectedShapeCovers.x,theSelectedShapeCovers.y+theSelectedShapeCovers.height,theSelectedShapeCovers.width,-theSelectedShapeCovers.height);
      if( theCurrentResizor == 0 )
        theCurrentResizor = 5;
      else if( theCurrentResizor == 5 )
        theCurrentResizor = 0;
      else if( theCurrentResizor == 1 )
        theCurrentResizor = 6;
      else if( theCurrentResizor == 6 )
        theCurrentResizor = 1;
      else if( theCurrentResizor == 2 )
        theCurrentResizor = 7;
      else if( theCurrentResizor == 7 )
        theCurrentResizor = 2;
    }

    if( theSelectedShapeCovers.width == 0 || theSelectedShapeCovers.height == 0 )
    {
      System.out.println("Zero size!!!!");
      theSelectedShapeCovers.setRect(oldRect);
    theResizors[0].x = theSelectedShapeCovers.x-theResizorSize;
    theResizors[0].y = theSelectedShapeCovers.y-theResizorSize;
    theResizors[1].x = (theSelectedShapeCovers.x+theSelectedShapeCovers.width/2)-theResizorSize;
    theResizors[1].y = theSelectedShapeCovers.y-theResizorSize;
    theResizors[2].x = (theSelectedShapeCovers.x+theSelectedShapeCovers.width)-theResizorSize;
    theResizors[2].y = theSelectedShapeCovers.y-theResizorSize;
    theResizors[3].x = theSelectedShapeCovers.x-theResizorSize;
    theResizors[3].y = (theSelectedShapeCovers.y+theSelectedShapeCovers.height/2)-theResizorSize;
    theResizors[4].x = (theSelectedShapeCovers.x+theSelectedShapeCovers.width)-theResizorSize;
    theResizors[4].y = (theSelectedShapeCovers.y+theSelectedShapeCovers.height/2)-theResizorSize;
    theResizors[5].x = theSelectedShapeCovers.x-theResizorSize;
    theResizors[5].y = (theSelectedShapeCovers.y+theSelectedShapeCovers.height)-theResizorSize;
    theResizors[6].x = (theSelectedShapeCovers.x+theSelectedShapeCovers.width/2)-theResizorSize;
    theResizors[6].y = (theSelectedShapeCovers.y+theSelectedShapeCovers.height)-theResizorSize;
    theResizors[7].x = (theSelectedShapeCovers.x+theSelectedShapeCovers.width)-theResizorSize;
    theResizors[7].y = (theSelectedShapeCovers.y+theSelectedShapeCovers.height)-theResizorSize;
      getDrawingArea().clearDrawingArea();
      paintCover();
    }
    else
    {
    theResizors[0].x = theSelectedShapeCovers.x-theResizorSize;
    theResizors[0].y = theSelectedShapeCovers.y-theResizorSize;
    theResizors[1].x = (theSelectedShapeCovers.x+theSelectedShapeCovers.width/2)-theResizorSize;
    theResizors[1].y = theSelectedShapeCovers.y-theResizorSize;
    theResizors[2].x = (theSelectedShapeCovers.x+theSelectedShapeCovers.width)-theResizorSize;
    theResizors[2].y = theSelectedShapeCovers.y-theResizorSize;
    theResizors[3].x = theSelectedShapeCovers.x-theResizorSize;
    theResizors[3].y = (theSelectedShapeCovers.y+theSelectedShapeCovers.height/2)-theResizorSize;
    theResizors[4].x = (theSelectedShapeCovers.x+theSelectedShapeCovers.width)-theResizorSize;
    theResizors[4].y = (theSelectedShapeCovers.y+theSelectedShapeCovers.height/2)-theResizorSize;
    theResizors[5].x = theSelectedShapeCovers.x-theResizorSize;
    theResizors[5].y = (theSelectedShapeCovers.y+theSelectedShapeCovers.height)-theResizorSize;
    theResizors[6].x = (theSelectedShapeCovers.x+theSelectedShapeCovers.width/2)-theResizorSize;
    theResizors[6].y = (theSelectedShapeCovers.y+theSelectedShapeCovers.height)-theResizorSize;
    theResizors[7].x = (theSelectedShapeCovers.x+theSelectedShapeCovers.width)-theResizorSize;
    theResizors[7].y = (theSelectedShapeCovers.y+theSelectedShapeCovers.height)-theResizorSize;
      resize(theSelectedShapeCovers.width/oldRect.width,theSelectedShapeCovers.height/oldRect.height,theCurrentResizor);
    }
  }

  public JComponent getToolbar()
  {
    return theSelectionToolbar;
  }
}