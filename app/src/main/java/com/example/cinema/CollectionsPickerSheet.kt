package com.example.cinema

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cinema.domain.model.Film

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionsPickerSheet(appContainer: AppContainer, film: Film, onDismiss: () -> Unit) {
    val vm: CollectionsPickerViewModel = viewModel(
        factory = CollectionsPickerViewModelFactory(appContainer.provideCollectionsRepository(), film)
    )
    val items = vm.items.collectAsState(initial = emptyList()).value
    val newName = remember { mutableStateOf("") }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Добавить в коллекции")
            LazyColumn(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                items(items) { item: CollectionItemUi ->
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                        Checkbox(checked = item.isChecked, onCheckedChange = { vm.toggle(item.id) })
                        Text("${item.name} (${item.count})", modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }
            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                OutlinedTextField(value = newName.value, onValueChange = { newName.value = it }, label = { Text("Новая коллекция") })
                Button(onClick = { if (newName.value.isNotBlank()) vm.createAndSelect(newName.value) }, modifier = Modifier.padding(start = 8.dp)) { Text("Создать") }
            }
            Button(onClick = onDismiss, modifier = Modifier.padding(top = 12.dp)) { Text("Готово") }
        }
    }
}


