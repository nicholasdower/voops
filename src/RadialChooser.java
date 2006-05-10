/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.TexturePaint;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.TexturePaint;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.geom.Point2D;
import java.util.Vector;

public class RadialChooser extends JPanel
{
  private MiniColorChooser          theChooser;
  private Paint                     theBackgroundPaint;
  private TransformableTexturePaint theForegroundPaint;
  private BufferedImage             theImage;
  private TransformableTexturePaint thePaint;
  private ColorLabel[]              theLabels;

  private final static int theSize = 300;
  private static double[][] thePercentages;

  private Vector theChangeListeners;

  public RadialChooser( MiniColorChooser aChooser )
  {
    theChangeListeners = new Vector();

    theChooser = aChooser;

    ChangeListener listener = new ChangeListener()
    {
      public void stateChanged( ChangeEvent e )
      {
        updateImage();
        repaint();
        notifyListeners();
      }
    };
    theLabels = new ColorLabel[2];
    for( int i = 0 ; i < 2 ; i++ )
    {
      theLabels[i] = new ColorLabel(theChooser);
      theLabels[i].setColor(new Color(0,0,0,0));
      theLabels[i].addChangeListener(listener);
    }

    setBackground(new Color(0,0,0,0));
    BufferedImage bi = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB); 
    Graphics2D big = bi.createGraphics(); 
    big.setColor(new Color(122,138,153)); 
    big.fillRect(0,0,8,8); 
    big.fillRect(8,8,8,8); 
    big.setColor(new Color(238,238,238)); 
    big.fillRect(8,0,8,8); 
    big.fillRect(0,8,8,8); 

    Rectangle r = new Rectangle(0,0,16,16); 
    theBackgroundPaint = new TexturePaint(bi,r); 


    GridBagLayout gridBagLayout = new GridBagLayout();
    this.setLayout(gridBagLayout);

    GridBagConstraints gbc;

    JPanel holder = new JPanel(new BorderLayout());
    holder.add(theLabels[0],BorderLayout.CENTER);
    holder.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 0;
    gbc.weighty = 1;
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gridBagLayout.setConstraints(holder, gbc);
    this.add(holder);
    holder.setMinimumSize(new Dimension(20,20));
    holder.setPreferredSize(new Dimension(20,20));

    holder = new JPanel(new BorderLayout());
    holder.add(theLabels[1],BorderLayout.CENTER);
    holder.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.anchor = GridBagConstraints.LAST_LINE_END;
    gridBagLayout.setConstraints(holder, gbc);
    this.add(holder);
    holder.setMinimumSize(new Dimension(20,20));
    holder.setPreferredSize(new Dimension(20,20));

