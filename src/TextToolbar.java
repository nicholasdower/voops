/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Insets;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Font;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import java.util.Vector;
import javax.swing.text.AbstractDocument.DefaultDocumentEvent;
import javax.swing.event.DocumentEvent.EventType;

public class TextToolbar extends JPanel
{
  private JLabel theTextSizeLabel;
  private JLabel theTextSpaceLabel;

  private SliderSpinnerCombo theTextSizeSlider;
  private SliderSpinnerCombo theTextSpaceSlider;

  private FontComboBox theFontBox;
  private JCheckBox    theBoldBox;
  private JCheckBox    theItalicBox;

  private JTextArea theText;

  private static final float theTextFontSize = 12;

  private Vector theListeners;

  public TextToolbar()
  {
    theListeners = new Vector();

    theTextSizeSlider = new SliderSpinnerCombo(100,50000,3000,"Size");
    theTextSpaceSlider = new SliderSpinnerCombo(0,10000,600,"Spacing");

    theTextSizeSlider.addChangeListener
    (
      new ChangeListener()
      {
        public void stateChanged(ChangeEvent e)
        {
          updateListeners();
        }
      }
    );

    theTextSpaceSlider.addChangeListener
    (
      new ChangeListener()
      {
        public void stateChanged(ChangeEvent e)
        {
          updateListeners();
        }
      }
    );
    theText = new JTextArea();
    theText.setLineWrap(true);

    theFontBox = new FontComboBox(true);
    theFontBox.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
    //theFontBox.setPreferredSize(new Dimension(50,30));
    theFontBox.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          theText.setFont(getFont().deriveFont(theTextFontSize));
          theText.revalidate();
          updateListeners();
        }
      }
    );

    theBoldBox = new JCheckBox("Bold");
    theBoldBox.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          theText.setFont(getFont().deriveFont(theTextFontSize));
          theText.revalidate();
          updateListeners();
        }
      }
    );

    theItalicBox = new JCheckBox("Italic");
    theItalicBox.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          theText.setFont(getFont().deriveFont(theTextFontSize));
          theText.revalidate();
          updateListeners();
        }
      }
    );

    JPanel sliderPanel = new JPanel();
           sliderPanel.setLayout(new GridLayout(0,1));
    sliderPanel.add(theTextSizeSlider);
    sliderPanel.add(theTextSpaceSlider);

    JPanel leftTopPanel = new JPanel();
           leftTopPanel.setLayout(new BoxLayout(leftTopPanel,BoxLayout.X_AXIS));
    leftTopPanel.add(sliderPanel);

    JPanel stylePanel = new JPanel(new GridLayout(1,0));
           stylePanel.add(theBoldBox);
           stylePanel.add(theItalicBox);
    JPanel leftPanel = new JPanel();
           leftPanel.setLayout(new GridLayout(0,1));
    leftPanel.setBorder(BorderFactory.createEmptyBorder(5,5,0,5));
    leftPanel.add(leftTopPanel);
    leftPanel.add(theFontBox);
    leftPanel.add(stylePanel);

    JScrollPane pane = new JScrollPane(theText);
    pane.setBorder(BorderFactory.createLineBorder(Color.black));

    GridBagLayout gridBagLayout = new GridBagLayout();
    this.setLayout(gridBagLayout);
    GridBagConstraints gbc;

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weighty = 1;
    gbc.weightx = 0;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.BOTH;
    gridBagLayout.setConstraints(leftPanel, gbc);
    this.add(leftPanel);
    leftPanel.setPreferredSize(new Dimension(170,120));
    leftPanel.setMaximumSize(new Dimension(170,120));
    leftPanel.setMinimumSize(new Dimension(170,120));

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.weighty = 1;
    gbc.weightx = 1;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.BOTH;
    gridBagLayout.setConstraints(pane, gbc);
    this.add(pane);
    pane.setPreferredSize(new Dimension(70,100));
    pane.setMinimumSize(new Dimension(70,100));
    pane.setMaximumSize(new Dimension(70,100));

    theText.setFont(getFont().deriveFont(theTextFontSize));
    theText.revalidate();
  }

  public Font getFont()
  {
    if( theBoldBox == null || theItalicBox == null || theFontBox == null )
      return null;
    Font toReturn = theFontBox.getFont().deriveFont(getFontSize());

    if( theBoldBox.isSelected() && theItalicBox.isSelected())
    {
      toReturn = toReturn.deriveFont(Font.BOLD+Font.ITALIC);
    }
    else if( theItalicBox.isSelected() )
    {
      toReturn = toReturn.deriveFont(Font.ITALIC);
    }
    else if( theBoldBox.isSelected() )
    {
      toReturn = toReturn.deriveFont(Font.BOLD);
    }
    else
    {
      toReturn = toReturn.deriveFont(Font.PLAIN);
    }

    return toReturn;
  }

  public String getText()
  {
    return theText.getText();
  }

  public void addDocumentListener( DocumentListener aListener )
  {
    theText.getDocument().addDocumentListener(aListener);
    theListeners.add(aListener);
  }

  public void removeDocumentListener( DocumentListener aListener )
  {
    theText.getDocument().removeDocumentListener(aListener);
    theListeners.remove(aListener);
  }

  public void updateListeners()
  {
    for( int i = 0 ; i < theListeners.size() ; i++ )
      ((DocumentListener)theListeners.get(i)).changedUpdate(null);
  }

  public float getFontSize()
  {
    return ((float)theTextSizeSlider.getValue())/100;
  }


  public double getFontSpace()
  {
    return ((double)theTextSpaceSlider.getValue())/100;
  }
}