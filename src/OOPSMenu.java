/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JLabel;
import javax.swing.JDialog;
import javax.swing.ImageIcon;
import javax.swing.ProgressMonitorInputStream;
import javax.swing.SwingUtilities;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Vector;
import java.util.Hashtable;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Toolkit;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.awt.Image;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.FilteredImageSource;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Cursor;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

import org.joshy.common.swing.xp.XPHelper;
import org.joshy.common.swing.xp.XPFactory;

public class OOPSMenu extends JMenuBar
{
  private OOPS theOOPS;

  private JFileChooser theFileChooser;
  private JFileChooser theOpenFileChooser;

  private Vector theShapeClipboard;

  private Hashtable theFilenames;
  private Hashtable theExtensions;

  private OOPSButton theToolbarSaveButton;
  private OOPSButton theToolbarCopyShapeButton;
  private OOPSButton theToolbarPasteShapeButton;
  private OOPSButton theToolbarNewButton;
  private OOPSButton theToolbarOpenButton;
  private OOPSButton theToolbarUndoButton;
  private OOPSButton theToolbarRedoButton;
  private OOPSButton theToolbarCopyToClipboardButton;
  private OOPSButton theToolbarPasteFromClipboardButton;
  private OOPSButton theToolbarInsertAnImageButton;

  private BevelChooser theBevelChooser;

  private JDialog theAboutFrame;

  private JCheckBox theSmoothButton;

