/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
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

public class LineToolbar extends JPanel
{
  private LineAngleLimitable theTool;
  private JTextField         theLengthField;
  private JTextField         theAngleField;
  private JLabel             theLengthLabel;
  private JLabel             theAngleLabel;

  public LineToolbar()
  { 
    DocumentListener listener = new DocumentListener()
    {
      public void changedUpdate( DocumentEvent e){limit();}
      public void insertUpdate( DocumentEvent e) {limit();}
      public void removeUpdate( DocumentEvent e) {limit();}
    };

    theLengthField = new JTextField("",7);
    theLengthField.getDocument().addDocumentListener(listener);
    theAngleField  = new JTextField("",7);
    theAngleField.getDocument().addDocumentListener(listener);
    theLengthLabel = new JLabel("Lengths:");
    theAngleLabel  = new JLabel("Angles:");

    GridBagLayout gridBagLayout = new GridBagLayout();
    this.setLayout(gridBagLayout);

    GridBagConstraints gbc;

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 0;
    gbc.weighty = 0;
    gbc.insets = new Insets(2,2,2,2);
    gbc.anchor = GridBagConstraints.LINE_START;
    gridBagLayout.setConstraints(theLengthLabel, gbc);
    this.add(theLengthLabel);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = 0;
    gbc.weighty = 0;
    gbc.insets = new Insets(2,2,2,2);
    gbc.anchor = GridBagConstraints.LINE_START;
    gridBagLayout.setConstraints(theAngleLabel, gbc);
    this.add(theAngleLabel);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.insets = new Insets(2,2,2,2);
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theLengthField, gbc);
    this.add(theLengthField);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.insets = new Insets(2,2,2,2);
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theAngleField, gbc);
    this.add(theAngleField);
  }

  public void setTool( LineAngleLimitable aTool )
  {
    theTool = aTool;
    limit();
  }

  private void limit()
  {
    if( theTool == null )
      return;
    try
    {
      String[] lengthStrings = theLengthField.getText().split(",");
      double[] lengths = new double[lengthStrings.length];

      for( int i = 0 ; i < lengthStrings.length ; i++ )
        lengths[i] = Double.parseDouble(lengthStrings[i]);

      theTool.setLengths(lengths);
    }
    catch( NumberFormatException nfe )
    {
      theTool.setLengths(null);
    }

    try
    {
      String[] angleStrings = theAngleField.getText().split(",");
      double[] angles  = new double[angleStrings.length];

      for( int i = 0 ; i < angleStrings.length ; i++ )
        angles[i] = Double.parseDouble(angleStrings[i]);

      theTool.setAngles(angles);
    }
    catch( NumberFormatException nfe )
    {
      theTool.setAngles(null);
    }
  }
}