package net.hiraok.feature.top

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.RoborazziTransparentActivity
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@ExperimentalRoborazziApi
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class TextComposeTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<RoborazziTransparentActivity>()

    @Test
    @Config(qualifiers = RobolectricDeviceQualifiers.Pixel5)
    fun captureRoboImageSample() {
        captureRoboImage {
            TopScreen()
        }
    }
}