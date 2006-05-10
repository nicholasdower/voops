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

public class AspectRatioToolbar extends JPanel
{
  private AspectRatioable   theTool;
  private JCheckBox         thePreserveButton;
  private JTextField        theWRatioField;
  private JTextField        theHRatioField;
  private JLabel            theColon;

  private JTextField        theXField;
  private JTextField        theYField;
  private JTextField        theWidthField;
  private JTextField        theHeightField;
  private JLabel            theXLabel;
  private JLabel            theYLabel;
  private JLabel            theWidthLabel;
  private JLabel            theHeightLabel;
  private OOPSButton        theAddButton;

  private AspectRatioStatusToolbar theStatus;

  public AspectRatioToolbar()
  {
    theStatus = new AspectRatioStatusToolbar();
    
    DocumentListener ratioListener = new DocumentListener()
    {
      public void changedUpdate( DocumentEvent e){updateRatio();}
      public void insertUpdate( DocumentEvent e) {updateRatio();}
      public void removeUpdate( DocumentEvent e) {updateRatio();}
    };

    theWRatioField  = new JTextField("1",4);
    theWRatioField.setEnabled(false);
    theWRatioField.getDocument().addDocumentListener(ratioListener);
    theHRatioField = new JTextField("1",4);
    theHRatioField.setEnabled(false);
    theHRatioField.getDocument().addDocumentListener(ratioListener);

    theColon = new JLabel(":");

    thePreserveButton = new JCheckBox("Preserve Ratio",false);
    thePreserveButton.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          if( theTool == null )
            return;
          if( !thePreserveButton.isSelected() )
          {
            theTool.shouldPreserveRatio(false);
            theWRatioField.setEnabled(false);
            theHRatioField.setEnabled(false);
            theColon.setEnabled(false);
          }
          else
          {
            updateRatio();
            theWRatioField.setEnabled(true);
            theHRatioField.setEnabled(true);
            theColon.setEnabled(true);
          }
        }
      }
    );

    
    JPanel ratioPanel = new JPanel();
    ratioPanel.setLayout(new BoxLayout(ratioPanel,BoxLayout.X_AXIS));
    ratioPanel.add(thePreserveButton);
    ratioPanel.add(theWRatioField);
    ratioPanel.add(theColon);
    ratioPanel.add(theHRatioField);


    theXField       = new JTextField("0",2);
    theYField       = new JTextField("0",2);
    theWidthField   = new JTextField("10",2);
    theHeightField  = new JTextField("10",2);
    theXLabel       = new JLabel("X:");
    theYLabel       = new JLabel("Y:");
    theWidthLabel   = new JLabel("W:");
    theHeightLabel  = new JLabel("H:");
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
              Double.parseDouble(theXField.getText()),
              Double.parseDouble(theYField.getText()),
              Double.parseDouble(theWidthField.getText()),
              Double.parseDouble(theHeightField.getText())
            );
          }
          catch( NumberFormatException nfe )
          {

          }
        }
      }
    );

    JPanel addPanel = new JPanel();
    addPanel.setLayout(new BoxLayout(addPanel,BoxLayout.X_AXIS));
    addPanel.add(theXLabel);
    addPanel.add(theXField);
    addPanel.add(theYLabel);
    addPanel.add(theYField);
    addPanel.add(theWidthLabel);
    addPanel.add(theWidthField);
    addPanel.add(theHeightLabel);
    addPanel.add(theHeightField);
    addPanel.add(theAddButton);

    GridBagLayout gridBagLayout = new GridBagLayout();
    this.setLayout(gridBagLayout);

    GridBagConstraints gbc;

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.insets = new Insets(2,2,2,2);
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(ratioPanel, gbc);
    this.add(ratioPanel);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(2,2,2,2);
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(addPanel, gbc);
    this.add(addPanel);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.insets = new Insets(2,2,2,2);
    gbc.anchor = GridBagConstraints.PAGE_END;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theStatus, gbc);
    this.add(theStatus);
  }

  private void updateRatio()
  {
    if( theTool == null )
      return;
    theTool.shouldPreserveRatio(true);
    try
    {
      double width  = Double.parseDouble(theWRatioField.getText());
      double height = Double.parseDouble(theHRatioField.getText());
      if( height <= 0 || width <= 0 )
      {
        theTool.shouldPreserveRatio(false);
        return;
      }

      theTool.setAspectRatio(width/height);
    }
    catch( NumberFormatException nfe )
    {
      theTool.shouldPreserveRatio(false);
    }
  }

  public void setTool(AspectRatioable aTool)
  {
    theTool = aTool;

    if( thePreserveButton.isSelected() )
      updateRatio();
  }

  public void setStatus( double aWidth, double aHeight )
  {
    theStatus.setStatus(aWidth,aHeight);
  }

  public void resetStatus()
  {
    theStatus.resetStatus();
  }
}