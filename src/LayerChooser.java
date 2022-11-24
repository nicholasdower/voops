/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Vector;
import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

public class LayerChooser extends JPanel
{
  private DrawingArea  theDrawingArea;
  private LayerManager theManager;
  private Vector       theLayers;

  private JPanel      theControls;
  private OOPSButton  theAddButton;

  private JScrollPane theScrollPane;
  private JPanel      theViewPortView;
  private GridBagLayout theGridBagLayout;

  private OOPSButton[]      theUpButtons;
  private OOPSButton[]      theDownButtons;
  private OOPSButton[]      theDeleteButtons;
  private OOPSButton[]      theVisibleButtons;
  private JRadioButton[] theRadioButtons;

  private ImageIcon theUpIcon;
  private ImageIcon theDownIcon;
  private ImageIcon theDeleteIcon;
  private ImageIcon theVisibleIcon;
  private ImageIcon theInvisibleIcon;
  private ImageIcon theMergerIcon;

  private JPanel[]  theLayerPanels;

  private JTextField[] theNameFields;

  public LayerChooser()
  {
    theUpIcon        = new ImageIcon(this.getClass().getClassLoader().getResource("Up.png"));
    theDownIcon      = new ImageIcon(this.getClass().getClassLoader().getResource("Down.png"));
    theDeleteIcon    = new ImageIcon(this.getClass().getClassLoader().getResource("Delete.png"));
    theVisibleIcon   = new ImageIcon(this.getClass().getClassLoader().getResource("Visible.png"));
    theInvisibleIcon = new ImageIcon(this.getClass().getClassLoader().getResource("Invisible.png"));
    theMergerIcon    = new ImageIcon(this.getClass().getClassLoader().getResource("Merger.png"));

    theControls  = new JPanel();
    theAddButton = new OOPSButton("Add.png","Add");
    theAddButton.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          theManager.addLayer();
          setDrawingArea(theDrawingArea);
        }
      }
    );
    theControls.add(theAddButton);

    theViewPortView = new JPanel();
    theGridBagLayout = new GridBagLayout();
    theViewPortView.setLayout(theGridBagLayout);
    theScrollPane = new JScrollPane(theViewPortView);

    this.setLayout(new BorderLayout());
    this.add(theControls,BorderLayout.PAGE_START);
    this.add(theScrollPane,BorderLayout.CENTER);
  }

  public void clear()
  {
    theViewPortView.removeAll();
  }

  public void setDrawingArea( DrawingArea anArea )
  {
    theViewPortView.removeAll();

    theDrawingArea = anArea;
    theManager     = theDrawingArea.getLayerManager();
    theLayers      = theManager.getLayers();

    theUpButtons      = new OOPSButton[theLayers.size()];
    theDownButtons    = new OOPSButton[theLayers.size()];
    theDeleteButtons  = new OOPSButton[theLayers.size()];
    theVisibleButtons = new OOPSButton[theLayers.size()];
    theRadioButtons   = new JRadioButton[theLayers.size()];
    theNameFields     = new JTextField[theLayers.size()];
    theLayerPanels    = new JPanel[theLayers.size()];

    GridBagConstraints gbc;

    ButtonGroup buttonGroup = new ButtonGroup();
    for( int i = (theLayers.size()-1) ; i >= 0 ; i-- )
    {
      final Layer currentLayer = (Layer)theLayers.get(i);

      theRadioButtons[i] = new JRadioButton();
      if( i == theManager.getCurrentLayerIndex() )
        theRadioButtons[i].setSelected(true);
      buttonGroup.add(theRadioButtons[i]);
      theRadioButtons[i].addActionListener
      (
        new ActionListener()
        {
          public void actionPerformed( ActionEvent e )
          {
            if( ((JRadioButton)e.getSource()).isSelected() )
              theManager.setCurrentLayer(currentLayer);
          }
        }
      );

      theUpButtons[i] = new OOPSButton(theUpIcon,"Up");
      theUpButtons[i].addActionListener
      (
        new ActionListener()
        {
          public void actionPerformed( ActionEvent e )
          {
            theManager.moveLayerUp(currentLayer);
            setDrawingArea(theDrawingArea);
            theDrawingArea.repaint();
          }
        }
      );

      theDownButtons[i] = new OOPSButton(theDownIcon,"Down");
      theDownButtons[i].addActionListener
      (
        new ActionListener()
        {
          public void actionPerformed( ActionEvent e )
          {
            theManager.moveLayerDown(currentLayer);
            setDrawingArea(theDrawingArea);
            theDrawingArea.repaint();
          }
        }
      );

      theDeleteButtons[i]  = new OOPSButton(theDeleteIcon,"Delete");
      theDeleteButtons[i].addActionListener
      (
        new ActionListener()
        {
          public void actionPerformed( ActionEvent e )
          {
            theManager.removeLayer(currentLayer);
            setDrawingArea(theDrawingArea);
            theDrawingArea.repaint();
            theDrawingArea.clearDrawingArea();
          }
        }
      );

      theVisibleButtons[i] = new OOPSButton(currentLayer.isVisible()?theVisibleIcon:theInvisibleIcon,"Visible?");
      theVisibleButtons[i].addActionListener
      (
        new ActionListener()
        {
          public void actionPerformed( ActionEvent e )
          {
            theManager.setLayerVisible(currentLayer,!currentLayer.isVisible());
            if( currentLayer.isVisible() )
              ((OOPSButton)e.getSource()).setIcon(theVisibleIcon);
            else
              ((OOPSButton)e.getSource()).setIcon(theInvisibleIcon);
            theDrawingArea.repaint();
          }
        }
      );

      theNameFields[i] = new JTextField(6);
      final JTextField currentField = theNameFields[i];
      theNameFields[i].getDocument().addDocumentListener
      (
        new DocumentListener()
        {
          public void changedUpdate( DocumentEvent e )
          {
            theManager.setLayerName(currentLayer,currentField.getText());
          }
          public void insertUpdate( DocumentEvent e )
          {
            theManager.setLayerName(currentLayer,currentField.getText());
          }
          public void removeUpdate( DocumentEvent e )
          {
            theManager.setLayerName(currentLayer,currentField.getText());
          }
        }
      );

      theNameFields[i].setText(((Layer)theLayers.get(i)).getName());


      JPanel rightPanel = new JPanel();
      rightPanel.setLayout(new BoxLayout(rightPanel,BoxLayout.X_AXIS));
      rightPanel.add(theVisibleButtons[i]);
      rightPanel.add(theUpButtons[i]);
      rightPanel.add(theDownButtons[i]);
      rightPanel.add(theDeleteButtons[i]);

      theLayerPanels[i] = new JPanel();
      theLayerPanels[i].setLayout(new BorderLayout());

      theLayerPanels[i].add(theRadioButtons[i],BorderLayout.LINE_START);
      theLayerPanels[i].add(theNameFields[i],BorderLayout.CENTER);
      theLayerPanels[i].add(rightPanel,BorderLayout.LINE_END);

      gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = (theLayers.size()-1)-i;
      gbc.weightx = 1;
      gbc.weighty = 0;
      gbc.insets = new Insets(1,1,1,1);
      gbc.anchor = GridBagConstraints.FIRST_LINE_START;
      gbc.fill = GridBagConstraints.HORIZONTAL;
      theGridBagLayout.setConstraints(theLayerPanels[i], gbc);
      theViewPortView.add(theLayerPanels[i]);

      if( i != 0 )
      {
        JLabel merger = new JLabel(theMergerIcon);
        merger.setToolTipText("Merge Layers");
        merger.addMouseListener
        (
          new MouseAdapter()
          {
            public void mouseReleased( MouseEvent e )
            {
              theManager.merge(currentLayer);
              setDrawingArea(theDrawingArea);
              theDrawingArea.repaint();
            }
          }
        );
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = (theLayers.size()-1)-i;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridheight = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        theGridBagLayout.setConstraints(merger, gbc);
        theViewPortView.add(merger);
      }
    }
    JLabel empty = new JLabel();
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = theLayers.size();
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    theGridBagLayout.setConstraints(empty, gbc);
    theViewPortView.add(empty);

    theViewPortView.revalidate();
    theViewPortView.repaint();
  }
}