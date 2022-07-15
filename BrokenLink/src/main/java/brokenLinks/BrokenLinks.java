package brokenLinks;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

public class BrokenLinks extends BaseInit {
	public static StringBuilder msg1 = new StringBuilder();

	@Test
	public void brokenLinks() throws EncryptedDocumentException, InvalidFormatException, IOException {
		WebDriverWait wait = new WebDriverWait(driver, 50);
		String Env = storage.getProperty("Env");

		int totalrows = getTotalRow("URL");
		logs.info("Total URL exist in sheet==" + totalrows);

		for (int row = 7; row < totalrows; row++) {
			StringBuilder msg = new StringBuilder();
			logs.info("===============Broken Links Test Start===============");
			msg.append("===============Broken Links Test Start===============" + "\n\n");
			String ServerName = getData("URL", row, 0);
			logs.info("Server Name==" + ServerName);
			msg.append("Server Name==" + ServerName + "\n");

			String URL = getData("URL", row, 1);
			logs.info("URL is==" + URL);
			msg.append("URL is==" + URL + "\n\n");

			// load the URL and check links
			logs.info("=====Broken Links Test Without Login=====");
			msg.append("=====Broken Links Test Without Login=====" + "\n");

			driver.get(URL);

			List<WebElement> links = driver.findElements(By.tagName("a"));

			System.out.println("Total links are " + links.size());
			logs.info("Total links are " + links.size());
			msg.append("Total links are " + links.size() + "\n\n");

			for (int i = 0; i < links.size(); i++) {

				WebElement ele = links.get(i);

				String linkUrl = ele.getAttribute("href");

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

			ServerName = getData("URL", row, 0);

			// --URL without login credentials
			if (ServerName.equalsIgnoreCase("FedEx CIL/ MDSI")
					|| ServerName.equalsIgnoreCase("Special Support - FedEx RW/ CR_46")
					|| ServerName.equalsIgnoreCase("Test Utility (Cheetah Order Processing)")) {
				msg.append("\n\n\n" + "Broken Link test Done for " + ServerName + "\n\n\n");

				Env = storage.getProperty("Env");
				String subject = "Selenium Automation Script: " + Env + " " + ServerName + " Broken Links";
				String File = ".\\Report\\ExtentReport\\ExtentReportResults.html,.\\Report\\log\\BrokenLinks.html";

				try {
					SendEmail.sendMail("ravina.prajapati@samyak.com,parth.doshi@samyak.com,asharma@samyak.com", subject,
							msg.toString(), File);

				} catch (Exception ex) {
					logs.error(ex);
				}

			}

			// --Login to URL and check links
			else if (ServerName.equalsIgnoreCase("Connect")) {
				logs.info("=====Broken Links Test After Login=====");
				msg.append("\n\n" + "=====Broken Links Test After Login=====" + "\n\n");

				driver.get(URL);
				try {
					String UserName = getData("URL", row, 2);
					String Password = getData("URL", row, 3);
					wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("login")));
					highLight(isElementPresent("UserName_id"), driver);
					isElementPresent("UserName_id").sendKeys(UserName);
					logs.info("Entered UserName");
					highLight(isElementPresent("Password_id"), driver);
					isElementPresent("Password_id").sendKeys(Password);
					logs.info("Entered Password");
					highLight(isElementPresent("Login_id"), driver);
					isElementPresent("Login_id").click();
					logs.info("Login done");
					wait.until(ExpectedConditions
							.visibilityOfElementLocated(By.xpath("//*[contains(text(),'Logging In...')]")));
					wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));

					try {
						wait.until(ExpectedConditions
								.visibilityOfAllElementsLocatedBy(By.xpath("//*[@class=\"welcomecontent\"]")));

					} catch (Exception ee) {
						WebDriverWait waitConnect = new WebDriverWait(driver, 180);
						waitConnect.until(ExpectedConditions
								.visibilityOfAllElementsLocatedBy(By.xpath("//*[@class=\"welcomecontent\"]")));

					}
					Thread.sleep(10000);

					links = driver.findElements(By.tagName("a"));

					System.out.println("Total links are " + links.size());
					logs.info("Total links are " + links.size());
					msg.append("Total links are " + links.size() + "\n");

					for (int i = 0; i < links.size(); i++) {

						WebElement ele = links.get(i);

						String linkUrl = ele.getAttribute("href");

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
					msg.append("\n\n\n" + "Broken Link test Done for " + ServerName + "\n\n\n");

					Env = storage.getProperty("Env");
					String subject = "Selenium Automation Script: " + Env + " " + ServerName + " Broken Links";
					String File = ".\\Report\\ExtentReport\\ExtentReportResults.html,.\\Report\\log\\BrokenLinks.html";

					try {
						SendEmail.sendMail("ravina.prajapati@samyak.com,parth.doshi@samyak.com,asharma@samyak.com",
								subject, msg.toString(), File);

					} catch (Exception ex) {
						logs.error(ex);
					}
				} catch (Exception e) {
					logs.info("Issue with URL==FAIL");
					msg1.append("Issue with URL==FAIL" + "\n");
					getScreenshot(driver, "URLIssue");

					Env = storage.getProperty("Env");
					String subject = "Selenium Automation Script: " + Env + ServerName + " Login";
					String File = ".//Report//Screnshots//URLIssue.png";

					try {
						SendEmail.sendMail("ravina.prajapati@samyak.com", subject, msg.toString(), File);

					} catch (Exception ex) {
						logs.error(ex);
					}

				}

			} else if (ServerName.equalsIgnoreCase("NetAgent")) {
				logs.info("=====Broken Links Test After Login=====");
				msg.append("\n\n" + "=====Broken Links Test After Login=====" + "\n\n");

				driver.get(URL);
				try {
					wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("login")));
					wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.name("loginForm")));
					String UserName = getData("URL", row, 2);
					String Password = getData("URL", row, 3);
					// Enter User_name and Password and click on Login
					isElementPresent("NAUserName_id").clear();
					isElementPresent("NAUserName_id").sendKeys(UserName);
					isElementPresent("NAPassword_id").clear();
					isElementPresent("NAPassword_id").sendKeys(Password);
					isElementPresent("NALogin_id").click();
					wait.until(ExpectedConditions
							.invisibilityOfElementLocated(By.xpath("//*[@class=\"ajax-loadernew\"]")));
					wait.until(ExpectedConditions
							.visibilityOfAllElementsLocatedBy(By.xpath("//*[@class=\"welcomecontent\"]")));

					try {
						wait.until(ExpectedConditions
								.visibilityOfAllElementsLocatedBy(By.xpath("//*[@class=\"welcomecontent\"]")));

					} catch (Exception ee) {
						WebDriverWait waitNA = new WebDriverWait(driver, 180);
						waitNA.until(ExpectedConditions
								.visibilityOfAllElementsLocatedBy(By.xpath("//*[@class=\"welcomecontent\"]")));

					}
					Thread.sleep(10000);

					links = driver.findElements(By.tagName("a"));

					System.out.println("Total links are " + links.size());

					for (int i = 0; i < links.size(); i++) {

						WebElement ele = links.get(i);

						String linkUrl = ele.getAttribute("href");

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
					msg.append("\n\n\n" + "Broken Link test Done for " + ServerName + "\n\n\n");

					Env = storage.getProperty("Env");
					String subject = "Selenium Automation Script: " + Env + " " + ServerName + " Broken Links";
					String File = ".\\Report\\ExtentReport\\ExtentReportResults.html,.\\Report\\log\\BrokenLinks.html";

					try {
						SendEmail.sendMail("ravina.prajapati@samyak.com,parth.doshi@samyak.com,asharma@samyak.com",
								subject, msg.toString(), File);

					} catch (Exception ex) {
						logs.error(ex);
					}
				} catch (Exception e) {
					logs.info("Issue with URL==FAIL");
					msg1.append("Issue with URL==FAIL" + "\n");
					getScreenshot(driver, "URLIssue");

					Env = storage.getProperty("Env");
					String subject = "Selenium Automation Script: " + Env + ServerName + " Login";
					String File = ".//Report//Screnshots//URLIssue.png";

					try {
						SendEmail.sendMail("ravina.prajapati@samyak.com", subject, msg.toString(), File);

					} catch (Exception ex) {
						logs.error(ex);
					}

				}

			} else if (ServerName.equalsIgnoreCase("NetAgent Support Login")) {
				logs.info("=====Broken Links Test After Login=====");
				msg.append("\n\n" + "=====Broken Links Test After Login=====" + "\n\n");

				driver.get(URL);
				try {
					wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.name("loginForm")));
					String UserName = getData("URL", row, 2);
					String Password = getData("URL", row, 3);
					isElementPresent(("NASUserName_id")).clear();
					isElementPresent(("NASUserName_id")).sendKeys(UserName);
					isElementPresent(("NASPassword_id")).clear();
					isElementPresent(("NASPassword_id")).sendKeys(Password);
					Thread.sleep(2000);
					isElementPresent(("NASSignIn_id")).click();
					Thread.sleep(2000);
					isElementPresent(("NASProcess_id")).click();

					try {
						try {
							wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("l_message")));
						} catch (Exception waittt) {
							WebDriverWait waitNew = new WebDriverWait(driver, 50);
							waitNew.until(ExpectedConditions.visibilityOfElementLocated(By.id("l_message")));

						}

					} catch (Exception e) {
						System.out.println("Validation Message is not Display.\n\n");
						msg.append("Validation Message is not Display.\n\n");
						System.out.println("============================================================\n\n");
						msg.append("============================================================\n\n");
					}
					String NACourier = getData("URL", row, 4);
					wait.until(ExpectedConditions.elementToBeClickable(By.id("supinputCourier")));
					isElementPresent(("NASCourier_id")).clear();
					isElementPresent(("NASCourier_id")).sendKeys(NACourier);
					isElementPresent(("NASCourier_id")).sendKeys(Keys.TAB);
					// robot.keyPress(KeyEvent.VK_TAB);
					wait.until(ExpectedConditions.elementToBeClickable(By.id("supdrpProfile")));

					Select opt = new Select(isElementPresent(("NASProfDrp_id")));
					Thread.sleep(2000);
					opt.selectByIndex(1);
					Thread.sleep(2000);

					wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("img_profile")));
					if (isElementPresent(("NASImg_id")).isEnabled() == true) {
						System.out.println("Role icon is display.\n\n");
					} else {
						System.out.println("Role icon is not display.\n\n");
					}

					wait.until(ExpectedConditions.elementToBeClickable(By.id("btnProceed")));
					isElementPresent(("NASProcess_id")).click();
					wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("btnProceed")));
					wait.until(ExpectedConditions
							.invisibilityOfElementLocated(By.xpath("//*[@class=\"ajax-loadernew\"]")));
					try {
						wait.until(ExpectedConditions
								.visibilityOfAllElementsLocatedBy(By.xpath("//*[@class=\"welcomecontent\"]")));

					} catch (Exception ee) {
						WebDriverWait waitNA = new WebDriverWait(driver, 180);
						waitNA.until(ExpectedConditions
								.visibilityOfAllElementsLocatedBy(By.xpath("//*[@class=\"welcomecontent\"]")));

					}
					Thread.sleep(10000);

					links = driver.findElements(By.tagName("a"));

					System.out.println("Total links are " + links.size());

					for (int i = 0; i < links.size(); i++) {

						WebElement ele = links.get(i);

						String linkUrl = ele.getAttribute("href");

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
					msg.append("\n\n\n" + "Broken Link test Done for " + ServerName + "\n\n\n");

					Env = storage.getProperty("Env");
					String subject = "Selenium Automation Script: " + Env + " " + ServerName + " Broken Links";
					String File = ".\\Report\\ExtentReport\\ExtentReportResults.html,.\\Report\\log\\BrokenLinks.html";

					try {
						SendEmail.sendMail("ravina.prajapati@samyak.com,parth.doshi@samyak.com,asharma@samyak.com",
								subject, msg.toString(), File);

					} catch (Exception ex) {
						logs.error(ex);
					}
				} catch (Exception e) {
					logs.info("Issue with URL==FAIL");
					msg1.append("Issue with URL==FAIL" + "\n");
					getScreenshot(driver, "URLIssue");

					Env = storage.getProperty("Env");
					String subject = "Selenium Automation Script: " + Env + ServerName;
					String File = ".//Report//Screnshots//URLIssue.png";

					try {
						SendEmail.sendMail("ravina.prajapati@samyak.com", subject, msg.toString(), File);

					} catch (Exception ex) {
						logs.error(ex);
					}

				}

			} else if (ServerName.equalsIgnoreCase("NetShip Support Login")) {
				logs.info("=====Broken Links Test After Login=====");
				msg.append("\n\n" + "=====Broken Links Test After Login=====" + "\n\n");

				driver.get(URL);
				try {
					wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//*[@class=\"login\"]")));
					String UserName = getData("URL", row, 2);
					String Password = getData("URL", row, 3);
					isElementPresent(("NSSUserName_id")).clear();
					isElementPresent(("NSSUserName_id")).sendKeys(UserName);
					isElementPresent(("NSSPassword_id")).clear();
					isElementPresent(("NSSPassword_id")).sendKeys(Password);
					Thread.sleep(2000);

					isElementPresent(("NSSSignIn_id")).click();
					Thread.sleep(2000);
					isElementPresent(("NSSProcess_id")).click();
					Thread.sleep(2000);
					String NSCustomer = getData("URL", row, 4);

					wait.until(ExpectedConditions.elementToBeClickable(By.id("txtCustomer")));
					isElementPresent(("NSSCustomer_id")).clear();
					isElementPresent(("NSSCustomer_id")).sendKeys(NSCustomer);
					isElementPresent(("NSSCustomer_id")).sendKeys(Keys.TAB);
					// robot.keyPress(KeyEvent.VK_TAB);
					wait.until(ExpectedConditions.elementToBeClickable(By.id("supdrpIMpopunit")));

					Select opt = new Select(isElementPresent(("NSSOPTDrp_id")));
					Thread.sleep(2000);
					opt.selectByIndex(2);

					wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("img_profile")));
					if (isElementPresent(("NSSImg_id")).isEnabled() == true) {
						System.out.println("Role icon is display.\n\n");
					} else {
						System.out.println("Role icon is not display.\n\n");
					}

					isElementPresent(("NSSProcess_id")).click();
					wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("btnProceed")));
					Thread.sleep(10000);

					links = driver.findElements(By.tagName("a"));

					System.out.println("Total links are " + links.size());

					for (int i = 0; i < links.size(); i++) {

						WebElement ele = links.get(i);

						String linkUrl = ele.getAttribute("href");

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
					msg.append("\n\n\n" + "Broken Link test Done for " + ServerName + "\n\n\n");

					Env = storage.getProperty("Env");
					String subject = "Selenium Automation Script: " + Env + " " + ServerName + " Broken Links";
					String File = ".\\Report\\ExtentReport\\ExtentReportResults.html,.\\Report\\log\\BrokenLinks.html";

					try {
						SendEmail.sendMail("ravina.prajapati@samyak.com,parth.doshi@samyak.com,asharma@samyak.com",
								subject, msg.toString(), File);

					} catch (Exception ex) {
						logs.error(ex);
					}

				} catch (Exception e) {
					logs.info("Issue with URL==FAIL");
					msg1.append("Issue with URL==FAIL" + "\n");
					getScreenshot(driver, "URLIssue");

					Env = storage.getProperty("Env");
					String subject = "Selenium Automation Script: " + Env + ServerName;
					String File = ".//Report//Screnshots//URLIssue.png";

					try {
						SendEmail.sendMail("ravina.prajapati@samyak.com", subject, msg.toString(), File);

					} catch (Exception ex) {
						logs.error(ex);
					}

				}

			} else if (ServerName.equalsIgnoreCase("NetShip")) {
				logs.info("=====Broken Links Test After Login=====");
				msg.append("\n\n" + "=====Broken Links Test After Login=====" + "\n\n");

				driver.get(URL);
				try {
					wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.name("loginForm")));
					String UserName = getData("URL", row, 2);
					String Password = getData("URL", row, 3);
					isElementPresent(("NSUserName_id")).clear();
					isElementPresent(("NSUserName_id")).sendKeys(UserName);
					isElementPresent(("NSPassword_id")).clear();
					isElementPresent(("NSPassword_id")).sendKeys(Password);
					isElementPresent(("NSLogin_id")).click();
					wait.until(ExpectedConditions
							.invisibilityOfElementLocated(By.xpath("//*[@class=\"ajax-loadernew\"]")));
					Thread.sleep(10000);
					links = driver.findElements(By.tagName("a"));

					System.out.println("Total links are " + links.size());

					for (int i = 0; i < links.size(); i++) {

						WebElement ele = links.get(i);

						String linkUrl = ele.getAttribute("href");

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
					msg.append("\n\n\n" + "Broken Link test Done for " + ServerName + "\n\n\n");

					Env = storage.getProperty("Env");
					String subject = "Selenium Automation Script: " + Env + " " + ServerName + " Broken Links";
					String File = ".\\Report\\ExtentReport\\ExtentReportResults.html,.\\Report\\log\\BrokenLinks.html";

					try {
						SendEmail.sendMail("ravina.prajapati@samyak.com,parth.doshi@samyak.com,asharma@samyak.com",
								subject, msg.toString(), File);

					} catch (Exception ex) {
						logs.error(ex);
					}
				} catch (Exception e) {
					logs.info("Issue with URL==FAIL");
					msg1.append("Issue with URL==FAIL" + "\n");
					getScreenshot(driver, "URLIssue");

					Env = storage.getProperty("Env");
					String subject = "Selenium Automation Script: " + Env + ServerName + " Login";
					String File = ".//Report//Screnshots//URLIssue.png";

					try {
						SendEmail.sendMail("ravina.prajapati@samyak.com", subject, msg.toString(), File);

					} catch (Exception ex) {
						logs.error(ex);
					}

				}

			} else if (ServerName.equalsIgnoreCase("FedEXSameDay")) {
				logs.info("=====Broken Links Test After Login=====");
				msg.append("\n\n" + "=====Broken Links Test After Login=====" + "\n\n");

				driver.get(URL);
				try {
					String UserName = getData("URL", row, 2);
					String Password = getData("URL", row, 3);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Header_fdx_main_liLogin")));
					isElementPresent(("FDXloginDiv_id")).click();
					isElementPresent(("FDXUserName_id")).clear();
					isElementPresent(("FDXUserName_id")).sendKeys(UserName);
					// enter password
					isElementPresent(("FDXPassword_id")).clear();
					isElementPresent(("FDXPassword_id")).sendKeys(Password);
					Thread.sleep(2000);
					wait.until(ExpectedConditions.elementToBeClickable(By.id("Header_fdx_main_cmdMenuLogin")));
					isElementPresent(("FDXLogin_id")).click();
					System.out.println("Login done");
					wait.until(ExpectedConditions
							.visibilityOfAllElementsLocatedBy(By.xpath("//*[@class=\"fdx-o-grid\"]")));
					Thread.sleep(10000);

					links = driver.findElements(By.tagName("a"));

					System.out.println("Total links are " + links.size());

					for (int i = 0; i < links.size(); i++) {

						WebElement ele = links.get(i);

						String linkUrl = ele.getAttribute("href");

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
					msg.append("\n\n\n" + "Broken Link test Done for " + ServerName + "\n\n\n");

					Env = storage.getProperty("Env");
					String subject = "Selenium Automation Script: " + Env + " " + ServerName + " Broken Links";
					String File = ".\\Report\\ExtentReport\\ExtentReportResults.html,.\\Report\\log\\BrokenLinks.html";

					try {
						SendEmail.sendMail("ravina.prajapati@samyak.com,parth.doshi@samyak.com,asharma@samyak.com",
								subject, msg.toString(), File);

					} catch (Exception ex) {
						logs.error(ex);
					}
				} catch (Exception e) {

					try {
						isElementPresent(("FDXLogin_id")).click();
						wait.until(
								ExpectedConditions.visibilityOfElementLocated(By.id("Header_fdx_main_lblloginmmsg")));
						String ValMsg = driver.findElement(By.id("Header_fdx_main_lblloginmmsg")).getText();
						msg.append("Step4 : Application Login Successfully : FAIL, " + ValMsg + "\n");
						getScreenshot(driver, "LoginIssue");
					} catch (Exception OnLOGinBTN) {
						msg.append("Step4 : Application Login Successfully : FAIL" + "\n");
						msg.append("URL is==" + driver.getCurrentUrl());
						getScreenshot(driver, "LoginIssue");

					}

					Env = storage.getProperty("Env");
					String subject = "Selenium Automation Script: " + Env + ServerName + " Login";
					String File = ".//Report//Screnshots//URLIssue.png";

					try {
						SendEmail.sendMail("ravina.prajapati@samyak.com", subject, msg.toString(), File);

					} catch (Exception ex) {
						logs.error(ex);
					}

				}

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
