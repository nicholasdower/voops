/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.util.Random;
import java.awt.Color;
public class RandomColor
{
  public static Color generate()
  {
    return generate(255,255,255);
  }
  public static Color generate(int r, int g, int b)
  {
    Random rand = new Random();
    return new Color(rand.nextInt(r+1),rand.nextInt(g+1),rand.nextInt(b+1));
  }
}