package com.example.shweta.echo2.Adaptor

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.app.FragmentManager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.shweta.echo2.Activities.MainActivity
import com.example.shweta.echo2.Fragment.AboutUsFragment
import com.example.shweta.echo2.Fragment.FavouriteFragment
import com.example.shweta.echo2.Fragment.MainScreenFragment
import com.example.shweta.echo2.Fragment.SettingsFragment
import com.example.shweta.echo2.R

/**
 * Created by Shweta on 1/7/2018.
 */
class NavigationDrawerAdaptor(_contentList: ArrayList<String> ,_getImages: IntArray,_context: Context) : RecyclerView.Adapter<NavigationDrawerAdaptor.NavViewHolder>(){
    var contentList: ArrayList<String>?=null
    var getImages: IntArray?=null
    var mContext: Context?=null
    init{
        this.contentList = _contentList
        this.getImages = _getImages
        this.mContext = _context
    }

    override fun onBindViewHolder(holder: NavViewHolder?, position: Int) {

            holder?.text_GET?.setText(contentList?.get(position))
        holder?.icon_GET?.setBackgroundResource(getImages?.get(position) as Int)
        holder?.contentHolder?.setOnClickListener({
            if(position == 0){
                val mainScreenFragment = MainScreenFragment()
                (mContext as MainActivity).supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.details_fragment, mainScreenFragment,"MainScreenNav")
                        .addToBackStack("MainScreenNav")
                        .commit()
            }
            else if(position == 1){
                val favouriteFragment = FavouriteFragment()
                (mContext as MainActivity).supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.details_fragment, favouriteFragment,"FavoriteNav")
                        .addToBackStack("FavoriteNav")
                        .commit()
            }
            else if(position==2){
                val settingsFragment = SettingsFragment()
                (mContext as MainActivity).supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.details_fragment, settingsFragment,"SettingNav")
                        .addToBackStack("SettingNav")
                        .commit()
            }
            else {
                val aboutUsFragment = AboutUsFragment()
                (mContext as MainActivity).supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.details_fragment, aboutUsFragment,"AboutUsNav")
                        .addToBackStack("AboutUsNav")
                        .commit()
            }
            MainActivity.Statified.drawerLayout?.closeDrawers()
        })
    }

    override fun getItemCount(): Int {
        return contentList?.size as Int
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): NavViewHolder {
           var itemView = LayoutInflater.from(parent?.context)
                    .inflate(R.layout.row_custom_navigationdrawer,parent,false)
            var returnThis = NavViewHolder(itemView)
            return returnThis

    }

    class NavViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
            var icon_GET: ImageView?=null
            var text_GET: TextView?=null
            var contentHolder: RelativeLayout?=null
            init{
                icon_GET = itemView?.findViewById(R.id.icon_navdrawer)
                text_GET = itemView?.findViewById(R.id.text_navdrawer)
                contentHolder = itemView?.findViewById(R.id.navdrawer_item_content_holder)
            }
    }

}