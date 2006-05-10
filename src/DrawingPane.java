/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Paint;
import java.awt.TexturePaint;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.LineBorder;

public class DrawingPane extends JScrollPane
{  
  private OOPS        theOOPS;

  private DrawingArea theDrawingArea;
  private Point       theDragPoint;

  private JLabel theResizor0;
  private JLabel theResizor1;
  private JLabel theResizor2;

  private Paint theBackgroundPaint;

  public DrawingPane(DrawingArea aDrawingArea, int aWidth, int aHeight, OOPS anOOPS)
  {
    super();

    theOOPS = anOOPS;

    theDrawingArea = aDrawingArea;

    BufferedImage bi = new BufferedImage(24, 24, BufferedImage.TYPE_INT_ARGB); 
    Graphics2D big = bi.createGraphics(); 
    big.setColor(new Color(122,138,153)); 
    big.fillRect(0,0,12,12); 
    big.fillRect(12,12,12,12); 
    big.setColor(new Color(238,238,238)); 
    big.fillRect(12,0,12,12); 
    big.fillRect(0,12,12,12); 

    Rectangle r = new Rectangle(0,0,24,24); 
    theBackgroundPaint = new TexturePaint(bi,r); 

    JPanel client = new JPanel()
    {
      public void paintComponent(Graphics g)
      {
        ((Graphics2D)g).setPaint(theBackgroundPaint);
        g.fillRect(0,0,getWidth(),getHeight());
        super.paintComponent(g);
      }
    };
    client.setOpaque(false);

    JPanel drawingAreaContainer = new JPanel(new BorderLayout());
    drawingAreaContainer.setBorder(new LineBorder(Color.black,1));
    drawingAreaContainer.add(theDrawingArea,BorderLayout.CENTER);

    GridBagLayout gridBagLayout = new GridBagLayout();
    client.setLayout(gridBagLayout);

    GridBagConstraints gbc;

    gbc = new GridBagConstraints();

    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.weightx = 0;
    gbc.weighty = 0;
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gridBagLayout.setConstraints(drawingAreaContainer, gbc);
    client.add(drawingAreaContainer);

    JLabel dummyResizor0 = new JLabel();
    dummyResizor0.setOpaque(true);
    dummyResizor0.setPreferredSize(new Dimension(5,5));
    dummyResizor0.setBackground(Color.black);
    dummyResizor0.setEnabled(false);
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 0;
    gbc.weighty = 0;
    //gbc.anchor = GridBagConstraints.LINE_START;
    gridBagLayout.setConstraints(dummyResizor0, gbc);
    client.add(dummyResizor0);

    JLabel dummyResizor1 = new JLabel();
    dummyResizor1.setOpaque(true);
    dummyResizor1.setPreferredSize(new Dimension(5,5));
    dummyResizor1.setBackground(Color.black);
    dummyResizor1.setEnabled(false);
    gbc.gridx = 2;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.anchor = GridBagConstraints.LINE_START;
    gridBagLayout.setConstraints(dummyResizor1, gbc);
    client.add(dummyResizor1);

    JLabel dummyResizor2 = new JLabel();
    dummyResizor2.setOpaque(true);
    dummyResizor2.setPreferredSize(new Dimension(5,5));
    dummyResizor2.setBackground(Color.black);
    dummyResizor2.setEnabled(false);
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.weightx = 0;
    gbc.weighty = 1;
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gridBagLayout.setConstraints(dummyResizor2, gbc);
    client.add(dummyResizor2);

    theResizor0 = new JLabel();
    theResizor0.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
    theResizor0.setOpaque(true);
    theResizor0.setPreferredSize(new Dimension(5,5));
    theResizor0.setBackground(Color.black);
    gbc.gridx = 2;
    gbc.gridy = 1;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.anchor = GridBagConstraints.LINE_START;
    gridBagLayout.setConstraints(theResizor0, gbc);
    client.add(theResizor0);

    theResizor1 = new JLabel();
    theResizor1.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
    theResizor1.setOpaque(true);
    theResizor1.setPreferredSize(new Dimension(5,5));
    theResizor1.setBackground(Color.black);
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.weightx = 0;
    gbc.weighty = 1;
    gbc.anchor = GridBagConstraints.PAGE_START;
    gridBagLayout.setConstraints(theResizor1, gbc);
    client.add(theResizor1);

    theResizor2 = new JLabel();
    theResizor2.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
    theResizor2.setOpaque(true);
    theResizor2.setPreferredSize(new Dimension(5,5));
    theResizor2.setBackground(Color.black);
    gbc.gridx = 2;
    gbc.gridy = 2;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gridBagLayout.setConstraints(theResizor2, gbc);
    client.add(theResizor2);

    client.setBackground(new Color(100,100,100));
    theDrawingArea.setBackground(new Color(255,255,255));

    MouseAdapter mouseAdapter = new MouseAdapter()
    {
      public void mousePressed( MouseEvent e )
      {
        theDragPoint = ((JLabel)e.getSource()).getLocationOnScreen();
        theDragPoint.x += e.getX();
        theDragPoint.y += e.getY();
      }

      public void mouseReleased( MouseEvent e )
      {
        theOOPS.setStatus("");
      }
    };

    MouseMotionAdapter motionAdapter = new MouseMotionAdapter()
    {
      public void mouseDragged( MouseEvent e )
      {
        Point newPoint = ((JLabel)e.getSource()).getLocationOnScreen();
        newPoint.x += e.getX();
        newPoint.y += e.getY();

        int width  = newPoint.x - theDragPoint.x;
        int height = newPoint.y - theDragPoint.y;

        int imageWidth = 0;
        int imageHeight = 0;

        if( e.getSource().equals(theResizor0) )
        {
          imageWidth  = theDrawingArea.getImageSize().width+(int)(width/theDrawingArea.getZoom());
          imageHeight = theDrawingArea.getImageSize().height;
        }
        else if( e.getSource().equals(theResizor1) )
        {
          imageWidth  = theDrawingArea.getImageSize().width;
          imageHeight = theDrawingArea.getImageSize().height+(int)(height/theDrawingArea.getZoom());
        }
        else if( e.getSource().equals(theResizor2) )
        {
          imageWidth  = theDrawingArea.getImageSize().width+(int)(width/theDrawingArea.getZoom());
          imageHeight = theDrawingArea.getImageSize().height+(int)(height/theDrawingArea.getZoom());
        }
        else
          return;


        if( imageHeight < 1 )
          imageHeight = 1;
        if( imageWidth < 1 )
          imageWidth = 1;

        theOOPS.setStatus("W: " + imageWidth + "\nH: " + imageHeight);

        theDrawingArea.setImageSize(imageWidth,imageHeight);

        theDragPoint = newPoint;
        revalidate();
        repaint();
      }
    };

    theResizor0.addMouseMotionListener(motionAdapter);
    theResizor0.addMouseListener(mouseAdapter);
    theResizor1.addMouseMotionListener(motionAdapter);
    theResizor1.addMouseListener(mouseAdapter);
    theResizor2.addMouseMotionListener(motionAdapter);
    theResizor2.addMouseListener(mouseAdapter);

    this.setImageSize(aWidth,aHeight);
    this.setViewportView(client);
  }

  public void setImageSize( int aWidth, int aHeight)
  {
    theDrawingArea.setImageSize(aWidth,aHeight);
  }
}