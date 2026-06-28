package com.example.app.baselineprofile

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until

const val PACKAGE_NAME = "com.example.app"

private const val TAG_USERNAME = "login_username"
private const val TAG_PASSWORD = "login_password"
private const val TAG_PRODUCTS_LIST = "products_list"

private const val DEMO_USERNAME = "emilys"
private const val DEMO_PASSWORD = "emilyspass"

private const val WAIT_MS = 10_000L

fun MacrobenchmarkScope.loginAndOpenProducts() {
    device.wait(Until.hasObject(By.res(TAG_USERNAME)), WAIT_MS)
    device.findObject(By.res(TAG_USERNAME)).text = DEMO_USERNAME
    device.findObject(By.res(TAG_PASSWORD)).text = DEMO_PASSWORD
    device.pressBack()

    device.wait(Until.hasObject(By.text("Masuk")), WAIT_MS)
    device.findObject(By.text("Masuk")).click()

    device.wait(Until.hasObject(By.text("Products")), WAIT_MS)
    device.findObject(By.text("Products")).click()

    device.wait(Until.hasObject(By.res(TAG_PRODUCTS_LIST)), WAIT_MS)
    device.waitForIdle()
}

fun MacrobenchmarkScope.scrollProductsList() {
    val list = device.findObject(By.res(TAG_PRODUCTS_LIST))
    list.setGestureMargin(device.displayWidth / 5)
    repeat(3) {
        list.fling(Direction.DOWN)
        device.waitForIdle()
    }
    list.fling(Direction.UP)
    device.waitForIdle()
}
