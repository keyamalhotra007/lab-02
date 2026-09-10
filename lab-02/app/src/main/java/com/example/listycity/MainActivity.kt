package com.example.listycity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listycity.ui.theme.ListyCityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) { //runs when Android screen is created (? -> means it can be null)
        super.onCreate(savedInstanceState) // super = parent class
        enableEdgeToEdge()
        val cityRepository = CityRepository() //instance of CityRepository class
        setContent { // setContant { .. } is the same as setContent(() -> { ... })
            ListyCityTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CityListScreen(
                        cities = cityRepository.cities,
                        onAddCity = { cityRepository.addCity(it) }, // same as onAddCity = { cityName -> cityRepository.addCity(cityName) }
                        onDeleteCity = { cityRepository.deleteCity(it) },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun CityListScreen(
    cities: List<String>,
    onAddCity: (String) -> Unit,
    onDeleteCity: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var newCityName by remember { mutableStateOf("") }
    // remember: runs code in {} once and keps whatever it returns alive across reruns of this func

    Column(modifier = modifier.fillMaxSize()) {

        Row(Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = newCityName,
                onValueChange = { newCityName = it },
                label = { Text("City name") },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button( // Button(onClick = { ... }) {Text("Button Text")}
                onClick = {
                    if (newCityName.isNotBlank()) {
                        onAddCity(newCityName)
                        newCityName = "" // reset the box to empty
                    }
                }
            ) {
                Text("Add City")
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(cities) { city ->
                CityRow(city = city, onDeleteCity = onDeleteCity)
            }
        }
    }
}

@Composable
fun CityRow(city: String, onDeleteCity: (String) -> Unit) {
    Row (
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = city,
            fontSize = 28.sp,
            modifier = Modifier
                .padding(horizontal = 18.dp, vertical = 14.dp)
        )

        // 1. This Spacer acts like a spring, pushing the button to the far right side
        Spacer(modifier = Modifier.weight(1f))

        // Button(onClick = { ... }) {Text("Button Text")}
        Button (
            onClick = {
                if (city.isNotBlank()){
                    onDeleteCity(city)
                }
            }
        )
        {Text("Delete")}
    }

}
class CityRepository {
    private val _cities = mutableStateListOf( // mutableStateListOf is designed for Compose to know what changed
        "Edmonton", "Vancouver", "Moscow",
        "Sydney", "Berlin", "Vienna",
        "Tokyo", "Beijing", "Osaka",
        "New Delhi"
    )

    // read-only, visible publicly
    val cities: List<String>
        get() = _cities

    fun addCity(city: String){
        _cities.add(city)
    }

    fun deleteCity(city: String){
        _cities.remove(city)
    }
}

