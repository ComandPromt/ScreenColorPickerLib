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

import com.bric.swing.ColorPicker;

import net.java.dev.colorchooser.ColorChooser;

@SuppressWarnings("all")

public class CopyColor extends javax.swing.JPanel {

	public static boolean transparency;

	Color colour;

	ColorChooser lblNewLabel;

	private static Clipboard getSystemClipboard() {

		Toolkit defaultToolkit = Toolkit.getDefaultToolkit();

		Clipboard systemClipboard = defaultToolkit.getSystemClipboard();

		return systemClipboard;

	}

	public CopyColor(Color color, boolean transparency) throws IOException {

		this.colour = color;

		this.transparency = transparency;

		setBackground(Color.WHITE);

		this.setBackground(Color.WHITE);

		lblNewLabel = new ColorChooser();

		lblNewLabel.setColor(color);

		lblNewLabel.transparency = transparency;

		add(lblNewLabel);

		JButton btnNewButton_2 = new JButton("Copy");

		btnNewButton_2.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				Clipboard clipboard = getSystemClipboard();

				clipboard.setContents(new StringSelection(getHtmlColor()), null);
			}

		});

		btnNewButton_2.setBorder(null);

		btnNewButton_2.setContentAreaFilled(false);

		btnNewButton_2.setFont(new Font("Tahoma", Font.PLAIN, 14));

		btnNewButton_2.setIcon(new ImageIcon(CopyColor.class.getResource("/images/copy.png")));

		add(btnNewButton_2);

		setSize(95, 36);

	}

	public Color getColor() {

		return lblNewLabel.getColor();

	}

	public String getHtmlColor() {

		Color color;

		if (this.lblNewLabel.getColor() == null) {

			color = colour;

		}

		else {

			color = this.lblNewLabel.getColor();

		}

		String transparencia = "";

		if (this.transparency) {

			transparencia = Integer.toHexString(ColorPicker.opacitySlider.getValue());

		}

		return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue()) + transparencia;

	}

}
