/*
 * This file is part of Share Moor
 * 
 * Share Moor is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * Share Moor is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Share Moor. If not,
 * see <https://www.gnu.org/licenses/>.
 */
package frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import tools.Checker;
import tools.ConfigHelper;
import tools.Events;
import tools.Networking;

public class TabbedPanes extends JPanel {

  private static final long serialVersionUID = 1L;
  public static JPanel configPanel;

  public TabbedPanes() {
    super(new GridLayout(1, 1));

    JTabbedPane tabbedPane = new JTabbedPane();

    // TODO: Adjust sizes of tabs?
    // TODO: allow tabs to be exited and reopened from menu bar?

    JPanel panel1 = new JPanel();
    mainPanelSetup(panel1);
    panel1.setLayout(new FlowLayout());

    JScrollPane scrollerPane1 = new JScrollPane(panel1);
    scrollerPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollerPane1.getVerticalScrollBar().setUnitIncrement(16);



    tabbedPane.addTab("Welcome", null, scrollerPane1, "Welcome!!");
    tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);


    JPanel panel2 = new JPanel(new BorderLayout());
    panel2 = editConfigTabSetup(panel2);

    JScrollPane scrollerPane2 = new JScrollPane(panel2);
    scrollerPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollerPane2.getVerticalScrollBar().setUnitIncrement(16);

