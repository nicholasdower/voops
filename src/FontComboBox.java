/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.awt.Font;
import javax.swing.JComboBox;
import java.util.Vector;
import java.awt.GraphicsEnvironment;
import javax.swing.JLabel;

public class FontComboBox extends JComboBox
{
  private static boolean       shouldOnlyAlpha;
  private static Vector  theFonts;

  public FontComboBox()
  {
    this(false);
  }

  public FontComboBox( boolean onlyAlpha)
  {
    shouldOnlyAlpha = onlyAlpha;
    findFonts();
    for( int i = 0 ; i < theFonts.size() ; i++ )
      addItem(new Integer(i));
    this.setRenderer(new FontRenderer(theFonts));
  }

  public static Vector findFonts()
  {
    String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

    theFonts = new Vector(fontNames.length);
    for( int i = 0 ; i < fontNames.length ; i++ )
    {
      Font f = new Font(fontNames[i], Font.PLAIN, 12);
      if( shouldOnlyAlpha )
      {
        if( f.canDisplay('a') )
          theFonts.add(f);
      }
      else
      {
        theFonts.add(f);
      }
    }

    return theFonts;
  }

  public Font getFont()
  {
    if( theFonts == null )
      return null;
    if( getSelectedItem() == null )
      return null;
    return (Font)(theFonts.get(((Integer)getSelectedItem()).intValue()));
  }
}