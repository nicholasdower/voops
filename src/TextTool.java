/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.awt.event.MouseEvent;
import java.awt.Point;
import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Paint;
import java.awt.geom.Area;
import java.awt.BasicStroke;
import java.awt.geom.Rectangle2D;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.TextLayout;
import java.awt.Font;
import java.awt.font.LineMetrics;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import javax.swing.JComponent;
import java.awt.Cursor;

public class TextTool extends Tool
{
  private Point              theStartPoint;
  private Rectangle2D.Double theRectangle;

  private static final double theResizorSize = 4;
  private Rectangle2D.Double[] theResizors;
  private int theCurrentResizor;

  private static TextToolbar theToolbar;

  private boolean isTyping   = false;
  private boolean isResizing = false;
  private boolean isMoving   = false;

  private Point theMovePoint;

  private static final BasicStroke theStroke = new BasicStroke(2,BasicStroke.CAP_BUTT,BasicStroke.JOIN_ROUND,10.0f,new float[]{1,2},0.5f);

  public TextTool( DrawingArea aDrawingArea )
  {
    super(aDrawingArea, PaintableShape.TYPE_DRAW);
  }

  public TextTool( DrawingArea aDrawingArea, int aType )
  {
    super(aDrawingArea, aType);

    if( theToolbar == null )
    {
      theToolbar = new TextToolbar();
    }
    theToolbar.addDocumentListener
    (
      new DocumentListener()
      {
        public void changedUpdate( DocumentEvent e){drawText();}
        public void insertUpdate( DocumentEvent e){drawText();}
        public void removeUpdate( DocumentEvent e){drawText();}
      }
    );
    theResizors = new Rectangle2D.Double[8];

    Cursor g = OOPSCursors.FILL;
    Cursor h = OOPSCursors.PEN;

    setCursor(OOPSCursors.TEXT);
  }

