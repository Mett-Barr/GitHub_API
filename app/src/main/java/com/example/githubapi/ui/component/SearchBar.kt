package com.example.githubapi.ui.component

//import androidx.compose.foundation.layout.BoxScopeInstance.matchParentSize
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.githubapi.MainViewModel
import com.example.githubapi.R
import com.example.githubapi.data.remote.github.Oauth
import com.example.githubapi.data.remote.github.Order
import com.example.githubapi.data.remote.github.PerPage
import com.example.githubapi.data.remote.github.Sort

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
fun BoxScope.SearchBar(
    viewModel: MainViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
//    var expanded by remember { mutableStateOf(true) }

//    var isSearchBarExpanded by remember {
//        mutableStateOf(false)
//    }

    var isFocused by remember {
        mutableStateOf(false)
    }

    var isAdvancedSearchMenuOpen by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(viewModel.isSearchBarExpanded) {
        if (!viewModel.isSearchBarExpanded) {
            focusManager.clearFocus()

            isFocused = false
            isAdvancedSearchMenuOpen = false
        }
    }

    Column(
        modifier = Modifier
//            .background(Color.Blue.copy(0.3f))
            .wrapContentWidth()
//            .clickable {
//
//                // test
////                isAdvancedMenuOpen = !isAdvancedMenuOpen
//                focusManager.clearFocus()
//
//
//                viewModel.onSearchBarStateChange(false)
//            }
            .padding(8.dp)
    ) {

        val queryText by viewModel.queryFlow.collectAsState()
        SearchBox(
            queryText = queryText,
            onQueryTextChange = { viewModel.queryFlow.value = it },
            cleanText = { viewModel.queryFlow.value = "" },

            isSearchBarExpanded = viewModel.isSearchBarExpanded,
            onSearchingStateChange = { viewModel.onSearchBarStateChange(it) },
            isAdvancedSearchMenuOpen = isAdvancedSearchMenuOpen,
            onAdvancedSearchMenuChange = { isAdvancedSearchMenuOpen = it },

            onClick = {
                focusRequester.requestFocus()
            },
            searchFocus = focusRequester,

            isFocused = isFocused,
            onFocusedStateChange = {
                isFocused = it
                if (it) {
                    isAdvancedSearchMenuOpen = false
                }
            }
        )

//        SimpleTextField(value = queryText, onValueChange = { viewModel.queryFlow.value = it })


        AnimatedVisibility(visible = isAdvancedSearchMenuOpen) {
            Column(
                Modifier
//                    .background(Color.Yellow)
//                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .padding(10.dp), verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextFieldDropdownMenu<Oauth>(
                    label = "Oauth",
                    selectedOptionText = viewModel.oauth.value.toString(),
                    select = { viewModel.oauth.value = it }
                )
                TextFieldDropdownMenu<Sort>(
                    label = "Sort",
                    selectedOptionText = viewModel.sort.value.toString(),
                    select = { viewModel.sort.value = it }
                )
                TextFieldDropdownMenu<Order>(
                    label = "Order",
                    selectedOptionText = viewModel.order.value.toString(),
                    select = { viewModel.order.value = it }
                )
                TextFieldDropdownMenu<PerPage>(
                    label = "Per Page",
                    selectedOptionText = viewModel.perPage.value.toString(),
                    select = { viewModel.perPage.value = it }
                )
            }
        }

//        AnimatedVisibility(visible = viewModel.isSearchBarExpanded) {
//            LazyColumn(Modifier.fillMaxWidth().height(500.dp)) {
//
//            }
//        }


//        Spacer(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(20.dp)
//                .background(Color.Gray)
//        )
    }

}


