/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;

public class MyFileFilter extends javax.swing.filechooser.FileFilter implements java.io.FileFilter 
{
    String[] theExtensions;
    boolean  theShowDirs;
    String   theDescription = "File Type";
 
    public MyFileFilter( String[] someExt, String aDescription )
    {
      this(true,someExt,aDescription);
    }

    public MyFileFilter( boolean showDirs, String[] someExt, String aDescription)
    {
      theExtensions = someExt;
      theShowDirs   = showDirs;
      theDescription = aDescription;
    }
  
    //Accept all directories files possibly containing icons
    public boolean accept(File f) 
    {
        if(f.isDirectory()) 
        {
          return theShowDirs;
        }

        if( theExtensions == null )
        {
          return true;
        }

        if( theExtensions.length == 0 )
          return false;

        String extension = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if( i > 0 && i < (s.length() - 1))
        {
          extension = s.substring(i+1).toLowerCase();
        }

        if(extension != null)
        {
          for( int j = 0 ; j < theExtensions.length ; j++ )
          {
            if( extension.equals(theExtensions[j]) )
            {
              return true;
            } 
          }
        }

        return false;
    }

    public String[] getExtensions()
    {
      return theExtensions;
    }

    //The description of this filter
    public String getDescription() 
    {
      return theDescription;
    }
}