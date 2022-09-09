package com.highoutput.texteditor

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

sealed class BlockOption(val type: String){
    object Header : BlockOption("header")
    object Paragraph : BlockOption("paragraph")
    object List : BlockOption("list")
    object TodoList : BlockOption("todoList")
    object Quote : BlockOption("quote")
    object Image : BlockOption("image")
}