  public void mouseMoved( MouseEvent e )
  {
    if( !getIsStarted() )
    {
      if( isTyping )
      {
        theCurrentResizor = -1;
        for( int i = 0 ; i < theResizors.length ; i++ )
        {
          if( theResizors[i] != null && theResizors[i].contains(e.getX(), e.getY()) )
          {
            theCurrentResizor = i;
            break;
          }
        }

        if( theCurrentResizor >= 0 )
        {
          switch(theCurrentResizor)
          {
            case 0 : getDrawingArea().setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR)); break;
            case 1 : getDrawingArea().setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));  break;
            case 2 : getDrawingArea().setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR)); break;
            case 3 : getDrawingArea().setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));  break;
            case 4 : getDrawingArea().setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));  break;
            case 5 : getDrawingArea().setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR)); break;
            case 6 : getDrawingArea().setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));  break;
            case 7 : getDrawingArea().setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR)); break;
          }
        }
        else if( !theRectangle.contains(e.getX(), e.getY()) )
        {
          getDrawingArea().setCursor(OOPSCursors.TEXT);
        }
        else
        {
          getDrawingArea().setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        }

      }
    }
  }

  public void mouseDragged( MouseEvent e )
  {
    if( !getIsStarted() )
      return;

    if( isResizing )
    {
      resize(e.getX(),e.getY());
    }
    else if( isMoving )
    {
      move(e.getX() - theMovePoint.x, e.getY() - theMovePoint.y );
      theMovePoint = new Point(e.getX(),e.getY());
    }
    else
    {
      drawRect(e.getX(),e.getY());
    }
  }

  public void mousePressed( MouseEvent e )
  {
    if( !getIsStarted() )
    {
      if( isTyping )
      {
        theCurrentResizor = -1;
        for( int i = 0 ; i < theResizors.length ; i++ )
        {
          if( theResizors[i] != null && theResizors[i].contains(e.getX(), e.getY()) )
          {
            theCurrentResizor = i;
            break;
          }
        }

        if( theCurrentResizor >= 0 )
        {
          isResizing = true;
          setIsStarted(true);
        }
        else if( !theRectangle.contains(e.getX(), e.getY()) )
        {
          theStartPoint = new Point(e.getX(),e.getY());
          setIsStarted(true);
          commit();
          isTyping = false;
          getDrawingArea().clearDrawingArea();
        }
        else
        {
          theMovePoint = new Point(e.getX(),e.getY());
          isMoving = true;
          setIsStarted(true);
        }
      }
      else
      {
        theStartPoint = new Point(e.getX(),e.getY());
        setIsStarted(true);
      }
    }
    else
    {
      setIsStarted(false);
      getDrawingArea().clearDrawingArea();
    }
  }

  public void loseControl()
  {
    commit();
  }

  public void mouseReleased( MouseEvent e )
  {
    if( !getIsStarted() )
      return;

    setIsStarted(false);

    if( !isTyping )
    {
      if( e.getX() == theStartPoint.x || e.getY() == theStartPoint.y )
        return;

      createResizors();

      isTyping = true;
      drawText();
    }
    else if( isResizing )
    {
      isResizing = false;
    }
    else if( isMoving )
    {
      isMoving = false;
    }
  }

  public void commit()
  {
    if( isTyping )
      super.commit();
    else
      isTyping = false;
  }

  private void drawRect(int x, int y)
  {
    if( theStartPoint.x > x )
    {
      if( theStartPoint.y > y )
      {
        theRectangle = new Rectangle2D.Double(x,y,theStartPoint.x-x,theStartPoint.y-y);
      }
      else
      {
        theRectangle = new Rectangle2D.Double(x,theStartPoint.y,theStartPoint.x-x,y-theStartPoint.y);
      }
    }
    else
    {
      if( theStartPoint.y > y )
      {
        theRectangle = new Rectangle2D.Double(theStartPoint.x,y,x-theStartPoint.x,theStartPoint.y-y);
      }
      else
      {
        theRectangle = new Rectangle2D.Double(theStartPoint.x,theStartPoint.y,x-theStartPoint.x,y-theStartPoint.y);
      }
    }

    getDrawingArea().clearDrawingArea();
    getDrawingArea().getDrawingGraphics().setStroke(theStroke);
    getDrawingArea().getDrawingGraphics().setPaint(Color.black);
    getDrawingArea().getDrawingGraphics().draw(theRectangle);
  }

  private void drawText()
  {
    if( !isTyping )
      return;
    String            text     = theToolbar.getText();
    Area              area     = new Area();
    FontRenderContext frc      = getDrawingArea().getDrawingGraphics().getFontRenderContext();
    Font              font     = theToolbar.getFont();
    LineMetrics       metrics  = font.getLineMetrics(text,frc);
    float             leading  = metrics.getLeading();
    float             ascent   = metrics.getAscent();
    float             descent  = metrics.getDescent();
    float             height   = metrics.getHeight();
    float             baseline = metrics.getBaselineOffsets()[metrics.getBaselineIndex()];

    double x = theRectangle.x + theStroke.getLineWidth();
    double y = theRectangle.y;

    double size   = theToolbar.getFontSize();
    double space  = theToolbar.getFontSpace();

    double curX = x;
    double curY = y + ascent  + theStroke.getLineWidth();

    for( int i = 0 ; i < text.length() ; i++ )
    {
      TextLayout currentLetterLayout = findLetterShape(text.charAt(i),curX,curY,font,frc);
      Area       currentLetterArea   = new Area(currentLetterLayout.getOutline(null));

      Rectangle2D.Double rect = new Rectangle2D.Double();
      rect.setRect(currentLetterArea.getBounds2D());

      if( curX > (theRectangle.width+x-(rect.width)) - theStroke.getLineWidth() ||  text.charAt(i) == '\n')
      {
        curX = x;
        curY += ascent+descent+leading;
      }

      if( curY > (theRectangle.height+y) - theStroke.getLineWidth() )
      {
        break;
      }
      double tx = (curX-rect.x);
      double ty = ((curY-rect.y)+ascent)-(ascent-rect.y);
      currentLetterArea = new Area(AffineTransform.getTranslateInstance(tx,ty).createTransformedShape(currentLetterArea));
      area.add(currentLetterArea);

      curX += rect.width;
      if( text.charAt(i) != '\n' )
        curX += space;
    }
    getShape().setShape(area);
    paintShape();

    getDrawingArea().getDrawingGraphics().setStroke(theStroke);
    getDrawingArea().getDrawingGraphics().setPaint(Color.black);
    getDrawingArea().getDrawingGraphics().draw(theRectangle);
    //getDrawingArea().getDrawingGraphics().setStroke(null);
    for( int i = 0 ; i < theResizors.length ; i++ )
      getDrawingArea().getDrawingGraphics().fill(theResizors[i]);
  }

  private TextLayout findLetterShape(char aLetter, double anX, double aY, Font aFont, FontRenderContext anFRC)
  {
    TextLayout letterShape = new TextLayout(new String(new char[]{aLetter}), aFont ,anFRC );
    return letterShape;
  }

  public void setDrawPaint( Paint aPaint )
  {
    super.setDrawPaint(aPaint);
    if( isTyping )
      drawText();
  }

  public void setFillPaint( Paint aPaint )
  {
    super.setFillPaint(aPaint);
    if( isTyping )
      drawText();
  }

  public void setStroke( BasicStroke aStroke )
  {
    super.setStroke(aStroke);
    if( isTyping )
      drawText();
  }

  private void move(int x, int y)
  {
    theRectangle.x += x;
    theRectangle.y += y;

    createResizors();
    drawText();
  }

  private void createResizors()
  {
    theResizors[0] = new Rectangle2D.Double(theRectangle.x-(theResizorSize/2),theRectangle.y-(theResizorSize/2),theResizorSize,theResizorSize);
    theResizors[1] = new Rectangle2D.Double((theRectangle.x+theRectangle.width/2)-(theResizorSize/2),theRectangle.y-(theResizorSize/2),theResizorSize,theResizorSize);
    theResizors[2] = new Rectangle2D.Double((theRectangle.x+theRectangle.width)-(theResizorSize/2),theRectangle.y-(theResizorSize/2),theResizorSize,theResizorSize);
    theResizors[3] = new Rectangle2D.Double(theRectangle.x-(theResizorSize/2),(theRectangle.y+theRectangle.height/2)-(theResizorSize/2),theResizorSize,theResizorSize);
    theResizors[4] = new Rectangle2D.Double((theRectangle.x+theRectangle.width)-(theResizorSize/2),(theRectangle.y+theRectangle.height/2)-(theResizorSize/2),theResizorSize,theResizorSize);
    theResizors[5] = new Rectangle2D.Double(theRectangle.x-(theResizorSize/2),(theRectangle.y+theRectangle.height)-(theResizorSize/2),theResizorSize,theResizorSize);
    theResizors[6] = new Rectangle2D.Double((theRectangle.x+theRectangle.width/2)-(theResizorSize/2),(theRectangle.y+theRectangle.height)-(theResizorSize/2),theResizorSize,theResizorSize);
    theResizors[7] = new Rectangle2D.Double((theRectangle.x+theRectangle.width)-(theResizorSize/2),(theRectangle.y+theRectangle.height)-(theResizorSize/2),theResizorSize,theResizorSize);
  }

  private void resize(int x, int y)
  {
    if( theCurrentResizor == 0 )
    {
      theRectangle = new Rectangle2D.Double(x,y,(theRectangle.x-x)+theRectangle.width,(theRectangle.y-y)+theRectangle.height );
    }
    else if( theCurrentResizor == 1 )
    {
      theRectangle = new Rectangle2D.Double(theRectangle.x,y,theRectangle.width,(theRectangle.y-y)+theRectangle.height );
    }
    else if( theCurrentResizor == 2 )
    {
      theRectangle = new Rectangle2D.Double(theRectangle.x,y,x-theRectangle.x,(theRectangle.y-y)+theRectangle.height );
    }
    else if( theCurrentResizor == 3 )
    {
      theRectangle = new Rectangle2D.Double(x,theRectangle.y,(theRectangle.x-x)+theRectangle.width,theRectangle.height );
    }
    else if( theCurrentResizor == 4 )
    {
      theRectangle = new Rectangle2D.Double(theRectangle.x,theRectangle.y,x-theRectangle.x,theRectangle.height );
    }
    else if( theCurrentResizor == 5 )
    {
      theRectangle = new Rectangle2D.Double(x,theRectangle.y,(theRectangle.x-x)+theRectangle.width,y-theRectangle.y );
    }
    else if( theCurrentResizor == 6 )
    {
      theRectangle = new Rectangle2D.Double(theRectangle.x,theRectangle.y,theRectangle.width,y-theRectangle.y );
    }
    else if( theCurrentResizor == 7 )
    {
      theRectangle = new Rectangle2D.Double(theRectangle.x,theRectangle.y,x-theRectangle.x,y-theRectangle.y );
    }

    if( theRectangle.width < 0 )
    {
      theRectangle = new Rectangle2D.Double(theRectangle.x+theRectangle.width,theRectangle.y,-theRectangle.width, theRectangle.height);
      if( theCurrentResizor == 3 )
        theCurrentResizor = 4;
      else if( theCurrentResizor == 4 )
        theCurrentResizor = 3;
      else if( theCurrentResizor == 0 )
        theCurrentResizor = 2;
      else if( theCurrentResizor == 2 )
        theCurrentResizor = 0;
      else if( theCurrentResizor == 5 )
        theCurrentResizor = 7;
      else if( theCurrentResizor == 7 )
        theCurrentResizor = 5;
    }
    if( theRectangle.height < 0 )
    {
      theRectangle = new Rectangle2D.Double(theRectangle.x,theRectangle.y+theRectangle.height,theRectangle.width,-theRectangle.height);
      if( theCurrentResizor == 0 )
        theCurrentResizor = 5;
      else if( theCurrentResizor == 5 )
        theCurrentResizor = 0;
      else if( theCurrentResizor == 1 )
        theCurrentResizor = 6;
      else if( theCurrentResizor == 6 )
        theCurrentResizor = 1;
      else if( theCurrentResizor == 2 )
        theCurrentResizor = 7;
      else if( theCurrentResizor == 7 )
        theCurrentResizor = 2;
    }
    createResizors();
    drawText();
  }

  public JComponent getToolbar()
  {
    return theToolbar;
  }

  public void setType( int aType )
  {
    super.setType(aType);
    if( isTyping )
      drawText();
  }
}