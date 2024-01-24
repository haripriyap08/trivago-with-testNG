//driver setup for the project
package com.trivago.utils;

import java.util.Scanner;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

//driver setup for given browsers( chrome ,edge)
public class driverSetup {

	public static WebDriver driver;
	public static WebDriver getWebDriver()
	{
	Scanner s=new Scanner(System.in);
	System.out.println("Enter the Browser name");
	String browser=s.next();
	s.close();
	if(browser.equalsIgnoreCase("chrome"))
	{
		WebDriverManager.chromedriver().setup(); // Setting up ChromeDriver using WebDriverManager
		driver=new ChromeDriver(); // Creating a new instance of ChromeDriver
		
	}
	else if(browser.equalsIgnoreCase("firefox"))
	{
		WebDriverManager.firefoxdriver().setup(); 
		driver=new FirefoxDriver(); 
		
	}
	else if(browser.equalsIgnoreCase("edge")) 
	{
		WebDriverManager.edgedriver().setup(); // Setting up EdgeDriver using WebDriverManager
		driver=new EdgeDriver(); 
		
	}
	return driver;
	
	}
 
}
