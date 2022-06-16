package brokenLinks;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

public class BrokenLinks extends BaseInit {

	@Test
	public void brokenLinks() throws EncryptedDocumentException, InvalidFormatException, IOException {

		logs.info("==========Broken Links Test Start==========");
		msg.append("==========Broken Links Test Start==========" + "/n");

		int totalrows = getTotalRow("URL");
		logs.info("Total URL exist in sheet==" + totalrows);

		for (int row = 1; row < totalrows; row++) {
			String ServerName = getData("URL", row, 1);
			logs.info("Server Name==" + ServerName);
			msg.append("Server Name==" + ServerName + "/n");

			String URL = getData("URL", row, 2);
			logs.info("URL is==" + URL);
			msg.append("URL is==" + URL + "/n");

			// load the URL and check links
			logs.info("=====Broken Links Test Without Login=====");
			msg.append("=====Broken Links Test Without Login=====" + "/n");
			driver.get(URL);
			List<WebElement> links = driver.findElements(By.tagName("a"));

			System.out.println("Total links are " + links.size());

			for (int i = 0; i < links.size(); i++) {

				WebElement ele = links.get(i);

				String url = ele.getAttribute("href");

				verifyLinkActive(url);

			}

			// --Login to URL and check links
			logs.info("=====Broken Links Test After Login=====");
			msg.append("=====Broken Links Test After Login=====" + "/n");

			links = driver.findElements(By.tagName("a"));

			System.out.println("Total links are " + links.size());

			for (int i = 0; i < links.size(); i++) {

				WebElement ele = links.get(i);

				String url = ele.getAttribute("href");

				verifyLinkActive(url);

			}
		}

	}

	public static void verifyLinkActive(String linkUrl) {
		try {
			URL url = new URL(linkUrl);

			HttpURLConnection httpURLConnect = (HttpURLConnection) url.openConnection();

			httpURLConnect.setConnectTimeout(3000);

			httpURLConnect.connect();

			if (httpURLConnect.getResponseCode() == 200) {
				System.out.println(linkUrl + " - " + httpURLConnect.getResponseMessage());
				msg.append(linkUrl + " - " + httpURLConnect.getResponseMessage() + "\n");
			}
			if (httpURLConnect.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
				System.out.println(linkUrl + " - " + httpURLConnect.getResponseMessage() + " - "
						+ HttpURLConnection.HTTP_NOT_FOUND);

				msg.append(linkUrl + " - " + httpURLConnect.getResponseMessage() + " - "
						+ HttpURLConnection.HTTP_NOT_FOUND + "\n");

			}
		} catch (Exception e) {

		}

	}

}
