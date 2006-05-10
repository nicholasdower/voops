/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.TexturePaint;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import java.awt.Insets;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.util.Vector;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class ColorLabel extends JLabel
{
  private Color            theColor;
  private Paint            theForegroundPaint;
  private MiniColorChooser theChooser;
  private Paint            theBackgroundPaint;

  private Vector theChangeListeners;

  public ColorLabel()
  {
    this(null);
    this.addComponentListener
    (
      new ComponentAdapter()
      {
        public void componentResized( ComponentEvent e )
        {
          if( theForegroundPaint != null )
            ((TransformableTexturePaint)theForegroundPaint).setAnchor(new Rectangle2D.Double(0,0,getWidth(),getHeight()));
        }
      } 
    );
  }

  public ColorLabel( MiniColorChooser aChooser )
  {
    theChangeListeners = new Vector();

    theChooser = aChooser;

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

    setOpaque(true);

    if( theChooser != null )
    {
      MouseAdapter adapter = new MouseAdapter()
      {
        public void mousePressed( MouseEvent e )
        {
          Color color = theChooser.getColor();
          theForegroundPaint = color;
          theColor = color;
          notifyListeners();
        }
      };

      addMouseListener(adapter);
    }
  }

  public void paint( Graphics g )
  {
    Graphics2D g2D = (Graphics2D)g;
    g2D.setPaint(theBackgroundPaint);
    g2D.fillRect(0,0,getWidth(),getHeight());
    g2D.setPaint(theForegroundPaint);
    g2D.fillRect(0,0,getWidth(),getHeight());
  }

  public Color getColor()
  {
    return theColor;
  }

  public void setColor( Color aColor)
  {
    theColor = aColor;
    theForegroundPaint = aColor;
  }

  public Paint getPaint()
  {
    return theForegroundPaint;
  }

  public void setPaint( TransformableTexturePaint aPaint)
  {
    theForegroundPaint = (TransformableTexturePaint)aPaint.clone();
    ((TransformableTexturePaint)theForegroundPaint).setAnchor(new Rectangle2D.Double(0,0,getWidth(),getHeight()));
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