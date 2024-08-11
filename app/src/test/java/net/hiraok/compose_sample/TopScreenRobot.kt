package net.hiraok.compose_sample

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.DEFAULT_ROBORAZZI_OUTPUT_DIR_PATH
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.captureRoboImage
import net.hiraok.feature.top.SecondScreen
import net.hiraok.feature.top.TopScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File

@ExperimentalMaterial3Api
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@RunWith(AndroidJUnit4::class)
class TopScreenRobot {

    @get:Rule
    val roborazziRule = RoborazziRule(
        options = RoborazziRule.Options(
            outputDirectoryPath = DEFAULT_ROBORAZZI_OUTPUT_DIR_PATH,
            outputFileProvider = { description, outputDirectory, fileExtension ->
                File(
                    outputDirectory,
                    "${description.methodName}.$fileExtension"
                )
            }
        ),
    )

    @Config(qualifiers = RobolectricDeviceQualifiers.Pixel7)
    @Test
    fun `TopScreenCapture`() {
        captureRoboImage {
            TopScreen()
        }
    }

    @Config(qualifiers = RobolectricDeviceQualifiers.Pixel7)
    @Test
    fun `SecondScreenCapture`() {
        captureRoboImage {
            SecondScreen()
        }
    }

}