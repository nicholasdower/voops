/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class LChooser extends JPanel
{
  private BufferedImage theImage;
  private Color         theColor = Color.black;

  private void updateImage()
  {
    if( getWidth() <= 0 || getHeight() <= 0 )
      return;

    theImage = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_RGB);
    float lStep = 100f/((float)getHeight());

    float[] hsl = HSLRGBConverter.RGBtoHSL(theColor.getRGB());
    for( int i = 0 ; i < getHeight() ; i++ )
    {
      int rgb = HSLRGBConverter.HSLtoRGB(hsl[0],hsl[1],i*lStep);
      for( int j = 0 ; j < getWidth() ; j++ )
      {
        theImage.setRGB(j,(getHeight()-1)-i,rgb);
      }
    }

  }

  public void paint( Graphics g )
  {
    if( theImage == null || theImage.getWidth() != getWidth() || theImage.getHeight() != getHeight() )
      updateImage();

    g.drawImage(theImage,0,0,this);
  }

  public void setColor( Color aColor )
  {
    float[] hsl = HSLRGBConverter.RGBtoHSL(aColor.getRGB());
    float lStep = 100f/((float)getHeight());
    theColor = aColor;
    updateImage();
    repaint();
  }

  public Color getColor()
  {
    return theColor;
  }
}