  private Vector theTypes;
  private Vector theOpenTypes;
  public OOPSMenu( OOPS anOOPS )
  {
    theOOPS = anOOPS;

    theBevelChooser = new BevelChooser(theOOPS);

    JLabel label  = new JLabel(new ImageIcon(this.getClass().getClassLoader().getResource("SplashScreen.png")));
    theAboutFrame = new JDialog(theOOPS,true);
    theAboutFrame.getContentPane().setLayout( new BorderLayout() );
    theAboutFrame.getContentPane().add(label);
    theAboutFrame.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    theAboutFrame.addWindowListener
    (
      new WindowAdapter()
      {
        public void windowClosing( WindowEvent e )
        {
          theAboutFrame.setVisible(false);
        }
      }
    );
    theAboutFrame.pack();

    theFilenames = new Hashtable();
    theExtensions = new Hashtable();

    theFileChooser = new JFileChooser(System.getProperty("user.home"));
    theFileChooser.addChoosableFileFilter(new MyFileFilter(new String[]{"ico"},"Windows Icon Files (*.ico)"));
    theFileChooser.addChoosableFileFilter(new MyFileFilter(new String[]{"oop"},"VOOPS Images"));
    theFileChooser.addChoosableFileFilter(new MyFileFilter(new String[]{"gif"},"GIF Images"));
    theFileChooser.addChoosableFileFilter(new MyFileFilter(new String[]{"svg"},"Scalable Vector Graphics"));

    String[] s = ImageIO.getWriterFormatNames();
    theTypes = new Vector();
    theTypes.add("oop");
    theTypes.add("ico");
    theTypes.add("gif");
    theTypes.add("svg");
    theFileChooser.removeChoosableFileFilter(theFileChooser.getAcceptAllFileFilter());
    MyFileFilter PNGFileFilter = null;
    for( int i = 0 ; i < s.length ; i++ )
    {
      if( !theTypes.contains(s[i].toLowerCase()) )
      {
        theTypes.add(s[i].toLowerCase());
        if( s[i].toLowerCase().equals("png") )
        {
          PNGFileFilter = new MyFileFilter(new String[]{s[i].toLowerCase()},s[i].toUpperCase() + " Images");
          theFileChooser.addChoosableFileFilter(PNGFileFilter);
        }
        else if( s[i].toLowerCase().equals("wbmp") )
        {}
        else
          theFileChooser.addChoosableFileFilter(new MyFileFilter(new String[]{s[i].toLowerCase()},s[i].toUpperCase() + " Images"));
      }
    }
    if( PNGFileFilter != null )
      theFileChooser.setFileFilter(PNGFileFilter);


    theOpenFileChooser = new JFileChooser(System.getProperty("user.home"));
    theOpenFileChooser.removeChoosableFileFilter(theFileChooser.getAcceptAllFileFilter());

    s = ImageIO.getReaderFormatNames();
    theOpenTypes = new Vector();
    theOpenTypes.add("oop");
    for( int i = 0 ; i < s.length ; i++ )
    {
      if( !theOpenTypes.contains(s[i].toLowerCase()) )
      {
        theOpenTypes.add(s[i].toLowerCase());
        theOpenFileChooser.addChoosableFileFilter(new MyFileFilter(new String[]{s[i].toLowerCase()},s[i].toUpperCase() + " Images"));
      }
    }
    String[] all = new String[theOpenTypes.size()];
    for( int i = 0 ; i < all.length ; i++ )
    {
      all[i] = (String)theOpenTypes.get(i);
    }
    theOpenFileChooser.addChoosableFileFilter(new MyFileFilter(s,"All Supported Images"));

    MyFileFilter VOOPSFileFilter = new MyFileFilter(new String[]{"oop"},"VOOPS Images");
    theOpenFileChooser.addChoosableFileFilter(VOOPSFileFilter);
    theOpenFileChooser.setFileFilter(VOOPSFileFilter);

   
    theSmoothButton = new JCheckBox("Smooth");
    theSmoothButton.setSelected(true);
    theSmoothButton.setOpaque(false);
    theSmoothButton.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          theOOPS.setSmooth(theSmoothButton.isSelected());
        }
      }
    );


    ActionListener saveListener = new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        resave();
      }
    };

    ActionListener saveAsListener = new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        save();
      }
    };

    ActionListener copyShapeListener = new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        copyShape();
      }
    };

    ActionListener pasteShapeListener = new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        pasteShape();
      }
    };

    ActionListener copyToClipboardTransparentListener = new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        copyToClipboard(false);
      }
    };

    ActionListener copyToClipboardOpaqueListener = new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        copyToClipboard(true);
      }
    };

    ActionListener saveSelectionListener = new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        saveSelection();
      }
    };

    ActionListener pasteFromClipboardListener = new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        pasteFromClipboard();
      }
    };

    ActionListener insertImageListener = new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        insertImage();
      }
    };

    ActionListener openListener = new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        open();
      }
    };

    ActionListener newListener = new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        theOOPS.addDrawingArea();
      }
    };

    ActionListener exitListener = new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        exit();
      }
    };

    ActionListener undoListener = new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        theOOPS.getDrawingArea().undo();
      }
    };

    ActionListener redoListener = new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        theOOPS.getDrawingArea().redo();
      }
    };

    ActionListener GIFCreateListener = new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        theOOPS.showGIFCreater();
      }
    };

    ActionListener BeanshellListener = new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        theOOPS.showBeanshell();
      }
    };

    ActionListener aboutListener = new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        theAboutFrame.setLocationRelativeTo(theOOPS);
        theAboutFrame.setVisible(true);
      }
    };

    ActionListener bevelListener = new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        bevel();
      }
    };

    ActionListener alignLeftListener = new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        align(AlignTypes.LEFT);
      }
    };

    ActionListener alignRightListener = new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        align(AlignTypes.RIGHT);
      }
    };

    ActionListener alignCenterVerticalListener = new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        align(AlignTypes.CENTER_VERTICAL);
      }
    };

    ActionListener alignCenterHorizontalListener = new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        align(AlignTypes.CENTER_HORIZONTAL);
      }
    };

    ActionListener alignTopListener = new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        align(AlignTypes.TOP);
      }
    };

    ActionListener alignBottomListener = new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        align(AlignTypes.BOTTOM);
      }
    };

    ActionListener alignCentersListener = new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        align(AlignTypes.CENTERS);
      }
    };



    theToolbarNewButton = new OOPSButton("New.png","New");
    theToolbarNewButton.addActionListener(newListener);

    theToolbarOpenButton = new OOPSButton("Open.png","Open");
    theToolbarOpenButton.addActionListener(openListener);

    theToolbarSaveButton = new OOPSButton("Save.png","Save");
    theToolbarSaveButton.addActionListener(saveListener);

    theToolbarCopyShapeButton = new OOPSButton("CopyShape.png","Copy Shape(s)");
    theToolbarCopyShapeButton.addActionListener(copyShapeListener);

    theToolbarPasteShapeButton = new OOPSButton("PasteShape.png","Paste Shape(s)");
    theToolbarPasteShapeButton.addActionListener(pasteShapeListener);

    theToolbarCopyToClipboardButton = new OOPSButton("ToClipboard.png","Copy To Clipboard");

    theToolbarInsertAnImageButton = new OOPSButton("InsertImage.png","Insert An Image");
    theToolbarInsertAnImageButton.addActionListener(insertImageListener);

    final JMenu copyToClipboardMenu = new JMenu();

    JMenuItem OpaqueItem      = new JMenuItem("Opaque",new ImageIcon(this.getClass().getClassLoader().getResource("Opaque.png")));
    JMenuItem TransparentItem = new JMenuItem("Transparent",new ImageIcon(this.getClass().getClassLoader().getResource("Transparent.png")));

    OpaqueItem.addActionListener(copyToClipboardOpaqueListener);
    TransparentItem.addActionListener(copyToClipboardTransparentListener);

    copyToClipboardMenu.add(OpaqueItem);
    copyToClipboardMenu.add(TransparentItem);

    theToolbarCopyToClipboardButton.addMouseListener
    (
      new MouseAdapter()
      {
        public void mousePressed( MouseEvent e )
        {
          copyToClipboardMenu.doClick();
        }
      }
    );
    

    theToolbarPasteFromClipboardButton = new OOPSButton("FromClipboard.png","Paste From Clipboard");
    theToolbarPasteFromClipboardButton.addActionListener(pasteFromClipboardListener);

    theToolbarUndoButton = new OOPSButton("Undo.png","Undo");
    theToolbarUndoButton.addActionListener(undoListener);

    theToolbarRedoButton = new OOPSButton("Redo.png","Redo");
    theToolbarRedoButton.addActionListener(redoListener);

    XPHelper xp = XPFactory.getXPHelper();

    JMenu fileMenu = new JMenu("File");


    JMenuItem openItem  = xp.getOpenMenu();//new JMenuItem("Open",theToolbarOpenButton.getIcon());
    fileMenu.add(openItem);

    JMenuItem newItem  = xp.getNewMenu();//new JMenuItem("New",theToolbarNewButton.getIcon());
    fileMenu.add(newItem);

    JMenuItem saveItem  = xp.getSaveMenu();//new JMenuItem("Save",theToolbarSaveButton.getIcon());
    fileMenu.add(saveItem);

    JMenuItem saveAsItem  = new JMenuItem("Save As...");//,new ImageIcon(this.getClass().getClassLoader().getResource("SaveAs.png")));
    fileMenu.add(saveAsItem);

    JMenuItem saveSelectionItem = new JMenuItem("Save Selection...");
    fileMenu.add(saveSelectionItem);

    JMenuItem exitItem  = xp.getQuitMenu();//new JMenuItem("Exit",new ImageIcon(this.getClass().getClassLoader().getResource("Exit.png")));
    if(!xp.isMac())
    {
      fileMenu.addSeparator();
      fileMenu.add(exitItem);
    }

    JMenu editMenu = new JMenu("Edit");

    JMenuItem undoItem  = new JMenuItem("Undo",theToolbarUndoButton.getIcon());
    editMenu.add(undoItem);

    JMenuItem redoItem  = new JMenuItem("Redo",theToolbarRedoButton.getIcon());
    editMenu.add(redoItem);

    editMenu.addSeparator();

    JMenu viewMenu = new JMenu("View");
    JMenuItem GIFCreateItem  = new JMenuItem("Animated GIF Creator",new ImageIcon(this.getClass().getClassLoader().getResource("GIFCreater.png")));
    JMenuItem BeanshellItem  = new JMenuItem("Beanshell",new ImageIcon(this.getClass().getClassLoader().getResource("Beanie.png")));
    viewMenu.add(GIFCreateItem);
    viewMenu.add(BeanshellItem);

    JMenu copyToClipboardEditMenu  = new JMenu("Copy To Clipboard");
    copyToClipboardEditMenu.setIcon(theToolbarCopyToClipboardButton.getIcon());
    JMenuItem EditOpaqueItem      = new JMenuItem("Opaque",new ImageIcon(this.getClass().getClassLoader().getResource("Opaque.png")));
    JMenuItem EditTransparentItem = new JMenuItem("Transparent",new ImageIcon(this.getClass().getClassLoader().getResource("Transparent.png")));

    EditOpaqueItem.addActionListener(copyToClipboardOpaqueListener);
    EditTransparentItem.addActionListener(copyToClipboardTransparentListener);

    copyToClipboardEditMenu.add(EditOpaqueItem);
    copyToClipboardEditMenu.add(EditTransparentItem);
    editMenu.add(copyToClipboardEditMenu);


    JMenu helpMenu = new JMenu("Help");
    JMenuItem aboutItem  = new JMenuItem("About",new ImageIcon(this.getClass().getClassLoader().getResource("OOPSIcon.png")));
    helpMenu.add(aboutItem);

    JMenu effectMenu = new JMenu("Effects");
    JMenuItem bevelItem  = new JMenuItem("Bevel",new ImageIcon(this.getClass().getClassLoader().getResource("BevelEffect.png")));
    effectMenu.add(bevelItem);

    JMenu modifyMenu = new JMenu("Modify");

    JMenu alignMenu  = new JMenu("Align");
    alignMenu.setIcon(new ImageIcon(this.getClass().getClassLoader().getResource("Align.png")));
    JMenuItem alignCentersItem          = new JMenuItem("Align Centers",new ImageIcon(this.getClass().getClassLoader().getResource("AlignCenters.png")));
    JMenuItem alignLeftItem             = new JMenuItem("Align Left",new ImageIcon(this.getClass().getClassLoader().getResource("AlignLeft.png")));
    JMenuItem alignRightItem            = new JMenuItem("Align Right",new ImageIcon(this.getClass().getClassLoader().getResource("AlignRight.png")));
    JMenuItem alignCenterVerticalItem   = new JMenuItem("Align Center (Vertical)",new ImageIcon(this.getClass().getClassLoader().getResource("AlignCenterVertical.png")));
    JMenuItem alignCenterHorizontalItem = new JMenuItem("Align Center (Horizontal)",new ImageIcon(this.getClass().getClassLoader().getResource("AlignCenterHorizontal.png")));
    JMenuItem alignTopItem              = new JMenuItem("Align Top",new ImageIcon(this.getClass().getClassLoader().getResource("AlignTop.png")));
    JMenuItem alignBottomItem           = new JMenuItem("Align Bottom",new ImageIcon(this.getClass().getClassLoader().getResource("AlignBottom.png")));

    alignCentersItem.addActionListener(alignCentersListener);
    alignLeftItem.addActionListener(alignLeftListener);
    alignRightItem.addActionListener(alignRightListener);
    alignCenterVerticalItem.addActionListener(alignCenterVerticalListener);
    alignCenterHorizontalItem.addActionListener(alignCenterHorizontalListener);
    alignTopItem.addActionListener(alignTopListener);
    alignBottomItem.addActionListener(alignBottomListener);

    alignMenu.add(alignCentersItem);
    alignMenu.addSeparator();
    alignMenu.add(alignLeftItem);
    alignMenu.add(alignRightItem);
    alignMenu.addSeparator();
    alignMenu.add(alignCenterVerticalItem);
    alignMenu.add(alignCenterHorizontalItem);
    alignMenu.addSeparator();
    alignMenu.add(alignTopItem);
    alignMenu.add(alignBottomItem);

    modifyMenu.add(alignMenu);



    JMenu moveMenu  = new JMenu("Move");
    moveMenu.setIcon(new ImageIcon(this.getClass().getClassLoader().getResource("MoveMenu.png")));
    final JMenuItem moveUpItem         = new JMenuItem("Move Up",new ImageIcon(this.getClass().getClassLoader().getResource("MoveUp.png")));
    final JMenuItem moveDownItem       = new JMenuItem("Move Down",new ImageIcon(this.getClass().getClassLoader().getResource("MoveDown.png")));
    final JMenuItem moveToFrontItem    = new JMenuItem("Move To Front",new ImageIcon(this.getClass().getClassLoader().getResource("MoveToFront.png")));
    final JMenuItem moveToBackItem     = new JMenuItem("Move To Back",new ImageIcon(this.getClass().getClassLoader().getResource("MoveToBack.png")));
    final JMenuItem moveUpALayerItem   = new JMenuItem("Move Up A Layer",new ImageIcon(this.getClass().getClassLoader().getResource("MoveUpALayer.png")));
    final JMenuItem moveDownALayerItem = new JMenuItem("Move Down A Layer",new ImageIcon(this.getClass().getClassLoader().getResource("MoveDownALayer.png")));


    ActionListener moveListener = new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        if( theOOPS.getCurrentTool() == null || theOOPS.getDrawingArea() == null )
          return;
        Tool tool = theOOPS.getCurrentTool();
        try
        {
          if( tool.getClass().equals(Class.forName("SubSelectFreeTool")) || tool.getClass().equals(Class.forName("SubSelectTool")) || tool.getClass().equals(Class.forName("MoveTool")) )
          {
            Selectable selectableTool = (Selectable)tool;

            JMenuItem item = (JMenuItem)e.getSource();

            if( item.equals(moveUpItem) )
              selectableTool.moveUp();
            else if( item.equals(moveDownItem) )
              selectableTool.moveBack();
            else if( item.equals(moveToFrontItem) )
              selectableTool.moveToFront();
            else if( item.equals(moveToBackItem) )
              selectableTool.moveToBack();
            else if( item.equals(moveUpALayerItem) )
              selectableTool.moveUpALayer();
            else if( item.equals(moveDownALayerItem) )
              selectableTool.moveBackALayer();
          }
        }
        catch( ClassNotFoundException cnfe )
        {
          System.out.println(cnfe);
        }
      }
    };

    moveUpItem.addActionListener(moveListener);
    moveDownItem.addActionListener(moveListener);
    moveToFrontItem.addActionListener(moveListener);
    moveToBackItem.addActionListener(moveListener);
    moveUpALayerItem.addActionListener(moveListener);
    moveDownALayerItem.addActionListener(moveListener);
 
    moveMenu.add(moveUpItem);
    moveMenu.add(moveDownItem);
    moveMenu.addSeparator();
    moveMenu.add(moveToFrontItem);
    moveMenu.add(moveToBackItem);
    moveMenu.addSeparator();
    moveMenu.add(moveUpALayerItem);
    moveMenu.add(moveDownALayerItem);

    modifyMenu.add(moveMenu);


    final JMenuItem deleteItem         = new JMenuItem("Delete",new ImageIcon(this.getClass().getClassLoader().getResource("DeleteShape.png")));
    final JMenuItem combineItem        = new JMenuItem("Combine",new ImageIcon(this.getClass().getClassLoader().getResource("Combine.png")));
    final JMenuItem redrawItem         = new JMenuItem("Redraw",new ImageIcon(this.getClass().getClassLoader().getResource("Redraw.png")));
    final JMenuItem flipHorizontalItem = new JMenuItem("Flip Horizontal",new ImageIcon(this.getClass().getClassLoader().getResource("FlipHorizontal.png")));
    final JMenuItem flipVerticalItem   = new JMenuItem("Flip Vertical",new ImageIcon(this.getClass().getClassLoader().getResource("FlipVertical.png")));
    final JMenuItem groupItem          = new JMenuItem("Group",new ImageIcon(this.getClass().getClassLoader().getResource("Group.png")));
    final JMenuItem degroupItem        = new JMenuItem("Degroup",new ImageIcon(this.getClass().getClassLoader().getResource("Degroup.png")));

    ActionListener transformListener = new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        if( theOOPS.getCurrentTool() == null || theOOPS.getDrawingArea() == null )
          return;
        Tool tool = theOOPS.getCurrentTool();
        try
        {
          if( tool.getClass().equals(Class.forName("SubSelectFreeTool")) || tool.getClass().equals(Class.forName("SubSelectTool")) || tool.getClass().equals(Class.forName("MoveTool")) )
          {
            Selectable selectableTool = (Selectable)tool;

            JMenuItem item = (JMenuItem)e.getSource();

            if( item.equals(deleteItem) )
              selectableTool.delete();
            else if( item.equals(combineItem) )
              selectableTool.combine();
            else if( item.equals(redrawItem) )
              selectableTool.redraw();
            else if( item.equals(flipHorizontalItem) )
              selectableTool.flipHorizontal();
            else if( item.equals(flipVerticalItem) )
              selectableTool.flipVertical();
            else if( item.equals(groupItem) )
              selectableTool.group();
            else if( item.equals(degroupItem) )
              selectableTool.deGroup();
          }
        }
        catch( ClassNotFoundException cnfe )
        {
          System.out.println(cnfe);
        }
      }
    };


    deleteItem.addActionListener(transformListener);
    combineItem.addActionListener(transformListener);
    redrawItem.addActionListener(transformListener);
    flipHorizontalItem.addActionListener(transformListener);
    flipVerticalItem.addActionListener(transformListener);
    groupItem.addActionListener(transformListener);
    degroupItem.addActionListener(transformListener);

    modifyMenu.addSeparator();
    modifyMenu.add(deleteItem);
    modifyMenu.add(combineItem);
    modifyMenu.add(redrawItem);
    modifyMenu.addSeparator();
    modifyMenu.add(flipHorizontalItem);
    modifyMenu.add(flipVerticalItem);
    modifyMenu.addSeparator();
    modifyMenu.add(groupItem);
    modifyMenu.add(degroupItem);



    JMenuItem pasteFromClipboardItem  = new JMenuItem("Paste From Clipboard",theToolbarPasteFromClipboardButton.getIcon());
    editMenu.add(pasteFromClipboardItem);

    editMenu.addSeparator();

    JMenuItem copyShapesItem  = new JMenuItem("Copy Shape(s)",theToolbarCopyShapeButton.getIcon());
    editMenu.add(copyShapesItem);

    JMenuItem pasteShapesItem  = new JMenuItem("Paste Shape(s)",theToolbarPasteShapeButton.getIcon());
    editMenu.add(pasteShapesItem);

    editMenu.addSeparator();

    JMenuItem insertImageItem  = new JMenuItem("Insert An Image",theToolbarInsertAnImageButton.getIcon());
    editMenu.add(insertImageItem);



    saveItem.addActionListener(saveListener);
    saveAsItem.addActionListener(saveAsListener);
    openItem.addActionListener(openListener);
    newItem.addActionListener(newListener);
    exitItem.addActionListener(exitListener);

    undoItem.addActionListener(undoListener);
    redoItem.addActionListener(undoListener);
    copyShapesItem.addActionListener(copyShapeListener);
    pasteShapesItem.addActionListener(pasteShapeListener);
    pasteFromClipboardItem.addActionListener(pasteFromClipboardListener);
    insertImageItem.addActionListener(insertImageListener);
    saveSelectionItem.addActionListener(saveSelectionListener);

    GIFCreateItem.addActionListener(GIFCreateListener);

    BeanshellItem.addActionListener(BeanshellListener);

    bevelItem.addActionListener(bevelListener);

    aboutItem.addActionListener(aboutListener);


    GridBagLayout gridBagLayout = new GridBagLayout();
    this.setLayout(gridBagLayout);

    GridBagConstraints gbc;

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 0;
    gbc.weighty = 1;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.LINE_START;
    gridBagLayout.setConstraints(fileMenu, gbc);
    this.add(fileMenu);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.weightx = 0;
    gbc.weighty = 1;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.LINE_START;
    gridBagLayout.setConstraints(editMenu, gbc);
    this.add(editMenu);

    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 0;
    gbc.weightx = 0;
    gbc.weighty = 1;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.LINE_START;
    gridBagLayout.setConstraints(modifyMenu, gbc);
    this.add(modifyMenu);

    gbc = new GridBagConstraints();
    gbc.gridx = 3;
    gbc.gridy = 0;
    gbc.weightx = 0;
    gbc.weighty = 1;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.LINE_START;
    gridBagLayout.setConstraints(effectMenu, gbc);
    this.add(effectMenu);

    gbc = new GridBagConstraints();
    gbc.gridx = 4;
    gbc.gridy = 0;
    gbc.weightx = 0;
    gbc.weighty = 1;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.LINE_START;
    gridBagLayout.setConstraints(viewMenu, gbc);
    this.add(viewMenu);

    gbc = new GridBagConstraints();
    gbc.gridx = 5;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.LINE_START;
    gridBagLayout.setConstraints(helpMenu, gbc);
    this.add(helpMenu);
 
    if(!"true".equals(System.getProperty("apple.laf.useScreenMenuBar")))
    {
      gbc = new GridBagConstraints();
      gbc.gridx = 6;
      gbc.gridy = 0;
      gbc.weightx = 0;
      gbc.weighty = 0;
      gbc.insets = new Insets(1,1,1,1);
      gbc.anchor = GridBagConstraints.LINE_END;
      gridBagLayout.setConstraints(theToolbarSaveButton, gbc);
      this.add(theToolbarNewButton);

      gbc = new GridBagConstraints();
      gbc.gridx = 7;
      gbc.gridy = 0;
      gbc.weightx = 0;
      gbc.weighty = 0;
      gbc.insets = new Insets(1,1,1,1);
      gbc.anchor = GridBagConstraints.LINE_START;
      gridBagLayout.setConstraints(theToolbarSaveButton, gbc);
      this.add(theToolbarOpenButton);

      gbc = new GridBagConstraints();
      gbc.gridx = 8;
      gbc.gridy = 0;
      gbc.weightx = 0;
      gbc.weighty = 0;
      gbc.insets = new Insets(1,1,1,1);
      gbc.anchor = GridBagConstraints.LINE_START;
      gridBagLayout.setConstraints(theToolbarSaveButton, gbc);
      this.add(theToolbarSaveButton);

      gbc = new GridBagConstraints();
      gbc.gridx = 9;
      gbc.gridy = 0;
      gbc.weightx = 0;
      gbc.weighty = 0;
      gbc.insets = new Insets(1,1,1,1);
      gbc.anchor = GridBagConstraints.LINE_START;
      gridBagLayout.setConstraints(theToolbarUndoButton, gbc);
      this.add(theToolbarUndoButton);

      gbc = new GridBagConstraints();
      gbc.gridx = 10;
      gbc.gridy = 0;
      gbc.weightx = 0;
      gbc.weighty = 0;
      gbc.insets = new Insets(1,1,1,1);
      gbc.anchor = GridBagConstraints.LINE_START;
      gridBagLayout.setConstraints(theToolbarRedoButton, gbc);
      this.add(theToolbarRedoButton);

      gbc = new GridBagConstraints();
      gbc.gridx = 11;
      gbc.gridy = 0;
      gbc.weightx = 0;
      gbc.weighty = 0;
      gbc.insets = new Insets(1,1,1,1);
      gbc.anchor = GridBagConstraints.LINE_START;
      gridBagLayout.setConstraints(theToolbarCopyShapeButton, gbc);
      this.add(theToolbarCopyShapeButton);

      gbc = new GridBagConstraints();
      gbc.gridx = 12;
      gbc.gridy = 0;
      gbc.weightx = 0;
      gbc.weighty = 0;
      gbc.insets = new Insets(1,1,1,1);
      gbc.anchor = GridBagConstraints.LINE_START;
      gridBagLayout.setConstraints(theToolbarPasteShapeButton, gbc);
      this.add(theToolbarPasteShapeButton);

      gbc = new GridBagConstraints();
      gbc.gridx = 13;
      gbc.gridy = 0;
      gbc.weightx = 0;
      gbc.weighty = 0;
      gbc.anchor = GridBagConstraints.LINE_START;
      gridBagLayout.setConstraints(copyToClipboardMenu, gbc);
      this.add(copyToClipboardMenu);
      copyToClipboardMenu.setMaximumSize(new Dimension(1,1));
      copyToClipboardMenu.setMinimumSize(new Dimension(1,1));
      copyToClipboardMenu.setPreferredSize(new Dimension(1,1));

      gbc = new GridBagConstraints();
      gbc.gridx = 13;
      gbc.gridy = 0;
      gbc.weightx = 0;
      gbc.weighty = 0;
      gbc.insets = new Insets(1,1,1,1);
      gbc.anchor = GridBagConstraints.LINE_START;
      gridBagLayout.setConstraints(theToolbarCopyToClipboardButton, gbc);
      this.add(theToolbarCopyToClipboardButton);

      gbc = new GridBagConstraints();
      gbc.gridx = 14;
      gbc.gridy = 0;
      gbc.weightx = 0;
      gbc.weighty = 0;
      gbc.insets = new Insets(1,1,1,1);
      gbc.anchor = GridBagConstraints.LINE_START;
      gridBagLayout.setConstraints(theToolbarPasteFromClipboardButton, gbc);
      this.add(theToolbarPasteFromClipboardButton);

      gbc = new GridBagConstraints();
      gbc.gridx = 15;
      gbc.gridy = 0;
      gbc.weightx = 0;
      gbc.weighty = 0;
      gbc.insets = new Insets(1,1,1,1);
      gbc.anchor = GridBagConstraints.LINE_START;
      gridBagLayout.setConstraints(theToolbarInsertAnImageButton, gbc);
      this.add(theToolbarInsertAnImageButton);

      gbc = new GridBagConstraints();
      gbc.gridx = 16;
      gbc.gridy = 0;
      gbc.weightx = .3;
      gbc.weighty = 0;
      gbc.insets = new Insets(1,1,1,1);
      gbc.anchor = GridBagConstraints.LINE_START;
      gridBagLayout.setConstraints(theSmoothButton, gbc);
      this.add(theSmoothButton);
    }
  }

  public void exit()
  {
    try
    {
      int option = JOptionPane.showConfirmDialog(theOOPS,"Are you sure you want to exit?","Exit?",JOptionPane.YES_NO_OPTION);
      if( option == JOptionPane.YES_OPTION )
        System.exit(1);
    }
    catch( HeadlessException he ){}
  }

  public void align(int aType)
  {
    if( theOOPS.getCurrentTool() == null || theOOPS.getDrawingArea() == null )
      return;
    Tool tool = theOOPS.getCurrentTool();
    try
    {
      if( tool.getClass().equals(Class.forName("SubSelectFreeTool")) || tool.getClass().equals(Class.forName("SubSelectTool")) || tool.getClass().equals(Class.forName("MoveTool")) )
      {
        ((Selectable)tool).align(aType);
      }
      else
        return;
    }
    catch( ClassNotFoundException cnfe )
    {
      System.out.println(cnfe);
    }
    return;
  }

  public void bevel()
  {
    theBevelChooser.setVisible(true);

    double angle      = theBevelChooser.getAngle();

    double lightSize  = theBevelChooser.getLightBevelSize();
    int    lightSteps = theBevelChooser.getLightSteps();

    double darkSize   = theBevelChooser.getDarkBevelSize();
    int    darkSteps  = theBevelChooser.getDarkSteps();

    int    darkAlpha  = theBevelChooser.getDarkAlpha();
    int    lightAlpha = theBevelChooser.getLightAlpha();

    boolean showDark  = theBevelChooser.showDark();
    boolean showLight = theBevelChooser.showLight();

    angle = Math.toRadians(360-angle);
    if( theOOPS.getCurrentTool() == null || theOOPS.getDrawingArea() == null )
      return;
    Tool tool = theOOPS.getCurrentTool();
    try
    {
      Vector holder;
      if( tool.getClass().equals(Class.forName("SubSelectFreeTool")) || tool.getClass().equals(Class.forName("SubSelectTool")) || tool.getClass().equals(Class.forName("MoveTool")) )
      {
        holder = ((Selectable)tool).getSelected();
      }
      else
        return;

      if( holder != null && holder.size() > 0 )
      {
        theOOPS.getDrawingArea().setUndoPoint(new CompleteUndo(theOOPS.getDrawingArea()));

        PaintableShape     currentShape       = null;
        PaintableShape     currentShapeAsFill = null;
        Area               area               = null;
        Area               movingArea         = null;
        Area[]             bevelPieces        = new Area[darkSteps+lightSteps];
        double             lightXIncrement    = (Math.cos(angle)*lightSize)/(double)lightSteps;
        double             lightYIncrement    = (Math.sin(angle)*lightSize)/(double)lightSteps;
        double             darkXIncrement     = (Math.cos(angle)*darkSize)/(double)darkSteps;
        double             darkYIncrement     = (Math.sin(angle)*darkSize)/(double)darkSteps;
        Graphics2D         currentGraphics;
        Rectangle2D.Double currentBounds      = new Rectangle2D.Double();
        Color              color0             = new Color(255,255,255,lightAlpha);
        Color              color1             = new Color(0,0,0,darkAlpha);
        for( int i = 0 ; i < holder.size() ; i++ )
        {
          currentShape       = ((PaintableShape)holder.get(i)).get();
          currentShapeAsFill = ((PaintableShape)holder.get(i)).convertToFill(theOOPS.getRenderingHints());
          currentGraphics    = ((TransformableTexturePaint)currentShapeAsFill.getFillPaint()).getImage().createGraphics();
          currentGraphics.addRenderingHints(theOOPS.getRenderingHints());

          area                      = currentShape.getArea();

          currentBounds.setRect(area.getBounds2D());
          AffineTransform translate = AffineTransform.getTranslateInstance(-currentBounds.x,-currentBounds.y);

          area       = new Area(translate.createTransformedShape(area));
          movingArea = new Area(area);

          if( showLight )
          {
            currentGraphics.setColor(color0);
            for( int t = 0 ; t < lightSteps ; t++ )
            {
              movingArea = new Area(AffineTransform.getTranslateInstance(lightXIncrement,lightYIncrement).createTransformedShape(movingArea));
              bevelPieces[t] = new Area(area);
              bevelPieces[t].subtract(movingArea);
              currentGraphics.fill(bevelPieces[t]);
            }
          }

          if( showDark )
          {
            movingArea = new Area(area);
            currentGraphics.setColor(color1);
            for( int a = lightSteps ; a < darkSteps+lightSteps ; a++ )
            {
              movingArea = new Area(AffineTransform.getTranslateInstance(-darkXIncrement,-darkYIncrement).createTransformedShape(movingArea));
              bevelPieces[a] = new Area(area);
              bevelPieces[a].subtract(movingArea);
              currentGraphics.fill(bevelPieces[a]);
            }
          }
          currentGraphics.dispose();
          theOOPS.getDrawingArea().replaceShape((PaintableShape)holder.get(i),currentShapeAsFill);
        }
      }
    }
    catch( ClassNotFoundException cnfe )
    {
      System.out.println(cnfe);
    }
  }

  public Vector convertToFill()
  {
    if( theOOPS.getCurrentTool() == null || theOOPS.getDrawingArea() == null )
      return null;
    Tool tool = theOOPS.getCurrentTool();
    try
    {
      Vector holder;
      if( tool.getClass().equals(Class.forName("SubSelectFreeTool")) || tool.getClass().equals(Class.forName("SubSelectTool")) || tool.getClass().equals(Class.forName("MoveTool")) )
      {
        holder = ((Selectable)tool).getSelected();
      }
      else
        return null;

      if( holder != null && holder.size() > 0 )
      {
        PaintableShape currentShape = null;
        for( int i = 0 ; i < holder.size() ; i++ )
        {
          currentShape = ((PaintableShape)holder.get(i)).convertToFill(theOOPS.getRenderingHints());
          theOOPS.getDrawingArea().replaceShape((PaintableShape)holder.get(i),currentShape);
          holder.setElementAt(currentShape,i);
        }
      }
      return holder;
    }
    catch( ClassNotFoundException cnfe )
    {
      System.out.println(cnfe);
    }
    return null;
  }

  public void saveSelection()
  {
    BufferedImage image = getSelectedImage(false);
    String[] file = getSaveFile();

    if( file == null )
      return;

    if( file[1].toLowerCase().equals("png") )
    {
      try
      {
        ImageIO.write( image, file[1], new File(file[0]+"."+file[1]) );
      }
      catch( java.io.IOException ioe )
      {
        System.out.println(ioe);
      }
    }
    else if( file[1].toLowerCase().equals("gif") )
    {
      AnimatedGifEncoder e = new AnimatedGifEncoder();
      e.start(file[0]+"."+file[1]);
      e.setQuality(1);
      e.addFrame(image);
      e.finish();
    }
  }

  public void insertImage()
  {
    if( TextureChooser.theFileChooser.showOpenDialog(theOOPS) == JFileChooser.APPROVE_OPTION)
    {
      try
      {
        BufferedImage image = ImageIO.read(TextureChooser.theFileChooser.getSelectedFile());
        insertImage(image,theOOPS.getDrawingArea());
      }
      catch( IOException ioe ) {System.out.println(ioe);} 
    }
  }
 
  public void insertImage(BufferedImage anImage, DrawingArea aDrawingArea)
  {
    TransformableTexturePaint paint = new TransformableTexturePaint(anImage,TransformableTexturePaint.KIND_TEXTURE);
    PaintableShape shape = new PaintableShape(new Rectangle2D.Double(0,0,anImage.getWidth(),anImage.getHeight()),PaintableShape.TYPE_FILL);
    shape.setFillPaint(paint);
    aDrawingArea.setUndoPoint(new CompleteUndo(aDrawingArea));
    aDrawingArea.addShape(shape);
    aDrawingArea.repaint();
  }

  public void copyToClipboard( boolean shouldDrawBackground )
  {
    BufferedImage image = getSelectedImage(shouldDrawBackground);
    if( image != null )
    {
      ImageSelection imgSel = new ImageSelection(image);
      Toolkit.getDefaultToolkit().getSystemClipboard().setContents(imgSel, null);
    }
  }

  public BufferedImage getSelectedImage( boolean shouldDrawBackground )
  {
    if( theOOPS.getDrawingArea() == null )
      return null;

    BufferedImage image = null;

    Tool tool = theOOPS.getCurrentTool();
    if( tool == null )
    {
      if( shouldDrawBackground )
        image = theOOPS.getDrawingArea().getImage(BufferedImage.TYPE_INT_RGB);
      else
        image = theOOPS.getDrawingArea().getImage();
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
            if( shouldDrawBackground )
            {
              g2D.setPaint(theOOPS.getDrawingArea().getBackground());
              g2D.fillRect(0,0,image.getWidth(),image.getHeight());
            }
            g2D.addRenderingHints(theOOPS.getRenderingHints());
            g2D.setTransform(AffineTransform.getTranslateInstance(-bounds.x,-bounds.y));
            for( int i = 0 ; i < holder.size() ; i++ )
            {
              ((PaintableShape)holder.get(i)).paint(g2D);
            }
          }
          else
          {
            if( shouldDrawBackground )
              image = theOOPS.getDrawingArea().getImage(BufferedImage.TYPE_INT_RGB);
            else
              image = theOOPS.getDrawingArea().getImage();
          }
        }
        else
        {
          if( shouldDrawBackground )
            image = theOOPS.getDrawingArea().getImage(BufferedImage.TYPE_INT_RGB);
          else
            image = theOOPS.getDrawingArea().getImage();
        }
      }
      catch( ClassNotFoundException cnfe )
      {
        System.out.println(cnfe);
      }

      return image;
    }
    return null;
  }

  public void pasteFromClipboard()
  {
    try
    {
      Image image = (Image)Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null).getTransferData(DataFlavor.imageFlavor);
      if( image == null )
        return;

      BufferedImage buffImage = new BufferedImage(image.getWidth(null),image.getHeight(null),BufferedImage.TYPE_INT_ARGB);
      buffImage.createGraphics().drawImage(image,0,0,null);

      Rectangle2D.Double rect = new Rectangle2D.Double(0,0,buffImage.getWidth(),buffImage.getHeight());

      TransformableTexturePaint paint = new TransformableTexturePaint(buffImage,rect,TransformableTexturePaint.KIND_TEXTURE);
      paint.setType(TransformableTexturePaint.TYPE_STRETCH);

      PaintableShape shape = new PaintableShape(PaintableShape.TYPE_FILL);
      shape.setFillPaint(paint);
      shape.setDrawPaint(new Color(0,0,0,0));
      shape.setStroke(new BasicStroke(0f)); 
      shape.setShape(rect);

      theOOPS.getDrawingArea().setUndoPoint(new CompleteUndo(theOOPS.getDrawingArea()));
      theOOPS.getDrawingArea().addShape(shape);
      theOOPS.getDrawingArea().repaint();
    }
    catch( IOException ioe ){}
    catch( UnsupportedFlavorException ufe ){}
  }

  public void copyShape()
  {
    if( theOOPS.getCurrentTool() == null || theOOPS.getDrawingArea() == null )
      return;
    Tool tool = theOOPS.getCurrentTool();
    try
    {
      Vector holder;
      if( tool.getClass().equals(Class.forName("SubSelectFreeTool")) || tool.getClass().equals(Class.forName("SubSelectTool")) || tool.getClass().equals(Class.forName("MoveTool")) )
      {
        holder = ((Selectable)tool).getSelected();
      }
      else
        return;

      if( holder != null )
      {
        Vector groups = new Vector();
        for( int i = 0 ; i < holder.size() ; i++ )
        {
          Vector currentGroup = ((PaintableShape)holder.get(i)).getGroup();
          if(  currentGroup != null )
          {
            boolean groupSelected = true;
            for( int j = 0 ; j < currentGroup.size() ; j++ )
            {
              if( !holder.contains((PaintableShape)currentGroup.get(j)) )
              {
                groupSelected = false;
                break;
              }
            }
            if( groupSelected && !groups.contains(currentGroup))
              groups.add(currentGroup);
            else
              groups.add(new Vector());
          }
          else
            groups.add(new Vector());
        }
        Vector newGroups = new Vector();
        for( int i = 0 ; i < groups.size() ; i++ )
          newGroups.add(((Vector)groups.get(i)).clone());

        for( int i = 0 ; i < holder.size() ; i++ )
        {
          PaintableShape oldShape = (PaintableShape)holder.get(i);
          PaintableShape newShape = oldShape.get();
          Vector currentGroup = null;
          for( int j = 0 ; j < groups.size() ; j++ )
          {
            if( ((Vector)groups.get(j)).contains(oldShape) )
            {
              currentGroup = (Vector)newGroups.get(j);
              break;
            }
          }
          if( currentGroup != null )
          {
            currentGroup.setElementAt(newShape,currentGroup.indexOf(oldShape));
            newShape.setGroup(currentGroup);
          }
          holder.setElementAt(newShape,i);
        }

        theShapeClipboard = holder;

        Area combination = new Area();
        for( int i = 0 ; i < theShapeClipboard.size() ; i++ )
        {
          combination.add(((PaintableShape)theShapeClipboard.get(i)).getArea());
        }
        Rectangle2D.Double bounds = new Rectangle2D.Double();
        bounds.setRect(combination.getBounds2D());
        
        for( int i = 0 ; i < theShapeClipboard.size() ; i++ )
        {
          ((PaintableShape)theShapeClipboard.get(i)).transform(AffineTransform.getTranslateInstance(-bounds.x,-bounds.y));
        }
      }
    }
    catch( ClassNotFoundException cnfe )
    {
      System.out.println(cnfe);
    }
  }

  public void pasteShape()
  {
    if( theOOPS.getDrawingArea() == null || theShapeClipboard == null)
      return;

    if( theShapeClipboard != null && theShapeClipboard.size() > 0 )
    {
      theOOPS.getDrawingArea().setUndoPoint(new CompleteUndo(theOOPS.getDrawingArea()));
      Vector oldGroups = new Vector();
      for( int i = 0 ; i < theShapeClipboard.size() ; i++ )
      {
        if( !oldGroups.contains(((PaintableShape)theShapeClipboard.get(i)).getGroup()) )
          oldGroups.add(((PaintableShape)theShapeClipboard.get(i)).getGroup());
      }

      Vector newGroups = new Vector();
      for( int i = 0 ; i < oldGroups.size() ; i++ )
      {
        if( oldGroups.get(i) != null )
          newGroups.add(((Vector)oldGroups.get(i)).clone());
        else
          newGroups.add(null);
      }

      Vector newShapes = new Vector();
      for( int i = 0 ; i < theShapeClipboard.size() ; i++ )
      {
        newShapes.add(((PaintableShape)theShapeClipboard.get(i)).get());
        Vector currentGroup = ((PaintableShape)theShapeClipboard.get(i)).getGroup();
        if( currentGroup != null )
        {
          int index = oldGroups.indexOf(currentGroup);
          ((PaintableShape)newShapes.get(newShapes.size()-1)).setGroup((Vector)newGroups.get(index));
        }
      } 

      for( int i = 0 ; i < oldGroups.size() ; i++ )
      {
        Vector currentGroup = (Vector)oldGroups.get(i);
        Vector currentNewGroup = (Vector)newGroups.get(i);
        if( currentGroup != null)
        {
          for( int j = 0 ; j < currentGroup.size() ; j++ )
          {
            PaintableShape currentShape = ((PaintableShape)currentGroup.get(j));
            int index = theShapeClipboard.indexOf(currentShape);
            PaintableShape currentNewShape = ((PaintableShape)newShapes.get(index));
            currentNewGroup.setElementAt(currentNewShape,currentNewGroup.indexOf(currentShape));
          }
        }
      }
      for( int i = 0 ; i < newShapes.size() ; i++ )
      {
        theOOPS.getDrawingArea().addShape((PaintableShape)newShapes.get(i));
      }
      theOOPS.getDrawingArea().repaint();
    }
  }
  
  public void open()
  {
    if( theOpenFileChooser.showOpenDialog(theOOPS) == JFileChooser.APPROVE_OPTION )
    {
      openFile(theOpenFileChooser.getSelectedFile());
    }
  }

  public void openFile( final File aFile )
  {
    String filename = null;
    filename = theOpenFileChooser.getSelectedFile().getAbsolutePath();
    String extension = null;
    int i = filename.lastIndexOf('.');

    if (i > 0 &&  i < filename.length() - 1)
    {
      extension = filename.substring(i+1).toLowerCase();
    }

    if( extension == null || !theOpenTypes.contains(extension.toLowerCase()) )
      return; //TODO(me):Need an error message

    if(!extension.equals("oop"))
    {
      try
      {
        BufferedImage image = ImageIO.read(theOpenFileChooser.getSelectedFile());
        DrawingArea area = theOOPS.addDrawingArea();
        area.setImageSize(image.getWidth(),image.getHeight());
        insertImage(image,area);
        theFilenames.put(area,filename);
        theExtensions.put(area,extension);
        theOOPS.getDrawingFrame().setTitle(filename.substring(filename.lastIndexOf(System.getProperty("file.separator"))+1) + "." + extension);
      } 
      catch( IOException ioe ) {System.out.println(ioe);}
      return;
    }

    final Runnable run;
    final Thread thread;

    run = new Runnable()
    {
      public void run()
      {
        try
        {
          InputStream       is  = new ProgressMonitorInputStream
                                  (
                                    theOOPS,
                                    "Opening " + aFile.getName(),
                                    new FileInputStream(aFile)
                                  );
          ObjectInputStream ois = new ObjectInputStream(is);
          final DrawingArea da        = new DrawingArea(theOOPS);


          String filename = aFile.getAbsolutePath();
          filename  = filename.substring(0,filename.lastIndexOf('.'));

          theFilenames.put(da,filename);
          theExtensions.put(da,"oop");

          da.read(ois);
          Runnable adder = new Runnable()
          {
            public void run()
            {
              theOOPS.addDrawingArea(da,aFile.getName());
            }
          };
          SwingUtilities.invokeLater(adder);
          is.close();
          ois.close();
        }
        catch( Exception e )
        {
          System.out.println(e);
          e.printStackTrace();
        }
      }
    };

    thread = new Thread(run);
    thread.start();
  }

  public void resave()
  {
    if( theOOPS.getDrawingArea() == null )
      return;

    String filename  = (String)theFilenames.get(theOOPS.getDrawingArea());
    String extension = (String)theExtensions.get(theOOPS.getDrawingArea());
 
    if( filename == null || extension == null )
      save();
    else
      doSaveWork(filename,extension);
  }

  public void save()
  {
    String[] file = getSaveFile();
    if( file != null )
      doSaveWork(file[0],file[1]);
  }

  public String[] getSaveFile()
  {
    if( theOOPS.getDrawingArea() == null )
      return null;

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

        if( theTypes.contains(extension.toLowerCase()) )
          filename  = filename.substring(0,i);
        else
          extension = null;
      }

      if( extension == null )
      {
        filename  = theFileChooser.getSelectedFile().getAbsolutePath();
        extension = ((MyFileFilter)(theFileChooser.getFileFilter())).getExtensions()[0];
      }

      return new String[]{filename,extension};
    }
    return null;
  }

  private void doSaveWork( String filename, String extension )
  {
      theFilenames.put(theOOPS.getDrawingArea(),filename);
      theExtensions.put(theOOPS.getDrawingArea(),extension);

      if( extension.toLowerCase().equals("ico") )
      {
        try 
        {
          BufferedImage[] images = new BufferedImage[8];

          images[4] = theOOPS.getDrawingArea().getImage(16,16,BufferedImage.TYPE_INT_ARGB);
          images[5] = theOOPS.getDrawingArea().getImage(32,32,BufferedImage.TYPE_INT_ARGB);
          images[6] = theOOPS.getDrawingArea().getImage(48,48,BufferedImage.TYPE_INT_ARGB);
          images[7] = theOOPS.getDrawingArea().getImage(64,64,BufferedImage.TYPE_INT_ARGB);

          images[0] = theOOPS.getDrawingArea().getImage(16,16,BufferedImage.TYPE_INT_ARGB);
          WritableRaster wRaster = images[0].getRaster();
          for( int x = 0 ; x < 16 ; x++ )
          {
            for( int y = 0 ; y < 16 ; y++ )
            {
              if( wRaster.getSample(x,y,3) == 0 )
              {
                wRaster.setSample(x,y,3,0);
              }
              else if( wRaster.getSample(x,y,3) < 255 )
              {
                wRaster.setSample(x,y,3,255);
              }
            }
          }

          images[1] = theOOPS.getDrawingArea().getImage(32,32,BufferedImage.TYPE_INT_ARGB);
          wRaster = images[1].getRaster();
          for( int x = 0 ; x < 32 ; x++ )
          {
            for( int y = 0 ; y < 32 ; y++ )
            {
              if( wRaster.getSample(x,y,3) == 0 )
              {
                wRaster.setSample(x,y,3,0);
              }
              else if( wRaster.getSample(x,y,3) < 255 )
              {
                wRaster.setSample(x,y,3,255);
              }
            }
          }

          images[2] = theOOPS.getDrawingArea().getImage(48,48,BufferedImage.TYPE_INT_ARGB);
          wRaster = images[2].getRaster();
          for( int x = 0 ; x < 48 ; x++ )
          {
            for( int y = 0 ; y < 48 ; y++ )
            {
              if( wRaster.getSample(x,y,3) == 0 )
              {
                wRaster.setSample(x,y,3,0);
              }
              else if( wRaster.getSample(x,y,3) < 255 )
              {
                wRaster.setSample(x,y,3,255);
              }
            }
          }

          images[3] = theOOPS.getDrawingArea().getImage(64,64,BufferedImage.TYPE_INT_ARGB);
          wRaster = images[3].getRaster();
          for( int x = 0 ; x < 64 ; x++ )
          {
            for( int y = 0 ; y < 64 ; y++ )
            {
              if( wRaster.getSample(x,y,3) == 0 )
              {
                wRaster.setSample(x,y,3,0);
              }
              else if( wRaster.getSample(x,y,3) < 255 )
              {
                wRaster.setSample(x,y,3,255);
              }
            }
          }

          int[] qualities = new int[]{24,24,24,24,32,32,32,32};

          MyICOEncoder encoder = new MyICOEncoder();

          FileOutputStream fos = new FileOutputStream(filename+"."+extension);
          LEDataOutputStream icoFile = new LEDataOutputStream(fos);
          encoder.encode(images, icoFile, qualities);
          fos.close();
          icoFile.close();
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
      else if( extension.toLowerCase().equals("png") )
      {
        try
        {
          ImageIO.write( theOOPS.getDrawingArea().getImage(), extension, new File(filename+"."+extension) );
        } 
        catch( java.io.IOException           ioe  ){System.out.println(ioe); }
      }
      else if( extension.toLowerCase().equals("gif") )
      {
        AnimatedGifEncoder e = new AnimatedGifEncoder();
        e.start(filename+"."+extension);
        e.setQuality(1); 
        e.addFrame(theOOPS.getDrawingArea().getImage(BufferedImage.TYPE_INT_RGB));
        e.finish();
      }
      else if( extension.toLowerCase().equals("oop") )
      {
        try
        {
          ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(filename+"."+extension) ));
          DrawingArea da = theOOPS.getDrawingArea();
          Cursor c = da.getCursor();
          double oldZoom = da.getZoom();
          da.setZoom(1);
          da.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
          da.write(oos);
          oos.close();
          da.setCursor(c);
          da.setZoom(oldZoom);
        } 
        catch( Exception           ioe  ){System.out.println(ioe); ioe.printStackTrace();}
      }
      else if( extension.toLowerCase().equals("svg") )
      {
        try
        {
          PrintWriter pw = new PrintWriter(new FileOutputStream(new File(filename+"."+extension) ));
          DrawingArea da = theOOPS.getDrawingArea();

          pw.println("<?xml version=\"1.0\" standalone=\"no\"?>");
          pw.println("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">");

          int width  = da.getImageSize().width;
          int height = da.getImageSize().height;
          pw.println("<svg width=\"" + width + "\" height=\"" + height + "\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\">");

          pw.println("  <g>");
          pw.println("    <defs>");

          Vector         shapes       = da.getShapes();

          //Print Out Fills
          Paint          fillPaint;
          Vector         fillPaintVector = new Vector();
          for( int i = 0 ; i < shapes.size() ; i++ )
          {
            fillPaint = ((PaintableShape)shapes.get(i)).getFillPaint();

            if( fillPaint != null && fillPaint.getClass().equals(Class.forName("TransformableTexturePaint")) )
            {
              if( !fillPaintVector.contains(fillPaint) )
                fillPaintVector.add(fillPaint);
            }
          }
          TransformableTexturePaint currentTexturePaint;
          for( int i = 0 ; i < fillPaintVector.size() ; i++ )
          {
            currentTexturePaint = (TransformableTexturePaint)fillPaintVector.get(i);
            Color[] colors = currentTexturePaint.getColors();
            if( currentTexturePaint.getKind() == TransformableTexturePaint.KIND_RADIAL )
            {
              String color0 = "rgb(" + colors[0].getRed() + "," + colors[0].getGreen() + "," + colors[0].getBlue() + ")";
              String color1 = "rgb(" + colors[1].getRed() + "," + colors[1].getGreen() + "," + colors[1].getBlue() + ")";

              double cx = currentTexturePaint.getCurrentAnchor().x + currentTexturePaint.getCurrentAnchor().width/2;
              double cy = currentTexturePaint.getCurrentAnchor().y + currentTexturePaint.getCurrentAnchor().height/2;

              double r  = DistanceGetter.getDistance(currentTexturePaint.getCurrentAnchor().x, currentTexturePaint.getCurrentAnchor().y, cx, cy);

              pw.println("      <radialGradient id=\"fill" + i + "\" cx=\"" + cx + "\" cy=\"" + cy + "\" r=\"" + r + "\" gradientUnits=\"userSpaceOnUse\">");
              pw.println("        <stop stop-color=\"" + color1 + "\" stop-opacity=\"" + ((float)colors[1].getAlpha())/255f + "\" offset=\"0%\" />");
              pw.println("        <stop stop-color=\"" + color0 + "\" stop-opacity=\"" + ((float)colors[0].getAlpha())/255f + "\" offset=\"100%\" />");
              pw.println("      </radialGradient>");
            }
            else if( currentTexturePaint.getKind() == TransformableTexturePaint.KIND_GRADIENT )
            {
              if( colors[0].getRGB() == colors[1].getRGB() && colors[2].getRGB() == colors[3].getRGB() )
              {
                String color0 = "rgb(" + colors[0].getRed() + "," + colors[0].getGreen() + "," + colors[0].getBlue() + ")";
                String color2 = "rgb(" + colors[2].getRed() + "," + colors[2].getGreen() + "," + colors[2].getBlue() + ")";

                double x1 = currentTexturePaint.getAnchor().x;
                double y1 = currentTexturePaint.getAnchor().y;
                double x2 = currentTexturePaint.getAnchor().x;
                double y2 = currentTexturePaint.getAnchor().y + currentTexturePaint.getAnchor().height/2;

                if( currentTexturePaint.getType() == TransformableTexturePaint.TYPE_STRETCH )
                  pw.println("      <linearGradient id=\"fill" + i + "\" gradientUnits=\"userSpaceOnUse\" spreadMethod=\"pad\" >");
                else
                  pw.println("      <linearGradient id=\"fill" + i + "\" gradientUnits=\"userSpaceOnUse\" spreadMethod=\"reflect\" x1=\"" + x1 + "\" y1=\"" + y1 + "\" x2=\"" + x2 + "\" y2=\"" + y2 + "\" >");
                pw.println("        <stop stop-color=\"" + color0 + "\" stop-opacity=\"" + ((float)colors[0].getAlpha())/255f + "\" offset=\"0%\" />");
                pw.println("        <stop stop-color=\"" + color2 + "\" stop-opacity=\"" + ((float)colors[2].getAlpha())/255f + "\" offset=\"100%\" />");
                pw.println("      </linearGradient>");
              }
              else if( colors[0].getRGB() == colors[2].getRGB() && colors[1].getRGB() == colors[3].getRGB() )
              {
                String color0 = "rgb(" + colors[0].getRed() + "," + colors[0].getGreen() + "," + colors[0].getBlue() + ")";
                String color1 = "rgb(" + colors[1].getRed() + "," + colors[1].getGreen() + "," + colors[1].getBlue() + ")";

                double x1 = currentTexturePaint.getAnchor().x;
                double y1 = currentTexturePaint.getAnchor().y;
                double x2 = currentTexturePaint.getAnchor().x + currentTexturePaint.getAnchor().width/2;
                double y2 = currentTexturePaint.getAnchor().y;

                if( currentTexturePaint.getType() == TransformableTexturePaint.TYPE_STRETCH )
                  pw.println("      <linearGradient id=\"fill" + i + "\" gradientUnits=\"userSpaceOnUse\" spreadMethod=\"pad\" >");
                else
                  pw.println("      <linearGradient id=\"fill" + i + "\" gradientUnits=\"userSpaceOnUse\" spreadMethod=\"reflect\" x1=\"" + x1 + "\" y1=\"" + y1 + "\" x2=\"" + x2 + "\" y2=\"" + y2 + "\" >");
                pw.println("        <stop stop-color=\"" + color0 + "\" stop-opacity=\"" + ((float)colors[1].getAlpha())/255f + "\" offset=\"0%\" />");
                pw.println("        <stop stop-color=\"" + color1 + "\" stop-opacity=\"" + ((float)colors[0].getAlpha())/255f + "\" offset=\"100%\" />");
                pw.println("      </linearGradient>");
              }
            }
            else if( currentTexturePaint.getKind() == TransformableTexturePaint.KIND_RECTANGULAR )
            {

            }
            else if( currentTexturePaint.getKind() == TransformableTexturePaint.KIND_TEXTURE )
            {

            }
          }
          pw.println("    </defs>");

          pw.println("");

          PathIterator    iterator;
          PaintableShape  currentShape;
          Color           strokePaint;
          AffineTransform originalTransform = null;
          AffineTransform inverseTransform  = null;
          String          matrixString      = null;
          for( int i = 0 ; i < shapes.size() ; i++ )
          {
            currentShape = ((PaintableShape)shapes.get(i));
            fillPaint = currentShape.getFillPaint();

            //transform the shape with the inverse of the fills transform so that when it is rendered they can be transformed correctly together
            if( fillPaint.getClass().equals(Class.forName("TransformableTexturePaint")) )
            {
              currentShape = currentShape.get();
              
              try
              {
                originalTransform = ((TransformableTexturePaint)fillPaint).getTransform();
                inverseTransform  = originalTransform.createInverse();
                currentShape.transform(inverseTransform);
              }
              catch( NoninvertibleTransformException nite )
              {

              }
              String transformString = originalTransform.toString();
              String[] splitTransformString0 = new String[3];
              String[] splitTransformString1 = new String[3];

              transformString = transformString.substring(17,transformString.length()-2);

              String split0 = transformString.substring(0,transformString.indexOf("]"));
              String split1 = transformString.substring(transformString.lastIndexOf("[")+1,transformString.length());

              splitTransformString0 = split0.split(",");
              splitTransformString1 = split1.split(",");
              
              matrixString = "matrix(" + splitTransformString0[0].trim() + " " + splitTransformString1[0].trim() + " " + splitTransformString0[1].trim() + " " + splitTransformString1[1].trim() + " " + splitTransformString0[2] + " " + splitTransformString1[2] + ")";
            }
            else
              matrixString = null;

            if( matrixString !=  null )
              pw.println("    <g id=\"set" + i + "\" transform=\"" + matrixString + "\">");
            else
              pw.println("    <g id=\"set" + i + "\">");
            pw.print("      <path id=\"shape" + i + "\" d=\"");
            iterator = currentShape.getFillShape().getPathIterator(null);

            float[] points = new float[6];
            float[] lastPoint = null;
            float[] firstPoint = null;
            int type;
            while( !iterator.isDone() )
            {
              type = iterator.currentSegment(points);
              switch(type)
              {
                case PathIterator.SEG_CLOSE   :
                {
                  pw.print(" Z");
                  break;
                }
                case PathIterator.SEG_CUBICTO :
                {
                  pw.print(" C" + points[0] + " " + points[1] + " " + points[2] + " " + points[3] + " " + points[4] + " " + points[5]);
                  break;
                }
                case PathIterator.SEG_LINETO  :
                {
                  pw.print(" L" + points[0] + " " + points[1]);
                  break;
                }
                case PathIterator.SEG_MOVETO  :
                {
                  pw.print(" M" + points[0] + " " + points[1]);
                  break;
                }
                case PathIterator.SEG_QUADTO  :
                {
                  pw.print(" C" + points[0] + " " + points[1] + " " + points[2] + " " + points[3]);
                  break;
                }
              }
              iterator.next();
            }
            pw.print("\"");

            int fillRule = currentShape.getFillShape().getPathIterator(null).getWindingRule();
            if( fillRule == PathIterator.WIND_EVEN_ODD )
              pw.print(" fill-rule=\"evenodd\"");
            else if( fillRule == PathIterator.WIND_NON_ZERO )
              pw.print(" fill-rule=\"nonzero\"");

            if( fillPaint != null )
            {
              if( fillPaint.getClass().equals(Class.forName("java.awt.Color")) )
              {
                pw.print(" fill=\"rgb(" + ((Color)fillPaint).getRed() + "," + ((Color)fillPaint).getGreen() + "," + ((Color)fillPaint).getBlue() + ")\"");
                pw.print(" fill-opacity=\"" + ((float)((Color)fillPaint).getAlpha())/255f + "\"");
              }
              else if( fillPaint.getClass().equals(Class.forName("TransformableTexturePaint")) )
              {
                if( fillPaintVector.contains(fillPaint) )
                {
                  pw.print(" fill=\"url(#fill" + fillPaintVector.indexOf(fillPaint) + ")\"");
                }
              }
            }

            pw.println(" />");

         Shape strokeShape = currentShape.getDrawShape();
         if( strokeShape != null )
         {
            pw.print("      <path id=\"stroke" + i + "\" d=\"");

            iterator = strokeShape.getPathIterator(null);

            points = new float[6];
            lastPoint = null;
            firstPoint = null;
            while( !iterator.isDone() )
            {
              type = iterator.currentSegment(points);
              switch(type)
              {
                case PathIterator.SEG_CLOSE   :
                {
                  pw.print(" Z");
                  break;
                }
                case PathIterator.SEG_CUBICTO :
                {
                  pw.print(" C" + points[0] + " " + points[1] + " " + points[2] + " " + points[3] + " " + points[4] + " " + points[5]);
                  break;
                }
                case PathIterator.SEG_LINETO  :
                {
                  pw.print(" L" + points[0] + " " + points[1]);
                  break;
                }
                case PathIterator.SEG_MOVETO  :
                {
                  pw.print(" M" + points[0] + " " + points[1]);
                  break;
                }
                case PathIterator.SEG_QUADTO  :
                {
                  pw.print(" C" + points[0] + " " + points[1] + " " + points[2] + " " + points[3]);
                  break;
                }
              }
              iterator.next();
            }

            pw.print("\"");

            strokePaint = (Color)currentShape.getDrawPaint();
            if( strokePaint != null )
            {
              String strokePaintString = strokePaint.getRed() + "," + strokePaint.getGreen() + "," + strokePaint.getBlue();
              pw.print(" fill=\"rgb(" + strokePaintString + ")\"");
              pw.print(" fill-opacity=\"" + ((float)strokePaint.getAlpha())/255f + "\"");
            }
            pw.println(" />");
            pw.println("    </g>");
          }
          }

          pw.println("");

          pw.println("  </g>");
          pw.print("</svg>");
          pw.close();
        } 
        catch( Exception           ioe  ){System.out.println(ioe); ioe.printStackTrace();}
      }
      else
      {
        try
        {
          ImageIO.write( theOOPS.getDrawingArea().getImage(BufferedImage.TYPE_INT_RGB), extension, new File(filename+"."+extension) );
        } 
        catch( java.io.IOException           ioe  ){System.out.println(ioe); }
      }
      theOOPS.getDrawingFrame().setTitle(filename.substring(filename.lastIndexOf(System.getProperty("file.separator"))+1) + "." + extension);
  }
}