@Composable
fun DropdownDemo() {
    val items = listOf("A", "B", "C", "D", "E", "F")
    val disabledValue = "B"
    var expanded by remember { mutableStateOf(true) }

    Box(modifier = Modifier
//        .width(intrinsicSize = IntrinsicSize.Min)
        .background(Color.Yellow)
        .clickable { expanded = true }
//        .fillMaxSize()
        .wrapContentSize(Alignment.TopStart)) {
        Button(onClick = { expanded = !expanded }) {
            Text(expanded.toString())

        }
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Default.MoreVert, contentDescription = "Localized description")
        }
        DropdownMenu(
            modifier = Modifier.clip(CircleShape),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                modifier = Modifier.clip(RoundedCornerShape(20.dp)),

                onClick = { /* Handle refresh! */ }, text = {
                    Text("Refresh")
                })
            DropdownMenuItem(onClick = { /* Handle settings! */ }, text = {
                Text("Settings")
            })
            Divider()
            DropdownMenuItem(onClick = { /* Handle send feedback! */ }, text = {
                Text("Send Feedback")
            })
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
inline fun <reified T : Enum<T>> SettingChips(
    chipText: String,
    crossinline select: (T) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    Box {
        AssistChip(
            modifier = Modifier.wrapContentSize(),
            shape = RoundedCornerShape(8.dp),
            onClick = { expanded = true },
            trailingIcon = {
                Crossfade(targetState = expanded) { isExpanded ->

                    if (isExpanded) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(id = R.drawable.expand_less_fill0_wght400_grad0_opsz48),
                            contentDescription = "expand less"
                        )
                    } else
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(id = R.drawable.expand_more_fill0_wght400_grad0_opsz48),
                            contentDescription = "expand more"
                        )
                }
            },
            label = {
                Text(
                    text = chipText,
                    maxLines = 1
                )
            })
        DropdownSettingChips(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            select = select
        )
    }
}


