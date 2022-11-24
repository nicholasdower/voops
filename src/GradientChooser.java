/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.util.Vector;

public class GradientChooser extends JPanel
{
  public static final int TYPE_STRETCH = 12345;
  public static final int TYPE_TILE    = 54321;

  private MiniColorChooser theChooser;
  private Gradient         theGradient;
  private int              theType = TYPE_TILE;

  private JRadioButton theStretchButton;
  private JRadioButton theTileButton;

  private SliderSpinnerCombo theWidthSlider;
  private SliderSpinnerCombo theHeightSlider;

  private TransformableTexturePaint thePaint;

  private Vector theChangeListeners;

  public GradientChooser( MiniColorChooser aChooser )
  {
    theChangeListeners = new Vector();

    theChooser = aChooser;


    ChangeListener listener = new ChangeListener()
    {
      public void stateChanged( ChangeEvent e )
      {
        thePaint = theGradient.getPaint();
        setType();
        notifyListeners();
      }
    };

    theGradient = new Gradient(theChooser);
    theGradient.addChangeListener(listener);

    GridBagLayout gridBagLayout = new GridBagLayout();
    this.setLayout(gridBagLayout);

    GridBagConstraints gbc;

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(2,2,2,2);
    gbc.anchor = GridBagConstraints.LINE_END;
    gbc.fill = GridBagConstraints.BOTH;
    gridBagLayout.setConstraints(theGradient, gbc);
    this.add(theGradient);

    theStretchButton = new JRadioButton("Stretch");
    theStretchButton.setSelected(true);
    theStretchButton.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          theType = TYPE_STRETCH;
          theGradient.setType(TYPE_STRETCH);
          thePaint = theGradient.getPaint();
          setType();
          theWidthSlider.setEnabled(false);
          theHeightSlider.setEnabled(false);
          notifyListeners();
        }
      }
    );
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = 1;
    gbc.weighty = .06f;
    gbc.insets = new Insets(1,2,1,2);
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theStretchButton, gbc);
    this.add(theStretchButton);


    theTileButton = new JRadioButton("Tile");
    theTileButton.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          theType = TYPE_TILE;
          theGradient.setType(TYPE_TILE);
          thePaint = theGradient.getPaint();
          setType();
          theWidthSlider.setEnabled(true);
          theHeightSlider.setEnabled(true);
          notifyListeners();
        }
      }
    );
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.weightx = 1;
    gbc.weighty = .06f;
    gbc.insets = new Insets(1,2,1,2);
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theTileButton, gbc);
    this.add(theTileButton);

    ButtonGroup bg = new ButtonGroup();
    bg.add(theStretchButton);
    bg.add(theTileButton);

    theWidthSlider = new SliderSpinnerCombo(0,1000,1,"W");
    theWidthSlider.addChangeListener(listener);
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.weightx = 1;
    gbc.weighty = .06f;
    gbc.insets = new Insets(1,2,1,2);
    gbc.anchor = GridBagConstraints.LINE_END;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theWidthSlider, gbc);
    this.add(theWidthSlider);

    theHeightSlider = new SliderSpinnerCombo(0,1000,1,"H");
    theHeightSlider.addChangeListener(listener);
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.weightx = 1;
    gbc.weighty = .06f;
    gbc.insets = new Insets(1,2,1,2);
    gbc.anchor = GridBagConstraints.LINE_END;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theHeightSlider, gbc);
    this.add(theHeightSlider);

    theWidthSlider.setEnabled(false);
    theHeightSlider.setEnabled(false);
  }

  public TransformableTexturePaint getPaint()
  {
    return thePaint;
  }

  public void setPaint( TransformableTexturePaint aPaint )
  {
    theGradient.setPaint(aPaint);


    if( aPaint.getType() == TransformableTexturePaint.TYPE_STRETCH )
    {
      theStretchButton.setSelected(true);
      theWidthSlider.setEnabled(false);
      theHeightSlider.setEnabled(false);
    }
    else
    {
      theTileButton.setSelected(true);
      theWidthSlider.setValue((int)aPaint.getAnchor().width);
      theHeightSlider.setValue((int)aPaint.getAnchor().height);
      theWidthSlider.setEnabled(true);
      theHeightSlider.setEnabled(true);
    }
    notifyListeners();
  }

  private void setType()
  {
    if( thePaint != null )
    {
      if( theStretchButton.isSelected() )
        thePaint.setType(TransformableTexturePaint.TYPE_STRETCH);
      else
      {
        thePaint.setType(TransformableTexturePaint.TYPE_STRETCH);
        thePaint.setAnchor(new Rectangle(0,0,theWidthSlider.getValue(),theHeightSlider.getValue()));
        thePaint.setType(TransformableTexturePaint.TYPE_TILE);
      }
    }
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