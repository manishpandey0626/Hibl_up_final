package smsinfosolutions.ind.hibl.claim

/**
 * Created by Manish on 01-Mar-21.
 */

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import smsinfosolutions.ind.hibl.utilities.ClaimDetail
import smsinfosolutions.ind.hibl.databinding.ItemTimelineBinding



class ClaimStatusDetailAdapter(private val mFeedList: List<ClaimDetail>) :
    RecyclerView.Adapter<ClaimStatusDetailAdapter.TimeLineViewHolder>() {

    private lateinit var mLayoutInflater: LayoutInflater

/*    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, itemCount)
    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineViewHolder {

        val binding =
            ItemTimelineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        //   val view = LayoutInflater.from(parent.context).inflate(R.layout.payment_item, parent, false)
        return TimeLineViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {

        return holder.bind(mFeedList[position])
    }

/*    private fun setMarker(holder: TimeLineViewHolder, drawableResId: Int, colorFilter: Int) {
          holder.timeline.marker = VectorDrawableUtils.getDrawable(holder.itemView.context, drawableResId, ContextCompat.getColor(holder.itemView.context, colorFilter))
    }*/

    override fun getItemCount() = mFeedList.size

    inner class TimeLineViewHolder(val binding: ItemTimelineBinding) :
        RecyclerView.ViewHolder(binding.root) {


        /*   when {
               timeLineModel.status == OrderStatus.INACTIVE -> {
                   setMarker(holder, R.drawable.ic_marker_inactive, R.color.material_grey_500)
               }
               timeLineModel.status == OrderStatus.ACTIVE -> {
                   setMarker(holder, R.drawable.ic_marker_active, R.color.material_grey_500)
               }
               else -> {
                   setMarker(holder, R.drawable.ic_marker, R.color.material_grey_500)
               }
           }*/


        fun bind(data: ClaimDetail) {

            binding.timeline.initLine(0)

            binding.textTimelineDate.text = data.end_dt
            binding.textTimelineTitle.text = data.status
            binding.remark.text = data.remark
       //     binding.timeline.marker = VectorDrawableUtils.getDrawable(holder.itemView.context, drawableResId, ContextCompat.getColor(holder.itemView.context, colorFilter))

        }
    }

}