package com.trivago;

import org.testng.annotations.Test;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import com.trivago.utils.driverSetup;
import com.trivago.utils.excelUtils;

@Listeners(com.trivago.utils.extentReports.class)
public class hotelAvailability {

	public static WebDriver driver;
    		
	@BeforeSuite
    //creating web driver
	public WebDriver createDriver()
	{
		
		driver=driverSetup.getWebDriver();
		driver.manage().window().maximize()	;
		driver.get("https://www.trivago.in/"); //opening URL
		return driver;
		
	}

	//giving value to destination
	@Test(priority=1)
	public void destination() throws Exception
	{
		WebElement destination=driver.findElement(By.xpath("//input[@id='input-auto-complete']"));
		destination.click();
	    String file=(System.getProperty("user.dir")+"/src/test/resources/outputs.xlsx");
		String loc=excelUtils.getCellData(file,"Sheet1", 0, 0);     //taking the value from excel
		destination.sendKeys(loc);
		
		List<WebElement> opts= driver.findElements(By.xpath("//*[@id=\"suggestion-list\"]/ul//li//div[@data-testid=\"ssg-element\"]//mark")); 	//path for auto suggestion shown
 
		Thread.sleep(2000);
		for(WebElement opt:opts)
        {
			String text=opt.getText();
			
			if(text.equalsIgnoreCase(loc))
			{
				opt.click();
			}
		}
	}