    createPercentages();
  }

  private static void createPercentages()
  {
    thePercentages = new double[theSize][theSize];

    Point2D.Double center = new Point2D.Double(((double)theSize)/2,((double)theSize)/2);
    double         radius = (Math.pow(   Math.pow(theSize,2)+Math.pow(theSize,2)   ,  0.5   ))/2;

    Point2D.Double current;
    double distance;
    double percentage;
    for( int x = 0 ; x < theSize ; x++ )
    {
      for( int y = 0 ; y < theSize ; y++ )
      {
        current    = new Point2D.Double(x,y);
        distance   = DistanceGetter.getDistance(center,current);
        percentage = distance/radius;
        thePercentages[x][y] = percentage;
      }
    }
  }

  private void updateImage()
  {
    theImage   = new BufferedImage(theSize, theSize, BufferedImage.TYPE_INT_ARGB);

    Color outerColor = theLabels[0].getColor();
    Color innerColor = theLabels[1].getColor();

    int rDiff = outerColor.getRed()   - innerColor.getRed();
    int gDiff = outerColor.getGreen() - innerColor.getGreen();
    int bDiff = outerColor.getBlue()  - innerColor.getBlue();
    int aDiff = outerColor.getAlpha() - innerColor.getAlpha();

    Color  curColor;
    for( int x = 0 ; x < theSize ; x++ )
    {
      for( int y = 0 ; y < theSize ; y++ )
      {
        curColor   = new Color
                     (
                       (int)(innerColor.getRed()   + rDiff*thePercentages[x][y]),
                       (int)(innerColor.getGreen() + gDiff*thePercentages[x][y]),
                       (int)(innerColor.getBlue()  + bDiff*thePercentages[x][y]),
                       (int)(innerColor.getAlpha() + aDiff*thePercentages[x][y])
                     );
        theImage.setRGB(x,y,curColor.getRGB());
      }
    }

    thePaint = new TransformableTexturePaint(theImage,new Rectangle(theSize,theSize), TransformableTexturePaint.KIND_RADIAL);
    thePaint.setType(TransformableTexturePaint.TYPE_STRETCH);
    thePaint.setColors(new Color[]{theLabels[0].getColor(),theLabels[1].getColor()});

    theForegroundPaint = new TransformableTexturePaint(theImage,new Rectangle(getWidth(),getHeight()), TransformableTexturePaint.KIND_RADIAL);
    theForegroundPaint.setType(TransformableTexturePaint.TYPE_STRETCH);
    theForegroundPaint.setColors(new Color[]{theLabels[0].getColor(),theLabels[1].getColor()});
  }

  public static BufferedImage createImage( Color[] someColors, int width, int height )
  {
    createPercentages();

    BufferedImage image = new BufferedImage(theSize,theSize,BufferedImage.TYPE_INT_ARGB);


    Color outerColor = someColors[0];
    Color innerColor = someColors[1];

    int rDiff = outerColor.getRed()   - innerColor.getRed();
    int gDiff = outerColor.getGreen() - innerColor.getGreen();
    int bDiff = outerColor.getBlue()  - innerColor.getBlue();
    int aDiff = outerColor.getAlpha() - innerColor.getAlpha();

    Color  curColor;
    for( int x = 0 ; x < theSize ; x++ )
    {
      for( int y = 0 ; y < theSize ; y++ )
      {
        curColor   = new Color
                     (
                       (int)(innerColor.getRed()   + rDiff*thePercentages[x][y]),
                       (int)(innerColor.getGreen() + gDiff*thePercentages[x][y]),
                       (int)(innerColor.getBlue()  + bDiff*thePercentages[x][y]),
                       (int)(innerColor.getAlpha() + aDiff*thePercentages[x][y])
                     );
        image.setRGB(x,y,curColor.getRGB());
      }
    }
    return image;
  }
 
  public void paint( Graphics g )
  {
    if( thePaint == null )
      updateImage();

    if( theForegroundPaint.getAnchor().width != getWidth() || theForegroundPaint.getAnchor().height != getHeight() )
      theForegroundPaint.setAnchor(new Rectangle(getWidth(),getHeight()));

    Graphics2D g2D = (Graphics2D)g;
    g2D.setPaint(theBackgroundPaint);
    g2D.fillRect(0,0,getWidth(),getHeight());
    g2D.setPaint(theForegroundPaint);
    g2D.fillRect(0,0,getWidth(),getHeight());

    super.paint(g);
  }
  
  public TransformableTexturePaint getPaint()
  {
    return thePaint;
  }

  public void setPaint( TransformableTexturePaint aPaint )
  {
    if( aPaint.getKind() != TransformableTexturePaint.KIND_RADIAL )
      return;

    Color[] colors = aPaint.getColors();
    for( int i = 0 ; i < colors.length ; i++ )
      theLabels[i].setColor(colors[i]);

    updateImage();
    notifyListeners();
  }

  public void addChangeListener( ChangeListener aListener )
  {
    theChangeListeners.add(aListener);
  }

  public void removeChangeListener( ChangeListener aListener )
  {
    theChangeListeners.remove(aListener);
  }

  private void notifyListeners()
  {
    for( int i = 0 ; i < theChangeListeners.size() ; i++ )
    {
      ((ChangeListener)theChangeListeners.get(i)).stateChanged(new ChangeEvent(this));
    }
  }
}