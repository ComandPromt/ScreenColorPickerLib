package com.bric.awt;

import java.awt.AWTException;
import java.util.TimerTask;

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