package com.highoutput.texteditor

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.getSelectedText
import androidx.compose.ui.text.input.getTextBeforeSelection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.highoutput.texteditor.ui.theme.TextEditorTheme

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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Greeting(name: String) {
    val focusManager = LocalFocusManager.current
    var currentIndexFocus by remember {
        mutableStateOf(-1)
    }

    val blockData = remember {
        mutableStateListOf(
            BlockData(
                data = ParagraphData(
                    text = TextFieldValue(""),
                )
            ),
        )
    }

    val addNewLine: (type: String, data: Data) -> Unit = { type, data ->
        blockData.add(
            BlockData(
                type = type,
                data = data,
            )
        )
    }

    val backspace: (index: Int, event: KeyEvent) -> Boolean = { index, event ->
        if (event.key == Key.Backspace &&
            blockData[index].data.text?.text?.isEmpty() == true
        ) {
            blockData.removeAt(index)
            focusManager.moveFocus(FocusDirection.Up)
            true
        } else {
            false
        }
    }

    LazyColumn {
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
                                        backspace(blockIndex, event)
                                    },
                                type = block.type,
                                value = block.data.text ?: TextFieldValue(""),
                                onValueChange = {
                                    blockData[blockIndex] = block.copy(
                                        data = HeadingData(
                                            text = it,
                                            level = 1,
                                        )
                                    )
                                },
                                textStyle = TextStyle(
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Green
                                ),
                                onNext = {
                                    addNewLine(
                                        BlockOption.Paragraph.type,
                                        ParagraphData(
                                            text = TextFieldValue(""),
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
                                        backspace(blockIndex, event)
                                    },
                                type = block.type,
                                value = block.data.text ?: TextFieldValue(""),
                                onValueChange = {
                                    blockData[blockIndex] = block.copy(
                                        data = HeadingData(
                                            text = it,
                                            level = 2,
                                        )
                                    )
                                },
                                textStyle = TextStyle(
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Cyan
                                ),
                                onNext = {
                                    addNewLine(
                                        BlockOption.Paragraph.type,
                                        ParagraphData(
                                            text = TextFieldValue(""),
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
                                        backspace(blockIndex, event)
                                    },
                                type = block.type,
                                value = block.data.text ?: TextFieldValue(""),
                                onValueChange = {
                                    blockData[blockIndex] = block.copy(
                                        data = HeadingData(
                                            text = it,
                                            level = 3,
                                        )
                                    )
                                },
                                textStyle = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Blue
                                ),
                                onNext = {
                                    addNewLine(
                                        BlockOption.Paragraph.type,
                                        ParagraphData(
                                            text = TextFieldValue(""),
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
                                        backspace(blockIndex, event)
                                    },
                                type = block.type,
                                value = block.data.text ?: TextFieldValue(""),
                                onValueChange = {
                                    blockData[blockIndex] = block.copy(
                                        data = HeadingData(
                                            text = it,
                                            level = 4,
                                        )
                                    )
                                },
                                textStyle = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Blue
                                ),
                                onNext = {
                                    addNewLine(
                                        BlockOption.Paragraph.type,
                                        ParagraphData(
                                            text = TextFieldValue(""),
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
                                backspace(blockIndex, event)
                            }
                            .onFocusChanged {
                                if(it.hasFocus) {
                                    currentIndexFocus = blockIndex
                                }
                            },
                        value = block.data.text ?: TextFieldValue(""),
                        onValueChange = {
                            blockData[blockIndex] = block.copy(
                                data = ParagraphData(
                                    text = it,
                                )
                            )
                        },
                        textStyle = TextStyle(
                            fontSize = 14.sp,
                            color = Color.Red
                        ),
                        onNext = {
                            addNewLine(
                                BlockOption.Paragraph.type,
                                ParagraphData(
                                    text = TextFieldValue(""),
                                ),
                            )
                        },
                    )
                }
                BlockOption.List.type -> {
                    val listData = block.data as ListData
                    Column {
                        listData.items.forEachIndexed { itemIndex, item ->
                            CustomTextField(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                    .fillMaxWidth()
                                    .onKeyEvent { event ->
                                        if (event.type == KeyEventType.KeyUp &&
                                            event.key == Key.Backspace &&
                                            item.text.isEmpty()
                                        ) {
                                            if (listData.items.size == 1) {
                                                blockData.removeAt(blockIndex)
                                            } else {
                                                blockData[blockIndex] = block.copy(
                                                    data = listData.copy(
                                                        items = listData.items - listData.items[itemIndex]
                                                    )
                                                )
                                            }
                                            focusManager.moveFocus(FocusDirection.Up)
                                            true
                                        } else {
                                            false
                                        }
                                    },
                                verticalAlignment = Alignment.Top,
                                value = item,
                                onNext = {
                                    blockData[blockIndex] = block.copy(
                                        data = listData.copy(
                                            items = listData.items + TextFieldValue(""),
                                        )
                                    )
                                },
                                onValueChange = { text ->
                                    val newItems =
                                        listData.items.mapButReplace(listData.items[itemIndex],
                                            text)
                                    blockData[blockIndex] = block.copy(
                                        data = listData.copy(
                                            items = newItems
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
                        todoListData.items.forEachIndexed { itemIndex, item ->
                            CustomTextField(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                    .fillMaxWidth()
                                    .onKeyEvent { event ->
                                        if (event.type == KeyEventType.KeyUp &&
                                            event.key == Key.Backspace &&
                                            item.text.text.isEmpty()
                                        ) {
                                            if (todoListData.items.size == 1) {
                                                blockData.removeAt(blockIndex)
                                            } else {
                                                blockData[blockIndex] = block.copy(
                                                    data = todoListData.copy(
                                                        items = todoListData.items - item
                                                    )
                                                )
                                            }
                                            focusManager.moveFocus(FocusDirection.Up)
                                            true
                                        } else {
                                            false
                                        }
                                    },
                                value = item.text,
                                onNext = {
                                    val newItem = TodoItem()
                                    blockData[blockIndex] = block.copy(
                                        data = todoListData.copy(
                                            items = todoListData.items + newItem
                                        )
                                    )
                                },
                                onValueChange = { text ->
                                    val newItems =
                                        todoListData.items.mapButReplace(
                                            todoListData.items[itemIndex],
                                            item.copy(text = text),
                                        )
                                    blockData[blockIndex] = block.copy(
                                        data = todoListData.copy(
                                            items = newItems
                                        )
                                    )
                                },
                                leadingIcon = {
                                    Checkbox(
                                        checked = item.checked,
                                        onCheckedChange = { checked ->
                                            val newItems =
                                                todoListData.items.mapButReplace(
                                                    todoListData.items[itemIndex],
                                                    item.copy(checked = checked),
                                                )
                                            blockData[blockIndex] = block.copy(
                                                data = todoListData.copy(
                                                    items = newItems
                                                )
                                            )
                                        },
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
        item {
            Column {
                Button(
                    onClick = {
                        addNewLine(
                            BlockOption.Header.type,
                            HeadingData(
                                text = TextFieldValue(""),
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
                                text = TextFieldValue(""),
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
                                text = TextFieldValue(""),
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
                                text = TextFieldValue(""),
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
                                items = mutableListOf(
                                    TextFieldValue(""),
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
                                items = mutableListOf(
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
                        blockData[currentIndexFocus].data.text?.let {
                            blockData[currentIndexFocus] = blockData[currentIndexFocus].copy(
                                data = ParagraphData(
                                    text = changeToBold(it)
                                )
                            )
                        }

                    },
                ) {
                    Text("Bold")
                }
                Button(
                    onClick = {
                        blockData[currentIndexFocus].data.text?.let {
                            blockData[currentIndexFocus] = blockData[currentIndexFocus].copy(
                                data = ParagraphData(
                                    text = changeToItalic(it),
                                )
                            )
                        }

                    },
                ) {
                    Text("Italic")
                }
                Button(
                    onClick = {
                        val gson = Gson()
                        val tutorials = gson.toJsonTree(blockData.toList())
                        Log.d("JSON", "$tutorials")
                    },
                ) {
                    Text("Submit")
                }
            }
        }
    }
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
    return textFVal.copy(annotatedString = txtAnnotatedBuilder.toAnnotatedString())
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


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TextEditorTheme {
        Greeting("Android")
    }
}