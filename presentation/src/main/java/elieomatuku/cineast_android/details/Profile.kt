package elieomatuku.cineast_android.details

import android.text.SpannableString
import android.text.style.URLSpan
import android.text.util.Linkify
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.utils.UiUtils

@OptIn(ExperimentalCoilApi::class)
@Composable
fun Profile(
    imagePath: String?,
    title: String?,
    subTitle: String?,
    description: String?,
    webSiteLink: String?,
    onProfileClick: () -> Unit,
    onWebSiteLinkClick: (String) -> Unit,
    child: @Composable () -> Unit = {}
) {
    val imageUrl = UiUtils.getImageUrl(imagePath, stringResource(id = R.string.image_small))

    Row {
        Image(
            painter = rememberImagePainter(
                data = imageUrl,
            ),
            contentDescription = null,
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.image_height_xxlarge))
                .width(dimensionResource(id = R.dimen.image_width_xlarge))
                .padding(
                    top = dimensionResource(id = R.dimen.padding_small),
                    start = dimensionResource(id = R.dimen.padding_small),
                    bottom = dimensionResource(id = R.dimen.padding_medium)
                )
                .clickable {
                    onProfileClick()
                }
        )
        Column(
            modifier = Modifier.padding(
                top = dimensionResource(id = R.dimen.padding_small),
                start = dimensionResource(id = R.dimen.padding_large)
            )
        ) {
            title?.let {
                Text(
                    it
                )
            }

            subTitle?.let {
                Text(
                    text = it,
                    fontSize = dimensionResource(id = R.dimen.text_size_small).value.sp,
                    modifier = Modifier.padding(
                        top = dimensionResource(id = R.dimen.padding_small)
                    )
                )
            }

            webSiteLink?.let {
                val linkStyle = SpanStyle(
                    textDecoration = TextDecoration.Underline,
                )
                ClickableText(
                    text = remember(it) { it.linkify(linkStyle) },
                    onClick = { position ->
                        it.linkify(linkStyle).urlAt(position) { link ->
                            onWebSiteLinkClick(link)
                        }
                    },
                    modifier = Modifier.padding(
                        top = dimensionResource(id = R.dimen.padding_small)
                    )
                )
            }

            description?.let {
                Text(
                    text = it,
                    fontSize = dimensionResource(id = R.dimen.text_size_small).value.sp,
                    modifier = Modifier.padding(
                        top = dimensionResource(id = R.dimen.padding_small)
                    )
                )
            }

            child()
        }
    }
}

const val URL = "URL"
fun String.linkify(
    linkStyle: SpanStyle,
) = buildAnnotatedString {
    append(this@linkify)

    val spannable = SpannableString(this@linkify)
    Linkify.addLinks(spannable, Linkify.WEB_URLS)

    val spans = spannable.getSpans(0, spannable.length, URLSpan::class.java)
    for (span in spans) {
        val start = spannable.getSpanStart(span)
        val end = spannable.getSpanEnd(span)

        addStyle(
            start = start,
            end = end,
            style = linkStyle,
        )
        addStringAnnotation(
            tag = URL,
            annotation = span.url,
            start = start,
            end = end
        )
    }
}

fun AnnotatedString.urlAt(position: Int, onFound: (String) -> Unit) =
    getStringAnnotations(URL, position, position).firstOrNull()?.item?.let {
        onFound(it)
    }