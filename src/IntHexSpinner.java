/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import javax.swing.*;
import java.awt.*;

public class IntHexSpinner extends JSpinner
{
  private boolean          isInt;
  private boolean          isHex;
  private SpinnerListModel theIntModel;
  private SpinnerListModel theHexModel;
  private String[]         anIntArray;
  private String[]         aHexArray;

  public IntHexSpinner()
  {
    String[] aHexArray = new String[256];
    for( int i = 0 ; i <= 255 ; i++ )
    {
      aHexArray[i] = (Integer.toHexString(i)).toUpperCase();
      if( aHexArray[i].length() == 1 )
        aHexArray[i] = "0" + aHexArray[i];
    }

    String[] anIntArray = new String[256];
    for( int i = 0 ; i <= 255 ; i++ )
      anIntArray[i] = String.valueOf(i);

    theIntModel   = new SpinnerListModel(anIntArray);
    theHexModel   = new SpinnerListModel(aHexArray);

    this.setModel(theIntModel);
    isInt = true;
    isHex = false;
  }

  public void setHex()
  {
    if( isInt )
    {
      String value = (String)(this.getValue());
      this.setModel(theHexModel);
      isInt = false;
      isHex = true;
      this.setValue((String)(((theHexModel.getList()).toArray())[((Integer.valueOf(value)).intValue())]));
    }
  }

  public void setInt()
  {
    if( isHex )
    {
      int value = hexToInt((String)(this.getValue()));
      this.setModel(theIntModel);
      isInt = true;
      isHex = false;
      this.setValue((String)(((theIntModel.getList()).toArray())[value]));
    }
  }

  public void setCurrentValue(int aValue)
  {
    if( isInt )
    {
      this.setValue((String)(((theIntModel.getList()).toArray())[aValue]));
    }
    if( isHex )
    {
      this.setValue((String)(((theHexModel.getList()).toArray())[aValue]));
    }
  }

  public int getCurrentValue()
  {
    if( isInt )
    {
      return (Integer.valueOf((String)this.getValue())).intValue();
    }
    if( isHex )
    {
      return hexToInt((String)this.getValue());
    }
    return -1;
  }

  public String getCurrentString()
  {
    if( isInt )
    {
      return "" + (Integer.valueOf((String)this.getValue())).intValue();
    }
    if( isHex )
    {
      return (String)this.getValue();
    }
    return null;
  }

  private int hexToInt(String aString)
  {
    aString = aString.toLowerCase();
    int length = aString.length();
    int value  = 0;
    int curr;
    int power = length-1;
    for( int i= 0 ; i < length ; i++ )
    {
      curr = aString.charAt(i)-'0';

      if( curr < 0 )
        return -1;
      if( curr > 9 )
      {
        curr = aString.charAt(i)-'a';
        if( curr > 5 || curr < 0 )
          return -1;
        curr += 10;
      }
      value += curr*Math.pow(16,power--);
    }

    return value;
  }
}