/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.TexturePaint;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.BasicStroke;
import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Cursor;
import java.awt.Point;

public class OOPSToolPanel extends JPanel
{
  private OOPS theOOPS;
  private Tool theCurrentTool;
  private TexturePaint theTexturePaint;
 
  private JTextArea theStatus;

  public OOPSToolPanel(OOPS anOOPS)
  {
    theOOPS = anOOPS;

    BufferedImage bi = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB); 
    Graphics2D big = bi.createGraphics(); 
    big.setColor(new Color(0,0,0,110)); 
    big.fillRect(0,0,5,5); 
    big.fillRect(5,5,5,5); 
    big.setColor(new Color(255,255,255,110)); 
    big.fillRect(5,0,5,5); 
    big.fillRect(0,5,5,5); 

    Rectangle r = new Rectangle(0,0,10,10); 
    theTexturePaint = new TexturePaint(bi,r); 

    OOPSButton penButton             = new OOPSButton("Pen.png","Pencil");
    OOPSButton lineButton            = new OOPSButton("Line.png","Line");
    OOPSButton rectangleButton       = new OOPSButton("Rectangle.png","Rectangle");
    OOPSButton roundRectangleButton  = new OOPSButton("RoundRectangle.png","Round Rectangle");
    OOPSButton ovalButton            = new OOPSButton("Circle.png","Ellipse");
    OOPSButton polygonButton         = new OOPSButton("Polygon.png","Polygon");
    OOPSButton polygonPenButton      = new OOPSButton("PolygonPen.png","Polygon Pen");
    OOPSButton smoothPolygonButton   = new OOPSButton("SmoothPolygon.png","Smooth Polygon");
    OOPSButton cubicCurveButton      = new OOPSButton("CubicCurve.png","Cubic Curve");
    OOPSButton quadCurveButton       = new OOPSButton("QuadCurve.png","Quad Curve");
    OOPSButton manipulateButton      = new OOPSButton("Manipulator.png","Manipulator");
    OOPSButton textButton            = new OOPSButton("Text.png","Text");
    OOPSButton moveButton            = new OOPSButton("Move.png","Move");
    OOPSButton subSelectButton       = new OOPSButton("Subselect.png","Sub-Select");
    OOPSButton subSelectFreeButton   = new OOPSButton("SubselectFree.png","Sub-Select Free");
    OOPSButton fillerButton          = new OOPSButton("Fill.png","Fill");
    OOPSButton clearButton           = new OOPSButton("Clear.png","Clear");
    OOPSButton zoomInButton          = new OOPSButton("ZoomIn.png","Zoom In");
    OOPSButton zoomOutButton         = new OOPSButton("ZoomOut.png","Zoom Out"); 
    OOPSButton shapeGetterButton     = new OOPSButton("ShapeChooser.png","Get Fill/Stroke"); 
    OOPSButton screenColorButton     = new OOPSButton("ScreenChooser.png","Get ScreenColor"); 

    penButton.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          if( theOOPS.getDrawingArea() == null )
            return;
          theOOPS.getDrawingArea().removeMouseListener(theCurrentTool);
          theOOPS.getDrawingArea().removeMouseMotionListener(theCurrentTool);

          if( theCurrentTool != null )
            theCurrentTool.loseControl();
          theCurrentTool = null;
          theCurrentTool = new PenTool(theOOPS.getDrawingArea(),PaintableShape.TYPE_DRAW);

          theOOPS.setToolbar(theCurrentTool.getToolbar(),((OOPSButton)e.getSource()).getIcon(),"Pen Tool");
 

