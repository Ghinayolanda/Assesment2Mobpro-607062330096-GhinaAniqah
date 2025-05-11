package com.ghina0096.assessment2_mobpro.ui.theme.screen


import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ghina0096.assessment2_mobpro.R
import com.ghina0096.assessment2_mobpro.model.Resep
import com.ghina0096.assessment2_mobpro.navigation.Screen
import com.ghina0096.assessment2_mobpro.ui.theme.Assessment2_mobproTheme
import com.ghina0096.assessment2_mobpro.util.SettingsDataStore
import com.ghina0096.assessment2_mobpro.util.ViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val context = LocalContext.current
    val settings = remember { SettingsDataStore(context) }
    val scope = rememberCoroutineScope()

    // Ambil preferensi dari DataStore
    val isList by settings.layoutFlow.collectAsState(initial = true)

    // Gunakan state yang bisa dimodifikasi di Compose
    var showList by rememberSaveable { mutableStateOf(isList) }
    val colorIntState by settings.colorFlow.collectAsState(initial = Color.White.hashCode())


    val selectedColor = Color(colorIntState)
    val gridBorderColor = if (selectedColor == Color.White) Color(0xFFE0E0E0) else Color.White
    val cardColor = Color.White // isi card tetap putih

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(onClick = {
                        showList = !showList
                        scope.launch {
                            settings.saveLayout(showList)
                        }
                    }) {
                        Icon(
                            painter = painterResource(
                                if (showList) R.drawable.baseline_grid_view_24
                                else R.drawable.baseline_view_list_24
                            ),
                            contentDescription = if (showList) "Grid" else "List",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    ColorPickerDropdown { pickedColor ->
                        scope.launch {
                            settings.saveThemeColor(pickedColor.hashCode())
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.FormBaru.route)
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Tambah Resep",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    ) { innerPadding ->
        ScreenContent(
            showList = showList,
            modifier = Modifier
                .padding(innerPadding)
                .background(selectedColor),
            navController = navController,
            cardColor = cardColor,
            borderColor = gridBorderColor
        )
    }
}





@Composable
fun ScreenContent(showList: Boolean, modifier: Modifier=Modifier, navController: NavHostController, cardColor: Color, borderColor: Color) {
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: MainViewModel = viewModel(factory = factory)
    val data by viewModel.data.collectAsState()

    if (data.isEmpty()){
        Column (
            modifier = modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = stringResource(id= R.string.list_kosong))
        }
    }
    else {
        if (showList) {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 84.dp)
            ) {
                items(data) {
                    ListItem(
                        resep = it,
                        onClick = { navController.navigate(Screen.FormUbah.withId(it.id)) },
                        borderColor = borderColor
                    )
                }
            }
        }
        else {
            LazyVerticalStaggeredGrid(
                modifier = modifier.fillMaxSize(),
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(8.dp, 8.dp, 8.dp, 84.dp)
            ) {
                items(data) {
                    GridItem(
                        resep = it,
                        cardColor = cardColor,
                        borderColor = borderColor
                    ) {
                        navController.navigate(Screen.FormUbah.withId(it.id))
                    }
                }
            }
        }
    }
}



@Composable
fun ListItem(resep: Resep, onClick: () -> Unit, borderColor: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 4.dp), // kasih jarak antar card
        colors = CardDefaults.cardColors(
            containerColor = Color.White // isi card selalu putih
        ),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = resep.namaMasakan,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = resep.resep,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(text = resep.tanggal)
        }
    }
}



@Composable
fun GridItem(resep: Resep, cardColor: Color, borderColor: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        border = BorderStroke(1.dp, borderColor) // ← Gunakan warna dinamis
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = resep.namaMasakan,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = resep.resep,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
            Text(text = resep.tanggal)
        }
    }
}


@Composable
fun ColorPickerDropdown(onColorSelected: (Color) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    // 🎨 Warna pastel lucu
    val colors = listOf(
        Color(0xFFFFC1CC), // Pink pastel
        Color(0xFFFFF3B0), // Kuning pastel
        Color(0xFFB5EAD7), // Hijau pastel
        Color(0xFFB0E0E6), // Biru muda pastel
        Color(0xFFE0BBE4), // Ungu pastel
        Color(0xFFFFDAB9),  // Peach pastel
        Color.White
    )
    val colorNames = listOf("Pink", "Kuning", "Hijau", "Biru", "Ungu", "Peach", "Putih")

    IconButton(onClick = { expanded = true }) {
        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Pilih Warna")
    }

    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        colors.forEachIndexed { index, color ->
            DropdownMenuItem(
                onClick = {
                    onColorSelected(color)
                    expanded = false
                },
                text = { Text("Warna ${colorNames[index]}") }
            )
        }
    }
}





@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    Assessment2_mobproTheme {
        MainScreen(rememberNavController())
    }
}