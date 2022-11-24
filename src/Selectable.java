/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.util.Vector;

public interface Selectable
{
  public void rotate( double aTheta);
  public void resetRotation();
  public void resize( double anXFactor, double aYFactor, double aResizor );
  public void skew( double anXFactor, double aYFactor, double aResizor );
  public void delete();
  public void subtract();
  public void intersect();
  public void combine();
  public void redraw();
  public void group();
  public void deGroup();
  public void moveBack();
  public void moveUp();
  public void moveToBack();
  public void moveToFront();
  public void moveBackALayer();
  public void moveUpALayer();
  public void flipHorizontal();
  public void flipVertical();
  public Vector getSelected();
  public void align( int aType );
}