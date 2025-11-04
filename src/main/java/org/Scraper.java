package org;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Scraper {

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        System.out.println("Starting scraper...");

        // Setup WebDriverManager
        WebDriverManager.chromedriver().setup();

        // Setup Chrome to run "headless"
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--window-size=1920,1080");

        WebDriver driver = new ChromeDriver(options);

        // This list will hold all our Senator objects
        List<Senator> senators = new ArrayList<>();
        String targetUrl = "https://akleg.gov/senate.php";

        try {
            // Navigate to the page
            driver.get(targetUrl);
            System.out.println("Navigated to " + targetUrl);

            // Wait for the main content block to be visible
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            System.out.println("Waiting for senator list container (id='tab1-2')...");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tab1-2")));
            System.out.println("Container found!");

            // Get the list of all senator <li> elements
            List<WebElement> senatorBlocks = driver.findElements(By.cssSelector("#tab1-2 ul.item > li"));
            System.out.println("Found " + senatorBlocks.size() + " senator blocks.");

            // Loop through each senator block and extract data
            for (WebElement block : senatorBlocks) {
                Senator senator = new Senator();

                // Use a helper function to safely get data using XPath
                // This prevents one missing field from crashing the whole scrape
                String name = safeFindText(block, By.cssSelector("strong.name"));
                String url = safeFindAttribute(block, By.tagName("a"), "href");
                String email = safeFindAttribute(block, By.cssSelector("a[href^='mailto:']"), "href")
                        .replace("mailto:", "");

                String party = safeFindText(block, By.xpath(".//dt[text()='Party:']/following-sibling::dd[1]"));
                String position = safeFindText(block, By.xpath(".//dt[text()='District:']/following-sibling::dd[1]"));
                String phone = safeFindText(block, By.xpath(".//dt[text()='Phone:']/following-sibling::dd[1]"));
                String address = safeFindText(block, By.xpath(".//dt[text()='City:']/following-sibling::dd[1]"));

                // The "Title" field isn't in this block. We'll set it to "Senator".
                senator.setTitle("Senator");
                senator.setName(name);
                senator.setUrl(url);
                senator.setEmail(email);
                senator.setParty(party);
                senator.setPosition("Senate District " + position);
                senator.setPhone(phone);
                senator.setAddress(address);

                senators.add(senator);
                System.out.println("Scraped: " + senator.getName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }

        // Write the List to a JSON file
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(new File("senators.json"), senators);

        long endTime = System.currentTimeMillis();
        long totalTime = (endTime - startTime) / 1000;

        System.out.println("==================================================");
        System.out.println("Finished scraping!");
        System.out.println("Saved data for " + senators.size() + " senators to senators.json");
        System.out.println("Total time taken: " + totalTime + " seconds.");
    }

//    A helper function to safely find text and return "N/A" if not found.
//    This prevents NoSuchElementException.

    private static String safeFindText(WebElement element, By by) {
        try {
            return element.findElement(by).getText();
        } catch (NoSuchElementException e) {
            return "N/A";
        }
    }


//    A helper function to safely find an attribute and return "N/A" if not found.

    private static String safeFindAttribute(WebElement element, By by, String attribute) {
        try {
            return element.findElement(by).getAttribute(attribute);
        } catch (NoSuchElementException e) {
            return "N/A";
        }
    }
}