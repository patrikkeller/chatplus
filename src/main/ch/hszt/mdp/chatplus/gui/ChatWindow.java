package ch.hszt.mdp.chatplus.gui;

import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

import ch.hszt.mdp.chatplus.logic.concrete.message.LoginMessage;
import ch.hszt.mdp.chatplus.logic.concrete.message.ManageBoardMessage;
import ch.hszt.mdp.chatplus.logic.concrete.message.SimpleMessage;
import ch.hszt.mdp.chatplus.logic.concrete.TcpServerPeer;
import ch.hszt.mdp.chatplus.logic.contract.context.IClientContext;

public class ChatWindow extends JFrame implements IClientContext {

	private static final long serialVersionUID = -1671373001474835583L;
	private JTabbedPane chatTabPane;
	private JMenuItem connectItem;
	private JMenuItem disconnectItem;
	private JMenuItem exitItem;
	private JMenu fileMenu;
	private JPopupMenu jPopupMenu1;
	private JMenuBar menubar;
	private TcpServerPeer serverPeer;
	private String serverIP;
	private Integer serverPort;
	private String username = null;
	private ConcurrentHashMap<String, ChatTab> chatTabs = new ConcurrentHashMap<String, ChatTab>();
	private JMenuItem enterBoardItem;
	private final String windowTitle = "ChatPlus";

