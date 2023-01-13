package com.wnyaonjr.movieexplorer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.wnyaonjr.movieexplorer.databinding.ActivityMoviesBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Parent container of the movie views
 */
@AndroidEntryPoint
class MoviesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMoviesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movies)
    }
}