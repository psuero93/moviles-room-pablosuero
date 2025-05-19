package com.pablosuero.apppablo.ui.screen.ListScreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pablosuero.apppablo.data.db.MealDB
import com.pablosuero.apppablo.data.db.MealDao
import com.pablosuero.apppablo.data.MealItem
import com.pablosuero.apppablo.data.repositories.RemoteConnection
import com.pablosuero.apppablo.data.repositories.models.Meal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Modelo de UI para la pantalla de detalle:
 * contiene id, nombre, URL de imagen e ingredientes.
 */
data class MealDetailItem(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val ingredients: List<String>
)

/**
 * ViewModel que gestiona la carga de datos desde la BBDD Room o la API si está vacía,
 * y además provee un método para obtener el detalle de un meal por su id.
 */
class ListViewModel(private val mealDao: MealDao) : ViewModel() {
    private val _lista = MutableLiveData<List<MealItem>>()
    val lista: LiveData<List<MealItem>> = _lista

    private val _progressBar = MutableLiveData<Boolean>(false)
    val progressBar: LiveData<Boolean> = _progressBar

    init {
        _progressBar.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val count = mealDao.mealCount()
                if (count == 0) {
                    // Si no hay datos, los coge de la API y guarda
                    val fetchedItems = mutableListOf<MealItem>()
                    repeat(10) {
                        try {
                            val response = RemoteConnection.service.getRandomMeal()
                            response.meals?.firstOrNull()?.let { meal ->
                                val entity = meal.toEntity()
                                mealDao.insertMeals(listOf(entity))
                                fetchedItems.add(entity.toMealItem())
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    _lista.postValue(fetchedItems)
                } else {
                    // Ya hay datos, los leemos de la BBDD
                    val stored = mealDao.getAll()
                    _lista.postValue(stored.map { it.toMealItem() })
                }
                _progressBar.postValue(false)
            }
        }
    }

    /**
     * Recupera de Room un registro por su id y lo transforma en MealDetailItem.
     */
    suspend fun getMealDetailById(id: Int): MealDetailItem? =
        withContext(Dispatchers.IO) {
            mealDao.findById(id)?.toDetailItem()
        }

    /**
     * Conversión de la entidad Room a objeto de UI para listado
     */
    private fun MealDB.toMealItem(): MealItem = MealItem(
        id = id,
        name = name,
        imageURL = imageUrl
    )

    /**
     * Conversión de la entidad Room a objeto de UI para detalle
     */
    private fun MealDB.toDetailItem(): MealDetailItem = MealDetailItem(
        id = id,
        name = name,
        imageUrl = imageUrl,
        ingredients = ingredients
    )

    /**
     * Conversión de API a entidad Room
     */
    private fun Meal.toEntity(): MealDB = MealDB(
        id = idMeal.toIntOrNull() ?: (10000..99999).random(),
        name = strMeal,
        imageUrl = strMealThumb ?: "",
        ingredients = buildIngredientList(this)
    )

    /**
     * Extrae la lista de ingredientes del modelo API
     */
    private fun buildIngredientList(meal: Meal): List<String> {
        val ingredients = mutableListOf<String>()
        for (i in 1..20) {
            runCatching {
                val ingredientField = Meal::class.java.getDeclaredField("strIngredient$i").apply { isAccessible = true }
                val measureField = Meal::class.java.getDeclaredField("strMeasure$i").apply { isAccessible = true }

                val ing = (ingredientField.get(meal) as? String)?.trim().orEmpty()
                val meas = (measureField.get(meal) as? String)?.trim().orEmpty()

                if (ing.isNotEmpty() && ing.lowercase() != "null") {
                    ingredients.add(if (meas.isNotEmpty()) "$meas $ing" else ing)
                }
            }.onFailure {
                it.printStackTrace()
            }
        }
        return ingredients
    }



    /**
     * Factory para crear el ViewModel con MealDao inyectado
     */
    class Factory(private val mealDao: MealDao) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ListViewModel(mealDao) as T
        }
    }
}