@Composable
inline fun <reified T : Enum<T>> DropdownSettingChips(
    expanded: Boolean,
    noinline onDismissRequest: () -> Unit,
    crossinline select: (T) -> Unit
) {

    val enumValues = enumValues<T>().toList()

    DropdownMenu(
        modifier = Modifier,
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        enumValues.forEach {
            DropdownMenuItem(text = { Text(text = it.toString()) }, onClick = {
                select(it)
                onDismissRequest()
            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
inline fun <reified T : Enum<T>> TextFieldDropdownMenu(
    label: String,
    selectedOptionText: String,
    crossinline select: (T) -> Unit
) {
    val enumValues = enumValues<T>().toList()
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        TextField(
            // The `menuAnchor` modifier must be passed to the text field for correctness.
            modifier = Modifier.menuAnchor(),
            readOnly = true,
            value = selectedOptionText,
            onValueChange = {},
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            enumValues.forEach {
                DropdownMenuItem(
                    text = { Text(it.toString()) },
                    onClick = {
                        select(it)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}


@Preview
@Composable
fun ExpandTest() {

    var exp by remember {
        mutableStateOf(false)
    }

    Column(
        Modifier
            .clickable { exp = !exp }
            .fillMaxWidth()) {
        repeat(10) {
            AnimatedVisibility(visible = it < 5 || exp) {

                Text(text = it.toString())
            }
//            if (it < 5 || exp) {
//            }
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .background(Color.Gray)
        )
    }
}

//@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBox(
    queryText: String,
    onQueryTextChange: (String) -> Unit,
    cleanText: () -> Unit = {},

    isSearchBarExpanded: Boolean,
    onSearchingStateChange: (Boolean) -> Unit = {},

    isAdvancedSearchMenuOpen: Boolean,
    onAdvancedSearchMenuChange: (Boolean) -> Unit = {},

    onClick: () -> Unit = {},
    searchFocus: FocusRequester,

    isFocused: Boolean,
    onFocusedStateChange: (Boolean) -> Unit = {}
) {

    val focusManager = LocalFocusManager.current

    Card(
        onClick = {
            onSearchingStateChange(true)
            onClick()
        },
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(4.dp),
//                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AnimatedVisibility(visible = isSearchBarExpanded) {
//                IconButton(onClick = {
//                    onSearchingStateChange(false)
//                    focusManager.clearFocus()
//                }) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.arrow_back_fill0_wght400_grad0_opsz48),
//                        contentDescription = "search"
//                    )
//                }
                ClickableIcon(painter = painterResource(id = R.drawable.arrow_back_fill0_wght400_grad0_opsz48)) {
                    onSearchingStateChange(false)
//                    focusManager.clearFocus()
                }
//                Icon(
//                    painter = painterResource(id = R.drawable.arrow_back_fill0_wght400_grad0_opsz48),
//                    contentDescription = "search",
//                    modifier = Modifier
//                        .size(24.dp)
//                        .clickable {
//                            onSearchingStateChange(false)
//                            focusManager.clearFocus()
//                        }
//                )
            }

            AnimatedVisibility(visible = !isSearchBarExpanded) {
                Spacer(modifier = Modifier.width(16.dp))
            }


            SimpleTextField(
                value = queryText,
                onValueChange = onQueryTextChange,
                modifier = Modifier.weight(1f),
                focusRequester = searchFocus,

//                onClick = {
//                    onSearchingStateChange(true)
//                    onClick()
//                },

                isFocused = isFocused,
                onFocusedStateChange = { focused ->

                    if (focused) {
                        onSearchingStateChange(true)
                        onClick()
                    }

                    onFocusedStateChange(focused)
                }
            )
            Spacer(modifier = Modifier.width(8.dp))


            AnimatedVisibility(visible = isSearchBarExpanded) {
                Crossfade(targetState = isAdvancedSearchMenuOpen) { opening ->
                    if (opening) {
//                        Icon(
//                            painter = painterResource(id = R.drawable.expand_less_fill0_wght400_grad0_opsz48),
//                            contentDescription = "search",
//                            modifier = Modifier
//                                .size(24.dp)
//                                .clickable { onAdvancedSearchMenuChange(false) })

                        ClickableIcon(
                            painter = painterResource(id = R.drawable.expand_less_fill0_wght400_grad0_opsz48),
                            contentDescription = "expand less",
                            onClick = {
                                onAdvancedSearchMenuChange(false)
                                focusManager.clearFocus()
                            }
                        )
                    } else {
//                        Icon(
//                            painter = painterResource(id = R.drawable.expand_more_fill0_wght400_grad0_opsz48),
//                            contentDescription = "search",
//                            modifier = Modifier
//                                .size(24.dp)
//                                .clickable { onAdvancedSearchMenuChange(true) }
//                        )
                        ClickableIcon(
                            painter = painterResource(id = R.drawable.expand_more_fill0_wght400_grad0_opsz48),
                            contentDescription = "expand more",
                            onClick = {
                                onAdvancedSearchMenuChange(true)
                                focusManager.clearFocus()
                            }
                        )

                    }
                }
            }

            Crossfade(targetState = isSearchBarExpanded) {
                if (it) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.close_fill0_wght400_grad0_opsz48),
//                        contentDescription = "close",
//                        modifier = Modifier.size(24.dp)
////                            .wrapContentWidth()
//                    )
                    ClickableIcon(
                        painter = painterResource(id = R.drawable.close_fill0_wght400_grad0_opsz48),
                        contentDescription = "close",
                        onClick = cleanText
                    )

                } else {
                    ClickableIcon(
                        painter = painterResource(id = R.drawable.search_fill0_wght400_grad0_opsz48),
                        contentDescription = "close"
                    )
//                    Icon(
//                        painter = painterResource(id = R.drawable.search_fill0_wght400_grad0_opsz48),
//                        contentDescription = "close",
//                        modifier = Modifier.size(24.dp)
////                            .wrapContentWidth()
//                    )
                }
            }
        }
    }
}

@Composable
fun SimpleTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester,
    onClick: () -> Unit = {},

    isFocused: Boolean, // !!! 新增
    onFocusedStateChange: (Boolean) -> Unit = {}, // !!! 新增

    startSearch: () -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme
    val textStyle = LocalTextStyle.current
    val textColor = textStyle.color.takeOrElse {
        colorScheme.onSurface
    }
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))
    val cursorColor = colorScheme.primary

    BasicTextField(
        value = value,
        modifier = modifier
            .focusRequester(focusRequester)
            .onFocusChanged {
                onFocusedStateChange(it.isFocused)
            },
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = mergedTextStyle,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = { startSearch() }
        ),
        cursorBrush = SolidColor(cursorColor),
        decorationBox = @Composable { content ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                content()
            }
        }
    )
}
