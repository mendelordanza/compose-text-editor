package com.highoutput.texteditor

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.getSelectedText
import androidx.compose.ui.text.input.getTextBeforeSelection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.SubcomposeAsyncImage
import coil.memory.MemoryCache
import coil.request.ImageRequest
import com.google.gson.Gson
import com.highoutput.texteditor.ui.theme.TextEditorTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TextEditorTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {
                    Greeting("Android")
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun Greeting(name: String) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    var currentIndexFocus by remember {
        mutableStateOf(-1)
    }
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }

    val blockData = remember {
        mutableStateListOf(
            BlockData(
                data = ParagraphData(
                    textFieldValue = TextFieldValue(""),
                )
            )
        )
    }

    val addNewLine: (type: String, data: Data) -> Unit = { type, data ->
        blockData.add(
            BlockData(
                type = type,
                data = data,
            )
        )
        scope.launch {
            sheetState.animateTo(ModalBottomSheetValue.Hidden)
        }
    }

    val backspace: (type: String, blockIndex: Int, event: KeyEvent) -> Boolean =
        { type, blockIndex, event ->
            if (event.key == Key.Backspace &&
                blockData[blockIndex].data.textFieldValue?.text?.isEmpty() == true
            ) {
                if (blockData.size > 1) {
                    blockData.removeAt(blockIndex)
                }
                focusManager.moveFocus(FocusDirection.Up)
                true
            } else {
                false
            }
        }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            val file = uri?.let {
                FileHelper.fileFromContentUri(
                    context = context,
                    contentUri = it
                )
            }
            addNewLine(
                BlockOption.Image.type,
                ImageData(
                    file = file
                )
            )
        },
    )

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            Box(
                modifier = Modifier
                    .defaultMinSize(1.dp)
                    .clip(shape = RoundedCornerShape(8.dp)),
            ) {
                Column {
                    Button(
                        onClick = {
                            addNewLine(
                                BlockOption.Header.type,
                                HeadingData(
                                    textFieldValue = TextFieldValue(""),
                                    level = 1,
                                ),
                            )
                        },
                    ) {
                        Text("Heading1")
                    }
                    Button(
                        onClick = {
                            addNewLine(
                                BlockOption.Header.type,
                                HeadingData(
                                    textFieldValue = TextFieldValue(""),
                                    level = 2,
                                ),
                            )
                        },
                    ) {
                        Text("Heading2")
                    }
                    Button(
                        onClick = {
                            addNewLine(
                                BlockOption.Header.type,
                                HeadingData(
                                    textFieldValue = TextFieldValue(""),
                                    level = 3,
                                ),
                            )
                        },
                    ) {
                        Text("Heading3")
                    }
                    Button(
                        onClick = {
                            addNewLine(
                                BlockOption.Header.type,
                                HeadingData(
                                    textFieldValue = TextFieldValue(""),
                                    level = 4,
                                ),
                            )
                        },
                    ) {
                        Text("Heading4")
                    }
                    Button(
                        onClick = {
                            addNewLine(
                                BlockOption.List.type,
                                ListData(
                                    style = "unordered",
                                    listItems = mutableListOf(
                                        ListItem(),
                                    )
                                ),
                            )
                        },
                    ) {
                        Text("List")
                    }
                    Button(
                        onClick = {
                            addNewLine(
                                BlockOption.TodoList.type,
                                TodoListData(
                                    todoItems = mutableListOf(
                                        TodoItem()
                                    )
                                ),
                            )
                        },
                    ) {
                        Text("Todo List")
                    }
                    Button(
                        onClick = {
                            galleryLauncher.launch("image/*")
                        },
                    ) {
                        Text("Image")
                    }
                    Button(
                        onClick = {
                            val format = blockData.map { block ->
                                block.formatJson()
                            }
                            val gson = Gson()
                            val tutorials = gson.toJsonTree(format.toList())
                            Log.d("JSON", "$tutorials")
                        },
                    ) {
                        Text("Submit")
                    }
                }
            }
        },
        content = {
            Column {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            val block = blockData.getOrNull(currentIndexFocus)
                            if (block != null) {
                                when (block.type) {
                                    BlockOption.Paragraph.type -> {
                                        if (block.data.textFieldValue?.text?.isNotEmpty() == true) {
                                            addNewLine(
                                                BlockOption.Paragraph.type,
                                                ParagraphData(
                                                    textFieldValue = TextFieldValue("")
                                                )
                                            )
                                        }
                                    }
                                    else -> {
                                        addNewLine(
                                            BlockOption.Paragraph.type,
                                            ParagraphData(
                                                textFieldValue = TextFieldValue("")
                                            )
                                        )
                                    }
                                }
                            }
                        }
                ) {
                    items(blockData.size) { blockIndex ->
                        val block = blockData[blockIndex]
                        when (block.type) {
                            BlockOption.Header.type -> {
                                val header = block.data as HeadingData
                                when (header.level) {
                                    1 -> {
                                        CustomTextField(
                                            modifier = Modifier
                                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                                .onKeyEvent { event ->
                                                    backspace(block.type, blockIndex, event)
                                                }
                                                .onFocusChanged {
                                                    if (it.hasFocus) {
                                                        currentIndexFocus = blockIndex
                                                    }
                                                },
                                            type = block.type,
                                            value = block.data.textFieldValue ?: TextFieldValue(""),
                                            onValueChange = {
                                                blockData[blockIndex] = block.copy(
                                                    data = HeadingData(
                                                        textFieldValue = it,
                                                        level = 1,
                                                    )
                                                )
                                            },
                                            textStyle = TextStyle(
                                                fontSize = 30.sp,
                                                fontWeight = FontWeight.SemiBold,
                                            ),
                                            onNext = {
                                                addNewLine(
                                                    BlockOption.Paragraph.type,
                                                    ParagraphData(
                                                        textFieldValue = TextFieldValue(""),
                                                    ),
                                                )
                                            },
                                        )
                                    }
                                    2 -> {
                                        CustomTextField(
                                            modifier = Modifier
                                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                                .onKeyEvent { event ->
                                                    backspace(block.type, blockIndex, event)
                                                },
                                            type = block.type,
                                            value = block.data.textFieldValue ?: TextFieldValue(""),
                                            onValueChange = {
                                                blockData[blockIndex] = block.copy(
                                                    data = HeadingData(
                                                        textFieldValue = it,
                                                        level = 2,
                                                    )
                                                )
                                            },
                                            textStyle = TextStyle(
                                                fontSize = 26.sp,
                                                fontWeight = FontWeight.SemiBold,
                                            ),
                                            onNext = {
                                                addNewLine(
                                                    BlockOption.Paragraph.type,
                                                    ParagraphData(
                                                        textFieldValue = TextFieldValue(""),
                                                    ),
                                                )
                                            },
                                        )
                                    }
                                    3 -> {
                                        CustomTextField(
                                            modifier = Modifier
                                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                                .onKeyEvent { event ->
                                                    backspace(block.type, blockIndex, event)
                                                },
                                            type = block.type,
                                            value = block.data.textFieldValue ?: TextFieldValue(""),
                                            onValueChange = {
                                                blockData[blockIndex] = block.copy(
                                                    data = HeadingData(
                                                        textFieldValue = it,
                                                        level = 3,
                                                    )
                                                )
                                            },
                                            textStyle = TextStyle(
                                                fontSize = 24.sp,
                                                fontWeight = FontWeight.SemiBold,
                                            ),
                                            onNext = {
                                                addNewLine(
                                                    BlockOption.Paragraph.type,
                                                    ParagraphData(
                                                        textFieldValue = TextFieldValue(""),
                                                    ),
                                                )
                                            },
                                        )
                                    }
                                    4 -> {
                                        CustomTextField(
                                            modifier = Modifier
                                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                                .onKeyEvent { event ->
                                                    backspace(block.type, blockIndex, event)
                                                },
                                            type = block.type,
                                            value = block.data.textFieldValue ?: TextFieldValue(""),
                                            onValueChange = {
                                                blockData[blockIndex] = block.copy(
                                                    data = HeadingData(
                                                        textFieldValue = it,
                                                        level = 4,
                                                    )
                                                )
                                            },
                                            textStyle = TextStyle(
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.SemiBold,
                                            ),
                                            onNext = {
                                                addNewLine(
                                                    BlockOption.Paragraph.type,
                                                    ParagraphData(
                                                        textFieldValue = TextFieldValue(""),
                                                    ),
                                                )
                                            },
                                        )
                                    }
                                }
                            }
                            BlockOption.Paragraph.type -> {
                                CustomTextField(
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                        .onKeyEvent { event ->
                                            backspace(block.type, blockIndex, event)
                                        }
                                        .onFocusChanged {
                                            if (it.hasFocus) {
                                                currentIndexFocus = blockIndex
                                            }
                                        },
                                    value = block.data.textFieldValue ?: TextFieldValue(""),
                                    onValueChange = {
                                        if (it.text.startsWith("- ")) {
                                            blockData[blockIndex] = block.copy(
                                                type = BlockOption.List.type,
                                                data = ListData(
                                                    style = "unordered",
                                                    listItems = mutableListOf(
                                                        ListItem(),
                                                    )
                                                )
                                            )
                                        } else {
                                            blockData[blockIndex] = block.copy(
                                                data = ParagraphData(
                                                    textFieldValue = it,
                                                )
                                            )
                                        }
                                    },
                                    textStyle = TextStyle(
                                        fontSize = 14.sp,
                                    ),
                                    onNext = {
                                        addNewLine(
                                            BlockOption.Paragraph.type,
                                            ParagraphData(
                                                textFieldValue = TextFieldValue(""),
                                            ),
                                        )
                                    },
                                )
                            }
                            BlockOption.List.type -> {
                                val listData = block.data as ListData
                                Column {
                                    listData.listItems?.forEachIndexed { itemIndex, item ->
                                        CustomTextField(
                                            modifier = Modifier
                                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                                .fillMaxWidth()
                                                .onKeyEvent { event ->
                                                    if (event.key == Key.Backspace &&
                                                        item.text.text.isEmpty()
                                                    ) {
                                                        if (listData.listItems.size == 1) {
                                                            blockData.removeAt(blockIndex)
                                                        } else {
                                                            blockData[blockIndex] = block.copy(
                                                                data = listData.copy(
                                                                    listItems = listData.listItems - listData.listItems[itemIndex]
                                                                )
                                                            )
                                                            focusManager.moveFocus(FocusDirection.Up)
                                                        }
                                                        true
                                                    } else {
                                                        false
                                                    }
                                                }
                                                .onFocusChanged {
                                                    if (it.hasFocus) {
                                                        currentIndexFocus = blockIndex
                                                    }
                                                },
                                            verticalAlignment = Alignment.Top,
                                            value = item.text,
                                            onNext = {
                                                val listItem = ListItem()
                                                blockData[blockIndex] = block.copy(
                                                    data = listData.copy(
                                                        listItems = listData.listItems + listItem,
                                                    )
                                                )
                                            },
                                            onValueChange = { text ->
                                                val newItems =
                                                    listData.listItems.mapButReplace(
                                                        listData.listItems[itemIndex],
                                                        item.copy(text = text),
                                                    )
                                                blockData[blockIndex] = block.copy(
                                                    data = listData.copy(
                                                        listItems = newItems
                                                    )
                                                )
                                            },
                                            leadingIcon = {
                                                Icon(Icons.Filled.Circle,
                                                    contentDescription = "")
                                            }
                                        )
                                    }
                                }
                            }
                            BlockOption.TodoList.type -> {
                                val todoListData = block.data as TodoListData
                                Column {
                                    todoListData.todoItems?.forEachIndexed { itemIndex, item ->
                                        CustomTextField(
                                            modifier = Modifier
                                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                                .fillMaxWidth()
                                                .onKeyEvent { event ->
                                                    if (event.type == KeyEventType.KeyUp &&
                                                        event.key == Key.Backspace &&
                                                        item.text.text.isEmpty()
                                                    ) {
                                                        if (todoListData.todoItems.size == 1) {
                                                            blockData.removeAt(blockIndex)
                                                        } else {
                                                            blockData[blockIndex] = block.copy(
                                                                data = todoListData.copy(
                                                                    todoItems = todoListData.todoItems - item
                                                                )
                                                            )
                                                            focusManager.moveFocus(FocusDirection.Up)
                                                        }
                                                        true
                                                    } else {
                                                        false
                                                    }
                                                }
                                                .onFocusChanged {
                                                    if (it.hasFocus) {
                                                        currentIndexFocus = blockIndex
                                                    }
                                                },
                                            value = item.text,
                                            onNext = {
                                                val newItem = TodoItem()
                                                blockData[blockIndex] = block.copy(
                                                    data = todoListData.copy(
                                                        todoItems = todoListData.todoItems + newItem
                                                    )
                                                )
                                            },
                                            onValueChange = { text ->
                                                val newItems =
                                                    todoListData.todoItems.mapButReplace(
                                                        todoListData.todoItems[itemIndex],
                                                        item.copy(text = text),
                                                    )
                                                blockData[blockIndex] = block.copy(
                                                    data = todoListData.copy(
                                                        todoItems = newItems
                                                    )
                                                )
                                            },
                                            leadingIcon = {
                                                Checkbox(
                                                    checked = item.checked,
                                                    onCheckedChange = { checked ->
                                                        val newItems =
                                                            todoListData.todoItems.mapButReplace(
                                                                todoListData.todoItems[itemIndex],
                                                                item.copy(checked = checked),
                                                            )
                                                        blockData[blockIndex] = block.copy(
                                                            data = todoListData.copy(
                                                                todoItems = newItems
                                                            )
                                                        )
                                                    },
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                            BlockOption.Image.type -> {
                                val imageData = block.data as ImageData
                                RenderPhoto(url = imageData.file)
                            }
                        }
                    }
                }
                Row(
                ) {
                    TextButton(
                        onClick = {
                            blockData[currentIndexFocus].data.textFieldValue?.let {
                                blockData[currentIndexFocus] =
                                    blockData[currentIndexFocus].copy(
                                        data = ParagraphData(
                                            textFieldValue = changeToBold(it)
                                        )
                                    )
                            }
                        },
                    ) {
                        Text("B", fontWeight = FontWeight.Bold)
                    }
                    TextButton(
                        onClick = {
                            blockData[currentIndexFocus].data.textFieldValue?.let {
                                blockData[currentIndexFocus] =
                                    blockData[currentIndexFocus].copy(
                                        data = ParagraphData(
                                            textFieldValue = changeToItalic(it),
                                        )
                                    )
                            }
                        },
                    ) {
                        Text("I", fontStyle = FontStyle.Italic)
                    }
                    TextButton(
                        onClick = {
                            scope.launch {
                                sheetState.animateTo(ModalBottomSheetValue.Expanded)
                            }
                        },
                    ) {
                        Icon(Icons.Filled.KeyboardArrowDown,
                            contentDescription = "ic_arrow_down")
                    }
                }
            }
        }
    )

}

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    type: String = "",
    value: TextFieldValue,
    leadingIcon: @Composable (() -> Unit)? = null,
    textStyle: TextStyle = LocalTextStyle.current,
    onNext: KeyboardActionScope.() -> Unit,
    onValueChange: (TextFieldValue) -> Unit,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
) {
    val focusRequester = FocusRequester()

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    BasicTextField(
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
        ),
        keyboardActions = KeyboardActions(
            onNext = onNext,
        ),
        textStyle = textStyle,
        value = value,
        onValueChange = onValueChange,
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = verticalAlignment,
            ) {
                if (leadingIcon != null) {
                    Box {
                        leadingIcon()
                    }
                }
                Box {
                    if (value.text.isEmpty()) {
                        Text(
                            text = type,
                            style = textStyle
                        )
                    }
                    innerTextField()
                }
            }
        },
    )
}

