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

public class QuadCurveToolbar extends JPanel
{
  private QuadCurveTool theTool;
  private JTextField    theLengthField;
  private JTextField    theAngleField;
  private JLabel        theLengthLabel;
  private JLabel        theAngleLabel;

  private JTextField    theCurveLengthField;
  private JTextField    theCurveAngleField;
  private JLabel        theCurveLengthLabel;
  private JLabel        theCurveAngleLabel;

  private LineStatusToolbar theLineStatusToolbar;

  public QuadCurveToolbar()
  {
    theLineStatusToolbar = new LineStatusToolbar();

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

    theCurveLengthField = new JTextField("",7);
    theCurveLengthField.getDocument().addDocumentListener(listener);
    theCurveAngleField  = new JTextField("",7);
    theCurveAngleField.getDocument().addDocumentListener(listener);
    theCurveLengthLabel = new JLabel("Lengths:");
    theCurveAngleLabel  = new JLabel("Angles:");

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




    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.weightx = 0;
    gbc.weighty = 0;
    gbc.insets = new Insets(10,2,2,2);
    gbc.anchor = GridBagConstraints.LINE_START;
    gridBagLayout.setConstraints(theCurveLengthLabel, gbc);
    this.add(theCurveLengthLabel);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.weightx = 0;
    gbc.weighty = 0;
    gbc.insets = new Insets(2,2,2,2);
    gbc.anchor = GridBagConstraints.LINE_START;
    gridBagLayout.setConstraints(theCurveAngleLabel, gbc);
    this.add(theCurveAngleLabel);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.insets = new Insets(10,2,2,2);
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theCurveLengthField, gbc);
    this.add(theCurveLengthField);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.insets = new Insets(2,2,2,2);
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theCurveAngleField, gbc);
    this.add(theCurveAngleField);




    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.gridwidth = 2;
    gbc.insets = new Insets(0,1,0,1);
    gbc.anchor = GridBagConstraints.PAGE_END;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theLineStatusToolbar, gbc);
    this.add(theLineStatusToolbar);
  }

  public void setTool( QuadCurveTool aTool )
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

      theTool.setLineLengths(lengths);
    }
    catch( NumberFormatException nfe )
    {
      theTool.setLineLengths(null);
    }

    try
    {
      String[] angleStrings = theAngleField.getText().split(",");
      double[] angles  = new double[angleStrings.length];

      for( int i = 0 ; i < angleStrings.length ; i++ )
        angles[i] = Double.parseDouble(angleStrings[i]);

      theTool.setLineAngles(angles);
    }
    catch( NumberFormatException nfe )
    {
      theTool.setLineAngles(null);
    }



    try
    {
      String[] lengthStrings = theCurveLengthField.getText().split(",");
      double[] lengths = new double[lengthStrings.length];

      for( int i = 0 ; i < lengthStrings.length ; i++ )
        lengths[i] = Double.parseDouble(lengthStrings[i]);

      theTool.setCurveLengths(lengths);
    }
    catch( NumberFormatException nfe )
    {
      theTool.setCurveLengths(null);
    }

    try
    {
      String[] angleStrings = theCurveAngleField.getText().split(",");
      double[] angles  = new double[angleStrings.length];

      for( int i = 0 ; i < angleStrings.length ; i++ )
        angles[i] = Double.parseDouble(angleStrings[i]);

      theTool.setCurveAngles(angles);
    }
    catch( NumberFormatException nfe )
    {
      theTool.setCurveAngles(null);
    }
  }

  public void setStatus( double aLength, double anAngle )
  {
    theLineStatusToolbar.setStatus(aLength,anAngle);
  }

  public void resetStatus()
  {
    theLineStatusToolbar.resetStatus();
  }
}