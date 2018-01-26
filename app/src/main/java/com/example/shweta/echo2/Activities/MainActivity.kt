package com.example.shweta.echo2.Activities

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.NotificationCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import com.example.shweta.echo2.Adaptor.NavigationDrawerAdaptor
import com.example.shweta.echo2.Fragment.MainScreenFragment
import com.example.shweta.echo2.Fragment.SongPlayingFragment
import com.example.shweta.echo2.R


class MainActivity : AppCompatActivity() {
    var navigationDrawerIconList: ArrayList<String> = arrayListOf()
    var trackNotificationBuilder: Notification?= null
    var images_for_navdrawer :IntArray = intArrayOf(R.drawable.navigation_allsongs,
            R.drawable.navigation_favorites, R.drawable.navigation_settings,R.drawable.navigation_aboutus)
    object Statified{
        var drawerLayout : DrawerLayout?=null
        var notificationManager: NotificationManager?= null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        MainActivity.Statified.drawerLayout = findViewById(R.id.drawer_layout)
        navigationDrawerIconList.add("All Songs")
        navigationDrawerIconList.add("Favorites")
        navigationDrawerIconList.add("Settings")
        navigationDrawerIconList.add("AboutUs")

        var toggle = ActionBarDrawerToggle(this@MainActivity,MainActivity.Statified.drawerLayout,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        MainActivity.Statified.drawerLayout?.setDrawerListener(toggle)
        toggle.syncState()

        val mainScreenFragment = MainScreenFragment()
        this.supportFragmentManager
                .beginTransaction()
                .add(R.id.details_fragment, mainScreenFragment,"MainScreenFragment")
                .commit()

        var _navAdapter = NavigationDrawerAdaptor(navigationDrawerIconList,images_for_navdrawer,this)
        _navAdapter.notifyDataSetChanged()
        var nav_recycler_view = findViewById<RecyclerView>(R.id.nav_recycler_view)
        nav_recycler_view.layoutManager = LinearLayoutManager(this)
        nav_recycler_view.itemAnimator = DefaultItemAnimator()
        nav_recycler_view.adapter = _navAdapter
        nav_recycler_view.setHasFixedSize(true)

        val intent = Intent(this@MainActivity,MainActivity :: class.java)
        val pIntent = PendingIntent.getActivity(this@MainActivity,
                System.currentTimeMillis().toInt() , intent,PendingIntent.FLAG_UPDATE_CURRENT)
        trackNotificationBuilder = NotificationCompat.Builder(this@MainActivity,"channel")
                .setContentTitle("a track is playing in background")
                .setSmallIcon(R.drawable.echo_logo)
                .setContentIntent(pIntent)
                .setOngoing(true)
                .setAutoCancel(true)
                .build()

        Statified.notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
    override fun onStart(){
        super.onStart()
        try{
            Statified.notificationManager?.cancel(2000)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun onStop() {
        super.onStop()
        try{
            if(SongPlayingFragment.Statified.mediaplayer?.isPlaying as Boolean){
                Statified.notificationManager?.notify(2000, trackNotificationBuilder)
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        try{
            Statified.notificationManager?.cancel(2000)
        }catch (e: Exception){
            e.printStackTrace()
        }

    }
}
