package com.franciscogarrido.popupnotifications;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.franciscogarrido.popupnotifications.finances.abengoa.AbengoaNotificationScrapper;
import com.franciscogarrido.popupnotifications.sport.as.ASNotificationScrapper;

@SpringBootApplication
public class DesktopPopupNotificationsApplication implements Observer{

	public static void main(String[] args) {
		final DesktopPopupNotificationsApplication desktopPopupNotificationsApplication = new DesktopPopupNotificationsApplication();
		final ASNotificationScrapper asNotificationScrapper = new ASNotificationScrapper();
		final AbengoaNotificationScrapper abengoaNotificationScrapper = new AbengoaNotificationScrapper();

		asNotificationScrapper.addObserver(desktopPopupNotificationsApplication);
		abengoaNotificationScrapper.addObserver(desktopPopupNotificationsApplication);
		
		new Thread(asNotificationScrapper, "AS Thread").start();
		new Thread(abengoaNotificationScrapper, "Abengoa Thread").start();
		
//		SpringApplication.run(DesktopPopupNotificationsApplication.class, args);
	}

	@Override
	public void update(Observable o, Object arg) {
		/* Use an appropriate Look and Feel */
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		UIManager.put("swing.boldMetal", Boolean.FALSE);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI(arg);
			}
		});
	}

	private static void createAndShowGUI(Object arg) {
		if (!SystemTray.isSupported()) {
			System.out.println("SystemTray is not supported");
			return;
		}
		final PopupMenu popup = new PopupMenu();
		final TrayIcon trayIcon = new TrayIcon(createImage("/images/bulb.gif", "tray icon"));
		
		trayIcon.setToolTip((String) arg);
		
		final SystemTray tray = SystemTray.getSystemTray();

		// Create a popup menu components
		MenuItem exitAll = new MenuItem("Exit All");
		MenuItem exitItem = new MenuItem("Exit");

		// Add components to popup menu
		popup.add(exitAll);
		popup.add(exitItem);

		trayIcon.setPopupMenu(popup);

		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			System.out.println("TrayIcon could not be added.");
			return;
		}

		trayIcon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "This dialog box is run from System Tray");
			}
		});

		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tray.remove(trayIcon);
//				System.exit(0);
			}
		});
		
		exitAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(TrayIcon t : tray.getTrayIcons()) {
					tray.remove(t);
				}
//				System.exit(0);
			}
		});
		
		final String message = (String) arg;
		trayIcon.setActionCommand(message.split(":")[0]+":"+message.split(":")[1]);
		
		trayIcon.displayMessage(message.split(":")[0]+": "+message.split(":")[1], message.split(":")[2], TrayIcon.MessageType.INFO);
	}

	// Obtain the image URL
	protected static Image createImage(String path, String description) {
		URL imageURL = null;
		imageURL = DesktopPopupNotificationsApplication.class.getResource(path);

		if (imageURL == null) {
			System.err.println("Resource not found: " + path);
			return null;
		} else {
			return (new ImageIcon(imageURL, description)).getImage();
		}
	}
}
