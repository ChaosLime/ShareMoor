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
package shareMoor.domain;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;


public class ConfigHandler {
  private static Map<String, Object> getConfigAsMap(String path) {
    try {
      return strToMapping(path);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private static Map<String, Object> strToMapping(String path) throws IOException {
    String sb = FileIO.getStrFromFile(path);
    Map<String, Object> map = null;
    Yaml yaml = new Yaml();
    return map = (Map<String, Object>) yaml.load(sb);
  }

  private static String mappingToStr(Map<String, Object> map) {
    Yaml yaml = new Yaml();
    return yaml.dump(map);
  }

  public static List<Object> getExtensionList(Map<String, Object> map) {
    List<Object> list = null;
    Map<String, Object> parent = new HashMap<>();
    parent = (Map<String, Object>) map.get("Files");
    list = (List<Object>) parent.get("Extensions");
    return list;
  }

  public static Map<String, Object> getExtensionMapping(List<Object> list) {
    Map<String, Object> map = new HashMap<>();
    map.put("Extensions", list);
    return map;
  }

  @SuppressWarnings("unused")
  private static void getAllExtOfType(final String type, final List<Object> list) {
    // Print out all types of extensions
    for (int i = 0; i < list.size(); i++) {
      Map<String, Object> extensionObj = (Map<String, Object>) list.get(i);

      String currentKey = "";
      if (extensionObj != null) {
        for (String key : extensionObj.keySet()) {
          currentKey = key;
        }
        Map<String, Object> extension = (Map<String, Object>) extensionObj.get(currentKey);
      }
    }
  }

  public static Map<String, Object> findByExt(String ext, List<Object> list) {
    for (int i = 0; i < list.size(); i++) {
      Map<String, Object> extensionObj = (Map<String, Object>) list.get(i);

      if (extensionObj.get(ext) != null) {
        Map<String, Object> extension = (Map<String, Object>) extensionObj.get(ext);
        return extension;
      }
    }
    return null;
  }

  /*
   * When passing a mapping, a String can be passed to return the object contained by the string.
   * ex. {test={key={key:value, key:value}}} if mapping if above is provided with string "test"
   * should return {key={key:value, key:value}}
   */
  public static Map<String, Object> getConfMapByPath(Map<String, Object> map, String str) {
    return (Map<String, Object>) map.get(str);
  }

  public static Map<String, Object> loadConfig() {
    String savePath = ".." + File.separator + "config.yaml";
    String path = "";

    boolean stateOfConfigFile = FileIO.checkIfFileExists(savePath);

    if (stateOfConfigFile == true) {
      path = savePath;
    } else {
      System.out.println("Config file at: [" + savePath + "] Not found.");
      System.out.println("Grabbing default config.");
    }

    Map<String, Object> map = getConfigAsMap(path);
    if (!stateOfConfigFile) {
      saveMappingToFile(map, savePath);
    }
    return map;
  }

  public static boolean checkExtStatus(String ext) {
    List<Object> list = ConfigHandler.getExtList();
    Map<String, Object> resultMap = ConfigHandler.findByExt(ext, list);
    if (resultMap == null) {
      System.out.println("Extension [" + ext + "] not found on extensions list.");
    } else {
      String status = resultMap.get("status").toString();
      if (status.equals("true")) {
        return true;
      }
    }
    return false;
  }

  public static boolean checkTypeStatus(String type) {
    if (type == null) {
      System.out.println("Type [" + type + "] not found.");
      return false;
    }
    String result = ConfigHandler.getSettingsDir(type);
    if (result.equals("true")) {
      return true;
    }
    System.out.println("Type [" + type + "] not allowed.");
    return false;
  }

  public static String checkExtType(String ext) {
    List<Object> list = ConfigHandler.getExtList();
    Map<String, Object> resultMap = ConfigHandler.findByExt(ext, list);
    if (resultMap == null) {
      System.out.println("No type found for [" + ext + "].");
    } else {
      String type = resultMap.get("type").toString();
      return type;
    }
    return null;
  }


  public static List<Object> getExtList() {
    Map<String, Object> map = ConfigHandler.loadConfig();
    List<Object> list = ConfigHandler.getExtensionList(map);
    return list;
  }

  public static void saveMappingToFile(Map<String, Object> map, String savePath) {
    String sb = mappingToStr(map);
    FileIO.writeStrBufferToNewFile(savePath, sb);
    System.out.println("Saved.");
  }

  public static String getSSID() {
    Map<String, Object> map = loadConfig();
    Map<String, Object> settingsMap = getConfMapByPath(map, "Settings");
    String ssid = settingsMap.get("SSID").toString();
    return ssid;
  }

  public static String getWifiPass() {
    Map<String, Object> map = loadConfig();
    Map<String, Object> settingsMap = getConfMapByPath(map, "Settings");
    String pass = settingsMap.get("wifiPass").toString();
    return pass;
  }

  public static String getEncryptionType() {
    Map<String, Object> map = loadConfig();
    Map<String, Object> settingsMap = getConfMapByPath(map, "Settings");
    String type = settingsMap.get("wifiType").toString();
    return type;
  }

  public static String getSettingsDir(String key) {
    Map<String, Object> configMap = ConfigHandler.loadConfig();
    Map<String, Object> settingsMap = ConfigHandler.getConfMapByPath(configMap, "Settings");
    String result = settingsMap.get(key).toString();
    for (int i = 0; i < result.length(); i++) {
      char c = result.charAt(i);
      if (c == '/' || c == '\\') {
        c = File.separatorChar;
      }
    }
    return result;
  }

  public static String getSettingsValue(String key) {
    Map<String, Object> configMap = loadConfig();
    Map<String, Object> settingsMap = getConfMapByPath(configMap, "Settings");
    return settingsMap.get(key).toString();
  }

  public static String getServerValue(String key) {
    Map<String, Object> configMap = loadConfig();
    Map<String, Object> springServerMap =
        getConfMapByPath(configMap, "SpringServerApplicationObject");
    Map<String, Object> serverMap = getConfMapByPath(springServerMap, "server");
    return serverMap.get(key).toString();
  }

  // TODO: remove when going through refactor of class.
  public static String getPort() {
    // TODO: reduce loading of config here.
    Map<String, Object> map = loadConfig();
    Map<String, Object> springServerMap = getConfMapByPath(map, "SpringServerApplicationObject");
    Map<String, Object> serverMap = getConfMapByPath(springServerMap, "server");

    return serverMap.get("port").toString();
  }

  public static String getProtocol() {
    // TODO: remove when going through refactor of class.
    Map<String, Object> map = ConfigHandler.loadConfig();
    Map<String, Object> settingsMap = getConfMapByPath(map, "Settings");
    // TODO: fix up and reduce code reuse.
    // System.out.println(getSettingsValue("https"));
    if ((boolean) settingsMap.get("https")) {
      return "https";
    } else {
      return "http";
    }
  }

  public static String getFullAddress() {
    String address = getServerValue("address");
    String port = getServerValue("port");
    String protocol = getProtocol();
    String result = protocol + "://" + address + ":" + port;
    return result;
  }

}
