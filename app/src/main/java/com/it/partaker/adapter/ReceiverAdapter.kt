package com.it.partaker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.it.partaker.ItemClickListener.MyRequestsClickListener
import com.it.partaker.R
import com.it.partaker.models.Request
import kotlinx.android.synthetic.main.rv_mrf_receiver_item.view.*

class ReceiverAdapter(val context: Context, private val requestItemClickListener: MyRequestsClickListener):RecyclerView.Adapter<ReceiverAdapter.ReceiverViewHolder>()
{
    class ReceiverViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private var receiverList = mutableListOf<Request>()

    fun setRequests(request: MutableList<Request>){
        receiverList = request
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiverViewHolder {
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.rv_mrf_receiver_item, parent, false)
        return ReceiverViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReceiverViewHolder, position: Int) {
        val requests = receiverList[position]

        val textView = holder.itemView.tvRVMRFReceiverName
        textView.text = requests.getName()

        val textView2 = holder.itemView.tvRVMRFReceiverDesc
        textView2.text = requests.getDesc()

        Glide.with(context)
            .load(requests.getImage())
            .transform(CircleCrop())
            .placeholder(R.drawable.default_profile_pic)
            .into(holder.itemView.ivRVMRFReceiverItem)

        holder.itemView.setOnClickListener {

            requestItemClickListener.OnMyRequestsItemClickListener(it, requests)

        }
    }

    override fun getItemCount(): Int {
        return receiverList.size
    }
}