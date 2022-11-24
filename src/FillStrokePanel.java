/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class FillStrokePanel extends JTabbedPane
{
  private OOPS theOOPS;

  private Paint theFillPaint;
  private Paint theStrokePaint;
  private FillChooser theFillChooser;
  private StrokeChooser theStrokeChooser;

  public FillStrokePanel( MiniColorChooser aChooser, OOPS anOOPS )
  {
    theOOPS = anOOPS;

    theFillChooser = new FillChooser(aChooser,theOOPS);
    theStrokeChooser = new StrokeChooser(aChooser);
    this.addTab("Fill",theFillChooser);
    this.addTab("Stroke",theStrokeChooser);

    ChangeListener listener = new ChangeListener()
    {
      public void stateChanged( ChangeEvent e )
      {
        fireStateChanged();
      }
    };

    theFillChooser.addChangeListener(listener);
    theStrokeChooser.addChangeListener(listener);
  }

  public Paint getFillPaint()
  {
    return theFillChooser.getPaint();
  }

  public Paint getStrokePaint()
  {
    return theStrokeChooser.getPaint();
  }

  public BasicStroke getStroke()
  {
    return theStrokeChooser.getStroke();
  }


  public void setFillPaint( Paint aPaint)
  {
    theFillChooser.setPaint(aPaint);
  }

  public void setDrawPaint( Paint aPaint )
  {
    theStrokeChooser.setColor(aPaint);
  }

  public void setStroke( BasicStroke aStroke )
  {
    theStrokeChooser.setStroke(aStroke);
  }
}