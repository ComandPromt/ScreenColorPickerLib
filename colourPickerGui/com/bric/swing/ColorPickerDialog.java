package com.bric.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import net.java.dev.colorchooser.ColorChooser;
import net.java.dev.colorchooser.TextColor;

public class ColorPickerDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	public static ColorPicker cp;

	int alpha;

	JButton ok = new JButton(ColorPicker.strings.getObject("OK").toString());

	JButton cancel = new JButton(ColorPicker.strings.getObject("Cancel").toString());

	Color returnValue = null;

	ActionListener buttonListener = new ActionListener() {

		public void actionPerformed(ActionEvent e) {

			Object src = e.getSource();

			if (src == ok) {

				returnValue = cp.getColor();

				String transparencia = "";

				if (ColorChooser.transparency) {

					transparencia = Integer.toHexString(ColorPicker.opacitySlider.getValue());

				}

				TextColor.color.setText(String.format("#%02x%02x%02x", returnValue.getRed(), returnValue.getGreen(),
						returnValue.getBlue()) + transparencia);

			}

			setVisible(false);

		}

	};

	public ColorPickerDialog(Frame owner, Color color, boolean includeOpacity) {

		super(owner);

		initialize(owner, color, includeOpacity);

	}

	public ColorPickerDialog(Dialog owner, Color color, boolean includeOpacity) {

		super(owner);

		initialize(owner, color, includeOpacity);

	}

	private void initialize(Component owner, Color color, boolean includeOpacity) {

		cp = new ColorPicker(true, includeOpacity);

		super.setModal(true);

		setResizable(false);

		getContentPane().setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;

		c.gridy = 0;

		c.weightx = 1;

		c.weighty = 1;

		c.fill = GridBagConstraints.BOTH;

		c.gridwidth = GridBagConstraints.REMAINDER;

		c.insets = new Insets(10, 10, 10, 10);

		getContentPane().add(cp, c);

		c.gridy++;

		c.gridwidth = 1;

		getContentPane().add(new JPanel(), c);

		c.gridx++;

		c.weightx = 0;

		getContentPane().add(cancel, c);

		c.gridx++;

		c.weightx = 0;

		getContentPane().add(ok, c);

		cp.setRGB(color.getRed(), color.getGreen(), color.getBlue());

		cp.setOpacity(((float) color.getAlpha()) / 255f);

		alpha = color.getAlpha();

		pack();

		setLocationRelativeTo(owner);

		ok.addActionListener(buttonListener);

		cancel.addActionListener(buttonListener);

		getRootPane().setDefaultButton(ok);

	}

	public Color getColor() {

		return returnValue;

	}

}
