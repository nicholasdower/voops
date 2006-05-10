/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.util.Vector;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.RenderingHints;
import java.awt.AlphaComposite;
import java.util.Enumeration;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import javax.swing.JFrame;
import javax.swing.JComponent;
import java.awt.BorderLayout;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

public class DrawingArea extends JPanel
{
  private LayerManager theLayerManager;

  private BufferedImage theCommitedBelowImage;
  private Graphics2D    theCommitedBelowGraphics;

  private BufferedImage theDrawingImage;
  private Graphics2D    theDrawingGraphics;

  private BufferedImage theCommitedAboveImage;
  private Graphics2D    theCommitedAboveGraphics;

  private double theZoomFactor = 1;

  private int theImageWidth;
  private int theImageHeight;
 
  private OOPS theOOPS;

  private RenderingHints theRenderingHints;

  private int a = 0;

  public static Vector thePaintImages;

  public DrawingArea(OOPS anOOPS)
  {
    theOOPS = anOOPS;
    theLayerManager = new LayerManager(this);

    this.addComponentListener
    (
      new ComponentAdapter()
      {
        public void componentResized( ComponentEvent e )
        {
          initialize();
          repaint();
        }
      }
    ); 

    this.addMouseMotionListener
    (
      new MouseMotionAdapter()
      {
        public void mouseMoved( MouseEvent e )
        {
          theOOPS.setStatus("X: " + (int)(e.getX()/theZoomFactor) + "\n" + "Y: " + (int)(e.getY()/theZoomFactor));
        }
        public void mouseDragged( MouseEvent e )
        {
          if( e.getX() < 0 || e.getY() < 0 || e.getX() > getWidth() || e.getY() > getHeight() )
            theOOPS.setStatus("");
          else
            theOOPS.setStatus("X: " + (int)(e.getX()/theZoomFactor) + "\n" + "Y: " + (int)(e.getY()/theZoomFactor));
        }
      }
    ); 

    this.addMouseListener
    (
      new MouseAdapter()
      {
        public void mouseExited( MouseEvent e )
        {
          theOOPS.setStatus("");
        }
      }
    ); 
  }

  public void initialize()
  {
    theCommitedBelowImage    = new BufferedImage((int)(getPreferredSize().getWidth()),(int)(getPreferredSize().getHeight()),BufferedImage.TYPE_INT_ARGB);
    theCommitedBelowGraphics = theCommitedBelowImage.createGraphics();
    theCommitedBelowGraphics.addRenderingHints(theRenderingHints);

    theCommitedAboveImage    = new BufferedImage((int)(getPreferredSize().getWidth()),(int)(getPreferredSize().getHeight()),BufferedImage.TYPE_INT_ARGB);
    theCommitedAboveGraphics = theCommitedAboveImage.createGraphics();
    theCommitedAboveGraphics.addRenderingHints(theRenderingHints);

    theDrawingImage    = new BufferedImage((int)(getPreferredSize().getWidth()),(int)(getPreferredSize().getHeight()),BufferedImage.TYPE_INT_ARGB);
    theDrawingGraphics = theDrawingImage.createGraphics();
    theDrawingGraphics.addRenderingHints(theRenderingHints);

    System.gc();
  }

  public void setImageSize( int x, int y )
  {
    theImageWidth  = x;
    theImageHeight = y;
    setPreferredSize(new Dimension((int)(x*theZoomFactor),(int)(y*theZoomFactor)));
    initialize();
    invalidate();
    validate();
    repaint();
  }

  public Dimension getImageSize()
  {
    return new Dimension(theImageWidth,theImageHeight);
  }

  public LayerManager getLayerManager()
  {
    return theLayerManager;
  }

  public void setLayerManager( LayerManager aManager)
  {
    theLayerManager = aManager;
  }

  public void setOOPS( OOPS anOOPS )
  {
    theOOPS = anOOPS;
  }

  public void setZoom(double aFactor)
  {
    if( aFactor == theZoomFactor )
      return;

    theLayerManager.scale(aFactor/theZoomFactor,aFactor/theZoomFactor);
    theLayerManager.zoom(aFactor);

    theZoomFactor = aFactor;
    setImageSize(theImageWidth,theImageHeight);

    repaint();
  }

  public double getZoom()
  {
    return theZoomFactor;
  }

  public RenderingHints getRenderingHints()
  {
    return theRenderingHints;
  }

  public void setRenderingHints( RenderingHints someHints )
  {
    theRenderingHints = someHints;
    if( theCommitedAboveGraphics != null )
    {
      theCommitedAboveGraphics.addRenderingHints(theRenderingHints);
      theCommitedBelowGraphics.addRenderingHints(theRenderingHints);
      theDrawingGraphics.addRenderingHints(theRenderingHints);
    }
  }

  public void paint( Graphics g )
  {
    clearCommited();

    theLayerManager.paintShapesBelow(theCommitedBelowGraphics);
    theLayerManager.paintShapesAbove(theCommitedAboveGraphics);

    g.drawImage(theCommitedBelowImage,0,0,this);
    g.drawImage(theDrawingImage,0,0,this);
    g.drawImage(theCommitedAboveImage,0,0,this);
  }

  public void clearCommited()
  {
    theCommitedBelowGraphics.setColor(getBackground());
    theCommitedBelowGraphics.fillRect(0,0,getWidth(),getHeight());

    theCommitedAboveGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
    theCommitedAboveGraphics.fillRect(0,0,getWidth(),getHeight());
    theCommitedAboveGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
  }

  public void clearAll()
  {
    theLayerManager.clearCurrent();
    clearCommited();
    clearDrawingArea();
  }

  public void clearDrawingArea()
  {
    theDrawingGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
    theDrawingGraphics.fillRect(0,0,getWidth(),getHeight());
    theDrawingGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    repaint();
  }

