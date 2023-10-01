package com.example.noteautomatic.screens.projectCreation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.noteautomatic.R
import com.example.noteautomatic.databinding.ItemImageBinding
import com.example.noteautomatic.foundation.classes.Image
import com.example.noteautomatic.screens.ImageDiffCallback


interface ImageActionListener {

}

class ImagesAdapter(private val actionListener: ImageActionListener) :
    RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder>(), View.OnClickListener {

    var images: List<Image> = emptyList()
        set(newValue) {
            val diffCallback = ImageDiffCallback(field, newValue)
            val diffResult =
                DiffUtil.calculateDiff(diffCallback)
            field = newValue
            diffResult.dispatchUpdatesTo(this)
        }


    override fun onClick(v: View?) {
        val image = v?.tag as Image
        when (v.id) {
            R.id.btnDelete -> {

            }

            else -> {

            }
        }
    }

    override fun getItemCount(): Int = images.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemImageBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)
        binding.btnDelete.setOnClickListener(this)

        return ImagesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        val image = images[position]
        holder.itemView.tag = image
        with(holder.binding) {
            btnDelete.tag = image
        }
    }

    class ImagesViewHolder(
        val binding: ItemImageBinding
    ) : RecyclerView.ViewHolder(binding.root)

}