/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.awt.Paint;
import java.awt.TexturePaint;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.Image;
import java.awt.image.ColorModel;
import java.awt.image.BufferedImage;
import java.awt.PaintContext;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.RenderingHints;
import java.awt.image.AffineTransformOp;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.FilteredImageSource;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Area;
import java.awt.geom.NoninvertibleTransformException;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public class TransformableTexturePaint implements Paint, Serializable
{
  static final long serialVersionUID = 6326909155925459132L;

  public static final int TYPE_STRETCH = 12345;
  public static final int TYPE_TILE    = 54321;

  public static final int KIND_RADIAL      = 666;
  public static final int KIND_GRADIENT    = 555;
  public static final int KIND_TEXTURE     = 444;
  public static final int KIND_RECTANGULAR = 333;

  private int theKind;
  private int theType;

  private Color[] theColors;

  private BufferedImage      theOriginalImage;
  private Rectangle2D.Double theOriginalAnchor;

  private Rectangle2D.Double     theAnchor;

  private TexturePaint thePaint;

  private AffineTransform theTransform;

  private static RenderingHints theHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

  public TransformableTexturePaint( BufferedImage anImage, Rectangle2D anAnchor, int aKind )
  {
    this(anImage,anAnchor, new AffineTransform(),aKind);
  }

  public TransformableTexturePaint( BufferedImage anImage, int aKind  )
  {
    this(anImage,new Rectangle(0,0,anImage.getWidth(),anImage.getHeight()),aKind);
  }

  public TransformableTexturePaint( BufferedImage anImage, Rectangle2D anAnchor, AffineTransform aTransform, int aKind  )
  {
    theType = TYPE_STRETCH;
    theKind = aKind;

    theOriginalImage  = anImage;
    theOriginalAnchor = new Rectangle2D.Double();
    theOriginalAnchor.setRect(anAnchor);

    theAnchor = theOriginalAnchor;

    theTransform = aTransform;

    thePaint = new TexturePaint(theOriginalImage,theAnchor);
  }

  public PaintContext createContext(ColorModel cm, Rectangle deviceBounds, Rectangle2D userBounds, AffineTransform xform, RenderingHints hints)
  {
    AffineTransform transform = (AffineTransform)xform.clone();
    transform.concatenate(theTransform);
   
    if( hints == null )
      hints = theHints;

    return thePaint.createContext(cm,deviceBounds,userBounds,transform,hints);
  }

  public void setTransform( AffineTransform aTransform )
  {
    theTransform = aTransform;
  }

  public void setType( int aType )
  {
    theType = aType;
  }

  public void setKind( int aKind )
  {
    theKind = aKind;
  }

  public int getType()
  {
    return theType;
  }

  public int getKind()
  {
    return theKind;
  }

  public BufferedImage getImage()
  {
    return theOriginalImage;
  }

  public Color[] getColors()
  {
    return theColors;
  } 

  public void setColors( Color[] someColors )
  {
    theColors = someColors;
  } 

  public void transform( AffineTransform aTransform )
  {
    //theAnchor.x += aTransform.getTranslateX();
    //theAnchor.y += aTransform.getTranslateY();

    theTransform.preConcatenate(aTransform);
    //theTransform.concatenate(AffineTransform.getTranslateInstance(-aTransform.getTranslateX(),-aTransform.getTranslateY()));

    //thePaint = new TexturePaint(theOriginalImage,theAnchor);
  }

  public Rectangle2D.Double getAnchor( )
  {
    return theOriginalAnchor;
  }

  public Rectangle2D.Double getCurrentAnchor( )
  {
    return theAnchor;
  }

  public void setAnchor( Rectangle2D anAnchor )
  {
    if( theType == TYPE_STRETCH )
    {
      theAnchor.setRect(anAnchor);
    }
    else
    {
      Rectangle2D.Double r2D = new Rectangle2D.Double();
      r2D.setRect(anAnchor);
      theAnchor.x = r2D.x;
      theAnchor.y = r2D.y;
    }
    thePaint = new TexturePaint(theOriginalImage,theAnchor);
  }

  public int getTransparency()
  {
    return thePaint.getTransparency();
  }

  public AffineTransform getTransform()
  {
    return theTransform;
  }

  public TransformableTexturePaint subset(Area aSubset)
  {
    if( true )
      return (TransformableTexturePaint)(this.clone());
    Rectangle2D.Double subsetRect = new Rectangle2D.Double();
    try
    {
      subsetRect.setRect(((theTransform.createInverse()).createTransformedShape(aSubset)).getBounds2D());
    }
    catch( NoninvertibleTransformException nite )
    {
      System.out.println(nite);
      return (TransformableTexturePaint)(this.clone());
    }

    if( subsetRect.x < theAnchor.x )
      subsetRect.x = theAnchor.x;
    if( subsetRect.y < theAnchor.y )
      subsetRect.y = theAnchor.y;

    if( subsetRect.x + subsetRect.width > theAnchor.x + theAnchor.width )
      subsetRect.width = (theAnchor.x + theAnchor.width) - subsetRect.x;
    if( subsetRect.y + subsetRect.height > theAnchor.y + theAnchor.height )
      subsetRect.height = (theAnchor.y + theAnchor.height) - subsetRect.y;

    int x      = (int)((((double)(subsetRect.x - theAnchor.x))/((double)theAnchor.width)) * ((double)theOriginalImage.getWidth()) );
    int y      = (int)((((double)(subsetRect.y - theAnchor.y))/((double)theAnchor.height)) * ((double)theOriginalImage.getHeight()) );
    int width  = (int)( (((double)subsetRect.width)/((double)theAnchor.width)) * theOriginalImage.getWidth() ); 
    int height = (int)( (((double)subsetRect.height)/((double)theAnchor.height)) * theOriginalImage.getHeight() ); 

    if( theType == TYPE_STRETCH )
      return new TransformableTexturePaint(theOriginalImage.getSubimage(x,y,width,height),new Rectangle(x,y,width,height),theTransform,theKind );
    else
    {
      TransformableTexturePaint paint = new TransformableTexturePaint(theOriginalImage,new Rectangle(x,y,width,height),theTransform,theKind);
      paint.setType(getType());
      return paint;
    }
  }

  public Object clone()
  {
    Rectangle2D.Double rect = new Rectangle2D.Double(theAnchor.x,theAnchor.y,theAnchor.width,theAnchor.height);
    TransformableTexturePaint paint = new TransformableTexturePaint(theOriginalImage,rect,(AffineTransform)(theTransform.clone()),theKind);
    paint.setType(getType());
    paint.setKind(getKind());
    paint.setColors(getColors());
    return paint;
  }

  private void writeObject( ObjectOutputStream out) throws IOException
  {
    out.writeInt(theType);
    out.writeInt(theKind);

    if( theKind == KIND_TEXTURE )
    {
      if( DrawingArea.thePaintImages.contains(theOriginalImage) )
        out.writeInt(DrawingArea.thePaintImages.indexOf(theOriginalImage));
      else
      { 
        DrawingArea.thePaintImages.add(theOriginalImage);
        out.writeInt(DrawingArea.thePaintImages.indexOf(theOriginalImage));
        out.writeInt(theOriginalImage.getWidth());
        out.writeInt(theOriginalImage.getHeight());
        for( int x = 0 ; x < theOriginalImage.getWidth() ; x++ )
        {
          for( int y = 0 ; y < theOriginalImage.getHeight() ; y++ )
          {
            out.writeInt(theOriginalImage.getRGB(x,y));
          }
        }
      }
    }

    out.writeDouble(theOriginalAnchor.x);
    out.writeDouble(theOriginalAnchor.y);
    out.writeDouble(theOriginalAnchor.width);
    out.writeDouble(theOriginalAnchor.height);

    out.writeDouble(theAnchor.x);
    out.writeDouble(theAnchor.y);
    out.writeDouble(theAnchor.width);
    out.writeDouble(theAnchor.height);

    if( theColors != null )
    {
      out.writeInt(theColors.length);
      for( int i = 0 ; i < theColors.length ; i++ )
        out.writeObject(theColors[i]);

      out.writeInt(theOriginalImage.getWidth());
      out.writeInt(theOriginalImage.getHeight());
    }
    else
      out.writeInt(0);

    out.writeObject(theTransform);
  }

  private void readObject( ObjectInputStream in) throws IOException, ClassNotFoundException
  {
    theType = in.readInt();
    theKind = in.readInt();

    if( theKind == KIND_TEXTURE )
    {
      int index = in.readInt();
      if( DrawingArea.thePaintImages.size() > index )
        theOriginalImage = (BufferedImage)DrawingArea.thePaintImages.get(index);
      else
      {
        int width  = in.readInt();
        int height = in.readInt();
        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        for( int x = 0 ; x < width ; x++ )
        {
          for( int y = 0 ; y < height ; y++ )
          {
            image.setRGB(x,y,in.readInt());
          }
        }
        DrawingArea.thePaintImages.add(image);
        theOriginalImage = image;
      }
    }

    theOriginalAnchor = new Rectangle2D.Double(in.readDouble(),in.readDouble(),in.readDouble(),in.readDouble());
    theAnchor = new Rectangle2D.Double(in.readDouble(),in.readDouble(),in.readDouble(),in.readDouble());

    int size = in.readInt();
    if( size > 0 )
    {
      theColors = new Color[size];
      for( int i= 0 ; i < size ; i++ )
        theColors[i] = (Color)in.readObject();

      if( theKind == KIND_RADIAL )
        theOriginalImage = RadialChooser.createImage(theColors,in.readInt(),in.readInt());
      else if( theKind == KIND_GRADIENT )
        theOriginalImage = Gradient.createImage(theColors,in.readInt(),in.readInt());
      else if( theKind == KIND_RECTANGULAR )
        theOriginalImage = RectangularChooser.createImage(theColors,in.readInt(),in.readInt());
    }

    thePaint = new TexturePaint(theOriginalImage,theAnchor);

    theTransform = (AffineTransform)in.readObject();
  }
}
