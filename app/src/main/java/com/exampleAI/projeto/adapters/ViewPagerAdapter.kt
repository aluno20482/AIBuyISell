package com.exampleAI.projeto.adapters


import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.exampleAI.projeto.R
import java.util.*

//Adaptador para o slider de imagens presente na ProductDetailActivity
class ViewPagerAdapter(var context: Context, val imageList: List<Uri>) : PagerAdapter() {

    /**retorna o tamanho da lista*/
    override fun getCount(): Int {
        return imageList.size
    }

    /**retornar o objeto*/
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as RelativeLayout
    }

    /**inicializar o item e fazer o inflate do layout file*/
    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        //inicializar o inflater
        val mLayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        // fazer o inflate do image_slider_item.
        val itemView: View = mLayoutInflater.inflate(R.layout.image_slider_item, container, false)

        //buscar a view dentro do slider
        val imageView: ImageView = itemView.findViewById<View>(R.id.idIVImage) as ImageView

        Glide.with(context)
            .load(imageList[position])
            .into(imageView)

        //adicionar o item ao container.
        Objects.requireNonNull(container).addView(itemView)

        // devolver a item view.
        return itemView
    }

    /**apagar um item*/
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        //remover a view
        container.removeView(`object` as RelativeLayout)
    }
}
