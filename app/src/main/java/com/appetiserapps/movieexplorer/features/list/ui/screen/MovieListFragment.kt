package com.appetiserapps.movieexplorer.features.list.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    private fun initObservers() {
        viewModel.navigateToMovieDetails.observe(viewLifecycleOwner) {
            navigateToMovieDetails(it)
        }
    }

    private fun navigateToMovieDetails(trackId: Int) {
        findNavController().navigate(
            MovieListFragmentDirections.actionMovieListFragmentToMovieDetailsFragment(
                trackId
            )
        )
    }
}

@Composable
fun MovieListLayout(viewModel: MovieListViewModel) {
    val movies by viewModel.movies.observeAsState()
    val trackName by viewModel.trackName.observeAsState()

    MovieListLayout(
        movies = movies,
        trackName = trackName,
        onFavoriteClickListener = viewModel::onFavoriteClick,
        onMovieClickListener = viewModel::onMovieClick,
        searchListener = viewModel::onSearch
    )
}

@Composable
fun MovieListLayout(
    movies: List<Movie>?,
    trackName: String?,
    onFavoriteClickListener: ((trackId: Int, favorite: Boolean) -> Unit)? = null,
    onMovieClickListener: ((trackId: Int) -> Unit)? = null,
    searchListener: ((query: String) -> Unit)? = null
) {

    var text by remember { mutableStateOf(TextFieldValue(trackName.orEmpty())) }

    Column(modifier = Modifier.background(colorResource(R.color.gray))) {
        OutlinedTextField(
            value = text,
            singleLine = true,
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            label = { Text(text = stringResource(R.string.movie_name)) },
            onValueChange = {
                text = it
                searchListener?.invoke(text.text)
            }
        )
        MovieList(
            movies = movies,
            onFavoriteClickListener = onFavoriteClickListener,
            onMovieClickListener = onMovieClickListener
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MovieList(
    movies: List<Movie>?,
    onFavoriteClickListener: ((trackId: Int, favorite: Boolean) -> Unit)? = null,
    onMovieClickListener: ((trackId: Int) -> Unit)? = null
) {
    if (!movies.isNullOrEmpty()) {
        LazyColumn {
            items(movies) { movie ->
                MovieItem(
                    movie = movie,
                    onFavoriteClickListener = onFavoriteClickListener,
                    onMovieClickListener = onMovieClickListener,
                    modifier = Modifier.animateItemPlacement()
                )
            }
        }
    } else {
        NoResultFoundDisplay()
    }
}

@Composable
fun MovieItem(
    modifier: Modifier = Modifier,
    movie: Movie,
    onFavoriteClickListener: ((trackId: Int, favorite: Boolean) -> Unit)? = null,
    onMovieClickListener: ((trackId: Int) -> Unit)? = null,
) {
    val interactionSource = MutableInteractionSource()
    val favoriteState = remember { mutableStateOf(true) }
    favoriteState.value = movie.favorite

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onMovieClickListener?.invoke(movie.trackId)
            }
    ) {
        Column(modifier = Modifier.padding(8.dp)) {

            Text(
                text = movie.trackName,
                style = MaterialTheme.typography.h6,
            )

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

                Column(modifier = Modifier.weight(1f, fill = true)) {
                    Text(
                        text = stringResource(R.string.genre, movie.primaryGenreName),
                        style = MaterialTheme.typography.subtitle1,
                    )
                    Text(
                        text = stringResource(R.string.price, movie.trackPrice.toString()),
                        style = MaterialTheme.typography.subtitle1,
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }

                Image(
                    painter = painterResource(
                        id = if (favoriteState.value) {
                            R.drawable.ic_heart_solid
                        } else {
                            R.drawable.ic_heart_regular
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                        .width(24.dp)
                        .height(24.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            val newState = favoriteState.value.not()
                            onFavoriteClickListener?.invoke(movie.trackId, newState)
                            favoriteState.value = newState
                        }
                )

            }
        }

    }


}

@Preview
@Composable
fun MovieItemPreview() {
    MaterialTheme {
        MovieItem(
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

@Composable
fun NoResultFoundDisplay() {
    Column(
        modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.no_movies_found),
            modifier = Modifier.padding(top = 32.dp),
            style = MaterialTheme.typography.subtitle1,
            color = colorResource(R.color.black),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Image(
            painter = painterResource(id = R.drawable.ic_video_solid),
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )
    }
}