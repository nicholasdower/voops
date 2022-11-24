/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Area;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.RenderingHints;
import java.util.Vector;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

public class PaintableShape implements Serializable
{
  static final long serialVersionUID = 2534632512913375706L;

  public static final int FILL_COLOR   = 12345;
  public static final int FILL_TEXTURE = 54321;
  public static final int NULL_PAINT   = 54421;

  public static final int TYPE_FILL = 12345;
  public static final int TYPE_DRAW = 54321;
  public static final int TYPE_BOTH = 87654;

  private Shape theDrawShape;
  private Shape theFillShape;

  private Paint theDrawPaint;
  private Paint theFillPaint;

  private BasicStroke theStroke = new BasicStroke(.5f);

  private int theType;

  public double theZoomFactor = 1;

  private Vector theGroup = null;

  private boolean isFillTransformable = false;

  public PaintableShape()
  {
    this(null,TYPE_DRAW);
  }

  public PaintableShape( int aType )
  {
    this(null,aType);
  }

  public PaintableShape( Shape aShape )
  {
    this(aShape,TYPE_DRAW);
  }

  public PaintableShape( Shape aShape, int aType )
  {
    theType      = aType;
    setShape(aShape);
  }

  public Shape getFillShape()
  {
    return theFillShape;
  }

  public Shape getDrawShape()
  {
    return theDrawShape;
  }

  public PaintableShape get()
  {
    PaintableShape shape = new PaintableShape(theType);

    if( getFillTransformable() )
    {
      shape.setFillPaint((TransformableTexturePaint)(((TransformableTexturePaint)theFillPaint).clone()));
    }
    else
    {
      shape.setFillPaint(theFillPaint);
    }
    shape.setDrawPaint(theDrawPaint);
    shape.setStroke(theStroke);
    shape.setFillShape((new AffineTransform()).createTransformedShape(theFillShape));
    shape.setDrawShape((new AffineTransform()).createTransformedShape(theDrawShape));
    shape.theZoomFactor = theZoomFactor;
    shape.setFillTransformable(getFillTransformable());
    return shape;
  }

  public Paint getDrawPaint()
  {
    return theDrawPaint;
  }

  public Paint getFillPaint()
  {
    return theFillPaint;
  }

  public double getZoom()
  {
    return theZoomFactor;
  }

  public void setFillTransformable( boolean isTransformable )
  {
    isFillTransformable = isTransformable;
  }

  public boolean getFillTransformable()
  {
    return isFillTransformable;
  }

  public void setShape( Shape aShape )
  {
    if( aShape == null )
      return;
    theDrawShape = theStroke.createStrokedShape(aShape);
    setFillShape(aShape);

    if( getFillTransformable() )
    {
      ((TransformableTexturePaint)theFillPaint).setAnchor(theFillShape.getBounds2D());
    }

    //if( theZoomFactor != 1 )
    //  scale(theZoomFactor,theZoomFactor);
  }

  public void setFillShape( Shape aShape )
  {
    theFillShape = aShape;
  }

  public void resetFill()
  {
    if( getFillTransformable() )
    {
      ((TransformableTexturePaint)theFillPaint).setTransform(new AffineTransform());
      ((TransformableTexturePaint)theFillPaint).setAnchor(theFillShape.getBounds2D());
    }
  }

  public void setDrawShape( Shape aShape )
  {
    theDrawShape = aShape;
  }

  public void setZoom( double aFactor )
  {
    if( theZoomFactor != aFactor )
    {
      zoomStroke(aFactor);
      theZoomFactor = aFactor;
    }
  }

  public void setStroke( BasicStroke aStroke )
  {
    theStroke = aStroke;
    zoomStroke(theZoomFactor*theZoomFactor);
    if( theFillShape != null )
      setShape(theFillShape);
  }

  public void setDrawPaint( Paint aPaint )
  {
    theDrawPaint = aPaint;
  }

  public void setFillPaint( Paint aPaint )
  {
    theFillPaint = aPaint;
    try
    {
      if( theFillPaint == null )
        setFillTransformable(false);
      else if( theFillPaint.getClass().equals(Class.forName("TransformableTexturePaint")) )
      {
        setFillTransformable(true);
        if( theFillShape != null )
          ((TransformableTexturePaint)theFillPaint).setAnchor(theFillShape.getBounds2D());
      }
      else
        setFillTransformable(false);
    }
    catch( ClassNotFoundException cnfe )
    {
      setFillTransformable(false);
    }
  }

  public void setType( int aType )
  {
    theType = aType;
  }

  public int getType()
  {
    return theType;
  }

  public BasicStroke getStroke()
  {
    return theStroke;
  }

