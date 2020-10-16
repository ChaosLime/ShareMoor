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

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Map;

public class Networking {

  private String ipAddr;
  private static String protocol;
  private static String port;

  public Networking(String ipAddr, String protocol, String port) {
    super();
    this.ipAddr = ipAddr;
    Networking.protocol = protocol;
    Networking.port = port;
  }

  /*
   * The addresses should not be a loopback, must be UP, and have a MAC address that is not null See
   * https://stackoverflow.com/questions/2381316/java-inetaddress-getlocalhost-returns-127-0-0-1-how
   * -to-get-real-ip for more details
   */
  public static String getHostAddress() {
    String HostAddresses = "";
    try {
      for (NetworkInterface ni : Collections.list(NetworkInterface.getNetworkInterfaces())) {
        if (!ni.isLoopback() && ni.isUp() && ni.getHardwareAddress() != null) {
          for (InterfaceAddress ia : ni.getInterfaceAddresses()) {
            if (ia.getBroadcast() != null) { // If limited to IPV4
              HostAddresses = ia.getAddress().getHostAddress();
            }
          }
        }
      }
    } catch (SocketException e) {
    }
    return HostAddresses;
  }

  // TODO: move to ConfigHelper.
  public static String getPort() {

    Map<String, Object> map = ConfigHelper.loadConfig();
    Map<String, Object> springServerMap =
        ConfigHelper.getConfMapByPath(map, "SpringServerApplicationObject");
    Map<String, Object> serverMap = ConfigHelper.getConfMapByPath(springServerMap, "server");
    port = serverMap.get("port").toString();
    return port;
  }

  public static void setPort(String port) {
    Networking.port = port;
  }

  // TODO: move to ConfigHelper.
  public static String getProtocol() {
    Map<String, Object> map = ConfigHelper.loadConfig();
    Map<String, Object> settingsMap = ConfigHelper.getConfMapByPath(map, "Settings");

    if ((boolean) settingsMap.get("https")) {
      return "https";
    } else {
      return "http";
    }

  }

  public static void setProtocol(String protocol) {
    Networking.protocol = protocol;
  }

  public String getIpAddr() {
    return ipAddr;
  }

  public void setIpAddr(String ipAddr) {
    this.ipAddr = ipAddr;
  }


  public static String getFullAddress() {
    String address = getHostAddress();
    port = getPort();
    protocol = getProtocol();
    String result = protocol + "://" + address + ":" + port;
    return result;
  }

  public String getHostName() {
    try {
      return InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
    return "[unknown hostname]";

  }

}
