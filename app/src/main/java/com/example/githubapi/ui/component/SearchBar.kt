package com.example.githubapi.ui.component

//import androidx.compose.foundation.layout.BoxScopeInstance.matchParentSize
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.layout.RowScopeInstance.weight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.githubapi.MainViewModel
import com.example.githubapi.R
import com.example.githubapi.data.local.room.history.SearchHistory
import com.example.githubapi.data.remote.github.Oauth
import com.example.githubapi.data.remote.github.Order
import com.example.githubapi.data.remote.github.PerPage
import com.example.githubapi.data.remote.github.Sort
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalLayoutApi::class, ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class
)
//@Preview
@Composable
fun BoxScope.SearchBar(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
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

//    val searchText by viewModel.queryFlow.collectAsState()

    LaunchedEffect(viewModel.isSearchBarExpanded) {
        if (!viewModel.isSearchBarExpanded) {
            focusManager.clearFocus()

            isFocused = false
            isAdvancedSearchMenuOpen = false
            if (viewModel.queryFlow.value.isBlank()) {
                viewModel.backToLastSearch()
            }
        }
    }


    val historyList = viewModel.searchHistories.observeAsState()

    Column(
        modifier = Modifier
            .wrapContentWidth()
            .padding(8.dp),

        verticalArrangement = Arrangement.spacedBy(8.dp)
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
//                viewModel.textFieldValue = viewModel.textFieldValue.copy(selection = TextRange(viewModel.textFieldValue.selection.end))
            },
            searchFocus = focusRequester,

            isFocused = isFocused,
            onFocusedStateChange = {
                isFocused = it
                if (it) {
                    isAdvancedSearchMenuOpen = false
                }
            },

            startSearch = {
//                viewModel.searchRepo()
                viewModel.searchRepoAndStore()
                viewModel.onSearchBarStateChange(false)
            },
            onBackClick = {
                viewModel.onSearchBarStateChange(false)
                viewModel.backToLastSearch()
            }
        )

//        SimpleTextField(value = queryText, onValueChange = { viewModel.queryFlow.value = it })


        AnimatedVisibility(visible = isAdvancedSearchMenuOpen) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
