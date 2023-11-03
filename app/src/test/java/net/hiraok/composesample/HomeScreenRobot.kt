package net.hiraok.composesample

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onParent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.GraphicsMode

@GraphicsMode(GraphicsMode.Mode.NATIVE)
@RunWith(AndroidJUnit4::class)
class HomeScreenRobot {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun captureRoboImageSample() {
        // Tips: You can use Robolectric with Espresso API
        // launch
        ActivityScenario.launch(MainActivity::class.java)

        // Capture screen
        onView(ViewMatchers.isRoot())
            // If you don't specify a screenshot file name, Roborazzi will automatically use the method name as the file name for you.
            // The format of the file name will be as follows:
            // build/outputs/roborazzi/com_..._ManualTest_captureRoboImageSample.png
            .captureRoboImage()

        // Capture Jetpack Compose Node
        composeTestRule.onNodeWithTag("Home")
            .onParent()
            .captureRoboImage("build/home.png")
    }

}