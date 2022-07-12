
package com.bric.swing;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.java.dev.colorchooser.demo.CopyColor;

public class ColorPicker extends JPanel {

	private static final long serialVersionUID = 3L;

	public static final String SELECTED_COLOR_PROPERTY = "selected color";

	public static final String MODE_CONTROLS_VISIBLE_PROPERTY = "mode controls visible";

	public static final String OPACITY_PROPERTY = "opacity";

	public static final String MODE_PROPERTY = "mode";

	protected static final int HUE = 0;

	protected static final int BRI = 1;

	protected static final int SAT = 2;

	protected static final int RED = 3;

	protected static final int GREEN = 4;

	protected static final int BLUE = 5;

	private JSlider slider = new JSlider(JSlider.VERTICAL, 0, 100, 0);

	protected static ResourceBundle strings = ResourceBundle.getBundle("com.bric.swing.resources.ColorPicker");

	public static Color showDialog(Container owner, Color originalColor) {

		if (owner instanceof Window) {

			return showDialog((Window) owner, originalColor);

		}

		else {

			Toolkit.getDefaultToolkit().beep();

		}

		return null;

	}

	public static Color showDialog(Window owner, Color originalColor) {

		return showDialog(owner, null, originalColor, false);

	}

	public static Color showDialog(Window owner, Color originalColor, boolean includeOpacity) {

		return showDialog(owner, null, originalColor, includeOpacity);

	}

	public static Color showDialog(Window owner, String title, Color originalColor, boolean includeOpacity) {

		ColorPickerDialog d;

		if (owner instanceof Frame || owner == null) {

			d = new ColorPickerDialog((Frame) owner, originalColor, includeOpacity);

		}

		else if (owner instanceof Dialog) {

			d = new ColorPickerDialog((Dialog) owner, originalColor, includeOpacity);

		}

		else {

			throw new IllegalArgumentException(
					"the owner (" + owner.getClass().getName() + ") must be a java.awt.Frame or a java.awt.Dialog");

		}

		d.setTitle(title == null ? strings.getObject("ColorPickerDialogTitle").toString() : title);

		d.setBackground(Color.WHITE);

		d.pack();

		d.setVisible(true);

		return d.getColor();

	}

	ChangeListener changeListener = new ChangeListener() {

		public void stateChanged(ChangeEvent e) {

			Object src = e.getSource();

			if (hue.contains(src) || sat.contains(src) || bri.contains(src)) {

				if (adjustingSpinners > 0)

					return;

				setHSB(hue.getFloatValue() / 360f, sat.getFloatValue() / 100f, bri.getFloatValue() / 100f);

			}

			else if (red.contains(src) || green.contains(src) || blue.contains(src)) {

				if (adjustingSpinners > 0)

					return;

				setRGB(red.getIntValue(), green.getIntValue(), blue.getIntValue());

			}

			else if (src == colorPanel) {

				if (adjustingColorPanel > 0)

					return;

				int mode = getMode();

				if (mode == HUE || mode == BRI || mode == SAT) {

					float[] hsb = colorPanel.getHSB();

					setHSB(hsb[0], hsb[1], hsb[2]);

				}

				else {

					int[] rgb = colorPanel.getRGB();

					setRGB(rgb[0], rgb[1], rgb[2]);

				}

			}

			else if (src == slider) {

				if (adjustingSlider > 0)

					return;

				int v = slider.getValue();

				Option option = getSelectedOption();

				option.setValue(v);

			}

			else if (alpha.contains(src)) {

				if (adjustingOpacity > 0)

					return;

				int v = alpha.getIntValue();

				setOpacity(((float) v) / 255f);

			}

			else if (src == opacitySlider) {

				if (adjustingOpacity > 0)

					return;

				float newValue = (((float) opacitySlider.getValue()) / 255f);

				setOpacity(newValue);

			}

		}

	};

