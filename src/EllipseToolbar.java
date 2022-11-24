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
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.geom.Arc2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

public class EllipseToolbar extends JPanel
{
  private static AspectRatioToolbar theAspectRatioToolbar = new AspectRatioToolbar();
  private EllipseTool theTool;
  private JTextField         theStartField;
  private JTextField         theAngleField;
  private JLabel             theStartLabel;
  private JLabel             theAngleLabel;

  private JRadioButton thePieButton;
  private JRadioButton theChordButton;
  private JRadioButton theOpenButton;

  public EllipseToolbar()
  {
    theAspectRatioToolbar = new AspectRatioToolbar();

    DocumentListener angleListener = new DocumentListener()
    {
      public void changedUpdate( DocumentEvent e){updateAngle();}
      public void insertUpdate( DocumentEvent e) {updateAngle();}
      public void removeUpdate( DocumentEvent e) {updateAngle();}
    };

    theStartField   = new JTextField("0",2);
    theStartField.getDocument().addDocumentListener(angleListener);
    theAngleField  = new JTextField("360",2);
    theAngleField.getDocument().addDocumentListener(angleListener);
    theStartLabel   = new JLabel("S:");
    theAngleLabel  = new JLabel('\u03B8' + ":");

    JPanel anglePanel = new JPanel();
    anglePanel.setLayout(new BoxLayout(anglePanel,BoxLayout.X_AXIS));
    anglePanel.add(theStartLabel);
    anglePanel.add(theStartField);
    anglePanel.add(theAngleLabel);
    anglePanel.add(theAngleField);


    ActionListener typeListener = new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        updateAngle();
      }
    };

    thePieButton   = new JRadioButton();
    thePieButton.setSelected(true);
    theChordButton = new JRadioButton();
    theOpenButton  = new JRadioButton();

    ButtonGroup group = new ButtonGroup();
    group.add(thePieButton);
    group.add(theChordButton);
    group.add(theOpenButton);

    thePieButton.addActionListener(typeListener);
    theChordButton.addActionListener(typeListener);
    theOpenButton.addActionListener(typeListener);

    JPanel typePanel = new JPanel();
    typePanel.setLayout(new BoxLayout(typePanel,BoxLayout.X_AXIS));
    typePanel.add(thePieButton);
    typePanel.add(new JLabel(new ImageIcon(this.getClass().getClassLoader().getResource("Arc_PIE.png"))));
    typePanel.add(theChordButton);
    typePanel.add(new JLabel(new ImageIcon(this.getClass().getClassLoader().getResource("Arc_CHORD.png"))));
    typePanel.add(theOpenButton);
    typePanel.add(new JLabel(new ImageIcon(this.getClass().getClassLoader().getResource("Arc_OPEN.png"))));

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
    gridBagLayout.setConstraints(typePanel, gbc);
    this.add(typePanel);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.insets = new Insets(2,2,0,2);
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(anglePanel, gbc);
    this.add(anglePanel);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 2;
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
      double start = Double.parseDouble(theStartField.getText());
      double angle = Double.parseDouble(theAngleField.getText());


      theTool.setAngle(start,angle);
    }
    catch( NumberFormatException nfe )
    {
    }
    if( theChordButton.isSelected() )
      theTool.setArcType(Arc2D.CHORD);
    else if( theOpenButton.isSelected() )
      theTool.setArcType(Arc2D.OPEN);
    else if( thePieButton.isSelected() )
      theTool.setArcType(Arc2D.PIE);
  }

  public void setTool(EllipseTool aTool)
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