    tabbedPane.addTab("Edit Config", null, scrollerPane2, "Configure and Edit");
    tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);


    JPanel panel3 = new JPanel(new BorderLayout());

    panel3.setLayout(new BoxLayout(panel3, BoxLayout.Y_AXIS));
    aboutTabSetup(panel3);

    JScrollPane scrollerPane3 = new JScrollPane(panel3);
    scrollerPane3.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollerPane3.getVerticalScrollBar().setUnitIncrement(16);

    tabbedPane.addTab("About", null, scrollerPane3, "About");
    tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);



    JPanel panel4 = new JPanel();
    helpTabSetup(panel4);
    // panel4.setLayout(new BoxLayout(panel4, BoxLayout.Y_AXIS));

    JScrollPane scrollerPane4 = new JScrollPane(panel4);
    scrollerPane4.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollerPane4.getVerticalScrollBar().setUnitIncrement(16);

    tabbedPane.addTab("Help", null, scrollerPane4, "Need some Help?");
    tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);

    // Add the tabbed pane to this panel.
    add(tabbedPane);

    // The following line enables to use scrolling tabs.
    tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
  }


  private String getMainPanelText() {
    // TODO: move this to an external file or handle this as a swing object?
    return "Welcome to Share Moor!\n" + "This program is a simple file sharing program.\n"
        + "Please check out and configure the server in the tab above.\n"
        + "If any help is required, please check out the help tab\n"
        + "or see the additional documentation.";
  }

  // tab1
  private void mainPanelSetup(JPanel panel) {

    JTextPane textPane = new JTextPane();
    textPane.setEditable(false);
    textPane.isBackgroundSet();
    panel.add(textPane, BorderLayout.CENTER);

    textPane.setText(getMainPanelText());
    textPane.setEditable(false);
    textPane.isOpaque();
    textPane.setHighlighter(null);

    panel.add(textPane);
  }

  // tab2
  /**
   * This tab is the configuration editor page. Logic should be moved out of the tab into its out
   * class.
   * 
   * @param panel
   * @return
   */
  private JPanel editConfigTabSetup(JPanel panel) {

    // TODO: abstract getting parts of map object.
    Map<String, Object> map = ConfigHelper.loadConfig();

    Map<String, Object> springServerMap =
        ConfigHelper.getConfMapByPath(map, "SpringServerApplicationObject");
    Map<String, Object> SpringMap = ConfigHelper.getConfMapByPath(springServerMap, "spring");
    Map<String, Object> servletMap = ConfigHelper.getConfMapByPath(SpringMap, "servlet");
    Map<String, Object> multipartMap = ConfigHelper.getConfMapByPath(servletMap, "multipart");
    Map<String, Object> serverMap = ConfigHelper.getConfMapByPath(springServerMap, "server");
    Map<String, Object> settingsMap = ConfigHelper.getConfMapByPath(map, "Settings");

    // get settings info
    String savePath = settingsMap.get("mainConfig-dir").toString();
    String wifiType = settingsMap.get("wifiType").toString();
    String ipAddr = Networking.getHostAddress();
    serverMap.put("address", ipAddr);

    // Setting Up Save/Update control panel
    JPanel btnPanel = new JPanel();
    JLabel buttonLabel = new JLabel("Double click button to update.");
    JButton saveBtn = new JButton("Update");

    btnPanel.add(buttonLabel);
    btnPanel.add(saveBtn);

    // Setting Up Item Config panel
    JPanel configOptionPanel = new JPanel();
    configOptionPanel.setLayout(new GridLayout(0, 1));

    // Componetents
    JLabel wifiLbl = new JLabel("Wifi QR code:");
    configOptionPanel.add(wifiLbl);

    stringInputStringSave(configOptionPanel, settingsMap, saveBtn, "SSID", "Network name (SSID):");

    stringInputStringSave(configOptionPanel, settingsMap, saveBtn, "wifiPass", "Network password:");

    jCheckBoxtoMap(configOptionPanel, settingsMap, saveBtn, "wifiType", wifiType);

    JLabel fileLbl = new JLabel("Enabled File Types:");
    configOptionPanel.add(fileLbl);

    List<Object> extObjList = ConfigHelper.getExtensionList(map);
    checkBox(configOptionPanel, settingsMap, "photo", "Photo", extObjList);
    checkBox(configOptionPanel, settingsMap, "video", "Video", extObjList);
    checkBox(configOptionPanel, settingsMap, "audio", "Audio", extObjList);
    checkBox(configOptionPanel, settingsMap, "document", "Document", extObjList);

    // Gets all extentions from the list, and passed them into
    // boolean photoStatus = Checker.getFileTypeStatus("photo", settingsMap);
    // generateExtCheckboxes(extObjList, panel, photoStatus);
    Map<String, Object> extObjListMap = ConfigHelper.getExtensionMapping(extObjList);

    JLabel advLbl = new JLabel("Advanced Options:");
    configOptionPanel.add(advLbl);

    browseFileSystem(configOptionPanel, settingsMap, "processedFull-dir");
    browseFileSystemForFile(configOptionPanel, settingsMap, "contactInfo-dir");
    // browseFileSystem(configOptionPanel, settingsMap, "upload-dir");
    // browseFileSystem(configOptionPanel, settingsMap, "denied-dir");
    // browseFileSystem(configOptionPanel, settingsMap, "reviewThumb-dir");
    // browseFileSystem(configOptionPanel, settingsMap, "processedThumb-dir");
    // browseFileSystemForFile(configOptionPanel, settingsMap, "mainConfig-dir");

    // checkBox(configOptionPanel, settingsMap, "https", "Enable SSL (https)", null);
    checkBox(configOptionPanel, settingsMap, "allowUserContacts",
        "Allow collection of user contacts", null);
    checkBox(configOptionPanel, settingsMap, "deleteUserContacts", "Delete Existing Contacts",
        null);
    checkBox(configOptionPanel, settingsMap, "deleteOldFiles", "Delete old files", null);
    checkBox(configOptionPanel, settingsMap, "autoApprove", "Auto Approve Uploads", null);

    // stringInputStringSave(panel, multipartMap, updateBtn, "max-file-size", "Max File Size(MB):");
    // stringInputStringSave(panel, multipartMap, updateBtn, "max-request-size",
    // "Max Request Size(MB):");
    stringInputStringSave(configOptionPanel, serverMap, saveBtn, "port", "Server Port:");

    lockConfigPanel(panel);

    panel.add(configOptionPanel, BorderLayout.PAGE_START);
    panel.add(btnPanel);
    // TODO remove. replace with user prompt to double click rather than using event mouse hover.
    /*
     * saveBtn.addMouseListener(new java.awt.event.MouseAdapter() { public void
     * mouseEntered(java.awt.event.MouseEvent evt) { saveBtn.doClick(); } });
     */
    Events.saveButtonEvent(saveBtn, savePath, map, springServerMap, settingsMap, extObjListMap);

    return panel;
  }


  private String getAboutText() {
    // TODO: move this to an external file or handle this as a swing object?
    return " Share Moor is a privacy centered crowdsourced file sharing tool.\n"
        + "    Copyright (C) 2020  Mitchell and Nicholas Saunders\n" + "\n"
        + "    This program is free software: you can redistribute it and/or modify\n"
        + "    it under the terms of the GNU General Public License as published by\n"
        + "    the Free Software Foundation, either version 3 of the License, or\n"
        + "    (at your option) any later version.\n" + "\n"
        + "    This program is distributed in the hope that it will be useful,\n"
        + "    but WITHOUT ANY WARRANTY; without even the implied warranty of\n"
        + "    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n"
        + "    GNU General Public License for more details.\n" + "\n"
        + "    You should have received a copy of the GNU General Public License\n"
        + "    along with this program.  If not, see <https://www.gnu.org/licenses/>.";
  }

  // tab3
  private void aboutTabSetup(JPanel panel) {

    JTextPane textPane = new JTextPane();

    JLabel copyingLbl = new JLabel();
    copyingLbl.setText("License:");

    JLabel projectPreviewLbl = new JLabel();
    projectPreviewLbl.setText("Project Page:");

    JLabel projectHyperLink = new JLabel();
    projectHyperLink.setText("<html><a>https://github.com/ChaosLime/ShareMoor</a></html>");
    projectHyperLink.setForeground(Color.BLUE);
    projectHyperLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

    projectHyperLink.addMouseListener(new MouseAdapter() {

      // TODO: move to events class
      @Override
      public void mouseClicked(MouseEvent e) {
        try {

          Desktop.getDesktop().browse(new URI("https://github.com/ChaosLime/ShareMoor"));

        } catch (IOException | URISyntaxException e1) {
          e1.printStackTrace();
        }
      }

    });


    textPane.setEditable(false);
    textPane.isBackgroundSet();

    textPane.setText(getAboutText());
    textPane.setEditable(false);
    textPane.isOpaque();
    textPane.setHighlighter(null);


    panel.setLayout(new BorderLayout());
    panel.add(copyingLbl, BorderLayout.PAGE_START);
    panel.add(textPane, BorderLayout.CENTER);
    panel.add(projectHyperLink, BorderLayout.SOUTH);

  }

  private String getHelpText() {
    // TODO: move this to an external file or handle this as a swing object?
    return "This section will contain general tips and tricks to use the software.\n"
        + "At this time, if there are any issues,\ncheck out the project page found on the about tab.";
  }

  // tab4
  private void helpTabSetup(JPanel panel) {
    JTextPane textPane = new JTextPane();

    textPane.setText(getHelpText());
    textPane.setEditable(false);
    textPane.isOpaque();
    textPane.setHighlighter(null);

    panel.add(textPane, BorderLayout.CENTER);

  }

  public static void getConfigPanel(JButton btn, boolean b) {
    Events.togglePanel(btn, configPanel, b);
  }

  /**
   * All functions below should be in their own class. TODO: move functions below. Only panel and
   * tab config should remain
   * 
   * @author nick
   */

  private void lockConfigPanel(JPanel panel) {
    configPanel = panel;
  }

  // Contains a mapping all objects by label of extension.
  static Map<String, Object> mapOfCheckBoxes = new HashMap<String, Object>();

  public static void setCheckBoxStatus(JCheckBox c, JPanel p, boolean status, String label) {
    c.setSelected(status);
    mapOfCheckBoxes.put(label, c);
  }

  public static void setExtCheckboxesState(List<Object> list, boolean status, JCheckBox c,
      JPanel panel) {
    for (int i = 0; i < list.size(); i++) {
      Map<String, Object> tempMap = (Map<String, Object>) list.get(i);
      for (Map.Entry<String, Object> entry : tempMap.entrySet()) {

        Map<String, Object> temp = ConfigHelper.findByExt(entry.getKey(), list);
        String key = entry.getKey();

        JCheckBox Obj = (JCheckBox) mapOfCheckBoxes.get(key);
        panel.remove(Obj);

        String type = "photo";
        if (temp.get("type").equals(type)) {
          // System.out.println(key + ": of type: [" + type + "] found.");
        }
      }
    }

    panel.updateUI();
    generateExtCheckboxes(list, panel, status);
  }

  /**
   * Generates checkboxes by a list of Objects. Currently only handles the check for photo types,
   * but it should be made generic TODO: abstract photoStatus to a generic status call.
   * 
   * @param list
   * @param panel
   * @param photoStatus
   */
  public static void generateExtCheckboxes(List<Object> list, JPanel panel, boolean photoStatus) {
    for (int i = 0; i < list.size(); i++) {
      Map<String, Object> tempMap = (Map<String, Object>) list.get(i);
      for (Map.Entry<String, Object> entry : tempMap.entrySet()) {
        Map<String, Object> temp = ConfigHelper.findByExt(entry.getKey(), list);
        // NOTE: if you set the temp here,all of the temps can be get the same.
        // i.e. temp.put("status",false); given map.put(key, true);
        // will set all extentions status to false;
        if (photoStatus) {
          temp.put("status", true);
        } else {
          temp.put("status", false);
        }
        checkBox(panel, temp, "status", entry.getKey(), list);

      }
    }
  }

  public static void checkBox(JPanel panel, Map<String, Object> map, String key, String label,
      List<Object> list) {
    boolean status = (boolean) map.get(key);
    // check if checkbox already exists
    JCheckBox c = new JCheckBox();
    c.setText(label);
    setCheckBoxStatus(c, panel, status, label);

    panel.add(c);

    checkBoxEvent(c, map, key, label, panel, list);
  }

  public static void browseFileSystem(JPanel panel, Map<String, Object> map, String key) {
    JLabel label = new JLabel(key + " : [" + map.get(key).toString() + "]");
    JButton browseBtn = new JButton("Browse");
    // JFileChooser fc = new JFileChooser();
    panel.add(label);
    panel.add(browseBtn);

    browseBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int option = fileChooser.showOpenDialog(panel);
        if (option == JFileChooser.APPROVE_OPTION) {
          File file = fileChooser.getSelectedFile();
          String path = file.getAbsolutePath() + File.separator + key;
          System.out.println("Full Path: [" + path + "]");
          label.setText(key + " : [" + path + "]");
          map.put(key, path);

        } else {
          label.setText("Open command canceled");
        }
      }
    });

  }

  public static void browseFileSystemForFile(JPanel panel, Map<String, Object> map, String key) {
    JLabel label = new JLabel(key + " : [" + map.get(key).toString() + "]");
    JButton browseBtn = new JButton("Browse");
    // JFileChooser fc = new JFileChooser();
    panel.add(label);
    panel.add(browseBtn);

    browseBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int option = fileChooser.showOpenDialog(panel);
        if (option == JFileChooser.APPROVE_OPTION) {
          File file = fileChooser.getSelectedFile();
          String path = file.getAbsolutePath() + File.separator;
          System.out.println("Full Path: [" + path + "]");
          label.setText(key + " : [" + path + "]");
          map.put(key, path);

        } else {
          label.setText("Open command canceled");
        }
      }
    });

  }

  public static void jCheckBoxtoMap(JPanel panel, Map<String, Object> map, JButton button,
      String key, String current) {

    String[] encryptionTypes = new String[] {"WPA3", "WPA2", "WPA1", "WEP", "none"};

    JComboBox<String> encryptionList = new JComboBox<>(encryptionTypes);

    encryptionList.setSelectedItem(current);
    encryptionList.setMaximumSize(new Dimension(500, 200));
    panel.add(encryptionList);

    button.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent arg0) {
        String str = encryptionList.getSelectedItem().toString();
        map.put(key, str);

      }
    });

  }


  // generic text field.
  public static void stringInputStringSave(JPanel panel, Map<String, Object> map, JButton button,
      String key, String labelStr) {
    JLabel label = new JLabel(labelStr);
    JTextField jt = new JTextField();

    jt.setText(map.get(key).toString());
    jt.setMaximumSize(new Dimension(500, 200));
    panel.add(label);
    panel.add(jt);

    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        map.put(key, jt.getText());

      }

    });

  }

  public static void checkBoxEvent(JCheckBox checkBox, Map<String, Object> map, String key,
      String label, JPanel panel, List<Object> list) {
    checkBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        boolean photoStatus = false;
        if (checkBox.isSelected()) {
          System.out.println("Enabled: [" + label + "]");
          map.put(key, true);
          if (key == "photos") {
            photoStatus = Checker.getFileTypeStatus("photos", map);
            updateCheckboxes(checkBox, list, panel, photoStatus);

          }
        }
        if (!checkBox.isSelected()) {
          System.out.println("Disabled: [" + label + "]");
          map.put(key, false);
          if (key == "photos") {
            photoStatus = Checker.getFileTypeStatus("photos", map);
            updateCheckboxes(checkBox, list, panel, photoStatus);
          }
        }

      }

      private void updateCheckboxes(JCheckBox c, List<Object> list, JPanel panel, boolean status) {
        setExtCheckboxesState(list, status, c, panel);
      }

    });
  }

}
