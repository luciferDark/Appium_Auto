package com.ll;

import com.ll.appium.AppiumStart;
import com.ll.util.Utils;

public class Main {
    public static void main(String[] args) {
        startAppium();
    }

    private static void startAppium() {
        AppiumStart appiumStart = new AppiumStart();
        appiumStart.init();
        appiumStart.start();
    }
}
