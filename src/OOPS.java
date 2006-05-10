/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import javax.swing.border.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.io.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.Vector;

import bsh.Interpreter;
import bsh.util.JConsole;
import bsh.EvalError;

public class OOPS extends JFrame
{
  private DrawingArea    theDrawingArea;
  private JInternalFrame theDrawingFrame;

  private Vector theDrawingVector;

  private OOPSToolPanel  theToolPanel;
  private JInternalFrame theToolFrame;
  private JDesktopPane   theDesktopPane;
  private JInternalFrame theToolbar;
  private MiniColorChooser theColorChooser;
  private JInternalFrame   theChooserFrame;
  private FillStrokePanel  theFillStrokePanel;
  private JInternalFrame   theFillStrokeFrame;
  private LayerChooser     theLayerChooser;
  private JInternalFrame   theLayerFrame;

  private Dimension theToolbarSize    = new Dimension(300,200);
  private Dimension theChooserSize    = new Dimension(260,270);
  private Dimension theFillStrokeSize = new Dimension(260,240);
  private Dimension theLayerSize      = new Dimension(260,145);
  private Dimension theGIFSize        = new Dimension(200,500);
  private Dimension theBeanshellSize  = new Dimension(300,200);

  private JInternalFrame theGIFCreateFrame;
  private GIFCreatePanel theGIFCreater;

  private JInternalFrame theBeanshellFrame;
  private JConsole       theBeanshellConsole;
  private Interpreter    theBeanshellInterpreter;
  private ScriptTool     theScriptTool;

  private RenderingHints theRenderingHints;
  
  public OOPS( String[] args )
  {
    super("VOOPS - NicholasDower@gmail.com 2005");

    JLabel label = new JLabel(new ImageIcon(this.getClass().getClassLoader().getResource("SplashScreen.png")));

    JProgressBar progress = new JProgressBar(0,31);
    progress.setForeground(new Color(35,85,183));
    int progressCount = 1;
    progress.setStringPainted(true);
         
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
    panel.add(label,BorderLayout.CENTER);
    panel.add(progress,BorderLayout.PAGE_END);

    JFrame.setDefaultLookAndFeelDecorated(false);
    JFrame splash = new JFrame();
    splash.setUndecorated(true);
    splash.getContentPane().setLayout(new BorderLayout());
    splash.getContentPane().add(panel,BorderLayout.CENTER);

    splash.pack();

    try
    {
      Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
      splash.setLocation((int)(size.width/2 - splash.getWidth()/2),(int)(size.height/2 - splash.getHeight()/2));
    }
    catch( HeadlessException he ){}
    splash.setVisible(true);
          

    this.setIconImage(new ImageIcon(this.getClass().getClassLoader().getResource("OOPSIcon2.png")).getImage());
    progress.setValue(progressCount++);

    final OOPSMenu menu = new OOPSMenu(this);
    setJMenuBar(menu);
    progress.setValue(progressCount++);

    theRenderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    theRenderingHints.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);

    theDrawingVector = new Vector();
    progress.setValue(progressCount++);

    theDesktopPane = new JDesktopPane();
    this.setContentPane(theDesktopPane);
    progress.setValue(progressCount++);

    theToolPanel = new OOPSToolPanel(this);
    theToolFrame = new JInternalFrame("Tools",false,false,false,true);
    progress.setValue(progressCount++);

    theToolFrame.setFrameIcon(new ImageIcon(this.getClass().getClassLoader().getResource("Tools.png")));
    theToolFrame.getContentPane().add(theToolPanel);
    progress.setValue(progressCount++);

    theToolFrame.pack();
    theToolFrame.setSize(110,420);
    progress.setValue(progressCount++);

    theToolFrame.setLocation(0,0);
    theToolFrame.setVisible(true);
    progress.setValue(progressCount++);

    theToolbar = new JInternalFrame("Current Tool",true,false,false,true);
    theToolbar.setFrameIcon(null);
    theToolbar.setTitle("");
    progress.setValue(progressCount++);

    theToolbar.getContentPane().setLayout(new BorderLayout());
    theToolbar.pack();
    progress.setValue(progressCount++);
 
    theToolbar.setSize(theToolbarSize.width,theToolbarSize.height);
    theToolbar.setVisible(true);
    progress.setValue(progressCount++);

    theColorChooser = new MiniColorChooser();
    progress.setValue(progressCount++);

    theChooserFrame = new JInternalFrame("Chooser",true,false,false,true);
    theChooserFrame.setFrameIcon(new ImageIcon(this.getClass().getClassLoader().getResource("ColorChooser.png")));
    progress.setValue(progressCount++);

