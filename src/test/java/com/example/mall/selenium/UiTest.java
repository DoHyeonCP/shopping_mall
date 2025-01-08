package com.example.mall.selenium;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;



public class UiTest {


    private WebDriverWait wait;
    private WebDriver driver;

    @BeforeEach
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "selenium-webdriver/chromedriver-win64(131.0.6778.85)/chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get("http://localhost:8080"); // Spring Boot 쇼핑몰 URL
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void login(String username, String password) {
        driver.findElement(By.id("login-link")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("login-button")).click();
    }

    @Test
    public void testLogin() {
        login("user@example.com", "password123");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("welcome-message")));
        assertTrue(driver.findElement(By.id("welcome-message")).isDisplayed());
        System.out.println("로그인 테스트 성공");
    }

    @Test
    public void testViewProducts() {
        driver.findElement(By.id("products-link")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("product-item")));
        assertTrue(driver.findElements(By.className("product-item")).size() > 0);
        System.out.println("상품 조회 테스트 성공");
    }

    @Test
    public void testViewProductDetail() {
        driver.findElement(By.id("products-link")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("product-item")));
        driver.findElements(By.className("product-item")).get(0).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("product-detail")));
        assertTrue(driver.findElement(By.id("product-detail")).isDisplayed());
        System.out.println("상품 상세 조회 테스트 성공");
    }

    @Test
    public void testAddToCart() {
        testViewProductDetail();
        driver.findElement(By.id("add-to-cart-button")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cart-success-message")));
        assertTrue(driver.findElement(By.id("cart-success-message")).isDisplayed());
        System.out.println("장바구니 담기 테스트 성공");
    }

    @Test
    public void testViewCart() {
        driver.findElement(By.id("cart-link")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cart-items")));
        assertTrue(driver.findElement(By.id("cart-items")).isDisplayed());
        System.out.println("장바구니 조회 테스트 성공");
    }

    @Test
    public void testPurchase() {
        testViewCart();
        driver.findElement(By.id("checkout-button")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("order-confirmation")));
        assertTrue(driver.findElement(By.id("order-confirmation")).isDisplayed());
        System.out.println("구매 테스트 성공");
    }

    @Test
    public void testViewOrders() {
        driver.findElement(By.id("orders-link")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("order-item")));
        assertTrue(driver.findElements(By.className("order-item")).size() > 0);
        System.out.println("구매 조회 테스트 성공");
    }

    @Test
    public void testAdminProductManagement() {
        login("admin@example.com", "adminpassword");

        // 상품 등록
        driver.findElement(By.id("admin-link")).click();
        driver.findElement(By.id("add-product-button")).click();
        driver.findElement(By.id("product-name")).sendKeys("새 상품");
        driver.findElement(By.id("product-price")).sendKeys("10000");
        driver.findElement(By.id("product-description")).sendKeys("새 상품 설명");
        driver.findElement(By.id("submit-button")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("product-item")));
        assertTrue(driver.findElements(By.className("product-item")).stream()
                .anyMatch(item -> item.getText().contains("새 상품")));
        System.out.println("상품 등록 테스트 성공");

        // 상품 수정
        driver.findElement(By.className("edit-product-button")).click();
        WebElement nameField = driver.findElement(By.id("product-name"));
        nameField.clear();
        nameField.sendKeys("수정된 상품");
        driver.findElement(By.id("submit-button")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("product-item")));
        assertTrue(driver.findElements(By.className("product-item")).stream()
                .anyMatch(item -> item.getText().contains("수정된 상품")));
        System.out.println("상품 수정 테스트 성공");

        // 상품 삭제
        driver.findElement(By.className("delete-product-button")).click();
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
        System.out.println("상품 삭제 테스트 성공");
    }
    
}
