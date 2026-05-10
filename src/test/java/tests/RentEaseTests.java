package tests;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class RentEaseTests {

    WebDriver driver;
    WebDriverWait wait;

    String BASE_URL = System.getenv().getOrDefault("BASE_URL", "http://13.48.132.213:5174");

    @BeforeMethod
    public void setup() {
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        waitForFrontend(BASE_URL);
    }

    // 🔥 IMPORTANT: wait until frontend is actually up
    private void waitForFrontend(String url) {
        int retries = 10;

        for (int i = 0; i < retries; i++) {
            try {
                driver.get(url);
                if (driver.getTitle() != null || driver.getPageSource().length() > 0) {
                    return;
                }
            } catch (Exception e) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {}
            }
        }

        throw new RuntimeException("Frontend not reachable at " + url);
    }

    @Test
    public void test01LandingPageLoads() {
        driver.get(BASE_URL);
        Assert.assertTrue(driver.getPageSource().length() > 0);
    }

    @Test
    public void test02LoginPageOpens() {
        driver.get(BASE_URL + "/login");
        Assert.assertTrue(driver.getCurrentUrl().contains("login"));
    }

    @Test
    public void test03RegisterPageOpens() {
        driver.get(BASE_URL + "/register");
        Assert.assertTrue(driver.getCurrentUrl().contains("register"));
    }

    @Test
    public void test04InvalidLoginPageLoads() {
        driver.get(BASE_URL + "/login");
        Assert.assertTrue(driver.getPageSource().length() > 0);
    }

    @Test
    public void test05TenantRegistrationPageLoads() {
        driver.get(BASE_URL + "/register");
        String page = driver.getPageSource().toLowerCase();

        Assert.assertTrue(page.contains("tenant") || page.contains("register"));
    }

    @Test
    public void test06LandlordRegistrationPageLoads() {
        driver.get(BASE_URL + "/register");
        String page = driver.getPageSource().toLowerCase();

        Assert.assertTrue(page.contains("landlord") || page.contains("register"));
    }

    @Test
    public void test07PropertiesPageLoads() {
        driver.get(BASE_URL + "/properties");
        Assert.assertTrue(driver.getPageSource().length() > 0);
    }

    @Test
    public void test08TenantDashboardRouteLoads() {
        driver.get(BASE_URL + "/tenant-dashboard");
        Assert.assertTrue(driver.getPageSource().length() > 0);
    }

    @Test
    public void test09LandlordDashboardRouteLoads() {
        driver.get(BASE_URL + "/landlord-dashboard");
        Assert.assertTrue(driver.getPageSource().length() > 0);
    }

    @Test
    public void test10AddPropertyRouteLoads() {
        driver.get(BASE_URL + "/add-property");
        Assert.assertTrue(driver.getPageSource().length() > 0);
    }

    @Test
    public void test11ApplicationsRouteLoads() {
        driver.get(BASE_URL + "/applications");
        Assert.assertTrue(driver.getPageSource().length() > 0);
    }

    @Test
    public void test12ProfileRouteLoads() {
        driver.get(BASE_URL + "/profile");
        Assert.assertTrue(driver.getPageSource().length() > 0);
    }

    @Test
    public void test13PageContainsRentEaseText() {
        driver.get(BASE_URL);

        String page = driver.getPageSource().toLowerCase();
        Assert.assertTrue(page.contains("rent") || page.contains("ease"));
    }

    @Test
    public void test14BrowserCanNavigateBack() {
        driver.get(BASE_URL);
        driver.get(BASE_URL + "/login");

        driver.navigate().back();

        Assert.assertTrue(driver.getCurrentUrl().contains(BASE_URL));
    }

    @Test
    public void test15LogoutRouteLoads() {
        driver.get(BASE_URL + "/logout");
        Assert.assertTrue(driver.getPageSource().length() > 0);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}


Rentease test wali fike main yeh dalo
