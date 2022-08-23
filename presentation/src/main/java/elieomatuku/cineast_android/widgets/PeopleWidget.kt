package elieomatuku.cineast_android.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.domain.model.Person
import elieomatuku.cineast_android.utils.UiUtils


/**
 * Created by elieomatuku on 2022-07-20
 */

@Composable
fun PeopleWidget(
    people: List<Person>,
    sectionTitle: String,
    onItemClick: (content: Content) -> Unit,
    onSeeAllClick: (contents: List<Content>) -> Unit
) {
    Column(modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.holder_item_movie_textview_margin))) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = dimensionResource(id = R.dimen.holder_movie_layout_padding_start),
                    end = dimensionResource(id = R.dimen.holder_movie_layout_padding),
                    top = dimensionResource(id = R.dimen.holder_movie_layout_padding),
                    bottom = dimensionResource(id = R.dimen.holder_item_movie_image_view_margin)
                )
                .clickable(onClick = { onSeeAllClick(people) })
        ) {
            Text(
                text = sectionTitle,
                color = colorResource(R.color.color_white)
            )
            Row(horizontalArrangement = Arrangement.End) {
                Text(
                    stringResource(id = R.string.see_all),
                    color = colorResource(R.color.color_orange_app)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_keyboard_arrow_right_black_24dp),
                    contentDescription = null,
                    tint = colorResource(R.color.color_orange_app)
                )
            }
        }
        LazyRow(
            modifier = Modifier.padding(
                top = dimensionResource(id = R.dimen.holder_movie_layout_padding),
                start = dimensionResource(id = R.dimen.holder_movie_listview_padding_start)
            )
        ) {
            items(people) { person ->
                PeopleItem(person = person, onPersonClick = onItemClick)
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun PeopleItem(
    person: Person,
    onPersonClick: (content: Content) -> Unit
) {
    val imageUrl =
        UiUtils.getImageUrl(person.profilePath, stringResource(id = R.string.image_small))
    Column(Modifier.clickable(onClick = { onPersonClick.invoke(person) })) {
        Image(
            painter = rememberImagePainter(
                data = imageUrl,
            ),
            contentDescription = null,
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.movie_summary_image_size))
                .width(dimensionResource(id = R.dimen.movie_summary_image_width))
        )
        person.name?.let {
            Text(
                text = it,
                color = colorResource(R.color.color_white),
                maxLines = 1,
                fontSize = dimensionResource(id = R.dimen.holder_item_movie_textview_size).value.sp,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(
                        top = dimensionResource(id = R.dimen.holder_item_movie_textview_margin),
                        start = dimensionResource(id = R.dimen.holder_item_movie_textview_margin),
                        end = dimensionResource(id = R.dimen.holder_item_movie_textview_margin)
                    )
                    .widthIn(max = 70.dp)
            )
        }
    }
}