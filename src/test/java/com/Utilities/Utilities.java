package com.Utilities;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.testng.annotations.Optional;

import com.aventstack.extentreports.ExtentTest;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;


public class Utilities  extends BaseObjs<Utilities>{
	

	public Utilities(WebDriver driver, Logger log, ExtentTest test) {
		super(driver, log, test);		
	}
	
	public Utilities(Logger log, ExtentTest test) {
		super(log, test);		
	}
	
	/**
	* Random UUID/Number in String format
	*/
	public String getRandomNo() {
		String uuid = UUID.randomUUID().toString();
		
		return uuid;
	}
	
	/**
	* 1-4 digit Random Number in String format
	*/
	public String get_RandomNo() {
		Random random =  new Random();
		String randomNum = String.valueOf(random.nextInt(10000));
		
		return randomNum;
	}
	
	public String get_BuildNo() {
		
		String buildNumber = "";
		try {
			buildNumber = System.getenv("BUILD_NUMBER");
		} catch (Exception e) {
			// ignore
		}
		
		return buildNumber;
	}
	
	/**
	* Random 16 digit Number in String format
	*/
	public String createRandomVoucherNumber(){
	   Random rand = new Random();
	   String randomNumber = "";
	   
	   for(int i=0;i<2;i++) {
		   int num = rand.nextInt(9000000) + 10000000;
		   randomNumber = randomNumber + String.valueOf(num);
	   }
	   return randomNumber;
	}
	
	/**
	* Verifies if String contains other String
	*/
//	public void AssertContains(String Actual, String Expected) {
//		
//		if(Actual.trim().contains(Expected.trim()) || Expected.trim().contains(Actual.trim())) {
//			logmessage(Status.INFO, "Verification Success : " + Actual +" contains " + Expected);
//		}else {
//			logmessage(Status.FAIL, "Verification fail : " + Actual +" not contain " + Expected);
//			Assert.fail("Verification fail : " + Actual +" not contain " + Expected);
//		}
//	}

	/**
	* type each char of String in text box
	*/
	public void type_each_char(WebElement element, String Value) {
	     for (int i = 0; i < Value.length(); i++){
	        char c = Value.charAt(i);
	        String s = new StringBuilder().append(c).toString();
	        element.sendKeys(s);
	    }  
	}
	
	/**
	* scroll to end of the page Ios by direction: down, up, left, right
	*/
	public void Scroll_to_end_Ios(String direction) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		HashMap<String, String> scrollObject = new HashMap<String, String>();
		
		scrollObject.clear();
		scrollObject.put("direction", direction);

		js.executeScript("mobile: scroll", scrollObject);
	}
	
	/**
	* scroll to end of the page Ios by direction: down, up, left, right
	*/
	public void Scroll_Ios(String direction, WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		HashMap<String, String> scrollObject = new HashMap<String, String>();
		
		scrollObject.clear();
		scrollObject.put("direction", direction);
		for(int i=0;i<5;i++) {
			
			if(element.getAttribute("visible").equals("true")) {
				break;
			}
			
			js.executeScript("mobile: scroll", scrollObject);
		}
	}
	
	/**
	* scroll down to end of the page Android 
	*/
