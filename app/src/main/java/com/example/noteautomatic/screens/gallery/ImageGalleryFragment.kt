package com.example.noteautomatic.screens.gallery
//
//import android.content.Context
//import android.net.Uri
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import androidx.core.content.ContentProviderCompat.requireContext
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.noteautomatic.R
//
//class ImageGalleryAdapter(private val context: Context, private val uriList: List<Uri>) : RecyclerView.Adapter<ImageGalleryAdapter.ViewHolder>() {
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val imageView: ImageView = itemView.findViewById(R.id.imageView)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(context).inflate(R.layout.item_image_gallery, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val uri = uriList[position]
//        showImageFromUri(uri, holder.imageView)
//    }
//
//    override fun getItemCount(): Int {
//        return uriList.size
//    }
//}
