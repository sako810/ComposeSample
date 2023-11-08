package net.hiraok.composesample

import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@GraphicsMode(GraphicsMode.Mode.NATIVE)
@RunWith(AndroidJUnit4::class)
class HomeScreenRobot {

    @get:Rule
    val composeTestRule = createComposeRule()


    @Config(qualifiers = RobolectricDeviceQualifiers.Pixel7)
    @Test
    fun `スクリーンテストで表示される`() {
        composeTestRule.setContent {
            Home()
        }
        composeTestRule.onNode(isRoot())
            .captureRoboImage("build/outputs/roborazzi/screenshotTest.png")
        composeTestRule.onNodeWithText("init diff diff3")
    }

}