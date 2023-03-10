package net.java.dev.colorchooser.demo;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Timer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

import com.bric.awt.ColorPicker;

import net.java.dev.colorchooser.ColorChooser;

@SuppressWarnings("all")

public class CopyColor extends javax.swing.JPanel {

	public boolean transparency;

	Color colour;

	ColorChooser lblNewLabel;

	public Timer t;

	private static Clipboard getSystemClipboard() {

		Toolkit defaultToolkit = Toolkit.getDefaultToolkit();

		Clipboard systemClipboard = defaultToolkit.getSystemClipboard();

		return systemClipboard;

	}

	public void setLineBorderColor(Color color) {

		setBorder(new LineBorder(color));

	}

	public void setColor(Color color) {

		this.colour = color;

		lblNewLabel.setColor(color);

	}

	public CopyColor(Color color, boolean transparency) throws IOException {

		setBorder(new LineBorder(new Color(0, 0, 0)));

		this.colour = color;

		this.transparency = transparency;

		setBackground(Color.WHITE);

		this.setBackground(Color.WHITE);

		lblNewLabel = new ColorChooser();

		lblNewLabel.setColor(color);

		lblNewLabel.transparency = transparency;

		lblNewLabel.setBorder(
				new BevelBorder(BevelBorder.RAISED, Color.GRAY, Color.LIGHT_GRAY, Color.GRAY, Color.LIGHT_GRAY));

		add(lblNewLabel);

		JButton btnNewButton_2 = new JButton("Copy");

		btnNewButton_2.addActionListener(new ActionListener() {

			@Override
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

		setSize(163, 36);

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
