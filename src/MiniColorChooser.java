/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import javax.swing.JPanel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.Color;
import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

public class MiniColorChooser extends JPanel
{
  private SliderSpinnerPanel theSliders;
  private HSLChooser         theHSL;
  private PreviewPanel       thePreview;

  private boolean isChanging = false;


  public MiniColorChooser()
  {
    this(RandomColor.generate());
  }

  public MiniColorChooser( Color aColor )
  {
    theHSL     = new HSLChooser();
    theSliders = new SliderSpinnerPanel();
    thePreview = new PreviewPanel();

    theHSL.addChangeListener
    (
      new ChangeListener()
      {
        public void stateChanged( ChangeEvent e )
        {
          if( isChanging )
            return;

          isChanging = true;
          theSliders.setHSL(theHSL.getHSL());
          isChanging = false;
          thePreview.setColor(theSliders.getColor());
        }
      }
    );

    theSliders.addChangeListener
    (
      new ChangeListener()
      {
        public void stateChanged( ChangeEvent e )
        {
          if( isChanging )
            return;

          isChanging = true;
          theHSL.setHSL(theSliders.getHSL());
          isChanging = false;
          thePreview.setColor(theSliders.getColor());
        }
      }
    );

    GridBagLayout gridBagLayout = new GridBagLayout();
    this.setLayout(gridBagLayout);

    GridBagConstraints gbc;

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridheight = GridBagConstraints.REMAINDER;
    gbc.gridwidth = 1;
    gbc.weightx = .25;
    gbc.weighty = 1;
    gbc.insets = new Insets(2,2,2,0);
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.BOTH;
    gridBagLayout.setConstraints(theHSL, gbc);
    this.add(theHSL);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.gridheight = 1;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.weightx = .5;
    gbc.weighty = 0;
    gbc.insets = new Insets(2,2,2,2);
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gridBagLayout.setConstraints(theSliders, gbc);
    this.add(theSliders);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.gridheight = 1;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.weightx = .5;
    gbc.weighty = 1;
    gbc.insets = new Insets(5,5,5,5);
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.BOTH;
    gridBagLayout.setConstraints(thePreview, gbc);
    this.add(thePreview);

    setColor(aColor);
  }

  public void setColor(Color aColor)
  {
    theSliders.setColor(aColor);
    isChanging = true;
    theHSL.setColor(aColor);
    isChanging = false;
  }

  public Color getColor()
  {
    return theSliders.getColor();
  }
}