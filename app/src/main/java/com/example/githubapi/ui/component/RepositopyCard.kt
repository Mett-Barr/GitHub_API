package com.example.githubapi.ui.component

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.githubapi.R
import com.example.githubapi.data.remote.github.getrepo.json.GetRepoItem
import com.example.githubapi.data.remote.github.search.repositories.Item
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log10


//@Preview(
//    showBackground = true,
//    backgroundColor = 0xFFFFFF
//)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoCard(
    item: Item,
    onClick: () -> Unit,
    isBookmarkExist: Boolean = false,
    onBookmarkChange: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Column(Modifier.padding(8.dp)) {
            Row(Modifier.height(116.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(80.dp)
                ) {

                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(item.owner.avatar_url)
                            .crossfade(true)
                            .build(),
                        contentDescription = "avatar url",
                        placeholder = painterResource(
                            id = R.drawable.iconmonstr_github_1
                        ),
                        modifier = Modifier
                            .size(80.dp)
                            .clip(
                                if (item.owner.type == "User") RoundedCornerShape(50) else RoundedCornerShape(
                                    15
                                )
                            )

                    )
                    Text(
                        text = item.owner.login,
                        maxLines = 2,
                        modifier = Modifier.wrapContentHeight(),
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Box() {
                    Column(modifier = Modifier.height(IntrinsicSize.Min)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = item.name,
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier
                                    .wrapContentHeight()
                                    .weight(1f)
                            )
                            @OptIn(ExperimentalAnimationApi::class)
                            AnimatedContent(targetState = isBookmarkExist) {
                                if (it) {
                                    ClickableIcon(painter = painterResource(id = R.drawable.bookmark_black_24dp)) { onBookmarkChange() }
                                } else {
                                    ClickableIcon(painter = painterResource(id = R.drawable.bookmark_add_black_24dp)) { onBookmarkChange() }
                                }
                            }

                        }
                        Text(
                            text = item.description ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.fillMaxHeight(),
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Text(
                    text = "⭐  ${item.stargazers_count.toUnitNumber()}",
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "\uD83C\uDF74  ${item.forks_count.toUnitNumber()}",
                    modifier = Modifier.weight(1f)
                )
                Text(text = "⌨  ${item.language ?: ""}", modifier = Modifier.weight(1f))
                Text(text = item.updated_at.extractDate(), modifier = Modifier.wrapContentWidth())

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoCard(
    item: GetRepoItem,
    onClick: () -> Unit,
    isBookmarkExist: Boolean = false,
    onBookmarkChange: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            onClick()
            Log.d("!!!", "RepoCard onClick ")
        }
    ) {
        Column(Modifier.padding(8.dp)) {
            Row(Modifier.height(116.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(80.dp)
                ) {

                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(item.owner.avatar_url)
                            .crossfade(true)
                            .build(),
                        contentDescription = "avatar url",
                        placeholder = painterResource(
                            id = R.drawable.iconmonstr_github_1
                        ),
                        modifier = Modifier
                            .size(80.dp)
                            .clip(
                                if (item.owner.type == "User") RoundedCornerShape(50) else RoundedCornerShape(
                                    15
                                )
                            )

                    )
                    Text(
                        text = item.owner.login,
                        maxLines = 2,
                        modifier = Modifier.wrapContentHeight(),
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Box() {
                    Column(modifier = Modifier.height(IntrinsicSize.Min)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = item.name,
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier
                                    .wrapContentHeight()
                                    .weight(1f)
                            )
                            @OptIn(ExperimentalAnimationApi::class)
                            AnimatedContent(targetState = isBookmarkExist) {
                                if (it) {
                                    ClickableIcon(painter = painterResource(id = R.drawable.bookmark_black_24dp)) { onBookmarkChange() }
                                } else {
                                    ClickableIcon(painter = painterResource(id = R.drawable.bookmark_add_black_24dp)) { onBookmarkChange() }
                                }
                            }

                        }
                        Text(
                            text = item.description ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.fillMaxHeight(),
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Text(
                    text = "⭐  ${item.stargazers_count.toUnitNumber()}",
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "\uD83C\uDF74  ${item.forks_count.toUnitNumber()}",
                    modifier = Modifier.weight(1f)
                )
                Text(text = "⌨  ${item.language ?: ""}", modifier = Modifier.weight(1f))
                Text(text = item.updated_at.extractDate(), modifier = Modifier.wrapContentWidth())
            }
        }
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showBackground = true)
//@Composable
//fun CardTest() {
//    LazyColumn(
//        Modifier.fillMaxSize(),
//        contentPadding = PaddingValues(top = 72.dp, start = 8.dp, end = 8.dp),
//        verticalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//
//        items(50) {
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                onClick = {}
//            ) {
//
//                Column(Modifier.padding(8.dp)) {
//                    Row(
//                        Modifier
////                            .padding(16.dp)
//                            .height(116.dp)
//                    ) {
//                        Column(
//                            horizontalAlignment = Alignment.CenterHorizontally,
//                            modifier = Modifier.width(80.dp)
//                        ) {
//                            Image(
//                                painter = rememberAsyncImagePainter("https://avatars.githubusercontent.com/u/878437?v=4"),
//                                contentDescription = "",
//                                modifier = Modifier
//                                    .size(80.dp)
//                                    .clip(RoundedCornerShape(15))
//                            )
//
////                        Text(text = "asdghahsdahaerarehjerh", maxLines = 1, modifier = Modifier.fillMaxHeight(), overflow = TextOverflow.Ellipsis)
//                            Text(
//                                text = "JetBrains aeyaererah",
//                                maxLines = 2,
//                                modifier = Modifier.wrapContentHeight(),
//                                overflow = TextOverflow.Ellipsis,
//                                style = MaterialTheme.typography.bodySmall
//                            )
//                        }
//
//                        Box() {
////                            ClickableIcon(painter = painterResource(id = R.drawable.bookmark_add_black_24dp)) {
////
////                            }
//
//                            Column(modifier = Modifier.height(IntrinsicSize.Min)) {
//                                Text(
//                                    text = "kotlin",
//                                    style = MaterialTheme.typography.displaySmall,
//                                    modifier = Modifier.wrapContentHeight()
//                                )
//                                Text(
//                                    text = "The Kotlin Programming Language $longStr",
//                                    style = MaterialTheme.typography.bodySmall,
//                                    modifier = Modifier.fillMaxHeight(),
//                                    overflow = TextOverflow.Ellipsis
//                                )
////                    Text()
//                            }
//                        }
//                    }
//
//                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//
//                        Text(text = "⭐  5000", modifier = Modifier.weight(1f))
//                        Text(text = "\uD83C\uDF74  300", modifier = Modifier.weight(1f))
//                        Text(text = "⌨  Kotlin", modifier = Modifier.weight(1f))
//                        Text(text = "2023/02/14", modifier = Modifier.wrapContentWidth())
//                    }
//                }
//            }
//        }
//    }
//}

const val longStr =
    "ahfjkldsahsfjk7igaluivbwABEIUYWYIgv UIUIvb luiHUfh UFUIhf UIHu uUIFHifsud uasuih aufaufiuaaufhiafuauidfuiAFsjkfhuiafhafjh hkajsdhf ahsd fjkha dfkjhasd fkjjahsd fkjahsd fkajhsd f"

//@Composable
//fun CardSample() {
//
//    Card(
////        colors = CardColors(contentColor = Color.Gray),
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(10.dp),
////            onClick = onClick
//    ) {
//
//        Row {
//            Column {
//                Image(
//                    painter = rememberAsyncImagePainter("https://avatars.githubusercontent.com/u/878437?v=4"),
//                    contentDescription = "",
//                    modifier = Modifier
//                        .size(50.dp)
//                        .clip(
//                            RoundedCornerShape(15)
//                        )
//                )
//
//                Text(text = "JetBrains")
//            }
//
//            Column {
//                Text(text = "kotlin")
//                Text(text = "The Kotlin Programming Language")
////                    Text()
//            }
//        }
//    }
//}

fun Int.toUnitNumber(): String {
    if (this < 1000) {
        return this.toString()
    }
    val exp = (log10(toDouble()) / 3).toInt()
    return String.format("%.1f%c", this / Math.pow(1000.0, exp.toDouble()), "kMBTPE"[exp - 1])
}

fun String.extractDate(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = inputFormat.parse(this)
    return date?.let { outputFormat.format(it) } ?: ""
}