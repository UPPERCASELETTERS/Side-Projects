import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

public class WebClientTest {
	
	class GasPrice {
		String gasPrice = "";
		String gasCity = "";
		String gasAddress = "";
		String lastUpdated = "";
		
		public String getGasPrice() {
			return gasPrice;
		}
		public void setGasPrice(String gasPrice) {
			this.gasPrice = gasPrice;
		}
		public String getGasCity() {
			return gasCity;
		}
		public void setGasCity(String gasCity) {
			this.gasCity = gasCity;
		}
		public String getGasAddress() {
			return gasAddress;
		}
		public void setGasAddress(String gasAddress) {
			this.gasAddress = gasAddress;
		}
		public String getLastUpdated() {
			return lastUpdated;
		}
		public void setLastUpdated(String lastUpdated) {
			this.lastUpdated = lastUpdated;
		}
		
		
	}
	
	private static final Logger log = Logger.getLogger(WebClientTest.class.getName());
	
	static {
		org.apache.log4j.BasicConfigurator.configure();
		LogManager.getRootLogger().setLevel(Level.INFO);
		log.setLevel(Level.INFO);
	}
	
	public static void main(String[] strings) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		
		System.out.println("Hello");
		
		try(final WebClient webClient = new WebClient() ){
			
			
			WebClientOptions options = webClient.getOptions();
			
			options.setJavaScriptEnabled(true);
			options.setThrowExceptionOnFailingStatusCode(false);
			options.setThrowExceptionOnScriptError(false);
			
			HtmlPage page = (HtmlPage) webClient.getPage("https://gasbuddy.com");
			if (page != null) {

				HtmlInput searchInput = (HtmlInput) page.getElementByName("search");
				if (searchInput != null) {
					searchInput.setValueAttribute("Independence");
					HtmlButton findGasButton = (HtmlButton) page.getFirstByXPath("//button[text() = 'Find Gas']");
					if (findGasButton != null) {
						page = findGasButton.click();
						if (page != null) {
							//System.out.println(page.getWebResponse().getContentAsString());
							HtmlTable resultsTable = (HtmlTable) page.getFirstByXPath("//table");
						
							for(HtmlTableRow tableRow : resultsTable.getRows()) {
								List<HtmlTableCell> cells = tableRow.getCells();
								int count = 0;
								String price = "";
								String name = "";
								String address = "";
								String city = "";
								String time = "";
								String index = "";
								for(HtmlTableCell cell : cells) {
									count++;
									
						
									List<String> text = new ArrayList<>();
									text.add(cell.getTextContent());
									
									if(count == 1) {
										price = cell.getTextContent().trim();
									}
									if(count == 2) {										
										name = cell.getTextContent().trim();
										int period = name.indexOf(".");
										name = name.substring(0, period);
										name = name.trim();
										
										address = cell.getTextContent().trim();
										int para = address.lastIndexOf(")")+1;
										
										address = address.substring(para, address.length());
										address = address.trim();
										int drop = address.indexOf("\n");
										address = address.substring(0, drop);
										address = address.replaceAll(" +", " ");
									}
									if(count == 3) {
										city = cell.getTextContent().trim();
									}
									if(count == 4) {
										time = cell.getTextContent().trim();
										time = time.substring(0, 6);
									}
								}
								System.out.println("\nprice:" + price + "\nname:" + name + "\naddress:" 
										+ address + "\ncity:" +  city + "\ntime:" + time + "\n====");
							}
						}
					}
				}
			}
		}
	}
}
