/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import javax.swing.ListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import java.awt.Component;
import java.util.Vector;
import java.awt.Font;
import javax.swing.BoxLayout;

public class FontRenderer implements ListCellRenderer
{
  private Vector theFonts;
  public FontRenderer( Vector someFonts )
  {
    theFonts = someFonts;
  }

  public Component getListCellRendererComponent(JList list,Object value,int index,boolean isSelected,boolean cellHasFocus)
  {
    int selectedIndex = ((Integer)value).intValue();

    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
    panel.setOpaque(true);

    if(isSelected)
    {
      panel.setBackground(list.getSelectionBackground());
      panel.setForeground(list.getSelectionForeground());
    }
    else
    {
      panel.setBackground(list.getBackground());
      panel.setForeground(list.getForeground());
    }

    if( !((Font)(theFonts.get(selectedIndex))).canDisplay('a') )
    {
      JLabel title = new JLabel();
      title.setHorizontalAlignment(JLabel.LEFT);
      title.setVerticalAlignment(JLabel.CENTER);
      title.setText(((Font)(theFonts.get(selectedIndex))).getFontName());

      JLabel sample = new JLabel();
      sample.setHorizontalAlignment(JLabel.LEFT);
      sample.setVerticalAlignment(JLabel.CENTER);
      sample.setFont((Font)(theFonts.get(selectedIndex)));
      sample.setText("\uF021\uF085\uF0FF");

      panel.add(title);
      panel.add(sample);

      return panel;
    }
    else
    {
      JLabel title = new JLabel();
      title.setHorizontalAlignment(JLabel.LEFT);
      title.setVerticalAlignment(JLabel.CENTER);
      title.setFont((Font)(theFonts.get(selectedIndex)));
      title.setText(((Font)(theFonts.get(selectedIndex))).getFontName());

      panel.add(title);

      return panel;
    }
  }
}