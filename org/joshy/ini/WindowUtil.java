package org.joshy.jni;
import java.awt.Component;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Canvas;
import java.awt.*;

public class WindowUtil extends Canvas {
    // load the native dll
    static {
        System.loadLibrary("WindowUtil");
    }
    // the actual native method
    public native void flash(Component c, boolean bool);

    /**
    <p>A utility method that flashes the window on and off. This method
    will not block to wait for the flashing.
     Instead it will spawn an thread to do the flashing.
    </p>

     @param frame The JFrame to be flashed
     @param intratime The amount of time between the on and off states of a single flash
     @param intertime The amount of time between different flashes
     @param count The number of times to flash the window
    */
    public void flash(final JFrame frame, final int intratime, final int intertime, final int count) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    // flash on and off each time
                    for(int i=0; i<count; i++) {
                        flash(frame,true);
                        Thread.sleep(intratime);
                        flash(frame,true);
                        Thread.sleep(intertime);
                    }
                    // turn the flash off
                    flash(frame,false);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
        }}).start();
    }

    public static void main(String[] args) throws Exception {
        final JFrame frame = new JFrame();
        JButton button = new JButton("stuff");
        frame.getContentPane().add(button);
        final WindowUtil winutil = new WindowUtil();
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                winutil.flash(frame,750,1500,5);
            }
        });
        frame.pack();
        frame.show();
    }

}
