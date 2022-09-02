
package com.bric.awt;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.Toolkit;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Timer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

public class ColorSwatch extends JPanel {

	private static final long serialVersionUID = 1L;

	JPopupMenu menu;

	JMenuItem copyItem;

	public static Timer t;

	int w;

	MouseListener mouseListener = new MouseAdapter() {

		public void mousePressed(MouseEvent e) {

			if (e.isPopupTrigger()) {

				if (menu == null) {

					menu = new JPopupMenu();

					copyItem = new JMenuItem(ColorPicker.strings.getObject("Copy").toString());

					menu.add(copyItem);

					copyItem.addActionListener(actionListener);

				}

				menu.show(ColorSwatch.this, e.getX(), e.getY());

			}

		}

	};

	ActionListener actionListener = new ActionListener() {

		public void actionPerformed(ActionEvent e) {

			Object src = e.getSource();

			if (src == copyItem) {

				BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);

				Graphics2D g = image.createGraphics();

				g.setColor(getBackground());

				g.fillRect(0, 0, image.getWidth(), image.getHeight());

				g.dispose();

				Transferable contents = new ImageTransferable(image);

				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(contents, null);

			}

		}

	};

	public ColorSwatch(int width) {

		w = width;

		setPreferredSize(new Dimension(134, 112));

		setMinimumSize(new Dimension(width, width));

		JButton btnNewButton = new JButton("Color Picker");

		btnNewButton.setContentAreaFilled(false);

		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 14));

		btnNewButton.setIcon(new ImageIcon(ColorSwatch.class.getResource("/images/color_picker.png")));

		btnNewButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				try {

					try {

						t = new Timer();

						MyTask mTask = new MyTask();

						t.scheduleAtFixedRate(mTask, 0, 999999999);

					}

					catch (Exception e1) {

						e1.printStackTrace();

					}

				}

				catch (Exception e1) {

				}

			}

		});

		add(btnNewButton);

		addMouseListener(mouseListener);

	}

	private static TexturePaint checkerPaint = null;

	private static TexturePaint getCheckerPaint() {

		if (checkerPaint == null) {

			int t = 8;

			BufferedImage bi = new BufferedImage(t * 2, t * 2, BufferedImage.TYPE_INT_RGB);

			Graphics g = bi.createGraphics();

			g.setColor(Color.white);

			g.fillRect(0, 0, 2 * t, 2 * t);

			g.setColor(Color.lightGray);

			g.fillRect(0, 0, t, t);

			g.fillRect(t, t, t, t);

			checkerPaint = new TexturePaint(bi, new Rectangle(0, 0, bi.getWidth(), bi.getHeight()));

		}

		return checkerPaint;

	}

	public void paint(Graphics g0) {

		super.paint(g0);

		Graphics2D g = (Graphics2D) g0;

		Color c = getForeground();

		int w2 = Math.min(getWidth(), w);

		int h2 = Math.min(getHeight(), w);

		Rectangle r = new Rectangle(getWidth() / 2 - w2 / 2, getHeight() / 2 - h2 / 2, w2, h2);

		if (c.getAlpha() < 255) {

			TexturePaint checkers = getCheckerPaint();

			g.setPaint(checkers);

			g.fillRect(r.x, r.y, r.width, r.height);

		}

		g.setColor(c);

		g.fillRect(r.x, r.y, r.width, r.height);

		PaintUtils.drawBevel(g, r);

	}

}