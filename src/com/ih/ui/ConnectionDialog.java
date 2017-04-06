package com.ih.ui;

import com.ih.beans.ConnectionData;
import com.ih.connection.ConnectionManager;
import com.ih.contracts.QueryInterface;
import com.ih.connection.ServerInfo;
import com.ih.beans.Configs;
import com.ih.beans.FlaggedResult;
import com.ih.utils.FrameUtils;
import com.ih.utils.XMLSAXParser;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;

import java.awt.event.FocusEvent;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import javax.swing.SwingUtilities;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

public class ConnectionDialog extends JDialog {
    private static final long serialVersionUID = -8686226056993402807L;
    private JTextField txtDataSource = new JTextField();
    private JTextField txtServerIP = new JTextField();
    private JTextField txtUsername = new JTextField();
    private JComboBox cmbServers = initServersData();
    private JButton btnConnect = new JButton();
    private JButton btnExit = new JButton();
    private JPasswordField txtPassword = new JPasswordField();
    private JLabel jLabel1 = new JLabel();
    private JLabel jLabel2 = new JLabel();
    private JLabel jLabel3 = new JLabel();
    private JLabel jLabel4 = new JLabel();
    private JLabel jLabel5 = new JLabel();
    private JTextField txtPort = new JTextField();
    private JLabel jLabel6 = new JLabel();
    private ServerInfo currentServer = null;
    private QueryInterface queryInterface = null;

    public ConnectionDialog(QueryInterface parent, String title,
                            boolean modal) {
        super();
        setTitle(title);
        setModal(modal);
        this.queryInterface = parent;
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.getContentPane().setLayout(null);
        txtDataSource.setBounds(new Rectangle(145, 50, 205, 20));
        txtServerIP.setBounds(new Rectangle(145, 90, 105, 20));
        txtUsername.setBounds(new Rectangle(145, 125, 205, 20));
        cmbServers.setBounds(new Rectangle(145, 10, 205, 20));
        cmbServers.addFocusListener(new FocusAdapter() {
                public void focusLost(FocusEvent e) {
                    cmbServers_focusLost(e);
                }
            });
        btnConnect.setText("Connect");
        btnConnect.setBounds(new Rectangle(250, 205, 100, 20));
        btnConnect.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    btnConnect_actionPerformed(e);
                }
            });
        btnExit.setText("Exit");
        btnExit.setBounds(new Rectangle(15, 205, 75, 20));
        btnExit.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    btnExit_actionPerformed(e);
                }
            });
        txtPassword.setBounds(new Rectangle(145, 165, 205, 20));
        jLabel1.setText("Server");
        jLabel1.setBounds(new Rectangle(5, 15, 115, 15));
        jLabel2.setText("DataSource JNDI");
        jLabel2.setBounds(new Rectangle(5, 55, 115, 15));
        jLabel3.setText("Server IP");
        jLabel3.setBounds(new Rectangle(5, 90, 115, 15));
        jLabel4.setText("Username");
        jLabel4.setBounds(new Rectangle(5, 130, 115, 15));
        jLabel5.setText("Password");
        jLabel5.setBounds(new Rectangle(5, 165, 115, 15));
        txtPort.setBounds(new Rectangle(300, 90, 50, 20));
        jLabel6.setText("Port");
        jLabel6.setBounds(new Rectangle(255, 95, 45, 15));
        this.getContentPane().add(jLabel6, null);
        this.getContentPane().add(txtPort, null);
        this.getContentPane().add(jLabel5, null);
        this.getContentPane().add(jLabel4, null);
        this.getContentPane().add(jLabel3, null);
        this.getContentPane().add(jLabel2, null);
        this.getContentPane().add(jLabel1, null);
        this.getContentPane().add(txtPassword, null);
        this.getContentPane().add(btnExit, null);
        this.getContentPane().add(btnConnect, null);
        this.getContentPane().add(cmbServers, null);
        this.getContentPane().add(txtUsername, null);
        this.getContentPane().add(txtServerIP, null);
        this.getContentPane().add(txtDataSource, null);
        this.setUndecorated(true);
        txtDataSource.setText("wcsitesDS");
        txtServerIP.setText("localhost");
        txtPort.setText("7003");
        txtUsername.setText("weblogic");
        txtPassword.setText("welcome1");
        this.pack();
        this.setSize(new Dimension(380, 280));
        FrameUtils.setCenterScreen(this);

    }

    private void btnExit_actionPerformed(ActionEvent e) {
        this.dispose();
    }

    private JComboBox initServersData() {
        java.util.List<ServerInfo> infos = parseServersFile();
        DefaultComboBoxModel<ServerInfo> model =
            new DefaultComboBoxModel<ServerInfo>();
        for (ServerInfo info : infos)
            model.addElement(info);
        return new JComboBox(model);

    }

    private void btnConnect_actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    btnConnect.setEnabled(false);
                }
        });
        new Thread() {

            @Override
            public void run() {
                
                ConnectionData data = new ConnectionData();
                data.setServerName(currentServer.getServerName());
                data.setServerClass(currentServer.getServerClass());
                data.setConnectionDriver(currentServer.getDriver());
                data.setDataSourceJNDI(txtDataSource.getText());
                data.setServerIP(txtServerIP.getText());
                data.setServerConsolePort(txtPort.getText());
                data.setUserName(txtUsername.getText());
                data.setPassword(new String(txtPassword.getPassword()));
                ConnectionManager.setConnectionDetails(data);
                FlaggedResult result = ConnectionManager.connect();
                if (!result.isFlag()){
                    JOptionPane.showMessageDialog(btnConnect,
                                                  result.getMessage(),
                                                  "Connection Error",
                                                  JOptionPane.ERROR_MESSAGE);
                SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            btnConnect.setEnabled(true);
                        }
                });
            }
                else {
                    queryInterface.notifyConnectionStarted();
                    dispose();
                }
            }
        }.start();


    }

    private java.util.List<ServerInfo> parseServersFile() {
        final ArrayList<ServerInfo> infos = new ArrayList<ServerInfo>(); // to
        XMLSAXParser h = new XMLSAXParser("server") {

            @Override
            public void insertObjects(ArrayList<HashMap<String, String>> nodeList) {
                for (HashMap<String, String> node : nodeList) {
                    ServerInfo info = new ServerInfo();
                    info.setServerName(node.get("/server/name"));
                    info.setDriver(node.get("/server/driver"));
                    info.setServerClass(node.get("/server/class"));
                    infos.add(info);
                }

            }
        };
        try {
            SAXParserFactory.newInstance().newSAXParser().parse(new File(Configs.SERVERS_FILE),
                                                                h);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return infos;
    }

    private void cmbServers_focusLost(FocusEvent e) {
        if (cmbServers.getModel().getSelectedItem() != currentServer) {
            currentServer =
                    (ServerInfo)cmbServers.getModel().getSelectedItem();
            System.out.println("Value Changed");
        }
    }
}
