/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public abstract class EffectChooser extends JDialog
{
  private OOPS theOOPS;

  private JPanel theContentPanel;

  private JButton theOKButton;
  private JButton theCancelButton;

  private boolean theShouldEffect = false;

  public EffectChooser(String aTitle, OOPS anOOPS)
  {
    super(anOOPS,aTitle,true);

    theOOPS = anOOPS; 

    this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    final JDialog thisDialog = this;
    this.addWindowListener
    (
      new WindowAdapter()
      {
        public void windowClosing( WindowEvent e )
        {
          theShouldEffect = false;
          thisDialog.setVisible(false);
        }
      }
    );
    theContentPanel = new JPanel();
    theOKButton     = new JButton("OK");
    theCancelButton = new JButton("Cancel");

    theOKButton.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          theShouldEffect = true;
          thisDialog.setVisible(false);
        }
      }
    );

    theCancelButton.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          theShouldEffect = false;
          thisDialog.setVisible(false);
        }
      }
    );
 
    GridBagLayout layout = new GridBagLayout();
    getContentPane().setLayout(layout);
 
    GridBagConstraints gbc;

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.BOTH;
    layout.setConstraints(theContentPanel, gbc);
    getContentPane().add(theContentPanel);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = .5f;
    gbc.weighty = .2f;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.CENTER;
    layout.setConstraints(theOKButton, gbc);
    getContentPane().add(theOKButton);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.weightx = .5f;
    gbc.weighty = .2f;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.CENTER;
    layout.setConstraints(theCancelButton, gbc);
    getContentPane().add(theCancelButton);

    this.pack();
  }

  public void setVisible( boolean shouldShow )
  {
    if( shouldShow )
    {
      theShouldEffect = true;
      setLocationRelativeTo(theOOPS);
    }
    super.setVisible(shouldShow);
  }

  public boolean shouldEffect()
  {
    return theShouldEffect;
  }
  
  public JPanel getContentPanel()
  {
    return theContentPanel;
  }
}