package org.joshy.common.swing.xp;

import org.joshy.u;
import java.awt.event.*;
import java.awt.Toolkit;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import org.joshy.common.swing.xp.XPHelper;
import javax.swing.Action;
import javax.swing.text.DefaultEditorKit;

public class DefaultHelper implements XPHelper {

    public JMenuItem getNewMenu() {
        JMenuItem item = new JMenuItem("New");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
            Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        return item;
    }
    public JMenuItem getOpenMenu() {
        JMenuItem item = new JMenuItem("Open");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
            Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        return item;
    }
    public JMenuItem getCloseMenu() {
        JMenuItem item = new JMenuItem("Close");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,
            Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        return item;
    }
    public JMenuItem getSaveMenu() {
        JMenuItem item = new JMenuItem("Save");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
            Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        return item;
    }
    public JMenuItem getPrintMenu() {
        JMenuItem item = new JMenuItem("Print");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
            Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        return item;
    }


    public JMenuItem getQuitMenu() {
        final JMenuItem item = new JMenuItem("Exit");
        return item;
    }

	public JMenuItem getPreferencesMenu() {
			final JMenuItem item = new JMenuItem("Prefs");
			return item;
		}
		
    public JMenuItem getAboutMenu() {
        final JMenuItem item = new JMenuItem("About");
        return item;
    }

    public boolean isMac() {
        return false;
    }


    public int getAccelKey() {
        return ActionEvent.CTRL_MASK;
    }

    public Action getCopyAction() {
        Action copy = new DefaultEditorKit.CopyAction();
        copy.putValue(copy.NAME, "Copy");
        copy.putValue(copy.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        return copy;
    }

    public Action getCutAction() {
        Action cut = new DefaultEditorKit.CutAction();
        cut.putValue(cut.NAME, "Cut");
        cut.putValue(cut.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        u.p("cut = " + cut);
        u.p("val = " + cut.getValue(cut.ACCELERATOR_KEY));
        return cut;
    }

    public Action getPasteAction() {
        Action paste = new DefaultEditorKit.PasteAction();
        paste.putValue(paste.NAME, "Paste");
        paste.putValue(paste.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        return paste;
    }


    
    public void addOpenListener(ActionListener listener) {
      // don't do anything. we don't support open file events
      // on non OSX platforms yet.
		}

}

