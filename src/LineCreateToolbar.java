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

public class LineCreateToolbar extends JPanel
{
  private LineTool theTool;

  private JTextField        theX0Field;
  private JTextField        theY0Field;
  private JTextField        theX1Field;
  private JTextField        theY1Field;
  private JLabel            theX0Label;
  private JLabel            theY0Label;
  private JLabel            theX1Label;
  private JLabel            theY1Label;
  private OOPSButton        theAddButton;

  private JTextField        theRayX0Field;
  private JTextField        theRayY0Field;
  private JTextField        theRayDirectionField;
  private JTextField        theRayLengthField;
  private JLabel            theRayX0Label;
  private JLabel            theRayY0Label;
  private JLabel            theRayDirectionLabel;
  private JLabel            theRayLengthLabel;
  private OOPSButton        theRayAddButton;

  public LineCreateToolbar()
  { 
    theX0Field  = new JTextField("0",2);
    theY0Field  = new JTextField("0",2);
    theX1Field  = new JTextField("10",2);
    theY1Field  = new JTextField("10",2);
    theX0Label  = new JLabel("X0:");
    theY0Label  = new JLabel("Y0:");
    theX1Label  = new JLabel("X1:");
    theY1Label  = new JLabel("Y1:");
    theAddButton    = new OOPSButton("Add.png","Add");

    theAddButton.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          try
          {
            theTool.add
            (
              Double.parseDouble(theX0Field.getText()),
              Double.parseDouble(theY0Field.getText()),
              Double.parseDouble(theX1Field.getText()),
              Double.parseDouble(theY1Field.getText())
            );
          }
          catch( NumberFormatException nfe )
          {

          }
        }
      }
    );


    theRayX0Field         = new JTextField("0",2);
    theRayY0Field         = new JTextField("0",2);
    theRayDirectionField  = new JTextField("10",2);
    theRayLengthField     = new JTextField("10",2);
    theRayX0Label         = new JLabel("X0:");
    theRayY0Label         = new JLabel("Y0:");
    theRayDirectionLabel  = new JLabel("A:");
    theRayLengthLabel     = new JLabel("D:");
    theRayAddButton       = new OOPSButton("Add.png","Add");

    theRayAddButton.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          try
          {
            theTool.addRay
            (
              Double.parseDouble(theRayX0Field.getText()),
              Double.parseDouble(theRayY0Field.getText()),
              Double.parseDouble(theRayDirectionField.getText()),
              Double.parseDouble(theRayLengthField.getText())
            );
          }
          catch( NumberFormatException nfe )
          {

          }
        }
      }
    );


    GridBagLayout gridBagLayout = new GridBagLayout();
    this.setLayout(gridBagLayout);

    GridBagConstraints gbc;

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 0;
    gbc.weighty = 0;
    gbc.insets = new Insets(2,2,2,2);
    gbc.anchor = GridBagConstraints.LINE_END;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theX0Label, gbc);
    this.add(theX0Label);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.insets = new Insets(2,2,2,2);
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theX0Field, gbc);
    this.add(theX0Field);

    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 0;
    gbc.weightx = 0;
    gbc.weighty = 0;
    gbc.insets = new Insets(2,2,2,2);
    gbc.anchor = GridBagConstraints.LINE_END;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theY0Label, gbc);
    this.add(theY0Label);

    gbc = new GridBagConstraints();
    gbc.gridx = 3;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.insets = new Insets(2,2,2,2);
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theY0Field, gbc);
    this.add(theY0Field);

    gbc = new GridBagConstraints();
    gbc.gridx = 4;
    gbc.gridy = 0;
    gbc.weightx = 0;
    gbc.weighty = 0;
    gbc.insets = new Insets(2,2,2,2);
    gbc.anchor = GridBagConstraints.LINE_END;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theX1Label, gbc);
    this.add(theX1Label);

    gbc = new GridBagConstraints();
    gbc.gridx = 5;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.insets = new Insets(2,2,2,2);
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theX1Field, gbc);
    this.add(theX1Field);

    gbc = new GridBagConstraints();
    gbc.gridx = 6;
    gbc.gridy = 0;
    gbc.weightx = 0;
    gbc.weighty = 0;
    gbc.insets = new Insets(2,2,2,2);
    gbc.anchor = GridBagConstraints.LINE_END;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theY1Label, gbc);
    this.add(theY1Label);

    gbc = new GridBagConstraints();
    gbc.gridx = 7;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.insets = new Insets(2,2,2,2);
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theY1Field, gbc);
    this.add(theY1Field);

    gbc = new GridBagConstraints();
    gbc.gridx = 8;
    gbc.gridy = 0;
    gbc.weightx = 0;
    gbc.weighty = 0;
    gbc.insets = new Insets(2,2,2,2);
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theAddButton, gbc);
    this.add(theAddButton);


    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = 0;
    gbc.weighty = 0;
    gbc.insets = new Insets(2,2,2,2);
    gbc.anchor = GridBagConstraints.LINE_END;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theRayX0Label, gbc);
    this.add(theRayX0Label);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.insets = new Insets(2,2,2,2);
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theRayX0Field, gbc);
    this.add(theRayX0Field);

    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 1;
    gbc.weightx = 0;
    gbc.weighty = 0;
    gbc.insets = new Insets(2,2,2,2);
    gbc.anchor = GridBagConstraints.LINE_END;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theRayY0Label, gbc);
    this.add(theRayY0Label);

    gbc = new GridBagConstraints();
    gbc.gridx = 3;
    gbc.gridy = 1;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.insets = new Insets(2,2,2,2);
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theRayY0Field, gbc);
    this.add(theRayY0Field);

    gbc = new GridBagConstraints();
    gbc.gridx = 4;
    gbc.gridy = 1;
    gbc.weightx = 0;
    gbc.weighty = 0;
    gbc.insets = new Insets(2,2,2,2);
    gbc.anchor = GridBagConstraints.LINE_END;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theRayDirectionLabel, gbc);
    this.add(theRayDirectionLabel);

    gbc = new GridBagConstraints();
    gbc.gridx = 5;
    gbc.gridy = 1;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.insets = new Insets(2,2,2,2);
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theRayDirectionField, gbc);
    this.add(theRayDirectionField);

    gbc = new GridBagConstraints();
    gbc.gridx = 6;
    gbc.gridy = 1;
    gbc.weightx = 0;
    gbc.weighty = 0;
    gbc.insets = new Insets(2,2,2,2);
    gbc.anchor = GridBagConstraints.LINE_END;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theRayLengthLabel, gbc);
    this.add(theRayLengthLabel);

    gbc = new GridBagConstraints();
    gbc.gridx = 7;
    gbc.gridy = 1;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.insets = new Insets(2,2,2,2);
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theRayLengthField, gbc);
    this.add(theRayLengthField);

    gbc = new GridBagConstraints();
    gbc.gridx = 8;
    gbc.gridy = 1;
    gbc.weightx = 0;
    gbc.weighty = 0;
    gbc.insets = new Insets(2,2,2,2);
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theRayAddButton, gbc);
    this.add(theRayAddButton);
  }

  public void setTool( LineTool aTool )
  {
    theTool = aTool;
  }
}