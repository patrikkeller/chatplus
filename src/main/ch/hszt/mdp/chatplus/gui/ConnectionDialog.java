package ch.hszt.mdp.chatplus.gui;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;

public class ConnectionDialog extends JDialog {

	private static final long serialVersionUID = -1598319247624850190L;
	private JButton connectButton;
	private JTextField serverIPField;
	private JLabel serverIPLabel;
	private JTextField serverPortField;
	private JLabel serverPortLabel;
	private JTextField usernameField;
	private JLabel usernameLabel;

	/** Creates new form LoginForm */
	public ConnectionDialog(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */

	private void initComponents() {

		serverIPLabel = new JLabel();
		serverIPField = new JTextField();
		serverPortLabel = new JLabel();
		serverPortField = new JTextField();
		usernameField = new JTextField();
		usernameLabel = new JLabel();
		connectButton = new JButton();

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Login details");
		setModal(true);
		setResizable(false);
		setSize(400, 182);
		setLocationRelativeTo(null);

		serverIPLabel.setText("Server IP:");
		serverPortLabel.setText("Server Port:");
		serverPortField.setText("9999");
		usernameLabel.setText("Username:");
		connectButton.setText("Connect");
		connectButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				connectButtonActionPerformed(evt);
			}
		});

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout
				.setHorizontalGroup(layout
						.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(
								layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												layout
														.createParallelGroup(
																GroupLayout.Alignment.LEADING)
														.addGroup(
																layout
																		.createSequentialGroup()
																		.addGroup(
																				layout
																						.createParallelGroup(
																								GroupLayout.Alignment.LEADING)
																						.addComponent(
																								serverPortLabel)
																						.addComponent(
																								serverIPLabel)
																						.addComponent(
																								usernameLabel))
																		.addPreferredGap(
																				LayoutStyle.ComponentPlacement.RELATED)
																		.addGroup(
																				layout
																						.createParallelGroup(
																								GroupLayout.Alignment.LEADING)
																						.addComponent(
																								serverIPField,
																								GroupLayout.DEFAULT_SIZE,
																								264,
																								Short.MAX_VALUE)
																						.addComponent(
																								serverPortField,
																								GroupLayout.DEFAULT_SIZE,
																								264,
																								Short.MAX_VALUE)
																						.addComponent(
																								usernameField,
																								GroupLayout.DEFAULT_SIZE,
																								264,
																								Short.MAX_VALUE)))
														.addComponent(
																connectButton,
																GroupLayout.Alignment.TRAILING))
										.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup().addContainerGap().addGroup(
						layout.createParallelGroup(
								GroupLayout.Alignment.BASELINE).addComponent(
								serverIPLabel).addComponent(serverIPField,
								GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)).addPreferredGap(
						LayoutStyle.ComponentPlacement.UNRELATED).addGroup(
						layout.createParallelGroup(
								GroupLayout.Alignment.BASELINE).addComponent(
								serverPortLabel).addComponent(serverPortField,
								GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)).addPreferredGap(
						LayoutStyle.ComponentPlacement.UNRELATED).addGroup(
						layout.createParallelGroup(
								GroupLayout.Alignment.BASELINE).addComponent(
								usernameField, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE).addComponent(
								usernameLabel)).addPreferredGap(
						LayoutStyle.ComponentPlacement.UNRELATED).addComponent(
						connectButton).addContainerGap(
						GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		pack();
	}

	private void connectButtonActionPerformed(java.awt.event.ActionEvent evt) {
		dispose();
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				ConnectionDialog dialog = new ConnectionDialog(new JFrame(),
						true);
				dialog.addWindowListener(new java.awt.event.WindowAdapter() {
					public void windowClosing(java.awt.event.WindowEvent e) {
						System.exit(0);
					}
				});
				dialog.setVisible(true);
			}
		});
	}

	public String getServerIP() {
		return serverIPField.getText();
	}

	public Integer getServerPort() {
		try {
			return Integer.parseInt(serverPortField.getText());
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public String getUsername() {
		return usernameField.getText();
	}

}