  public PaintableShape convertToFill( RenderingHints someHints )
  {
    PaintableShape thisShape = this.get();
    Area           thisArea  = this.getArea();

    Rectangle2D.Double theseBounds = new Rectangle2D.Double();
    theseBounds.setRect(thisArea.getBounds2D());

    BufferedImage  thisImage = new BufferedImage((int)Math.ceil(theseBounds.width),(int)Math.ceil(theseBounds.height),BufferedImage.TYPE_INT_ARGB);
    Graphics2D     g2D       = thisImage.createGraphics();
    g2D.addRenderingHints(someHints);

    thisShape.transform(AffineTransform.getTranslateInstance(-theseBounds.x,-theseBounds.y));
    thisShape.paint(g2D);
    g2D.dispose();

    thisShape.setStroke(new BasicStroke(0f));
    thisShape.setDrawPaint(new Color(0,0,0,0));
    thisShape.setShape(new Rectangle2D.Double(theseBounds.x-.5,theseBounds.y-.5,theseBounds.width+1,theseBounds.height+1));

    TransformableTexturePaint newPaint = new TransformableTexturePaint(thisImage,theseBounds,TransformableTexturePaint.KIND_TEXTURE);
    newPaint.setType(TransformableTexturePaint.TYPE_STRETCH);
    thisShape.setFillPaint(newPaint);

    return thisShape;
  }

  public void zoomStroke( double aZoomFactor )
  {
    float factor = (float)(aZoomFactor/theZoomFactor);

    float  width = theStroke.getLineWidth()*factor;
    int    cap   = theStroke.getEndCap();
    int    join  = theStroke.getLineJoin();
    float  limit = theStroke.getMiterLimit();

    float[] dash = theStroke.getDashArray();
    if( dash != null )
    {
      for( int i = 0 ; i < dash.length ; i++ )
        dash[i] *= factor;
    }
    float  phase = theStroke.getDashPhase()*factor;

    theStroke = new BasicStroke(width,cap,join,limit,dash,phase);
  }

  public void paint( Graphics2D g )
  {
    if( theType == TYPE_DRAW )
    {
      g.setPaint(theDrawPaint);
      g.fill(theDrawShape);
    }
    else if( theType == TYPE_FILL )
    {
      g.setPaint(theFillPaint);
      g.fill(theFillShape);
    }
    else
    {
      g.setPaint(theFillPaint);
      g.fill(theFillShape);
      g.setPaint(theDrawPaint);
      g.fill(theDrawShape);
    }
  }

  public void translate( double x, double y )
  {
    translate(x,y,false);
  }
  public void translate( double x, double y, boolean applyToFill )
  {
    AffineTransform transform = AffineTransform.getTranslateInstance(x,y);

    setFillShape(transform.createTransformedShape(getFillShape()));

    setDrawShape(transform.createTransformedShape(getDrawShape()));

    if( getFillTransformable() && applyToFill)
    {
      ((TransformableTexturePaint)theFillPaint).transform(transform);
    }
  }

  public void scale( double x, double y )
  {
    AffineTransform transform = AffineTransform.getScaleInstance(x,y);

    setFillShape(transform.createTransformedShape(getFillShape()));

    setDrawShape(transform.createTransformedShape(getDrawShape()));

    if( getFillTransformable())
    {
      ((TransformableTexturePaint)theFillPaint).transform(transform);
    }
  }

  public void transform( AffineTransform aTransform )
  {
    setFillShape(aTransform.createTransformedShape(getFillShape()));
    setDrawShape(aTransform.createTransformedShape(getDrawShape()));

    if( getFillTransformable())
    {
      ((TransformableTexturePaint)theFillPaint).transform(aTransform);
    }
  }

  public void intersect( Area anArea )
  {
    Area fillArea = new Area(theFillShape);
    Area drawArea = new Area(theDrawShape);

    fillArea.intersect(anArea);
    //if( getFillTransformable() )
    //{
      //setFillPaint(((TransformableTexturePaint)theFillPaint).subset(fillArea));
    //}
    setFillShape(fillArea);

    drawArea.intersect(anArea);
    setDrawShape(drawArea);
  }

  public void subtract( Area anArea )
  {
    Area fillArea = new Area(theFillShape);
    Area drawArea = new Area(theDrawShape);

    fillArea.subtract(anArea);
    //if( getFillTransformable() )
    //{
    //  setFillPaint(((TransformableTexturePaint)theFillPaint).subset(fillArea));
    //}
    setFillShape(fillArea);

    drawArea.subtract(anArea);
    setDrawShape(drawArea);
  }

  public void subtract( PaintableShape aShape )
  {
    Area fillArea = new Area(aShape.getFillShape());
    subtract(fillArea);

    Area drawArea = new Area(aShape.getDrawShape());
    subtract(drawArea);
  }

  public void intersect( PaintableShape aShape )
  {
    Area area = new Area(aShape.getFillShape());
    area.add(new Area(aShape.getDrawShape()));
    intersect(area);
  }

  public Area getArea()
  {
    Area area = new Area(getFillShape());
    area.add(new Area(getDrawShape()));
    return area;
  }

  public boolean intersects( double x, double y, double w, double h )
  {
    if( theFillShape.intersects(x,y,w,h) || theDrawShape.intersects(x,y,w,h) )
      return true;

    return false;
  }

