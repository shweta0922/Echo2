package com.example.shweta.echo2.databases

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.shweta.echo2.Fragment.SongPlayingFragment
import com.example.shweta.echo2.Songs

/**
 * Created by Shweta on 1/10/2018.
 */
class EchoDatabase: SQLiteOpenHelper{
    var songList = ArrayList<Songs>()



    object staticated{
        var DB_VERSION =1
        val DB_NAME = "FavouriteDatabases"
        val TABLE_NAME="FavoriteTable"
        val COLUMN_ID="SongID"
        val COLUMN_SONG_TITLE="SongTitle"
        val COLUMN_SONG_ARTIST="SongArtist"
        val COLUMN_SONG_PATH="SongPath"

    }
    override fun onCreate(sqliteDatabases: SQLiteDatabase?) {
        sqliteDatabases?.execSQL("CREATE TABLE " +staticated.TABLE_NAME + " ( " + staticated.COLUMN_ID + " INTEGER," + staticated.COLUMN_SONG_ARTIST + " STRING," +
                staticated.COLUMN_SONG_TITLE + " STRING," + staticated.COLUMN_SONG_PATH + " STRING);")

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    constructor(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : super(context, name, factory, version)
    constructor(context: Context?) : super(context, staticated.DB_NAME ,null,staticated.DB_VERSION)

    fun storeAsFavorite(id: Int?,artist: String? , songTitle: String?, path: String?){
                val db = this.writableDatabase
                var contentValues = ContentValues()
                contentValues.put(staticated.COLUMN_ID,id)
                contentValues.put(staticated.COLUMN_SONG_ARTIST,artist)
                contentValues.put(staticated.COLUMN_SONG_TITLE,songTitle)
                contentValues.put(staticated.COLUMN_SONG_PATH,path)
                db.insert(staticated.TABLE_NAME,null,contentValues)
                db.close()
    }

    fun queryDBList(): ArrayList<Songs>?{
        try{

        val db =this.readableDatabase
        val query_params = "SELECT * FROM " + staticated.TABLE_NAME
        var cSor = db.rawQuery(query_params,null)
        if (cSor.moveToFirst()){
            do{
                var id=cSor.getInt(cSor.getColumnIndexOrThrow(staticated.COLUMN_ID))
                var artist=cSor.getString(cSor.getColumnIndexOrThrow(staticated.COLUMN_SONG_ARTIST))
                var title=cSor.getString(cSor.getColumnIndexOrThrow(staticated.COLUMN_SONG_TITLE))
                var songPath=cSor.getString(cSor.getColumnIndexOrThrow(staticated.COLUMN_SONG_PATH))
                songList.add(Songs(id.toLong(),title,artist,songPath,0))
            }while (cSor.moveToNext())
        }else{
            return null
        }
    }catch (e: Exception){
        e.printStackTrace()
        }
        return songList
    }
    fun checkifIdExists(_id: Int): Boolean{
        var storeId=-1010
        val db=this.readableDatabase
        val query_params="SELECT * FROM " + staticated.TABLE_NAME + " WHERE SongId = '$_id'"
        val cSor =db.rawQuery(query_params,null)
        if(cSor.moveToFirst()){
            do{
                storeId = cSor.getInt(cSor.getColumnIndexOrThrow(staticated.COLUMN_ID))
            }while (cSor.moveToNext())
        }else{
            return false
        }
        return storeId !=-1010
    }
    fun deleteFavourite(_id:Int){
        val db=this.writableDatabase
        db.delete(staticated.TABLE_NAME,staticated.COLUMN_ID + "=" + _id,null)
        db.close()
    }
    fun checkSize():Int{
        var counter=0
        val db = this.readableDatabase
        val query_params ="SELECT * FROM " + staticated.TABLE_NAME
        val cSor = db.rawQuery(query_params,null)
        if(cSor.moveToFirst()){
            do{
                counter =counter+1

            }while (cSor.moveToNext())
        }else{
            return 0
        }
        return counter
    }
}
