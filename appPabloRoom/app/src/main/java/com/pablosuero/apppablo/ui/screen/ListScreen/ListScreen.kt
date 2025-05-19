package com.pablosuero.apppablo.ui.screen.ListScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.pablosuero.apppablo.data.MealItem
import com.pablosuero.apppablo.ui.theme.GreenDAM
import com.pablosuero.apppablo.ui.theme.OrangeDAM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    listViewModel: ListViewModel,
    navigateToDetail: (String) -> Unit
) {
    // Estado observado desde LiveData
    val meals by listViewModel.lista.observeAsState(emptyList())
    val loading by listViewModel.progressBar.observeAsState(false)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "DAM MEALS",
                            color = Color.White,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 33.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GreenDAM
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                loading -> {
                    // Indicador de carga centrado
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                meals.isEmpty() -> {
                    // Mensaje cuando no hay datos
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay comidas disponibles.",
                            fontSize = 18.sp,
                            color = Color.Gray
                        )
                    }
                }
                else -> {
                    // Lista de comidas
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(items = meals) { meal ->
                            MealCard(
                                meal = meal,
                                onClick = { navigateToDetail(meal.id.toString()) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MealCard(meal: MealItem, onClick: () -> Unit) {
    ElevatedCard(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = OrangeDAM)
    ) {
        Column {
            AsyncImage(
                model = meal.imageURL,
                contentDescription = meal.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Text(
                text = meal.name,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
