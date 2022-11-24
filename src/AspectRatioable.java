/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
public interface AspectRatioable
{
  public void setAspectRatio( double anAspectRatio );
  public void shouldPreserveRatio( boolean aShouldPreserveRatio );
  public void add(double anX, double aY, double aWidth, double aHeight);
}