private fun changeToBold(textFVal: TextFieldValue): TextFieldValue {
    val txtAnnotatedBuilder = AnnotatedString.Builder()
    val realStartIndex = textFVal.getTextBeforeSelection(textFVal.text.length).length
    val endIndex = realStartIndex + textFVal.getSelectedText().length
    txtAnnotatedBuilder.append(textFVal.annotatedString)
    val myStyle = SpanStyle(
        fontWeight = FontWeight.Bold,
    )
    txtAnnotatedBuilder.addStyle(myStyle, realStartIndex, endIndex)
    return textFVal.copy(
        annotatedString = txtAnnotatedBuilder.toAnnotatedString(),
        selection = TextRange(realStartIndex, endIndex),
    )
}

private fun changeToItalic(textFVal: TextFieldValue): TextFieldValue {
    val txtAnnotatedBuilder = AnnotatedString.Builder()
    val realStartIndex = textFVal.getTextBeforeSelection(textFVal.text.length).length
    val endIndex = realStartIndex + textFVal.getSelectedText().length
    txtAnnotatedBuilder.append(textFVal.annotatedString)
    val myStyle = SpanStyle(
        fontStyle = FontStyle.Italic,
    )
    txtAnnotatedBuilder.addStyle(myStyle, realStartIndex, endIndex)
    return textFVal.copy(annotatedString = txtAnnotatedBuilder.toAnnotatedString())
}

@Composable
fun RenderPhoto(
    url: Any?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .memoryCache {
            MemoryCache.Builder(context)
                .maxSizePercent(0.50)
                .build()
        }
        .build()

    SubcomposeAsyncImage(
        model = ImageRequest.Builder(context)
            .data(url)
            .crossfade(true)
            .build(),
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.LightGray),
            )
        },
        imageLoader = imageLoader,
        contentDescription = "",
        contentScale = contentScale,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TextEditorTheme {
        Greeting("Android")
    }
}