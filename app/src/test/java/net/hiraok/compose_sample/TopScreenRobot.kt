package net.hiraok.compose_sample

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@ExperimentalMaterial3Api
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@RunWith(AndroidJUnit4::class)
class TopScreenRobot {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Config(qualifiers = RobolectricDeviceQualifiers.Pixel7)
    @Test
    fun `スクリーンテストで表示される`() {
        composeTestRule.onNode(isRoot())
            .captureRoboImage("build/outputs/roborazzi/screenshotTest.png")
    }

}