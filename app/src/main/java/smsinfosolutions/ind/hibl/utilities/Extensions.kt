package smsinfosolutions.ind.hibl.utilities

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import smsinfosolutions.ind.hibl.ContextUtils

import smsinfosolutions.ind.hibl.R
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.util.*

/**
 * Created by Manish on 28-Jul-20.
 */

fun Context.showMsg(msg: String, duration: Int = Toast.LENGTH_SHORT)
{
    Toast.makeText(this, msg, duration).show()

}


class Utils
{

    companion object{
        fun Bitmap_to_base64(imagebitmap: Bitmap): String {
            val baos = ByteArrayOutputStream()
            imagebitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos)
            val byteArray = baos.toByteArray()
            return Base64.encodeToString(byteArray, Base64.DEFAULT)
        }

        fun Base64_to_bitmap(image: String?, context: Context): Bitmap
        {
            image?.let {
                val decodedString: ByteArray = Base64.decode(image, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                return bitmap
            }
            val icon = BitmapFactory.decodeResource(context.resources, R.drawable.upload)
            return icon
        }

        fun getFileName(context: Context, uri: Uri?): String {
            val cursor = context.contentResolver.query(uri!!, null, null, null, null)
            cursor!!.moveToFirst()
            val name =
                cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME))
            cursor.close()
            return name
        }

        @Throws(FileNotFoundException::class)
        fun decodeUri(c: Context, uri: Uri?, requiredSize: Int): Bitmap? {
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            BitmapFactory.decodeStream(c.contentResolver.openInputStream(uri!!), null, o)
            var width_tmp = o.outWidth
            var height_tmp = o.outHeight
            var scale = 1
            while (true) {
                if (width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize) break
                width_tmp /= 2
                height_tmp /= 2
                scale *= 2
            }
            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            return BitmapFactory.decodeStream(c.contentResolver.openInputStream(uri), null, o2)
        }

    }
}
