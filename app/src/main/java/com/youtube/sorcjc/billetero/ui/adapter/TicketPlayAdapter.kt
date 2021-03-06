package com.youtube.sorcjc.billetero.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import com.youtube.sorcjc.billetero.R
import com.youtube.sorcjc.billetero.model.TicketPlay
import kotlinx.android.synthetic.main.item_ticket_play.view.*


class TicketPlayAdapter (
        private var mDataSet: ArrayList<TicketPlay> = ArrayList(),
        private val allowRemoveItem: Boolean
)
    : RecyclerView.Adapter<TicketPlayAdapter.ViewHolder>() {

    fun addPlay(ticketPlay: TicketPlay) {
        mDataSet.add(ticketPlay)
        notifyItemInserted(mDataSet.size - 1)
    }

    fun setDataSet(plays: ArrayList<TicketPlay>) {
        mDataSet = plays
        notifyDataSetChanged()
    }

    fun clear() {
        mDataSet.clear();
        notifyDataSetChanged();
    }

    fun getPlays(): ArrayList<TicketPlay> {
        return mDataSet;
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setTexts(ticketPlay: TicketPlay) = with (itemView) {
            tvNumber.text = ticketPlay.number.toString()
            tvPoints.text = ticketPlay.points.toString()
            tvType.text = ticketPlay.type

        }

        fun setDeleteButton(removeItem: Boolean, onClick: (View)->Unit) = with (itemView) {
            if (removeItem) {
                ibDelete.visibility = View.VISIBLE
                ibDelete.setOnClickListener(onClick)
            } else {
                ibDelete.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_ticket_play, parent, false) as LinearLayout

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ticketPlay = mDataSet[position]

        holder.setTexts(ticketPlay)

        holder.setDeleteButton(allowRemoveItem) {
            mDataSet.remove(ticketPlay)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount() = mDataSet.size
}