package org.joshy.common.swing.xp.osx;

import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import org.joshy.common.swing.xp.XPHelper;
import java.util.Iterator;
import java.util.*;
import org.joshy.u;
//import com.apple.MRJQuitHandler;
//import com.apple.eawt.*;
import net.roydesign.app.*;
import org.joshy.common.swing.xp.DefaultHelper;

public class OSXXPHelper extends DefaultHelper {

    public JMenuItem getQuitMenu() {
        return getApplication().getQuitJMenuItem();
        /*
        JMenuItem item = new JMenuItem("Quit") {
            public void addActionListener(ActionListener listener) {
                addQuitListener(listener);
            }
        };
        return item;
        */
    }

    public JMenuItem getAboutMenu() {
        u.p("getting: " + getApplication().getAboutJMenuItem()); 
        return getApplication().getAboutJMenuItem();
        /*
        JMenuItem item = new JMenuItem("About") {
            public void addActionListener(ActionListener listener) {
                addAboutListener(listener);
            }
        };
        return item;
        */
    }

    public JMenuItem getPreferencesMenu() {
        return getApplication().getPreferencesJMenuItem();
        /*
      JMenuItem item = new JMenuItem("Preferences") {
        public void addActionListener(ActionListener listener) {
          addPreferencesListener(listener);
        }
      };
      return item;
      */
    }

    /*
    public void addAboutListener(ActionListener listener) {
        JMenuApplicationAdapter aa = getAdapter();
        aa.addAbout(listener);
    }
    */

    public void addOpenListener(ActionListener listener) {
        getApplication().addOpenDocumentListener(listener);
        /*
        JMenuApplicationAdapter aa = getAdapter();
        aa.addOpen(listener);
        */
    }



    /*
    public void addQuitListener(ActionListener listener) {
            JMenuApplicationAdapter aa = getAdapter();
            aa.addQuit(listener);
    }
    public void addPreferencesListener(ActionListener listener) {
            JMenuApplicationAdapter aa = getAdapter();
            aa.addPreferences(listener);
    }
    */


    public boolean isMac() {
        return true;
    }

    public int getAccelKey() {
        return ActionEvent.META_MASK;
    }


        /* internal stuff */

    private static Application app;
    private static Application getApplication() {
        if(app == null) {
            app = Application.getInstance();
        }
        //MRJApplicationUtils.registerAboutHandler()
        return app;
    }

    /*
        private static JMenuApplicationAdapter quit_adapter;
        private static JMenuApplicationAdapter getAdapter() {
            if(quit_adapter == null) {
                quit_adapter = new JMenuApplicationAdapter();
        getApplication().addApplicationListener(quit_adapter);
        quit_adapter.setApplication(getApplication());
            }
            return quit_adapter;
        }
        */
}

/*
        class JMenuApplicationAdapter extends ApplicationAdapter {
            List quit = new ArrayList();
            List about = new ArrayList();
      List prefs = new ArrayList();
      List open = new ArrayList();
      Application app;
      public void setApplication(Application app) {
        this.app = app;
      }
            public void addQuit(ActionListener list) {
                this.quit.add(list);
            }
            public void addAbout(ActionListener list) {
                this.about.add(list);
            }
      public void addPreferences(ActionListener list) {
        this.prefs.add(list);
        app.setEnabledPreferencesMenu(true);
      }
      public void addOpen(ActionListener list) {
        this.open.add(list);
      }
            public void handleAbout(ApplicationEvent evt) {
                ActionEvent ae = new ActionEvent(evt,-999,"about");
                Iterator it = about.iterator();
                while(it.hasNext()) {
                    ((ActionListener)it.next()).actionPerformed(ae);
                }
        evt.setHandled(true);
            }
            public void handleQuit(ApplicationEvent evt) {
        ActionEvent ae = new ActionEvent(evt,-999,"quit");
        Iterator it = quit.iterator();
        while(it.hasNext()) {
          ((ActionListener)it.next()).actionPerformed(ae);
        }
        evt.setHandled(true);
            }
            public void handlePreferences(ApplicationEvent evt) {
        ActionEvent ae = new ActionEvent(evt,-999,"preferences");
        Iterator it = prefs.iterator();
        while(it.hasNext()) {
          ((ActionListener)it.next()).actionPerformed(ae);
        }
        evt.setHandled(true);
            }
      public void handleOpenFile(ApplicationEvent evt) {
        ActionEvent ae = new ActionEvent(evt,-999,evt.getFilename());
        Iterator it = open.iterator();
        while(it.hasNext()) {
          ((ActionListener)it.next()).actionPerformed(ae);
        }
        evt.setHandled(true);
        System.out.println("done opening");
      }
        }
*/

