/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.BoxLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

public class RoundRectangleToolbar extends JPanel
{
  private static AspectRatioToolbar theAspectRatioToolbar = new AspectRatioToolbar();
  private RoundRectangleTool theTool;
  private JTextField         theWidthField;
  private JTextField         theHeightField;
  private JLabel             theWidthLabel;
  private JLabel             theHeightLabel;

  public RoundRectangleToolbar()
  {
    theAspectRatioToolbar = new AspectRatioToolbar();

    DocumentListener angleListener = new DocumentListener()
    {
      public void changedUpdate( DocumentEvent e){updateAngle();}
      public void insertUpdate( DocumentEvent e) {updateAngle();}
      public void removeUpdate( DocumentEvent e) {updateAngle();}
    };

    theWidthField   = new JTextField("10",2);
    theWidthField.getDocument().addDocumentListener(angleListener);
    theHeightField  = new JTextField("10",2);
    theHeightField.getDocument().addDocumentListener(angleListener);
    theWidthLabel   = new JLabel("W:");
    theHeightLabel  = new JLabel("H:");

    JPanel anglePanel = new JPanel();
    anglePanel.setLayout(new BoxLayout(anglePanel,BoxLayout.X_AXIS));
    anglePanel.add(theWidthLabel);
    anglePanel.add(theWidthField);
    anglePanel.add(theHeightLabel);
    anglePanel.add(theHeightField);

    GridBagLayout gridBagLayout = new GridBagLayout();
    this.setLayout(gridBagLayout);

    GridBagConstraints gbc;

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.insets = new Insets(2,2,0,2);
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(anglePanel, gbc);
    this.add(anglePanel);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.fill = GridBagConstraints.BOTH;
    gridBagLayout.setConstraints(theAspectRatioToolbar, gbc);
    this.add(theAspectRatioToolbar);
  }

  private void updateAngle()
  {
    if( theTool == null )
      return;
    try
    {
      double width  = Double.parseDouble(theWidthField.getText());
      double height = Double.parseDouble(theHeightField.getText());


      theTool.setArcWidth(width);
      theTool.setArcHeight(height);
    }
    catch( NumberFormatException nfe )
    {
    }
  }

  public void setTool(RoundRectangleTool aTool)
  {
    theAspectRatioToolbar.setTool(aTool);
    theTool = aTool;
    updateAngle();
  }

  public void setStatus( double aWidth, double aHeight )
  {
    theAspectRatioToolbar.setStatus(aWidth,aHeight);
  }

  public void resetStatus()
  {
    theAspectRatioToolbar.resetStatus();
  }
}