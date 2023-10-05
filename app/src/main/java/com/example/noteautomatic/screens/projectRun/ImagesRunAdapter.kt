package com.example.noteautomatic.screens.projectRun

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.pdf.PdfRenderer
import android.util.DisplayMetrics
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

    @SuppressLint("ClickableViewAccessibility")
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
        if (image.countPages == -1) {
            Glide.with(holder.itemView.context)
                .load(image.resImage)
                .into(holder.binding.ivImageRun)
        } else {
            val fileDescriptor =
                holder.itemView.context.contentResolver.openFileDescriptor(image.resImage, "r")
            val renderer = fileDescriptor?.let { PdfRenderer(it) }
            val page = renderer?.openPage(image.countPages)

            val displayMetrics = DisplayMetrics()
            @Suppress("DEPRECATION")
            (holder.itemView.context as Activity).windowManager.defaultDisplay.getMetrics(
                displayMetrics
            )
            val screenWidth = displayMetrics.widthPixels
            val bitmap = page?.width?.let {
                Bitmap.createBitmap(
                    screenWidth, (screenWidth.toFloat() / it.toFloat() * page.height).toInt(),
                    Bitmap.Config.ARGB_8888
                )
            }
            val bounds = Rect(0, 0, bitmap?.width ?: 0, bitmap?.height ?: 0)
            bitmap?.let { page.render(it, bounds, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY) }
            holder.binding.ivImageRun.setImageBitmap(bitmap)
            page?.close()
            renderer?.close()
        }
    }

    class ImagesRunViewHolder(
        val binding: ItemRunImageBinding
    ) : RecyclerView.ViewHolder(binding.root)

}