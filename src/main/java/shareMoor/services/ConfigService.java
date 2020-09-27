package shareMoor.services;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

public class ConfigService {
  private static Map<String, Object> getConfigAsMap(String path) {
    try {
      return strToMapping(path);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;

  }

  private static Map<String, Object> strToMapping(String path) throws IOException {
    String sb = FileIOService.getStrFromFile(path);

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
    // load config
    String defaultConfigPath =
        "src" + File.separator + "resources" + File.separator + "config_default.yaml";
    // default saving path for the config file.
    String activeConfigPath = ".." + File.separator + "config.yaml";
    String savePath = activeConfigPath;

    String path = "";

    boolean stateOfConfigFile = FileIOService.checkIfFileExists(activeConfigPath);

    if (stateOfConfigFile == true) {
      path = activeConfigPath;
    } else {
      System.out.println("Config file at: [" + activeConfigPath + "] Not found.");
      System.out.println("Grabbing default config.");
      path = defaultConfigPath;

    }

    Map<String, Object> map = getConfigAsMap(path);
    if (!stateOfConfigFile) {
      saveMappingToFile(map, savePath);
    }

    return map;

  }

  public static void saveMappingToFile(Map<String, Object> map, String savePath) {
    String sb = mappingToStr(map);
    FileIOService.writeStrBufferToNewFile(savePath, sb);
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
    Map<String, Object> configMap = ConfigService.loadConfig();
    Map<String, Object> settingsMap = ConfigService.getConfMapByPath(configMap, "Settings");
    String result = settingsMap.get(key).toString();
    // System.out.println("Before: " + result);
    for (int i = 0; i < result.length(); i++) {
      char c = result.charAt(i);
      if (c == '/' || c == '\\') {
        c = File.separatorChar;
      }
    }
    // System.out.println("After: " + result);
    return result;
  }

}
