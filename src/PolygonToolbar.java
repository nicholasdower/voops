/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.BoxLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class PolygonToolbar extends JPanel
{
  private PolygonTool theTool;
  private static LineToolbar theLineToolbar = new LineToolbar();
  private JCheckBox   theCloseButton;

  private LineStatusToolbar theLineStatusToolbar;

  public PolygonToolbar()
  {
    theLineStatusToolbar = new LineStatusToolbar();

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
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theCloseButton, gbc);
    this.add(theCloseButton);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theLineToolbar, gbc);
    this.add(theLineToolbar);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.insets = new Insets(0,1,0,1);
    gbc.anchor = GridBagConstraints.PAGE_END;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theLineStatusToolbar, gbc);
    this.add(theLineStatusToolbar);
  }

  public void setTool( PolygonTool aTool )
  {
    theTool = aTool;
    theLineToolbar.setTool(aTool);
    theTool.setShouldClose(theCloseButton.isSelected());
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