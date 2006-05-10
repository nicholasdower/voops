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
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.image.AffineTransformOp;
import java.awt.geom.AffineTransform;
import java.util.Vector;

public class Gradient extends JPanel
{
  private MiniColorChooser theChooser;
  private Paint theBackgroundPaint;
  private BufferedImage theImage;
  private TransformableTexturePaint thePaint;
  private ColorLabel[]       theLabels;

  private int theType;

  private Vector theChangeListeners;

  public Gradient( MiniColorChooser aChooser )
  {
    theChangeListeners = new Vector();

    theType = TransformableTexturePaint.TYPE_STRETCH;
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
    theLabels = new ColorLabel[4];
    for( int i = 0 ; i < 4 ; i++ )
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
    gbc.weighty = 0;
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
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.anchor = GridBagConstraints.FIRST_LINE_END;
    gridBagLayout.setConstraints(holder, gbc);
    this.add(holder);
    holder.setMinimumSize(new Dimension(20,20));
    holder.setPreferredSize(new Dimension(20,20));

    holder = new JPanel(new BorderLayout());
    holder.add(theLabels[2],BorderLayout.CENTER);
    holder.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.anchor = GridBagConstraints.LAST_LINE_START;
    gridBagLayout.setConstraints(holder, gbc);
    this.add(holder);
    holder.setMinimumSize(new Dimension(20,20));
    holder.setPreferredSize(new Dimension(20,20));

    holder = new JPanel(new BorderLayout());
    holder.add(theLabels[3],BorderLayout.CENTER);
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
  }

  private void updateImage()
  {
    theImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB); 

    Color[] gradient0 = ColorGradienter.gradient(theLabels[0].getColor(),theLabels[2].getColor(),getHeight());
    Color[] gradient1 = ColorGradienter.gradient(theLabels[1].getColor(),theLabels[3].getColor(),getHeight());
    Color[] gradient2;
    for( int i = 0 ; i < getHeight() ; i++ )
    {
      gradient2 = ColorGradienter.gradient(gradient0[i],gradient1[i],getWidth());
      for( int j = 0 ; j < getWidth() ; j++ )
      {
        theImage.setRGB(j,i,gradient2[j].getRGB());
      }
    }
    if( theType == TransformableTexturePaint.TYPE_STRETCH )
    {
      thePaint = new TransformableTexturePaint(theImage,new Rectangle(getWidth(),getHeight()),TransformableTexturePaint.KIND_GRADIENT);
      thePaint.setColors(new Color[]{theLabels[0].getColor(),theLabels[1].getColor(),theLabels[2].getColor(),theLabels[3].getColor()});
    }
    else
    {
      BufferedImage tileImage = new BufferedImage(theImage.getWidth()*2, theImage.getHeight()*2,BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2D = tileImage.createGraphics();

      g2D.drawImage(theImage,0,0,null);

      AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
      tx.translate(-theImage.getWidth(null), 0);
      AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
      BufferedImage bufferedImage = op.filter(theImage, null);
      g2D.drawImage(bufferedImage,theImage.getWidth(),0,null);

      tx = AffineTransform.getScaleInstance(1, -1);
      tx.translate(0,-theImage.getHeight(null));
      op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
      bufferedImage = op.filter(theImage, null);
      g2D.drawImage(bufferedImage,0,theImage.getHeight(),null);

      tx = AffineTransform.getScaleInstance(-1, -1);
      tx.translate(-theImage.getWidth(null),-theImage.getHeight(null));
      op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
      bufferedImage = op.filter(theImage, null);
      g2D.drawImage(bufferedImage,theImage.getWidth(),theImage.getHeight(),null);

      g2D.dispose();

      thePaint = new TransformableTexturePaint(tileImage,new Rectangle(getWidth(),getHeight()),TransformableTexturePaint.KIND_GRADIENT);
      thePaint.setColors(new Color[]{theLabels[0].getColor(),theLabels[1].getColor(),theLabels[2].getColor(),theLabels[3].getColor()});
    }
  }

  public static BufferedImage createImage( Color[] someColors, int width, int height )
  {
    BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
    Color[] gradient0 = ColorGradienter.gradient(someColors[0],someColors[2],height);
    Color[] gradient1 = ColorGradienter.gradient(someColors[1],someColors[3],height);
    Color[] gradient2;
    for( int i = 0 ; i < height ; i++ )
    {
      gradient2 = ColorGradienter.gradient(gradient0[i],gradient1[i],width);
      for( int j = 0 ; j < width ; j++ )
      {
        image.setRGB(j,i,gradient2[j].getRGB());
      }
    }
    return image;
  }
 
  public void paint( Graphics g )
  {
    if( theImage == null || theImage.getWidth() != getWidth() || theImage.getHeight() != getHeight() )
      updateImage();
    Graphics2D g2D = (Graphics2D)g;
    g2D.setPaint(theBackgroundPaint);
    g2D.fillRect(0,0,getWidth(),getHeight());
    g2D.drawImage(theImage,0,0,this);
    super.paint(g);
  }

  public void setType( int aType )
  {
    theType = aType;
    updateImage();
  }
  
  public TransformableTexturePaint getPaint()
  {
    return thePaint;
  }

  public void setPaint( TransformableTexturePaint aPaint )
  {
    if( aPaint.getKind() != TransformableTexturePaint.KIND_GRADIENT )
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