package com.ll.appium;

import com.ll.util.FileUtils;
import com.ll.util.StringUtils;
import com.ll.util.Utils;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.imagecomparison.FeatureDetector;
import io.appium.java_client.imagecomparison.FeaturesMatchingOptions;
import io.appium.java_client.imagecomparison.FeaturesMatchingResult;
import io.appium.java_client.imagecomparison.MatchingFunction;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.commons.codec.binary.Base64;
import java.util.concurrent.TimeUnit;

import static java.time.Duration.ofSeconds;

public class AppiumHelper {
    public static AndroidDriver driver;
    protected static DesiredCapabilities capabilities;
    public int DEVICE_SCREEN_WIDTH = 0;
    public int DEVICE_SCREEN_HEIGHT = 0;

    private AppiumHelper() {
    }

    private static class AppiumHelpInstance {
        private static final AppiumHelper instance = new AppiumHelper();
    }

    public static AppiumHelper getInstance() {
        return AppiumHelpInstance.instance;
    }

    public void init() {
        this.init(null, null, null, null, null);
    }

    public void init(String deviceName, String plantformVersion, String udid) {
        this.init(deviceName, plantformVersion, udid, null, null);
    }

    public void init(String packageName, String mainActivity) {
        this.init(null, null, null, packageName, mainActivity);
    }

    public void init(String deviceName, String plantformVersion, String udid, String packageName, String mainActivity) {
        try {
            capabilities = new DesiredCapabilities();
            capabilities.setCapability("automationName", AppiumConfig.CONFIG_AUTOMATIONNAME);//appium做自动化
            // capabilities.setCapability("app", "C:\\software\\jrtt.apk");//安装apk
            // capabilities.setCapability("browserName", "chrome");//设置HTML5的自动化，打开谷歌浏览器
            capabilities.setCapability("deviceName", StringUtils.isEmpty(deviceName) ? AppiumConfig.CONFIG_DEVICENAME : deviceName);//设备名称
            capabilities.setCapability("platformName", AppiumConfig.CONFIG_PLATFORMNAME); //安卓自动化还是IOS自动化
            capabilities.setCapability("platformVersion", StringUtils.isEmpty(plantformVersion) ? AppiumConfig.CONFIG_PLATFORMVERSION : plantformVersion); //安卓操作系统版本
            capabilities.setCapability("udid", StringUtils.isEmpty(udid) ? AppiumConfig.CONFIG_PLATFORMUDID : udid); //设备的udid (adb devices 查看到的)
            capabilities.setCapability("appPackage", StringUtils.isEmpty(packageName) ? AppiumConfig.CONFIG_APP_PACKAGENAME : packageName);//被测app的包名
            capabilities.setCapability("appActivity", StringUtils.isEmpty(mainActivity) ? AppiumConfig.CONFIG_APP_MAINACTIVITY : mainActivity);//被测app的入口Activity名称
            capabilities.setCapability("unicodeKeyboard", AppiumConfig.CONFIG_CN_INPUT_UNICODE); //支持中文输入
            capabilities.setCapability("resetKeyboard", AppiumConfig.CONFIG_CN_INPUT_RESETKEY); //支持中文输入，必须两条都配置
            capabilities.setCapability("noSign", AppiumConfig.CONFIG_APP_NOSIGN); //不重新签名apk
            capabilities.setCapability("noReset", AppiumConfig.CONFIG_APP_NORESET); //启动app时不要清除app里的原有的数据
            capabilities.setCapability("newCommandTimeout", AppiumConfig.CONFIG_QUITAPPUIM_TIMEOUT); //没有新命令，appium30秒退出
            driver = new AndroidDriver(new URL(AppiumConfig.CONFIG_SERVER_URL), capabilities);
            Utils.log("Start AndroidDriver:" + (StringUtils.isEmpty(udid) ? AppiumConfig.CONFIG_PLATFORMUDID : udid));
            DEVICE_SCREEN_WIDTH = getScreenWidth();
            DEVICE_SCREEN_HEIGHT = getScreenHeight();
            Utils.log("AndroidDriver screen size w=" + DEVICE_SCREEN_WIDTH + " , h=" + DEVICE_SCREEN_HEIGHT);
            // driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);//隐式等待
        } catch (MalformedURLException e) {
            Utils.log("init Exception\n:" + e.getMessage());
        }
    }

    public int getScreenWidth() {
        if (null == driver)
            return 0;
        return driver.manage().window().getSize().width;
    }

    public int getScreenHeight() {
        if (null == driver)
            return 0;
        return driver.manage().window().getSize().height;
    }

