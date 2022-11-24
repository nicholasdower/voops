/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

public class FillToolbar extends JPanel
{
  private JCheckBox theFillBox;
  private JCheckBox theStrokeBox;
  private JCheckBox theStrokeColorBox;

  private FillTool theTool;

  public FillToolbar()
  {
    theFillBox        = new JCheckBox("Fill Color");
    theStrokeBox      = new JCheckBox("Stroke Style");
    theStrokeColorBox = new JCheckBox("Stroke Color");

    theFillBox.setSelected(true);
    theStrokeBox.setSelected(false);
    theStrokeColorBox.setSelected(false);

    ActionListener listener = new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        setFillType();
      }
    };
    theFillBox.addActionListener(listener);
    theStrokeBox.addActionListener(listener);
    theStrokeColorBox.addActionListener(listener);


    GridBagLayout gridBagLayout = new GridBagLayout();
    this.setLayout(gridBagLayout);

    GridBagConstraints gbc;

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theFillBox, gbc);
    this.add(theFillBox);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theStrokeColorBox, gbc);
    this.add(theStrokeColorBox);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theStrokeBox, gbc);
    this.add(theStrokeBox);
  }

  public void setFillType()
  {
    if( theTool == null )
      return;

    theTool.setFillType(theFillBox.isSelected(),theStrokeBox.isSelected(),theStrokeColorBox.isSelected());
  }

  public void setTool( FillTool aTool )
  {
    theTool = aTool;
    setFillType();
  }
}