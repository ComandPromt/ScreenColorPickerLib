package net.java.dev.colorchooser;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.JComponent;
import javax.swing.UIManager;

public final class ColorChooser extends JComponent {

	public static final String UI_CLASS_ID = "nbColorChooserUI";

	private transient Palette[] palettes = null;

	public Color color = Color.BLUE;

	private transient Color transientColor = null;

	private transient List<ActionListener> actionListenerList;

	public static final String PROP_COLOR = "color";

	public static final String PROP_TRANSIENT_COLOR = "transientColor";

	public static final String PROP_CONTINUOUS_PALETTE = "continuousPalette";

	public static final String PROP_PICKER_VISIBLE = "pickerVisible";

	private boolean continuousPalette = true;

	public static boolean transparency;

	public ColorChooser() {

		this((java.awt.Color) null);

		this.setBackground(Color.WHITE);

		setBackground(Color.WHITE);

	}

	public ColorChooser(Color initialColor) {

		this(null, initialColor);

		this.setBackground(Color.WHITE);

		setBackground(Color.WHITE);

	}

	public ColorChooser(Palette[] palettes, Color initialColor) {

		setPalettes(palettes);

		if (initialColor != null) {

			color = initialColor;

		}

		updateUI();

		this.setBackground(Color.WHITE);

		setBackground(Color.WHITE);

	}

	public ColorChooser(Palette[] palettes) {

		this(palettes, null);

		this.setBackground(Color.WHITE);

		setBackground(Color.WHITE);

	}

	public String getUIClassId() {

		return UI_CLASS_ID;

	}

	public void updateUI() {

		this.setBackground(Color.WHITE);

		setBackground(Color.WHITE);

		if (UIManager.get(UI_CLASS_ID) != null) {

			setUI((ColorChooserUI) UIManager.getUI(this));

		}

		else {

			setUI(DefaultColorChooserUI.createUI(this));

		}

	}

	public Color getColor() {

		return color;

	}

	public void setColor(Color c) {

		if (c.getClass() != Color.class) {

			c = new Color(c.getRed(), c.getGreen(), c.getBlue());

		}

		if ((color != null && !color.equals(c)) || (color == null && c != null)) {

			Color old = color;

			color = c;

			repaint();

			firePropertyChange(PROP_COLOR, old, c);

		}

	}

	void setTransientColor(Color c) {

		Color old = transientColor;

		transientColor = c;

		if ((c != null && !color.equals(old)) || (old == null && c != null)) {

			firePropertyChange(PROP_TRANSIENT_COLOR, old, getTransientColor());

			repaint();

		}

		else if (c == null) {

			firePropertyChange(PROP_TRANSIENT_COLOR, old, getColor());

			repaint();

		}

	}

	public Color getTransientColor() {

		return transientColor == null ? null
				: new Color(transientColor.getRed(), transientColor.getGreen(), transientColor.getBlue());

	}

	public static String colorToString(Color c) {

		RecentColors.getDefault();

		NamedColor named = RecentColors.findNamedColor(c);

		if (named == null) {

			StringBuffer sb = new StringBuffer();

			sb.append(c.getRed());

			sb.append(',');

			sb.append(c.getGreen());

			sb.append(',');

			sb.append(c.getBlue());

			return sb.toString();

		}

		else {

			return named.getDisplayName();

		}

	}

	Color transientColor() {

		return transientColor;

	}

	public static String getColorName(Color color) {

		return PredefinedPalette.getColorName(color);

	}

	public void setContinuousPalettePreferred(boolean val) {

		if (val != continuousPalette) {

			continuousPalette = val;

			setPalettes(null);

			firePropertyChange(PROP_CONTINUOUS_PALETTE, !val, val);

		}

	}

	public boolean isContinuousPalettePreferred() {

		return continuousPalette;

	}

	public void setPalettes(Palette[] palettes) {

		if (palettes != null && palettes.length > 8) {

			throw new IllegalArgumentException("Must be <= 8 palettes");

		}

		Palette[] old = this.palettes;

		if (palettes == null) {

			palettes = Palette.getDefaultPalettes(continuousPalette);

		}

		this.palettes = palettes;

		firePropertyChange("palettes", old, palettes.clone());

	}

	public Palette[] getPalettes() {

		Palette[] result = new Palette[palettes.length];

		System.arraycopy(palettes, 0, result, 0, palettes.length);

		return result;

	}

	static String getString(String key) {

		String BUNDLE = "net.java.dev.colorchooser.Bundle";

		try {

			return ResourceBundle.getBundle(BUNDLE).getString(key);

		}

		catch (MissingResourceException mre) {

			return key;

		}

	}

	public synchronized void addActionListener(java.awt.event.ActionListener listener) {

		if (actionListenerList == null) {

			actionListenerList = new ArrayList<ActionListener>();

		}

		actionListenerList.add(listener);

	}

	public synchronized void removeActionListener(java.awt.event.ActionListener listener) {

		if (actionListenerList != null) {

			actionListenerList.remove(listener);

		}

	}

	@SuppressWarnings("unchecked")

	void fireActionPerformed(ActionEvent event) {

		List<ActionListener> list;

		synchronized (this) {

			if (actionListenerList == null)

				return;

			list = (List<ActionListener>) ((ArrayList<ActionListener>) actionListenerList).clone();

		}

		for (int i = 0; i < list.size(); i++) {

			((java.awt.event.ActionListener) list.get(i)).actionPerformed(event);

		}

	}

	void firePickerVisible(boolean val) {

		firePropertyChange(PROP_PICKER_VISIBLE, !val, val);

	}

}
