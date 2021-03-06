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
package tools;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import frames.StatusBar;
import frames.TabbedPanes;

public class Events {
  private static long PID = 0;
  private static boolean SERVERSTATUS = false;
  // public static boolean configUpdated = false;
  private static boolean CONFIGUPDATESTATUS = false;

  public static void startButtonEvent(JButton button) {
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {

        if (button.getText() == "Start" && CONFIGUPDATESTATUS) {
          SERVERSTATUS = true;

          GenerateQRCode.webSiteAddress(Networking.getFullAddress().toString());
          GenerateQRCode.wifiAccess();

          String fullAddress = Networking.getFullAddress();
          JOptionPane.showMessageDialog(null, "Server has started at " + fullAddress);

          Map<String, Object> map = ConfigHelper.loadConfig();
          Map<String, Object> settingsMap = ConfigHelper.getConfMapByPath(map, "Settings");

          String serverJarPath = settingsMap.get("serverJar").toString();
          serverJarPath = ConfigHelper.setSeperators(serverJarPath);
          String appPropPath = " --spring.config.location=.." + File.separator + "application.yaml";

          PID = CrossPlatformTools.callExternalProgram("java -jar ", serverJarPath + appPropPath);

          StatusBar.setRunningIndicator(true);
          lockConfigPanel(button, false);

          System.out.println("Server [Start]");
          button.setText("Stop");

        } else if (!CONFIGUPDATESTATUS) {
          JOptionPane.showMessageDialog(null,
              "Configuration is out of date. Please check and update before starting.");
        } else if (button.getText() == "Stop") {
          if (JOptionPane.showConfirmDialog(null, "Are you sure you want to stop the program?",
              "WARNING", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            SERVERSTATUS = false;
            CrossPlatformTools.killProgram("kill ", PID);
            PID = 0;

            StatusBar.setRunningIndicator(false);
            lockConfigPanel(button, true);

            button.setText("Start");
            System.out.println("Server [Stop]");

          } else {
            button.setText("Stop");
            System.out.println("Server [Idle]");
          }
        }
      }
    });
  }

  public static void goToSiteButtonEvent(JButton button) {
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {

        if (isSERVERSTATUS()) {

          String link = Networking.getFullAddress();
          // Opens default browser and opens websites landing page.
          Desktop desktop = java.awt.Desktop.getDesktop();
          try {

            URI oURL = new URI(link);
            desktop.browse(oURL);
          } catch (URISyntaxException | IOException e1) {
            e1.printStackTrace();
          }

        } else {
          JOptionPane.showMessageDialog(null,
              "Site is currently not running. Please start the site before navigating to it.");
        }
      }
    });

  }

  public static void saveButtonEvent(JButton button, String path, Map<String, Object> map,
      Map<String, Object> springServerMap, Map<String, Object> settingsMap,
      Map<String, Object> extObjListMap) {


    button.addActionListener(new ActionListener() {


      public void actionPerformed(ActionEvent ae) {

        if (CONFIGUPDATESTATUS == true) {
          if (JOptionPane.showConfirmDialog(null, "Save changes to config file?", "WARNING",
              JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            Map<String, Object> resultMap =
                ConfigHelper.updateMap(map, springServerMap, settingsMap, extObjListMap);

            JOptionPane.showMessageDialog(null,
                "The config and its changes have been saved to: " + path);
            ConfigHelper.saveMappingToFile(resultMap, path + "config.yaml");
            Map<String, Object> springServerMap =
                ConfigHelper.getConfMapByPath(resultMap, "SpringServerApplicationObject");
            ConfigHelper.saveMappingToFile(springServerMap,
                ".." + File.separator + "application.yaml");

            GenerateQRCode.webSiteAddress(Networking.getFullAddress().toString());
            GenerateQRCode.wifiAccess();

            CONFIGUPDATESTATUS = true;

          } else {
            System.out.println("File not Saved.");
            CONFIGUPDATESTATUS = false;
          }

        } else {
          CONFIGUPDATESTATUS = true;
        }

      }

    });

  }

  public static boolean isSERVERSTATUS() {
    return SERVERSTATUS;
  }

  public static void togglePanel(JButton button, JPanel panel, boolean b) {
    setPanelEnabled(panel, b);
  }

  // locks all elements of a panel
  static void setPanelEnabled(JPanel panel, boolean b) {
    panel.setEnabled(b);
    panel.getUI();
    Component[] components = panel.getComponents();

    for (int i = 0; i < components.length; i++) {
      if (components[i].getClass().getName() == "javax.swing.JPanel") {
        setPanelEnabled((JPanel) components[i], b);
      }

      components[i].setEnabled(b);
    }
  }

  public static void lockConfigPanel(JButton btn, boolean b) {
    TabbedPanes.getConfigPanel(btn, b);
  }



}
