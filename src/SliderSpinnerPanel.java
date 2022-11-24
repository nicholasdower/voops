/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.util.Vector;

public class SliderSpinnerPanel extends JPanel
{
  private JLabel theHEXINTButton;
  private SliderSpinnerCombo[] theHSLControls;
  private IntHexSliderSpinnerCombo[] theRGBAControls;
  private boolean isHex = false;
  private boolean isChanging;

  private Vector theChangeListeners;

  public SliderSpinnerPanel()
  {
    theChangeListeners = new Vector();

    JTabbedPane pane = new JTabbedPane();

    theHSLControls = new SliderSpinnerCombo[3];
    theHSLControls[0] = new SliderSpinnerCombo(0,359,0,"H");
    theHSLControls[1] = new SliderSpinnerCombo(0,100,0,"S");
    theHSLControls[2] = new SliderSpinnerCombo(0,100,0,"L");

    theRGBAControls = new IntHexSliderSpinnerCombo[4];
    theRGBAControls[0] = new IntHexSliderSpinnerCombo("R");
    theRGBAControls[1] = new IntHexSliderSpinnerCombo("G");
    theRGBAControls[2] = new IntHexSliderSpinnerCombo("B");
    theRGBAControls[3] = new IntHexSliderSpinnerCombo("A");

    theRGBAControls[3].setValue(255);

    ChangeListener listener = new ChangeListener()
    {
      public void stateChanged( ChangeEvent e )
      {
        if( isChanging )
          return;

        isChanging = true;
        if( e.getSource().equals(theRGBAControls[0]) || e.getSource().equals(theRGBAControls[1]) || e.getSource().equals(theRGBAControls[2]) )
        {
          float[] hsl = HSLRGBConverter.RGBtoHSL(theRGBAControls[0].getValue(),theRGBAControls[1].getValue(),theRGBAControls[2].getValue());
          theHSLControls[0].setValue((int)hsl[0]);
          theHSLControls[1].setValue((int)hsl[1]);
          theHSLControls[2].setValue((int)hsl[2]);
        }
        else
        {
          Color color = new Color(HSLRGBConverter.HSLtoRGB(theHSLControls[0].getValue(),theHSLControls[1].getValue(),theHSLControls[2].getValue()));
          theRGBAControls[0].setValue(color.getRed());
          theRGBAControls[1].setValue(color.getGreen());
          theRGBAControls[2].setValue(color.getBlue());
        }
        isChanging = false;
        notifyListeners();
      }
    };

    theRGBAControls[0].addChangeListener(listener);
    theRGBAControls[1].addChangeListener(listener);
    theRGBAControls[2].addChangeListener(listener);
    theRGBAControls[3].addChangeListener(listener);
    theHSLControls[0].addChangeListener(listener);
    theHSLControls[1].addChangeListener(listener);
    theHSLControls[2].addChangeListener(listener);

    JPanel RGBAPanel = new JPanel();
    pane.addTab("RGB",RGBAPanel);

    JPanel HSLPanel = new JPanel();
    pane.addTab("HSL",HSLPanel);

    GridBagLayout gridBagLayout = new GridBagLayout();
    HSLPanel.setLayout(gridBagLayout);

    GridBagConstraints gbc;

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(2,0,2,0);
    gbc.anchor = GridBagConstraints.PAGE_END;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theHSLControls[0], gbc);
    HSLPanel.add(theHSLControls[0]);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.insets = new Insets(2,0,2,0);
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theHSLControls[1], gbc);
    HSLPanel.add(theHSLControls[1]);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.insets = new Insets(2,0,2,0);
    gbc.anchor = GridBagConstraints.PAGE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theHSLControls[2], gbc);
    HSLPanel.add(theHSLControls[2]);



    GridBagLayout gridBagLayout2 = new GridBagLayout();
    RGBAPanel.setLayout(gridBagLayout2);

    theHEXINTButton = new JLabel("DEC");
    theHEXINTButton.addMouseListener
    (
      new MouseAdapter()
      {
        public void mousePressed( MouseEvent e )
        {
          isHex = !isHex;
          if( isHex )
          {
            theRGBAControls[0].setHex();
            theRGBAControls[1].setHex();
            theRGBAControls[2].setHex();
            theHEXINTButton.setText("HEX");
          }
          else
          {
            theRGBAControls[0].setInt();
            theRGBAControls[1].setInt();
            theRGBAControls[2].setInt();
            theHEXINTButton.setText("DEC");
          }
        }
      }
    );
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weighty = 1;
    gbc.insets = new Insets(2,0,2,0);
    gbc.anchor = GridBagConstraints.LAST_LINE_END;
    gridBagLayout2.setConstraints(theHEXINTButton, gbc);
    RGBAPanel.add(theHEXINTButton);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.insets = new Insets(2,0,2,0);
    gbc.anchor = GridBagConstraints.PAGE_END;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout2.setConstraints(theRGBAControls[0], gbc);
    RGBAPanel.add(theRGBAControls[0]);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.insets = new Insets(2,0,2,0);
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout2.setConstraints(theRGBAControls[1], gbc);
    RGBAPanel.add(theRGBAControls[1]);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(2,0,2,0);
    gbc.anchor = GridBagConstraints.PAGE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout2.setConstraints(theRGBAControls[2], gbc);
    RGBAPanel.add(theRGBAControls[2]);




    GridBagLayout gridBagLayout3 = new GridBagLayout();
    this.setLayout(gridBagLayout3);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(2,0,2,0);
    gbc.anchor = GridBagConstraints.PAGE_END;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout3.setConstraints(pane, gbc);
    this.add(pane);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(2,2,2,3);
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout3.setConstraints(theRGBAControls[3], gbc);
    this.add(theRGBAControls[3]);
    theRGBAControls[3].setMinimumSize(new Dimension(50,20));
    theRGBAControls[3].setMaximumSize(new Dimension(50,20));
    theRGBAControls[3].setPreferredSize(new Dimension(50,20));
  }

  public void setColor( Color aColor )
  {
    theRGBAControls[0].setValue(aColor.getRed());
    theRGBAControls[1].setValue(aColor.getGreen());
    theRGBAControls[2].setValue(aColor.getBlue());
    theRGBAControls[3].setValue(aColor.getAlpha());
  }

  public Color getColor()
  {
    Color color = new Color(HSLRGBConverter.HSLtoRGB(theHSLControls[0].getValue(),theHSLControls[1].getValue(),theHSLControls[2].getValue()));
    return new Color(color.getRed(),color.getGreen(),color.getBlue(),theRGBAControls[3].getValue());
  }

  public float[] getHSL()
  {
    return new float[]{359-theHSLControls[0].getValue(),theHSLControls[1].getValue(),theHSLControls[2].getValue()};
  }

  public void setHSL(float[] anHSL)
  {
    theHSLControls[0].setValue((int)anHSL[0]);
    theHSLControls[1].setValue((int)anHSL[1]);
    theHSLControls[2].setValue((int)anHSL[2]);
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