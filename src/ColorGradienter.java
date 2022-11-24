/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.awt.Color;

public class ColorGradienter
{
  public static Color[] gradient( Color aColor0, Color aColor1, int aLength )
  {
    Color[] gradient = new Color[aLength];

    int redChange   = aColor1.getRed()   - aColor0.getRed();
    int greenChange = aColor1.getGreen() - aColor0.getGreen();
    int blueChange  = aColor1.getBlue()  - aColor0.getBlue();
    int alphaChange = aColor1.getAlpha() - aColor0.getAlpha();

    double redStep   = ((double)redChange)   / ((double)aLength);
    double greenStep = ((double)greenChange) / ((double)aLength);
    double blueStep  = ((double)blueChange)  / ((double)aLength);
    double alphaStep = ((double)alphaChange) / ((double)aLength);

    double curRed   = aColor0.getRed();
    double curGreen = aColor0.getGreen();
    double curBlue  = aColor0.getBlue();
    double curAlpha = aColor0.getAlpha();

    for( int i = 0 ; i < aLength-1 ; i++ )
    {
      gradient[i] = new Color((int)curRed,(int)curGreen,(int)curBlue,(int)curAlpha);
      curRed   += redStep;
      curGreen += greenStep;
      curBlue  += blueStep;
      curAlpha += alphaStep;
    }
    gradient[aLength-1] = aColor1;

    return gradient;
  }
}