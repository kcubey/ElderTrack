package eldertrack.ui;

import java.awt.EventQueue;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import javax.swing.UIManager;
import java.awt.CardLayout;
import javax.swing.JComboBox;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EtchedBorder;

import eldertrack.diet.Elderly;
import eldertrack.diet.MenuItem;
import eldertrack.login.AccessLevel;
import eldertrack.login.StaffSession;
import eldertrack.weather.Weather;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;

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
    private DietSection DietSection;
    private MedPanel MedPanel;
    private ReportMainPanel ReportPanel;
    private MgmtSection MgmtSection;
    private MainMenuPanel MainMenu;
	private WeatherPanel weatherPanel;
	private AnnouncementPanel announcementPanel;
    static JPanel CardsPanel;
	JComboBox<String> comboBox;
	// Singleton Class Design
	private StaffSession session;
	private MarqueePanel marqueePanel;
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
					DecryptDBFrame dbauth = new DecryptDBFrame();
					dbauth.setVisible(true);
					frame = new MainFrame();
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
		this.setTitle("ElderTrack - Utilities For Nursing Homes (ITP192-03-Team 2)");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 810);
		setResizable(false);
		
		Toolkit kit = Toolkit.getDefaultToolkit();
		URL url = MainFrame.class.getResource("/eldertrack/resources/icon.png");
		Image img = kit.createImage(url);
		setIconImage(img);
		
		MasterPane = new JPanel();
		MasterPane.setBorder(null);
		MasterPane.setLayout(null);
		setContentPane(MasterPane);
		
		weatherPanel = new WeatherPanel();

		weatherPanel.setLocation(790, 671);
		MasterPane.add(weatherPanel);
		Thread thrd = new Thread(weatherTask);
		thrd.start();
		
		LoginPanel = new LoginPanel();
		LoginPanel.setBorder(lBorder);
		
		CardsPanel = new JPanel(new CardLayout());
		MasterPane.add(CardsPanel);
		CardsPanel.add(LoginPanel, LOGINPANEL);

		CardsPanel.setLocation(0, 0);
		CardsPanel.setSize(994, 671);
		((CardLayout)CardsPanel.getLayout()).show(CardsPanel, LOGINPANEL);
		
		String s = "Text from database not shown for security reasons, please login!";
		marqueePanel = new MarqueePanel(s, 160);
		marqueePanel.setBackground(new Color(0, 153, 255));
		marqueePanel.setBounds(0, 752, 790, 29);
		marqueePanel.start();
		MasterPane.add(marqueePanel);
		
		JLabel lblCurrentTime = new JLabel("Current Time Now");
		lblCurrentTime.setForeground(new Color(25, 25, 112));
		lblCurrentTime.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCurrentTime.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 21));
		lblCurrentTime.setBounds(496, 724, 293, 29);
		MasterPane.add(lblCurrentTime);
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy / HH:mm:ss");
		Timer SimpleTimer = new Timer(1000, new ActionListener(){
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        lblCurrentTime.setText(sdf.format(new Date()));
		    }

		});
		SimpleTimer.start();

	}
	
	void constructPanels() {
		JProgressBar jpbar = LoginPanel.progressBar;
		System.out.println("--------------------- PANELS ARE BEING CONSTRUCTED! ---------------------");
		jpbar.setValue(10);
				
		// Initialize Diet Panel
		jpbar.setString("Initializing Diet Management...");
		jpbar.update(jpbar.getGraphics());
		DietSection = new DietSection();
		CardsPanel.add(DietSection, DIETPANEL);
		jpbar.setValue(20);

		
		// Initialize Med Panel
		jpbar.setString("Initializing Medication...");
		jpbar.update(jpbar.getGraphics());
		MedPanel = new MedPanel();
		CardsPanel.add(MedPanel, MEDICATIONPANEL);
		jpbar.setValue(30);

		
		// Initialize Management Panel
		jpbar.setString("Initializing Management...");
		jpbar.update(jpbar.getGraphics());
		if(isManagementShown()) {
			MgmtSection = new MgmtSection();
			CardsPanel.add(MgmtSection, MGMTPANEL);
		}else{
			announcementPanel = new AnnouncementPanel();
		}
		jpbar.setValue(50);

		// Initialize Report Panel
		jpbar.setString("Initializing Report Generation...");
		jpbar.update(jpbar.getGraphics());
		ReportPanel = new ReportMainPanel();
		CardsPanel.add(ReportPanel, REPORTPANEL);
		jpbar.setValue(70);

		// Initialize Main Menu Panel
		jpbar.setString("Initializing Main Menu...");
		jpbar.update(jpbar.getGraphics());
		MainMenu = new MainMenuPanel();
		MainMenu.setBorder(lBorder);
		MainMenu.fillDetails();
		CardsPanel.add(MainMenu, MENUPANEL);
		jpbar.setValue(85);
		jpbar.update(jpbar.getGraphics());
		
		jpbar.setString("Initializing Scroll Text");
		setScrollText();
		jpbar.setValue(100);
		jpbar.update(jpbar.getGraphics());
		System.out.println("--------------------- DONE! PANELS ADDED TO CARDLAYOUT ---------------------");
	}
	
	void deconstructPanels() {
		System.out.println("--------------------- DECONSTRUCTING ALL PANELS NOW! ---------------------");
		CardsPanel.remove(DietSection);
		CardsPanel.remove(MedPanel);
		CardsPanel.remove(ReportPanel);
		if(isManagementShown())
			CardsPanel.remove(MgmtSection);
		CardsPanel.remove(MainMenu);
		DietSection = null;
		MedPanel = null;
		ReportPanel = null;
		MgmtSection = null;
		MainMenu = null;
		LoginPanel.progressBar.setValue(0);
		LoginPanel.progressBar.setString("Login to begin loading!");
		LoginPanel.progressBar.update(LoginPanel.progressBar.getGraphics());
		Elderly.nullMap();
		MenuItem.nullMap();
		StaffSession.nullMap();
		System.out.println("--------------------- DONE! PANELS REMOVED FROM CARDLAYOUT ---------------------");
	}
	
	
	Runnable weatherTask = () -> {
		System.out.println("Obtaining data from URL on " + Thread.currentThread().getName());
		Weather weatherinfo = Weather.getWeather();
		weatherPanel.showWeatherInfo(weatherinfo);
	};
	
	
	// Singleton Class Design
	public static MainFrame getInstance() {
		return frame;
	}
	
	// For getting logged in user information
	public StaffSession getSessionInstance() {
		return session;
	}
	
	StaffSession setSessionInstance(StaffSession session) {
		this.session = session;
		return getInstance().getSessionInstance();
	}
	
	// Triggers on logout
	boolean endCurrentSession() {
			CardLayout cards = (CardLayout) MainFrame.CardsPanel.getLayout();
			cards.show(MainFrame.CardsPanel, MainFrame.LOGINPANEL);
			deconstructPanels();
			this.session = null;
			System.out.println("Successfully logged out!");
			return true;
	}
	
	boolean isManagementShown() {
		AccessLevel al = MainFrame.getInstance().getSessionInstance().getAccessLevel();
		if (al == AccessLevel.MANAGER || al == AccessLevel.ADMIN || al == AccessLevel.SRSTAFF)
			return true;
		else
			return false;
	}
	
	MgmtPanel getManagementPanel() {
		return MgmtSection.getManagementPanel();
	}
	
	DietSection getDietPanel() {
		return this.DietSection;
	}
	
	MgmtSection getManagementSection() {
		return this.MgmtSection;
	}
	
	AnnouncementPanel getAnnouncementPanel() {
		return this.announcementPanel;
	}
	
	public void setScrollText() {
		AnnouncementPanel ap;
		if (MgmtSection != null) {
			ap = MainFrame.getInstance().getManagementSection().getAnnouncementPanel();
		} else {
			ap = getAnnouncementPanel();
		}
		MasterPane.remove(marqueePanel);
		this.repaint();
		marqueePanel = null;
		marqueePanel = new MarqueePanel(ap.getSex(), 160, ap.getFont());
		marqueePanel.setBackground(new Color(0, 153, 255));
		marqueePanel.setBounds(0, 752, 790, 29);
		marqueePanel.start();
		MasterPane.add(marqueePanel);

	}
}
