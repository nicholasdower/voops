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

public class SolidChooser extends JPanel
{
  private Color            theColor;
  private MiniColorChooser theChooser;
  private JLabel           theLabel;

  private Vector theChangeListeners;

  public SolidChooser( MiniColorChooser aChooser, String aTitle )
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
    final Paint backgroundPaint = new TexturePaint(bi,r); 

    theLabel = new JLabel()
    {
      public void paint( Graphics g )
      {
        Graphics2D g2D = (Graphics2D)g;
        g2D.setPaint(backgroundPaint);
        g2D.fillRect(0,0,getWidth(),getHeight());
        g2D.setPaint(getBackground());
        g2D.fillRect(0,0,getWidth(),getHeight());
      }
    };
    theLabel.setOpaque(true);

    MouseAdapter adapter = new MouseAdapter()
    {
      public void mousePressed( MouseEvent e )
      {
        Color color = theChooser.getColor();
        theLabel.setBackground(color);
        theColor = color;
        notifyListeners();
      }
    };

    theLabel.addMouseListener(adapter);

    JPanel holder = new JPanel(new BorderLayout());
    holder.add(theLabel,BorderLayout.CENTER);
    holder.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

    GridBagLayout gridBagLayout = new GridBagLayout();
    this.setLayout(gridBagLayout);

    GridBagConstraints gbc;

    JLabel title = new JLabel(aTitle);
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = .2;
    gbc.weighty = 1;
    gbc.insets = new Insets(2,2,2,4);
    gbc.anchor = GridBagConstraints.LINE_END;
    gridBagLayout.setConstraints(title, gbc);
    this.add(title);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.weightx = .8;
    gbc.weighty = 1;
    gbc.anchor = GridBagConstraints.LINE_START;
    gridBagLayout.setConstraints(holder, gbc);
    this.add(holder);
    holder.setMinimumSize(new Dimension(40,40));
    holder.setPreferredSize(new Dimension(40,40));
  }

  public Paint getColor()
  {
    return theColor;
  }

  public void setColor( Color aColor)
  {
    theColor = aColor;
    theLabel.setBackground(theColor);
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