    public void clickCoordinate(int x, int y) {
        if (null == driver) {
            return;
        }
        PointOption pointOption = new PointOption();
        pointOption.withCoordinates(x, y);
        new TouchAction(driver).press(pointOption).perform();
        Utils.log("testLY press x: " + x + "  y:" + y);
        stopSecond(2);
    }

    public void touchByCoordinatePercent(double xPercent, double yPercent) {
        if (null == driver)
            return;
        Utils.log("touchByCoordinatePercent: xPercent=" + xPercent + ", yPercent=" + yPercent);
        int x = (int) (xPercent *((DEVICE_SCREEN_WIDTH == 0) ? getScreenWidth() : DEVICE_SCREEN_WIDTH));
        int y = (int) (yPercent *((DEVICE_SCREEN_HEIGHT == 0) ? getScreenHeight() : DEVICE_SCREEN_HEIGHT));
        touchByCoordinate(new Point(x, y));
    }


    public void touchByCoordinate(Point point) {
        Utils.log("touchByCoordinate: x=" + point.x + "  y=" + point.y);
        if (null == point || null == driver)
            return;
        try {
            TouchAction dragNDrop = new TouchAction(driver)
                    .press(PointOption.point(point))
                    .release();
            Utils.log("touchByCoordinate point x: " + point.x + "  y:" + point.y);
            dragNDrop.perform();
        } catch (Exception e) {
            Utils.log("touchByCoordinate Exception\n:" + e.getMessage());
        }
        stopSecond(2);
    }

    public void clickByTxt(String txt) {
        if (StringUtils.isEmpty(txt) || null == driver) {
            return;
        }
        try {
            WebElement primaryElement = driver.findElementByAndroidUIAutomator("new UiSelector().text(\"" + txt + "\")");
            if (primaryElement != null) {
                Utils.log("clickByTxt click:" + txt);
                primaryElement.click();
            } else {
                Utils.log("not find :" + txt);
            }
        } catch (Exception e) {
            Utils.log("clickByTxt Exception\n:" + e.getMessage());
        }
        stopSecond(2);
    }

    public boolean compareImgAndScreen(String imgUrl) {
        if (StringUtils.isEmpty(imgUrl) || null == driver) {
            return false;
        }
        String resPath = FileUtils.getResRoot1();
        if (StringUtils.isEmpty(resPath)) {
            return false;
        }
        String imgPath = resPath + File.separator + imgUrl;
        boolean compareResult = false;
        try {
            Utils.log("compareImgAndScreen imgPath=" + imgPath);
            File file = new File(imgPath);
            File refImgFile = Paths.get(file.toURI()).toFile();
            byte[] imgBytes =Files.readAllBytes(refImgFile.toPath());
            byte[] screenshot = Base64.encodeBase64(driver.getScreenshotAs(OutputType.BYTES));
            FeaturesMatchingResult result = driver
                    .matchImagesFeatures(imgBytes, screenshot, new FeaturesMatchingOptions()
                            .withDetectorName(FeatureDetector.ORB)
                            .withGoodMatchesFactor(40)
                            .withMatchFunc(MatchingFunction.BRUTE_FORCE_HAMMING)
                            .withEnabledVisualization());
            compareResult = true;
        } catch (Exception e) {
            Utils.log("compareImgAndScreen Exception\n:" + e.getMessage());
            return false;
        }
        return compareResult;
    }

    public void clickByImg(String imgUrl) {
        if (StringUtils.isEmpty(imgUrl) || null == driver) {
            return;
        }
        String resPath = FileUtils.getResRoot1();
        if (StringUtils.isEmpty(resPath)) {
            return;
        }
        String imgPath = resPath + File.separator + imgUrl;
        try {
            File file = new File(imgPath);
            File refImgFile = Paths.get(file.toURI()).toFile();
            byte[] imgBytes =Files.readAllBytes(refImgFile.toPath());
            String s = Base64.encodeBase64String(imgBytes);
            Utils.log("img base64 code=" + s);
            WebElement primaryElement = driver.findElementByImage(s);
            if (primaryElement != null) {
                primaryElement.click();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        stopSecond(2);
    }

    public void sleepThread(int sec) {
        if (null == driver) {
            return;
        }
        try {
            Thread.sleep(sec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stopSecond(int sec) {
        if (null == driver) {
            return;
        }
        try {
            driver.manage().timeouts().implicitlyWait(sec, TimeUnit.SECONDS);//隐式等待
        } catch (Exception e) {
            Utils.log("stopSecond Exception\n:" + e.getMessage());
        }
    }
}
