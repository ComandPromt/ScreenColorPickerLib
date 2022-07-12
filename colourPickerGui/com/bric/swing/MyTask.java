package com.bric.swing;

import java.awt.AWTException;
import java.util.TimerTask;

import com.colourpicker.ColourPicker;

public class MyTask extends TimerTask {

	@Override

	public void run() {

		try {

			new ColourPicker();

		}

		catch (AWTException e) {

		}

	}

}