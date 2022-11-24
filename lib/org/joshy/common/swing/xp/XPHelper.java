package org.joshy.common.swing.xp;

import javax.swing.Action;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;

public interface XPHelper {
    public JMenuItem getNewMenu();
    public JMenuItem getOpenMenu();
    public JMenuItem getCloseMenu();
    public JMenuItem getSaveMenu();
    public JMenuItem getPrintMenu();
    public JMenuItem getQuitMenu();
    public JMenuItem getAboutMenu();
    public JMenuItem getPreferencesMenu();
    public Action getCopyAction();
    public Action getCutAction();
    public Action getPasteAction();
    
    public void addOpenListener(ActionListener listener);
    /*
		public void addAboutListener(ActionListener listener);
		public void addQuitListener(ActionListener listener);
    */
    
		public int getAccelKey();
		public boolean isMac();
}

