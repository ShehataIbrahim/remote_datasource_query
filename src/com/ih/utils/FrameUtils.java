package com.ih.utils;

import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class FrameUtils {
    public FrameUtils() {
        super();
    }
    public static void setCenterScreen(JFrame frame) {
            frame.setLocation((int)((Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 
                                     frame.getSize().width) / 2), 
                              (int)((Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 
                                     frame.getSize().height) / 2));
        }
    public static void setCenterScreen(JDialog dialog) {
            dialog.setLocation((int)((Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 
                                     dialog.getSize().width) / 2), 
                              (int)((Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 
                                     dialog.getSize().height) / 2));
        }

}
