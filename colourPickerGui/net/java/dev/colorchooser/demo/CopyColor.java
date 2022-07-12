package net.java.dev.colorchooser.demo;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import net.java.dev.colorchooser.ColorChooser;
import net.java.dev.colorchooser.TextColor;

@SuppressWarnings("all")

public class CopyColor extends javax.swing.JPanel {

	public static boolean transparency;

	TextColor color;

	private static Clipboard getSystemClipboard() {

		Toolkit defaultToolkit = Toolkit.getDefaultToolkit();

		Clipboard systemClipboard = defaultToolkit.getSystemClipboard();

		return systemClipboard;

	}

	public CopyColor(boolean transparency) throws IOException {

		this.transparency = transparency;

		setBackground(Color.WHITE);

		this.setBackground(Color.WHITE);

		ColorChooser lblNewLabel = new ColorChooser();

		lblNewLabel.setColor(Color.BLACK);

		lblNewLabel.transparency = transparency;

		add(lblNewLabel);

		color = new TextColor();

		color.setFont(new Font("Tahoma", Font.PLAIN, 14));

		if (transparency) {

			color.setText(color.getText() + "ff");

		}

		add(color);

		JButton btnNewButton_2 = new JButton("Copy");

		btnNewButton_2.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				Clipboard clipboard = getSystemClipboard();

				clipboard.setContents(new StringSelection(color.getText()), null);
			}

		});

		btnNewButton_2.setBorder(null);

		btnNewButton_2.setContentAreaFilled(false);

		btnNewButton_2.setFont(new Font("Tahoma", Font.PLAIN, 14));

		btnNewButton_2.setIcon(new ImageIcon(CopyColor.class.getResource("/images/copy.png")));

		add(btnNewButton_2);

		setSize(216, 36);

	}

	public String getHtmlColor() {

		return String.format("#%02x%02x%02x", ColorChooser.color.getRed(), ColorChooser.color.getGreen(),
				ColorChooser.color.getBlue());

	}

}
