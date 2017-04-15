package com.ih.ui;


import com.ih.beans.Configs;
import com.ih.beans.ConnectionData;
import com.ih.beans.FlaggedResult;
import com.ih.beans.SavedConnection;
import com.ih.connection.ConnectionManager;
import com.ih.connection.ServerInfo;
import com.ih.contracts.QueryInterface;
import com.ih.utils.FrameUtils;
import com.ih.utils.XMLSAXParser;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import java.awt.event.MouseAdapter;

import java.awt.event.MouseEvent;

import java.io.File;
import java.io.IOException;

import java.io.PrintStream;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
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
    private JComboBox cmbServers = new JComboBox();
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
    private JButton btnSaveConnection = new JButton();
    private JScrollPane jScrollPane1 = new JScrollPane();
    private JList lstConnections = new JList();
    private JPanel jPanel1 = new JPanel();
    private GridLayout gridLayout1 = new GridLayout();
    private JScrollPane pnlScrollConnections = new JScrollPane();
    private JTextField txtConnectionName = new JTextField();
    private JLabel jLabel7 = new JLabel();

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
        initServersData();
        initConnectionsData();
        this.getContentPane().setLayout(null);
        txtDataSource.setBounds(new Rectangle(350, 81, 205, 20));
        txtServerIP.setBounds(new Rectangle(350, 119, 105, 20));
        txtUsername.setBounds(new Rectangle(350, 157, 205, 20));
        cmbServers.setBounds(new Rectangle(350, 43, 205, 20));
        cmbServers.addFocusListener(new FocusAdapter() {
                public void focusLost(FocusEvent e) {
                    cmbServers_focusLost(e);
                }
            });
        btnConnect.setText("Connect");
        btnConnect.setBounds(new Rectangle(455, 235, 100, 20));
        btnConnect.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    btnConnect_actionPerformed(e);
                }
            });
        btnExit.setText("Exit");
        btnExit.setBounds(new Rectangle(220, 235, 75, 20));
        btnExit.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    btnExit_actionPerformed(e);
                }
            });
        txtPassword.setBounds(new Rectangle(350, 195, 205, 20));
        jLabel1.setText("Server");
        jLabel1.setBounds(new Rectangle(210, 43, 135, 20));
        jLabel2.setText("DataSource JNDI");
        jLabel2.setBounds(new Rectangle(210, 81, 135, 20));
        jLabel3.setText("Server IP");
        jLabel3.setBounds(new Rectangle(210, 119, 135, 20));
        jLabel4.setText("Username");
        jLabel4.setBounds(new Rectangle(210, 157, 135, 20));
        jLabel5.setText("Password");
        jLabel5.setBounds(new Rectangle(210, 195, 135, 20));
        txtPort.setBounds(new Rectangle(505, 119, 50, 20));
        jLabel6.setText("Port");
        jLabel6.setBounds(new Rectangle(458, 119, 45, 20));
        btnSaveConnection.setText("Save Connection");
        btnSaveConnection.setBounds(new Rectangle(310, 235, 130, 20));
        btnSaveConnection.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    btnSaveConnection_actionPerformed(e);
                }
            });
        DefaultListModel<SavedConnection> model=new DefaultListModel<SavedConnection>();
       List<SavedConnection> data=parseConnectionsFile();
       for(SavedConnection con:data)
           model.addElement(con);
        lstConnections.setModel(model);
        pnlScrollConnections.setViewportView(lstConnections);
        jScrollPane1.setBounds(new Rectangle(0, 0, 2, 2));
        lstConnections.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    lstConnections_mouseClicked(e);
                }
            });
        jPanel1.setBounds(new Rectangle(5, 40, 180, 235));
        jPanel1.setLayout(gridLayout1);
        txtConnectionName.setBounds(new Rectangle(350, 5, 200, 20));
        jLabel7.setText("Connection Name");
        jLabel7.setBounds(new Rectangle(210, 5, 135, 20));
        jPanel1.add(pnlScrollConnections, null);
        this.getContentPane().add(jLabel7, null);
        this.getContentPane().add(txtConnectionName, null);
        this.getContentPane().add(jPanel1, null);
        this.getContentPane().add(jScrollPane1, null);
        this.getContentPane().add(btnSaveConnection, null);
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
      /*  txtDataSource.setText("wcsitesDS");
        txtServerIP.setText("192.168.134.30");
        txtPort.setText("7005");
        txtUsername.setText("asem");
        txtPassword.setText("asem123$");*/
        this.pack();
        this.setSize(new Dimension(575, 315));
        FrameUtils.setCenterScreen(this);

    }

    private void btnExit_actionPerformed(ActionEvent e) {
        this.dispose();
    }

    private void initServersData() {
        List<ServerInfo> infos = parseServersFile();
        DefaultComboBoxModel<ServerInfo> model =
            new DefaultComboBoxModel<ServerInfo>();
        for (ServerInfo info : infos)
            model.addElement(info);
        cmbServers.setModel(model);
        currentServer=infos.get(0);

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
                
                ConnectionData data =convertInputsToObject();
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
private ConnectionData convertInputsToObject() {
    ConnectionData data = new ConnectionData();
    data.setConnectionName(txtConnectionName.getText());
    data.setServerName(currentServer.getServerName());
    data.setServerClass(currentServer.getServerClass());
    data.setConnectionDriver(currentServer.getDriver());
    data.setDataSourceJNDI(txtDataSource.getText());
    data.setServerIP(txtServerIP.getText());
    data.setServerConsolePort(txtPort.getText());
    data.setUserName(txtUsername.getText());
    data.setPassword(new String(txtPassword.getPassword()));
    return data;
}
    private SavedConnection convertInputsToConnection() {
        SavedConnection data = new SavedConnection();
        data.setConnectionName(txtConnectionName.getText());
        data.setServerName(currentServer.getServerName());
        data.setDataSourceJNDI(txtDataSource.getText());
        data.setServerIP(txtServerIP.getText());
        data.setServerConsolePort(txtPort.getText());
        data.setUserName(txtUsername.getText());
        data.setPassword(new String(txtPassword.getPassword()));
        return data;
    }
    private List<ServerInfo> parseServersFile() {
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

    private void btnSaveConnection_actionPerformed(ActionEvent e) {
        if(txtConnectionName.getText().isEmpty())
        {JOptionPane.showMessageDialog(btnConnect,
                                          "Invalid Connection name",
                                          "Save Error",
                                          JOptionPane.ERROR_MESSAGE);
         return;
        }
        SavedConnection data=convertInputsToConnection();
        if(!data.validate())
        {
            JOptionPane.showMessageDialog(btnConnect,
                                          "Connection Details has missing info",
                                          "Save Error",
                                          JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        boolean alreadyExists=false;
        DefaultListModel<SavedConnection> model = (DefaultListModel<SavedConnection>)lstConnections.getModel();
        for(int i=0;i<model.getSize();i++) {
            SavedConnection connection = model.getElementAt(i);
            if(connection.getConnectionName().equalsIgnoreCase(data.getConnectionName())) {
                model.removeElementAt(i);
                model.add(i, data);
                alreadyExists=true;
                break;
            }
        }
        
        if(!alreadyExists)
            model.addElement(data);
                try {
        File f=new File(Configs.CONNECTIONS_FILE);
        if(!f.exists())
                f.createNewFile();
            
        PrintStream writer = new PrintStream(f);
        writer.println("<connections>");
        for(int i=0;i<model.getSize();i++) {
            SavedConnection conn=model.getElementAt(i);
            writer.print(conn.toXmlString());
        }
            writer.print("</connections>");
                    writer.flush();
                    writer.close();
        } catch (IOException g) {
            g.printStackTrace();
        }
    }

    private void initConnectionsData() {
    }
    private List<SavedConnection> parseConnectionsFile (){
        final ArrayList<SavedConnection> infos = new ArrayList<SavedConnection>(); // to
        
        XMLSAXParser h = new XMLSAXParser("connection") {

            @Override
            public void insertObjects(ArrayList<HashMap<String, String>> nodeList) {
                for (HashMap<String, String> node : nodeList) {
                    SavedConnection conn=new SavedConnection();
                    conn.setConnectionName(node.get("/connection/connectionName"));
                    conn.setServerIP(node.get("/connection/serverIP"));
                    conn.setServerName(node.get("/connection/serverName"));
                    conn.setServerConsolePort(node.get("/connection/serverConsolePort"));
                    conn.setDataSourceJNDI(node.get("/connection/dataSourceJNDI"));
                    conn.setUserName(node.get("/connection/userName"));
                    conn.setPassword(node.get("/connection/password"));
                    infos.add(conn);
                }

            }
        };
        try {
            SAXParserFactory.newInstance().newSAXParser().parse(new File(Configs.CONNECTIONS_FILE),
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

    private void lstConnections_mouseClicked(MouseEvent e) {
        SavedConnection con = (SavedConnection)lstConnections.getSelectedValue();
        txtConnectionName.setText(con.getConnectionName());
        for(int i=0;i<cmbServers.getModel().getSize();i++) {
            if(((ServerInfo)cmbServers.getModel().getElementAt(i)).getServerName().equalsIgnoreCase(con.getServerName()))
            {   
                cmbServers.setSelectedIndex(i);
                currentServer =
                        (ServerInfo)cmbServers.getModel().getSelectedItem();
                break;
            }
        }
        txtDataSource.setText(con.getDataSourceJNDI());
        txtServerIP.setText(con.getServerIP());
        txtPort.setText(con.getServerConsolePort());
        txtUsername.setText(con.getUserName());
        txtPassword.setText(con.getPassword());
    }
}
