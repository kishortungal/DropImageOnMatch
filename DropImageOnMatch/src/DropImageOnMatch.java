import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import sun.misc.BASE64Decoder;
 


public class DropImageOnMatch {
	static WebDriver driver = null;
	static WebElement source = null;
	static WebElement destination = null;
	static WebDriverWait wait =null;
	
	public static void main(String[] args) throws InterruptedException {		
	    

	    	try {
	    		System.setProperty("webdriver.chrome.driver","chromedriver path");
	    		ChromeOptions options = new ChromeOptions().setBinary("Browser exe path");
	    		driver = new ChromeDriver(options);
	    		wait = new WebDriverWait(driver, 100);
	    		
	    		Logger logger;
	    		String logFileName = "Log file path"; //LogFile if you want to log
	    		logger = Logger.getLogger("MyLog");  
	    	    FileHandler fh;
	    	    
				driver.get("your URL");
				driver.manage().window().maximize();
			     
			    while (true) {
			    	
			    	source = driver.findElement(By.xpath("Source image path"));	
					destination = driver.findElement(By.xpath("Destination Image path"));
					
					downloadImage("DestinationImage");
					downloadImage("SourceImage");
					
					Point destinationLocaion = getDestinationPoint();
					
					Point sourcePoint = source.getLocation();
					Point destinationPoint = destination.getLocation();		
					
					int x = (destinationLocaion.getX() + destinationPoint.getX());
			        int y = (destinationLocaion.getY() + destinationPoint.getY());
			        
					Actions act=new Actions(driver);
					act.clickAndHold(source).pause(2000).moveToElement(destination, destinationLocaion.getX(), destinationLocaion.y).release().build().perform();
										
			    }
			}catch (Exception e) {
		        e.printStackTrace();
		        driver.quit();
			}
	    	
	}	
	
	public static Point getDestinationPoint() {
		        
		        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		        Imgcodecs imageCodecs = new Imgcodecs();
		        Mat DestinationImage=null;
		        Mat SourceImage=null;
		        String filePath="Image Folder Path";
		        //Load image file
		        DestinationImage=imageCodecs.imread(filePath+"DestinationImage.png" );
		        SourceImage=imageCodecs.imread(filePath+"SourceImage.png");
		        
		        if (DestinationImage.empty()) {
		            System.out.println("Can't read Destination images");
		        }
		        
		        if (SourceImage.empty()) {
		            System.out.println("Can't read Source images");
		        }
		    
		        Mat outputImage=new Mat();    
		        int machMethod=Imgproc.TM_CCOEFF;
		        
		        Imgproc.matchTemplate(DestinationImage, SourceImage, outputImage, machMethod);
		 
		    
		        MinMaxLocResult mmr = Core.minMaxLoc(outputImage);
		        org.opencv.core.Point matchLoc=mmr.maxLoc;
		        
		        double x = (matchLoc.x+matchLoc.x + SourceImage.cols())/2;
		        double y = ((matchLoc.y+matchLoc.y + SourceImage.rows())/2)+1;
		        
		        return new Point((int) Math.round(x),(int) Math.round(y));
		    }
	
	public static void downloadImage(String ImageType) throws IOException {
		String logoSRC="";
		String downloadFileName="";
		
		if (ImageType.equals("DestinationImage")) {
			logoSRC = destination.getAttribute("src");
			downloadFileName = "Path to store DestinationImage.png";
		}
		else if (ImageType.equals("SourceImage")) {
			logoSRC = source.getAttribute("src");
			downloadFileName = "Path to store SourceImage.png";
		}
	     
		String base64Image = logoSRC.split(",")[1];
	     
	     BASE64Decoder decoder = new BASE64Decoder();
	     byte[] decodedBytes = decoder.decodeBuffer(base64Image);
	     
	     
        // buffered image from the decoded bytes 
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(decodedBytes));
        
        File f = new File(downloadFileName);	 
        // write the image
        ImageIO.write(image, "png", f);
	}
	
	
}