	ActionListener actionListener = new ActionListener() {

		public void actionPerformed(ActionEvent e) {

			Object src = e.getSource();

			if (src == hue.radioButton) {

				setMode(HUE);

			}

			else if (src == bri.radioButton) {

				setMode(BRI);

			}

			else if (src == sat.radioButton) {

				setMode(SAT);

			}

			else if (src == red.radioButton) {

				setMode(RED);

			}

			else if (src == green.radioButton) {

				setMode(GREEN);

			}

			else if (src == blue.radioButton) {

				setMode(BLUE);

			}

		}

	};

	private Option getSelectedOption() {

		int mode = getMode();

		if (mode == HUE) {

			return hue;

		}

		else if (mode == SAT) {

			return sat;

		}

		else if (mode == BRI) {

			return bri;

		}

		else if (mode == RED) {

			return red;

		}

		else if (mode == GREEN) {

			return green;

		}

		else {

			return blue;

		}

	}

	class HexUpdateThread extends Thread {

		long myStamp;

		String text;

		public HexUpdateThread(long stamp, String s) {

			myStamp = stamp;

			text = s;

		}

		public void run() {

			if (SwingUtilities.isEventDispatchThread() == false) {

				long wait = 1500;

				while (System.currentTimeMillis() - myStamp < wait) {

					try {

						long delay = wait - (System.currentTimeMillis() - myStamp);

						if (delay < 1)
							delay = 1;

						Thread.sleep(delay);

					}

					catch (Exception e) {

						Thread.yield();

					}

				}

				SwingUtilities.invokeLater(this);

				return;

			}

			if (myStamp != hexDocListener.lastTimeStamp) {

				return;

			}

			if (text.length() > 6)

				text = text.substring(0, 6);

			while (text.length() < 6) {

				text = text + "0";

			}

			if (hexField.getText().equals(text))

				return;

			int pos = hexField.getCaretPosition();

			hexField.setText(text);

			hexField.setCaretPosition(pos);

		}

	}

	HexDocumentListener hexDocListener = new HexDocumentListener();

	class HexDocumentListener implements DocumentListener {

		long lastTimeStamp;

		public void changedUpdate(DocumentEvent e) {

			lastTimeStamp = System.currentTimeMillis();

			if (adjustingHexField > 0)

				return;

			String s = hexField.getText();

			s = stripToHex(s);

			if (s.length() == 6) {

				try {

					int i = Integer.parseInt(s, 16);

					setRGB(((i >> 16) & 0xff), ((i >> 8) & 0xff), ((i) & 0xff));

					return;

				}

				catch (NumberFormatException e2) {

				}

			}

			Thread thread = new HexUpdateThread(lastTimeStamp, s);

			thread.start();

			while (System.currentTimeMillis() - lastTimeStamp == 0) {

				Thread.yield();

			}

		}

		private String stripToHex(String s) {

			s = s.toUpperCase();

			String s2 = "";

			for (int a = 0; a < s.length(); a++) {

				char c = s.charAt(a);

				if (c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7'
						|| c == '8' || c == '9' || c == '0' || c == 'A' || c == 'B' || c == 'C' || c == 'D' || c == 'E'
						|| c == 'F') {

					s2 = s2 + c;

				}

			}

			return s2;

		}

		public void insertUpdate(DocumentEvent e) {

			changedUpdate(e);

		}

		public void removeUpdate(DocumentEvent e) {

			changedUpdate(e);

		}

	};

	private Option alpha = new Option(strings.getObject("alphaLabel").toString(), 255);

	private Option hue = new Option(strings.getObject("hueLabel").toString(), 360);

	private Option sat = new Option(strings.getObject("saturationLabel").toString(), 100);

	private Option bri = new Option(strings.getObject("brightnessLabel").toString(), 100);

	private Option red = new Option(strings.getObject("redLabel").toString(), 255);

	private Option green = new Option(strings.getObject("greenLabel").toString(), 255);

	private Option blue = new Option(strings.getObject("blueLabel").toString(), 255);

	private ColorSwatch preview = new ColorSwatch(50);

