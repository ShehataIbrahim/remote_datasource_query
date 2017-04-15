package com.ih;

import com.ih.ui.ConnectionDialog;
import com.ih.ui.QueryForm;
import com.ih.utils.FrameUtils;

import javax.swing.JFrame;

public class Runner {
    public Runner() {
        super();
    }

    public static void main(String[] args) {
        
        QueryForm form = new QueryForm();
       // ConnectionDialog form=new ConnectionDialog(null,"",true);
        form.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FrameUtils.setCenterScreen(form);
        form.setVisible(true);
    }
}
