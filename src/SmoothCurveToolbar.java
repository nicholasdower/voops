/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class SmoothCurveToolbar extends JPanel
{
  private Curveable theTool;

  private JSlider thePaddingSlider;
  private JLabel  thePaddingLabel;

  private JTextField theMinDistanceField;
  private JLabel     theMinDistanceLabel;

  public SmoothCurveToolbar()
  {
    thePaddingSlider = new JSlider(0,100,45);
    thePaddingLabel = new JLabel("Padding:");

    thePaddingSlider.addChangeListener
    (
      new ChangeListener()
      {
        public void stateChanged( ChangeEvent e )
        {
          update();
        }
      }
    );

    theMinDistanceField = new JTextField("14");
    theMinDistanceLabel = new JLabel("Threshold:");

    theMinDistanceField.getDocument().addDocumentListener
    (
      new DocumentListener()
      {
        public void changedUpdate( DocumentEvent e){update();}
        public void insertUpdate( DocumentEvent e) {update();}
        public void removeUpdate( DocumentEvent e) {update();}
      }
    );

    GridBagLayout gridBagLayout = new GridBagLayout();
    this.setLayout(gridBagLayout);

    GridBagConstraints gbc;

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 0;
    gbc.weighty = 1;
    gbc.insets = new Insets(2,2,2,2);
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(thePaddingLabel, gbc);
    this.add(thePaddingLabel);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.insets = new Insets(2,2,2,2);
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(thePaddingSlider, gbc);
    this.add(thePaddingSlider);

    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 0;
    gbc.weightx = 0;
    gbc.weighty = 0;
    gbc.insets = new Insets(2,2,2,2);
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theMinDistanceLabel, gbc);
    this.add(theMinDistanceLabel);

    gbc = new GridBagConstraints();
    gbc.gridx = 3;
    gbc.gridy = 0;
    gbc.weightx = .4f;
    gbc.weighty = 0;
    gbc.insets = new Insets(2,2,2,2);
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theMinDistanceField, gbc);
    this.add(theMinDistanceField);
  }

  private void update()
  {
    if( theTool == null )
      return;
    try
    {
      theTool.setCurveSettings(((double)thePaddingSlider.getValue())/100,Double.parseDouble(theMinDistanceField.getText()));
    }
    catch( NumberFormatException nfe )
    {
    }
  }

  public void setTool( Curveable aTool )
  {
    theTool = aTool;
    update();
  }
}