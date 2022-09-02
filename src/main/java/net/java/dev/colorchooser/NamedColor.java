
package net.java.dev.colorchooser;

import java.awt.Color;

abstract class NamedColor extends Color implements Comparable<Object> {

	private static final long serialVersionUID = 1L;

	protected NamedColor(String name, int r, int g, int b) {

		super(r, g, b);

	}

	public abstract String getDisplayName();

	public abstract String getName();

	public String getInstantiationCode() {

		return toString();

	}

	static NamedColor create(Color c, String name) {

		return new DefaultNamedColor(c, name);

	}

	private static final class DefaultNamedColor extends NamedColor {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String name;

		public DefaultNamedColor(Color c, String name) {

			super(name, c.getRed(), c.getGreen(), c.getBlue());

			this.name = name;

		}

		public String getDisplayName() {

			return name;

		}

		public String getName() {

			return name;

		}

		public int compareTo(Object o) {

			if (o instanceof NamedColor) {

				NamedColor nc = (NamedColor) o;

				String nm = nc.getDisplayName();

				if (nm == null && getDisplayName() == null) {

					return 0;

				}

				else {

					return nm != null && getDisplayName() != null ? getDisplayName().compareTo(nm) : -1;

				}

			}

			else {

				return -1;

			}

		}

	}

}
