
package net.java.dev.colorchooser;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

final class DefaultColorChooserUI extends ColorChooserUI {

	DefaultColorChooserUI() {

	}

	private static DefaultColorChooserUI INSTANCE = null;

	public static ComponentUI createUI(JComponent jc) {

		return getDefault();

	}

	static DefaultColorChooserUI getDefault() {

		if (INSTANCE == null) {

			INSTANCE = new DefaultColorChooserUI();

		}

		return INSTANCE;

	}

	protected void init(ColorChooser c) {

		c.setBorder(null);

	}

	protected void uninit(ColorChooser c) {

		if (c.getBorder() instanceof CCBorder) {

			c.setBorder(null);

		}

	}

	public void paint(Graphics g, JComponent c) {

		ColorChooser chooser = (ColorChooser) c;

		Color col = chooser.transientColor() != null ? chooser.transientColor() : chooser.getColor();

		g.setColor(col);

		g.fillRect(0, 0, chooser.getWidth() - 1, chooser.getHeight() - 1);

		if (chooser.hasFocus()) {

			g.setColor(invertColor(col));

			g.drawRect(4, 4, chooser.getWidth() - 8, chooser.getHeight() - 8);

		}

	}

	private static final Color invertColor(Color c) {

		int r = checkRange(255 - c.getRed());

		int g = checkRange(255 - c.getGreen());

		int b = checkRange(255 - c.getBlue());

		return new Color(r, g, b);

	}

	private static final int checkRange(int i) {

		int result = i;

		if (Math.abs(128 - i) < 24) {

			result = Math.abs(128 - i);

		}

		return result;

	}

}
