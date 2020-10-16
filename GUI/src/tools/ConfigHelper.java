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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

/**
 * For all snakeYaml interfaces within the program, the documentation found at:
 * https://bitbucket.org/asomov/snakeyaml/wiki/Documentation is an invaluable tool.
 * 
 * @author nick
 **/
public class ConfigHelper {
  private static Map<String, Object> getConfigAsMap(String path) {
    try {
      return strToMapping(path);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;

  }

  /**
   * By inputting yaml file to be read's path, it will convert it from a string to a yaml mapping
   * object which can be parsed using the snameYaml library.
   * 
   * @param path
   * @return
   * @throws IOException
   * @author nick
   */
  @SuppressWarnings({"unchecked", "unused"})
  private static Map<String, Object> strToMapping(String path) throws IOException {
    String sb = FileIO.getStrFromFile(path);

    Map<String, Object> map = null;
    Yaml yaml = new Yaml();
    return map = (Map<String, Object>) yaml.load(sb);
  }

  /**
   * As suggested, returning a yaml mapping back into a yaml file via a string buffer.
   * 
   * @param map
   * @return
   * @author nick
   */
  private static String mappingToStr(Map<String, Object> map) {
    Yaml yaml = new Yaml();
    return yaml.dump(map);
  }

  /**
   * A yaml mapping that contains a list, can return the list objects give that the parent then
   * subset are 'Files' and 'Extensions' as reflected in the code, as well as the configuration yaml
   * file.
   * 
   * @param map
   * @return
   * @author nick
   */
  @SuppressWarnings("unchecked")
  public static List<Object> getExtensionList(Map<String, Object> map) {
    List<Object> list = null;
    Map<String, Object> parent = new HashMap<>();
    parent = (Map<String, Object>) map.get("Files");
    list = (List<Object>) parent.get("Extensions");
    return list;
  }

  /**
   * From the ExentionList Objects returned above, a particular list element can be returned back as
   * a map.
   * 
   * @param list
   * @return
   * @author nick
   */
  public static Map<String, Object> getExtensionMapping(List<Object> list) {
    Map<String, Object> map = new HashMap<>();
    map.put("Extensions", list);
    return map;

  }

  /**
   * Given the type of file, it checks the list for its type, then only returns a list of extentions
   * of that type.
   * 
   * @param type
   * @param list
   * @author nick
   */
  @SuppressWarnings({"unused", "unchecked"})
  private static void getAllExtOfType(String type, List<Object> list) {
    // Print out all types of extensions
    Map<String, Object> newList = null;
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

  @SuppressWarnings("unchecked")
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

  /**
   * When passing a mapping, a String can be passed to return the object contained by the string.
   * ex. {test={key={key:value, key:value}}} if mapping if above is provided with string "test"
   * should return {key={key:value, key:value}}
   * 
   * @param map
   * @param str
   * @return
   * @author nick
   */
  @SuppressWarnings("unchecked")
  public static Map<String, Object> getConfMapByPath(Map<String, Object> map, String str) {
    return (Map<String, Object>) map.get(str);
  }



  public static void saveMappingToFile(Map<String, Object> map, String savePath) {
    String sb = mappingToStr(map);
    FileIO.writeStrBufferToNewFile(savePath, sb);
    // TODO remove?
    System.out.println("Saved.");
  }

  /**
   * Sections below are specfic calls to the configuation yaml file
   * 
   * @author nick
   */

  public static Map<String, Object> loadConfig() {

    String defaultConfigPath =
        ".." + File.separator + "assets-dir" + File.separator + "config_default.yaml";

    // default saving path for the config file.
    String savePath = ".." + File.separator + "config.yaml";

    String path = "";
    boolean stateOfConfigFile = false;
    stateOfConfigFile = FileIO.checkIfFileExists(savePath);

    Map<String, Object> map = null;
    Map<String, Object> springServerMap = null;

    if (stateOfConfigFile) {
      path = savePath;
      map = getConfigAsMap(path);
      saveMappingToFile(map, savePath);
      springServerMap = ConfigHelper.getConfMapByPath(map, "SpringServerApplicationObject");
      return map;
    } else {
      System.out.println("Config file at: [" + savePath + "] Not found.");
      System.out.println("Grabbing default config.");
      path = defaultConfigPath;
      map = getConfigAsMap(path);
      saveMappingToFile(map, savePath);
      springServerMap = ConfigHelper.getConfMapByPath(map, "SpringServerApplicationObject");
      return map;
    }

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

  public static String setSeperators(String str) {
    String result = "";
    for (int i = 0; i < str.length(); i++) {
      char c = str.charAt(i);
      // "/ -> \\"
      if (c == '/') {
        c = File.separatorChar;
      }
      // "\\ -> /"
      else if (c == '\\') {
        c = File.separatorChar;
      }
      result += c;
    }

    return result;
  }

  public static String getSettingsDir(String key) {
    Map<String, Object> configMap = ConfigHelper.loadConfig();
    Map<String, Object> settingsMap = ConfigHelper.getConfMapByPath(configMap, "Settings");
    String result = settingsMap.get(key).toString();
    result = setSeperators(result);
    return result;
  }

  public static Map<String, Object> updateMap(Map<String, Object> map,
      Map<String, Object> springServerMap, Map<String, Object> settingsMap,
      Map<String, Object> extObjListMap) {
    System.out.println("Updating...");
    map.put("SpringServerApplicationObject", springServerMap);
    map.put("Settings", settingsMap);
    map.put("Files", extObjListMap);
    return map;

  }

}
