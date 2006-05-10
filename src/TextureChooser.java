/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.imageio.ImageIO;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.geom.Area;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.util.Vector;
import java.io.IOException;

public class TextureChooser extends JPanel
{
  public static final int TYPE_STRETCH = 12345;
  public static final int TYPE_TILE    = 54321;

  private OOPS theOOPS;

  public static final JFileChooser    theFileChooser = new JFileChooser(System.getProperty("user.home"));

  private JButton          theBrowseButton;
  private JButton          theFromSelectedButton;
  private JTextField       theFileField;
  private int              theType = TYPE_TILE;

  private ColorLabel thePreview;

  private TransformableTexturePaint thePaint;

  private JRadioButton theStretchButton;
  private JRadioButton theTileButton;

  private SliderSpinnerCombo theWidthSlider;
  private SliderSpinnerCombo theHeightSlider;

  private Vector theChangeListeners;

  public TextureChooser( OOPS anOOPS )
  {
    theOOPS = anOOPS;

    theChangeListeners = new Vector();

    ChangeListener listener = new ChangeListener()
    {
      public void stateChanged( ChangeEvent e )
      {
        setType();
        thePreview.setPaint(thePaint);
        notifyListeners();
      }
    };

    String[] s = ImageIO.getReaderFormatNames();
    Vector types = new Vector();
    theFileChooser.removeChoosableFileFilter(theFileChooser.getAcceptAllFileFilter());
    MyFileFilter PNGFileFilter = null;
    for( int i = 0 ; i < s.length ; i++ )
    {
      if( !types.contains(s[i].toLowerCase()) )
      {
        types.add(s[i].toLowerCase());
        theFileChooser.addChoosableFileFilter(new MyFileFilter(new String[]{s[i].toLowerCase()},s[i].toUpperCase() + " Images"));
      }
    }
    MyFileFilter allFileFilter = new MyFileFilter(s,"All Image Files");
    theFileChooser.addChoosableFileFilter(allFileFilter);
    theFileChooser.setFileFilter(allFileFilter);

    theFileField = new JTextField();
    theFileField.setEditable(false);

    Font buttonFont = new Font("Arial Narrow",Font.BOLD,8);

    theBrowseButton = new JButton("Browse...");
    theBrowseButton.setFont(buttonFont);
    theBrowseButton.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          selectFile();
        }
      }
    );

    theFromSelectedButton = new JButton("From Selected");
    theFromSelectedButton.setFont(buttonFont);
    theFromSelectedButton.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          setFromSelected();
        }
      }
    );

    thePreview = new ColorLabel();

    GridBagLayout topLayout = new GridBagLayout();

    GridBagConstraints gbc;

    JPanel topPanel = new JPanel();
    topPanel.setLayout(topLayout);

    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 1;
    gbc.weightx = 0;
    gbc.weighty = 1;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.LINE_END;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    topLayout.setConstraints(theFromSelectedButton, gbc);
    topPanel.add(theFromSelectedButton);

    JPanel holder = new JPanel(new BorderLayout());
    holder.add(thePreview,BorderLayout.CENTER);
    holder.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridheight = 2;
    gbc.weightx = .3;
    gbc.weighty = 1;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.BOTH;
    topLayout.setConstraints(holder, gbc);
    topPanel.add(holder);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.LINE_END;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    topLayout.setConstraints(theFileField, gbc);
    topPanel.add(theFileField);


    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 0;
    gbc.weightx = 0;
    gbc.weighty = 1;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.LINE_END;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    topLayout.setConstraints(theBrowseButton, gbc);
    topPanel.add(theBrowseButton);
    gbc = new GridBagConstraints();



    GridBagLayout gridBagLayout = new GridBagLayout();
    this.setLayout(gridBagLayout);


    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(2,2,2,2);
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(topPanel, gbc);
    this.add(topPanel);

    theStretchButton = new JRadioButton("Stretch");
    theStretchButton.setSelected(true);
    theStretchButton.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          theType = TYPE_STRETCH;
          setType();
          theWidthSlider.setEnabled(false);
          theHeightSlider.setEnabled(false);
          thePreview.setPaint(thePaint);
          notifyListeners();
        }
      }
    );
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = 1;
    gbc.weighty = .06f;
    gbc.insets = new Insets(1,2,1,2);
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theStretchButton, gbc);
    this.add(theStretchButton);


    theTileButton = new JRadioButton("Tile");
    theTileButton.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          theType = TYPE_TILE;
          setType();
          theWidthSlider.setEnabled(true);
          theHeightSlider.setEnabled(true);
          thePreview.setPaint(thePaint);
          notifyListeners();
        }
      }
    );
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.weightx = 1;
    gbc.weighty = .06f;
    gbc.insets = new Insets(1,2,1,2);
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theTileButton, gbc);
    this.add(theTileButton);

    ButtonGroup bg = new ButtonGroup();
    bg.add(theStretchButton);
    bg.add(theTileButton);

    theWidthSlider = new SliderSpinnerCombo(0,1000,1,"W");
    theWidthSlider.addChangeListener(listener);
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.weightx = 1;
    gbc.weighty = .06f;
    gbc.insets = new Insets(1,2,1,2);
    gbc.anchor = GridBagConstraints.LINE_END;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theWidthSlider, gbc);
    this.add(theWidthSlider);

    theHeightSlider = new SliderSpinnerCombo(0,1000,1,"H");
    theHeightSlider.addChangeListener(listener);
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.weightx = 1;
    gbc.weighty = .06f;
    gbc.insets = new Insets(1,2,1,2);
    gbc.anchor = GridBagConstraints.LINE_END;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theHeightSlider, gbc);
    this.add(theHeightSlider);

    theWidthSlider.setEnabled(false);
    theHeightSlider.setEnabled(false);
  }

  public TransformableTexturePaint getPaint()
  {
    if( thePaint == null )
      return null;
    return (TransformableTexturePaint)(thePaint.clone());
  }

  public void setFromSelected()
  {
    if( theOOPS.getDrawingArea() == null )
      return;

    BufferedImage image = null;

    Tool tool = theOOPS.getCurrentTool();
    if( tool == null )
    {
      return;
    }
    else
    {
      try
      {
        Vector holder;
        if( tool.getClass().equals(Class.forName("SubSelectFreeTool")) || tool.getClass().equals(Class.forName("SubSelectTool")) || tool.getClass().equals(Class.forName("MoveTool")) )
        {
          holder = ((Selectable)tool).getSelected();

          if( holder != null )
          {
            Area combination = new Area();
            for( int i = 0 ; i < holder.size() ; i++ )
            {
              combination.add(((PaintableShape)holder.get(i)).getArea());
            }
            Rectangle2D.Double bounds = new Rectangle2D.Double();
            bounds.setRect(combination.getBounds2D());

            image = new BufferedImage((int)Math.ceil(bounds.width),(int)Math.ceil(bounds.height),BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2D = image.createGraphics();

            g2D.addRenderingHints(theOOPS.getRenderingHints());
            g2D.setTransform(AffineTransform.getTranslateInstance(-bounds.x,-bounds.y));
            for( int i = 0 ; i < holder.size() ; i++ )
            {
              ((PaintableShape)holder.get(i)).paint(g2D);
            }
          }
          else
          {
            return;
          }
        }
        else
        {
          return;
        }
      }
      catch( ClassNotFoundException cnfe )
      {
        System.out.println(cnfe);
      }

      if( image != null )
      {
        Rectangle2D.Double rect = new Rectangle2D.Double(0,0,image.getWidth(),image.getHeight());

        TransformableTexturePaint paint = new TransformableTexturePaint(image,rect,TransformableTexturePaint.KIND_TEXTURE);
        paint.setType(TransformableTexturePaint.TYPE_STRETCH);

        setPaint(paint);
      }
    }
  }

  public void setPaint( TransformableTexturePaint aPaint )
  {
    if( aPaint.getType() == TransformableTexturePaint.TYPE_STRETCH )
    {
      theStretchButton.setSelected(true);
      theWidthSlider.setEnabled(false);
      theHeightSlider.setEnabled(false);
    }
    else
    {
      theTileButton.setSelected(true);
      theWidthSlider.setValue((int)aPaint.getAnchor().width);
      theHeightSlider.setValue((int)aPaint.getAnchor().height);
      theWidthSlider.setEnabled(true);
      theHeightSlider.setEnabled(true);
    }
    thePaint = new TransformableTexturePaint( aPaint.getImage(), aPaint.getAnchor(), aPaint.getKind());
    thePaint.setType(aPaint.getType());
    thePaint.setKind(aPaint.getKind());
    thePaint.setColors(aPaint.getColors());
    theFileField.setText("");
    thePreview.setPaint(thePaint);
    notifyListeners();
  }

  public void selectFile()
  {
    if( theFileChooser.showOpenDialog(theOOPS) == JFileChooser.APPROVE_OPTION)
    {
      String filename = null;
      try
      {
        thePaint = new TransformableTexturePaint(ImageIO.read(theFileChooser.getSelectedFile()),TransformableTexturePaint.KIND_TEXTURE);
        thePreview.setPaint(thePaint);
        setType();
        theFileField.setText(theFileChooser.getSelectedFile().getAbsolutePath());
        notifyListeners();
      }
      catch( IOException ioe )
      {
        System.out.println(ioe);
        theFileField.setText("");
      }
    }
  }

  private void setType()
  { 
    if( thePaint != null )
    {
      if( theStretchButton.isSelected() )
        thePaint.setType(TransformableTexturePaint.TYPE_STRETCH);
      else
      {
        thePaint.setType(TransformableTexturePaint.TYPE_STRETCH);
        thePaint.setAnchor(new Rectangle(0,0,theWidthSlider.getValue(),theHeightSlider.getValue()));
        thePaint.setType(TransformableTexturePaint.TYPE_TILE);
      }
    }
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