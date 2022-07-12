package net.java.dev.colorchooser;

import java.awt.Color;

import javax.swing.JLabel;

public class TextColor extends JLabel {

	public static JLabel color;

	public TextColor() {

		super();

		setBackground(Color.WHITE);

		this.setBackground(Color.WHITE);

		TextColor.color = this;

		TextColor.color.setBackground(Color.WHITE);

		TextColor.color.setText("#000000");

		TextColor.color.setBorder(null);

	}

	public JLabel getColor() {

		return color;

	}

}
