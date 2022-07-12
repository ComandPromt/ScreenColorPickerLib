
package net.java.dev.colorchooser;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public abstract class Palette {

	public abstract Color getColorAt(int x, int y);

	public abstract String getNameAt(int x, int y);

	public abstract void paintTo(Graphics g);

	public abstract Dimension getSize();

	public abstract String getDisplayName();

	public static final Palette[] getDefaultPalettes(boolean continuousFirst) {

		Palette[] result = new Palette[8];

		Palette[] first = continuousFirst ?

				ContinuousPalette.createDefaultPalettes() :

				PredefinedPalette.createDefaultPalettes();

		Palette[] second = !continuousFirst ?

				ContinuousPalette.createDefaultPalettes() :

				PredefinedPalette.createDefaultPalettes();

		result = new Palette[second.length + first.length];

		System.arraycopy(first, 0, result, 0, 4);

		System.arraycopy(second, 0, result, 4, 4);

		return result;

	}

	public static final Palette createContinuousPalette(String name, Dimension size, float saturation) {

		if (size.width <= 0)
			throw new IllegalArgumentException("width less than or equal 0");

		if (size.height <= 0)
			throw new IllegalArgumentException("height less than or equal 0");

		return new ContinuousPalette(name, size.width, size.height, saturation);

	}

	public static final Palette createPredefinedPalette(String name, Color[] colors, String[] names) {

		NamedColor[] cc = new NamedColor[colors.length];

		for (int i = 0; i < colors.length; i++) {

			cc[i] = NamedColor.create(colors[i], names[i]);

		}

		return new PredefinedPalette(name, cc);

	}

}
