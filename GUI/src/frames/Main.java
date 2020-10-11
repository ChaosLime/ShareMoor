package frames;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import tools.ConfigHelper;
import tools.CrossPlatformTools;
import tools.Events;

public class Main extends JFrame {

  private static final long serialVersionUID = 1L;
  static Dimension DIMENSION = new Dimension(720, 480);

  public static Dimension getDimension() {
    return DIMENSION;
  }

  @SuppressWarnings("static-access")
  public void setDimension(Dimension dimension) {
    this.DIMENSION = dimension;
  }

  public static void addComponentsToPane(Container pane, JFrame frame) {

    pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

    JPanel subPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    StatusBar statusBar = new StatusBar();
    subPane.add(statusBar);

    pane.add(subPane);
    pane.add(new TabbedPanes());
  }


  public static void createAndShowGUI() {
    JFrame frame = new JFrame("Share Moor");
    // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        handleClosing(frame);
      }
    });

    ImageIcon img =
        new ImageIcon(ConfigHelper.getSettingsDir("assests-dir") + File.separator + "icon.png");
    frame.setIconImage(img.getImage());
    // frame.setJMenuBar(MenuBar.createMenuBar());

    // creates a container which all contents of the window
    // will reside within using a borderlayout.
    Container contentPane = frame.getContentPane();
    addComponentsToPane(contentPane, frame);

    frame.setSize(DIMENSION);
    frame.setMinimumSize(DIMENSION);
    // sets window to center of screen rather than default
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

  private static void handleClosing(JFrame frame) {

    int answer = showWarningMessage();

    switch (answer) {
      case JOptionPane.YES_OPTION:
        System.out.println("Quitting.");
        frame.dispose();
        break;
      case JOptionPane.NO_OPTION:
        System.out.println("Not Quitting.");
        break;
    }

  }

  private static int showWarningMessage() {
    String[] buttonLabels = new String[] {"Yes", "No"};
    String defaultOption = buttonLabels[0];
    Icon icon = null;
    int result = 1; // Defaults to no
    if (Events.isSERVERSTATUS()) {
      JOptionPane.showMessageDialog(null,
          "The server is still running.\n" + "Please stop server before quitting.");
    } else {
      result = JOptionPane.showOptionDialog(null, "Are you sure you want to quit?\n", "Warning",
          JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, icon, buttonLabels,
          defaultOption);
    }
    return result;
  }

  public static void main(String[] args) throws IOException {
    CrossPlatformTools.init();

    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGUI();
      }
    });
  }

}
