package com.example.shweta.echo2.Fragment


import android.app.Activity
import android.os.Bundle
import android.app.FragmentManager
import android.content.Context
import android.media.MediaPlayer
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.shweta.echo2.Adaptor.FavoriteAdaptor
import com.example.shweta.echo2.R
import com.example.shweta.echo2.Songs
import com.example.shweta.echo2.databases.EchoDatabase


/**
 * A simple [Fragment] subclass.
 */
class FavouriteFragment : Fragment() {
    var myActivity: Activity? = null
    var getSongsList: ArrayList<Songs>? = null
    var noFavorite: TextView? = null
    var nowPlayingBottomBar: RelativeLayout? = null
    var songTitle: TextView? = null
    var recyclerView: RecyclerView? = null
    var playPauseButton: ImageButton? = null
    var trackPosition: Int=0
    var favoriteContent:EchoDatabase?=null
    var refreshList :ArrayList<Songs>?=null
    var getListFromDatabase:ArrayList<Songs>?=null
    object Statified{
        var mediaPlayer :MediaPlayer?=null
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_favourite, container, false)
        activity.title="Favourites"
        noFavorite = view?.findViewById(R.id.noFavourite)
        nowPlayingBottomBar = view?.findViewById(R.id.hiddenBarFavScreen)
        songTitle = view?.findViewById(R.id.songTitleFavScreen)
        playPauseButton = view?.findViewById(R.id.playPauseButtonFav)
        recyclerView = view?.findViewById(R.id.favoriteRecycler)
        return view
    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        myActivity = context as Activity
    }
    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        myActivity = activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        favoriteContent = EchoDatabase(myActivity)
        getSongsList = getSongsFromPhone()
        if (getSongsList == null) {
            recyclerView?.visibility = View.INVISIBLE
            noFavorite?.visibility = View.VISIBLE
        } else {
            var favoriteAdaptor = FavoriteAdaptor(getSongsList as ArrayList<Songs>, myActivity as Context)
            val mLayoutManager = LinearLayoutManager(activity)
            recyclerView?.layoutManager = mLayoutManager
            recyclerView?.itemAnimator = DefaultItemAnimator()
            recyclerView?.adapter = favoriteAdaptor
            recyclerView?.setHasFixedSize(true)
        }
        bottomBarSetup()
        displayFavoritesBySeaching()
    }
     override fun onResume() {
        super.onResume()
    }
    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        var item = menu?.findItem(R.id.action_sort)
        item?.isVisible=false
    }

    fun getSongsFromPhone(): ArrayList<Songs> {
        var arrayList = ArrayList<Songs>()
        var contentResolver = myActivity?.contentResolver
        var songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        var songCursor = contentResolver?.query(songUri, null, null, null, null)
        if (songCursor != null && songCursor.moveToFirst()) {
            val songId = songCursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val songData = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA)
            val dateIndex = songCursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED)
            while (songCursor.moveToNext()) {
                var currentId = songCursor.getLong(songId)
                var currentTitle = songCursor.getString(songTitle)
                var currentArtist = songCursor.getString(songArtist)
                var currentData = songCursor.getString(songData)
                var currentDate = songCursor.getLong(dateIndex)
                arrayList.add(Songs(currentId, currentTitle, currentArtist, currentData, currentDate))
            }
        }
        return arrayList
    }

    fun bottomBarSetup() {
        try {
            bottomBarClickHandler()
            songTitle?.setText(SongPlayingFragment.Statified.currentSongHelper?.songTitle)
            SongPlayingFragment.Statified.mediaplayer?.setOnCompletionListener({
                songTitle?.setText(SongPlayingFragment.Statified.currentSongHelper?.songTitle)
                SongPlayingFragment.Staticated.onSongComplete()


            })
            if(SongPlayingFragment.Statified.mediaplayer?.isPlaying as Boolean){
                nowPlayingBottomBar?.visibility = View.VISIBLE
            }else{
                nowPlayingBottomBar?.visibility=View.INVISIBLE
            }
        }
    catch (e: Exception) {
        e.printStackTrace()
    }
    }
    fun bottomBarClickHandler(){
        nowPlayingBottomBar?.setOnClickListener({
            Statified.mediaPlayer=SongPlayingFragment.Statified.mediaplayer


            val songPlayingFragment = SongPlayingFragment()
            var args = Bundle()
            args.putString("songArtist", SongPlayingFragment.Statified?.currentSongHelper?.songArtist)
            args.putString("songTitle", SongPlayingFragment.Statified?.currentSongHelper?.songTitle)
            args.putString("path", SongPlayingFragment.Statified?.currentSongHelper?.songPath)
            args.putInt("songId", SongPlayingFragment.Statified?.currentSongHelper?.songId?.toInt() as Int)
            args.putInt("songPosition", SongPlayingFragment.Statified?.currentSongHelper?.currentPosition?.toInt() as Int)
            args.putParcelableArrayList("songData", SongPlayingFragment.Statified.fetchSongs)

            args.putString("FavBottomBar","success")
            songPlayingFragment.arguments = args
            fragmentManager.beginTransaction()
                    .replace(R.id.details_fragment,songPlayingFragment)
                    .addToBackStack("songPlayingFragment")
                    .commit()

        })
        playPauseButton?.setOnClickListener({
            if(SongPlayingFragment.Statified.mediaplayer?.isPlaying as Boolean){
                SongPlayingFragment.Statified.mediaplayer?.pause()
                trackPosition = SongPlayingFragment.Statified.mediaplayer?.getCurrentPosition() as Int
                playPauseButton?.setBackgroundResource(R.drawable.play_icon)

            }else{
                SongPlayingFragment.Statified.mediaplayer?.seekTo(trackPosition)
                SongPlayingFragment.Statified.mediaplayer?.start()
                playPauseButton?.setBackgroundResource(R.drawable.pause_icon)
            }
        })
    }
    fun displayFavoritesBySeaching(){
        if(favoriteContent?.checkSize() as Int >0 ){
            refreshList=ArrayList<Songs>()
            getListFromDatabase=favoriteContent?.queryDBList()
            var fetchListFromDevice = getSongsFromPhone()
            if(fetchListFromDevice!=null){
                for(i in 0..fetchListFromDevice?.size -1){
                    for(j in 0..getListFromDatabase?.size as Int -1){
                        if((getListFromDatabase?.get(j)?.songID)==(fetchListFromDevice?.get(i)?.songID)){
                            refreshList?.add((getListFromDatabase as ArrayList<Songs>)[j])
                        }

                    }
                }
            }
            if(refreshList==null) {
                recyclerView?.visibility=View.INVISIBLE
                noFavorite?.visibility =View.VISIBLE
            }else
             {
                var favoriteAdapter = FavoriteAdaptor(refreshList as ArrayList<Songs>,myActivity as Context)
                val mLayoutManager = LinearLayoutManager(activity)
                recyclerView?.layoutManager = mLayoutManager
                recyclerView?.itemAnimator = DefaultItemAnimator()
                recyclerView?.adapter = favoriteAdapter
                recyclerView?.setHasFixedSize(true)
            }
        } else {
            recyclerView?.visibility = View.INVISIBLE
            noFavorite?.visibility = View.VISIBLE
        }

            }
            }


// Required empty public constructor
