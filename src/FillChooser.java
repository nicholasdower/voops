/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class FillChooser extends JTabbedPane
{
  private OOPS theOOPS;

  private Paint              thePaint;
  private MiniColorChooser   theChooser;
  private SolidChooser       theSolidChooser;
  private GradientChooser    theGradientChooser;
  private RadialChooser      theRadialChooser;
  private RectangularChooser theRectangularChooser;
  private TextureChooser     theTextureChooser;

  public FillChooser( MiniColorChooser aChooser, OOPS anOOPS )
  {
    theOOPS = anOOPS;

    this.setTabPlacement(JTabbedPane.BOTTOM);
    this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT );

    theChooser = aChooser;
    theSolidChooser = new SolidChooser(theChooser, "Fill Color:");
    theSolidChooser.setColor(theChooser.getColor());
    this.addTab("",new ImageIcon(this.getClass().getClassLoader().getResource("Solid.png")),theSolidChooser);

    theGradientChooser = new GradientChooser(theChooser);
    this.addTab("",new ImageIcon(this.getClass().getClassLoader().getResource("Gradient.png")),theGradientChooser);

    theRadialChooser = new RadialChooser(theChooser);
    this.addTab("",new ImageIcon(this.getClass().getClassLoader().getResource("Radial.png")),theRadialChooser);

    theRectangularChooser = new RectangularChooser(theChooser);
    this.addTab("",new ImageIcon(this.getClass().getClassLoader().getResource("Rectangular.png")),theRectangularChooser);

    theTextureChooser = new TextureChooser(theOOPS);
    this.addTab("",new ImageIcon(this.getClass().getClassLoader().getResource("Texture.png")),theTextureChooser);

    ChangeListener listener = new ChangeListener()
    {
      public void stateChanged( ChangeEvent e )
      {
        fireStateChanged();
      }
    };

    theSolidChooser.addChangeListener(listener);
    theGradientChooser.addChangeListener(listener);
    theRadialChooser.addChangeListener(listener);
    theRectangularChooser.addChangeListener(listener);
    theTextureChooser.addChangeListener(listener);
  }

  public Paint getPaint()
  {
    if( getSelectedIndex() == 0 )
      return theSolidChooser.getColor();
    else if( getSelectedIndex() == 1 )
      return theGradientChooser.getPaint();
    else if( getSelectedIndex() == 2 )
      return theRadialChooser.getPaint();
    else if( getSelectedIndex() == 3 )
      return theRectangularChooser.getPaint();
    else if( getSelectedIndex() == 4 )
      return theTextureChooser.getPaint();
    else
      return theSolidChooser.getColor();
  }

  public void setPaint( Paint aPaint)
  {
    try
    {
      if( aPaint.getClass().equals(Class.forName("TransformableTexturePaint")) )
      {
        TransformableTexturePaint paint = (TransformableTexturePaint)aPaint;
        if( paint.getKind() == TransformableTexturePaint.KIND_GRADIENT )
        {
          theGradientChooser.setPaint(paint);
 
          setSelectedIndex(1);
        }
        else if( paint.getKind() == TransformableTexturePaint.KIND_RADIAL )
        {
          theRadialChooser.setPaint(paint);
          setSelectedIndex(2);
        }
        else if( paint.getKind() == TransformableTexturePaint.KIND_RECTANGULAR )
        {
          theRectangularChooser.setPaint(paint);
          setSelectedIndex(3);
        }
        else if( paint.getKind() == TransformableTexturePaint.KIND_TEXTURE )
        {
          theTextureChooser.setPaint(paint);
          setSelectedIndex(4);
        }
      }
      else
      {
        setSelectedIndex(0);
        theSolidChooser.setColor((Color)aPaint);
        theChooser.setColor((Color)aPaint);
      }
    }
    catch( ClassNotFoundException cnfe )
    {
      System.out.println(cnfe);
    }
    fireStateChanged();
  }

}