  public void setUndoPoint(  Undo anUndo  )
  {
    theLayerManager.setUndoPoint( anUndo );
  }

  public void undo()
  {
    theLayerManager.undo();
    clearDrawingArea();
    repaint();
    theOOPS.refreshLayerChooser();
  }

  public void redo()
  {
    theLayerManager.redo();
    clearDrawingArea();
    repaint();
    theOOPS.refreshLayerChooser();
  }

  public void moveBackALayer( Vector someShapes )
  {
    theLayerManager.moveBackALayer(someShapes);
    theOOPS.refreshLayerChooser();
  }

  public void moveUpALayer( Vector someShapes )
  {
    theLayerManager.moveUpALayer(someShapes);
    theOOPS.refreshLayerChooser();
  }

  public void replaceShape( PaintableShape anOldShape, PaintableShape aNewShape )
  {
    theLayerManager.replaceShape(anOldShape,aNewShape );
  }

  public void insertShape( PaintableShape anOldShape, PaintableShape aNewShape )
  {
    theLayerManager.insertShape(anOldShape,aNewShape );
  }

  public void insertShape( PaintableShape aNewShape, int anIndex )
  {
    theLayerManager.insertShape(aNewShape,anIndex);
  }

  public PaintableShape getFirstShapeAt( Point2D.Double aPoint )
  {
    return theLayerManager.getFirstShapeAt(aPoint);
  }

  public void removeShape( Object aShape )
  {
    theLayerManager.removeShape(aShape);
  }

  public void moveUp( Vector someShapes )
  {
    theLayerManager.moveUp(someShapes);
  }

  public void moveBack( Vector someShapes )
  {
    theLayerManager.moveBack(someShapes);
  }

  public void moveToFront( Vector someShapes )
  {
    theLayerManager.moveToFront(someShapes);
  }

  public void moveToBack( Vector someShapes )
  {
    theLayerManager.moveToBack(someShapes);
  }

  public void paintShape( PaintableShape aShape )
  {
    aShape.setZoom(theZoomFactor);
    aShape.paint(theDrawingGraphics);
  }

  public Graphics2D getDrawingGraphics()
  {
    return theDrawingGraphics;
  }

  public Vector getShapes()
  {
    return theLayerManager.getShapes();
  }

  public void addShape( PaintableShape aShape )
  { 
    theLayerManager.addShape(aShape);
    aShape.setZoom(theZoomFactor);
  }

  public void setToolbar(JComponent aComponent)
  {
    theOOPS.setToolbar(aComponent);
  }

  public BufferedImage getImage()
  {
    double oldZoom = theZoomFactor;
    setZoom(1);

    BufferedImage  image = new BufferedImage((int)(getPreferredSize().getWidth()),(int)(getPreferredSize().getHeight()),BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2D       = image.createGraphics();
    g2D.addRenderingHints(theRenderingHints);

    theLayerManager.paintShapesBelow(g2D);
    theLayerManager.paintShapesAbove(g2D);

    setZoom(oldZoom);

    return image;
  }

  public BufferedImage getImage( int type )
  {
    return getImage(theImageWidth,theImageHeight,type);
  }
  public BufferedImage getImage( int aWidth, int aHeight, int type )
  {
    BufferedImage image = new BufferedImage(aWidth,aHeight,BufferedImage.TYPE_INT_ARGB);
    Graphics2D    g2D   = image.createGraphics();

    g2D.addRenderingHints(theRenderingHints);

    if( type != BufferedImage.TYPE_INT_ARGB )
    {
      g2D.setColor(getBackground());
      g2D.fillRect(0,0,aWidth,aHeight);
    }

    double oldZoom = theZoomFactor;
    setZoom(1);
    double xFactor = ((double)aWidth)/((double)theImageWidth);
    double yFactor = ((double)aHeight)/((double)theImageHeight);

    theLayerManager.scale(xFactor,yFactor);
    theLayerManager.paintShapesBelow(g2D);
    theLayerManager.paintShapesAbove(g2D);
    theLayerManager.scale(1/xFactor,1/yFactor);
    setZoom(oldZoom);

    g2D.dispose();
    if( type != BufferedImage.TYPE_INT_ARGB )
    {
      BufferedImage opaqueImage = new BufferedImage(aWidth,aHeight,BufferedImage.TYPE_INT_RGB);
      for( int x = 0 ; x < aWidth ; x++ )
      {
        for( int y = 0 ; y < aHeight ; y++ )
        {
          opaqueImage.setRGB(x,y,image.getRGB(x,y));
        }
      }
      return opaqueImage;
    }

    return image;
  }

  public void write( ObjectOutputStream out) throws IOException
  {
    thePaintImages = new Vector();

    Color color = getBackground();
    out.writeInt(color.getRed());
    out.writeInt(color.getGreen());
    out.writeInt(color.getBlue());
    out.writeInt(color.getAlpha());
    out.writeObject(theLayerManager);
    out.writeDouble(theZoomFactor);
    out.writeInt(theImageWidth);
    out.writeInt(theImageHeight);
  }
  public void read( ObjectInputStream in) throws IOException, ClassNotFoundException
  {
    thePaintImages = new Vector();

    setBackground(new Color(in.readInt(),in.readInt(),in.readInt(),in.readInt()));
    theLayerManager = (LayerManager)in.readObject();
    theLayerManager.setDrawingArea(this);
    theZoomFactor   = in.readDouble();
    theImageWidth   = in.readInt();
    theImageHeight  = in.readInt();
    setPreferredSize(new Dimension((int)(theImageWidth*theZoomFactor),(int)(theImageHeight*theZoomFactor)));
  }
}