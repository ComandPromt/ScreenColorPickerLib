package com.colourpicker;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;

@SuppressWarnings("all")

class ColourPicker extends JFrame implements ActionListener, ChangeListener {

	public static Color colour;

	public static JFrame frame;

	Robot robot;

	public static Color getColour() {

		return colour;

	}

	public static void main(String[] args) throws AWTException {

		new ColourPicker();

	}

	public static void cerrar() {

		try {

			GlobalScreen.unregisterNativeHook();

			frame.dispose();

		}

		catch (Exception e) {

			e.printStackTrace();

		}

	}

	public ColourPicker() throws AWTException {

		getContentPane().setBackground(Color.WHITE);

		setTitle("ColourPicker");

		initComponents();

		robot = new Robot();

		try {

			GlobalScreen.registerNativeHook();

			GlobalScreen.addNativeMouseListener((NativeMouseListener) new MouseLogger());

			GlobalScreen.addNativeKeyListener((NativeKeyListener) new KeyLogger(robot));

		}

		catch (Exception e) {

			e.printStackTrace();

		}

		JPanel panel = new JPanel();

		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

		panel.setLayout(new BorderLayout());

		frame = new JFrame();

		frame.setUndecorated(true);

		frame.setShape(new RoundRectangle2D.Double(0, 0, 200, 200, 100, 100));

		frame.setResizable(false);

		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		frame.setType(Type.UTILITY);

		frame.getContentPane().setLayout(new BorderLayout());

		frame.setPreferredSize(new Dimension(128, 128));

		frame.setAlwaysOnTop(true);

		frame.getContentPane().add(panel, BorderLayout.CENTER);

		JPanel panel_1 = new JPanel();

		frame.getContentPane().add(panel_1, BorderLayout.NORTH);

		JLabel lblNewLabel = new JLabel("\u00A0");

		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));

		panel_1.add(lblNewLabel);

		frame.setVisible(true);

		frame.pack();

		try {

			while (true) {

				colour = robot.getPixelColor(MouseInfo.getPointerInfo().getLocation().x,
						MouseInfo.getPointerInfo().getLocation().y);

				if (ScreenColor.picar) {

					ScreenColor.color.setBackground(colour);

				}

				frame.setLocation(MouseInfo.getPointerInfo().getLocation().x + 10,
						MouseInfo.getPointerInfo().getLocation().y - 10);

				BufferedImage image = robot.createScreenCapture(
						new Rectangle(MouseInfo.getPointerInfo().getLocation(), new Dimension(10, 10)));

				panel.removeAll();

				panel.add(new JLabel(new ImageIcon(
						image.getScaledInstance(panel.getWidth(), panel.getHeight(), BufferedImage.SCALE_FAST))),
						BorderLayout.CENTER);

				panel_1.setBackground(colour);

				panel.validate();

				Thread.sleep(400);

			}

		}

		catch (Exception ex) {

			ex.printStackTrace();

		}

		this.setVisible(true);

	}

	private void initComponents() {

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

		setResizable(false);

		Date myDate = new Date();

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());

		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.TRAILING).addGap(0, 847, Short.MAX_VALUE));

		layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGap(0, 600, Short.MAX_VALUE));

		getContentPane().setLayout(layout);

		setSize(new Dimension(104, 102));

		setLocationRelativeTo(null);

	}

	@Override
	public void stateChanged(ChangeEvent e) {

	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}

}
