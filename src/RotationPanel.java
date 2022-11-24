/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.Vector;

public class RotationPanel extends JPanel
{
  private RotatePanel    theRotatePanel;
  private JTextField     theField;
  private OOPSButton     theGoButton;

  private Selectable     theSelectable;

  private static DecimalFormat theFormat = new DecimalFormat("0.00");

  public RotationPanel(Selectable aSelectable )
  {
    theSelectable = aSelectable;

    theRotatePanel = new RotatePanel();

    final JLabel rotationLabel = new JLabel("0" + '\u00B0');
    rotationLabel.setOpaque(false);
    rotationLabel.setBorder(null);
    rotationLabel.setForeground(Color.red);
    rotationLabel.setFont(new Font("Times",Font.BOLD,16));

    final JLabel deltaLabel = new JLabel("0" + '\u00B0');
    deltaLabel.setOpaque(false);
    deltaLabel.setBorder(null);
    deltaLabel.setForeground(new Color(100,100,255));
    deltaLabel.setFont(new Font("Times",Font.BOLD,16));

    final JLabel deltaTitleLabel = new JLabel("   " + '\u0394');
    deltaTitleLabel.setHorizontalAlignment(JLabel.RIGHT);
    deltaTitleLabel.setOpaque(false);
    deltaTitleLabel.setBorder(null);
    deltaTitleLabel.setForeground(new Color(100,100,255));
    deltaTitleLabel.setFont(new Font("Times",Font.BOLD,18));


    ActionListener actionListener = new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        try
        {
          theRotatePanel.setRotation(Math.toRadians(Double.parseDouble(theField.getText())%360));
        }
        catch( NumberFormatException nfe )
        {

        }
      }
    };

    KeyAdapter keyListener = new KeyAdapter()
    {
      public void keyReleased( KeyEvent e )
      {
        if( e.getKeyCode() == KeyEvent.VK_ENTER )
        {
          try
          {
            theRotatePanel.setRotation(Math.toRadians(Double.parseDouble(theField.getText())%360));
          }
          catch( NumberFormatException nfe )
          {

          }
        }
      }
    };

    theGoButton = new OOPSButton("GO.png","Go");
    theGoButton.addActionListener(actionListener);

    theRotatePanel.addChangeListener
    (
      new ChangeListener()
      {
        public void stateChanged( ChangeEvent e )
        {
          if( !((RotatePanel)e.getSource()).isResizing() )
            theSelectable.resetRotation();
          else
            theSelectable.rotate(theRotatePanel.getLastRotation());

          rotationLabel.setText(theFormat.format(Math.toDegrees(theRotatePanel.getDirection())) + '\u00B0');
          deltaLabel.setText(theFormat.format(theRotatePanel.getDelta()) + '\u00B0');
        }
      }
    );

    theField = new JTextField("0");
    theField.setHorizontalAlignment(JTextField.RIGHT);
    theField.addKeyListener(keyListener);



    JPanel rightPanel = new JPanel();
    GridBagLayout gridBagLayout = new GridBagLayout();
    rightPanel.setLayout(gridBagLayout);

    GridBagConstraints gbc;

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.LINE_END;
    gridBagLayout.setConstraints(rotationLabel, gbc);
    rightPanel.add(rotationLabel);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = 0;
    gbc.weighty = 1;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.LINE_END;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(deltaTitleLabel, gbc);
    rightPanel.add(deltaTitleLabel);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.LINE_END;
    gridBagLayout.setConstraints(deltaLabel, gbc);
    rightPanel.add(deltaLabel);

    JPanel entryPanel = new JPanel(new BorderLayout());
    entryPanel.add(theField,BorderLayout.CENTER);
    entryPanel.add(theGoButton,BorderLayout.LINE_END);
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 2;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.LINE_END;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(entryPanel, gbc);
    rightPanel.add(entryPanel);


    this.setLayout(new GridLayout(1,0));
    this.add(theRotatePanel);
    this.add(rightPanel);
  }

}