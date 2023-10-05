package com.example.noteautomatic.screens.projectCreation

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.noteautomatic.R
import com.example.noteautomatic.databinding.ItemImageBinding
import com.example.noteautomatic.foundation.database.entities.Image
import com.example.noteautomatic.screens.ImageDiffCallback
import java.io.File


interface ImageActionListener {

    fun deleteImage(image: Image)

    fun onItemMove(fromPosition: Int, toPosition: Int)

}

class ItemTouchHelperCallback(private val adapter: ImagesAdapter) : ItemTouchHelper.Callback() {
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.START or ItemTouchHelper.END
        val swipeFlags = 0
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        adapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

}

class ImagesAdapter(private val actionListener: ImageActionListener) :
    RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder>(), View.OnClickListener {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return images[position].hashCode().toLong()
    }


    fun onItemMove(fromPosition: Int, toPosition: Int) {
        actionListener.onItemMove(fromPosition, toPosition)
    }

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
        if (v.id == R.id.btnDelete) {
            actionListener.deleteImage(image)
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
            if (image.countPages == -1) {
                Glide.with(holder.itemView.context)
                    .load(image.resImage)
                    .into(ivImage)
            } else {
                tvIsFile.visibility = View.VISIBLE
                tvIsFile.text = "file"
                val fileDescriptor = holder.itemView.context.contentResolver.openFileDescriptor(image.resImage, "r")
                val renderer = fileDescriptor?.let { PdfRenderer(it) }
                val firstPage = renderer?.openPage(0)
                val bitmap =
                    firstPage?.width?.let { firstPage.height.let { it1 ->
                        Bitmap.createBitmap(it,
                            it1, Bitmap.Config.ARGB_8888)
                    } }
                bitmap?.let { firstPage.render(it, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY) }
                ivImage.setImageBitmap(bitmap)
                firstPage?.close()
                renderer?.close()
            }
        }
    }

    class ImagesViewHolder(
        val binding: ItemImageBinding
    ) : RecyclerView.ViewHolder(binding.root)

}