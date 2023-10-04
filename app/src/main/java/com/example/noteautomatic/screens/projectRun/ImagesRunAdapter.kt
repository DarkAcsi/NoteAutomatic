package com.example.noteautomatic.screens.projectRun

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.noteautomatic.databinding.ItemRunImageBinding
import com.example.noteautomatic.foundation.database.entities.Image
import com.example.noteautomatic.screens.ImageDiffCallback


interface ImagesRunListener {

    fun setPauseScroll()

}

class ImagesRunAdapter(private val actionListener: ImagesRunListener) :
    RecyclerView.Adapter<ImagesRunAdapter.ImagesRunViewHolder>(), View.OnTouchListener {

    var images: List<Image> = emptyList()
        set(newValue) {
            val diffCallback = ImageDiffCallback(field, newValue)
            val diffResult =
                DiffUtil.calculateDiff(diffCallback)
            field = newValue
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            actionListener.setPauseScroll()
        }
        return true
    }

    override fun getItemCount(): Int = images.size

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesRunViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRunImageBinding.inflate(inflater, parent, false)

        binding.ivImageRun.setOnTouchListener(this)

        return ImagesRunViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImagesRunViewHolder, position: Int) {
        val image = images[position]
        holder.itemView.tag = image
        holder.binding.ivImageRun.tag = image
        Glide.with(holder.itemView.context)
                    .load(image.resImage)
                    .into(holder.binding.ivImageRun)
    }

    class ImagesRunViewHolder(
        val binding: ItemRunImageBinding
    ) : RecyclerView.ViewHolder(binding.root)

}