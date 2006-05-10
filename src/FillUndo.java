/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.awt.Paint;
import java.awt.BasicStroke;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.Serializable;

public class FillUndo extends Undo implements Serializable
{
  static final long serialVersionUID = 2857572094852936956L;

  private Paint       theFillPaint;
  private Paint       theStrokePaint;
  private BasicStroke theStroke;

  private PaintableShape theShape;

  public FillUndo( DrawingArea anArea, PaintableShape aShape )
  {
    super(anArea);
    theShape = aShape;
    if( theShape.getFillTransformable() )
    {
      theFillPaint   = (TransformableTexturePaint) ((TransformableTexturePaint)theShape.getFillPaint()).clone();
    }
    else
    {
      theFillPaint   = theShape.getFillPaint();
    }
    theStrokePaint = theShape.getDrawPaint(); 

    double zoom = theShape.getZoom();
    theShape.zoomStroke(1);
    theStroke = theShape.getStroke();
    theStroke = new BasicStroke(theStroke.getLineWidth(),theStroke.getEndCap(),theStroke.getLineJoin(),theStroke.getMiterLimit(),theStroke.getDashArray(),theStroke.getDashPhase());
    theShape.zoomStroke(zoom*zoom);
  }
 
  public void undo()
  {
    Paint tempFill = null;
    if( theShape.getFillTransformable() )
    {
      tempFill = (TransformableTexturePaint) ((TransformableTexturePaint)theShape.getFillPaint()).clone();
    }
    else
    {
      tempFill = theShape.getFillPaint();
    }
    Paint tempStrokePaint = theShape.getDrawPaint();

    double zoom = theShape.getZoom();
    theShape.zoomStroke(1);
    BasicStroke tempStroke = theShape.getStroke();
    tempStroke             = new BasicStroke(tempStroke.getLineWidth(),tempStroke.getEndCap(),tempStroke.getLineJoin(),tempStroke.getMiterLimit(),tempStroke.getDashArray(),tempStroke.getDashPhase());
    theShape.zoomStroke(zoom*zoom);

    theShape.setFillPaint(theFillPaint);
    theShape.setDrawPaint(theStrokePaint);
    theShape.setStroke(theStroke);

    theFillPaint   = tempFill;
    theStrokePaint = tempStrokePaint;
    theStroke      = tempStroke;
  }

  public void redo()
  {
    undo();
  }

  private void writeObject( ObjectOutputStream out) throws IOException
  {
    out.writeObject(theShape);

    out.writeObject(theFillPaint);
    out.writeObject(theStrokePaint);

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

  }

  private void readObject( ObjectInputStream in) throws IOException, ClassNotFoundException
  {
    theShape = (PaintableShape)in.readObject();
    theFillPaint   = (Paint)in.readObject();
    theStrokePaint = (Paint)in.readObject();

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
  }
}