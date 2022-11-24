/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.Point;

public class OOPSCursors
{
  private static final Cursor[] theCursors = getCursors();

  private static final int NUM_OF_CURS = 17;

  private static int index = 0;
  public static Cursor PEN             = theCursors[index++];
  public static Cursor LINE            = theCursors[index++];
  public static Cursor RECTANGLE       = theCursors[index++];
  public static Cursor ROUND_RECTANGLE = theCursors[index++];
  public static Cursor ELLIPSE         = theCursors[index++];
  public static Cursor POLYGON         = theCursors[index++];
  public static Cursor POLYGON_PEN     = theCursors[index++];
  public static Cursor SMOOTH_POLYGON  = theCursors[index++];
  public static Cursor CUBIC_CURVE     = theCursors[index++];
  public static Cursor QUAD_CURVE      = theCursors[index++];
  public static Cursor TEXT            = theCursors[index++];
  public static Cursor MOVE_UP         = theCursors[index++];
  public static Cursor MOVE_DOWN       = theCursors[index++];
  public static Cursor SUB_SELECT      = theCursors[index++];
  public static Cursor SUB_SELECT_FREE = theCursors[index++];
  public static Cursor FILL            = theCursors[index++];
  public static Cursor GETTER          = theCursors[index++];

  private OOPSCursors(){}

  private static Cursor[] getCursors()
  {
    String[] names = new String[]
                     {
                       "PEN_CUR"       ,"LINE_CUR"       ,"RECT_CUR",
                       "ROUND_RECT_CUR","ELLIPSE_CUR"    ,"POLY_CUR",
                       "POLY_PEN_CUR"  ,"SMOOTH_POLY_CUR","CUBE_CUR",
                       "QUAD_CUR"      ,"TEXT_CUR"       ,"MOVE_UP_CUR",
                       "MOVE_DOWN_CUR" ,"SUB_CUR"        ,"SUB_FREE_CUR",
                       "FILL_CUR"      ,"GETTER_CUR"
                     };
    Point[] points = new Point[]
                     {
                       new Point(2,13),new Point(1,1),new Point(1,1),
                       new Point(1,1) ,new Point(1,1),new Point(1,1),
                       new Point(1,1) ,new Point(1,1),new Point(1,1),
                       new Point(1,1) ,new Point(1,1),new Point(1,1),
                       new Point(1,1) ,new Point(1,1),new Point(1,1),
                       new Point(1,14),new Point(1,14)
                     };

    Cursor[] cursors = new Cursor[NUM_OF_CURS];

    Dimension cursorSize     = null;
    try
    {
      cursorSize     = Toolkit.getDefaultToolkit().getBestCursorSize(16,16);
    }
    catch( HeadlessException he )
    {
      System.out.println(he);
      return null;
    }

    for( int i = 0 ; i < NUM_OF_CURS ; i++ )
    {
      Image image = null;

      image = (new ImageIcon(ClassLoader.getSystemClassLoader().getResource(names[i] + ".png"))).getImage();

      BufferedImage sizedImage = new BufferedImage(cursorSize.width,cursorSize.height,BufferedImage.TYPE_INT_ARGB);
      sizedImage.getGraphics().drawImage(image,0,0,null);
      cursors[i] = Toolkit.getDefaultToolkit().createCustomCursor(sizedImage,points[i],names[i]);
    }

    return cursors;
  }
}