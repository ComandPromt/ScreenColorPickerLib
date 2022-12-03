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
import javax.swing.border.LineBorder;

import com.bric.awt.ColorPicker;
import com.bric.awt.MyTask;

import net.java.dev.colorchooser.ColorChooser;

@SuppressWarnings("all")

public class CopyColor extends javax.swing.JPanel {

	public static boolean transparency;

	Color colour;

	Color colourLine;

	public static ColorChooser colorSelect;

	public static Timer t;

	private static Clipboard getSystemClipboard() {

		Toolkit defaultToolkit = Toolkit.getDefaultToolkit();

		Clipboard systemClipboard = defaultToolkit.getSystemClipboard();

		return systemClipboard;

	}

	public void setColor(Color color) {

		this.colour = color;

		colorSelect.setColor(color);

	}

	public void setLineBorderColor(Color color) {

		this.colourLine = color;

		setBorder(new LineBorder(color));

	}

	public void setThicknessLine(int thickness) {

		setBorder(new LineBorder(this.colourLine, thickness));

	}

	public CopyColor(Color color, boolean transparency) throws IOException {

		this.colourLine = Color.BLACK;

		this.colour = color;

		this.transparency = transparency;

		this.setBackground(Color.WHITE);

		setBorder(new LineBorder(this.colourLine));

		colorSelect = new ColorChooser();

		colorSelect.setColor(color);

		colorSelect.transparency = transparency;

		add(colorSelect);

		JButton copy = new JButton("Copy");

		copy.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				Clipboard clipboard = getSystemClipboard();

				clipboard.setContents(new StringSelection(getHtmlColor()), null);
			}

		});

		JButton colorPicker = new JButton("");

		colorPicker.setBorder(null);

		colorPicker.setContentAreaFilled(false);

		colorPicker.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				try {

					t = new Timer();

					MyTask mTask = new MyTask();

					t.scheduleAtFixedRate(mTask, 0, 999999999);

				}

				catch (Exception e1) {

				}

			}

		});

		colorPicker.setIcon(new ImageIcon(CopyColor.class.getResource("/images/color_picker.png")));

		add(colorPicker);

		copy.setBorder(null);

		copy.setContentAreaFilled(false);

		copy.setFont(new Font("Tahoma", Font.PLAIN, 14));

		copy.setIcon(new ImageIcon(CopyColor.class.getResource("/images/copy.png")));

		add(copy);

		setSize(215, 36);

	}

	public Color getColor() {

		return colorSelect.getColor();

	}

	public String getHtmlColor() {

		Color color;

		if (this.colorSelect.getColor() == null) {

			color = colour;

		}

		else {

			color = this.colorSelect.getColor();

		}

		String transparencia = "";

		if (this.transparency) {

			transparencia = Integer.toHexString(ColorPicker.opacitySlider.getValue());

		}

		return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue()) + transparencia;

	}

}
