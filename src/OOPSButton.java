/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.Dimension;

public class OOPSButton extends JButton
{
  public OOPSButton( String anImageURL, String aToolTip )
  {
    super();
    this.setHorizontalAlignment(JButton.CENTER);
    this.setVerticalAlignment(JButton.CENTER);
    this.setIcon(new ImageIcon(this.getClass().getClassLoader().getResource(anImageURL)));
    this.setToolTipText(aToolTip);
    this.setPreferredSize(new Dimension(22,22));
  }

  public OOPSButton( ImageIcon anImageIcon, String aToolTip )
  {
    super();
    this.setHorizontalAlignment(JButton.CENTER);
    this.setVerticalAlignment(JButton.CENTER);
    this.setIcon(anImageIcon);
    this.setToolTipText(aToolTip);
    this.setPreferredSize(new Dimension(22,22));
  }
}