package webdriverrr;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
//import com.mysql.jdbc.PreparedStatement;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;

import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;

public class prg1 {

	public String tokenvalue = null;

	void reachtatoc(WebDriver driver2) {
		driver2.get("http://10.0.1.86");
		List<WebElement> links1 = driver2.findElements(By.cssSelector("html>body>div>ul>li>ul>li>a"));
		links1.get(1).click();

		List<WebElement> links2 = driver2.findElements(By.cssSelector(".page>a"));
		links2.get(1).click();

	}

	void menu(WebDriver driver2) {
		driver2.get("http://10.0.1.86/tatoc/advanced/hover/menu");
		Actions actions = new Actions(driver2);
		WebElement moveonmenu = driver2.findElement(By.cssSelector(".menutitle"));
		actions.moveToElement(moveonmenu);
		moveonmenu.click();
		List<WebElement> options = driver2.findElements(By.cssSelector(".menuitem"));
		options.get(3).click();
		actions.perform();

	}

	void sql(WebDriver driver2) throws ClassNotFoundException, SQLException {

		driver2.get("http://10.0.1.86/tatoc/advanced/query/gate");
		String dburl = "jdbc:mysql://10.0.1.86/tatoc";
		String username = "tatocuser";
		String password = "tatoc01";
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = (Connection) DriverManager.getConnection(dburl, username, password);
		System.out.println("connected");

		String text = driver2.findElement(By.cssSelector("#symboldisplay")).getText();
		Statement stm = con.createStatement();
		ResultSet rs = stm.executeQuery("select id from identity where symbol= '" + text + "'");
		int id = 0;
		while (rs.next()) {
			id = rs.getInt(1);
			System.out.print(id);
		}
		rs.close();
		ResultSet rs1 = stm.executeQuery("select name,passkey from credentials where id='" + id + "'");
		while (rs1.next()) {
			String name = rs1.getString(1);
			driver2.findElement(By.cssSelector("#name")).sendKeys(name);
			System.out.println(name);
			String pwd = rs1.getString(2);
			driver2.findElement(By.cssSelector("#passkey")).sendKeys(pwd);
			System.out.print(pwd);

		}
		rs1.close();
		con.close();
		driver2.findElement(By.cssSelector("#submit")).click();

	}