  public boolean intersects( Rectangle2D aRect )
  {
    return intersects(aRect.getX(),aRect.getY(),aRect.getWidth(),aRect.getHeight());
  }

  public boolean intersects( PaintableShape aShape )
  {
    if( intersects(aShape.getDrawShape().getBounds2D()) || intersects(aShape.getFillShape().getBounds2D()) )
      return true;

    return false;
  }

  public boolean intersects( Shape aShape )
  {
    return intersects(aShape.getBounds2D());
  }

  public boolean contains( double x, double y )
  {
    if( theFillShape.contains(x,y) || theDrawShape.contains(x,y) )
      return true;

    return false;
  }

  public void setGroup( Vector aGroup )
  {
    theGroup = aGroup;
  }

  public void addToGroup( Vector aGroup )
  {
    if( aGroup == null )
      return;

    if( !aGroup.contains(this) )
      aGroup.add(this);
    theGroup = aGroup;
  }

  public Vector getGroup()
  {
    return theGroup;
  }

  public void removeFromGroup()
  {
    if( theGroup != null )
    {
      theGroup.remove(this);
      theGroup = null;
    }
  }

  private void writeObject( ObjectOutputStream out) throws IOException
  {
    ShapeIO.writeShape(out,theDrawShape);
    ShapeIO.writeShape(out,theFillShape);

    try
    {
      if( theDrawPaint == null )
      {
        out.writeInt(NULL_PAINT);
      }
      else if( theDrawPaint.getClass().equals(Class.forName("java.awt.Color")) )
      {
        Color color = (Color)theDrawPaint;
        out.writeInt(FILL_COLOR);
        out.writeInt(color.getRed());
        out.writeInt(color.getGreen());
        out.writeInt(color.getBlue());
        out.writeInt(color.getAlpha());
      }
      else if( theDrawPaint.getClass().equals(Class.forName("TransformableTexturePaint")) )
      {
        out.writeInt(FILL_TEXTURE);
        out.writeObject((TransformableTexturePaint)theDrawPaint);
      }

      if( theFillPaint == null )
      {
        out.writeInt(NULL_PAINT);
      }
      else if( theFillPaint.getClass().equals(Class.forName("java.awt.Color")) )
      {
        Color color = (Color)theFillPaint;
        out.writeInt(FILL_COLOR);
        out.writeInt(color.getRed());
        out.writeInt(color.getGreen());
        out.writeInt(color.getBlue());
        out.writeInt(color.getAlpha());
      }
      else if( theFillPaint.getClass().equals(Class.forName("TransformableTexturePaint")) )
      {
        out.writeInt(FILL_TEXTURE);
        out.writeObject((TransformableTexturePaint)theFillPaint);
      }
    }
    catch( ClassNotFoundException cnfe )
    {
      System.out.println(cnfe);
    }

    out.writeFloat(theStroke.getLineWidth());
    out.writeInt(theStroke.getEndCap());
    out.writeInt(theStroke.getLineJoin());
    out.writeFloat(theStroke.getMiterLimit());

    float[] dash = theStroke.getDashArray();
    if( dash != null )
    {
      out.writeInt(dash.length);
      for( int i = 0 ; i < dash.length ; i++ )
        out.writeFloat(dash[i]);
    }
    else
      out.writeInt(0);

    out.writeFloat(theStroke.getDashPhase());
    out.writeInt(theType);
    out.writeDouble(theZoomFactor);
    out.writeObject(theGroup);
    out.writeBoolean(isFillTransformable);
  }

  private void readObject( ObjectInputStream in) throws IOException, ClassNotFoundException
  {
    theDrawShape = ShapeIO.readShape(in);
    theFillShape = ShapeIO.readShape(in);

    int type = in.readInt();
    if( type == FILL_COLOR )
    {
      theDrawPaint = new Color(in.readInt(),in.readInt(),in.readInt(),in.readInt());
    }
    else if( type == FILL_TEXTURE )
    {
      theDrawPaint = (TransformableTexturePaint)in.readObject();
    }
    else
    {
      theDrawPaint = null;
    }

    type = in.readInt();
    if( type == FILL_COLOR )
    {
      theFillPaint = new Color(in.readInt(),in.readInt(),in.readInt(),in.readInt());
    }
    else if( type == FILL_TEXTURE )
    {
      theFillPaint = (TransformableTexturePaint)in.readObject();
    }
    else
    {
      theFillPaint = null;
    }

    float lineWidth  = in.readFloat();
    int   endCap     = in.readInt();
    int   lineJoin   = in.readInt();
    float miterLimit = in.readFloat();

    int size = in.readInt();
    float[] dash = null;
    if( size > 0 )
    {
      dash = new float[size];
      for( int i = 0 ; i < size ; i++ )
        dash[i] = in.readFloat();
    }

    float dashPhase = in.readFloat();

    theStroke = new BasicStroke(lineWidth,endCap,lineJoin,miterLimit,dash,dashPhase);

    theType = in.readInt();

    theZoomFactor = in.readDouble();

    theGroup = (Vector)in.readObject();

    isFillTransformable = in.readBoolean();
  }

}