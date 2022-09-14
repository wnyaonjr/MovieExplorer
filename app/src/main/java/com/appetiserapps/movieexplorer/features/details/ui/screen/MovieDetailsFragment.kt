package com.appetiserapps.movieexplorer.features.details.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.appetiserapps.movieexplorer.R
import com.appetiserapps.movieexplorer.databinding.FragmentMovieDetailsBinding
import com.appetiserapps.movieexplorer.features.list.domain.Movie
import com.appetiserapps.movieexplorer.features.list.ui.screen.HeaderLayout
import com.appetiserapps.movieexplorer.features.list.ui.screen.MovieInitialDetails
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private lateinit var binding: FragmentMovieDetailsBinding
    private val viewModel: MovieDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentMovieDetailsBinding.inflate(inflater, container, false).apply {
        binding = this

        composeView.setContent {
            MaterialTheme {
                MovieDetailsLayout(viewModel)
            }
        }
    }.root
}

@Composable
fun MovieDetailsLayout(viewModel: MovieDetailsViewModel) {
    val movie by viewModel.movie.observeAsState()

    Column(
        modifier = Modifier.background(colorResource(R.color.gray))
            .verticalScroll(rememberScrollState())
    ) {
        HeaderLayout(R.string.movie_details)
        MovieDetailsLayout(
            movie = movie,
            onFavoriteClickListener = viewModel::onFavoriteClick
        )
    }
}

@Composable
fun MovieDetailsLayout(
    movie: Movie?,
    onFavoriteClickListener: ((trackId: Int, favorite: Boolean) -> Unit)? = null,
) {
    if (movie != null) {

        val interactionSource = MutableInteractionSource()
        val favoriteState = remember { mutableStateOf(true) }
        favoriteState.value = movie.favorite

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                MovieInitialDetails(
                    movie,
                    favoriteState,
                    interactionSource,
                    onFavoriteClickListener
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = movie.longDescription.orEmpty(),
                    style = MaterialTheme.typography.subtitle1,
                )
            }

        }
    }
}

@Preview
@Composable
fun MovieDetailsLayoutPreview() {
    MaterialTheme {
        MovieDetailsLayout(
            movie = Movie(
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