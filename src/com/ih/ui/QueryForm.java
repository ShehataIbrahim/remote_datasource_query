package com.ih.ui;


import com.ih.connection.ConnectionManager;
import com.ih.connection.ServerInfo;
import com.ih.contracts.QueryInterface;
import com.ih.utils.FrameUtils;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import java.awt.GridLayout;
import java.awt.Rectangle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;

import java.awt.event.WindowEvent;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;

import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

import org.apache.commons.io.output.StringBuilderWriter;

public class QueryForm extends JFrame implements QueryInterface {

    private Statement statement = null;
    private static final long serialVersionUID = 7191762624400857868L;
    private JMenuBar jMenuBar1 = new JMenuBar();
    private JMenu mnuFile = new JMenu();
    private JMenuItem mnuConnect = new JMenuItem();
    private JMenuItem mnuExit = new JMenuItem();
    private JMenu mnuAbout = new JMenu();
    private JPanel pnlActions = new JPanel();
    private JButton btnShowTables = new JButton();
    private JComboBox cmbSchemas = new JComboBox();
    private JButton btnShowSchemas = new JButton();
    private JPanel pnlQuery = new JPanel();
    private JPanel pnlTables = new JPanel();
    private JList lstTables = new JList();
    private GridLayout gridLayout1 = new GridLayout();
    private JScrollPane pnlScrollTable = new JScrollPane();
    private BorderLayout borderLayout1 = new BorderLayout();
    private JTextArea jTextArea2 = new JTextArea();
    private JScrollPane jScrollPane1 = new JScrollPane();
    private JEditorPane txtResultArea = new JEditorPane();
    private JButton btnExecute = new JButton();
    private JScrollPane jScrollPane2 = new JScrollPane();
    private JTextArea txtQuery = new JTextArea();
    private JButton btnClearResults = new JButton();

