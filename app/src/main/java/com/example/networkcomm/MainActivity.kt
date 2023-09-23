package com.example.networkcomm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

import com.github.kittinunf.fuel.httpGet

import com.github.kittinunf.fuel.gson.responseObject

import com.github.kittinunf.result.Result



// use a data class matching our JSON, to allow us to use GSON
data class Song (val ID: Int, val title: String, val artist: String, val year: Int, val chart: Int, val likes: Int, val downloads: Int, val review: String, val quantity: Int)


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tv1 = findViewById<TextView>(R.id.tv1)
        tv1.movementMethod = ScrollingMovementMethod()
        // 3 - use GSON

        val btn1 = findViewById<Button>(R.id.btn1)
        btn1.setOnClickListener {

            val artist = findViewById<EditText>(R.id.etArtist).text.toString()
            val url = "http://10.0.2.2:3000/artist/${artist}"

            // Use Fuel GSON to convert the JSON into a list of Songs
            url.httpGet().responseObject<List<Song>> { request, response, result ->

                when (result) {
                    is Result.Success -> {
                        // Get the array of results, this will be a list of Song objects
                        // Map each Song object in the list to a string containing the title, artist and year
                        // and then join together each entry in the output list into one big string
                        val text =
                            result.get().map { "${it.title} by ${it.artist}, year ${it.year}" }
                                .joinToString("\n")

                        // Set the contents of the text view to this string
                        tv1.text = text
                    }

                    is Result.Failure -> {
                        // is failure if HTTP error
                        Log.d("fuel", result.getException().message ?: "Unknown error")
                        tv1.text = "ERROR ${result.error}"
                    }
                }

            }
        }

    }
}