	public void video(WebDriver driver2) {

		JavascriptExecutor js = (JavascriptExecutor) driver2;

		js.executeScript("player.play()");

		System.out.println("runn");
		String getTotalTime = (String) js.executeScript("return player.getTotalTime()");

		long Time = Long.parseLong(getTotalTime);
		try {

			Thread.sleep(Time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<WebElement> links2 = driver2.findElements(By.cssSelector(".page>a"));
		links2.get(0).click();

		System.out.println("hey2");

	}

	/*public void restful(WebDriver driver2) throws MalformedURLException {
		driver2.get("http://10.0.1.86/tatoc/advanced/rest");
	/*	JavascriptExecutor js = (JavascriptExecutor) driver2;
		WebElement sessionId = driver2.findElement(By.cssSelector("#session_id"));
		String sessionid = sessionId.getText();
		String id1 = sessionid.substring(12, 16);
		System.out.println(sessionid);

		List<WebElement> links2 = driver2.findElements(By.cssSelector(".page>ul>li"));
		String url2 = links2.get(1).getText();
		String url1 = url2.substring(36, 87);
		String finurl = url1 + id1;

		System.out.println(finurl);

		// get method

		/*

		try {
			URL url = new URL(finurl);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);

				tokenvalue = output.substring(10, 42);
				System.out.println(tokenvalue);

			}

			// String tokenvalue = output.substring(9, 41);
			// System.out.println(tokenvalue);
			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
*/
		/* ***************************************************************************** */
		/* post method
		links2 = driver2.findElements(By.cssSelector(".page>ul>li"));
		String url3 = links2.get(2).getText();
		String url4 = url3.substring(42, 87);
		System.out.println(url4);

		try {

			// URL url = new
			// URL("http://10.0.1.86/tatoc/advanced/rest/service/register");
			URL url = new URL(url4);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);

			String urlParameters = "id=" + id1 + "&signature=" + tokenvalue + "&allow_access=1";

			System.out.println("############### " + conn.getURL());

			conn.setDoOutput(true);
			conn.setInstanceFollowRedirects(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("charset", "utf-8");
			byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
			int postDataLength = postData.length;
			conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
			conn.setUseCaches(false);

			try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
				wr.write(postData);
			}

			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			int responseCode = conn.getResponseCode();
			conn.disconnect();
			driver2.findElement(By.cssSelector(".page a")).click();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		*/
	
		/*
		 String sessid = driver2.findElement(By.id("session_id")).getText();
	        sessid = sessid.substring(12,sessid.length());
	        String Resturl = "http://10.0.1.86/tatoc/advanced/rest/service/token/"+sessid;

	        URL url = new URL(Resturl);
	        HttpURLConnection conn;
			try {
				conn = (HttpURLConnection) url.openConnection();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        try {
				conn.setRequestMethod("GET");
			} catch (ProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        conn.setRequestProperty("Accept", "application/json");

	        if (conn.getResponseCode() != 200) {
	            throw new RuntimeException("Failed : HTTP error code : "
	                    + conn.getResponseCode());
	        }

	        BufferedReader in = new BufferedReader(
	                new InputStreamReader(conn.getInputStream()));
	        String inputLine;
	        StringBuffer response = new StringBuffer();

	        while ((inputLine = in.readLine()) != null) {
	            response.append(inputLine);
	        }
	        in.close();
	        String res=new String(response);
	        
	        JSONObject obj=new JSONObject(res);
	        res=(String) obj.get("token");
	        
	        URL url1 = new URL("http://10.0.1.86/tatoc/advanced/rest/service/register");
	        HttpURLConnection conn1 = (HttpURLConnection) url1.openConnection();
	        

	        conn1.setRequestMethod("POST");
	        
	        conn1.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

	        String urlParameters = "id="+sessid+"&signature="+res+"&allow_access=1";
	        
	        conn1.setDoOutput(true);
	        DataOutputStream wr = new DataOutputStream(conn1.getOutputStream());
	        wr.writeBytes(urlParameters);
	        wr.flush();
	        wr.close();

	        int responseCode = conn1.getResponseCode();
	        
	        conn1.disconnect();
	        driver2.findElement(By.cssSelector(".page a")).click();
		*/

	public static void main(String args[]) throws ClassNotFoundException, SQLException {
		System.out.println("Select your choice ");
		System.out.println(" Press 1 for - Firing Tatoc in FireFox");
		System.out.println(" Press 2 for - Firing Tatoc in Chrome");

		Scanner input = new Scanner(System.in);
		int choice;
		System.out.println("Enter an integer");
		choice = input.nextInt();

		switch (choice) {
		case 1:
			File binaryPath = new File("/home/aakritisrivastava/Desktop/firefox (2)/firefox");
			FirefoxBinary ffbinary = new FirefoxBinary(binaryPath);
			FirefoxProfile ffProfile = new FirefoxProfile();
			WebDriver driver2 = new FirefoxDriver(ffbinary, ffProfile);
			prg1 obj = new prg1();
			 obj.reachtatoc(driver2);
			 obj.menu(driver2);
			 obj.sql(driver2);
			 obj.video(driver2);
			/*try {
				obj.restful(driver2);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			input.close();
			break;

		case 2:
			System.setProperty("webdriver.chrome.driver", "/home/aakritisrivastava/Desktop/chromedriver");
			WebDriver driver = new ChromeDriver();
			prg1 obj2 = new prg1();
			// obj2.reachtatoc(driver);
			// obj2.menu(driver);
			obj2.sql(driver);

			input.close();

			break;

		default:
			choice = 1;
			System.out.println("By default it will run in firefox");
			input.close();
			break;
		}

	}

}
