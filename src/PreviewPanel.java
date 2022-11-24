/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Paint;
import java.awt.TexturePaint;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.RenderingHints;
import javax.swing.JPanel;

public class PreviewPanel extends JPanel
{
  private BufferedImage theImage;
  private Paint         theBackgroundPaint;
  private Color         theColor = Color.black;

  public PreviewPanel()
  {

  }

  private void updateImage()
  {
    BufferedImage bi = new BufferedImage(24, 24, BufferedImage.TYPE_INT_RGB);
    Graphics2D big = bi.createGraphics();
    big.setColor(new Color(122,138,153));
    big.fillRect(0,0,12,12);
    big.fillRect(12,12,12,12);
    big.setColor(new Color(238,238,238));
    big.fillRect(12,0,12,12);
    big.fillRect(0,12,12,12);

    Rectangle r = new Rectangle(0,0,24,24);
    theBackgroundPaint = new TexturePaint(bi,r);

    RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                   rh.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);

    theImage = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2D = theImage.createGraphics();
    g2D.addRenderingHints(rh);

    double rectWidth  = ((double)getWidth())/4;
    double rectHeight = (double)getHeight();

    double thickness = Math.min(rectWidth,rectHeight)/6;

    Rectangle2D.Double r2D0 = new Rectangle2D.Double(0,0,rectWidth,rectHeight);
    Rectangle2D.Double r2D1 = new Rectangle2D.Double(thickness,thickness,rectWidth-thickness*2,rectHeight-thickness*2);
    Rectangle2D.Double r2D2 = new Rectangle2D.Double(thickness*2,thickness*2,rectWidth-thickness*4,rectHeight-thickness*4);

    Area a0 = new Area(r2D0);
    a0.subtract(new Area(r2D1));

    g2D.setColor(Color.black);
    g2D.fill(a0);
    g2D.setColor(Color.white);
    g2D.fill(r2D2);


    r2D0 = new Rectangle2D.Double(rectWidth,0,rectWidth,rectHeight);
    r2D1 = new Rectangle2D.Double(rectWidth+thickness,thickness,rectWidth-thickness*2,rectHeight-thickness*2);

    a0 = new Area(r2D0);
    a0.subtract(new Area(r2D1));

    g2D.setColor(Color.white);
    g2D.fill(a0);



    r2D0 = new Rectangle2D.Double(rectWidth*2,0,rectWidth,rectHeight);
    r2D1 = new Rectangle2D.Double(rectWidth*2+thickness,thickness,rectWidth-thickness*2,rectHeight-thickness*2);

    a0 = new Area(r2D0);
    a0.subtract(new Area(r2D1));

    g2D.setColor(Color.black);
    g2D.fill(a0);



    r2D0 = new Rectangle2D.Double(rectWidth*3,0,rectWidth,rectHeight);
    r2D1 = new Rectangle2D.Double(rectWidth*3+thickness,thickness,rectWidth-thickness*2,rectHeight-thickness*2);
    r2D2 = new Rectangle2D.Double(rectWidth*3+thickness*2,thickness*2,rectWidth-thickness*4,rectHeight-thickness*4);

    a0 = new Area(r2D0);
    a0.subtract(new Area(r2D1));

    g2D.setColor(Color.white);
    g2D.fill(a0);
    g2D.setColor(Color.black);
    g2D.fill(r2D2);
  }

  public void paint( Graphics g )
  {
    if( theImage == null || theImage.getWidth() != getWidth() || theImage.getHeight() != getHeight() )
      updateImage();

    Graphics2D g2d = (Graphics2D)g;
    g2d.setPaint(theBackgroundPaint);
    g2d.fillRect(0,0,getWidth(),getHeight());
    g2d.setPaint(theColor);
    g2d.fillRect(0,0,getWidth(),getHeight());
    g2d.drawImage(theImage,0,0,this);
  }

  public void setColor( Color aColor )
  {
    theColor = aColor;
    repaint();
  }
}