    public QueryForm() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.getContentPane().setLayout(null);
        this.setJMenuBar(jMenuBar1);
        this.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    onWindowClose(e);
                }
            });
        mnuFile.setText("File");
        mnuConnect.setText("Connect");
        mnuConnect.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    mnuConnectAction(e);
                }
            });
        mnuExit.setText("Exit");
        mnuExit.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    exitAction(e);
                }
            });
        mnuAbout.setText("About");
        pnlActions.setBounds(new Rectangle(225, 0, 515, 35));

        btnShowTables.setText("Show Tables");
        btnShowTables.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    btnShowTables_actionPerformed(e);
                }
            });
        btnShowSchemas.setText("Show Schemas");
        btnShowSchemas.setActionCommand("");
        btnShowSchemas.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    btnShowSchemas_actionPerformed(e);
                }
            });
        pnlScrollTable.setViewportView(lstTables);
        pnlQuery.setBounds(new Rectangle(220, 45, 520, 490));
        pnlQuery.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        pnlQuery.setLayout(null);
        pnlTables.setBounds(new Rectangle(5, 5, 210, 535));
        pnlTables.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        pnlTables.setLayout(gridLayout1);

        lstTables.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    lstTables_mouseClicked(e);
                }
            });
        pnlActions.setVisible(false);
        pnlQuery.setVisible(false);
        pnlTables.setVisible(false);

        jScrollPane1.setBounds(new Rectangle(10, 90, 500, 395));
        btnExecute.setText("Execute");
        btnExecute.setBounds(new Rectangle(410, 5, 95, 25));
        btnExecute.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    btnExecute_actionPerformed(e);
                }
            });
        jScrollPane2.setBounds(new Rectangle(10, 5, 395, 75));
        btnClearResults.setText("Clear");
        btnClearResults.setBounds(new Rectangle(410, 55, 95, 25));
        btnClearResults.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    btnClearResults_actionPerformed(e);
                }
            });
        mnuFile.add(mnuConnect);
        mnuFile.add(mnuExit);
        jMenuBar1.add(mnuFile);
        jMenuBar1.add(mnuAbout);
        pnlActions.add(btnShowSchemas, null);
        pnlActions.add(cmbSchemas, null);
        pnlActions.add(btnShowTables, null);
        pnlTables.add(pnlScrollTable, null);
        this.getContentPane().add(pnlTables, null);
        txtResultArea.setEditable(false);
        txtResultArea.setContentType("text/html");
        txtResultArea.setText("</html>\n" +
                "  </body>\n" +
                "  </body>\n" +
                "</html>");
        jScrollPane1.getViewport().add(txtResultArea, null);
        jScrollPane2.getViewport().add(txtQuery, null);
        pnlQuery.add(btnClearResults, null);
        pnlQuery.add(jScrollPane2, null);
        pnlQuery.add(btnExecute, null);
        pnlQuery.add(jScrollPane1, null);
        this.getContentPane().add(pnlQuery, null);
        this.getContentPane().add(pnlActions, null);
        this.pack();
        this.setSize(new Dimension(750, 600));
        this.setMaximumSize(new Dimension(750, 600));
        this.setResizable(false);
    }

    private void exitAction(ActionEvent e) {
        beforeClose();
        this.dispose();
    }

    private void onWindowClose(WindowEvent e) {
        beforeClose();
    }

    private void beforeClose() {
        try {
            if (statement != null && !statement.isClosed())
                statement.close();
        } catch (SQLException e) {
        }
        ConnectionManager.disconnect();
    }

    private void mnuConnectAction(ActionEvent e) {
        ConnectionManager.switchConnectionStatus(this);

    }

    public void notifyConnectionStarted() {
        try {
            statement = ConnectionManager.getConnection().createStatement();
            SwingUtilities.invokeLater(new Runnable(){

                    public void run() {
                        pnlQuery.setVisible(true);
                        
                    }
                });
        } catch (SQLException e) {
        }
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    mnuConnect.setText("Disconnect");
                    btnShowSchemas.setVisible(true);
                    btnShowTables.setVisible(false);
                    cmbSchemas.setVisible(false);
                    pnlActions.setVisible(true);
                }
            });
    }

    public void notifyConnectionClosed() {
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    mnuConnect.setText("Connect");
                    btnShowSchemas.setVisible(false);
                    btnShowTables.setVisible(false);
                    cmbSchemas.setVisible(false);
                    cmbSchemas.setModel(new DefaultComboBoxModel());
                    pnlActions.setVisible(false);
                    pnlQuery.setVisible(false);
                    pnlTables.setVisible(false);
                }
            });
    }

    private void btnShowSchemas_actionPerformed(ActionEvent e) {
        new Thread() {

            @Override
            public void run() {
                btnShowSchemas.setEnabled(false);
                try {
                    List<String> schemas = ConnectionManager.getSchemasList();
                    DefaultComboBoxModel<String> model =
                        new DefaultComboBoxModel<String>();
                    for (String sch : schemas)
                        model.addElement(sch);
                    cmbSchemas.setModel(model);

                    SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                cmbSchemas.updateUI();
                                btnShowSchemas.setVisible(false);
                                btnShowTables.setVisible(true);
                                cmbSchemas.setVisible(true);

                            }
                        });
                } catch (SQLException f) {
                    f.printStackTrace();
                }
                btnShowSchemas.setEnabled(true);
            }
        }.start();


    }

    private void btnShowTables_actionPerformed(ActionEvent e) {
        new Thread() {

            @Override
            public void run() {
                btnShowTables.setEnabled(false);
                try {
                    List<String> tables =
                        ConnectionManager.getSchemaTables((String)cmbSchemas.getSelectedItem());
                    DefaultListModel<String> listModel =
                        new DefaultListModel<String>();
                    for (String tbl : tables)
                        listModel.addElement(tbl);
                    lstTables.setModel(listModel);
                    SwingUtilities.invokeLater(new Runnable() {

                            public void run() {
                                lstTables.updateUI();
                                btnShowTables.setVisible(true);
                                cmbSchemas.setVisible(true);
                                pnlActions.setVisible(false);
                                pnlTables.setVisible(true);
                                pnlTables.setVisible(true);
                                

                            }
                        });
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                btnShowTables.setEnabled(true);
            }
        }.start();

    }

    private void lstTables_mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            txtQuery.append((String)lstTables.getSelectedValue());
        }
        if (e.getClickCount() > 2) {
            txtQuery.setText("SELECT * FROM " +
                             (String)lstTables.getSelectedValue());
        }
    }
    StringBuilder currentResults = new StringBuilder();

    private void btnExecute_actionPerformed(ActionEvent e) {
        new Thread(){

            @Override
            public void run() {
                btnExecute.setEnabled(false);
                StringBuilder builder = new StringBuilder();

                try {
                    ResultSet result = statement.executeQuery(txtQuery.getText());
                    int colCount = result.getMetaData().getColumnCount();
                    builder.append("<TABLE border=\"1\" >");
                    builder.append("<TR style=\"background-color:lightgray;\">");
                    for (int i = 1; i <= colCount; i++) {
                        builder.append("<TH>" + result.getMetaData().getColumnName(i) +
                                       "</TH>\n");
                    }
                    builder.append("</TR>");
                    while (result.next()) {
                        builder.append("<TR>");
                        for (int i = 1; i <= colCount; i++) {
                            builder.append("<TD>" + result.getString(i) + "</TD>");
                        }
                        builder.append("</TR>");
                    }
                    builder.append("</TABLE>");
                    currentResults.insert(0, builder.toString());
                    SwingUtilities.invokeLater(new Runnable() {

                            public void run() {

                                txtResultArea.setText(currentResults.toString());
                                txtResultArea.updateUI();
                            }
                        });

                } catch (SQLException f) {
                    JOptionPane.showMessageDialog(btnExecute, f.getMessage(),
                                                  "Execution Error",
                                                  JOptionPane.ERROR_MESSAGE);
                    f.printStackTrace();
                }
                btnExecute.setEnabled(true);
            }
        }.start();

    }

    private void btnClearResults_actionPerformed(ActionEvent e) {
        currentResults = new StringBuilder("</html>\n" +
                "  </body>\n" +
                "  </body>\n" +
                "</html>");
        SwingUtilities.invokeLater(new Runnable() {

                public void run() {

                    txtResultArea.setText(currentResults.toString());
                    txtResultArea.updateUI();
                }
            });
    }
}
