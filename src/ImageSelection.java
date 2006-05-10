/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.awt.datatransfer.*;
import java.awt.*;
import java.io.*;

public class ImageSelection implements Transferable {
  private Image theImage;
    
  public ImageSelection(Image image) 
  {
    this.theImage = image;
  }
    
  // Returns supported flavors
  public DataFlavor[] getTransferDataFlavors() 
  {
    return new DataFlavor[]{DataFlavor.imageFlavor};
  }
    
  // Returns true if flavor is supported
  public boolean isDataFlavorSupported(DataFlavor flavor) 
  {
    return DataFlavor.imageFlavor.equals(flavor);
  }
    
  // Returns image
  public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException 
  {
    if (!DataFlavor.imageFlavor.equals(flavor)) 
    {
      throw new UnsupportedFlavorException(flavor);
    }
    return theImage;
  }
}