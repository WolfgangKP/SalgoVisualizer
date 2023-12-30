package salgo_visualizer;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * This class launches Salgo Visualizer.
 * 
 * @author Wolfgang Koch-Paiz
 */
public class Launcher {

	public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> { // Ensures proper thread usage

			try {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); // Sets look and feel
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e) {
				e.printStackTrace();
			}

			GUI.show();
		});
	}
}