          theCurrentTool.setDrawPaint(theOOPS.getDrawPaint());
          theCurrentTool.setFillPaint(theOOPS.getFillPaint());
          theCurrentTool.setStroke(theOOPS.getStroke());
        }
      }
    );

    lineButton.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          if( theOOPS.getDrawingArea() == null )
            return;
          theOOPS.getDrawingArea().removeMouseListener(theCurrentTool);
          theOOPS.getDrawingArea().removeMouseMotionListener(theCurrentTool);

          if( theCurrentTool != null )
            theCurrentTool.loseControl();
          theCurrentTool = null;
          theCurrentTool = new LineTool(theOOPS.getDrawingArea(),PaintableShape.TYPE_DRAW);

          theOOPS.setToolbar(theCurrentTool.getToolbar(),((OOPSButton)e.getSource()).getIcon(),"Line Tool");


          theCurrentTool.setDrawPaint(theOOPS.getDrawPaint());
          theCurrentTool.setFillPaint(theOOPS.getFillPaint());
          theCurrentTool.setStroke(theOOPS.getStroke());
        }
      }
    );

    rectangleButton.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          if( theOOPS.getDrawingArea() == null )
            return;
          theOOPS.getDrawingArea().removeMouseListener(theCurrentTool);
          theOOPS.getDrawingArea().removeMouseMotionListener(theCurrentTool);

          if( theCurrentTool != null )
            theCurrentTool.loseControl();
          theCurrentTool = null;
          theCurrentTool = new RectangleTool(theOOPS.getDrawingArea(),theOOPS.getType());

          theOOPS.setToolbar(theCurrentTool.getToolbar(),((OOPSButton)e.getSource()).getIcon(),"Rectangle Tool");


          theCurrentTool.setDrawPaint(theOOPS.getDrawPaint());
          theCurrentTool.setFillPaint(theOOPS.getFillPaint());
          theCurrentTool.setStroke(theOOPS.getStroke());
        }
      }
    );

    roundRectangleButton.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          if( theOOPS.getDrawingArea() == null )
            return;
          theOOPS.getDrawingArea().removeMouseListener(theCurrentTool);
          theOOPS.getDrawingArea().removeMouseMotionListener(theCurrentTool);

          if( theCurrentTool != null )
            theCurrentTool.loseControl();
          theCurrentTool = null;
          theCurrentTool = new RoundRectangleTool(theOOPS.getDrawingArea(),theOOPS.getType());

          theOOPS.setToolbar(theCurrentTool.getToolbar(),((OOPSButton)e.getSource()).getIcon(),"Round Rectangle Tool");


          theCurrentTool.setDrawPaint(theOOPS.getDrawPaint());
          theCurrentTool.setFillPaint(theOOPS.getFillPaint());
          theCurrentTool.setStroke(theOOPS.getStroke());
        }
      }
    );

    ovalButton.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          if( theOOPS.getDrawingArea() == null )
            return;
          theOOPS.getDrawingArea().removeMouseListener(theCurrentTool);
          theOOPS.getDrawingArea().removeMouseMotionListener(theCurrentTool);

          if( theCurrentTool != null )
            theCurrentTool.loseControl();
          theCurrentTool = null;
          theCurrentTool = new EllipseTool(theOOPS.getDrawingArea(),theOOPS.getType());

          theOOPS.setToolbar(theCurrentTool.getToolbar(),((OOPSButton)e.getSource()).getIcon(),"Ellipse Tool");


          theCurrentTool.setDrawPaint(theOOPS.getDrawPaint());
          theCurrentTool.setFillPaint(theOOPS.getFillPaint());
          theCurrentTool.setStroke(theOOPS.getStroke());
        }
      }
    );

    polygonButton.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          if( theOOPS.getDrawingArea() == null )
            return;
          theOOPS.getDrawingArea().removeMouseListener(theCurrentTool);
          theOOPS.getDrawingArea().removeMouseMotionListener(theCurrentTool);

          if( theCurrentTool != null )
            theCurrentTool.loseControl();
          theCurrentTool = null;
          theCurrentTool = new PolygonTool(theOOPS.getDrawingArea(),theOOPS.getType());

          theOOPS.setToolbar(theCurrentTool.getToolbar(),((OOPSButton)e.getSource()).getIcon(),"Polygon Tool");


          theCurrentTool.setDrawPaint(theOOPS.getDrawPaint());
          theCurrentTool.setFillPaint(theOOPS.getFillPaint());
          theCurrentTool.setStroke(theOOPS.getStroke());
        }
      }
    );

    polygonPenButton.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          if( theOOPS.getDrawingArea() == null )
            return;
          theOOPS.getDrawingArea().removeMouseListener(theCurrentTool);
          theOOPS.getDrawingArea().removeMouseMotionListener(theCurrentTool);

          if( theCurrentTool != null )
            theCurrentTool.loseControl();

          theCurrentTool = null;
          theCurrentTool = new PolygonPenTool(theOOPS.getDrawingArea(),theOOPS.getType());

          theOOPS.setToolbar(theCurrentTool.getToolbar(),((OOPSButton)e.getSource()).getIcon(),"Polygon Pen Tool");


          theCurrentTool.setDrawPaint(theOOPS.getDrawPaint());
          theCurrentTool.setFillPaint(theOOPS.getFillPaint());
          theCurrentTool.setStroke(theOOPS.getStroke());
        }
      }
    );

    smoothPolygonButton.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          if( theOOPS.getDrawingArea() == null )
            return;
          theOOPS.getDrawingArea().removeMouseListener(theCurrentTool);
          theOOPS.getDrawingArea().removeMouseMotionListener(theCurrentTool);

          if( theCurrentTool != null )
            theCurrentTool.loseControl();
          theCurrentTool = null;
          theCurrentTool = new SmoothPolygonTool(theOOPS.getDrawingArea(),theOOPS.getType());



          theOOPS.setToolbar(theCurrentTool.getToolbar(),((OOPSButton)e.getSource()).getIcon(),"Smooth Polygon Tool");

          theCurrentTool.setDrawPaint(theOOPS.getDrawPaint());
          theCurrentTool.setFillPaint(theOOPS.getFillPaint());
          theCurrentTool.setStroke(theOOPS.getStroke());
        }
      }
    );

    cubicCurveButton.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          if( theOOPS.getDrawingArea() == null )
            return;
          theOOPS.getDrawingArea().removeMouseListener(theCurrentTool);
          theOOPS.getDrawingArea().removeMouseMotionListener(theCurrentTool);

          if( theCurrentTool != null )
            theCurrentTool.loseControl();
          theCurrentTool = null;
          theCurrentTool = new CubicCurveTool(theOOPS.getDrawingArea(),theOOPS.getType());

          theOOPS.setToolbar(theCurrentTool.getToolbar(),((OOPSButton)e.getSource()).getIcon(),"Cubic Curve Tool");


          theCurrentTool.setDrawPaint(theOOPS.getDrawPaint());
          theCurrentTool.setFillPaint(theOOPS.getFillPaint());
          theCurrentTool.setStroke(theOOPS.getStroke());
        }
      }
    );

    quadCurveButton.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          if( theOOPS.getDrawingArea() == null )
            return;
          theOOPS.getDrawingArea().removeMouseListener(theCurrentTool);
          theOOPS.getDrawingArea().removeMouseMotionListener(theCurrentTool);

          if( theCurrentTool != null )
            theCurrentTool.loseControl();
          theCurrentTool = null;
          theCurrentTool = new QuadCurveTool(theOOPS.getDrawingArea(),theOOPS.getType());

          theOOPS.setToolbar(theCurrentTool.getToolbar(),((OOPSButton)e.getSource()).getIcon(),"Quad Curve Tool");


          theCurrentTool.setDrawPaint(theOOPS.getDrawPaint());
          theCurrentTool.setFillPaint(theOOPS.getFillPaint());
          theCurrentTool.setStroke(theOOPS.getStroke());
        }
      }
    );

    textButton.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          if( theOOPS.getDrawingArea() == null )
            return;
          theOOPS.getDrawingArea().removeMouseListener(theCurrentTool);
          theOOPS.getDrawingArea().removeMouseMotionListener(theCurrentTool);

          if( theCurrentTool != null )
            theCurrentTool.loseControl();
          theCurrentTool = null;
          theCurrentTool = new TextTool(theOOPS.getDrawingArea(),theOOPS.getType());

          theOOPS.setToolbar(theCurrentTool.getToolbar(),((OOPSButton)e.getSource()).getIcon(),"Text Tool");


          theCurrentTool.setDrawPaint(theOOPS.getDrawPaint());
          theCurrentTool.setFillPaint(theOOPS.getFillPaint());
          theCurrentTool.setStroke(theOOPS.getStroke());
        }
      }
    );

    moveButton.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          if( theOOPS.getDrawingArea() == null )
            return;
          theOOPS.getDrawingArea().removeMouseListener(theCurrentTool);
          theOOPS.getDrawingArea().removeMouseMotionListener(theCurrentTool);

          if( theCurrentTool != null )
            theCurrentTool.loseControl();
          theCurrentTool = null;
          theCurrentTool = new MoveTool(theOOPS.getDrawingArea());

          theOOPS.setToolbar(theCurrentTool.getToolbar(),((OOPSButton)e.getSource()).getIcon(),"Move Tool");


          ((MoveTool)theCurrentTool).setSelectDrawPaint(new Color(255,0,0,100));
          ((MoveTool)theCurrentTool).setSelectFillPaint(theTexturePaint);
          ((MoveTool)theCurrentTool).setSelectStroke(new BasicStroke(0));
        }
      }
    );

    manipulateButton.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          if( theOOPS.getDrawingArea() == null )
            return;
          theOOPS.getDrawingArea().removeMouseListener(theCurrentTool);
          theOOPS.getDrawingArea().removeMouseMotionListener(theCurrentTool);

          if( theCurrentTool != null )
            theCurrentTool.loseControl();
          theCurrentTool = null;
          theCurrentTool = new ManipulateTool(theOOPS.getDrawingArea());

          theOOPS.setToolbar(theCurrentTool.getToolbar(),((OOPSButton)e.getSource()).getIcon(),"Manipulate Tool");
        }
      }
    );

    subSelectButton.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          if( theOOPS.getDrawingArea() == null )
            return;
          theOOPS.getDrawingArea().removeMouseListener(theCurrentTool);
          theOOPS.getDrawingArea().removeMouseMotionListener(theCurrentTool);

          if( theCurrentTool != null )
            theCurrentTool.loseControl();
          theCurrentTool = null;
          theCurrentTool = new SubSelectTool(theOOPS.getDrawingArea());

          theOOPS.setToolbar(theCurrentTool.getToolbar(),((OOPSButton)e.getSource()).getIcon(),"Sub-Select Tool");


          ((SubSelectTool)theCurrentTool).setSelectDrawPaint(new Color(255,0,0,100));
          ((SubSelectTool)theCurrentTool).setSelectFillPaint(theTexturePaint);
          ((SubSelectTool)theCurrentTool).setSelectStroke(new BasicStroke(0));
        }
      }
    );

    subSelectFreeButton.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          if( theOOPS.getDrawingArea() == null )
            return;
          theOOPS.getDrawingArea().removeMouseListener(theCurrentTool);
          theOOPS.getDrawingArea().removeMouseMotionListener(theCurrentTool);

          if( theCurrentTool != null )
            theCurrentTool.loseControl();
          theCurrentTool = null;
          theCurrentTool = new SubSelectFreeTool(theOOPS.getDrawingArea());

          theOOPS.setToolbar(theCurrentTool.getToolbar(),((OOPSButton)e.getSource()).getIcon(),"Sub-Select Free Tool");


          ((SubSelectFreeTool)theCurrentTool).setSelectDrawPaint(new Color(255,0,0));
          ((SubSelectFreeTool)theCurrentTool).setSelectFillPaint(theTexturePaint);
          ((SubSelectFreeTool)theCurrentTool).setSelectStroke(new BasicStroke(0));
        }
      }
    );

    fillerButton.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          if( theOOPS.getDrawingArea() == null )
            return;
          theOOPS.getDrawingArea().removeMouseListener(theCurrentTool);
          theOOPS.getDrawingArea().removeMouseMotionListener(theCurrentTool);

          if( theCurrentTool != null )
            theCurrentTool.loseControl();
          theCurrentTool = null;
          theCurrentTool = new FillTool(theOOPS.getDrawingArea());

          theOOPS.setToolbar(theCurrentTool.getToolbar(),((OOPSButton)e.getSource()).getIcon(),"Fill Tool");


          theCurrentTool.setDrawPaint(theOOPS.getDrawPaint());
          theCurrentTool.setFillPaint(theOOPS.getFillPaint());
          theCurrentTool.setStroke(theOOPS.getStroke());
        }
      }
    );

    clearButton.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          if( theOOPS.getDrawingArea() == null )
            return;

          theOOPS.getDrawingArea().setUndoPoint(new CompleteUndo(theOOPS.getDrawingArea()));

          theOOPS.getDrawingArea().removeMouseListener(theCurrentTool);
          theOOPS.getDrawingArea().removeMouseMotionListener(theCurrentTool); 

          if( theCurrentTool != null )
            theCurrentTool.loseControl();
          theCurrentTool = null;
          theOOPS.getDrawingArea().clearAll();
          theOOPS.getDrawingArea().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
          theOOPS.setToolbar(null);
        }
      }
    );

    zoomInButton.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          if( theOOPS.getDrawingArea() == null )
            return;
          theOOPS.getDrawingArea().setZoom(theOOPS.getDrawingArea().getZoom()*1.5);
          theOOPS.invalidate();
          theOOPS.validate();
          theOOPS.repaint();
        }
      }
    );
    zoomOutButton.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          if( theOOPS.getDrawingArea() == null )
            return;
          if( theOOPS.getDrawingArea().getZoom() >= 1.5 )
          {
            theOOPS.getDrawingArea().setZoom(theOOPS.getDrawingArea().getZoom()/1.5);
            theOOPS.invalidate();
            theOOPS.validate();
            theOOPS.repaint();
          }
        }
      }
    );

    shapeGetterButton.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          if( theOOPS.getDrawingArea() == null )
            return;
          theOOPS.getDrawingArea().removeMouseListener(theCurrentTool);
          theOOPS.getDrawingArea().removeMouseMotionListener(theCurrentTool);

          if( theCurrentTool != null )
            theCurrentTool.loseControl();
          theCurrentTool = null;
          theCurrentTool = new ShapeGetterTool(theOOPS.getDrawingArea(), theOOPS);

          theOOPS.setToolbar(theCurrentTool.getToolbar(),((OOPSButton)e.getSource()).getIcon(),"Shape Getter Tool");
        }
      }
    );


    screenColorButton.addMouseListener
    (
      new MouseAdapter()
      {
        public void mouseReleased( MouseEvent e )
        {
          Getter getter = new Getter(theOOPS);
          Point point = ((OOPSButton)e.getSource()).getLocationOnScreen();
          point.x += e.getX();
          point.y += e.getY();
          theOOPS.getColorChooser().setColor( getter.showGetDialog(theOOPS, point) );
        }
      }
    );

    GridBagLayout gridBagLayout = new GridBagLayout();
    this.setLayout(gridBagLayout);

    GridBagConstraints gbc;

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.weighty = .0769f;
    gbc.ipadx = 3;
    gbc.ipady = 3;
    gbc.insets = new Insets(0,3,3,1);
    gbc.anchor = GridBagConstraints.CENTER;
    gridBagLayout.setConstraints(penButton, gbc);
    this.add(penButton);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.weighty = .0769f;
    gbc.ipadx = 3;
    gbc.ipady = 3;
    gbc.insets = new Insets(0,1,3,3);
    gbc.anchor = GridBagConstraints.CENTER;
    gridBagLayout.setConstraints(lineButton, gbc);
    this.add(lineButton);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = 1;
    gbc.weighty = .0769f;
    gbc.ipadx = 3;
    gbc.ipady = 3;
    gbc.insets = new Insets(0,3,3,1);
    gbc.anchor = GridBagConstraints.CENTER;
    gridBagLayout.setConstraints(rectangleButton, gbc);
    this.add(rectangleButton);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.weightx = 1;
    gbc.weighty = .0769f;
    gbc.ipadx = 3;
    gbc.ipady = 3;
    gbc.insets = new Insets(0,1,3,3);
    gbc.anchor = GridBagConstraints.CENTER;
    gridBagLayout.setConstraints(roundRectangleButton, gbc);
    this.add(roundRectangleButton);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.weightx = 1;
    gbc.weighty = .0769f;
    gbc.ipadx = 3;
    gbc.ipady = 3;
    gbc.insets = new Insets(0,3,3,1);
    gbc.anchor = GridBagConstraints.CENTER;
    gridBagLayout.setConstraints(ovalButton, gbc);
    this.add(ovalButton);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.weightx = 1;
    gbc.weighty = .0769f;
    gbc.ipadx = 3;
    gbc.ipady = 3;
    gbc.insets = new Insets(0,1,3,3);
    gbc.anchor = GridBagConstraints.CENTER;
    gridBagLayout.setConstraints(polygonButton, gbc);
    this.add(polygonButton);


    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.weightx = 1;
    gbc.weighty = .0769f;
    gbc.ipadx = 3;
    gbc.ipady = 3;
    gbc.insets = new Insets(0,3,3,1);
    gbc.anchor = GridBagConstraints.CENTER;
    gridBagLayout.setConstraints(polygonPenButton, gbc);    
    this.add(polygonPenButton);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.weightx = 1;
    gbc.weighty = .0769f;
    gbc.ipadx = 3;
    gbc.ipady = 3;
    gbc.insets = new Insets(0,1,3,3);
    gbc.anchor = GridBagConstraints.CENTER;
    gridBagLayout.setConstraints(smoothPolygonButton, gbc);    
    this.add(smoothPolygonButton);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.weightx = 1;
    gbc.weighty = .0769f;
    gbc.ipadx = 3;
    gbc.ipady = 3;
    gbc.insets = new Insets(0,3,3,1);
    gbc.anchor = GridBagConstraints.CENTER;
    gridBagLayout.setConstraints(cubicCurveButton, gbc);  
    this.add(cubicCurveButton);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 4;
    gbc.weightx = 1;
    gbc.weighty = .0769f;
    gbc.ipadx = 3;
    gbc.ipady = 3;
    gbc.insets = new Insets(0,1,3,3);
    gbc.anchor = GridBagConstraints.CENTER;
    gridBagLayout.setConstraints(quadCurveButton, gbc);
    this.add(quadCurveButton);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 5;
    gbc.weightx = 1;
    gbc.weighty = .0769f;
    gbc.ipadx = 3;
    gbc.ipady = 3;
    gbc.insets = new Insets(0,3,3,1);
    gbc.anchor = GridBagConstraints.CENTER;
    gridBagLayout.setConstraints(moveButton, gbc);
    this.add(moveButton);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 5;
    gbc.weightx = 1;
    gbc.weighty = .0769f;
    gbc.ipadx = 3;
    gbc.ipady = 3;
    gbc.insets = new Insets(0,1,3,3);
    gbc.anchor = GridBagConstraints.CENTER;
    gridBagLayout.setConstraints(manipulateButton, gbc);
    this.add(manipulateButton);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 6;
    gbc.weightx = 1;
    gbc.weighty = .0769f;
    gbc.ipadx = 3;
    gbc.ipady = 3;
    gbc.insets = new Insets(0,3,3,1);
    gbc.anchor = GridBagConstraints.CENTER;
    gridBagLayout.setConstraints(subSelectButton, gbc);
    this.add(subSelectButton);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 6;
    gbc.weightx = 1;
    gbc.weighty = .0769f;
    gbc.ipadx = 3;
    gbc.ipady = 3;
    gbc.insets = new Insets(0,1,3,3);
    gbc.anchor = GridBagConstraints.CENTER;
    gridBagLayout.setConstraints(subSelectFreeButton, gbc);
    this.add(subSelectFreeButton);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 7;
    gbc.weightx = 1;
    gbc.weighty = .0769f;
    gbc.ipadx = 3;
    gbc.ipady = 3;
    gbc.insets = new Insets(0,3,3,1);
    gbc.anchor = GridBagConstraints.CENTER;
    gridBagLayout.setConstraints(fillerButton, gbc);
    this.add(fillerButton);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 7;
    gbc.weightx = 1;
    gbc.weighty = .0769f;
    gbc.ipadx = 3;
    gbc.ipady = 3;
    gbc.insets = new Insets(0,1,3,3);
    gbc.anchor = GridBagConstraints.CENTER;
    gridBagLayout.setConstraints(textButton, gbc);
    this.add(textButton);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 8;
    gbc.weightx = 1;
    gbc.weighty = .0769f;
    gbc.ipadx = 3;
    gbc.ipady = 3;
    gbc.insets = new Insets(0,3,3,1);
    gbc.anchor = GridBagConstraints.CENTER;
    gridBagLayout.setConstraints(zoomInButton, gbc);
    this.add(zoomInButton);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 8;
    gbc.weightx = 1;
    gbc.weighty = .0769f;
    gbc.ipadx = 3;
    gbc.ipady = 3;
    gbc.insets = new Insets(0,1,3,3);
    gbc.anchor = GridBagConstraints.CENTER;
    gridBagLayout.setConstraints(zoomOutButton, gbc);
    this.add(zoomOutButton);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 9;
    gbc.weightx = 1;
    gbc.weighty = .0769f;
    gbc.ipadx = 3;
    gbc.ipady = 3;
    gbc.insets = new Insets(0,3,3,1);
    gbc.anchor = GridBagConstraints.CENTER;
    gridBagLayout.setConstraints(shapeGetterButton, gbc);
    this.add(shapeGetterButton);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 9;
    gbc.weightx = 1;
    gbc.weighty = .0769f;
    gbc.ipadx = 3;
    gbc.ipady = 3;
    gbc.insets = new Insets(0,1,3,3);
    gbc.anchor = GridBagConstraints.CENTER;
    gridBagLayout.setConstraints(screenColorButton, gbc);
    this.add(screenColorButton);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 10;
    gbc.weightx = 1;
    gbc.gridwidth = 2;
    gbc.weighty = .0769f;
    gbc.ipadx = 3;
    gbc.ipady = 3;
    gbc.insets = new Insets(0,3,3,1);
    gbc.anchor = GridBagConstraints.CENTER;
    gridBagLayout.setConstraints(clearButton, gbc);
    this.add(clearButton);

    theStatus = new JTextArea();
    theStatus.setLineWrap(false);
    theStatus.setEditable(false);
    theStatus.setFont(new Font("Times",Font.PLAIN,12));
    theStatus.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    theStatus.setBackground(getBackground());
    theStatus.setText("  ");
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 11;
    gbc.gridwidth = 2;
    gbc.weightx = 1;
    gbc.weighty = .231f;
    gbc.insets = new Insets(1,3,3,3);
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.BOTH;
    gridBagLayout.setConstraints(theStatus, gbc);
    this.add(theStatus);
    theStatus.setRows(3);
  }

  public Tool getCurrentTool()
  {
    return theCurrentTool;
  }

  public void setStatus( String aStatus )
  {
    theStatus.setText(aStatus);
  }
}