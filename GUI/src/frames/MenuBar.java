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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

public class MenuBar {
  public static JMenuBar createMenuBar() {
    JMenuBar menuBar;
    JMenu menu, submenu;
    JMenuItem menuItem;

    menuBar = new JMenuBar();

    menu = new JMenu("Menu");
    menu.setMnemonic(KeyEvent.VK_A);
    menu.getAccessibleContext()
        .setAccessibleDescription("The only menu in this program that has menu items");
    menuBar.add(menu);
    menuItem = new JMenuItem("Settings", KeyEvent.VK_T);
    menuItem.setMnemonic(KeyEvent.VK_T); // used constructor instead
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));

    menuItem.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");
    menu.add(menuItem);

    menuItem = new JMenuItem("Help", KeyEvent.VK_T);
    menuItem.setMnemonic(KeyEvent.VK_T); // used constructor instead
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));


    menuItem.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");
    menu.add(menuItem);

    submenu = new JMenu("Configuration");
    submenu.setMnemonic(KeyEvent.VK_S);

    menuItem = new JMenuItem("Import");
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
    submenu.add(menuItem);

    menuItem = new JMenuItem("Export");
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));
    submenu.add(menuItem);
    menu.add(submenu);

    submenu.addSeparator();

    menuItem = new JMenuItem("Default");
    submenu.add(menuItem);
    menu.add(submenu);

    menu.addSeparator();

    menuItem = new JMenuItem("Quit");
    menu.add(menuItem);

    menuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        if (JOptionPane.showConfirmDialog(null, "Are you sure you want to Quit?", "WARNING",
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
          System.exit(0);
        }
      }
    });

    return menuBar;
  }
}
