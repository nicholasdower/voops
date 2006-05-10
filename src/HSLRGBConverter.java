/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.awt.Color;
import java.lang.Math;

public class HSLRGBConverter
{
  private static final int RGBMAX = 255;
  private static final int HMAX   = 359;
  private static final int SMAX   = 100;
  private static final int LMAX   = 100;

  public static int HSLtoRGB(float h, float s, float l)
  {
    int   result = 0;
    h = h/HMAX;
    s = s/SMAX;
    l = l/LMAX;
    float m2     = l <= 0.5 ? l * (s + 1f): l + s - l * s;
    float m1     = l * 2f - m2;
    int   r      = (int) (255 * HUEtoRGB(m1, m2, h + 1f / 3f));
    int   g      = (int) (255 * HUEtoRGB(m1, m2, h));
    int   b      = (int) (255 * HUEtoRGB(m1, m2, h - 1f / 3f));
    return (new Color(r,g,b)).getRGB();
  }

  private static float HUEtoRGB(float m1, float m2, float h)    
  {
    if (h < 0f)        
    {
      h += 1f;
    }
    if (h > 1f)
    {            
      h -= 1f;
    }
    if (h * 6f < 1f)
    {
      return m1 + (m2 - m1) * h * 6f;
    }
    if (h * 2f < 1f)
    {
      return m2;
    }
    if (h * 3f < 2f)
    {
      return m1 + (m2 - m1) * (2f / 3f - h) * 6f;
    }
    return m1;
  }

  public static float[] RGBtoHSL(int rgb)
  {
    Color color = new Color(rgb);
    return RGBtoHSL(color.getRed(),color.getGreen(),color.getBlue());
  }
  public static float[] RGBtoHSL(int R, int G, int B)
  {
    int cMax, cMin; 
    float H, S, L;
    float Rdelta, Gdelta, Bdelta;

    //lightness
    cMax = Math.max( Math.max(R,G), B );
    cMin = Math.min( Math.min(R,G), B );
    L = ( ((cMax+cMin)*LMAX) + RGBMAX )/(2*RGBMAX);


    if (cMax == cMin) //R==G==B achromatic
    {
      S = 0;
      H = 0;          //Truly undefined
    }
    else              //chromatic
    {
      //saturation
      if (L <= (LMAX/2))
        S = ( ((cMax-cMin)*SMAX) + ((cMax+cMin)/2) ) / (cMax+cMin);
      else
        S = ( ((cMax-cMin)*SMAX) + ((2*RGBMAX-cMax-cMin)/2) )/ (2*RGBMAX-cMax-cMin);

      //hue
      Rdelta = ( ((cMax-R)*(HMAX/6)) + ((cMax-cMin)/2) ) / (cMax-cMin);
      Gdelta = ( ((cMax-G)*(HMAX/6)) + ((cMax-cMin)/2) ) / (cMax-cMin);
      Bdelta = ( ((cMax-B)*(HMAX/6)) + ((cMax-cMin)/2) ) / (cMax-cMin);

      if (R == cMax)
        H = Bdelta - Gdelta;
      else if (G == cMax)
        H = (HMAX/3) + Rdelta - Bdelta;
      else // B == cMax 
        H = ((2*HMAX)/3) + Gdelta - Rdelta;

      if (H < 0)
        H += HMAX;
      if (H > HMAX)
        H -= HMAX;
      }  

    float[] returnArray = new float[3];
    returnArray[0] = H;
    returnArray[1] = S;
    returnArray[2] = L;

    return returnArray;
  }
  
  public static int getMaxHue()
  {
    return HMAX;
  }
  public static int getMaxSat()
  {
    return SMAX;
  }
  public static int getMaxLum()
  {
    return LMAX;
  }
}