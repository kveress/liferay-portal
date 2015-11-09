/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.poshi.runner.selenium;

import com.liferay.poshi.runner.util.OSDetector;
import com.liferay.poshi.runner.util.PropsValues;
import com.liferay.poshi.runner.util.StringPool;
import com.liferay.poshi.runner.util.StringUtil;

import io.appium.java_client.android.AndroidDriver;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * @author Brian Wing Shun Chan
 * @author Michael Hashimoto
 */
public class WebDriverUtil extends PropsValues {

	public static WebDriver getWebDriver() {
		return _instance._getWebDriver();
	}

	public static void startWebDriver() {
		_instance._startWebDriver();
	}

	public static void stopWebDriver() {
		_instance._stopWebDriver();
	}

	private WebDriver _getAndroidDriver() {
		DesiredCapabilities desiredCapabilities = DesiredCapabilities.android();

		desiredCapabilities.setCapability("browserName", "Browser");
		desiredCapabilities.setCapability("deviceName", "deviceName");
		desiredCapabilities.setCapability("platformName", "Android");
		desiredCapabilities.setCapability("platformVersion", "4.4");

		URL url = null;

		try {
			url = new URL("http://0.0.0.0:4723/wd/hub/");
		}
		catch (MalformedURLException murle) {
		}

		return new AndroidDriver(url, desiredCapabilities);
	}

	private WebDriver _getChromeAndroidDriver() {
		DesiredCapabilities desiredCapabilities = DesiredCapabilities.android();

		desiredCapabilities.setCapability("browserName", "Chrome");
		desiredCapabilities.setCapability(
			"deviceName", PropsValues.MOBILE_DEVICE_NAME);
		desiredCapabilities.setCapability("platformName", "Android");
		desiredCapabilities.setCapability("platformVersion", "5.0.1");

		URL url = null;

		try {
			url = new URL("http://0.0.0.0:4723/wd/hub/");
		}
		catch (MalformedURLException murle) {
		}

		return new AndroidDriver(url, desiredCapabilities);
	}

	private WebDriver _getChromeDriver() {
		DesiredCapabilities desiredCapabilities = DesiredCapabilities.chrome();

		Map<String, Object> preferences = new HashMap<>();

		String outputDirName = PropsValues.OUTPUT_DIR_NAME;

		if (OSDetector.isWindows()) {
			outputDirName = StringUtil.replace(
				outputDirName, StringPool.FORWARD_SLASH, StringPool.BACK_SLASH);
		}

		preferences.put("download.default_directory", outputDirName);

		preferences.put("download.prompt_for_download", false);

		desiredCapabilities.setCapability("chrome.prefs", preferences);

		return new ChromeDriver(desiredCapabilities);
	}

	private WebDriver _getEdgeDriver() {
		return new EdgeDriver();
	}

	private WebDriver _getWebDriver() {
		return _webDriver;
	}

	private void _startWebDriver() {
		if (BROWSER_TYPE.equals("android")) {
			_webDriver = _getAndroidDriver();
		}
		else if (BROWSER_TYPE.equals("androidchrome")) {
			_webDriver = _getChromeAndroidDriver();
		}
		else if (BROWSER_TYPE.equals("chrome")) {
			_webDriver = _getChromeDriver();
		}
		else if (BROWSER_TYPE.equals("edge") &&
				 !SELENIUM_REMOTE_DRIVER_ENABLED) {

			_webDriver = _getEdgeDriver();
		}
		else {
			throw new RuntimeException("Invalid browser type " + BROWSER_TYPE);
		}
	}

	private void _stopWebDriver() {
		if (_webDriver != null) {
			_webDriver.quit();
		}

		_webDriver = null;
	}

	private static final WebDriverUtil _instance = new WebDriverUtil();

	private WebDriver _webDriver;

}