	private JLabel hexLabel = new JLabel(strings.getObject("hexLabel").toString());

	private JTextField hexField = new JTextField("000000");

	private int adjustingSpinners = 0;

	private int adjustingSlider = 0;

	private int adjustingColorPanel = 0;

	private int adjustingHexField = 0;

	private int adjustingOpacity = 0;

	private JPanel expertControls = new JPanel(new GridBagLayout());

	private ColorPickerPanel colorPanel = new ColorPickerPanel();

	public static JSlider opacitySlider = new JSlider(0, 255, 255);

	private JLabel opacityLabel = new JLabel(strings.getObject("opacityLabel").toString());

	public ColorPicker() {

		this(true, false);

	}

	public ColorPicker(boolean showExpertControls, boolean includeOpacity) {

		super(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		Insets normalInsets = new Insets(3, 3, 3, 3);

		JPanel options = new JPanel(new GridBagLayout());

		c.gridx = 0;

		c.gridy = 0;

		c.weightx = 1;

		c.weighty = 1;

		c.insets = normalInsets;

		ButtonGroup bg = new ButtonGroup();

		Option[] optionsArray = new Option[] { hue, sat, bri, red, green, blue };

		for (int a = 0; a < optionsArray.length; a++) {

			if (a == 3 || a == 6) {

				c.insets = new Insets(normalInsets.top + 10, normalInsets.left, normalInsets.bottom,
						normalInsets.right);

			}

			else {

				c.insets = normalInsets;

			}

			c.anchor = GridBagConstraints.EAST;

			c.fill = GridBagConstraints.NONE;

			options.add(optionsArray[a].label, c);

			c.gridx++;

			c.anchor = GridBagConstraints.WEST;

			c.fill = GridBagConstraints.HORIZONTAL;

			if (optionsArray[a].spinner != null) {

				options.add(optionsArray[a].spinner, c);

			}

			else {

				options.add(optionsArray[a].slider, c);

			}

			c.gridx++;

			c.fill = GridBagConstraints.NONE;

			options.add(optionsArray[a].radioButton, c);

			c.gridy++;

			c.gridx = 0;

			bg.add(optionsArray[a].radioButton);

		}

		c.insets = new Insets(normalInsets.top + 10, normalInsets.left, normalInsets.bottom, normalInsets.right);

		c.anchor = GridBagConstraints.EAST;

		c.fill = GridBagConstraints.NONE;

		options.add(hexLabel, c);

		c.gridx++;

		c.anchor = GridBagConstraints.WEST;

		c.fill = GridBagConstraints.HORIZONTAL;

		options.add(hexField, c);

		c.gridy++;

		c.gridx = 0;

		c.anchor = GridBagConstraints.EAST;

		c.fill = GridBagConstraints.NONE;

		options.add(alpha.label, c);

		c.gridx++;

		c.anchor = GridBagConstraints.WEST;

		c.fill = GridBagConstraints.HORIZONTAL;

		options.add(alpha.spinner, c);

		c.gridx = 0;

		c.gridy = 0;

		c.weightx = 1;

		c.weighty = 1;

		c.fill = GridBagConstraints.BOTH;

		c.anchor = GridBagConstraints.CENTER;

		c.insets = normalInsets;

		c.gridwidth = 2;

		add(colorPanel, c);

		c.gridwidth = 1;

		c.insets = normalInsets;

		c.gridx += 2;

		c.weighty = 1;

		c.gridwidth = 1;

		c.fill = GridBagConstraints.VERTICAL;

		c.weightx = 0;

		add(slider, c);

		c.gridx++;

		c.fill = GridBagConstraints.VERTICAL;

		c.gridheight = c.REMAINDER;

		c.anchor = GridBagConstraints.CENTER;

		c.insets = new Insets(0, 0, 0, 0);

		add(expertControls, c);

		c.gridx = 0;

		c.gridheight = 1;

		c.gridy = 1;

		c.weightx = 0;

		c.weighty = 0;

		c.insets = normalInsets;

		c.anchor = c.CENTER;

		add(opacityLabel, c);

		c.gridx++;

		c.gridwidth = 2;

		c.weightx = 1;

		c.fill = c.HORIZONTAL;

		add(opacitySlider, c);

		c.gridx = 0;

		c.gridy = 0;

		c.gridheight = 1;

		c.gridwidth = 1;

		c.fill = GridBagConstraints.BOTH;

		c.weighty = 1;

		c.anchor = GridBagConstraints.CENTER;

		c.weightx = 1;

		c.insets = new Insets(normalInsets.top, normalInsets.left + 8, normalInsets.bottom + 10,
				normalInsets.right + 8);

		expertControls.add(preview, c);

		c.gridy++;

		c.weighty = 0;

		c.anchor = GridBagConstraints.CENTER;

		c.insets = new Insets(normalInsets.top, normalInsets.left, 0, normalInsets.right);

		expertControls.add(options, c);

		preview.setOpaque(true);

		colorPanel.setPreferredSize(
				new Dimension(expertControls.getPreferredSize().height, expertControls.getPreferredSize().height));

		slider.addChangeListener(changeListener);

		colorPanel.addChangeListener(changeListener);

		slider.setUI(new ColorPickerSliderUI(slider, this));

		hexField.getDocument().addDocumentListener(hexDocListener);

		setMode(HUE);

		setExpertControlsVisible(showExpertControls);

		setOpacityVisible(CopyColor.transparency);

		opacitySlider.addChangeListener(changeListener);

		setOpacity(1);

	}

	public void setHexControlsVisible(boolean b) {

		hexLabel.setVisible(b);

		hexField.setVisible(b);

	}

	public void setPreviewSwatchVisible(boolean b) {

		preview.setVisible(b);

	}

	public void setExpertControlsVisible(boolean b) {

		expertControls.setVisible(b);

	}

	public float[] getHSB() {

		return new float[] { hue.getFloatValue() / 360f, sat.getFloatValue() / 100f, bri.getFloatValue() / 100f };

	}

	public int[] getRGB() {

		return new int[] { red.getIntValue(), green.getIntValue(), blue.getIntValue() };

	}

	public float getOpacity() {

		return ((float) opacitySlider.getValue()) / 255f;

	}

	private float lastOpacity = 1;

	public void setOpacity(float v) {

		if (v < 0 || v > 1)

			throw new IllegalArgumentException("The opacity (" + v + ") must be between 0 and 1.");

		adjustingOpacity++;

		try {

			int i = (int) (255 * v);

			opacitySlider.setValue(i);

			alpha.spinner.setValue(i);

			if (lastOpacity != v) {

				firePropertyChange(OPACITY_PROPERTY, lastOpacity, i);

				Color c = preview.getForeground();

				preview.setForeground(new Color(c.getRed(), c.getGreen(), c.getBlue(), i));

			}

			lastOpacity = v;

		}

		finally {

			adjustingOpacity--;

		}

	}

	public void setMode(int mode) {

		if (!(mode == HUE || mode == SAT || mode == BRI || mode == RED || mode == GREEN || mode == BLUE))
			throw new IllegalArgumentException("mode must be HUE, SAT, BRI, REd, GREEN, or BLUE");

		putClientProperty(MODE_PROPERTY, mode);

		hue.radioButton.setSelected(mode == HUE);

		sat.radioButton.setSelected(mode == SAT);

		bri.radioButton.setSelected(mode == BRI);

		red.radioButton.setSelected(mode == RED);

		green.radioButton.setSelected(mode == GREEN);

		blue.radioButton.setSelected(mode == BLUE);

		if (mode != RED && mode != GREEN && mode != BLUE) {

			colorPanel.setMode(HUE);

		}

		else {

			colorPanel.setMode(mode);

		}

		try {

			slider.setValue(0);

			Option option = getSelectedOption();

			slider.setInverted(mode == HUE);

			slider.setMaximum(option.getMaximum());

			slider.setValue(option.getIntValue());

			slider.repaint();

			if (mode == HUE || mode == SAT || mode == BRI) {

				setHSB(hue.getFloatValue() / 360f, sat.getFloatValue() / 100f, bri.getFloatValue() / 100f);

			}

			else {

				setRGB(red.getIntValue(), green.getIntValue(), blue.getIntValue());

			}

		}

		finally {

		}

	}

	public void setModeControlsVisible(boolean b) {

		hue.radioButton.setVisible(b && hue.isVisible());

		sat.radioButton.setVisible(b && sat.isVisible());

		bri.radioButton.setVisible(b && bri.isVisible());

		red.radioButton.setVisible(b && red.isVisible());

		green.radioButton.setVisible(b && green.isVisible());

		blue.radioButton.setVisible(b && blue.isVisible());

		putClientProperty(MODE_CONTROLS_VISIBLE_PROPERTY, b);

	}

	public int getMode() {

		Integer i = (Integer) getClientProperty(MODE_PROPERTY);

		if (i == null)

			return -1;

		return i.intValue();

	}

	public void setColor(Color c) {

		setRGB(c.getRed(), c.getGreen(), c.getBlue());

		float opacity = ((float) c.getAlpha()) / 255f;

		setOpacity(opacity);

	}

	public void setRGB(int r, int g, int b) {

		if (r < 0 || r > 255)

			throw new IllegalArgumentException("The red value (" + r + ") must be between [0,255].");

		if (g < 0 || g > 255)
			throw new IllegalArgumentException("The green value (" + g + ") must be between [0,255].");

		if (b < 0 || b > 255)
			throw new IllegalArgumentException("The blue value (" + b + ") must be between [0,255].");

		Color lastColor = getColor();

		boolean updateRGBSpinners = adjustingSpinners == 0;

		adjustingSpinners++;

		adjustingColorPanel++;

		int alpha = this.alpha.getIntValue();

		try {

			if (updateRGBSpinners) {

				red.setValue(r);

				green.setValue(g);

				blue.setValue(b);

			}

			preview.setForeground(new Color(r, g, b, alpha));

			float[] hsb = new float[3];

			Color.RGBtoHSB(r, g, b, hsb);

			hue.setValue((int) (hsb[0] * 360f + .49f));

			sat.setValue((int) (hsb[1] * 100f + .49f));

			bri.setValue((int) (hsb[2] * 100f + .49f));

			colorPanel.setRGB(r, g, b);

			updateHexField();

			updateSlider();

		}

		finally {

			adjustingSpinners--;

			adjustingColorPanel--;

		}

		Color newColor = getColor();

		if (lastColor.equals(newColor) == false)

			firePropertyChange(SELECTED_COLOR_PROPERTY, lastColor, newColor);

	}

	public Color getColor() {

		int[] i = getRGB();

		return new Color(i[0], i[1], i[2], opacitySlider.getValue());

	}

	private void updateSlider() {

		adjustingSlider++;

		try {

			int mode = getMode();

			if (mode == HUE) {

				slider.setValue(hue.getIntValue());

			}

			else if (mode == SAT) {

				slider.setValue(sat.getIntValue());

			}

			else if (mode == BRI) {

				slider.setValue(bri.getIntValue());

			}

			else if (mode == RED) {

				slider.setValue(red.getIntValue());

			}

			else if (mode == GREEN) {

				slider.setValue(green.getIntValue());

			}

			else if (mode == BLUE) {

				slider.setValue(blue.getIntValue());

			}

		}

		finally {

			adjustingSlider--;

		}

		slider.repaint();

	}

	public JPanel getExpertControls() {

		return expertControls;

	}

	public void setRGBControlsVisible(boolean b) {

		red.setVisible(b);

		green.setVisible(b);

		blue.setVisible(b);

	}

	public void setHSBControlsVisible(boolean b) {

		hue.setVisible(b);

		sat.setVisible(b);

		bri.setVisible(b);

	}

	public void setOpacityVisible(boolean b) {

		opacityLabel.setVisible(b);

		opacitySlider.setVisible(b);

		alpha.label.setVisible(b);

		alpha.spinner.setVisible(b);

	}

	public ColorPickerPanel getColorPanel() {

		return colorPanel;

	}

	public void setHSB(float h, float s, float b) {

		if (Float.isInfinite(h) || Float.isNaN(h))

			throw new IllegalArgumentException("The hue value (" + h + ") is not a valid number.");

		while (h < 0)

			h++;

		while (h > 1)

			h--;

		if (s < 0 || s > 1)

			throw new IllegalArgumentException("The saturation value (" + s + ") must be between [0,1]");

		if (b < 0 || b > 1)

			throw new IllegalArgumentException("The brightness value (" + b + ") must be between [0,1]");

		Color lastColor = getColor();

		boolean updateHSBSpinners = adjustingSpinners == 0;

		adjustingSpinners++;

		adjustingColorPanel++;

		try {

			if (updateHSBSpinners) {

				hue.setValue((int) (h * 360f + .49f));

				sat.setValue((int) (s * 100f + .49f));

				bri.setValue((int) (b * 100f + .49f));

			}

			Color c = new Color(Color.HSBtoRGB(h, s, b));

			int alpha = this.alpha.getIntValue();

			c = new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);

			preview.setForeground(c);

			red.setValue(c.getRed());

			green.setValue(c.getGreen());

			blue.setValue(c.getBlue());

			colorPanel.setHSB(h, s, b);

			updateHexField();

			updateSlider();

			slider.repaint();

		}

		finally {

			adjustingSpinners--;

			adjustingColorPanel--;

		}

		Color newColor = getColor();

		if (lastColor.equals(newColor) == false)

			firePropertyChange(SELECTED_COLOR_PROPERTY, lastColor, newColor);

	}

