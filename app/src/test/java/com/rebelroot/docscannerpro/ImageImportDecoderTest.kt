package com.rebelroot.docscannerpro

import com.rebelroot.docscannerpro.core.cv.ImageImportDecoder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.GraphicsMode
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class ImageImportDecoderTest {

    private fun pngBytes(width: Int, height: Int): ByteArray {
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val out = ByteArrayOutputStream()
        ImageIO.write(image, "png", out)
        return out.toByteArray()
    }

    @Test
    fun `valid image decodes even though the bounds pass returns null`() {
        val bytes = pngBytes(64, 48)
        var opens = 0
        val bitmap = ImageImportDecoder.decode(2560) { opens++; ByteArrayInputStream(bytes) }
        assertNotNull(bitmap)
        assertEquals(64, bitmap!!.width)
        assertEquals(48, bitmap.height)
        assertEquals(2, opens)
    }

    @Test
    fun `oversized image is downsampled on decode`() {
        val bytes = pngBytes(3000, 1000)
        val bitmap = ImageImportDecoder.decode(1000) { ByteArrayInputStream(bytes) }
        assertNotNull(bitmap)
        assertEquals(1500, bitmap!!.width)
        assertEquals(500, bitmap.height)
    }

    @Test
    fun `unreadable content returns null instead of throwing`() {
        val garbage = "not an image".toByteArray()
        assertNull(ImageImportDecoder.decode(2560) { ByteArrayInputStream(garbage) })
    }

    @Test
    fun `missing stream returns null`() {
        assertNull(ImageImportDecoder.decode(2560) { null })
    }

    @Test
    fun `sample size keeps the longest edge at or above the cap`() {
        assertEquals(1, ImageImportDecoder.sampleSizeFor(2000, 1500, 2560))
        assertEquals(1, ImageImportDecoder.sampleSizeFor(5119, 3000, 2560))
        assertEquals(2, ImageImportDecoder.sampleSizeFor(5120, 3840, 2560))
        assertEquals(4, ImageImportDecoder.sampleSizeFor(4000, 12000, 2560))
    }
}
