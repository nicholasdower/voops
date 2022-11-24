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

public class SmoothPolygonToolbar extends JPanel
{
  private SmoothPolygonTool theTool;
  private JCheckBox         theCloseButton;

  private static SmoothCurveToolbar theSmoothCurveToolbar = new SmoothCurveToolbar();

  public SmoothPolygonToolbar()
  {
    theCloseButton = new JCheckBox("Close",true);
    theCloseButton.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          if( theTool == null )
            return;
          theTool.setShouldClose(theCloseButton.isSelected());
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
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theCloseButton, gbc);
    this.add(theCloseButton);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.fill = GridBagConstraints.BOTH;
    gridBagLayout.setConstraints(theSmoothCurveToolbar, gbc);
    this.add(theSmoothCurveToolbar);
  }

  public void setTool( SmoothPolygonTool aTool )
  {
    theTool = aTool;
    theSmoothCurveToolbar.setTool( aTool);
  }
}