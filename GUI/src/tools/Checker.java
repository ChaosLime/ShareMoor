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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Just a easy way to check the object type. Useful to testing.
 * 
 * @author nick
 *
 */
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
