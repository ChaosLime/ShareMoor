package tools;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Checker {
  public static String dataType(Object obj) {

    if (obj.getClass() == String.class) {
      return "String";
    }
    if (obj.getClass() == Boolean.class) {
      return "Boolean";
    }
    if (obj.getClass() == ArrayList.class) {
      return "ArrayList";
    }

    if (obj.getClass() == LinkedHashMap.class) {
      return "LinkedHashMap";
    }

    return null;
  }

  public static boolean getFileTypeStatus(String type, Map<String, Object> map) {

    return (boolean) map.get(type);

  }

}
