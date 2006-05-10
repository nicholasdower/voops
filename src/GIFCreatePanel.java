/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JFileChooser;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.HeadlessException;
import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;
import java.awt.image.BufferedImage;
import java.awt.Color;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

public class GIFCreatePanel extends JPanel
{
  private OOPS theOOPS;

  private OOPSButton   thePreviewButton;
  private OOPSButton   theClearButton;
  private OOPSButton   theSaveButton;
  private OOPSButton   theSaveAsButton;
  private OOPSButton   theSnapButton;
  private JLabel       theDelayLabel;
  private JTextField   theDelayField;
  private JLabel       theLoopLabel;
  private JTextField   theLoopField;
  private JCheckBox    theLoopBox;
  private JRadioButton theTransparentButton;
  private JRadioButton theOpaqueButton;
  private JScrollPane  theScrollPane;
  private JPanel       theContent;

  private Color theBackgroundColor;

  private JFileChooser theFileChooser;

  private Vector theImages;
  private Vector theSnaps;
  
  private String theCurrentFileName = null;

  public GIFCreatePanel( OOPS anOOPS )
  {
    theOOPS = anOOPS;

    theFileChooser = new JFileChooser(System.getProperty("user.home"));
    theFileChooser.removeChoosableFileFilter(theFileChooser.getAcceptAllFileFilter());
    theFileChooser.addChoosableFileFilter(new MyFileFilter(new String[]{"gif"},"GIF Images"));

    theImages = new Vector();
    theSnaps  = new Vector();

    thePreviewButton = new OOPSButton("PreviewGIF.png","Preview");
    theClearButton   = new OOPSButton("Clear.png","Clear All");
    theSaveButton    = new OOPSButton("Save.png","Save");
    theSaveAsButton  = new OOPSButton("SaveAs.png","Save As...");
    theSnapButton    = new OOPSButton("Snap.png","Snap");
    theDelayLabel    = new JLabel("Deley(ms):");
    theDelayField    = new JTextField("1000");
    theLoopLabel     = new JLabel("Loops:");
    theLoopField     = new JTextField("1");
    theLoopBox       = new JCheckBox("Loop Forever");

    theTransparentButton = new JRadioButton("Transparent");
    theOpaqueButton = new JRadioButton("Opaque");
    ButtonGroup group = new ButtonGroup();
    group.add(theOpaqueButton);
    group.add(theTransparentButton);
    theOpaqueButton.setSelected(true);

    theLoopBox.setSelected(true);
    theLoopLabel.setEnabled(false);
    theLoopField.setEnabled(false);

    theSnapButton.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          snap();
        }
      }
    );

    thePreviewButton.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          preview();
        }
      }
    );


    theLoopBox.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          theLoopLabel.setEnabled(!theLoopBox.isSelected());
          theLoopField.setEnabled(!theLoopBox.isSelected());
        }
      }
    );

    theSaveButton.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          resave();
        }
      }
    );

    theClearButton.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          clear();
        }
      }
    );

    theSaveAsButton.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          save();
        }
      }
    );


    theContent    = new JPanel(new GridLayout(0,1));
    theScrollPane = new JScrollPane(theContent); 


    GridBagLayout layout = new GridBagLayout();
    this.setLayout(layout);
 
    GridBagConstraints gbc;

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.BOTH;
    layout.setConstraints(theScrollPane, gbc);
    this.add(theScrollPane);

    JPanel buttonPanel = new JPanel(new GridLayout(1,0));
    buttonPanel.add(theSnapButton);
    buttonPanel.add(theSaveButton);
    buttonPanel.add(theSaveAsButton);
    buttonPanel.add(thePreviewButton);
    buttonPanel.add(theClearButton);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 2;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    layout.setConstraints(buttonPanel, gbc);
    this.add(buttonPanel);

    JPanel transparentPanel = new JPanel(new GridLayout(1,0));
    transparentPanel.add(theOpaqueButton);
    transparentPanel.add(theTransparentButton);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 2;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    layout.setConstraints(transparentPanel, gbc);
    this.add(transparentPanel);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 2;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    layout.setConstraints(theLoopBox, gbc);
    this.add(theLoopBox);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.weightx = 0;
    gbc.weighty = 0;
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    layout.setConstraints(theLoopLabel, gbc);
    this.add(theLoopLabel);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 4;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    layout.setConstraints(theLoopField, gbc);
    this.add(theLoopField);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 5;
    gbc.weightx = 0;
    gbc.weighty = 0;
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    layout.setConstraints(theDelayLabel, gbc);
    this.add(theDelayLabel);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 5;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    layout.setConstraints(theDelayField, gbc);
    this.add(theDelayField);
  }

  public void preview()
  {
    if( theImages.size() == 0 )
      return;

    int delay = 500;
    try
    {
      delay = Integer.parseInt(theDelayField.getText());
    }
    catch( NumberFormatException nfe )
    {
      delay = 500;
    }


    final GIFPreviewPanel previewPanel = new GIFPreviewPanel(theImages,delay);

    JDialog previewFrame = new JDialog(theOOPS,true);
    previewFrame.getContentPane().setLayout( new BorderLayout() );
    previewFrame.addWindowListener
    (
      new WindowAdapter()
      {
        public void windowClosing( WindowEvent e )
        {
          previewPanel.stop();
        }

        public void windowOpened( WindowEvent e )
        {
          previewPanel.start();
        }
      }
    );

    previewFrame.getContentPane().add(previewPanel);
    previewFrame.pack();

    previewFrame.setSize(((BufferedImage)theImages.get(0)).getWidth()+100,((BufferedImage)theImages.get(0)).getHeight()+100);
    previewFrame.setLocationRelativeTo(theOOPS);
    previewFrame.setVisible(true);
  }

  public void clear()
  {
    theImages = new Vector();
    theSnaps  = new Vector();

    theCurrentFileName = null;

    theContent.removeAll();
    theScrollPane.revalidate();
    theScrollPane.repaint();
  }

  public void resave()
  {
    if( theCurrentFileName == null )
      save();
    else
      doSaveWork();
  }
 
  public void save()
  {
    if( theImages.size() <= 0 )
      return;

    while( theFileChooser.showSaveDialog(theOOPS) == JFileChooser.APPROVE_OPTION )
    {
      if( theFileChooser.getSelectedFile().exists() )
      {
        try
        {
          int option = JOptionPane.showConfirmDialog(theOOPS,"Overwrite existing file?","Overwrite?",JOptionPane.YES_NO_CANCEL_OPTION);
          if( option == JOptionPane.NO_OPTION )
            continue;
          if( option == JOptionPane.CANCEL_OPTION )
            break;
        }
        catch( HeadlessException he ){}
      }
      String filename = null;
      filename = theFileChooser.getSelectedFile().getAbsolutePath();
      String extension = null;
      int i = filename.lastIndexOf('.');

      if (i > 0 &&  i < filename.length() - 1) 
      {
        extension = filename.substring(i+1).toLowerCase();

        if( extension.toLowerCase().equals("gif") )
          filename  = filename.substring(0,i);
        else
          extension = null;
      }

      if( extension == null )
      {
        filename  = theFileChooser.getSelectedFile().getAbsolutePath();
        extension = "gif";
      }

      theCurrentFileName = filename+"."+extension;
      doSaveWork();

      break;
    }
  }

  public void doSaveWork()
  {
    if( theCurrentFileName == null )
      return;

    AnimatedGifEncoder e = new AnimatedGifEncoder();
    e.start(theCurrentFileName);
    e.setQuality(1);

    int delay = 500;
    try
    {
      delay = Integer.parseInt(theDelayField.getText());
    }
    catch( NumberFormatException nfe )
    {
      delay = 500;
    }
    e.setDelay(delay);

    int loops = -69;
    try
    {
      loops = Integer.parseInt(theLoopField.getText());
    }
    catch( NumberFormatException nfe )
    {
      loops = -69;
    }
    if( loops == 0 )
      loops = -1;
    else if( loops == -69 )
      loops = 0;

    if( theLoopBox.isSelected() )
      loops = 0;

    e.setRepeat(loops);

    if( theTransparentButton.isSelected() )
      e.setTransparent(theBackgroundColor);

    for( int j = 0 ; j < theImages.size() ; j++ )
    {
      e.addFrame((BufferedImage)theImages.get(j));
    }
    e.finish();
  }

  public void snap()
  {
    if( theOOPS.getDrawingArea() == null )
      return;
  
    final BufferedImage image = theOOPS.getDrawingArea().getImage(BufferedImage.TYPE_INT_RGB);
    if( theImages.size() == 0 )
      theBackgroundColor = theOOPS.getDrawingArea().getBackground();
 

    final JPanel newSnapShot = new JPanel();
    newSnapShot.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
    GridBagLayout layout = new GridBagLayout();
    newSnapShot.setLayout(layout);
 
    GridBagConstraints gbc;

    JLabel label = new JLabel(new ImageIcon(image));
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 3;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.PAGE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    layout.setConstraints(label, gbc);
    newSnapShot.add(label);

    JButton delete = new OOPSButton("Delete.png","Delete");
    delete.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          theImages.remove(image);
          theSnaps.remove(newSnapShot);

          theContent.remove(newSnapShot);
          theScrollPane.revalidate();
          theScrollPane.repaint();
        }
      }
    );
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 0;
    gbc.weightx = 0;
    gbc.weighty = 0;
    gbc.anchor = GridBagConstraints.FIRST_LINE_END;
    layout.setConstraints(delete, gbc);
    newSnapShot.add(delete);

    JButton up = new OOPSButton("Up.png","Up");
    up.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          int index = theSnaps.indexOf(newSnapShot);
          if( index == 0 )
            return;
 
          theSnaps.remove(newSnapShot);
          theSnaps.insertElementAt(newSnapShot,index-1);

          theImages.remove(image);
          theImages.insertElementAt(image,index-1);

          theContent.removeAll();
          for( int i = 0 ; i < theSnaps.size() ; i++ )
            theContent.add((JPanel)theSnaps.get(i));

          theScrollPane.revalidate();
          theScrollPane.repaint();
        }
      }
    );
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.weightx = 0;
    gbc.weighty = 0;
    gbc.anchor = GridBagConstraints.FIRST_LINE_END;
    layout.setConstraints(up, gbc);
    newSnapShot.add(up);

    JButton down = new OOPSButton("Down.png","Down");
    down.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          int index = theSnaps.indexOf(newSnapShot);
          if( index == theSnaps.size()-1 )
            return;
 
          theSnaps.remove(newSnapShot);
          theSnaps.insertElementAt(newSnapShot,index+1);

          theImages.remove(image);
          theImages.insertElementAt(image,index+1);

          theContent.removeAll();
          for( int i = 0 ; i < theSnaps.size() ; i++ )
            theContent.add((JPanel)theSnaps.get(i));

          theScrollPane.revalidate();
          theScrollPane.repaint();
        }
      }
    );
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.anchor = GridBagConstraints.FIRST_LINE_END;
    layout.setConstraints(down, gbc);
    newSnapShot.add(down);

    theImages.add(image);
    theSnaps.add(newSnapShot);
    theContent.add(newSnapShot);

    theScrollPane.revalidate();
    theScrollPane.repaint();
  }
}