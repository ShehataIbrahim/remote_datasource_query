package com.ih;

import com.ih.ui.QueryForm;
import com.ih.utils.FrameUtils;

import javax.swing.JFrame;

public class Runner {
    public Runner() {
        super();
    }

    public static void main(String[] args) {
        //System.out.println(new java.util.Date(Long.valueOf("1464602747740")));
        QueryForm form = new QueryForm();
  //      form.pack();
    //    form.setSize(600, 600);
        form.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FrameUtils.setCenterScreen(form);
        form.setVisible(true);
    }
}
