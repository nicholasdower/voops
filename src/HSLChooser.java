/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

public class HSLChooser extends JPanel
{
  private HSChooser theHSChooser;
  private LChooserWithTriangle  theLChooser;

  private Vector theChangeListeners;

  public HSLChooser()
  {
    theChangeListeners = new Vector();

    theHSChooser = new HSChooser();
    theLChooser  = new LChooserWithTriangle();

    theHSChooser.addChangeListener
    (
      new ChangeListener()
      {
        public void stateChanged( ChangeEvent e )
        {
          HSChanged();
        }
      }
    );

    theLChooser.addChangeListener
    (
      new ChangeListener()
      {
        public void stateChanged( ChangeEvent e )
        {
          LChanged();
        }
      }
    );

    GridBagLayout gridBagLayout = new GridBagLayout();
    this.setLayout(gridBagLayout);

    GridBagConstraints gbc;

    gbc = new GridBagConstraints();

    JPanel HSChooserWrapper = new JPanel(new BorderLayout());
    HSChooserWrapper.add(theHSChooser,BorderLayout.CENTER);
    HSChooserWrapper.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(3,2,2,0);
    gbc.anchor = GridBagConstraints.LINE_END;
    gbc.fill = GridBagConstraints.BOTH;
    gridBagLayout.setConstraints(HSChooserWrapper, gbc);
    this.add(HSChooserWrapper);

    gbc = new GridBagConstraints();
    theLChooser.setBorder(null);
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.weightx = .05f;
    gbc.weighty = 1;
    gbc.insets = new Insets(2,2,2,0);
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.fill = GridBagConstraints.BOTH;
    gridBagLayout.setConstraints(theLChooser, gbc);
    this.add(theLChooser);
  }

  public void setHSL(float[] anHSL)
  {
    theHSChooser.setHS(anHSL);
    theLChooser.setColor(theHSChooser.getColor());
    theLChooser.setLuminance(anHSL[2]/100f);
  }

  public void setColor(Color aColor)
  {
    theHSChooser.setColor(aColor);
    theLChooser.setColor(aColor);
  }

  public Color getColor()
  {
    return theLChooser.getColor();
  }

  public float[] getHSL()
  {
    float[] hs = theHSChooser.getHS();
    return new float[]{hs[0],hs[1],theLChooser.getLuminance()*100};
  }

  private void HSChanged()
  {
    theLChooser.setColorHS(theHSChooser.getColor());
    notifyListeners();
  }

  private void LChanged()
  {
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