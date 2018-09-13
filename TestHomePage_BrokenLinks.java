package com.rdz.testcases;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.rdz.base.BasePage;
import com.rdz.page.HomePage;

import java.net.HttpURLConnection;
import java.net.URL;

public class TestHomePage extends BasePage {

	public HomePage hp;
	
	@Test(description = "To Verify All Links on Home Page are Not Broken")
	public void TestBrokenLinks() throws IOException {


		List<WebElement> Links = findAllLinks(getDriver());
		
		 System.out.println("Total number of elements found " + Links.size());
		 
		 for( WebElement element : Links){
			 
		    	try

		    	{

			        System.out.println("URL: " + element.getAttribute("href")+ " returned " + isLinkBroken(new URL(element.getAttribute("href"))));


		    	}

		    	catch(Exception exp)

		    	{

		    		System.out.println("At " + element.getAttribute("innerHTML") + " Exception occured -&gt; " + exp.getMessage());	    		

		    	}

		    }


}
	
	
	  public static List<WebElement> findAllLinks(WebDriver driver)

	  {

		  List <WebElement>elementList = new ArrayList<WebElement>();

		  elementList = driver.findElements(By.tagName("a"));

		  elementList.addAll(driver.findElements(By.tagName("img")));

		  List<WebElement> finalList = new ArrayList<WebElement>(); ;

		  for (WebElement element : elementList)

		  {

			  if(element.getAttribute("href") != null)

			  {

				  finalList.add(element);
				  if(!element.getText().isEmpty()){
				  System.out.println(element.getText());
				  }

			  }		  

		  }	
		  
		  System.out.println("Total No. of Links are:    "+finalList.size());

		  return finalList;

	  }
	  
	  public static String isLinkBroken(URL url) throws Exception
	  
		{
	 
			url = new URL(config.getProperty("url"));
	 
			String response = "";
	 
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	 
			try
	 
			{
	 
			    connection.connect();
	 
			     response = connection.getResponseMessage();	        
	 
			    connection.disconnect();
	 
			    return response;
	 
			}
	 
			catch(Exception exp)
	 
			{
				Assert.assertTrue(true,exp.getMessage().toString());
				return exp.getMessage();
				
				
	 
			}  				
	 
		}
}
