/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.geom.GeneralPath;

public class ManipulateToolbar extends JPanel
{
  private JCheckBox theMirrorBox;
  private JCheckBox theBigBox;
  private JCheckBox theMidBox;
  private JCheckBox theSmallBox;

  private JRadioButton theWindEvenOdd;
  private JRadioButton theWindNonZero;

  private ManipulateTool theTool;

  public ManipulateToolbar()
  {
    theMirrorBox = new JCheckBox("Miror");
    theBigBox    = new JCheckBox("Linked Manipulators");
    theMidBox    = new JCheckBox("Free Manipulators");
    theSmallBox  = new JCheckBox("Points");

    theMirrorBox.setSelected(false);
    theBigBox.setSelected(true);
    theMidBox.setSelected(true);
    theSmallBox.setSelected(true);

    theWindEvenOdd = new JRadioButton("Even-Odd");
    theWindNonZero = new JRadioButton("Non-Zero");
    ButtonGroup group = new ButtonGroup();
    group.add(theWindEvenOdd);
    group.add(theWindNonZero);

    ActionListener radioListener = new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        setWindRule();
      }
    };
    theWindEvenOdd.addActionListener(radioListener);
    theWindNonZero.addActionListener(radioListener);

    ActionListener listener = new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        setMirrorType();
      }
    };
    theMirrorBox.addActionListener(listener);

    ActionListener manipulatorsListener = new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        if( !theSmallBox.isSelected() && !theMidBox.isSelected() && !theBigBox.isSelected() )
          ((JCheckBox)e.getSource()).setSelected(true);
        setManipulators();

        theMirrorBox.setEnabled(theBigBox.isSelected());
      }
    };
    theSmallBox.addActionListener(manipulatorsListener);
    theMidBox.addActionListener(manipulatorsListener);
    theBigBox.addActionListener(manipulatorsListener);

    GridBagLayout gridBagLayout = new GridBagLayout();
    this.setLayout(gridBagLayout);

    GridBagConstraints gbc;

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 0;
    gbc.weighty = 1;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theWindNonZero, gbc);
    this.add(theWindNonZero);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theWindEvenOdd, gbc);
    this.add(theWindEvenOdd);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = 0;
    gbc.weighty = 1;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.LAST_LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theSmallBox, gbc);
    this.add(theSmallBox);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.weightx = 0;
    gbc.weighty = 0;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theMidBox, gbc);
    this.add(theMidBox);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.weightx = 0;
    gbc.weighty = 0;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theBigBox, gbc);
    this.add(theBigBox);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theMirrorBox, gbc);
    this.add(theMirrorBox);
  }

  public void setWindRule()
  {
    if( theTool == null )
      return;

    if( theWindEvenOdd.isSelected() )
      theTool.setWindRule(GeneralPath.WIND_EVEN_ODD);
    else if( theWindNonZero.isSelected() )
      theTool.setWindRule(GeneralPath.WIND_NON_ZERO);
  }

  public void setWindRule( int aRule )
  {
    if( aRule == GeneralPath.WIND_EVEN_ODD )
      theWindEvenOdd.setSelected(true);
    else if( aRule == GeneralPath.WIND_NON_ZERO )
      theWindNonZero.setSelected(true);
  }

  public void setMirrorType()
  {
    if( theTool == null )
      return;

    theTool.setMirrored(theMirrorBox.isSelected());
  } 

  public void setManipulators()
  {
    if( theTool == null )
      return;

    theTool.setManipulators(theSmallBox.isSelected(),theMidBox.isSelected(),theBigBox.isSelected());
  } 

  public void setTool( ManipulateTool aTool )
  {
    theTool = aTool;
    setMirrorType();
    setManipulators();
  }

  public void resetWindRule()
  {
    theWindNonZero.setSelected(false);
    theWindEvenOdd.setSelected(false);
  }
}