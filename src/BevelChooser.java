/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import javax.swing.BoxLayout;
import java.awt.Insets;
import java.awt.GridBagConstraints;

public class BevelChooser extends EffectChooser
{
  private JLabel theLightDirectionLabel;

  private JLabel theLightSizeLabel;
  private JLabel theLightStepsLabel;
  private JLabel theLightAlphaLabel;

  private JLabel theDarkSizeLabel;
  private JLabel theDarkStepsLabel;
  private JLabel theDarkAlphaLabel;

  private JCheckBox theDarkBox;
  private JCheckBox theLightBox;

  private JTextField theLightDirectionField;

  private JTextField theLightSizeField;
  private JTextField theLightStepsField;
  private JTextField theLightAlphaField;

  private JTextField theDarkSizeField;
  private JTextField theDarkStepsField;
  private JTextField theDarkAlphaField;

  public BevelChooser(OOPS anOOPS)
  {
    super("Bevel", anOOPS);

    theLightDirectionLabel = new JLabel("Light Direction:");

    theLightSizeLabel           = new JLabel("Size:");
    theLightStepsLabel          = new JLabel("Steps:");
    theLightAlphaLabel          = new JLabel("Alpha:");

    theDarkSizeLabel           = new JLabel("Size:");
    theDarkStepsLabel          = new JLabel("Steps:");
    theDarkAlphaLabel          = new JLabel("Alpha:");

    theDarkBox  = new JCheckBox("Dark",true);
    theLightBox = new JCheckBox("Light",true);

    ActionListener lightDarkListener = new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        if( !theLightBox.isSelected() && !theDarkBox.isSelected() )
          ((JCheckBox)e.getSource()).setSelected(true);

        if( !theLightBox.isSelected() )
        {
          theLightSizeField.setEnabled(false);
          theLightStepsField.setEnabled(false);
          theLightAlphaField.setEnabled(false);
        }
        else
        {
          theLightSizeField.setEnabled(true);
          theLightStepsField.setEnabled(true);
          theLightAlphaField.setEnabled(true);
        }

        if( !theDarkBox.isSelected() )
        {
          theDarkSizeField.setEnabled(false);
          theDarkStepsField.setEnabled(false);
          theDarkAlphaField.setEnabled(false);
        }
        else 
        {
          theDarkSizeField.setEnabled(true);
          theDarkStepsField.setEnabled(true);
          theDarkAlphaField.setEnabled(true);
        }
      }
    };
    theDarkBox.addActionListener(lightDarkListener);
    theLightBox.addActionListener(lightDarkListener);

    theLightDirectionField = new JTextField("315",5);

    theLightSizeField           = new JTextField("10",5);
    theLightStepsField          = new JTextField("20",5);
    theLightAlphaField          = new JTextField("10",5);

    theDarkSizeField           = new JTextField("10",5);
    theDarkStepsField          = new JTextField("20",5);
    theDarkAlphaField          = new JTextField("10",5);

    GridBagLayout layout = new GridBagLayout();
    getContentPanel().setLayout(layout);
 
    GridBagConstraints gbc;

    JPanel holder = new JPanel();
    holder.setLayout(new BoxLayout(holder,BoxLayout.X_AXIS));
    holder.add(theLightDirectionLabel);
    holder.add(theLightDirectionField);
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 4;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(1,5,1,1);
    gbc.anchor = GridBagConstraints.LINE_START;
    layout.setConstraints(holder, gbc);
    getContentPanel().add(holder);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 2;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    layout.setConstraints(theDarkBox, gbc);
    getContentPanel().add(theDarkBox);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.insets = new Insets(1,5,1,1);
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    layout.setConstraints(theDarkSizeLabel, gbc);
    getContentPanel().add(theDarkSizeLabel);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.insets = new Insets(1,5,1,1);
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    layout.setConstraints(theDarkStepsLabel, gbc);
    getContentPanel().add(theDarkStepsLabel);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(1,5,1,1);
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    layout.setConstraints(theDarkAlphaLabel, gbc);
    getContentPanel().add(theDarkAlphaLabel);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.weightx = .5;
    gbc.weighty = 0;
    gbc.insets = new Insets(1,5,1,1);
    gbc.anchor = GridBagConstraints.FIRST_LINE_END;
    gbc.fill   = GridBagConstraints.HORIZONTAL;
    layout.setConstraints(theDarkSizeField, gbc);
    getContentPanel().add(theDarkSizeField);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.weightx = .5;
    gbc.weighty = 0;
    gbc.insets = new Insets(1,5,1,1);
    gbc.anchor = GridBagConstraints.FIRST_LINE_END;
    gbc.fill   = GridBagConstraints.HORIZONTAL;
    layout.setConstraints(theDarkStepsField, gbc);
    getContentPanel().add(theDarkStepsField);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 4;
    gbc.weightx = .5;
    gbc.weighty = 1;
    gbc.insets = new Insets(1,5,1,1);
    gbc.anchor = GridBagConstraints.FIRST_LINE_END;
    gbc.fill   = GridBagConstraints.HORIZONTAL;
    layout.setConstraints(theDarkAlphaField, gbc);
    getContentPanel().add(theDarkAlphaField);






    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 1;
    gbc.gridwidth = 2;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    layout.setConstraints(theLightBox, gbc);
    getContentPanel().add(theLightBox);

    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 2;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.insets = new Insets(1,5,1,1);
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    layout.setConstraints(theLightSizeLabel, gbc);
    getContentPanel().add(theLightSizeLabel);

    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 3;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.insets = new Insets(1,5,1,1);
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    layout.setConstraints(theLightStepsLabel, gbc);
    getContentPanel().add(theLightStepsLabel);

    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 4;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(1,5,1,1);
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    layout.setConstraints(theLightAlphaLabel, gbc);
    getContentPanel().add(theLightAlphaLabel);

    gbc = new GridBagConstraints();
    gbc.gridx = 3;
    gbc.gridy = 2;
    gbc.weightx = .5;
    gbc.weighty = 0;
    gbc.insets = new Insets(1,5,1,1);
    gbc.anchor = GridBagConstraints.FIRST_LINE_END;
    gbc.fill   = GridBagConstraints.HORIZONTAL;
    layout.setConstraints(theLightSizeField, gbc);
    getContentPanel().add(theLightSizeField);

    gbc = new GridBagConstraints();
    gbc.gridx = 3;
    gbc.gridy = 3;
    gbc.weightx = .5;
    gbc.weighty = 0;
    gbc.insets = new Insets(1,5,1,1);
    gbc.anchor = GridBagConstraints.FIRST_LINE_END;
    gbc.fill   = GridBagConstraints.HORIZONTAL;
    layout.setConstraints(theLightStepsField, gbc);
    getContentPanel().add(theLightStepsField);

    gbc = new GridBagConstraints();
    gbc.gridx = 3;
    gbc.gridy = 4;
    gbc.weightx = .5;
    gbc.weighty = 1;
    gbc.insets = new Insets(1,5,1,1);
    gbc.anchor = GridBagConstraints.FIRST_LINE_END;
    gbc.fill   = GridBagConstraints.HORIZONTAL;
    layout.setConstraints(theLightAlphaField, gbc);
    getContentPanel().add(theLightAlphaField);

    pack();
  }

  public boolean showDark()
  {
    return theDarkBox.isSelected();
  }

  public boolean showLight()
  {
    return theLightBox.isSelected();
  }

  public double getDarkBevelSize()
  {
    try
    {
      return Integer.parseInt(theDarkSizeField.getText());
    }
    catch( NumberFormatException nfe ){}
    return 10;
  }

  public double getLightBevelSize()
  {
    try
    {
      return Integer.parseInt(theLightSizeField.getText());
    }
    catch( NumberFormatException nfe ){}
    return 10;
  }

  public int getLightSteps()
  {
    try
    {
      return Integer.parseInt(theLightStepsField.getText());
    }
    catch( NumberFormatException nfe ){}
    return 20;
  }

  public int getDarkSteps()
  {
    try
    {
      return Integer.parseInt(theDarkStepsField.getText());
    }
    catch( NumberFormatException nfe ){}
    return 20;
  }

  public double getAngle()
  {
    try
    {
      return Integer.parseInt(theLightDirectionField.getText());
    }
    catch( NumberFormatException nfe ){}
    return 315;
  }

  public int getLightAlpha()
  {
    try
    {
      return Integer.parseInt(theLightAlphaField.getText());
    }
    catch( NumberFormatException nfe ){}
    return 10;
  }

  public int getDarkAlpha()
  {
    try
    {
      return Integer.parseInt(theDarkAlphaField.getText());
    }
    catch( NumberFormatException nfe ){}
    return 10;
  }
}