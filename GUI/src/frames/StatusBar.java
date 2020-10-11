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
