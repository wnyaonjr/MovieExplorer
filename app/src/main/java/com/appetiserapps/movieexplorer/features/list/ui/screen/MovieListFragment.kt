package com.appetiserapps.movieexplorer.features.list.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.appetiserapps.movieexplorer.R
import com.appetiserapps.movieexplorer.databinding.FragmentMovieListBinding
import com.appetiserapps.movieexplorer.features.list.domain.Movie
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieListFragment : Fragment() {

    private lateinit var binding: FragmentMovieListBinding
    private val viewModel: MovieListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentMovieListBinding.inflate(inflater, container, false).apply {
        binding = this

        composeView.setContent {
            MaterialTheme {
                MovieListLayout(viewModel)
            }
        }
    }.root
}

@Composable
fun MovieListLayout(viewModel: MovieListViewModel) {
    val movies by viewModel.movies.observeAsState()

    MovieListLayout(
        movies = movies
    )
}

@Composable
fun MovieListLayout(movies: List<Movie>?) {
    Column {
        MovieList(
            movies = movies
        )
    }
}

@Composable
fun MovieList(movies: List<Movie>?) {
    if (!movies.isNullOrEmpty()) {
        LazyColumn {
            items(movies) { movie ->
                MovieItem(movie)
            }
        }
    } else {

    }
}

@Composable
fun MovieItem(movie: Movie) {
    Card {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                GlideImage(
                    modifier = Modifier.size(48.dp),
                    imageModel = movie.artworkUrl100,
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = movie.trackName,
                        style = MaterialTheme.typography.h6,
                    )
                    Text(
                        text = movie.primaryGenreName,
                        style = MaterialTheme.typography.subtitle1,
                    )
                    Text(
                        text = stringResource(R.string.price, movie.trackPrice.toString()),
                        style = MaterialTheme.typography.subtitle1,
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Divider(
                color = colorResource(R.color.divider),
                thickness = 0.5.dp,
            )
        }

    }


}

@Preview
@Composable
fun MovieItemPreview() {
    MaterialTheme {
        MovieItem(
            Movie(
                trackId = 1437031362,
                trackName = "A Star Is Born (2018)",
                artworkUrl100 = "https://is1-ssl.mzstatic.com/image/thumb/Video115/v4/a2/26/fd/a226fd77-c80b-5ee7-e40f-6a0222e1645d/pr_source.jpg/100x100bb.jpg",
                trackPrice = 14.99,
                longDescription = "Seasoned musician Jackson Maine (Bradley Cooper) discovers—and falls in love with—struggling artist Ally (Lady Gaga). She has just about given up on her dream to make it big as a singer… until Jack coaxes her into the spotlight. But even as Ally’s career takes off, the personal side of their relationship is breaking down, as Jack fights an ongoing battle with his own internal demons.",
                primaryGenreName = "Romance",
                favorite = false
            )
        )
    }
}