//                    .padding(8.dp),
                shape = RoundedCornerShape(28.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 20.dp)

            ) {

                val pagerState = rememberPagerState()
                val scope = rememberCoroutineScope()

                val onDropdownMenuPage by remember {
                    derivedStateOf { (pagerState.currentPage == 1) && (pagerState.targetPage == 1) && !pagerState.isScrollInProgress }
                }

                HorizontalPager(2, modifier = Modifier.weight(1f), state = pagerState) { page ->
                    if (page == 0) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(8.dp)
                        ) {
                            items(historyList.value ?: emptyList()) { it ->
                                Card(
                                    onClick = {
//                                        viewModel.queryFlow.value = it.searchTerm
//                                        viewModel.searchRepo()
                                        viewModel.queryFlow.value = it.searchTerm
                                        viewModel.searchRepoAndStore()
                                        viewModel.onSearchBarStateChange(false)
                                    },
                                    elevation = CardDefaults.cardElevation(
                                        defaultElevation = 4.dp
                                    ),
                                    shape = RoundedCornerShape(20.dp)

                                ) {

                                    Row(
                                        Modifier.padding(start = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Text(
                                            text = it.searchTerm,
                                            maxLines = 1,
                                            fontSize = 18.sp,
                                            modifier = Modifier.weight(1f)
                                        )
                                        ClickableIcon(
                                            painter = painterResource(id = R.drawable.cancel_black_24dp),
                                            tint = MaterialTheme.colorScheme.error
                                        ) { viewModel.deleteSearchHistory(it.id) }
                                    }
                                }
                            }
                            item {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }

                    } else {
                        Column {
                            Spacer(modifier = Modifier.weight(1f))
                            TextFieldDropdownMenu<Oauth>(
                                label = "Oauth",
                                selectedOptionText = viewModel.oauth.value.toString(),
                                select = { viewModel.oauth.value = it },
                                onDropdownMenuPage
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            TextFieldDropdownMenu<Sort>(
                                label = "Sort",
                                selectedOptionText = viewModel.sort.value.toString(),
                                select = { viewModel.sort.value = it },
                                onDropdownMenuPage
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            TextFieldDropdownMenu<Order>(
                                label = "Order",
                                selectedOptionText = viewModel.order.value.toString(),
                                select = { viewModel.order.value = it },
                                onDropdownMenuPage
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            TextFieldDropdownMenu<PerPage>(
                                label = "Per Page",
                                selectedOptionText = viewModel.perPage.value.toString(),
                                select = { viewModel.perPage.value = it },
                                onDropdownMenuPage
                            )
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }

//                LaunchedEffect(pagerState.currentPage) {
//                    Log.d("swipe", pagerState.currentPage.toString())
//                }


                Swiper(
                    page = { pagerState.currentPage },
                    pageChange = {
                        scope.launch {
                            pagerState.animateScrollToPage(it)
                        }
                    })


//                Column(
//                    Modifier
////                    .background(Color.Yellow)
////                    .align(Alignment.CenterHorizontally)
//                        .fillMaxWidth()
////                        .padding(8.dp)
////                        .clip(RoundedCornerShape(8.dp))
//                        .padding(bottom = 20.dp, top = 10.dp),
//                    verticalArrangement = Arrangement.spacedBy(8.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    TextFieldDropdownMenu<Oauth>(
//                        label = "Oauth",
//                        selectedOptionText = viewModel.oauth.value.toString(),
//                        select = { viewModel.oauth.value = it }
//                    )
//                    TextFieldDropdownMenu<Sort>(
//                        label = "Sort",
//                        selectedOptionText = viewModel.sort.value.toString(),
//                        select = { viewModel.sort.value = it }
//                    )
//                    TextFieldDropdownMenu<Order>(
//                        label = "Order",
//                        selectedOptionText = viewModel.order.value.toString(),
//                        select = { viewModel.order.value = it }
//                    )
//                    TextFieldDropdownMenu<PerPage>(
//                        label = "Per Page",
//                        selectedOptionText = viewModel.perPage.value.toString(),
//                        select = { viewModel.perPage.value = it }
//                    )
//                }
            }
        }

//        AnimatedVisibility(visible = viewModel.isSearchBarExpanded) {
//            SearchHistoriesCard(searchHistories = generateRandomSearchHistories())
//        }


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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColumnScope.SearchHistoriesCard(
    searchHistories: List<SearchHistory>,
    deleteHistory: (Int) -> Unit = {},
    clickHistory: (Int) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp),
//            .weight(0.5f),
        shape = RoundedCornerShape(28.dp)
    ) {


        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            itemsIndexed(searchHistories) { index, it ->
                Card(
                    onClick = { clickHistory(index) },
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 10.dp
                    ),
                    shape = RoundedCornerShape(20.dp)

                ) {

                    Row(
                        Modifier.padding(start = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
//                    modifier = Modifier
//                        .clip(RoundedCornerShape(8.dp))
//                        .clickable { clickHistory(index) }
//                        .padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = it.searchTerm,
                            maxLines = 1,
                            fontSize = 18.sp,
                            modifier = Modifier.weight(1f)
                        )
                        ClickableIcon(
                            painter = painterResource(id = R.drawable.cancel_black_24dp),
                            tint = MaterialTheme.colorScheme.error
                        ) { deleteHistory(index) }
                    }
                }
//                if (index != searchHistories.lastIndex) {
//                    Spacer(modifier = Modifier.height(4.dp))
//                    Divider(color = MaterialTheme.colorScheme.onSurface)
//                }
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
    onFocusedStateChange: (Boolean) -> Unit = {},

    startSearch: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {

    val focusManager = LocalFocusManager.current

    val isKeyboardOpen by keyboardAsState()


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
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AnimatedVisibility(visible = isSearchBarExpanded) {
                ClickableIcon(painter = painterResource(id = R.drawable.arrow_back_fill0_wght400_grad0_opsz48)) {
                    onBackClick()
                }
            }

            AnimatedVisibility(visible = !isSearchBarExpanded) {
                Spacer(modifier = Modifier.width(16.dp))
            }


            // search text input
            SimpleTextField(
                value = queryText,
                onValueChange = onQueryTextChange,
                modifier = Modifier.weight(1f),
                focusRequester = searchFocus,

//                onClick = {
//                    onSearchingStateChange(true)
//                    onClick()
//                },

//                isFocused = isFocused,
                onFocusedStateChange = { focused ->

                    if (focused) {
                        onSearchingStateChange(true)
                        onClick()
                    }

                    onFocusedStateChange(focused)
                },

                onSearchClick = {
                    startSearch()
                    onSearchingStateChange(false)
                }
            )
            Spacer(modifier = Modifier.width(8.dp))


            AnimatedVisibility(visible = isSearchBarExpanded) {
                Crossfade(targetState = isAdvancedSearchMenuOpen) { opening ->
                    if (opening) {
                        ClickableIcon(
                            painter = painterResource(id = R.drawable.expand_less_fill0_wght400_grad0_opsz48),
                            contentDescription = "expand less",
                            onClick = {
                                onAdvancedSearchMenuChange(false)
                                focusManager.clearFocus()
                            }
                        )
                    } else {
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

//            Crossfade(targetState = isKeyboardOpen) {
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
//    onClick: () -> Unit = {},

//    isFocused: Boolean, // !!! 新增
    onFocusedStateChange: (Boolean) -> Unit = {}, // !!! 新增

    onSearchClick: () -> Unit = {},


//    viewModel: MainViewModel = viewModel()
) {
    val colorScheme = MaterialTheme.colorScheme
    val textStyle = LocalTextStyle.current
    val textColor = textStyle.color.takeOrElse {
        colorScheme.onSurface
    }
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))
    val cursorColor = colorScheme.primary

    var textFieldValue by remember {
        mutableStateOf(TextFieldValue(value))
    }

//    Log.d("!!!", "SimpleTextField: textFieldValue = $textFieldValue, value = $value")

    BasicTextField(
        value = textFieldValue.copy(value),
        onValueChange = {
            onValueChange(it.text)
            textFieldValue = it
        },
        modifier = modifier
            .focusRequester(focusRequester)
            .onFocusChanged {
                onFocusedStateChange(it.isFocused)

                if (it.isFocused) {
                    textFieldValue =
                        textFieldValue.copy(selection = TextRange(textFieldValue.text.length))
                }
            },
        singleLine = true,
        textStyle = mergedTextStyle,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = { onSearchClick() }
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


@Composable
fun keyboardAsState(): State<Boolean> {
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    return rememberUpdatedState(isImeVisible)
}