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
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import tools.Events;
import tools.Networking;

public class StatusBar extends JPanel {

  private static final long serialVersionUID = 1L;
  private int count = 0;
  private static JLabel runningLabel = new JLabel("[Not Running]");
  private static Timer timer;
  private static JLabel spinnerLabel = new JLabel("");

  public StatusBar() {
    // TODO: display address here on status bar?
    JLabel networkLabel = new JLabel(Networking.getFullAddress());

    JButton serverBtn = new JButton("Start");
    JButton goToSiteBtn = new JButton("Go To Site");

    add(spinnerLabel);
    add(runningLabel);
    // add(networkLabel);
    add(serverBtn);
    add(goToSiteBtn);

    Events.startButtonEvent(serverBtn);
    // Events.lockConfigPanel(serverBtn);
    Events.goToSiteButtonEvent(goToSiteBtn);

    timer = new Timer(100, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        count++;

        spinnerLabel.setText("[" + loadingWheel(count).toString() + "]");
      }
    });

  }

  public static void setRunningIndicator(boolean b) {
    if (b) {
      runningLabel.setText("[Running]");
      setSpinnerTimer(b);
    } else {
      runningLabel.setText("[Not Running]");
      setSpinnerTimer(b);
    }
  }

  private static void setSpinnerTimer(boolean b) {
    if (b) {
      timer.start();

    } else {
      timer.stop();
      setSpinnerLabel(b);
    }
  }

  // removes spinner if not currently in running state.
  private static void setSpinnerLabel(boolean b) {
    if (!b) {
      spinnerLabel.setText("");
    }
  }

  public static String loadingWheel(int count) {
    String spin = "-\\|/";
    return spin.charAt(count % 4) + "\r";

  }

}
