package com.rebelroot.docscannerpro.core.cv

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.InputStream

/**
 * Memory-bounded two-pass decoding for imported images (gallery / document providers).
 *
 * Pass one reads only the header to learn the size. With `inJustDecodeBounds`,
 * BitmapFactory always returns null and reports the size through the options,
 * so a null result from that pass is expected and must not be treated as failure.
 * Pass two decodes with a power-of-two sample size so huge photos never land in RAM at full size.
 */
object ImageImportDecoder {

    /** Largest power-of-two sample size that keeps the longest edge at or above [maxDim]. */
    fun sampleSizeFor(width: Int, height: Int, maxDim: Int): Int {
        var sample = 1
        while (maxOf(width, height) / (sample * 2) >= maxDim) sample *= 2
        return sample
    }

    /**
     * [openStream] is invoked once per pass (content:// streams are not rewindable).
     * Returns null when the stream cannot be opened or the content is not a decodable image.
     */
    fun decode(maxDim: Int, openStream: () -> InputStream?): Bitmap? {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        (openStream() ?: return null).use { BitmapFactory.decodeStream(it, null, bounds) }
        if (bounds.outWidth <= 0 || bounds.outHeight <= 0) return null
        val opts = BitmapFactory.Options().apply {
            inSampleSize = sampleSizeFor(bounds.outWidth, bounds.outHeight, maxDim)
        }
        return (openStream() ?: return null).use { BitmapFactory.decodeStream(it, null, opts) }
    }
}
