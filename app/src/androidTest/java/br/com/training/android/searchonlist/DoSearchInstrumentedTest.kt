package br.com.training.android.searchonlist

import android.content.Intent
import android.content.pm.PackageManager
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.*
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DoSearchInstrumentedTest {
    @get:Rule
    val activityRule = ActivityTestRule<MainActivity>(MainActivity::class.java)
    private lateinit var uiDevice: UiDevice

    @Before
    fun setup() {
        uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        // Start from the home screen
        uiDevice.pressHome()

        uiDevice.wait(Until.hasObject(By.pkg(getLauncherPackageName()).depth(0)), 1000)
    }

    private fun getLauncherPackageName(): String? {
        // Create launcher Intent
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)

        // Use PackageManager to get the launcher package name
        val pm: PackageManager = InstrumentationRegistry.getInstrumentation().context.packageManager
        val resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)

        return resolveInfo.activityInfo.packageName
    }

    @Test
    @Throws(UiObjectNotFoundException::class)
    fun checkSearchOnListApp() {
        uiDevice.pressHome()

        val allAppsButton: UiObject = uiDevice.findObject(UiSelector().description("Apps"))

        allAppsButton.clickAndWaitForNewWindow()

        val appViews = UiScrollable(
            UiSelector().scrollable(true))

        appViews.setAsVerticalList()

        val searchOnListViewer = appViews
            .getChildByText(
                UiSelector().className(TextView::class.java.name),
                activityRule.activity.resources.getString(R.string.app_name),
                true
            )

        searchOnListViewer.clickAndWaitForNewWindow()

        val searchOnListValidation = uiDevice
            .findObject(
                UiSelector()
                    .packageName(activityRule.activity.applicationContext.packageName)
            )

        assertTrue(searchOnListValidation.exists())

        val searchIcon = uiDevice.findObject(
            UiSelector()
                .resourceId("br.com.training.android.searchonlist:id/search_dialog_button"))

        searchIcon.clickAndWaitForNewWindow()

        val searchText = uiDevice.findObject(UiSelector().resourceId("android:id/search_src_text"))

        assertTrue(searchText.exists())

        searchText.text = "sslvador"

        uiDevice.pressEnter()

        val ssaText = uiDevice.findObject(
            UiSelector()
                .resourceId("br.com.training.android.searchonlist:id/listToSearch")
                .childSelector(UiSelector().text("Salvador"))
        )

        assertTrue(ssaText.exists())
    }

}
