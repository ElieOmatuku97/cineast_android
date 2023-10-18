package elieomatuku.cineast_android.widgets.movieswidget

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.domain.model.Genre
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.utils.UiUtils


/**
 * Created by elieomatuku on 2022-07-17
 */

@Composable
fun MoviesWidget(
    viewModelFactory: ViewModelProvider.Factory,
    viewModel: MoviesViewModel = viewModel(factory = viewModelFactory),
    movies: List<Movie>,
    sectionTitle: String = String(),
    onItemClick: (content: Content, genres: List<Genre>) -> Unit = { _, _ -> },
    onSeeAllClick: (contents: List<Content>) -> Unit = {}
) {
    val viewState by viewModel.viewState.observeAsState()
    val genres = viewState?.genres ?: emptyList()

    MoviesWidget(
        movies = movies,
        genres = genres,
        sectionTitle = sectionTitle,
        onItemClick = onItemClick,
        onSeeAllClick = onSeeAllClick
    )
}

@Composable
fun MoviesWidget(
    movies: List<Movie>,
    genres: List<Genre>,
    sectionTitle: String,
    onItemClick: (content: Content, genres: List<Genre>) -> Unit,
    onSeeAllClick: (contents: List<Content>) -> Unit
) {
    Surface {
        Column(modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_small))) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = dimensionResource(id = R.dimen.padding_small),
                        end = dimensionResource(id = R.dimen.padding_small),
                        top = dimensionResource(id = R.dimen.padding_small),
                        bottom = dimensionResource(id = R.dimen.padding_xsmall)
                    )
                    .clickable(onClick = { onSeeAllClick(movies) })
            ) {
                Text(
                    text = sectionTitle,
                )
                Row(horizontalArrangement = Arrangement.End) {
                    Text(
                        stringResource(id = R.string.see_all)
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_keyboard_arrow_right_black_24dp),
                        contentDescription = null,
                    )
                }
            }
            LazyRow(
                modifier = Modifier.padding(
                    top = dimensionResource(id = R.dimen.padding_small),
                    start = dimensionResource(id = R.dimen.padding_small)
                )
            ) {
                items(movies) { movie ->
                    MovieItem(movie = movie, genres = genres, onMovieClick = onItemClick)
                }
            }
        }
    }
}

@Composable
fun MovieItem(
    movie: Movie,
    genres: List<Genre>,
    onMovieClick: (content: Content, genres: List<Genre>) -> Unit
) {
    val imageUrl = UiUtils.getImageUrl(movie.imagePath, stringResource(id = R.string.image_small))
    Column(Modifier.clickable(onClick = { onMovieClick.invoke(movie, genres) })) {
        Image(
            painter = rememberImagePainter(
                data = imageUrl,
            ),
            contentDescription = null,
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.image_height_xxlarge))
                .width(dimensionResource(id = R.dimen.image_width_xlarge))
        )
        (movie.title ?: movie.originalTitle)?.let {
            Text(
                text = it,
                maxLines = 1,
                fontSize = dimensionResource(id = R.dimen.text_size_small).value.sp,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(
                        top = dimensionResource(id = R.dimen.padding_small),
                        start = dimensionResource(id = R.dimen.padding_small),
                        end = dimensionResource(id = R.dimen.padding_small)
                    )
                    .widthIn(max = 70.dp)
            )
        }
        movie.releaseDate?.let {
            Text(
                text = it,
                fontSize = dimensionResource(id = R.dimen.text_size_small).value.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(
                        start = dimensionResource(id = R.dimen.padding_small),
                        end = dimensionResource(id = R.dimen.padding_small)
                    )
                    .widthIn(max = 70.dp)
            )
        }
    }
}