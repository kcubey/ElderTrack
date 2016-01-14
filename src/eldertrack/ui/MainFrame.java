package eldertrack.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import java.awt.CardLayout;
import javax.swing.JComboBox;

import javax.swing.DefaultComboBoxModel;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.border.EtchedBorder;

import eldertrack.login.AccessLevel;
import eldertrack.login.StaffSession;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
    private static final EtchedBorder lBorder = new EtchedBorder(EtchedBorder.LOWERED, null, null);
	final static String LOGINPANEL = "Login Panel";
    final static String MEDICATIONPANEL = "Medication Panel";
    final static String DIETPANEL = "Diet Panel";
    final static String MGMTPANEL = "Management Panel";
    final static String REPORTPANEL = "Report Panel";
    final static String MENUPANEL = "Main Menu Panel";
    private JPanel MasterPane;
    private LoginPanel LoginPanel;
    private DietPanel DietPanel;
    private MedPanel MedPanel;
    private ReportMainPanel ReportPanel;
    private MgmtPanel MgmtPanel;
    private MainMenuPanel MainMenu;
    static JPanel CardsPanel;
	JComboBox<String> comboBox;
	// Singleton Class Design
	private static StaffSession session;
	private static MainFrame frame;
	// JFrame (MainFrame) > Normal JPanel (MasterPane) > CardLayout JPanel (MainPanel) > Feature Panels (LoginPanel)
	
	/**
	 * Launch the application.
	 */
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); // Change look to native Windows / OS X / Linux
					frame = new MainFrame();
					frame.setVisible(true); // Set the main frame as visible
					frame.showWeatherPanel();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame!
	 */
	
	private MainFrame() {
		this.setTitle("ElderTrack Toolkit - ITP192-03 Team 2");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1000, 810);
		setResizable(false);
		
		MasterPane = new JPanel();
		MasterPane.setBorder(null);
		MasterPane.setLayout(null);
		setContentPane(MasterPane);
		
		LoginPanel = new LoginPanel();
		LoginPanel.setBorder(lBorder);
		
		CardsPanel = new JPanel(new CardLayout());
		MasterPane.add(CardsPanel);
		CardsPanel.add(LoginPanel, LOGINPANEL);

		CardsPanel.setLocation(0, 0);
		CardsPanel.setSize(994, 671);
		((CardLayout)CardsPanel.getLayout()).show(CardsPanel, LOGINPANEL);
	}
	
	void constructPanels() {
		constructTask.run();
	}
	
	void deconstructPanels() {
		deconstructTask.run();
	}
	
	private void showWeatherPanel() {
		JPanel WeatherPanel = new WeatherPanel();
		WeatherPanel.setLocation(790, 671);
		getInstance().MasterPane.add(WeatherPanel);
	}
	
	// Singleton Class Design
	public static MainFrame getInstance() {
		return frame;
	}
	
	// For getting logged in user information
	public StaffSession getSessionInstance() {
		return MainFrame.session;
	}
	
	StaffSession setSessionInstance(StaffSession session) {
		MainFrame.session = session;
		return MainFrame.session;
	}
	
	// Triggers on logout
	boolean endCurrentSession() {
			CardLayout cards = (CardLayout) MainFrame.CardsPanel.getLayout();
			cards.show(MainFrame.CardsPanel, MainFrame.LOGINPANEL);
			deconstructPanels();
			MainFrame.session = null;
			System.out.println("Successfully logged out!");
			return true;
	}
	
	boolean isManagementShown() {
		AccessLevel al = MainFrame.getInstance().getSessionInstance().getAccessLevel();
		if (al == AccessLevel.MANAGER || al == AccessLevel.ADMIN)
			return true;
		else
			return false;
	}
	
	private Runnable deconstructTask = () -> {
		System.out.println("--------------------- DECONSTRUCTING ALL PANELS NOW! ---------------------");
		comboBox.setVisible(false);
		CardsPanel.remove(DietPanel);
		CardsPanel.remove(MedPanel);
		CardsPanel.remove(ReportPanel);
		if(isManagementShown())
			CardsPanel.remove(MgmtPanel);
		CardsPanel.remove(MainMenu);
		MasterPane.remove(comboBox);
		comboBox = null;
		DietPanel = null;
		MedPanel = null;
		ReportPanel = null;
		MgmtPanel = null;
		MainMenu = null;
		LoginPanel.progressBar.setValue(0);
		LoginPanel.progressBar.setString("Login to begin loading!");
		LoginPanel.progressBar.update(LoginPanel.progressBar.getGraphics());
		System.out.println("Panels deconstructed!");
	};
	
	private Runnable constructTask = () -> {
		JProgressBar jpbar = LoginPanel.progressBar;
		System.out.println("--------------------- CONSTRUCTING ALL PANELS NOW! ---------------------");
		jpbar.setValue(25);

		// Initialize Diet Panel
		jpbar.setString("Initializing Diet Management...");
		jpbar.update(jpbar.getGraphics());
		DietPanel = new DietPanel();
		DietPanel.setBorder(lBorder);
		CardsPanel.add(DietPanel, DIETPANEL);
		jpbar.setValue(50);

		
		// Initialize Med Panel
		jpbar.setString("Initializing Medication...");
		jpbar.update(jpbar.getGraphics());
		MedPanel = new MedPanel();
		MedPanel.setBorder(lBorder);
		CardsPanel.add(MedPanel, MEDICATIONPANEL);
		jpbar.setValue(65);

		
		// Initialize Management Panel
		jpbar.setString("Initializing Management...");
		jpbar.update(jpbar.getGraphics());
		if(session.getAccessLevel() == AccessLevel.MANAGER || session.getAccessLevel() == AccessLevel.SRSTAFF) {
			MgmtPanel = new MgmtPanel();
			MgmtPanel.setBorder(lBorder);
			CardsPanel.add(MgmtPanel, MGMTPANEL);
	
		}
		jpbar.setValue(85);

		// Initialize Report Panel
		jpbar.setString("Initializing Report Generation...");
		jpbar.update(jpbar.getGraphics());
		ReportPanel = new ReportMainPanel();
		ReportPanel.setBorder(lBorder);
		CardsPanel.add(ReportPanel, REPORTPANEL);

		// Initialize Main Menu Panel
		jpbar.setString("Initializing Main Menu...");
		LoginPanel.progressBar.setValue(90);
		jpbar.update(jpbar.getGraphics());
		MainMenu = new MainMenuPanel();
		MainMenu.setBorder(lBorder);
		CardsPanel.add(MainMenu, MENUPANEL);
		jpbar.setValue(95);
		jpbar.update(jpbar.getGraphics());
		
		// Add menus to combo box
		comboBox = new JComboBox<>();
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
		        JComboBox<?> jcb = (JComboBox<?>) evt.getSource();
		        CardLayout cl = (CardLayout) CardsPanel.getLayout();
		        cl.show(CardsPanel, jcb.getSelectedItem().toString());
			}
		});
		comboBox.setSize(174, 26);
		comboBox.setLocation(10, 682);
		comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {MENUPANEL, MGMTPANEL, MEDICATIONPANEL, DIETPANEL, REPORTPANEL}));
		comboBox.setSelectedIndex(0);
		jpbar.setValue(98);
		jpbar.update(jpbar.getGraphics());
	
		MasterPane.add(comboBox);
		MainMenu.fillDetails();
		jpbar.setValue(100);
		jpbar.update(jpbar.getGraphics());
	};
}
