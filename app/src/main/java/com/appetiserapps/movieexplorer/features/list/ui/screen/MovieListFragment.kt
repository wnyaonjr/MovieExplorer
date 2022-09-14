package com.appetiserapps.movieexplorer.features.list.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.compose.AsyncImage
import com.appetiserapps.movieexplorer.R
import com.appetiserapps.movieexplorer.databinding.FragmentMovieListBinding
import com.appetiserapps.movieexplorer.features.list.domain.Movie
import com.appetiserapps.movieexplorer.features.list.domain.MovieListEventListeners
import com.appetiserapps.movieexplorer.features.list.domain.MovieListState
import com.appetiserapps.movieexplorer.features.list.domain.MovieListStates
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dagger.hilt.android.AndroidEntryPoint

/**
 * Contains the layout for the movie list
 */
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

    /**
     * contains event listeners from viewModel
     */
    private fun initObservers() {
        viewModel.navigateToMovieDetails.observe(viewLifecycleOwner) {
            navigateToMovieDetails(it)
        }
    }

    /**
     * navigation to movie details after use click movie row
     */
    private fun navigateToMovieDetails(trackId: Int) {
        findNavController().navigate(
            MovieListFragmentDirections.actionMovieListFragmentToMovieDetailsFragment(
                trackId
            )
        )
    }
}

/**
 * function overload for movie layout to declare states and event listeners from view model
 */
@Composable
fun MovieListLayout(viewModel: MovieListViewModel) {
    val movies by viewModel.movies.observeAsState()
    val trackName by viewModel.trackName.observeAsState()
    val currentState by viewModel.currentState.observeAsState()

    MovieListLayout(
        movieListStates = MovieListStates(
            movies = movies,
            trackName = trackName,
            currentState = currentState
        ),
        movieListEventListeners = MovieListEventListeners(
            onFavoriteClickListener = viewModel::onFavoriteClick,
            onMovieClickListener = viewModel::onMovieClick,
            searchListener = viewModel::onTextChange,
            onRefresh = viewModel::onRefresh,
        )
    )
}

/**
 * movie list layout for header, search layout, and movie list
 */
@Composable
fun MovieListLayout(
    movieListStates: MovieListStates,
    movieListEventListeners: MovieListEventListeners? = null
) {


    Column(modifier = Modifier.background(colorResource(R.color.gray))) {
        HeaderLayout(R.string.movies)
        MovieSearchLayout(movieListStates, movieListEventListeners)
        MovieList(
            movieListStates = movieListStates,
            movieListEventListeners = movieListEventListeners
        )
    }
}

/**
 * layout for the search movie input
 */
@Composable
fun MovieSearchLayout(
    movieListStates: MovieListStates,
    movieListEventListeners: MovieListEventListeners?
) {
    var text by remember { mutableStateOf(TextFieldValue(movieListStates.trackName.orEmpty())) }
    OutlinedTextField(
        value = text,
        singleLine = true,
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        placeholder = { Text(text = stringResource(R.string.movie_name)) },
        onValueChange = {
            text = it
            movieListEventListeners?.searchListener?.invoke(text.text)
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            focusedIndicatorColor = colorResource(R.color.green),
            unfocusedIndicatorColor = Color.Black
        ),
    )
}

/**
 * common reusable layout for header
 */
@Composable
fun HeaderLayout(@StringRes stringId: Int) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(stringId),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
        },
        backgroundColor = Color.White,
    )
}

/**
 * layout of movie list based on states, includes refresh list support
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MovieList(
    movieListStates: MovieListStates,
    movieListEventListeners: MovieListEventListeners? = null
) {

    SwipeRefresh(
        state = rememberSwipeRefreshState(
            isRefreshing = movieListStates.currentState == MovieListState.LOADING
        ),
        onRefresh = {
            movieListEventListeners?.onRefresh?.invoke()
        }
    ) {
        when (movieListStates.currentState) {
            MovieListState.ERROR -> ErrorDisplay()
            else -> {
                if (!movieListStates.movies.isNullOrEmpty()) {
                    LazyColumn {
                        items(movieListStates.movies) { movie ->
                            MovieItem(
                                movie = movie,
                                onFavoriteClickListener = movieListEventListeners?.onFavoriteClickListener,
                                onMovieClickListener = movieListEventListeners?.onMovieClickListener,
                                modifier = Modifier.animateItemPlacement()
                            )
                        }
                    }
                } else {
                    NoResultFoundDisplay()
                }
            }
        }
    }

}

/**
 * layout for movie list item, contains track name, genre, price, cover, and favorite button
 */
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
            MovieInitialDetails(
                movie,
                favoriteState,
                interactionSource,
                onFavoriteClickListener
            )
        }
    }
}

/**
 * common layout for movie initial details (track name, genre, price, cover
 */
@Composable
fun MovieInitialDetails(
    movie: Movie,
    favoriteState: MutableState<Boolean>,
    interactionSource: MutableInteractionSource,
    onFavoriteClickListener: ((trackId: Int, favorite: Boolean) -> Unit)?
) {
    Text(
        text = movie.trackName,
        style = MaterialTheme.typography.h6,
    )

    Row(verticalAlignment = Alignment.CenterVertically) {

        AsyncImage(
            modifier = Modifier.size(48.dp),
            model = movie.artworkUrl100,
            contentDescription = null,
            error = painterResource(R.drawable.ic_broken_image),
            contentScale = ContentScale.Crop
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

/**
 * layout when no results found from search
 */
@Composable
fun NoResultFoundDisplay() {
    MessageDisplay(
        stringId = R.string.no_movies_found,
        drawableId = R.drawable.ic_video_solid
    )
}

/**
 * layout when error encountered from search
 */
@Composable
fun ErrorDisplay() {
    MessageDisplay(
        stringId = R.string.request_error,
        drawableId = R.drawable.ic_warning
    )
}

/**
 * common layout for displaying messages when list is not available
 */
@Composable
fun MessageDisplay(@StringRes stringId: Int, @DrawableRes drawableId: Int) {
    Column(
        modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = drawableId),
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(stringId),
            style = MaterialTheme.typography.subtitle1,
            color = colorResource(R.color.black),
        )
    }
}

/**
 * test function for showing movie row
 */
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