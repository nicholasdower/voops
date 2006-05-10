/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import javax.swing.JPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.BorderLayout;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.util.Vector;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;

public class LChooserWithTriangle extends JPanel
{
  private int theLum = 0;
  private LChooser theLChooser;
  private TrianglePanel theTriangle;

  private Vector theChangeListeners;

  public LChooserWithTriangle()
  {
    theLChooser = new LChooser();
    theLChooser.addMouseListener
    (
      new MouseAdapter()
      {
        public void mousePressed( MouseEvent e )
        {
          theTriangle.setPointer(e.getY());
          notifyListeners();
        }
      }
    );
    theLChooser.addMouseMotionListener
    (
      new MouseMotionAdapter()
      {
        public void mouseDragged( MouseEvent e )
        {
          theTriangle.setPointer(e.getY()); 
          notifyListeners();
        }
      }
    );

    theTriangle = new TrianglePanel();
    theTriangle.addMouseListener
    (
      new MouseAdapter()
      {
        public void mousePressed( MouseEvent e )
        {
          theTriangle.setPointer(e.getY());
          notifyListeners();
        }
      }
    );

    theTriangle.addMouseMotionListener
    (
      new MouseMotionAdapter()
      {
        public void mouseDragged( MouseEvent e )
        {
          theTriangle.setPointer(e.getY());
          notifyListeners();
        }
      }
    );
 
    GridBagLayout gridBagLayout = new GridBagLayout();
    this.setLayout(gridBagLayout);

    GridBagConstraints gbc;

    gbc = new GridBagConstraints();

    JPanel theLChooserWrapper = new JPanel(new BorderLayout());
    theLChooserWrapper.add(theLChooser,BorderLayout.CENTER);
    theLChooserWrapper.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(1,0,0,0);
    gbc.anchor = GridBagConstraints.LINE_END;
    gbc.fill = GridBagConstraints.BOTH;
    gridBagLayout.setConstraints(theLChooserWrapper, gbc);
    this.add(theLChooserWrapper);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.weightx = 0;
    gbc.weighty = 1;
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.fill = GridBagConstraints.BOTH;
    gridBagLayout.setConstraints(theTriangle, gbc);
    this.add(theTriangle);

    theChangeListeners = new Vector();
  }

  public void setColor( Color aColor ) 
  {
    theLChooser.setColor(aColor);
    //theTriangle.setColor(aColor); 
  }

  public Color getColor() 
  {
    Color c = theLChooser.getColor();
    float[] hsl = HSLRGBConverter.RGBtoHSL(c.getRGB());
    return new Color(HSLRGBConverter.HSLtoRGB(hsl[0],hsl[1],theTriangle.getLuminance()*100));
  }

  public void setColorHS( Color aColor ) 
  {
    theLChooser.setColor(aColor);
  }

  public void setLuminance( float aLum)
  {
    theTriangle.setLuminance(aLum);
  }

  public float getLuminance()
  {
    return theTriangle.getLuminance();
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