	/** Creates new form ChatWindow */
	public ChatWindow() {

		initComponents();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */

	private void initComponents() {

		jPopupMenu1 = new JPopupMenu();
		chatTabPane = new JTabbedPane();
		menubar = new JMenuBar();
		fileMenu = new JMenu();
		connectItem = new JMenuItem();
		disconnectItem = new JMenuItem();
		enterBoardItem = new JMenuItem();
		exitItem = new JMenuItem();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle(windowTitle);
		setSize(800, 600);
		setLocationRelativeTo(null);

		fileMenu.setText("File");

		connectItem.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_C,
				java.awt.event.InputEvent.SHIFT_MASK
						| java.awt.event.InputEvent.CTRL_MASK));
		connectItem.setText("Connect to server");
		connectItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				connectItemActionPerformed(evt);
			}
		});
		fileMenu.add(connectItem);

		disconnectItem.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_D,
				java.awt.event.InputEvent.SHIFT_MASK
						| java.awt.event.InputEvent.CTRL_MASK));
		disconnectItem.setText("Disconnect");
		disconnectItem.setEnabled(false);
		disconnectItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				disconnectItemActionPerformed(evt);
			}
		});
		fileMenu.add(disconnectItem);

		enterBoardItem.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_E,
				java.awt.event.InputEvent.SHIFT_MASK
						| java.awt.event.InputEvent.CTRL_MASK));
		enterBoardItem.setText("Enter board");
		enterBoardItem.setEnabled(false);
		enterBoardItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				enterBoardItemActionPerformed(evt);
			}
		});
		fileMenu.add(enterBoardItem);

		exitItem.setText("Exit");
		exitItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				exitItemActionPerformed(evt);
			}
		});
		fileMenu.add(exitItem);

		menubar.add(fileMenu);

		setJMenuBar(menubar);

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addComponent(chatTabPane,
				GroupLayout.DEFAULT_SIZE, 766, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addComponent(chatTabPane,
				GroupLayout.DEFAULT_SIZE, 581, Short.MAX_VALUE));

		pack();

	}

	private void connectItemActionPerformed(java.awt.event.ActionEvent evt) {
		displayConnectionDialog();
	}

	private void disconnectItemActionPerformed(java.awt.event.ActionEvent evt) {
		disconnectFromServer();
	}

	private void exitItemActionPerformed(java.awt.event.ActionEvent evt) {
		disconnectFromServer();
		System.exit(0);
	}

	private void enterBoardItemActionPerformed(java.awt.event.ActionEvent evt) {
		displayEnterBoardDialog();
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				ChatWindow mainWindow = new ChatWindow();
				mainWindow.setVisible(true);
				mainWindow.displayConnectionDialog();
			}
		});
	}

	/**
	 * Create Chat Tab
	 * 
	 * Add a new chat tab panel
	 * 
	 * @param tabName
	 */

	private void createChatTab(String tabName) {

		// add the new tab to the GUI
		ChatTab chatTabPanel = new ChatTab(this, tabName);
		chatTabPane.addTab(tabName, chatTabPanel);
		chatTabPane.setSelectedComponent(chatTabPanel);
		chatTabs.put(tabName, chatTabPanel);

		// notify the server that a new board has been created
		ManageBoardMessage manageMsg = new ManageBoardMessage();
		manageMsg.setBoardName(tabName);
		manageMsg.setUsername(username);
		manageMsg.setJoin(true);
		serverPeer.send(manageMsg);

	}

	/**
	 * Remove Chat Tab
	 * 
	 * remove a chat tab panel
	 * 
	 * @param tabName
	 */

	private void removeChatTab(String tabName) {

		// remove the tab
		chatTabPane.remove(chatTabs.get(tabName));
		chatTabs.remove(tabName);

		// notify the server that a user has left the board
		ManageBoardMessage manageMsg = new ManageBoardMessage();
		manageMsg.setBoardName(tabName);
		manageMsg.setUsername(username);
		manageMsg.setJoin(false);
		serverPeer.send(manageMsg);

	}

	/**
	 * Leave Board
	 * 
	 * Leave a board
	 * 
	 * @param boardName
	 */

	public void leaveBoard(String boardName) {
		removeChatTab(boardName);
	}

	/**
	 * Disable elements
	 * 
	 * Disables all interface elements
	 */

	private final void disableElements() {
		connectItem.setEnabled(true);
		disconnectItem.setEnabled(false);
		enterBoardItem.setEnabled(false);
	}

	/**
	 * Enable elements
	 * 
	 * Enables all interface elements
	 */

	private final void enableElements() {
		connectItem.setEnabled(false);
		disconnectItem.setEnabled(true);
		enterBoardItem.setEnabled(true);
	}

	/**
	 * Display connection dialog
	 * 
	 * Open the connection dialog
	 */

	public void displayConnectionDialog() {

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				final ConnectionDialog dialog = new ConnectionDialog(
						new JFrame(), true);

				dialog.addWindowListener(new java.awt.event.WindowAdapter() {
					public void windowClosed(WindowEvent e) {

						// retreive all the user input from the connection
						// dialog
						username = dialog.getUsername();
						serverIP = dialog.getServerIP();
						serverPort = dialog.getServerPort();

						// connect to the server
						if (connectToServer(serverIP, serverPort)) {
							LoginMessage msg = new LoginMessage();
							msg.setUsername(username);
							serverPeer.send(msg);

							createChatTab("Public");
						} else {
							System.out.println("not connected");
						}

					}

				});
				dialog.setVisible(true);
			}
		});

	}

	/**
	 * Display Enter Board Dialog
	 * 
	 * Show the dialog to enter a board name
	 */

	public void displayEnterBoardDialog() {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				final EnterBoardDialog dialog = new EnterBoardDialog(
						new JFrame(), true);
				dialog.addWindowListener(new java.awt.event.WindowAdapter() {
					public void windowClosed(WindowEvent e) {

						// retreive all the user input from the connection
						// dialog
						String boardName = dialog.getBoardName();

						// create a new tab
						createChatTab(boardName);

					}

				});
				dialog.setVisible(true);
			}
		});
	}

	/**
	 * Connect to the server
	 * 
	 * Establish the connection between the client and the server
	 * 
	 * @param ip
	 * @param port
	 */

	private boolean connectToServer(String ip, Integer port) {

		// init the server peer
		serverPeer = new TcpServerPeer();
		serverPeer.setServerIP(ip);
		serverPeer.setServerPort(port);
		serverPeer.setContext(this);

		try {
			// init the server peer. will throw various exceptions if the
			// connection failed
			serverPeer.Init();
			serverPeer.Start();

			// store the server ip and port for later usage
			serverIP = ip;
			serverPort = port;

			return true;
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(this, "Unknown host");
			return false;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this,
					"Could not connect to the server");
			return false;
		}

	}

	/**
	 * Disconnect from the server
	 * 
	 * Close the connection to the server
	 */

	private void disconnectFromServer() {
		serverPeer.Stop();
		disableElements();

		// close all tabs
		for (String chatTab : chatTabs.keySet()) {
			removeChatTab(chatTab);
		}

		// reset frame title
		setTitle(windowTitle);
	}

	/**
	 * Send message
	 * 
	 * Send a message to the server
	 * 
	 * @param message
	 * @param board
	 */

	public void sendMessage(String message, String board) {
		// add the message to the queue
		SimpleMessage msg = new SimpleMessage();
		msg.setMessage(message);
		msg.setSender(username);
		if (!board.equals("Public")) {
			msg.setBoard(board);
		}
		serverPeer.send(msg);
	}

	/**
	 * Display chat message
	 * 
	 * Display a message which was sent to the public board by the server
	 * 
	 * @param sender
	 * @param message
	 */

	@Override
	public void displayChatMessage(String sender, String message) {
		displayChatMessage(sender, message, "Public");
	}

	/**
	 * Display chat message
	 * 
	 * Display a message which was sent to a custom board by the server
	 * 
	 * @param sender
	 * @param message
	 * @param boardName
	 */

	@Override
	public void displayChatMessage(String sender, String message,
			String boardName) {

		ChatTab chatTab = chatTabs.get(boardName);
		if (chatTab != null) {
			chatTab.displayChatMessage(sender, message);
		}
	}

	/**
	 * Notify User Status Change
	 * 
	 * User state updates for public board
	 * 
	 * @param username
	 * @param isOnline
	 */

	@Override
	public void notifyUserStatusChange(String username, boolean isOnline) {

		for (String chatTab : chatTabs.keySet()) {
			notifyUserStatusChange(chatTab, username, isOnline);
		}

	}

	/**
	 * Notify User Status Change
	 * 
	 * User state updates for custom boards
	 * 
	 * @param username
	 * @param isOnline
	 */

	@Override
	public void notifyUserStatusChange(String boardName, String username,
			boolean isOnline) {

		if (username.equals(this.username) || this.username == null) {
			return;
		}

		ChatTab chatTab = chatTabs.get(boardName);
		if (chatTab != null) {
			chatTab.notifyUserStatusChange(username, isOnline);
		}

	}

	/**
	 * Process Login Response
	 * 
	 * Actions after the user has logged in
	 * 
	 * @param username
	 * @param isAuthorised
	 */

	@Override
	public void processLoginResponse(String username, boolean isAuthorised) {
		if (isAuthorised) {
			this.username = username;
			setTitle(windowTitle + " (" + username + " connected to " + serverIP + ":" + serverPort +")");
			enableElements();
		}
	}

}
