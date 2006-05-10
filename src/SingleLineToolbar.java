/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class SingleLineToolbar extends JPanel
{
  private LineTool theTool;
  private static LineToolbar theLineToolbar = new LineToolbar();
  private static LineCreateToolbar theLineCreateToolbar = new LineCreateToolbar();
  private LineStatusToolbar theLineStatusToolbar;

  public SingleLineToolbar()
  {
    theLineStatusToolbar = new LineStatusToolbar();

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
    gridBagLayout.setConstraints(theLineToolbar, gbc);
    this.add(theLineToolbar);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theLineCreateToolbar, gbc);
    this.add(theLineCreateToolbar);

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

  public void setTool( LineTool aTool )
  {
    theTool = aTool;
    theLineToolbar.setTool(aTool);
    theLineCreateToolbar.setTool(aTool);
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