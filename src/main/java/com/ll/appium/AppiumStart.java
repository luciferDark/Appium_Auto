package com.ll.appium;

import com.ll.appium.process.Login;
import com.ll.util.Utils;
import org.openqa.selenium.Point;

import java.io.File;

public class AppiumStart {
    AppiumHelper appiumHelper;

    public void init() {
        Utils.log("init!");
        //AppiumHelper.getInstance().init();//mumu模拟器
        appiumHelper = AppiumHelper.getInstance();
        appiumHelper.init(); //木木模拟器
        // appiumHelper.init("ASUS_X00QD","9","JAAXB76228352YV");//华硕手机
    }

    //435-975
    public void start() {
        Utils.log("start!");
        appiumHelper.stopSecond(60);
        appiumHelper.clickByTxt("允许");
        int xW = 435;
        int yH = 975;
//        Login login = new Login();
        appiumHelper.compareImgAndScreen("tw" + File.separator + "login" + File.separator + "login.png");
//        appiumHelper.touchByCoordinatePercent(0.805, 0.946);
//        appiumHelper.touchByCoordinatePercent(327.0/xW, 585.0/yH);
    }
}