//	public void Scroll_down_to_end_Android() {
//		TouchAction touchAction=new TouchAction((PerformsTouchActions) driver);
//		Dimension size = driver.manage().window().getSize();
//		
//		int y_start=(int)(size.height*0.80);
//        int y_end=(int)(size.height*0.02);
//        int x=size.width/2;
//	    
//	    try {
//	    	for(int i= 0; i<2;i++) {
//		    	touchAction.longPress(ElementOption.point(x, y_start))
//	 			.moveTo(ElementOption.point(x, y_end) )
//	 			.release()
//	 			.perform();
//				wait(1);
//	    	}   
//	    } catch (Exception e) {
//	      log.info("Scroll Failed in Android!");
//	    }
//	}
	
	/**
	* scroll down to find object in Ios
	*/
	public void Scroll_down_to_findIos(@Optional WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		HashMap<String, String> scrollObject = new HashMap<String, String>();

		scrollObject.clear();
		scrollObject.put("direction", "down");
		scrollObject.put("element", ((RemoteWebElement) element).getId());
		scrollObject.put("toVisible", "true");
		scrollObject.put("predicateString", "value == '"+element.getText() +"'");
		
		for(int i=0;i<10;i++) {
			if(element.getAttribute("visible").contains("true")) {
				log.info(element.getText() + " is visible : " + element.getAttribute("visible"));
				break;
			}
			js.executeScript("mobile: scroll", scrollObject);
		}
	}
	
	/**
	* click 'Done' button on keyboard to close in Ios
	*/
	public void click_done_btn_IOS() {
		By done_btn = By.xpath("//XCUIElementTypeButton[@name='Done'] | //XCUIElementTypeButton[@name='OK']");
		try {
			driver.findElement(done_btn).click();
		} catch (Exception e) {
			log.warn("ios click 'done' failed!!!");
		}
	}
	
	/**
	* scroll down 2X times to find object in Ios
	*/
	public void Scroll2X_down_to_findIos(@Optional WebElement element) {

		JavascriptExecutor js = (JavascriptExecutor) driver;
		HashMap<String, String> scrollObject = new HashMap<String, String>();

		scrollObject.clear();
		scrollObject.put("direction", "down");
		scrollObject.put("element", ((RemoteWebElement) element).getId());
		scrollObject.put("toVisible", "true");
		
		scrollObject.put("predicateString", "value == '"+element.getText() +"'");
		
		js.executeScript("mobile: scroll", scrollObject);
		
		for(int i=0;i<5;i++) {
			log.info("scrolling down for : " + element.getText());
			js.executeScript("mobile: scroll", scrollObject);
			
			if(element.getAttribute("visible").contains("true")) {
				log.info(element.getText() + " is visible : " + element.getAttribute("visible"));
				break;
			}
		}
	}
	
	/**
	* scroll down 3x times to find object in Ios
	*/
	public void Scroll3X_down_to_findIos(@Optional WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		HashMap<String, String> scrollObject = new HashMap<String, String>();

		scrollObject.clear();
		scrollObject.put("direction", "down");
		scrollObject.put("element", ((RemoteWebElement) element).getId());
		scrollObject.put("toVisible", "true");
		
		scrollObject.put("predicateString", "value == '"+element.getText() +"'");
		
		js.executeScript("mobile: scroll", scrollObject);
		js.executeScript("mobile: scroll", scrollObject);
		
		for(int i=0;i<10;i++) {
			log.info("scrolling down for : " + element.getText());
			js.executeScript("mobile: scroll", scrollObject);
			
			if(element.getAttribute("visible").contains("true")) {
				log.info(element.getText() + " is visible : " + element.getAttribute("visible"));
				break;
			}
		}
	}

	/**
	* Scroll Android elements within top 10 elements *Works with mvm only
	*/