	//selecting check in and checkout
	@Test(priority=2)
	public void checkinout()
	{
		try 
		{
			
		Thread.sleep(1000);
		
		WebElement checkinb=driver.findElement(By.xpath("//button[@data-testid='search-form-calendar-checkin']"));
		checkinb.click();
		
		Thread.sleep(1000);
		LocalDate today=LocalDate.now();												//taking today date
		LocalDate nextweek=today.plusWeeks(1);											//next week day from today
		
		DateTimeFormatter format=DateTimeFormatter.ofPattern("dd MMMM yyyy");			//formatting the date
		String nxtweekdate=nextweek.format(format);		
		int nxtweekday=nextweek.getDayOfMonth();										//getting day from date
		
		String monyr=nxtweekdate.substring(3); 	
		String date=Integer.toString(nxtweekday); 
		

		while(true)
		{
			String monyrac=driver.findElement(By.xpath("(//div[@class='text-center']/h3)[1]")).getText();    //path for month and year
			
			if(monyrac.equals(monyr))
			{
				break;
			}
			Thread.sleep(1000);
			driver.findElement(By.xpath("//button[@data-testid=\"calendar-button-next\"]")).click();
		}
		List<WebElement> alldates=driver.findElements(By.xpath("//div[@class='text-center'][1]//div[2]/button/time"));  ///path for all dates
		for(WebElement dt:alldates)
		{
			
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
			
			if(dt.getText().equals(date))
			{
				dt.click();
				break;
			}
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	//adult value selection
	@Test(priority=3)
	public void adult() throws Exception
	{
		Thread.sleep(1000);
		WebElement guesroom=driver.findElement(By.xpath("//button[@data-testid=\"search-form-guest-selector\"]"));		//path for guest button
		guesroom.click();
		Thread.sleep(1000);
		WebElement adult=driver.findElement(By.xpath("//input[@data-testid='adults-amount']"));		//path for adult text box
		String val=adult.getAttribute("value");
		
		int vals=Integer.parseInt(val);
		while(true)
		{
			while(vals>=2)
			{ 
				if(vals!=1)
				{
			
				WebElement minus=driver.findElement(By.xpath(" //button[@data-testid='adults-amount-minus-button']"));		//path for minus button
				minus.click();
				vals--;
				
				}
			}
			break;
		}
	}
	
	//rooms values
	@Test(priority=4)
	public void rooms()
	{
		String rooms=driver.findElement(By.xpath("//input[@data-testid='rooms-amount']")).getAttribute("value");		//path for rooms 
		int roomval=Integer.parseInt(rooms);
		if(roomval==1)
		{
		  
		}
	}
	
	//apply button
	@Test(priority=5)
	public void apply() throws Exception
	{
		WebElement app=driver.findElement(By.xpath("//button[@data-testid='guest-selector-apply']"));
		app.click();
		Thread.sleep(2000);
	}
	
	//search button
	public void search()
	{
		try {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		WebElement search=driver.findElement(By.xpath("//button[@data-testid='search-button']"));
		search.click();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	//selecting by rating only
	@Test(priority=6)
	public void sortBy() 
	{   
		WebElement sortbypath=driver.findElement(By.id("sorting-selector"));
		Select  sortbyele=new Select(sortbypath);
		sortbyele.selectByVisibleText("Rating only");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
	}
	
	
	public boolean isRatingDescending(List<String> rating,int len)    //verifying ratings is in descending or not
	{
		
		for(int i=0;i<(len-1);i++)
		{
			Double currentr=Double.parseDouble(rating.get(i));
			Double nextr=Double.parseDouble(rating.get(i+1));
			if(currentr<nextr)
			{
				return false;
			}
		}
		return true;
	}
	
    //getting the prices of all hotels and  verifying the ratings in descending or not
	@Test(priority=8)
	public void pricesandratings()
	{
		try 
		{
			
		List<String> prices=new ArrayList<String>();				//list for all prices
		List<String> allRatings=new ArrayList<String>();            //list for all ratings
		
		WebElement pageele=driver.findElement(By.xpath("//nav[@class='self-center']//li[5]//button"));			//path for page
		int pages=Integer.parseInt(pageele.getText());
        
		for(int i=1;i<=pages;i++)
		{
			//path for list of prices of all hotels
			List<WebElement> priceele=driver.findElements(By.xpath("//li[@data-testid='accommodation-list-element']//div[@data-testid='clickout-area']//div[@itemprop=\"priceSpecification\"]//p[@data-testid=\"recommended-price\"]"));
			//path for ratings of all hotels
			List<WebElement> rating=driver.findElements(By.xpath("//button[@data-testid='rating-section']//span[@class='mt-px truncate']/strong/span"));
		
		
			for(WebElement pr:priceele)       
			{
				prices.add(pr.getText());         //adding prices elements into list
			}
			
			for(WebElement pr:rating)       
			{
				allRatings.add(pr.getText());         //adding ratings of hotels into list.
			}
			
			//check for next page and navigate
			if(i==(pages))
			{
				break;
			}
			else
			{
				if(driver.findElement(By.xpath("//button[@data-testid='next-result-page']")).isDisplayed())
				{
					Thread.sleep(1000);
					driver.findElement(By.xpath("//button[@data-testid='next-result-page']")).click();
					Thread.sleep(2000);
				}
			}
			
		}
		
		//printing prices of all hotels
		String file=(System.getProperty("user.dir")+"/src/test/resources/outputs.xlsx");
		int r=1;
		for(String i:prices)
		{
			excelUtils.setCellData(file, "Sheet1", r, 2, i);
			r++;
		}
		
		//verifying whether ratings are in descending or not
		if(isRatingDescending(allRatings,allRatings.size()))
		{
			System.out.println("ratings are in descending order");
		}
		else
		{
			System.out.println("ratings are not in descending order");
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	//verifying whether the first five hotels are in destination are not
	@Test(priority=7)
	public void firstfive() throws Exception
	{
		Thread.sleep(5000);
		for(int i=1;i<=5;i++)
		{
			WebElement buttons=driver.findElement(By.xpath("(//button[@data-testid='distance-label-section'])["+i+"]"));
			buttons.click();
			
			String city=driver.findElement(By.xpath("//div[@itemprop=\"address\"]//span[@itemprop=\"addressLocaspanty\"]")).getText();
			if(city.contains("Mumbai"))
			{
				System.out.println("hotel "+i+ " is in " + "Mumbai");
			}
		}

	}
	
	//closing the browser
	@AfterSuite
	public void closeBrowser()
	{
		driver.quit();
	}
	
    //main file
//	public static void main(String[] args) throws Exception {
//		
//		hotelAvailability ha =new hotelAvailability();
//		ha.createDriver();
//		ha.destination();
//		ha.checkinout();
//		ha.adult();
//		ha.rooms();
//		ha.apply();
//		ha.sortBy();
//		ha.firstfive();
//		ha.pricesandratings();
//		ha.closeBrowser();
//	}

}
