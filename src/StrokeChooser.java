/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JPanel;
import javax.swing.SpinnerListModel;
import javax.swing.ButtonGroup;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.text.Document;
import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.Vector;

public class StrokeChooser extends JPanel
{
  private Paint            thePaint;
  private BasicStroke      theStroke;
  private MiniColorChooser theChooser;
  private SolidChooser     theSolidChooser;

  private JSpinner theMiterLimitSpinner;
  private JLabel   theMiterLimitLabel;

  private SliderSpinnerCombo theWidthSliderSpinner;

  private JSpinner theOffsetSpinner;
  private JLabel   theOffsetLabel;

  private JTextField theDashField;
  private JLabel     theDashLabel;

  private JRadioButton[] theJoinButtons;
  private JRadioButton[] theEndButtons;

  private JLabel     theJoinLabel;
  private JLabel     theEndLabel;

  private StrokePreviewPanel thePreview;

  private Vector theChangeListeners;

  private boolean changeOccuring = false;

  public StrokeChooser( MiniColorChooser aChooser )
  {
    theChangeListeners = new Vector();

    theStroke = new BasicStroke(1f);
    ChangeListener listener = new ChangeListener()
    {
      public void stateChanged( ChangeEvent e )
      {
        updateStroke();
      }
    };

    theChooser = aChooser;
    theSolidChooser = new SolidChooser(theChooser, "Stroke Color:");
    theSolidChooser.setColor(Color.black);
    theSolidChooser.addChangeListener(listener);

    theMiterLimitLabel = new JLabel("Miter Limit:");

    Integer[] o = new Integer[179];
    for( int i = 1 ; i < 179 ; i++ )
    {
      o[i] = Integer.valueOf(i);
    }

    theMiterLimitSpinner = new JSpinner(new SpinnerListModel(o));
    theMiterLimitSpinner.setValue(o[10]);
    theMiterLimitSpinner.setFont(new Font("Times",Font.BOLD,12));
    theMiterLimitSpinner.addChangeListener(listener);

    theWidthSliderSpinner = new SliderSpinnerCombo(0,500,10,"W");
    theWidthSliderSpinner.addChangeListener(listener);


    theOffsetLabel = new JLabel("Offset:");

    Float[] f = new Float[300];
    for( int i = 0 ; i < 300 ; i++ )
    {
      f[i] = Float.valueOf(((float)i)/10f);
    }

    theOffsetSpinner = new JSpinner(new SpinnerListModel(f));
    theOffsetSpinner.setValue(f[0]);
    theOffsetSpinner.setFont(new Font("Times",Font.BOLD,12));
    theOffsetSpinner.addChangeListener(listener);

    theDashLabel = new JLabel("Dash:");
    theDashField = new JTextField("");

    theDashField.getDocument().addDocumentListener
    (
      new DocumentListener()
      {
        public void changedUpdate( DocumentEvent e ){updateStroke();}
        public void insertUpdate( DocumentEvent e ){updateStroke();}
        public void removeUpdate( DocumentEvent e ){updateStroke();}
      }
    );

    theJoinLabel = new JLabel("Join");
    theEndLabel = new JLabel("End");


    theJoinButtons = new JRadioButton[3];
    theEndButtons  = new JRadioButton[3];
    ButtonGroup joinGroup = new ButtonGroup();
    ButtonGroup endGroup  = new ButtonGroup();
    ImageIcon[] joinImages = new ImageIcon[]
    {
      new ImageIcon(this.getClass().getClassLoader().getResource("Miter_Join.png")),
      new ImageIcon(this.getClass().getClassLoader().getResource("Round_Join.png")),
      new ImageIcon(this.getClass().getClassLoader().getResource("Bevel_Join.png"))
    };
    ImageIcon[] endImages = new ImageIcon[]
    {
      new ImageIcon(this.getClass().getClassLoader().getResource("Butt_End.png")),
      new ImageIcon(this.getClass().getClassLoader().getResource("Butt_Round.png")),
      new ImageIcon(this.getClass().getClassLoader().getResource("Butt_Square.png"))
    };
    for( int i = 0 ; i < 3 ; i++ )
    {
      theJoinButtons[i] = new JRadioButton();
      theJoinButtons[i].addChangeListener(listener);
      joinGroup.add(theJoinButtons[i]);
      theEndButtons[i]  = new JRadioButton();
      theEndButtons[i].addChangeListener(listener);
      endGroup.add(theEndButtons[i]);
    }
    theJoinButtons[0].setSelected(true);
    theEndButtons[0].setSelected(true);

    thePreview = new StrokePreviewPanel(theStroke);

    GridBagLayout gridBagLayout = new GridBagLayout();
    this.setLayout(gridBagLayout);

    GridBagConstraints gbc;

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.weightx = .6f;
    gbc.weighty = .3222f;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.fill = GridBagConstraints.BOTH;
    gridBagLayout.setConstraints(theSolidChooser, gbc);
    this.add(theSolidChooser);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.weightx = .6f;
    gbc.weighty = .1666f;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theMiterLimitLabel, gbc);
    this.add(theMiterLimitLabel);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.weightx = 0;
    gbc.weighty = .1666f;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.LINE_END;
    gridBagLayout.setConstraints(theMiterLimitSpinner, gbc);
    this.add(theMiterLimitSpinner);
    theMiterLimitSpinner.setMinimumSize(new Dimension(50,20));
    theMiterLimitSpinner.setMaximumSize(new Dimension(50,20));
    theMiterLimitSpinner.setPreferredSize(new Dimension(50,20));

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.gridwidth = 2;
    gbc.weightx = .6f;
    gbc.weighty = .1666f;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theWidthSliderSpinner, gbc);
    this.add(theWidthSliderSpinner);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = 0;
    gbc.weighty = .1666f;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theDashLabel, gbc);
    this.add(theDashLabel);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.weightx = .6;
    gbc.weighty = .1666f;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.LINE_END;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theDashField, gbc);
    this.add(theDashField);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.weightx = .6;
    gbc.weighty = .1666f;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theOffsetLabel, gbc);
    this.add(theOffsetLabel);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.weightx = 0;
    gbc.weighty = .1666f;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.LINE_END;
    gridBagLayout.setConstraints(theOffsetSpinner, gbc);
    this.add(theOffsetSpinner);
    theOffsetSpinner.setMinimumSize(new Dimension(50,20));
    theOffsetSpinner.setMaximumSize(new Dimension(50,20));
    theOffsetSpinner.setPreferredSize(new Dimension(50,20));

    JPanel topRightPanel = new JPanel(new GridLayout(0,2));
    topRightPanel.add(theJoinLabel);
    topRightPanel.add(theEndLabel);

    JPanel internalPanel;

    internalPanel = new JPanel(new BorderLayout());
    internalPanel.add(theJoinButtons[0],BorderLayout.LINE_START);
    internalPanel.add(new JLabel(joinImages[0]),BorderLayout.CENTER);
    topRightPanel.add(internalPanel);

    internalPanel = new JPanel(new BorderLayout());
    internalPanel.add(theEndButtons[0],BorderLayout.LINE_START);
    internalPanel.add(new JLabel(endImages[0]),BorderLayout.CENTER);
    topRightPanel.add(internalPanel);

    internalPanel = new JPanel(new BorderLayout());
    internalPanel.add(theJoinButtons[1],BorderLayout.LINE_START);
    internalPanel.add(new JLabel(joinImages[1]),BorderLayout.CENTER);
    topRightPanel.add(internalPanel);

    internalPanel = new JPanel(new BorderLayout());
    internalPanel.add(theEndButtons[1],BorderLayout.LINE_START);
    internalPanel.add(new JLabel(endImages[1]),BorderLayout.CENTER);
    topRightPanel.add(internalPanel);

    internalPanel = new JPanel(new BorderLayout());
    internalPanel.add(theJoinButtons[2],BorderLayout.LINE_START);
    internalPanel.add(new JLabel(joinImages[2]),BorderLayout.CENTER);
    topRightPanel.add(internalPanel);

    internalPanel = new JPanel(new BorderLayout());
    internalPanel.add(theEndButtons[2],BorderLayout.LINE_START);
    internalPanel.add(new JLabel(endImages[2]),BorderLayout.CENTER);
    topRightPanel.add(internalPanel);

    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 0;
    gbc.gridheight = 2;
    gbc.weightx = .4;
    gbc.weighty = .4888;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.LINE_END;
    gbc.fill = GridBagConstraints.BOTH;
    gridBagLayout.setConstraints(topRightPanel, gbc);
    this.add(topRightPanel);

    JPanel previewHolder = new JPanel(new BorderLayout());
    previewHolder.add(thePreview,BorderLayout.CENTER);
    previewHolder.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    thePreview.setBackground(Color.white);
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 2;
    gbc.gridheight = 3;
    gbc.weightx = .4;
    gbc.weighty = .4888;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.BOTH;
    gridBagLayout.setConstraints(previewHolder, gbc);
    this.add(previewHolder);
  }

  private void updateStroke()
  {
    if( changeOccuring )
      return;

    changeOccuring = true;

    float width =  (float)(theWidthSliderSpinner.getValue());
          width /= 10f;

    int end = 0;
    if( theEndButtons[0].isSelected() )
      end = BasicStroke.CAP_BUTT;
    else if( theEndButtons[1].isSelected() )
      end = BasicStroke.CAP_ROUND;
    else if( theEndButtons[2].isSelected() )
      end = BasicStroke.CAP_SQUARE;

    int join = 0;
    int miterLimit = 10;
    if( theJoinButtons[0].isSelected() )
    {
      join = BasicStroke.JOIN_MITER;
      theMiterLimitSpinner.setEnabled(true);
      theMiterLimitLabel.setEnabled(true);
      miterLimit = ((Integer)theMiterLimitSpinner.getValue()).intValue();
    }
    else if( theJoinButtons[1].isSelected() )
    {
      join = BasicStroke.JOIN_ROUND;
      theMiterLimitSpinner.setEnabled(false);
      theMiterLimitLabel.setEnabled(false);
    }
    else if( theJoinButtons[2].isSelected() )
    {
      join = BasicStroke.JOIN_BEVEL;
      theMiterLimitSpinner.setEnabled(false);
      theMiterLimitLabel.setEnabled(false);
    }

    float[] dashPhase = null;
    String dashString = theDashField.getText();
    if( dashString != null && !dashString.equals("") )
    {
      String[] dashes = dashString.split(",");
      dashPhase = new float[dashes.length];
      try
      {
        for( int i = 0 ; i < dashes.length ; i++ )
          dashPhase[i] = Float.parseFloat(dashes[i]);
      }
      catch( NumberFormatException nfe )
      {
        dashPhase = null;
      }
    }
    if( dashPhase != null )
    {
      boolean allZero = true;
      for( int i = 0 ; i < dashPhase.length ; i++ )
      {
        if( dashPhase[i] != 0 )
        {
          allZero = false;
          break;
        }
      }
      if( allZero )
        dashPhase = null;
    }

    float dashOffset = ((Float)theOffsetSpinner.getValue()).floatValue();
    theStroke = new BasicStroke(width,end,join,miterLimit,dashPhase,dashOffset);

    if( thePreview != null )
      thePreview.setStroke(theStroke);

    changeOccuring = false;
    notifyListeners();
  }

  public Paint getPaint()
  {
    return theSolidChooser.getColor();
  }

  public void setColor( Paint aColor)
  {
    theSolidChooser.setColor((Color)aColor);
  }

  public BasicStroke getStroke()
  {
    BasicStroke cloned = new BasicStroke(theStroke.getLineWidth(),theStroke.getEndCap(),theStroke.getLineJoin(),theStroke.getMiterLimit(),theStroke.getDashArray(),theStroke.getDashPhase());
    return theStroke;
  }

  public void setStroke( BasicStroke aStroke )
  {
    changeOccuring = true;

    theWidthSliderSpinner.setValue((int)(aStroke.getLineWidth()*10));

    int end = aStroke.getEndCap();
    if( end == BasicStroke.CAP_BUTT )
      theEndButtons[0].setSelected(true);
    else if( end == BasicStroke.CAP_ROUND )
      theEndButtons[1].setSelected(true);
    else if( end == BasicStroke.CAP_SQUARE )
      theEndButtons[2].setSelected(true);


    int join = aStroke.getLineJoin();
    int miterLimit = 10;
    if( join == BasicStroke.JOIN_MITER )
    {
      theJoinButtons[0].setSelected(true);
      join = BasicStroke.JOIN_MITER;
      theMiterLimitSpinner.setEnabled(true);
      theMiterLimitLabel.setEnabled(true);
      theMiterLimitSpinner.setValue(Integer.valueOf((int)aStroke.getMiterLimit()));
    }
    else if( join == BasicStroke.JOIN_ROUND )
    {
      theJoinButtons[1].setSelected(true);
      theMiterLimitSpinner.setEnabled(false);
      theMiterLimitLabel.setEnabled(false);
    }
    else if( join == BasicStroke.JOIN_BEVEL )
    {
      theJoinButtons[2].setSelected(true);
      theMiterLimitSpinner.setEnabled(false);
      theMiterLimitLabel.setEnabled(false);
    }


    float[] dashArray = aStroke.getDashArray();
    String dashString = "";
    if( dashArray != null )
    {
      for( int i = 0 ; i < dashArray.length ; i++ )
        dashString += dashArray[i] + ",";
    }
    theDashField.setText(dashString);

    theOffsetSpinner.setValue(Float.valueOf(((int)(aStroke.getDashPhase()*10))/10f));

    changeOccuring = false;
    updateStroke();
  }

  public void addChangeListener( ChangeListener aListener )
  {
    theChangeListeners.add(aListener);
  }

  public void removeChangeListener( ChangeListener aListener )
  {
    theChangeListeners.remove(aListener);
  }

  private void notifyListeners()
  {
    for( int i = 0 ; i < theChangeListeners.size() ; i++ )
    {
      ((ChangeListener)theChangeListeners.get(i)).stateChanged(new ChangeEvent(this));
    }
  }
}