//	public void Scroll_down_to_find_Android(@Optional WebElement element) {
//		By support_Mobility_tiles = By.xpath("//android.widget.TextView[contains(@resource-id, 'id/tvTitle')]");
//		List<WebElement> elements = driver.findElements(support_Mobility_tiles);
//		
//		TouchAction touchAction=new TouchAction((PerformsTouchActions) driver);
//	    Dimension size=driver.manage().window().getSize();
//	    
//	    int y_start=(int)(size.height*0.20);
//	    int y_end=(int)(size.height*0.01);
//	    int x_pointer=size.width/2;
//		
//		try {			
//			String x = element.getText();
//			log.info("Scrolling for : " + x);
//
//			outerloop:
//			for(int i=0;i<5;i++) {
//				elements = null;
//				sleep(2);
//				elements = driver.findElements(support_Mobility_tiles);
//				try {
//					for(int j=0;j< elements.size() -2;j++) {
//						sleep(1);
//						if( elements.get(j).equals(element) == true ||   elements.get(j).getText().contains(x) == true || x.contains(elements.get(j).getText()) == true || elements.get(j).getText().equals(x) == true) {
//
//							sleep(2);
//							break outerloop;	
//						}
//					}
//				} catch (Exception e) {
//					log.info(x + " - Not Found! scrolling down");
//					
//					touchAction.longPress(ElementOption.point(x_pointer, y_start))
//	     			.moveTo(ElementOption.point(x_pointer, y_end) )
//	     			.release()
//	     			.perform();
//					sleep(1);
//				}
//				log.info(x + " - Not Found! ~ scrolling down");
//				
//				touchAction.longPress(ElementOption.point(x_pointer, y_start))
//     			.moveTo(ElementOption.point(x_pointer, y_end) )
//     			.release()
//     			.perform();
//				
//				sleep(2);
//			}
//		} catch (Exception e) {
//			log.info("scrolling failed!");
//		}	
//	}
	
	/**
	* move test-data folder from shared to local
	*/
	public void moveFolderFromLocal(String copyFrom, String copyTo) {
        String hostname = "127.0.0.1"; //hostname
        String username = System.getProperty("user.name");//"dcxdevmac015"; //<username>
        
        log.info("username : "+ username);
        String password = "Bell1234";
        
//        String copyFrom = "/Users/Shared/Jenkins/PC/app-soak-release.apk";//file_path_in_remote_linux_server
//        String copyTo = "/Volumes/BQAT_AUTOMATION/PC/app-soak-release.apk"; 
        
//        String copyTo = "/Users/Shared/Jenkins/PC/app-soak-release.apk";//file_path_in_remote_linux_server
//        String copyFrom = "/Volumes/BQAT_AUTOMATION/PC/app-soak-release.apk"; 
        
        
        JSch jsch = new JSch();
        Session session = null;
        log.info("Trying to connect.....");
        try {
            session = jsch.getSession(username, hostname, 22);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(password);
            session.connect(); 
            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            
//            sftpChannel.get(copyFrom, copyTo);
            
            try {
//				lsFolderCopy("/Volumes/BQAT_AUTOMATION/PC", "/Users/Shared/Jenkins/", sftpChannel);
//				lsFolderCopy("/Volumes/BQAT_AUTOMATION/MVM", "/Users/Shared/Jenkins/", sftpChannel);
            	lsFolderCopy(copyFrom, copyTo, sftpChannel);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            
            sftpChannel.exit();
            session.disconnect();
            
            log.info("Done !!");
        } catch (JSchException e) {
            e.printStackTrace();  
        } catch (SftpException e) {
            e.printStackTrace();
        }
        
    }
    
    private void lsFolderCopy(String sourcePath, String destPath,
		            ChannelSftp sftpChannel) throws SftpException,   FileNotFoundException {
    	
		File localFile = new File(sourcePath);
		
		if(localFile.isFile()){
		    //copy if it is a file
		    sftpChannel.cd(destPath);
		
		    if(!localFile.getName().startsWith("."))
		    sftpChannel.put(new FileInputStream(localFile), localFile.getName(),ChannelSftp.OVERWRITE);
		}   
		else{
		    System.out.println("inside else "+localFile.getName());
		    File[] files = localFile.listFiles();
		
		    if(files!=null && files.length > 0 && !localFile.getName().startsWith(".")){
		
		        sftpChannel.cd(destPath);
		        SftpATTRS  attrs = null;
		
			    //check if the directory is already existing
			    try {
			        attrs = sftpChannel.stat(destPath+"/"+localFile.getName());
			    } catch (Exception e) {
			    	log.info(destPath+"/"+localFile.getName()+" not found");
			    }
			
			    //else create a directory   
			    if (attrs != null) {
			        log.info("Directory exists IsDir="+attrs.isDir());
			    } else {
			    	log.info("Creating dir "+localFile.getName());
			        sftpChannel.mkdir(localFile.getName());
			    }
			
			    //System.out.println("length " + files.length);
			
			     for(int i =0;i<files.length;i++) {
			         lsFolderCopy(files[i].getAbsolutePath(),destPath+"/"+localFile.getName(),sftpChannel);
			     }
		    }
		}
    }
    
    /*
     * reads voucher numbers
     */
//    public String getVoucherNumber() throws IOException {
//    	
//    	String voucherFile = "/Users/Shared/Jenkins/NativeApp/vouchersLucky.txt";
//        File f = new File(voucherFile);
//
//        List<String> lines = FileUtils.readLines(f, "UTF-8");
//         
//        String VoucherNumber = "";
//        for (String line : lines) {
//        	VoucherNumber = line;  
//        }
// 		try{
// 			RandomAccessFile raf = new RandomAccessFile(voucherFile, "rw");
//            long length = raf.length();
//            //supposing that last line is of 16+1
//            raf.setLength(length - 17);
//            raf.close();
//        }catch(Exception ex){
//        	log.error(ex.getLocalizedMessage());
//        }
//		return VoucherNumber;
//    }
    
    public String getVoucherNumber(String voucherFile) {
    	
    	String VoucherNumber = "1111111111111111";
             
    	try{
    		File f = new File(voucherFile);

        	List<String> lines = FileUtils.readLines(f, "UTF-8");
     
        	
        	for (String line : lines) {
        		VoucherNumber = line;  
        	}
        	
    		RandomAccessFile raf = new RandomAccessFile(voucherFile, "rw");
    		long length = raf.length();
          
    		//supposing that last line is of 16+1
    		raf.setLength(length - 17);
    		raf.close();
    	}catch(Exception ex){
            log.error("voucher file not found!!!");
        }
		return VoucherNumber;
    }

}
