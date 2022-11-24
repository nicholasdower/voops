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

public class LineStatusToolbar extends JPanel
{
  private JLabel        theLengthField;
  private JLabel        theDirectionField;

  private static DecimalFormat theFormat = new DecimalFormat("0.00");

  public LineStatusToolbar()
  {
    theLengthField = new JLabel("Length: ");
    theDirectionField = new JLabel("Angle: ");

    theLengthField.setEnabled(false);
    theDirectionField.setEnabled(false);

    //this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

    this.setLayout(new GridLayout(1,0));

    this.add(theLengthField);
    this.add(theDirectionField);
  }

  public void setStatus( double aLength, double anAngle )
  {
    theLengthField.setText("Length: " + theFormat.format(aLength));
    theDirectionField.setText("Angle: " + theFormat.format(anAngle));
    repaint();
  }

  public void resetStatus()
  {
    theLengthField.setText("Length: ");
    theDirectionField.setText("Angle: ");
  }
}