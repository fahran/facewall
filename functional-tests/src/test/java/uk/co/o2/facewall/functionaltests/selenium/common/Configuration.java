package uk.co.o2.facewall.functionaltests.selenium.common;

public class Configuration {

    public static String testDriver = System.getProperty("qa.testdriver", "webdriver"); // "jsoup" or "webdriver"
    public static String browser = System.getProperty("qa.browser", "firefox");
    public static String browserVersion = System.getProperty("qa.browserVersion", "22");

    public static String baseUrl = "http://localhost:9001/facewall";
}
