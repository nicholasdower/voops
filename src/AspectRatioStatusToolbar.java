/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import java.text.DecimalFormat;

public class AspectRatioStatusToolbar extends JPanel
{
  private JLabel        theWidthField;
  private JLabel        theHeightField;

  private static DecimalFormat theFormat = new DecimalFormat("0");

  public AspectRatioStatusToolbar()
  {
    theWidthField = new JLabel("Width: "); 
    theHeightField = new JLabel("Length: ");

    theWidthField.setEnabled(false);
    theHeightField.setEnabled(false);

    //this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

    this.setLayout(new GridLayout(1,0));

    this.add(theWidthField);
    this.add(theHeightField);
  }

  public void setStatus( double aWidth, double aHeight )
  {
    theWidthField.setText("Width: " + theFormat.format(aWidth));
    theHeightField.setText("Height: " + theFormat.format(aHeight));
    repaint();
  }

  public void resetStatus()
  {
    theWidthField.setText("Width: ");
    theHeightField.setText("Height: ");
  }
}