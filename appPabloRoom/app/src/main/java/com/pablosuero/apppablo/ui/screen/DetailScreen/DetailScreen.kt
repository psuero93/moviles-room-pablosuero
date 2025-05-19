package com.pablosuero.apppablo.ui.screen.DetailScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.pablosuero.apppablo.ui.screen.ListScreen.MealDetailItem
import com.pablosuero.apppablo.ui.screen.ListScreen.ListViewModel
import com.pablosuero.apppablo.ui.theme.GreenDAM
import com.pablosuero.apppablo.ui.theme.OrangeDAM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    mealId: String,
    onBack: () -> Unit,
    listViewModel: ListViewModel
) {
    // Convertimos el id a Int (0 como fallback)
    val id = mealId.toIntOrNull() ?: 0

    // Estado local para loading y para el detalle
    var isLoading by remember { mutableStateOf(true) }
    var mealDetail by remember { mutableStateOf<MealDetailItem?>(null) }

    // Al arrancar cargamos el detalle desde Room
    LaunchedEffect(id) {
        mealDetail = listViewModel.getMealDetailById(id)
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        Modifier.fillMaxWidth(),
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
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GreenDAM
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                // Mientras cargamos, indicador centrado
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                // Si no hay datos, mensaje de error
                mealDetail == null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No se encontró la receta.",
                            fontSize = 18.sp,
                            color = Color.Gray
                        )
                    }
                }
                // Cuando ya tenemos datos, los mostramos
                else -> {
                    val meal = mealDetail!!
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        item {
                            // Nombre e imagen
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = meal.name,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                                AsyncImage(
                                    model = meal.imageUrl,
                                    contentDescription = "${meal.name} image",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                )
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        item {
                            // Tarjeta de ingredientes
                            ElevatedCard(
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(8.dp),
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.elevatedCardColors(containerColor = OrangeDAM)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "Ingredientes",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                    meal.ingredients.forEach { ingredient ->
                                        Text(
                                            text = "• $ingredient",
                                            fontSize = 16.sp,
                                            modifier = Modifier.padding(bottom = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
