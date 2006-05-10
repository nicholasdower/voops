/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.awt.event.MouseEvent;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Paint;
import java.awt.geom.Area;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.util.Vector;
import java.awt.geom.GeneralPath;
import java.awt.geom.Area;
import java.awt.geom.AffineTransform;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;
import javax.swing.JComponent;
import java.awt.Cursor;

public class SubSelectFreeTool extends Tool implements Selectable
{
  private SmoothPath  thePath;

  private Point theDragPoint;

  private boolean isSelecting = true;
  private boolean isDragging  = false;
  private AffineTransform theTransform;

  private Vector theIntersectedShapes;
  private Vector theDifferenceShapes;
  private Vector theOldShapes;

  private KeyListener theDeleteListener; 

  private SelectionToolbar theSelectionToolbar;

  private Point2D.Double theRotationCenter;

  public SubSelectFreeTool( DrawingArea aDrawingArea, int aType )
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

        if( ( xTranslation != 0 || yTranslation != 0 ) && theIntersectedShapes != null )
        {
          if( theIntersectedShapes == null && !isSelecting )
            enterPointOfNoReturn();

          if( theIntersectedShapes == null )
            return;

          getDrawingArea().clearDrawingArea();

          if( theIntersectedShapes.size() > 0 )
          {
            if( getUndo() == null )
            {
              setUndo(new TranslateUndo(getDrawingArea(),theIntersectedShapes));
            }
            ((TranslateUndo)getUndo()).translate(xTranslation,yTranslation);

            for( int i = 0 ; i < theIntersectedShapes.size() ; i++ )
            {
              ((PaintableShape)theIntersectedShapes.get(i)).translate(xTranslation,yTranslation,true);
              getShape().translate(xTranslation,yTranslation);
            }
            getDrawingArea().paintShape(getShape());
          }
        }
      }
    };
    getDrawingArea().addKeyListener(theDeleteListener);
    getDrawingArea().requestFocusInWindow();

    theSelectionToolbar = new SelectionToolbar(this);

    setCursor(OOPSCursors.SUB_SELECT_FREE);
  }

  public SubSelectFreeTool( DrawingArea aDrawingArea )
  {
    this(aDrawingArea, PaintableShape.TYPE_BOTH);
  }

  public void mouseDragged( MouseEvent e )
  {
    if( !getIsStarted() )
      return;

    if( isSelecting )
    {
      drawCurve(e.getX(),e.getY());
    }
    else
      move(e.getX(),e.getY());
  }

  public void mousePressed( MouseEvent e )
  {
    if( isSelecting )
    {
      if( !getIsStarted() )
      {
        thePath = new SmoothPath(.45,14);
        thePath.start(e.getX(),e.getY());

        setIsStarted(true);
      }
      else
      {
        setIsStarted(false);
        thePath = null;
        getDrawingArea().clearDrawingArea();
        isSelecting = true;
      }
    }
    else
    {
      if( !getIsStarted() )
      {
        if( getShape().contains(e.getX(),e.getY()) )
        {
          setIsStarted(true);
          theDragPoint = new Point(e.getX(),e.getY());
          if( !isDragging )
          {
            enterPointOfNoReturn();
          }
        }
        else
        {
          if( isDragging )
          {
            {}//commitMove();
            getDrawingArea().repaint();
          }

          thePath = new SmoothPath(.45,14);
          thePath.start(e.getX(),e.getY());

          setIsStarted(true);
          isSelecting = true;
          theIntersectedShapes = null;
          theDifferenceShapes = null;
          isDragging = false;
          getDrawingArea().clearDrawingArea();
          drawCurve(e.getX(),e.getY());
        }
      }
      else
      {
        setIsStarted(false);
        cancelMove();
      }
    }
  }

  public void mouseReleased( MouseEvent e )
  {
    if( !isSelecting )
    {
      if( !getIsStarted() )
        return;

      if( !isDragging )
      {
        thePath = null;
        isSelecting = true;
      }

      commitUndo();

      setIsStarted(false);
    }
    else
    {
      if( !getIsStarted() )
        return;

      isDragging = false;
      isSelecting = false;
      setIsStarted(false);
      thePath.closePath();
      getShape().setShape(thePath.getPath());
      paintShape();
    }
  }

  private void drawCurve(int x, int y)
  {
    if( thePath == null )
      return;

    thePath.addPoint(x,y);
    getShape().setShape(thePath.getWithLastPoints(x,y));
    paintShape();
  }

  private void move(int x, int y)
  {
      getShape().translate(x-theDragPoint.x,y-theDragPoint.y);
      if( getUndo() == null )
      {
        setUndo(new TranslateUndo(getDrawingArea(),theIntersectedShapes));
      }
      ((TranslateUndo)getUndo()).translate(x-theDragPoint.x,y-theDragPoint.y);

      getDrawingArea().clearDrawingArea();
      for( int i = 0 ; i < theIntersectedShapes.size() ; i++ )
      {
        ((PaintableShape)theIntersectedShapes.get(i)).translate(x-theDragPoint.x,y-theDragPoint.y,true);
        getDrawingArea().paintShape(((PaintableShape)theIntersectedShapes.get(i)));
      }
      theDragPoint = new Point(x,y);
      getDrawingArea().paintShape(getShape());
  }

  private void cancelMove()
  {
    for( int i = 0 ; i < theDifferenceShapes.size() ; i++ )
    {
      getDrawingArea().replaceShape(((PaintableShape)(theDifferenceShapes.get(i))),((PaintableShape)theOldShapes.get(i)));
    }

    for( int i = 0 ; i < theIntersectedShapes.size() ; i++ )
    {
      getDrawingArea().removeShape(((PaintableShape)(theIntersectedShapes.get(i))));
    }
    theDifferenceShapes = null;
    getDrawingArea().clearDrawingArea();
    isDragging = false;
    isSelecting = true;
    theIntersectedShapes = null;
  }

  private void commitMove()
  {
    for( int i = 0 ; i < theIntersectedShapes.size() ; i++ )
    {
      getDrawingArea().insertShape((PaintableShape)theDifferenceShapes.get(i),(PaintableShape)theIntersectedShapes.get(i));
    }
  }

  public void delete()
  {
    if( theDifferenceShapes == null && !isSelecting )
      enterPointOfNoReturn();

    getDrawingArea().setUndoPoint(new CompleteUndo(getDrawingArea()));

    for( int i = 0 ; i < theIntersectedShapes.size() ; i++ )
    {
      getDrawingArea().removeShape((PaintableShape)theIntersectedShapes.get(i));
    }

    theDifferenceShapes = null;
    theIntersectedShapes = null;
    getDrawingArea().clearDrawingArea();
    getDrawingArea().repaint();
    isSelecting = true;
    isDragging  = false;
    setIsStarted(false);
  }

  public void loseControl()
  {
    if( isDragging )
    {
      getDrawingArea().repaint();
    }

    getDrawingArea().removeKeyListener(theDeleteListener);
  }


  private void enterPointOfNoReturn()
  {
    getDrawingArea().setUndoPoint(new CompleteUndo(getDrawingArea()));

    Vector shapes = getDrawingArea().getShapes();

    Area intersectedArea;
    Area differenceArea;

    PaintableShape intersectedShape;
    PaintableShape differenceShape;

    theIntersectedShapes = new Vector();
    theDifferenceShapes  = new Vector();
    theOldShapes         = new Vector();

    for( int i = 0 ; i < shapes.size() ; i++ )
    {
      intersectedShape = ((PaintableShape)(shapes.get(i))).get();
      if( intersectedShape.intersects(getShape()) )
      {
        isDragging = true;

        theOldShapes.add((PaintableShape)(shapes.get(i)));

        intersectedShape.intersect(getShape());
        theIntersectedShapes.add(intersectedShape);

        differenceShape = ((PaintableShape)(shapes.get(i))).get();
        differenceShape.subtract(getShape());
        theDifferenceShapes.add(differenceShape);

        differenceShape.addToGroup(((PaintableShape)(theOldShapes.get(theOldShapes.size()-1))).getGroup());
        ((PaintableShape)(theOldShapes.get(theOldShapes.size()-1))).removeFromGroup();
        getDrawingArea().replaceShape(((PaintableShape)(theOldShapes.get(theOldShapes.size()-1))),((PaintableShape)theDifferenceShapes.get(theDifferenceShapes.size()-1)));
      }
    }
    if( !isDragging )
    {
      getDrawingArea().clearDrawingArea();
    }
    commitMove();
    getDrawingArea().repaint();
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
    if( theIntersectedShapes == null && !isSelecting )
      enterPointOfNoReturn();

    if( theIntersectedShapes == null || isSelecting )
      return;

    getDrawingArea().clearDrawingArea();

    Area combination = new Area();
    for( int i = 0 ; i < theIntersectedShapes.size() ; i++ )
    {
      combination.add(new Area(((PaintableShape)theIntersectedShapes.get(i)).getArea()));
    }

    Rectangle2D.Double bounds = new Rectangle2D.Double();
    bounds.setRect(combination.getBounds2D());

    if( theRotationCenter == null )
      theRotationCenter = new Point2D.Double(bounds.x+bounds.width/2,bounds.y+bounds.height/2);

    AffineTransform transform = AffineTransform.getRotateInstance(aTheta,theRotationCenter.x,theRotationCenter.y);

    if( getUndo() == null )
    {
      setUndo(new TransformUndo(getDrawingArea(),theIntersectedShapes));
    }
    ((TransformUndo)getUndo()).transform(transform);

    getShape().transform(transform);

    for( int i = 0 ; i < theIntersectedShapes.size() ; i++ )
    {
      ((PaintableShape)theIntersectedShapes.get(i)).transform(transform);
      getDrawingArea().paintShape(((PaintableShape)theIntersectedShapes.get(i)));
    }
    getDrawingArea().paintShape(getShape());
    //getDrawingArea().repaint();
  }

  public void resetRotation()
  {
    commitUndo();
    theRotationCenter = null;
  }

  public void resize( double anXFactor, double aYFactor, double aResizor )
  {
    if( theIntersectedShapes == null && !isSelecting )
      enterPointOfNoReturn();

    if( theIntersectedShapes == null )
      return;

    getDrawingArea().clearDrawingArea();

    Area combination = new Area();
    for( int i = 0 ; i < theIntersectedShapes.size() ; i++ )
    {
      combination.add(new Area(((PaintableShape)theIntersectedShapes.get(i)).getArea()));
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
      setUndo(new SpecificTransformUndo(getDrawingArea(),theIntersectedShapes));
    }

    AffineTransform resizeTransform = AffineTransform.getScaleInstance(anXFactor,aYFactor);
    AffineTransform translateTransform;
    double xTranslation, yTranslation;
    Rectangle2D.Double oldBounds     = new Rectangle2D.Double();
    Rectangle2D.Double currentBounds = new Rectangle2D.Double();


    oldBounds.setRect(getShape().getArea().getBounds2D());
    xTranslation = (oldBounds.x-combinedBounds.x)/combinedBounds.width;
    yTranslation = (oldBounds.y-combinedBounds.y)/combinedBounds.height;

    xTranslation = newCombinedBounds.x + xTranslation*newCombinedBounds.width;
    yTranslation = newCombinedBounds.y + yTranslation*newCombinedBounds.height;

    getShape().transform(resizeTransform);
    currentBounds.setRect(getShape().getArea().getBounds2D());

    translateTransform = AffineTransform.getTranslateInstance(xTranslation-currentBounds.x, yTranslation-currentBounds.y);

    getShape().transform(translateTransform);

    for( int i = 0 ; i < theIntersectedShapes.size() ; i++ )
    {
      oldBounds.setRect(((PaintableShape)theIntersectedShapes.get(i)).getArea().getBounds2D());
      xTranslation = (oldBounds.x-combinedBounds.x)/combinedBounds.width;
      yTranslation = (oldBounds.y-combinedBounds.y)/combinedBounds.height;

      xTranslation = newCombinedBounds.x + xTranslation*newCombinedBounds.width;
      yTranslation = newCombinedBounds.y + yTranslation*newCombinedBounds.height;

      ((PaintableShape)theIntersectedShapes.get(i)).transform(resizeTransform);
      ((SpecificTransformUndo)getUndo()).transform(resizeTransform,(PaintableShape)theIntersectedShapes.get(i));
      currentBounds.setRect(((PaintableShape)theIntersectedShapes.get(i)).getArea().getBounds2D());

      translateTransform = AffineTransform.getTranslateInstance(xTranslation-currentBounds.x, yTranslation-currentBounds.y);

      ((PaintableShape)theIntersectedShapes.get(i)).transform(translateTransform);
      ((SpecificTransformUndo)getUndo()).transform(translateTransform,(PaintableShape)theIntersectedShapes.get(i));

      getDrawingArea().paintShape((PaintableShape)(theIntersectedShapes.get(i)));
    }
    getDrawingArea().paintShape(getShape());
    //getDrawingArea().repaint();
  }
 
  public void skew( double anXFactor, double aYFactor, double aResizor )
  {
    if( theIntersectedShapes == null && !isSelecting )
      enterPointOfNoReturn();

    if( theIntersectedShapes == null )
      return;

    getDrawingArea().clearDrawingArea();

    AffineTransform shearTransform = AffineTransform.getShearInstance(anXFactor,aYFactor);
    AffineTransform translateTransform = new AffineTransform();

    Rectangle2D.Double oldBounds = new Rectangle2D.Double();

    Area combination = new Area();
    for( int i = 0 ; i < theIntersectedShapes.size() ; i++ )
    {
      combination.add(new Area(((PaintableShape)theIntersectedShapes.get(i)).getArea()));
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

    getShape().transform(shearTransform);
    getShape().transform(translateTransform);

    if( getUndo() == null )
    {
      setUndo(new TransformUndo(getDrawingArea(),theIntersectedShapes));
    }
    ((TransformUndo)getUndo()).transform(shearTransform);
    ((TransformUndo)getUndo()).transform(translateTransform);

    for( int i = 0 ; i < theIntersectedShapes.size() ; i++ )
    {
      ((PaintableShape)theIntersectedShapes.get(i)).transform(shearTransform);

      ((PaintableShape)theIntersectedShapes.get(i)).transform(translateTransform);

      getDrawingArea().paintShape((PaintableShape)(theIntersectedShapes.get(i)));
    }
    getDrawingArea().paintShape(getShape());
  }

  public void subtract()
  {
    if( theIntersectedShapes == null && !isSelecting )
      enterPointOfNoReturn();

    if( theIntersectedShapes == null )
      return;

    getDrawingArea().setUndoPoint(new CompleteUndo(getDrawingArea()));
    getDrawingArea().clearDrawingArea();

    Area combination = new Area();
    for( int i = 0 ; i < theIntersectedShapes.size() ; i++ )
    {
      combination.add(new Area(((PaintableShape)theIntersectedShapes.get(i)).getArea()));
    }

    Vector shapes = getDrawingArea().getShapes();
    PaintableShape subtracted;
    for( int i = 0 ; i < shapes.size() ; i++ )
    {
      if( !theIntersectedShapes.contains((PaintableShape)shapes.get(i)) )
      {
        subtracted = ((PaintableShape)shapes.get(i)).get();
        subtracted.subtract(combination);
        if( theDifferenceShapes.contains(shapes.get(i)) )
        {
          theDifferenceShapes.set(theDifferenceShapes.indexOf((PaintableShape)shapes.get(i)),subtracted);
        }
        getDrawingArea().replaceShape((PaintableShape)shapes.get(i),subtracted);
      }
    }
    
    for( int i = 0 ; i < theIntersectedShapes.size() ; i++ )
    {
      getDrawingArea().paintShape((PaintableShape)(theIntersectedShapes.get(i)));
    }
    getDrawingArea().paintShape(getShape());
  }

  public void intersect()
  {
    if( theIntersectedShapes == null && !isSelecting )
      enterPointOfNoReturn();

    if( theIntersectedShapes == null )
      return;

    getDrawingArea().setUndoPoint(new CompleteUndo(getDrawingArea()));
    getDrawingArea().clearDrawingArea();

    Area combination = new Area();
    for( int i = 0 ; i < theIntersectedShapes.size() ; i++ )
    {
      combination.add(new Area(((PaintableShape)theIntersectedShapes.get(i)).getArea()));
    }

    Vector shapes = getDrawingArea().getShapes();
    Area temp;
    Rectangle2D.Double bounds = new Rectangle2D.Double();
    PaintableShape intersected;
    for( int i = 0 ; i < shapes.size() ; i++ )
    {
      if( !theIntersectedShapes.contains((PaintableShape)shapes.get(i)) )
      {
        temp = new Area(((PaintableShape)shapes.get(i)).getFillShape());
        temp.add(new Area(((PaintableShape)shapes.get(i)).getDrawShape()));
        temp.intersect(combination);
        bounds.setRect(temp.getBounds2D());
        if( bounds.width > 0 && bounds.height > 0 )
        {
          intersected = ((PaintableShape)shapes.get(i)).get();
          intersected.intersect(combination);
          if( theDifferenceShapes.contains(shapes.get(i)) )
          {
            theDifferenceShapes.set(theDifferenceShapes.indexOf((PaintableShape)shapes.get(i)),intersected);
          }
          getDrawingArea().replaceShape((PaintableShape)shapes.get(i),intersected);
        }
      }
    }
    
    for( int i = 0 ; i < theIntersectedShapes.size() ; i++ )
    {
      getDrawingArea().paintShape((PaintableShape)(theIntersectedShapes.get(i)));
    }
    getDrawingArea().paintShape(getShape());
  }

  public void redraw()
  {
    if( theIntersectedShapes == null && !isSelecting )
      enterPointOfNoReturn();

    if( theIntersectedShapes == null )
      return;

    getDrawingArea().setUndoPoint(new CompleteUndo(getDrawingArea()));
    getDrawingArea().clearDrawingArea();

    PaintableShape     newShape;
    Rectangle2D.Double theBounds = new Rectangle2D.Double();
    for( int i = 0 ; i < theIntersectedShapes.size() ; i++ )
    {
      newShape = ((PaintableShape)theIntersectedShapes.get(i)).get();
      theBounds.setRect(newShape.getFillShape().getBounds2D());
      if( theBounds.width > 0 && theBounds.height > 0 )
      {
        newShape.setShape(newShape.getFillShape());
        getDrawingArea().replaceShape((PaintableShape)theIntersectedShapes.get(i),newShape);
        theIntersectedShapes.set(i,newShape);
      }
    }

    getDrawingArea().repaint();
    setIsStarted(false);
    isSelecting = true;
    isDragging = false;
    theDifferenceShapes = null;
    theIntersectedShapes = null;
  }

  public void combine()
  {
    if( theIntersectedShapes == null && !isSelecting )
      enterPointOfNoReturn();

    if( theIntersectedShapes == null )
      return;

    getDrawingArea().setUndoPoint(new CompleteUndo(getDrawingArea()));
    getDrawingArea().clearDrawingArea();

    Area drawCombination = new Area();
    Area fillCombination = new Area();
    for( int i = 0 ; i < theIntersectedShapes.size() ; i++ )
    {
      fillCombination.add(new Area(((PaintableShape)theIntersectedShapes.get(i)).getFillShape()));
      drawCombination.add(new Area(((PaintableShape)theIntersectedShapes.get(i)).getDrawShape()));
    }

    PaintableShape newShape = ((PaintableShape)theIntersectedShapes.get(0)).get();
    newShape.setShape(fillCombination);
    newShape.resetFill();
    getDrawingArea().addShape(newShape);

    for( int i = 0 ; i < theIntersectedShapes.size() ; i++ )
    {
      getDrawingArea().removeShape((PaintableShape)theIntersectedShapes.get(i));
    }

    getDrawingArea().repaint();
    setIsStarted(false);
    isSelecting = true;
    isDragging = false;
    theDifferenceShapes = null;
    theIntersectedShapes = null;
  }

  public void group()
  {
    if( theIntersectedShapes == null && !isSelecting )
      enterPointOfNoReturn();

    if( theIntersectedShapes == null )
      return;

    getDrawingArea().setUndoPoint(new GroupUndo(getDrawingArea()));

    Vector group = new Vector();
    for( int i = 0 ; i < theIntersectedShapes.size() ; i++ )
    {
      group.add((PaintableShape)theIntersectedShapes.get(i));
    }
    for( int i = 0 ; i < theIntersectedShapes.size() ; i++ )
    {
      ((PaintableShape)theIntersectedShapes.get(i)).setGroup(group);
    }
    getDrawingArea().clearDrawingArea();
    {}//commitMove();
    getDrawingArea().repaint();
    setIsStarted(false);
    isSelecting = true;
    isDragging = false;
    theDifferenceShapes = null;
    theIntersectedShapes = null;
  }
  public void deGroup()
  {
    if( theIntersectedShapes == null && !isSelecting )
      enterPointOfNoReturn();

    if( theIntersectedShapes == null )
      return;

    getDrawingArea().setUndoPoint(new GroupUndo(getDrawingArea()));

    for( int i = 0 ; i < theIntersectedShapes.size() ; i++ )
    {
      ((PaintableShape)theIntersectedShapes.get(i)).setGroup(null);
    }
    getDrawingArea().clearDrawingArea();
    {}//commitMove();
    getDrawingArea().repaint();
    setIsStarted(false);
    isSelecting = true;
    isDragging = false;
    theDifferenceShapes = null;
    theIntersectedShapes = null;
  }

  public void moveToFront()
  {
    if( theIntersectedShapes == null && !isSelecting )
      enterPointOfNoReturn();

    if( theIntersectedShapes == null )
      return;

    getDrawingArea().setUndoPoint(new CompleteUndo(getDrawingArea()));
    getDrawingArea().moveToFront(theIntersectedShapes);

    getDrawingArea().clearDrawingArea();
    getDrawingArea().repaint();
    setIsStarted(false);
    isSelecting = true;
    isDragging = false;
    theDifferenceShapes = null;
    theIntersectedShapes = null;
  }

  public void moveToBack()
  {
    if( theIntersectedShapes == null && !isSelecting )
      enterPointOfNoReturn();

    if( theIntersectedShapes == null )
      return;

    getDrawingArea().setUndoPoint(new CompleteUndo(getDrawingArea()));
    getDrawingArea().moveToBack(theIntersectedShapes);

    getDrawingArea().clearDrawingArea();
    getDrawingArea().repaint();
    setIsStarted(false);
    isSelecting = true;
    isDragging = false;
    theDifferenceShapes = null;
    theIntersectedShapes = null;
  }

  public void moveBack()
  {
    if( theIntersectedShapes == null && !isSelecting )
      enterPointOfNoReturn();

    if( theIntersectedShapes == null )
      return;

    getDrawingArea().setUndoPoint(new CompleteUndo(getDrawingArea()));
    getDrawingArea().moveBack(theIntersectedShapes);

    getDrawingArea().clearDrawingArea();
    getDrawingArea().repaint();
    setIsStarted(false);
    isSelecting = true;
    isDragging = false;
    theDifferenceShapes = null;
    theIntersectedShapes = null;
  }
  public void moveUp()
  {
    if( theIntersectedShapes == null && !isSelecting )
      enterPointOfNoReturn();

    if( theIntersectedShapes == null )
      return;

    getDrawingArea().setUndoPoint(new CompleteUndo(getDrawingArea()));
    getDrawingArea().moveUp(theIntersectedShapes);

    getDrawingArea().clearDrawingArea();
    getDrawingArea().repaint();
    setIsStarted(false);
    isSelecting = true;
    isDragging = false;
    theDifferenceShapes = null;
    theIntersectedShapes = null;
  }

  public Vector getSelected()
  {
    if( theIntersectedShapes == null && !isSelecting )
      enterPointOfNoReturn();

    if( theIntersectedShapes == null || theIntersectedShapes.size() == 0 )
      return null;

    Vector clonedShapes = new Vector();
    Vector holder = (Vector)theIntersectedShapes.clone();
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

  public void flipHorizontal()
  {
    if( theIntersectedShapes == null && !isSelecting )
      enterPointOfNoReturn();

    if( theIntersectedShapes == null || theIntersectedShapes.size() == 0 )
      return;

    getDrawingArea().clearDrawingArea();

    Area combination = new Area();
    for( int i = 0 ; i < theIntersectedShapes.size() ; i++ )
    {
      combination.add(new Area(((PaintableShape)theIntersectedShapes.get(i)).getArea()));
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
    for( int i = 0 ; i < theIntersectedShapes.size() ; i++ )
    {
      oldBounds.setRect(((PaintableShape)theIntersectedShapes.get(i)).getArea().getBounds2D());
      xDiff = -((oldBounds.x + oldBounds.width/2)-centerX);

      PaintableShape holder = ((PaintableShape)theIntersectedShapes.get(i)).get();

      holder.transform(resizeTransform);

      currentBounds.setRect(holder.getArea().getBounds2D());
      newXDiff = (currentBounds.x + currentBounds.width/2)-centerX;

      xTranslation = xDiff - newXDiff;
      translateTransform.setToTranslation(xTranslation, 0);

      holder.transform(translateTransform);

      getDrawingArea().replaceShape(((PaintableShape)theIntersectedShapes.get(i)),holder);
      theIntersectedShapes.set(i,holder);
    }
    getDrawingArea().paintShape(getShape());
    getDrawingArea().repaint();
  }

  public void flipVertical()
  {
    if( theIntersectedShapes == null && !isSelecting )
      enterPointOfNoReturn();

    if( theIntersectedShapes == null || theIntersectedShapes.size() == 0 )
      return;

    getDrawingArea().clearDrawingArea();

    Area combination = new Area();
    for( int i = 0 ; i < theIntersectedShapes.size() ; i++ )
    {
      combination.add(new Area(((PaintableShape)theIntersectedShapes.get(i)).getArea()));
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
    for( int i = 0 ; i < theIntersectedShapes.size() ; i++ )
    {
      oldBounds.setRect(((PaintableShape)theIntersectedShapes.get(i)).getArea().getBounds2D());
      yDiff = -((oldBounds.y + oldBounds.height/2)-centerY);

      PaintableShape holder = ((PaintableShape)theIntersectedShapes.get(i)).get();

      holder.transform(resizeTransform);

      currentBounds.setRect(holder.getArea().getBounds2D());
      newYDiff = (currentBounds.y + currentBounds.height/2)-centerY;

      yTranslation = yDiff - newYDiff;
      translateTransform.setToTranslation(0,yTranslation);

      holder.transform(translateTransform);

      getDrawingArea().replaceShape(((PaintableShape)theIntersectedShapes.get(i)),holder);
      theIntersectedShapes.set(i,holder);
    }
    getDrawingArea().paintShape(getShape());
    getDrawingArea().repaint();
  }

  public void moveBackALayer()
  {
    if( theIntersectedShapes == null && !isSelecting )
      enterPointOfNoReturn();

    if( theIntersectedShapes == null )
      return;

    getDrawingArea().setUndoPoint(new CompleteUndo(getDrawingArea()));
    getDrawingArea().moveBackALayer(theIntersectedShapes);

    getDrawingArea().clearDrawingArea();
    getDrawingArea().repaint();
    setIsStarted(false);
    isSelecting = true;
    isDragging = false;
    theDifferenceShapes = null;
    theIntersectedShapes = null;
  }

  public void moveUpALayer()
  {
    if( theIntersectedShapes == null && !isSelecting )
      enterPointOfNoReturn();

    if( theIntersectedShapes == null )
      return;

    getDrawingArea().setUndoPoint(new CompleteUndo(getDrawingArea()));
    getDrawingArea().moveUpALayer(theIntersectedShapes);

    getDrawingArea().clearDrawingArea();
    getDrawingArea().repaint();
    setIsStarted(false);
    isSelecting = true;
    isDragging = false;
    theDifferenceShapes = null;
    theIntersectedShapes = null;
  }

  public void align( int aType )
  {
    if( theIntersectedShapes == null && !isSelecting )
      enterPointOfNoReturn();

    if( theIntersectedShapes == null || theIntersectedShapes.size() <= 1 )
      return;

    getDrawingArea().clearDrawingArea();

    Rectangle2D.Double[] bounds = new Rectangle2D.Double[theIntersectedShapes.size()];
    for( int i = 0 ; i < bounds.length ; i++ )
    {
      bounds[i] = new Rectangle2D.Double();
      bounds[i].setRect(((PaintableShape)theIntersectedShapes.get(i)).getArea().getBounds2D());
    }

    SpecificTransformUndo undo = new SpecificTransformUndo(getDrawingArea(),theIntersectedShapes);
    setUndo(undo);
    AffineTransform translateTransform = new AffineTransform();

    if( aType == AlignTypes.RIGHT )
    {
      double x;
      for( int i = 1 ; i < theIntersectedShapes.size() ; i++ )
      {
        x = (bounds[0].x + bounds[0].width)-(bounds[i].x + bounds[i].width);
        translateTransform.setToTranslation(x,0);
        ((PaintableShape)theIntersectedShapes.get(i)).transform(translateTransform);
        undo.transform(translateTransform,(PaintableShape)theIntersectedShapes.get(i));
      }
    }
    else if( aType == AlignTypes.LEFT )
    {
      double x;
      for( int i = 1 ; i < theIntersectedShapes.size() ; i++ )
      {
        x = (bounds[0].x)-(bounds[i].x);
        translateTransform.setToTranslation(x,0);
        ((PaintableShape)theIntersectedShapes.get(i)).transform(translateTransform);
        undo.transform(translateTransform,(PaintableShape)theIntersectedShapes.get(i));
      }
    }
    else if( aType == AlignTypes.TOP )
    {
      double y;
      for( int i = 1 ; i < theIntersectedShapes.size() ; i++ )
      {
        y = (bounds[0].y)-(bounds[i].y);
        translateTransform.setToTranslation(0,y);
        ((PaintableShape)theIntersectedShapes.get(i)).transform(translateTransform);
        undo.transform(translateTransform,(PaintableShape)theIntersectedShapes.get(i));
      }
    }
    else if( aType == AlignTypes.BOTTOM )
    {
      double y;
      for( int i = 1 ; i < theIntersectedShapes.size() ; i++ )
      {
        y = (bounds[0].y+bounds[0].height)-(bounds[i].y + bounds[i].height);
        translateTransform.setToTranslation(0,y);
        ((PaintableShape)theIntersectedShapes.get(i)).transform(translateTransform);
        undo.transform(translateTransform,(PaintableShape)theIntersectedShapes.get(i));
      }
    }
    else if( aType == AlignTypes.CENTER_VERTICAL )
    {
      double x;
      for( int i = 1 ; i < theIntersectedShapes.size() ; i++ )
      {
        x = (bounds[0].x+bounds[0].width/2)-(bounds[i].x+bounds[i].width/2);
        translateTransform.setToTranslation(x,0);
        ((PaintableShape)theIntersectedShapes.get(i)).transform(translateTransform);
        undo.transform(translateTransform,(PaintableShape)theIntersectedShapes.get(i));
      }
    }
    else if( aType == AlignTypes.CENTER_HORIZONTAL )
    {
      double y;
      for( int i = 1 ; i < theIntersectedShapes.size() ; i++ )
      {
        y = (bounds[0].y+bounds[0].height/2)-(bounds[i].y+bounds[i].height/2);
        translateTransform.setToTranslation(0,y);
        ((PaintableShape)theIntersectedShapes.get(i)).transform(translateTransform);
        undo.transform(translateTransform,(PaintableShape)theIntersectedShapes.get(i));
      }
    }
    else if( aType == AlignTypes.CENTERS )
    {
      double x;
      double y;
      for( int i = 1 ; i < theIntersectedShapes.size() ; i++ )
      {
        y = (bounds[0].y+bounds[0].height/2)-(bounds[i].y+bounds[i].height/2);
        x = (bounds[0].x+bounds[0].width/2)-(bounds[i].x+bounds[i].width/2);
        translateTransform.setToTranslation(x,y);
        ((PaintableShape)theIntersectedShapes.get(i)).transform(translateTransform);
        undo.transform(translateTransform,(PaintableShape)theIntersectedShapes.get(i));
      }
    }
    commitUndo();
    getDrawingArea().repaint();
  }

  public JComponent getToolbar()
  {
    return theSelectionToolbar;
  }
}