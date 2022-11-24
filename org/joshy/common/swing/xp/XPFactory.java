package org.joshy.common.swing.xp;

import org.joshy.u;

public class XPFactory {
    private XPFactory() {
    }
    private static boolean isMac() {
      u.p("system property os.name = " + System.getProperty("os.name"));
        if(System.getProperty("os.name").startsWith("Mac OS")) {
          u.p("detected mac platform");
            return true;
        } else {
            return false;
        }
    }
    public static XPHelper getXPHelper() {
        try {
            XPFactory xpf = new XPFactory();
            Class cls = xpf.getClass();
            Object helper = null;
            if(isMac()) {
                helper = cls.forName("org.joshy.common.swing.xp.osx.OSXXPHelper").newInstance();
            } else {
                helper = cls.forName("org.joshy.common.swing.xp.DefaultHelper").newInstance();
            }
            return (XPHelper)helper;
        } catch (Exception ex) {
            u.p(ex);
            return new DefaultHelper();
        }
    }
}


