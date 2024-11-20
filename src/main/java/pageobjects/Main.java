//package pageobjects;
//
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.chrome.ChromeDriver;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
////TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
//// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
//public class Main {
//    public static void main(String[] args) {
//        // Set the path for the ChromeDriver
//        System.setProperty("webdriver.chrome.driver", "D:/WEB Downloads/chromedriver-win64/chromedriver-win64");
//
//        // Initialize the ChromeDriver
//        WebDriver driver = new ChromeDriver();
//
//        try {
//            // Open the IPL website
//            driver.get("https://www.iplt20.com");
//
//            // Navigate to the Points Table
//            driver.findElement(By.linkText("POINTS TABLE")).click();
//
//            // Wait for the table to load
//            Thread.sleep(2000); // Use WebDriverWait for better practice in production code
//
//            // Get the points table rows
//            List<WebElement> rows = driver.findElements(By.xpath("//table[@class='points-table']/tbody/tr"));
//
//            // Create a map to hold points and corresponding teams
//            Map<Double, String> pointsMap = new HashMap<>();
//
//            // Iterate through the rows to extract team names and their points
//            for (WebElement row : rows) {
//                String teamName = row.findElement(By.xpath(".//td[2]")).getText(); // Team name is in the second column
//                String pointsStr = row.findElement(By.xpath(".//td[3]")).getText(); // Points are in the third column
//
//                // Convert points to a double
//                double points = Double.parseDouble(pointsStr);
//
//                // Add to the map (handling same points)
//                pointsMap.merge(points, teamName, (existingTeams, newTeam) -> existingTeams + ", " + newTeam);
//            }
//
//            // Print the results
//            for (Map.Entry<Double, String> entry : pointsMap.entrySet()) {
//                System.out.println(entry.getKey() + " - (" + entry.getValue() + ")");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            // Close the browser
//            driver.quit();
//        }
//    }
//}
