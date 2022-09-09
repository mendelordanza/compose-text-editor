package com.highoutput.texteditor

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.TextFieldValue
import kotlin.random.Random

data class BlockData(
    val id: String = Random.nextInt(Integer.MAX_VALUE).toString(),
    val type: String = BlockOption.Paragraph.type,
    val data: Data,
)

abstract class Data {
    abstract var text: TextFieldValue?
}

data class ParagraphData(
    override var text: TextFieldValue? = null,
) : Data()

data class HeadingData(
    override var text: TextFieldValue? = null,
    val level: Int,
) : Data()

data class ListData(
    override var text: TextFieldValue? = null,
    val style: String = "unordered",
    val items: List<TextFieldValue>
) : Data()

data class TodoListData(
    override var text: TextFieldValue? = null,
    val items: List<TodoItem>
) : Data()

data class TodoItem(
    val id: String = Random.nextInt(Integer.MAX_VALUE).toString(),
    val checked: Boolean = false,
    val text: TextFieldValue = TextFieldValue()
)
