package com.github.joaotfernandes.extension

import com.android.ddmlib.RawImage
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

/**
 * Converts this [RawImage] to a [BufferedImage] and saves it into the specified *file*. The conversion takes into
 * consideration the specified *imageType* and encodes the image to the given *imageFormat*.
 *
 * @param imageFormat informal name of the image format (e.g. png, jpg)
 * @param imageType type of the created image
 * @param file file where the [RawImage] will be saved to
 *
 * @see [BufferedImage.TYPE_INT_RGB]
 * @see [BufferedImage.TYPE_INT_ARGB]
 * @see [BufferedImage.TYPE_INT_ARGB_PRE]
 * @see [BufferedImage.TYPE_INT_BGR]
 * @see [BufferedImage.TYPE_3BYTE_BGR]
 * @see [BufferedImage.TYPE_4BYTE_ABGR]
 * @see [BufferedImage.TYPE_4BYTE_ABGR_PRE]
 * @see [BufferedImage.TYPE_BYTE_GRAY]
 * @see [BufferedImage.TYPE_USHORT_GRAY]
 * @see [BufferedImage.TYPE_BYTE_BINARY]
 * @see [BufferedImage.TYPE_BYTE_INDEXED]
 * @see [BufferedImage.TYPE_USHORT_565_RGB]
 * @see [BufferedImage.TYPE_USHORT_555_RGB]
 */
fun RawImage.toFile(imageFormat: String, imageType: Int, file: File) {
    // Creating a BufferedImage directly instead of using UIUtil.createImage due to UIUtil.createImage producing
    // an image double the size for retina devices whilst keeping the raw image in its original size
    val bufferedImage = BufferedImage(width, height, imageType)

    var index = 0
    val indexSteps = bpp shr bufferedImage.colorModel.numColorComponents
    for (y in 0 until height) {
        for (x in 0 until width) {
            bufferedImage.setRGB(x, y, getARGB(index))
            index += indexSteps
        }
    }

    ImageIO.write(bufferedImage, imageFormat, file)
}