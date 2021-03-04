package smsinfosolutions.ind.hibl.registration

import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import smsinfosolutions.ind.hibl.databinding.ActivityImageViewerBinding
import smsinfosolutions.ind.hibl.utilities.Utils


class ImageViewerActivity : AppCompatActivity() {
    lateinit var binding:ActivityImageViewerBinding
    private var mScaleGestureDetector: ScaleGestureDetector? = null
    private var mScaleFactor = 1.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityImageViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val file=intent.getStringExtra("file")
        binding.fullScreenImageView.setImageBitmap(Utils.Base64_to_bitmap(file, this))

        mScaleGestureDetector = ScaleGestureDetector(this, ScaleListener())
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mScaleGestureDetector?.onTouchEvent(event);
        return true;
    }


    private inner class ScaleListener : SimpleOnScaleGestureListener() {
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            mScaleFactor *= scaleGestureDetector.scaleFactor
            mScaleFactor = Math.max(
                0.1f,
                Math.min(mScaleFactor, 10.0f)
            )
            binding.fullScreenImageView.setScaleX(mScaleFactor)
            binding.fullScreenImageView.setScaleY(mScaleFactor)
            return true
        }
    }
}