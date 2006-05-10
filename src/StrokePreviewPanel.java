/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import javax.swing.JPanel;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Stroke;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.geom.GeneralPath;
import java.awt.RenderingHints;

public class StrokePreviewPanel extends JPanel
{
  private BufferedImage  theImage;
  private Stroke         theStroke;
  private GeneralPath    theShape;

  private RenderingHints theHints;

  public StrokePreviewPanel( Stroke aStroke )
  {
    theStroke = aStroke;
    theHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    theHints.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
  }

  private void createImage()
  {
    theImage = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_ARGB);
  }

  private void updateImage()
  {
    if( theImage == null )
      createImage();
    Graphics2D g2D = theImage.createGraphics();
    g2D.addRenderingHints(theHints);

    theShape = new GeneralPath();
    float inset = Math.max(((float)getWidth())/6f,((float)getHeight())/6f);

    theShape.moveTo(inset,inset);
    theShape.lineTo(getWidth()-inset,((float)getHeight())/2f);
    theShape.lineTo(inset,getHeight()-inset);

    g2D.setColor(getBackground());
    g2D.fillRect(0,0,getWidth(),getHeight());
    g2D.setColor(Color.black);
    g2D.setStroke(theStroke);
    g2D.draw(theShape);
    g2D.dispose();
  }

  public void paint( Graphics g )
  {
    if( theImage == null || theImage.getWidth() != getWidth() || theImage.getHeight() != getHeight() )
    {
      createImage();
      updateImage();
    }
    g.drawImage(theImage,0,0,this);
  }

  public void setStroke(Stroke aStroke)
  {
    theStroke = aStroke;
    updateImage();
    repaint();
  }
}