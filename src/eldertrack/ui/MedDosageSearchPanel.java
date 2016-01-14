package eldertrack.ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JComboBox;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.JTextPane;




public class MedDosageSearchPanel extends JPanel {

	private static final long serialVersionUID = -2593071831861718177L;
	private JComboBox<String> combobox;
	
	public static String selected;
	
	public MedDosageSearchPanel() {
		
		setBounds(5, 5, 975, 510);
		setLayout(null);
		
		
		JLabel lblDosageTrackingSystem = new JLabel("Dosage Tracking System");
		lblDosageTrackingSystem.setForeground(UIManager.getColor("EditorPane.selectionBackground"));
		lblDosageTrackingSystem.setFont(new Font("Segoe UI", Font.PLAIN, 30));
		lblDosageTrackingSystem.setBounds(25, 25, 349, 41);
		add(lblDosageTrackingSystem);
		
		
		JLabel lblRoomNumber = new JLabel("Room Number: ");
		lblRoomNumber.setBounds(90, 117, 151, 41);
		lblRoomNumber.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		lblRoomNumber.setForeground(new Color(0, 128, 128));
		add(lblRoomNumber);
		DateFormat dateFormat = new SimpleDateFormat("HH:mm");
		Calendar cal = Calendar.getInstance();
		combobox = new JComboBox<String>();
		combobox.setBackground(UIManager.getColor("TextField.highlight"));
		combobox.setFont( new Font( "Segoe UI", Font.BOLD, 18 ) );
		combobox.setBounds(277, 124, 125, 31);
		combobox.setModel(new javax.swing.DefaultComboBoxModel<String>(new String[] {"101 ","102", "103" }));
		add(combobox);
		
		
		JLabel lblTime = new JLabel("Time: ");
		lblTime.setBounds(90, 177, 80, 41);
		lblTime.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		lblTime.setForeground(new Color(0, 128, 128));
		add(lblTime);
		JTextField textField=new JTextField();
		textField = new JTextField();
		textField.setBounds(277, 184, 125, 31);
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setFont( new Font( "Segoe UI", Font.BOLD, 18 ) );
		textField.setText(dateFormat.format(cal.getTime()));
		textField.setColumns(10);
		add(textField);
		
		
		
		JLabel lblOverview = new JLabel("Overview:");
		lblOverview.setForeground(new Color(0, 128, 128));
		lblOverview.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		lblOverview.setBounds(551, 117, 151, 41);
		add(lblOverview);
		
		JTextPane txtpnRoomNumber = new JTextPane();
		txtpnRoomNumber.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		txtpnRoomNumber.setText("Room Number: ~\r\nTotal Number of Elderly: ~\r\nTotal Number of Male: ~\r\nTotal Number of Female: ~\r\nRecommandard Helper: ~\r\n");
		txtpnRoomNumber.setBounds(551, 155, 339, 226);
		add(txtpnRoomNumber);
		
		JButton btnGetDosage = new JButton("Get Dosage");
		btnGetDosage.setBounds(90, 256, 312, 31);
		btnGetDosage.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		add(btnGetDosage);

		// testing for choosing room options
		
		
		btnGetDosage.addActionListener(new ActionListener() {
			

			public void actionPerformed(ActionEvent e) {
				
				if(combobox.getSelectedItem().toString().equals(" ")){
					
					JOptionPane.showMessageDialog(null, "Please check if you have filled in the required fields");
					
					
					
				}
				else{
					setDosageSelect(combobox.getSelectedItem().toString());
					JPanel gottenDosage=new MedDosagePanel();
					MedPanel.MedCardPanel.add(gottenDosage,MedPanel.MDOSPANEL);
					
					CardLayout mainCards = (CardLayout) MedPanel.MedCardPanel.getLayout();
				    mainCards.show(MedPanel.MedCardPanel, MedPanel.MDOSPANEL);
				}
				
			}
		});
	}
	public void setDosageSelect(String select){
		this.selected=select;
	}
	public static  String getDosageSelect(){
		return selected;
	}
}