	private void updateHexField() {

		adjustingHexField++;

		try {

			int r = red.getIntValue();

			int g = green.getIntValue();

			int b = blue.getIntValue();

			int i = (r << 16) + (g << 8) + b;

			String s = Integer.toHexString(i).toUpperCase();

			while (s.length() < 6)

				s = "0" + s;

			if (hexField.getText().equalsIgnoreCase(s) == false)

				hexField.setText(s);

		}

		finally {

			adjustingHexField--;

		}

	}

	class Option {

		JRadioButton radioButton = new JRadioButton();

		JSpinner spinner;

		JSlider slider;

		JLabel label;

		public Option(String text, int max) {

			spinner = new JSpinner(new SpinnerNumberModel(0, 0, max, 5));

			spinner.addChangeListener(changeListener);

			label = new JLabel(text);

			radioButton.addActionListener(actionListener);

		}

		public void setValue(int i) {

			if (slider != null) {

				slider.setValue(i);

			}

			if (spinner != null) {

				spinner.setValue(i);

			}

		}

		public int getMaximum() {

			if (slider != null)

				return slider.getMaximum();

			return ((Number) ((SpinnerNumberModel) spinner.getModel()).getMaximum()).intValue();

		}

		public boolean contains(Object src) {

			return (src == slider || src == spinner || src == radioButton || src == label);

		}

		public float getFloatValue() {

			return getIntValue();

		}

		public int getIntValue() {

			if (slider != null)

				return slider.getValue();

			return ((Number) spinner.getValue()).intValue();

		}

		public boolean isVisible() {

			return label.isVisible();

		}

		public void setVisible(boolean b) {

			boolean radioButtonsAllowed = true;

			Boolean z = (Boolean) getClientProperty(MODE_CONTROLS_VISIBLE_PROPERTY);

			if (z != null)

				radioButtonsAllowed = z.booleanValue();

			radioButton.setVisible(b && radioButtonsAllowed);

			if (slider != null)

				slider.setVisible(b);

			if (spinner != null)

				spinner.setVisible(b);

			label.setVisible(b);

		}

	}

}