    theChooserFrame.getContentPane().add(theColorChooser);
    theChooserFrame.pack();
    progress.setValue(progressCount++);

    theChooserFrame.setSize(theChooserSize.width,theChooserSize.height);
    theChooserFrame.setVisible(true);

    theGIFCreater = new GIFCreatePanel(this);
    theGIFCreateFrame = new JInternalFrame("Animated GIF Creator",true,true,false,true);
    theGIFCreateFrame.setFrameIcon(new ImageIcon(this.getClass().getClassLoader().getResource("GIFCreater.png")));
    theGIFCreateFrame.setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE );
    theGIFCreateFrame.addInternalFrameListener
    (
      new InternalFrameAdapter()
      {
        public void internalFrameClosing( InternalFrameEvent e )
        {
          theGIFCreateFrame.setVisible(false);
        }
      }
    );
    theGIFCreateFrame.getContentPane().add(theGIFCreater);
    theGIFCreateFrame.pack();
    progress.setValue(progressCount++);

    theGIFCreateFrame.setSize(theGIFSize.width,theGIFSize.height);
    theGIFCreateFrame.setLocation(400,0);
    progress.setValue(progressCount++);


    theBeanshellConsole = new JConsole();
    theBeanshellFrame = new JInternalFrame("Beanshell",true,true,false,true);
    theBeanshellFrame.setFrameIcon(new ImageIcon(this.getClass().getClassLoader().getResource("Beanie.png")));
    theBeanshellFrame.setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE );
    theBeanshellFrame.addInternalFrameListener
    (
      new InternalFrameAdapter()
      {
        public void internalFrameClosing( InternalFrameEvent e )
        {
          theBeanshellFrame.setVisible(false);
        }
      }
    );
    theBeanshellFrame.getContentPane().add(theBeanshellConsole);
    theBeanshellFrame.pack();

    theBeanshellFrame.setSize(theBeanshellSize.width,theBeanshellSize.height);
    theBeanshellFrame.setLocation(400,0);


    theLayerChooser = new LayerChooser();
    progress.setValue(progressCount++);

    theLayerFrame = new JInternalFrame("Layers",true,false,false,true);
    theLayerFrame.setFrameIcon(new ImageIcon(this.getClass().getClassLoader().getResource("Layers.png")));
    progress.setValue(progressCount++);

    theLayerFrame.getContentPane().add(theLayerChooser);
    theLayerFrame.pack();
    progress.setValue(progressCount++);

    theLayerFrame.setSize(theLayerSize.width,theLayerSize.height);
    theLayerFrame.setVisible(true);
    progress.setValue(progressCount++);
 
    theFillStrokePanel = new FillStrokePanel(theColorChooser,this);
    progress.setValue(progressCount++);

    theFillStrokeFrame = new JInternalFrame("Fill/Stroke",true,false,false,true);
    progress.setValue(progressCount++);

    theFillStrokeFrame.setFrameIcon(new ImageIcon(this.getClass().getClassLoader().getResource("FillStroke.png")));
    theFillStrokeFrame.getContentPane().add(theFillStrokePanel);
    progress.setValue(progressCount++);

    theFillStrokeFrame.pack();
    theFillStrokeFrame.setSize(theFillStrokeSize.width,theFillStrokeSize.height);
    progress.setValue(progressCount++);

    theFillStrokeFrame.setVisible(true);
    progress.setValue(progressCount++);

    theFillStrokePanel.addChangeListener
    (
      new ChangeListener()
      {
        public void stateChanged( ChangeEvent e )
        {
          if( theToolPanel.getCurrentTool() == null )
            return;
          theToolPanel.getCurrentTool().setFillPaint(theFillStrokePanel.getFillPaint());
          theToolPanel.getCurrentTool().setDrawPaint(theFillStrokePanel.getStrokePaint());
          theToolPanel.getCurrentTool().setStroke(theFillStrokePanel.getStroke());
        }
      } 
    );
    progress.setValue(progressCount++);

    theDesktopPane.add(theToolFrame);
    theDesktopPane.add(theToolbar);
    progress.setValue(progressCount++);

    theDesktopPane.add(theChooserFrame);
    theDesktopPane.add(theFillStrokeFrame);
    progress.setValue(progressCount++);

    theDesktopPane.add(theLayerFrame);
    theDesktopPane.add(theGIFCreateFrame);
    theDesktopPane.add(theBeanshellFrame);
    progress.setValue(progressCount++);

    theDesktopPane.addComponentListener
    (
      new ComponentAdapter()
      {
        public void componentResized(ComponentEvent e)
        {
          resize();
        }
      }
    );
    progress.setValue(progressCount++);

    this.addWindowListener
    (
      new WindowAdapter()
      {
        public void windowClosing( WindowEvent e )
        {
          menu.exit();
        }
      }
    );
    progress.setValue(progressCount++);

    if( args == null || args.length == 0 )
      addDrawingArea();
    progress.setValue(progressCount++);

    splash.setVisible(false);

    JFrame.setDefaultLookAndFeelDecorated(true);
    pack();
    setSize(860,700);
    setLocation(100,50);
    setVisible(true);
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE );

    if( args != null && args.length > 0 )
      menu.openFile(new File(args[0]));

    // Start the Beanshell Interpreter
    theScriptTool = new ScriptTool(this);
    theBeanshellInterpreter = new Interpreter(theBeanshellConsole);
    new Thread(
      new Runnable()
      {
        public void run()
        {
          try
          {
            theBeanshellInterpreter.set("tools",theScriptTool);
            theBeanshellInterpreter.run();
          }
          catch( EvalError e )
          {
            System.out.println("Could Not Load Beanshell\n" +e);
          }
        }
      }
    ).start();
  }

  public void showGIFCreater()
  {
    theGIFCreateFrame.setVisible(true);
    theGIFCreateFrame.moveToFront();
  }

  public void showBeanshell()
  {
    theBeanshellFrame.setVisible(true);
    theBeanshellFrame.moveToFront();
  }

  public JDesktopPane getDesktopPane()
  {
    return theDesktopPane;
  }

  public Tool getCurrentTool()
  {
    return theToolPanel.getCurrentTool();
  }

  public OOPSToolPanel getToolPanel()
  {
    return theToolPanel;
  }

  public RenderingHints getRenderingHints()
  {
    return theRenderingHints;
  }

  public void setSmooth( boolean shouldSmooth )
  {
    if( shouldSmooth )
    {
      theRenderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
      theRenderingHints.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
    }
    else
    {
      theRenderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
      theRenderingHints.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
    }

    for( int i = 0 ; i < theDrawingVector.size() ; i++ )
    {
      ((DrawingArea)theDrawingVector.get(i)).setRenderingHints(theRenderingHints);
      ((DrawingArea)theDrawingVector.get(i)).repaint();
    }
  }

  public void addDrawingArea()
  {
    final DrawingArea area = new DrawingArea(this);
    area.setPreferredSize(new Dimension(300,300));
    addDrawingArea(area, "Untitled" );
  }

  public void addDrawingArea( final DrawingArea anArea, String aTitle )
  {
    theDrawingVector.add(anArea);
    anArea.setOOPS(this);
    anArea.setRenderingHints(theRenderingHints);
    DrawingPane pane = new DrawingPane(anArea,anArea.getPreferredSize().width,anArea.getPreferredSize().height,this);
    if( theDrawingArea != null )
    {
      theDrawingArea.removeMouseListener(theToolPanel.getCurrentTool());
      theDrawingArea.removeMouseMotionListener(theToolPanel.getCurrentTool());
    }
    theDrawingArea = anArea;
    if( theToolPanel.getCurrentTool() != null )
    {
      theDrawingArea.addMouseListener(theToolPanel.getCurrentTool());
      theDrawingArea.addMouseMotionListener(theToolPanel.getCurrentTool());
      theToolPanel.getCurrentTool().setDrawingArea(theDrawingArea);
    }

    theLayerChooser.setDrawingArea(theDrawingArea);

    final JInternalFrame drawingFrame = new JInternalFrame(aTitle,true,true,true,true);
    drawingFrame.setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);

    theDrawingArea.addMouseListener
    (
      new MouseAdapter()
      {
        public void mouseEntered( MouseEvent e )
        {
          if( !theDrawingArea.equals((DrawingArea)e.getSource()) )
            return;
          ((DrawingArea)e.getSource()).requestFocusInWindow();
          try
          {
            drawingFrame.setSelected(true);
            drawingFrame.moveToFront();
          }
          catch( java.beans.PropertyVetoException pve )
          {
            System.out.println(pve);
          }
        }
      }
    );
    drawingFrame.setFrameIcon(new ImageIcon(this.getClass().getClassLoader().getResource("OOPSIcon.png")));
    drawingFrame.getContentPane().add(pane);
    drawingFrame.pack();
    drawingFrame.setSize(400,400);
    if( theDrawingFrame == null )
      drawingFrame.setLocation(120,50);
    else
      drawingFrame.setLocation(theDrawingFrame.getLocation().x + 20,theDrawingFrame.getLocation().y + 20);
    drawingFrame.setVisible(true);

    theDrawingFrame = drawingFrame;

    theDesktopPane.add(drawingFrame);   

    drawingFrame.addInternalFrameListener
    (
      new InternalFrameAdapter()
      {
        public void internalFrameActivated(InternalFrameEvent e)
        {
          theDrawingArea.removeMouseListener(theToolPanel.getCurrentTool());
          theDrawingArea.removeMouseMotionListener(theToolPanel.getCurrentTool());
          theDrawingArea = anArea;
          theDrawingArea.requestFocusInWindow();

          theDrawingFrame = drawingFrame;
          theDrawingFrame.moveToFront();
          if( theToolPanel.getCurrentTool() != null )
          {
            theDrawingArea.addMouseListener(theToolPanel.getCurrentTool());
            theDrawingArea.addMouseMotionListener(theToolPanel.getCurrentTool());
            theToolPanel.getCurrentTool().setDrawingArea(theDrawingArea);
          }
          theLayerChooser.setDrawingArea(theDrawingArea);
        }

        public void internalFrameClosing(InternalFrameEvent e)
        {
          try
          {
            int option = JOptionPane.showConfirmDialog((JInternalFrame)e.getSource(),"Are you sure you want to close this image?","Close?",JOptionPane.YES_NO_OPTION);
            if( option == JOptionPane.YES_OPTION )
              ((JInternalFrame)e.getSource()).dispose();
          }
          catch( HeadlessException he ){}
        }

        public void internalFrameClosed(InternalFrameEvent e)
        {
          theDrawingVector.remove(e.getSource());
          theLayerChooser.clear();
          theLayerChooser.revalidate();
          theLayerChooser.repaint();
          theLayerFrame.moveToFront();
          theLayerFrame.requestFocusInWindow();
        }
      }
    ); 
    drawingFrame.requestFocusInWindow();
    theDrawingFrame.moveToFront();
    theDrawingArea.repaint();
  }

  public void refreshLayerChooser()
  {
    theLayerChooser.setDrawingArea(theDrawingArea);
  }

  public Paint getFillPaint()
  {
    return theFillStrokePanel.getFillPaint();
  }

  public Paint getDrawPaint()
  {
    return theFillStrokePanel.getStrokePaint();
  }

  public BasicStroke getStroke()
  {
    return theFillStrokePanel.getStroke();
  }

  public void setFillPaint( Paint aPaint)
  {
    theFillStrokePanel.setFillPaint(aPaint);
  }

  public void setDrawPaint( Paint aPaint )
  {
    theFillStrokePanel.setDrawPaint(aPaint);
  }

  public void setStroke( BasicStroke aStroke )
  {
    theFillStrokePanel.setStroke(aStroke);
  }

  public int getType()
  {
    return PaintableShape.TYPE_BOTH;
  }

  public DrawingArea getDrawingArea()
  {
    return theDrawingArea;
  }

  public JInternalFrame getDrawingFrame()
  {
    return theDrawingFrame;
  }

  public MiniColorChooser getColorChooser()
  {
    return theColorChooser;
  }

  public void setToolbar(JComponent aComponent)
  {
    theToolbar.getContentPane().removeAll();
    if( aComponent != null )
    {
      theToolbar.getContentPane().add(aComponent,BorderLayout.CENTER);
    }
    theToolbar.setTitle("");
    theToolbar.setFrameIcon(null);
    theToolbar.invalidate();
    theToolbar.validate();
    theToolbar.repaint();
  }
  public void setToolbar(JComponent aComponent, Icon anIcon, String aTitle )
  {
    theToolbar.getContentPane().removeAll();
    if( aComponent != null )
    {
      theToolbar.getContentPane().add(aComponent,BorderLayout.CENTER);
    }
    theToolbar.setTitle(aTitle);
    theToolbar.setFrameIcon(anIcon);
    theToolbar.revalidate();
    theToolbar.repaint();
  }


  public void resize()
  {
    theLayerFrame.setLocation(theDesktopPane.getWidth()-theLayerSize.width,theChooserSize.height+theFillStrokeSize.height);
    theToolbar.setLocation(0,(int)(theDesktopPane.getHeight()-theToolbarSize.getHeight()));
    theBeanshellFrame.setLocation(theToolbarSize.width,theDesktopPane.getHeight()-theBeanshellSize.height);
    theChooserFrame.setLocation(theDesktopPane.getWidth()-theChooserSize.width,0);
    theFillStrokeFrame.setLocation(theDesktopPane.getWidth()-theFillStrokeSize.width,theChooserSize.height);
  }

  public void setStatus( String aStatus )
  {
    theToolPanel.setStatus(aStatus);
  }
}
