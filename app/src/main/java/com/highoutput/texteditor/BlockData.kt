package com.highoutput.texteditor

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.TextFieldValue
import java.io.File
import kotlin.random.Random

data class BlockData(
    val id: String = Random.nextInt(Integer.MAX_VALUE).toString(),
    val type: String = BlockOption.Paragraph.type,
    val data: Data,
) {
    fun formatJson(): Any  {
        return when (type) {
            BlockOption.Header.type -> {
                this.copy(
                    data = HeadingData(
                        level = (this.data as HeadingData).level,
                        text = this.data.textFieldValue?.text,
                    )
                )
            }
            BlockOption.Paragraph.type -> {
                this.copy(
                    data = ParagraphData(
                        text = this.data.textFieldValue?.text,
                    )
                )
            }
            BlockOption.List.type -> {
                val list = this.data as ListData
                this.copy(
                    data = ListData(
                        style = list.style,
                        items = list.listItems?.map {
                            it.text.text
                        }
                    )
                )
            }
            BlockOption.TodoList.type -> {
                val todoList = this.data as TodoListData
                this.copy(
                    data = TodoListData(
                        items = todoList.todoItems?.map {
                            mapOf(
                                "checked" to it.checked,
                                "text" to it.text.text,
                            )
                        },
                    )
                )
            }
            else -> {}
        }
    }
}

abstract class Data {
    abstract var textFieldValue: TextFieldValue?
    abstract var text: String?
}

data class ImageData(
    override var textFieldValue: TextFieldValue? = null,
    override var text: String? = null,
    val file: File? = null,
) : Data()

data class ParagraphData(
    override var textFieldValue: TextFieldValue? = null,
    override var text: String? = null,
) : Data()

data class HeadingData(
    override var textFieldValue: TextFieldValue? = null,
    override var text: String? = null,
    val level: Int,
) : Data()

data class ListData(
    override var textFieldValue: TextFieldValue? = null,
    override var text: String? = null,
    val style: String = "unordered",
    val listItems: List<ListItem>? = null,
    val items: List<String>? = null
) : Data()

data class TodoListData(
    override var textFieldValue: TextFieldValue? = null,
    override var text: String? = null,
    val todoItems: List<TodoItem>? = null,
    val items: List<Map<String, Any>>? = null
) : Data()

data class TodoItem(
    val id: String = Random.nextInt(Integer.MAX_VALUE).toString(),
    val checked: Boolean = false,
    val text: TextFieldValue = TextFieldValue()
)

data class ListItem(
    val id: String = Random.nextInt(Integer.MAX_VALUE).toString(),
    val text: TextFieldValue = TextFieldValue()
)