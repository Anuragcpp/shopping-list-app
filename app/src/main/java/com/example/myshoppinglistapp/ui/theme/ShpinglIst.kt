package com.example.myshoppinglistapp.ui.theme

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class shoppingItem(
    val id:Int,
    var name : String,
    var quantity : Int,
    var isEditing : Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun shopingListApp() {
    var sItem by remember { mutableStateOf(listOf<shoppingItem>()) }
    var showDialogue by remember{ mutableStateOf(false) }
    var itemName by remember{ mutableStateOf("") }
    var itemQuantity by remember{ mutableStateOf("") }
    
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                      showDialogue = true
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Add item")
        }

        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
        ){
                items(sItem){item ->
                    // TODO I don't understand this logic
                    if (item.isEditing){
                        shopingItemEditor(item =item , onEditCompleted = {
                            editedName , editedQuantity ->
                            sItem = sItem.map { it.copy(isEditing = false) }
                            val editedItem = sItem.find { it.id == item.id }
                            editedItem?.let {
                                it.name = editedName
                                it.quantity = editedQuantity
                            }
                        })
                    }else{
                        //TODO I don't understand this logic
                        shopingListItem(
                            item = item,
                            onEditClick = {
                            sItem = sItem.map { it.copy(isEditing = it.id == item.id) }
                            },
                            onDeletClick = {
                                sItem = sItem - item
                            })
                    }
            }
        }


    }
    
    if(showDialogue){
       AlertDialog(
           onDismissRequest = { showDialogue = false},
           confirmButton = {
                           Row (modifier = Modifier
                               .fillMaxWidth()
                               .padding(10.dp),
                               horizontalArrangement = Arrangement.SpaceBetween
                           ){
                               Button(onClick = {
                                   if(itemName.isNotEmpty()){
                                       val newItem = shoppingItem(
                                           id = sItem.size +1,
                                           name = itemName,
                                           quantity = itemQuantity.toIntOrNull() ?: 0,
                                           isEditing = false
                                       )
                                       sItem += newItem
                                       itemName = ""
                                       itemQuantity = ""
                                       showDialogue = false
                                   }else{

                                   }
                               }) {
                                   Text(text = "Add")
                               }
                               Button(onClick = {showDialogue = false }) {
                                   Text(text = "Cancel")
                               }
                           }
           },
           title = { Text(text = "Add shoping items")},
           text = {
               Column {
                   OutlinedTextField(
                       value =itemName ,
                       onValueChange = { itemName = it},
                       singleLine = true,
                       modifier = Modifier.fillMaxWidth()
                       )
                   
                   Spacer(modifier = Modifier.height(15.dp))

                   OutlinedTextField(
                       value =itemQuantity ,
                       onValueChange = { itemQuantity = it},
                       singleLine = true,
                       modifier = Modifier.fillMaxWidth()
                   )
               }
           }
       )
    }

}

@Composable
fun shopingListItem(
    item : shoppingItem,
    onEditClick : () -> Unit,
    onDeletClick : () -> Unit
) {
    Row (horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(2.dp, Color.Cyan),
                shape = RoundedCornerShape(20)
            )){
        Text(text = item.name, modifier = Modifier.padding(8.dp))
        
        Text(text = "Qty : ${item.quantity}", modifier = Modifier.padding(8.dp))

        Row (modifier = Modifier.padding(8.dp)){
            IconButton(onClick = { onEditClick }) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit button")
            }
            IconButton(onClick = { onDeletClick}) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete button")
            }

        }


        
    }
    
}

@Composable
fun shopingItemEditor(
    item : shoppingItem,
    onEditCompleted : (String,Int) -> Unit
) {
   var editedName by remember{ mutableStateOf(item.name) }
   var editQuantity by remember{ mutableStateOf(item.quantity.toString()) }
    var isEdited by remember{ mutableStateOf(item.isEditing) }

    Row (modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(8.dp)
        , horizontalArrangement = Arrangement.SpaceEvenly){

        Column {
            BasicTextField(value = editedName,
                //change form  onValueChage ={It} to editedName = it;
                onValueChange = { it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)) {

            }

            BasicTextField(value = editQuantity,
                //change form  onValueChage ={It} to editQuantity = it;
                onValueChange = { it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)) {

            }

            Button(onClick = {
                isEdited = false
                onEditCompleted(editedName,editQuantity.toIntOrNull() ?: 1)
            }) {
                Text(text = "Save")
            }
        }
    }
}


