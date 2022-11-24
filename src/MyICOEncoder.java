/*****************************************************************
 * Creator:     Nicholas Dower                                   *
 * Date:        October, 2004                                    *
 * Contact:     NicholasDower@gmail.com                          *
 * Description: Converts an array of Java Images into a          *
 *              Windows Icon. NOTE: I know very little about     *
 *              the ICO format. This code may not be portable.   *
 *                                                               *
 *****************************************************************/

import java.awt.Image;                        //Images are the input to the only public funtion encode
import java.awt.Color;                        //Each pixels Color must be determined
import java.util.Vector;                      //A Vector is used to hold all of the Colors in a particular Image
import java.awt.image.Raster;                 //A raster is used to get A, R, G, and B samples from each pixel
import java.awt.image.ColorModel;             //A ColorModel is used to determine the bits per pixel (bpp) for each Image
import java.awt.image.BufferedImage;          //ColorModels and Rasters can only be used on BufferedImages. Each image must be drawn (drawImage()) onto a BufferedImage 
import javax.imageio.ImageTypeSpecifier;      //An ImageTypeSpecifier is gotten from a BufferedImage then used to get a ColorModel Object

  /**
   *  Encodes Images into an Icon (Probably an ICO file)
   */
  public class MyICOEncoder
  {
    /**
     *  An Icon can contain many images. This is the number
     *  of images that will be found in this Icon. 
     */
    private int               theNumOfImages;

    /**
     *  The images to use.
     */
    private Image[]           theImages;

    /**
     *  A stream to write the Icon to.
     */
    private LEDataOutputStream  theStream;

    /**
     *  This int will hold a four byte value. This value represents
     *  the offset of the current image.
     */
    private int               theCurrentOffset;

    /**
     *  The Vectors of each Images Colors
     */
    private Vector[]          theColors;

    /**
     *  The bit quality of each image
     */
    private int[]             theQualities;

    /**
     *  These should be the only two public functions. They are all that is neccessary
     *  to create an Icon
     */
    public void encode(Image[] someImages, LEDataOutputStream aStream) throws Exception
    {
      encode(someImages,aStream,null);
    }
    public void encode(Image[] someImages, LEDataOutputStream aStream, int[] someBits) throws Exception
    {
        theQualities = someBits;

      // There must be at least one image. Since the number of images
      // must be stored in two bytes there could never be more than 65535
      // NOTE: This is a theoretical maximum not a desirable one.

        theNumOfImages = someImages.length;

        if( theNumOfImages <= 0 || theNumOfImages > 65535)
          throw new Exception("Invalid number of images.");

      // We will need an OutputStream to write to.

        theStream = aStream;

        if( theStream == null )
          throw new Exception("Invalid OutputStream.");

      //Copy images
        theImages = someImages;

      //6 bytes for the Header + 16 bytes for each Images header
        theCurrentOffset = 6 + theNumOfImages*16;

      //Allocate memory for colors
        theColors = new Vector[theNumOfImages];

      writeFileHeader();
      writeIconDirectoryEntries();
      writeImages();
    } //END FUNCTION encode()


    /**
     *  The file header is the first part of an ICO file. It is six bytes
     *  and has the following format:
     *           Reserved  (2 bytes), Always 0
     *           IconType  (2 bytes), 1 for an Icon, 2 for a Cursor
     *           IconCount (2 bytes), Number of images(Icons) = private int theNumOfImages
     */
    private void writeFileHeader()
    {
      try
      {
        theStream.writeShort(0x0000);                //Reserved
        theStream.writeShort(0x0001);                //IconType
        theStream.writeShort(theNumOfImages);        //IconCount
      }
      catch(Exception e)
      {
        System.out.println("Error" + e);
      }
    } //END FUNCTION writeFileHeader()


    /**
     *  For each image there is an entry describing it. The format is:
     *                  Width       (1 byte ), Width of Icon (1 to 255)
     *                  Height      (1 byte ), Height of Icon (1 to 255)
     *                  ColorCount  (1 byte ), Number of colors, 0 for 24 bit or higher, 2 for monochrome, 16 for 16 color images.
     *                  Reserved    (1 byte ), Not used (always 0)
     *                  Planes      (2 bytes), Always 1 for Icons
     *                  BitCount    (2 bytes), Number of bits per pixel (1 for monochrome, 4 for 16 colors, 8 for 256 colors, 24 for true colors, 32 for true colors + alpha channel)
     *                  ImageSize   (4 bytes), Length of resource in bytes 
     *                  ImageOffset (4 bytes), start of the image in the file.
     */
    private void writeIconDirectoryEntries() throws Exception
    {
      //Used to get Pixel Colors
        BufferedImage curImage        = null; 

      try
      {
        //for each image
        for( int i = 0 ; i < theNumOfImages ; i++ )
        {
          //determine height and width of image
          int width  = (theImages[i]).getWidth(null);
          int height = (theImages[i]).getHeight(null);

          //width and height values are stored in one byte
          if( width > 255 || height > 255 )
            throw new Exception("An image may not exceed 255x255 pixels.");

          //Create a buffered image used to determine pixel color
            curImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
            curImage.createGraphics().drawImage(theImages[i],0,0,null);

          theStream.writeByte(width);           //Width
          theStream.writeByte(height);          //Height

          //Determine bitCount and number of colors
            theColors[i] = determineColors(curImage);

            int numberOfColors = 0;
            int bitCount       = 0;

            if( theColors[i] == null )
              bitCount = 32;
            else if( theColors[i].size() == 0 )
              bitCount = 24;
            else
              numberOfColors = theColors[i].size();

            if( numberOfColors == 0 )      //0 means there are more than 256 colors
            {
                                           //do nothing
            }                             
            else if( numberOfColors > 0 && numberOfColors <= 2 )    //Monochrome
            {
              numberOfColors = 2;
              bitCount       = 1;
            }
            else if( numberOfColors > 2 && numberOfColors <= 16 )   //16 Colors
            {
              numberOfColors = 16;
              bitCount       = 4;
            } 
            else if( numberOfColors > 16 && numberOfColors <= 256 ) //256 Colors
            {
              numberOfColors = 256; 
              bitCount       = 8;
            }

          if( theQualities != null )
          {
            if( theQualities[i] > bitCount )
            {
              bitCount = theQualities[i];
              if( bitCount < 24 )
              {
                numberOfColors = (int)Math.pow(2,bitCount);
              }
              else
              {
                numberOfColors = 0;
                if( bitCount == 24 )
                  theColors[i].clear();
                else if( bitCount == 32 )
                  theColors[i] = null;
              }
            }
          }

          theStream.writeByte(numberOfColors); //ColorCount
          theStream.writeByte(0);              //Not used
          theStream.writeShort(1);             //Planes
          theStream.writeShort(bitCount);     

          //Determine size of this image

            //Determine the size of the Palette It will be 0 if numberOfColors is 0 (24 bit or 32 bit)
              int Pbytes = (int)numberOfColors*4;  //Four bytes for each color(R, G, B, and *NOT USED*/Alpha)

            //Determine the number of bytes needed per row for the XOR Map
              int Xbytes = (int)((((float)width*bitCount)%8 == 0)?(((float)width*bitCount)/8):(((float)width*bitCount)/8)+1);
  
              if( (float)Xbytes%4 == 0)         //Xbytes must be divisable by 4
              {}
              else                              //If not add padding
              {
                while( (float)Xbytes%4 != 0 )
                  Xbytes += 1;
              }

            //Determine the number of bytes needed per row for the AND Map
              int Abytes = (int)((((float)width)%8 == 0)?(((float)width)/8):(((float)width)/8)+1);

              if( (float)Abytes%4 == 0)         //Abytes must be divisable by 4
              {}
              else                              //If not add padding
              {
                while( (float)Abytes%4 != 0 )
                  Abytes += 1;
              }
            // length is the size of this image.
            //   It is calculated by adding the size of the BitmapHeader(Always 40) + the size of the Palette(Pbytes)
            //   + the size of the XOR Map +  the size of the AND Map
              int length = 40 + Pbytes + Xbytes*height + Abytes*height;

          theStream.writeInt(length);                   //ImageSize
          theStream.writeInt(theCurrentOffset);  //ImageOffset

          //The offset should be moved up to the end of this image ( the begining of the next image).
          theCurrentOffset += length;
        }
      }
      catch(Exception e)
      {
        System.out.println("Error writing Icon Directory Entries " );
        e.printStackTrace(System.out);
      }
    } //END FUNCTION writeIconDirectoryEntries()


    /**
     *  BitmapHeader  (40 bytes)
     *      Size            (4 bytes), size of this structure (always 40)
     *      Width           (4 bytes), width of the image (same as iconinfo.width)
     *      Height          (4 bytes), scanlines in the color map + transparent map (iconinfo.height * 2)
     *      Planes          (2 bytes), always 1
     *      BitCount        (2 bytes), 1,4,8,24,32 (see iconinfo for details)
     *      Compression     (4 bytes), we don't use this (0)
     *      SizeImage       (4 bytes), we don't use this (0)
     *      XPelsPerMeter   (4 bytes), we don't use this (0)
     *      YPelsPerMeter   (4 bytes), we don't use this (0)
     *      ColorsUsed      (4 bytes), we don't use this (0)
     *      ColorsImportant (4 bytes), we don't use this (0)
     *
     *  Palette
     *      This will only be used for images containing 256 colors or less
     *
     *  XOR Map (Color)
     *      The actual pixels. Scanlines are used meaning there may be some padding.
     *
     *  AND Map (Transparency)
     *      One bit per pixel (0 for Opaque, 1 for Transparent). Scanlines are used meaning there may be some padding.
     */
    private void writeImages()
    {
      //Used to get Pixel Colors
        BufferedImage curImage        = null; 
        Raster        curImageRaster  = null;

      try
      {
        //For each image
        for( int i = 0 ; i < theNumOfImages ; i++ )
        {
          //Calculate width and height of the image
            int width  = (theImages[i]).getWidth(null);
            int height = (theImages[i]).getHeight(null);

          //Create a buffered image used to determine pixel color
            curImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
            curImage.createGraphics().drawImage(theImages[i],0,0,null);

          //Get a Raster to be used for determining R, G, B, & A
            curImageRaster = curImage.getRaster();

          //Determine bitCount
            int numberOfColors = 0;
            int bitCount       = 32;

            if( theColors[i] == null )
              bitCount = 32;
            else if( theColors[i].size() == 0 )
              bitCount = 24;
            else
              numberOfColors = theColors[i].size();

            if( numberOfColors == 0 )      //0 means there are more than 256 colors
            {
                                           //do nothing
            }                            
            else if( numberOfColors > 0 && numberOfColors <= 2 )    //Monochrome
            {
              numberOfColors = 2;
              bitCount       = 1;
            }
            else if( numberOfColors > 2 && numberOfColors <= 16 )   //16 Colors
            {
              numberOfColors = 16;
              bitCount       = 4;
            } 
            else if( numberOfColors > 16 && numberOfColors <= 256 ) //256 Colors
            {
              numberOfColors = 256; 
              bitCount       = 8;
            }

          if( theQualities != null )
          {
            if( theQualities[i] > bitCount )
            {
              bitCount = theQualities[i];
              if( bitCount < 24 )
              {
                numberOfColors = (int)Math.pow(2,bitCount);
              }
              else
              {
                numberOfColors = 0;
                if( bitCount == 24 )
                  theColors[i].clear();
                else if( bitCount == 32 )
                  theColors[i] = null;
              }
            }
          }

          //BitmapHeader
            theStream.writeInt(40);
            theStream.writeInt(width);          
            theStream.writeInt(height*2); 
            theStream.writeShort(1);
            theStream.writeShort(bitCount); 
            theStream.writeInt(0);              //Not used
            theStream.writeInt(0);              //Not used
            theStream.writeInt(0);              //Not used
            theStream.writeInt(0);              //Not used
            theStream.writeInt(0);              //Not used
            theStream.writeInt(0);              //Not used
 
          //Palette
            //Is a Palette needed?
              
              if( bitCount >= 24 )   //24 bit or 32 bit
              {
                                     //Do not write a Palette it is only used for 256 colors or less
              }
              else                   //256 colors or less
              {
                //Write Palette
                for( int p = 0 ; p < numberOfColors ; p++ )
                {
                  if( p >= theColors[i].size() )   //Ignored part of Palette
                  {
                    theStream.writeInt(0);        //Four dummy bytes
                  }
                  else
                  {
                    theStream.writeByte(((Color)(theColors[i].get(p))).getBlue());
                    theStream.writeByte(((Color)(theColors[i].get(p))).getGreen());
                    theStream.writeByte(((Color)(theColors[i].get(p))).getRed());
                    theStream.writeByte(((Color)(theColors[i].get(p))).getAlpha());
                  }
                }
              }

          //XOR Map (Color)

              int Xbytes = (int)((((float)width*bitCount)%8 == 0)?(((float)width*bitCount)/8):(((float)width*bitCount)/8)+1);

              if( (float)Xbytes%4 == 0)  //Xbytes must be divisable by 4
              {}
              else                       //If not add padding
              {
                while( (float)Xbytes%4 != 0 )
                  Xbytes += 1;
              }

            if( bitCount == 32 )   //32 bit
            {
              //for each row write the actual R, G, B, and A bytes
              for( int j = height-1 ; j >= 0 ; j-- )
              {
                //for each byte including padding
                  for( int l = 0 ; l < Xbytes ; l++ )
                  {
                    if( l >= (width*4) )          //padding
                      theStream.writeByte(0);   //ignore
                    else
                    {
                      // NOTE: I have found that instead of RGB, BGR is expected
                      // I assume this is because of Little Endian. 
 
                      if( l%4 == 0 )           //Blue
                      {
                        //theStream.writeByte((new Color(curImage.getRGB(l/4,j))).getBlue());
                        theStream.writeByte(curImageRaster.getSample(l/4,j,2));               
                      }
                      else if( l%4 == 1 )      //Green
                        theStream.writeByte(curImageRaster.getSample(l/4,j,1));
                      else if( l%4 == 2 )      //Red
                        theStream.writeByte(curImageRaster.getSample(l/4,j,0));
                      else if( l%4 == 3 )      //Alpha
                      {
                        theStream.writeByte(curImageRaster.getSample(l/4,j,3));
                        //System.out.print(curImageRaster.getSample(l/4,j,3) + " "); 
                      }
                    }
                  }
              }
            }
            else if( bitCount == 24 )   //24 bit
            {
              //for each row write the actual R, G, B bytes
              for( int j = height-1 ; j >= 0 ; j-- )
              {
                //for each byte including padding
                  for( int l = 0 ; l < Xbytes ; l++ )
                  {
                    if( l >= (width*3) )          //padding
                      theStream.writeByte(0);   //ignore
                    else
                    {
                      // NOTE: I have found that instead of RGB, BGR is expected
                      // I assume this is because of Little Endian. 
 
                      if( l%3 == 0 )           //Blue
                      {
                        //theStream.writeByte((new Color(curImage.getRGB(l/4,j))).getBlue());
                        theStream.writeByte(curImageRaster.getSample(l/3,j,2));               
                      }
                      else if( l%3 == 1 )      //Green
                        theStream.writeByte(curImageRaster.getSample(l/3,j,1));
                      else if( l%3 == 2 )      //Red
                        theStream.writeByte(curImageRaster.getSample(l/3,j,0));
                    }
                  }
              }
            }
            else             //256 or less colors means a Palette is in use
            {
              //NOTE: Different measures are taken based on which of the remaining 3 bitCount possibilities is used
              //      Since it is impossible to write a single bit to a file a bitCOunt of 1 or 4 results in a conversion
              //      of an array of boolean into an array of bytes

              if( bitCount == 1 ) //Monochrome
              {
                boolean[][] bits = new boolean[height][Xbytes*8];

                //for each row write the offset to the color in the Palette
                  int pixelColor;
                  int pixelOffset;
                  for( int j = height-1 ; j >= 0 ; j-- )
                  {
                    //for each bit including padding
                      for( int l = 0 ; l < Xbytes*8 ; l++ )
                      {
                        if( l >= (width) )          //padding
                          bits[j][l] = false;          //ignore
                        else
                        {
                          //The following may be too tricky. Since a Monochrome image can only be black 
                          //and white the Red sample (0) will either be 0(black) or 255(white)
                          pixelColor = curImageRaster.getSample(l,j,0);
                          if( pixelColor == 255 )
                            pixelOffset = theColors[i].indexOf(Color.white);
                          else
                            pixelOffset = theColors[i].indexOf(Color.black);

                          if( pixelOffset == 0 )
                            bits[j][l] = false;
                          else
                            bits[j][l] = true;
                        }
                      }
                  }

                  //Convert bits to bytes then print
                  for( int h = (height-1) ; h >= 0 ; h-- )
                  {
                    byte[] bytes = determineBytes(bits[h]);
                    for( int b = 0 ; b < bytes.length ; b++ )
                    {
                      theStream.writeByte((int)bytes[b]);
                    }
                  }
              }
              else if( bitCount == 4 ) //16 Colors
              {
                //Used to hold a pixels color
                  Color curColor;
                  
                //Since a colors offset is only 4 bits we must write two offsets at once
                //combinedPixelOffset will contain two offsets(1 byte)
                  int   combinedPixelOffset; 

                //for each row write the offset to the color in the Palette  
                  for( int j = height-1 ; j >= 0 ; j-- )
                  {
                    //for each bit including padding
                      for( int l = 0 ; l < Xbytes*2 ; l+=2 )   //Count by two and handle two pixels per iteration
                      {
                        if( l >= (width) )          //padding
                        {
                            theStream.writeByte(0); //ignore
                        }
                        else
                        {
                          //Get first color
                            curColor = new Color
                            (
                              curImageRaster.getSample(l,j,0),
                              curImageRaster.getSample(l,j,1),
                              curImageRaster.getSample(l,j,2),
                              curImageRaster.getSample(l,j,3)
                            );
                          //If not found this is probably a transparent pixel, make it black.
                          //Otherwise print the offset into the Palette
                            if( !theColors[i].contains((Object)curColor) )
                              combinedPixelOffset = 0;
                            else
                              combinedPixelOffset = theColors[i].indexOf((Object)curColor)*2*2*2*2;

                          if( !(l+1 >= (width)) )          //padding
                          {
                            //Get second color
                              curColor = new Color
                              (
                                curImageRaster.getSample(l+1,j,0),
                                curImageRaster.getSample(l+1,j,1),
                                curImageRaster.getSample(l+1,j,2),
                                curImageRaster.getSample(l+1,j,3)
                              );

                            //If not found this is probably a transparent pixel, make it black.
                            //Otherwise print the offset into the Palette
                              if( !theColors[i].contains((Object)curColor) )
                                combinedPixelOffset += 0;
                              else
                                combinedPixelOffset += theColors[i].indexOf((Object)curColor);
                          }

                          //Write the two offsets as one byte
                            theStream.writeByte(combinedPixelOffset);
                        }
                      }
                  }
              }
              else if( bitCount == 8 ) //256 Colors
              {
                //for each row write the offset to the color in the Palette
                  Color curColor;
                  for( int j = height-1 ; j >= 0 ; j-- )
                  {
                    //for each byte including padding
                      for( int l = 0 ; l < Xbytes ; l++ )
                      {
                        if( l >= (width) )          //padding
                          theStream.writeByte(0);   //ignore
                        else
                        {
                          //Determine pixel color
                          curColor = new Color
                          (
                            curImageRaster.getSample(l,j,0),
                            curImageRaster.getSample(l,j,1),
                            curImageRaster.getSample(l,j,2),
                            curImageRaster.getSample(l,j,3)
                          );
    
                          //If not found this is probably a transparent pixel, make it black.
                          //Otherwise print the offset into the Palette
                            if( !theColors[i].contains((Object)curColor) )
                              theStream.writeByte(0);
                            else
                              theStream.writeByte(theColors[i].indexOf((Object)curColor));
                        }
                      }
                  }
              }
            }

          //AND Map (Transparency) - 1 bit per pixel plus padding

            //Determine the number of bytes needed per row
              int Abytes = (int)((((float)width)%8 == 0)?(((float)width)/8):(((float)width)/8)+1);

              if( (float)Abytes%4 == 0)  //Xbytes must be divisable by 4
              {}
              else                       //If not, add padding
              {
                while( (float)Abytes%4 != 0 )
                  Abytes += 1;
              }

            //for each row
              for( int j = (height-1) ; j >= 0 ; j-- )
              {
                //use boolean to represent bits
                  boolean[] bits = new boolean[Abytes*8];

                //for each bit including padding
                  for( int l = 0 ; l < Abytes*8 ; l++ )
                  {
                    if( l >= (width) )          //padding
                      bits[l] = false;          //ignore
                    else
                    {
                      bits[l] = (curImageRaster.getSample(l,j,3) == 0);  //if transparent, bit = 1
                    }
                  }

                //convert bits to bytes then print
                  byte[] bytes = determineBytes(bits);
                  for( int b = 0 ; b < bytes.length ; b++ )
                  {
                    theStream.writeByte(bytes[b]);
                  }
              }
        }
      }
      catch(Exception e)
      {
        System.out.println("Error writing Images");
        e.printStackTrace(System.out);
      }
    } //END FUNCTION writeImages()

    /**
     *  This function gets all of the distinct Colors in a BufferedImage and returns a Vector.
     *  The return value of this function may be a little too tricky.
     *    If there are 256 Colors or less a Vector containing them is returned.
     *    Else if the image is 24 bit a Vector of size 0 is returned.
     *    Else the image is 32 bit and null is returned.
     *  This was created because bit depths are not always determined properly using a ColorModel
     *    and because it is best to know the exact colors used in an image.
     */
    private Vector determineColors( BufferedImage anImage )
    {
      Vector colorVector = new Vector();
      Raster imageRaster = anImage.getRaster();

      //black and white are needed for transparency
        //colorVector.add(new Color(0,0,0,255));
        //colorVector.add(new Color(255,255,255,255));
        colorVector.add(Color.black);
        colorVector.add(Color.white);

      //determine length and width
        int width  = anImage.getWidth();
        int height = anImage.getHeight();

      Color   curColor;
      boolean is256Plus = false;
      for( int i = 0 ; i < height ; i++ )
      {
        for( int j = 0 ; j < width ; j++ )
        {
          curColor = new Color
                     (
                       imageRaster.getSample(j,i,0),
                       imageRaster.getSample(j,i,1),
                       imageRaster.getSample(j,i,2),
                       imageRaster.getSample(j,i,3)
                     );
          if( ! colorVector.contains((Object)curColor)  && curColor.getAlpha() != 0)
          {
            colorVector.add((Object)curColor);
          }
          if( colorVector.size() > 256 )           //this image is 24 bit or 32 bit
          {
            is256Plus = true;
            break;
          }

          if( curColor.getAlpha() != 0 && curColor.getAlpha() != 255 )   //if an alpha channel is used this image will have to be 32 bit
          {
            is256Plus = true;
            break;
          }
        }
      } 

      if( is256Plus )  //this is either a 24 bit or 32 bit image
      {
        int     alphaSample;
        boolean is32Bit = false;
        for( int i = 0 ; i < height ; i++ )
        {
          for( int j = 0 ; j < width ; j++ )
          {
            alphaSample = imageRaster.getSample(j,i,3);
            if( alphaSample != 0 && alphaSample != 255 )  //32 bit
              is32Bit = true;
          }
        }
        if( is32Bit )  //32 bit
          return null;
        else           //24 bit
          return new Vector();   //empty Vector
      }

      return colorVector;
    } //END FUNCTION determineColors()

    /**
     *  Converts an array of bits(boolean) int an array of bytes
     */
    public byte[] determineBytes(boolean[] someBits)
    {
      //will hold bytes equal to someBits
        byte[] bytes = new byte[someBits.length/8];

      //For each byte
        for( int i = 0 ; i < bytes.length ; i++ )
        {
          //Will hold a single byte worth of bits
            boolean[] byteWorth = new boolean[8];
   
          //Will hold the int value of a single byte
            int sum = 0;
  
          //for next eight bits
            for( int b = 0 ; b < 8 ; b++ )
            {
              byteWorth[b] = someBits[ i*8 + b ]; //copy to byteWorth
            } 

          //for each bit in byteWorth
            for( int y = 0 ; y < 8 ; y++ )
            {
              if( byteWorth[y] )              //If bit is one
                sum += Math.pow(2,(8-1)-y);   //Add its value to sum
            }

          //update the bytes array
            bytes[i] = (byte)sum;
        }
     
      return bytes;
    } //END FUNCTION determineBytes()
  }
