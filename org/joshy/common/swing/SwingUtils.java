package org.joshy.util.swing;

import java.awt.*;
import javax.swing.*;

public class SwingUtils {

	public static void center(JDialog dialog) {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int)(dim.getWidth() - dialog.getWidth())/2;
		int y = (int)(dim.getHeight() - dialog.getHeight())/2;
		dialog.setLocation(x,y);
	}
	public static void center(JFrame dialog) {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int)(dim.getWidth() - dialog.getWidth())/2;
		int y = (int)(dim.getHeight() - dialog.getHeight())/2;
		dialog.setLocation(x,y);
	}
}
