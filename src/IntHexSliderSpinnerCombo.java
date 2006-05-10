/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.util.Vector;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import java.awt.Dimension;

public class IntHexSliderSpinnerCombo extends JPanel
{
  private JSlider       theSlider;
  private IntHexSpinner theSpinner;

  private Vector theChangeListeners;

  private boolean isChanging = false;

  public IntHexSliderSpinnerCombo(String aLabel)
  {
    theChangeListeners = new Vector();

    theSpinner = new IntHexSpinner();
    theSpinner.setFont(new Font("Times",Font.BOLD,12));
    theSlider = new JSlider(0,255,0);
  
    theSpinner.addChangeListener
    (
      new ChangeListener()
      {
        public void stateChanged( ChangeEvent e )
        {
          if( isChanging )
            return;

          isChanging = true;
          theSlider.setValue(theSpinner.getCurrentValue());
          notifyListeners();
          isChanging = false;
        }
      }
    );

    theSlider.addChangeListener
    (
      new ChangeListener()
      {
        public void stateChanged( ChangeEvent e )
        {
          if( isChanging )
            return;

          isChanging = true;
          theSpinner.setCurrentValue(theSlider.getValue());
          notifyListeners();
          isChanging = false;
        }
      }
    );

    GridBagLayout gridBagLayout = new GridBagLayout();
    this.setLayout(gridBagLayout);

    GridBagConstraints gbc;

    gbc = new GridBagConstraints();
    JLabel label = new JLabel(aLabel);
    label.setFont(new Font("Times",Font.BOLD,12));
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 0;
    gbc.weighty = 0;
    gbc.insets = new Insets(0,2,0,3);
    gbc.anchor = GridBagConstraints.LINE_END;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(label, gbc);
    this.add(label);
    label.setMinimumSize(new Dimension(20,20));
    label.setMaximumSize(new Dimension(20,20));
    label.setPreferredSize(new Dimension(20,20));


    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.weightx = 1f;
    gbc.weighty = 0;
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theSlider, gbc);
    this.add(theSlider);

    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 0;
    gbc.weightx = 0;
    gbc.weighty = 0;
    gbc.insets = new Insets(0,2,0,0);
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theSpinner, gbc);
    this.add(theSpinner);
    theSpinner.setMinimumSize(new Dimension(50,20));
    theSpinner.setMaximumSize(new Dimension(50,20));
    theSpinner.setPreferredSize(new Dimension(50,20));
  }

  public int getValue()
  {
    return theSlider.getValue();
  }

  public void setValue( int aValue )
  {
    theSlider.setValue(aValue);
  }

  public void setHex()
  {
    theSpinner.setHex();
  } 

  public void setInt()
  {
    theSpinner.setInt();
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