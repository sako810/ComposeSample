package net.hiraok.museum

import android.content.Context
import android.view.MotionEvent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import org.rajawali3d.animation.Animation
import org.rajawali3d.animation.Animation3D
import org.rajawali3d.animation.EllipticalOrbitAnimation3D
import org.rajawali3d.animation.RotateOnAxisAnimation
import org.rajawali3d.lights.PointLight
import org.rajawali3d.loader.LoaderOBJ
import org.rajawali3d.loader.ParsingException
import org.rajawali3d.math.vector.Vector3
import org.rajawali3d.renderer.Renderer
import org.rajawali3d.view.TextureView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

@Composable
fun ModelScreen() {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            TextureView(it).apply {
                val renderer = ModelRenderer(it)
                setSurfaceRenderer(renderer)
            }
        })
}

class ModelRenderer(
    context: Context
) : IRenderer(context) {

    private var anim: Animation3D? = null

    override fun initScene() {

        val light = PointLight()
        light.setPosition(10.0, 30.0, 4.0)
        light.power = 3f
        currentScene.addLight(light)
        currentCamera.z = 40.0

        val parser = LoaderOBJ(mContext.resources, mTextureManager, R.raw.cybertruck)
        try {
            parser.parse()
            val o = parser.parsedObject
            o.y = -.10
            currentScene.addChild(o)

            anim = RotateOnAxisAnimation(Vector3.Axis.Y, 360.0)
            anim?.durationMilliseconds = 8000
            anim?.repeatMode = Animation.RepeatMode.INFINITE
            anim?.transformable3D = o
        } catch (e: ParsingException) {
            e.printStackTrace()
        }
        val lightAnim = EllipticalOrbitAnimation3D(
            Vector3(),
            Vector3(0.0, 0.0, 0.0),
            Vector3.getAxisVector(Vector3.Axis.Z),
            0.0,
            360.0,
            EllipticalOrbitAnimation3D.OrbitDirection.CLOCKWISE
        )

        lightAnim.durationMilliseconds = 3000
        lightAnim.repeatMode = Animation.RepeatMode.INFINITE
        lightAnim.transformable3D = light
        currentScene.registerAnimation(anim)
        currentScene.registerAnimation(lightAnim)

        anim?.play()
        lightAnim.play()

    }

}

abstract class IRenderer(
    context: Context
) : Renderer(context) {

    override fun onRenderSurfaceCreated(config: EGLConfig?, gl: GL10?, width: Int, height: Int) {
        super.onRenderSurfaceCreated(config, gl, width, height)
    }

    override fun onOffsetsChanged(
        xOffset: Float,
        yOffset: Float,
        xOffsetStep: Float,
        yOffsetStep: Float,
        xPixelOffset: Int,
        yPixelOffset: Int
    ) {
    }

    override fun onTouchEvent(event: MotionEvent?) {
    }
}