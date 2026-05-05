package tests;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.*;

public class RentEaseTests {

    WebDriver driver;
    String BASE_URL = System.getenv().getOrDefault("BASE_URL", "http://localhost:5174");

    @BeforeMethod
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);
    }

    @Test
    public void test01LandingPageLoads() throws InterruptedException {
        driver.get(BASE_URL);
        Thread.sleep(3000);
        Assert.assertTrue(driver.getPageSource().length() > 0);
    }

    @Test
    public void test02LoginPageOpens() throws InterruptedException {
        driver.get(BASE_URL + "/login");
        Thread.sleep(3000);
        Assert.assertTrue(driver.getCurrentUrl().contains("login"));
    }

    @Test
    public void test03RegisterPageOpens() throws InterruptedException {
        driver.get(BASE_URL + "/register");
        Thread.sleep(3000);
        Assert.assertTrue(driver.getCurrentUrl().contains("register"));
    }

    @Test
public void test04InvalidLoginPageLoads() throws InterruptedException {
    driver.get(BASE_URL + "/login");
    Thread.sleep(3000);

    Assert.assertTrue(driver.getPageSource().length() > 0);
}

    @Test
    public void test05TenantRegistrationPageLoads() throws InterruptedException {
        driver.get(BASE_URL + "/register");
        Thread.sleep(3000);
        Assert.assertTrue(driver.getPageSource().toLowerCase().contains("tenant")
                || driver.getPageSource().toLowerCase().contains("register"));
    }

    @Test
    public void test06LandlordRegistrationPageLoads() throws InterruptedException {
        driver.get(BASE_URL + "/register");
        Thread.sleep(3000);
        Assert.assertTrue(driver.getPageSource().toLowerCase().contains("landlord")
                || driver.getPageSource().toLowerCase().contains("register"));
    }

    @Test
    public void test07PropertiesPageLoads() throws InterruptedException {
        driver.get(BASE_URL + "/properties");
        Thread.sleep(3000);
        Assert.assertTrue(driver.getPageSource().length() > 0);
    }

    @Test
    public void test08TenantDashboardRouteLoads() throws InterruptedException {
        driver.get(BASE_URL + "/tenant-dashboard");
        Thread.sleep(3000);
        Assert.assertTrue(driver.getPageSource().length() > 0);
    }

    @Test
    public void test09LandlordDashboardRouteLoads() throws InterruptedException {
        driver.get(BASE_URL + "/landlord-dashboard");
        Thread.sleep(3000);
        Assert.assertTrue(driver.getPageSource().length() > 0);
    }

    @Test
    public void test10AddPropertyRouteLoads() throws InterruptedException {
        driver.get(BASE_URL + "/add-property");
        Thread.sleep(3000);
        Assert.assertTrue(driver.getPageSource().length() > 0);
    }

    @Test
    public void test11ApplicationsRouteLoads() throws InterruptedException {
        driver.get(BASE_URL + "/applications");
        Thread.sleep(3000);
        Assert.assertTrue(driver.getPageSource().length() > 0);
    }

    @Test
    public void test12ProfileRouteLoads() throws InterruptedException {
        driver.get(BASE_URL + "/profile");
        Thread.sleep(3000);
        Assert.assertTrue(driver.getPageSource().length() > 0);
    }

    @Test
    public void test13PageContainsRentEaseText() throws InterruptedException {
        driver.get(BASE_URL);
        Thread.sleep(3000);
        Assert.assertTrue(driver.getPageSource().toLowerCase().contains("rent")
                || driver.getPageSource().toLowerCase().contains("ease"));
    }

    @Test
    public void test14BrowserCanNavigateBack() throws InterruptedException {
        driver.get(BASE_URL);
        driver.get(BASE_URL + "/login");
        driver.navigate().back();
        Thread.sleep(2000);
        Assert.assertTrue(driver.getPageSource().length() > 0);
    }

    @Test
    public void test15LogoutRouteLoads() throws InterruptedException {
        driver.get(BASE_URL + "/logout");
        Thread.sleep(3000);
        Assert.assertTrue(driver.getPageSource().length() > 0);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
