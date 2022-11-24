/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.ImageIcon;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SelectionToolbar extends JPanel
{
  private JTabbedPane    theTabbedPane;
  private Selectable     theSelectable;
  private RotationPanel  theRotationPanel;
  private ResizePanel    theResizePanel;
  private SkewPanel      theSkewPanel;
  private TransformPanel theTransformPanel;

  private static DecimalFormat theFormat = new DecimalFormat("0.00");

  public SelectionToolbar(Selectable aSelectable )
  {
    theSelectable = aSelectable;
    theTabbedPane = new JTabbedPane();
    theTabbedPane.setTabPlacement(JTabbedPane.TOP);
    theTabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT );

    theTabbedPane.addTab("",new ImageIcon(this.getClass().getClassLoader().getResource("Transform.png")),new TransformPanel(theSelectable));

    theResizePanel = new ResizePanel(theSelectable);
    theResizePanel.addChangeListener
    (
      new ChangeListener()
      {
        double[] returnArray;
        public void stateChanged( ChangeEvent e )
        {
          returnArray = theResizePanel.getLastSize();
          if( returnArray == null )
            return;
          theSelectable.resize(returnArray[0],returnArray[1],returnArray[2]);
        }
      }
    );
    final JCheckBox preserveBox = new JCheckBox("Preserve");
    preserveBox.setSelected(false);
    preserveBox.addActionListener
    (
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          theResizePanel.setShouldPreserve(preserveBox.isSelected());
        }
      }
    );

    JPanel resizeHolder = new JPanel();
    resizeHolder.setLayout(new GridLayout(1,0));
    resizeHolder.add(theResizePanel);
    resizeHolder.add(preserveBox);

    theTabbedPane.addTab("",new ImageIcon(this.getClass().getClassLoader().getResource("Resize.png")),resizeHolder);

    theSkewPanel = new SkewPanel(theSelectable);
    theSkewPanel.addChangeListener
    (
      new ChangeListener()
      {
        double[] returnArray;
        public void stateChanged( ChangeEvent e )
        {
          returnArray = theSkewPanel.getLastSkew();
          if( returnArray == null )
            return;
          theSelectable.skew(returnArray[0],returnArray[1],returnArray[2]);
        }
      }
    );
    theTabbedPane.addTab("",new ImageIcon(this.getClass().getClassLoader().getResource("Skew.png")),theSkewPanel);


    theRotationPanel = new RotationPanel(theSelectable);
    theTabbedPane.addTab("",new ImageIcon(this.getClass().getClassLoader().getResource("Rotate.png")),theRotationPanel);
    
    this.setLayout(new GridLayout(1, 1));
    this.add(theTabbedPane);
    this.setPreferredSize(new Dimension(270,170));
  }

  public boolean shouldPreserve() {
    return theResizePanel.shouldPreserve();
  }
}
