package br.com.training.android.searchonlist.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.training.android.searchonlist.utils.CheckPartialPermutation
import br.com.training.android.searchonlist.utils.CheckTypo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class CitiesViewModel: ViewModel() {
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    private val _sortedCities = MutableLiveData<List<String>>()
    val sortedCities: LiveData<List<String>> = _sortedCities

    fun isQueryAllowed(query: String) {
        var isPartialPermutation = false
        var isTypo = false

        viewModelScope.launch {
            val capitalsNames: List<String> = arrayListOf("Rio Branco",
                "Maceió",
                "Macapá",
                "Manaus",
                "Salvador",
                "Fortaleza",
                "Brasília",
                "Vitória",
                "Goiânia",
                "São Luís",
                "Cuiabá",
                "Campo Grande",
                "Belo Horizonte",
                "Belém",
                "João Pessoa",
                "Curitiba",
                "Recife",
                "Teresina",
                "Rio de Janeiro",
                "Natal",
                "Porto Alegre",
                "Porto Velho",
                "Boa Vista",
                "Florianópolis",
                "São Paulo",
                "Aracaju",
                "Palmas").sorted()
            var cityName = ""

            // suspend and resume to make the following main secure processing
            // so our ViewModel doesn't need to worry about threading
            for (elem in capitalsNames) {
                isPartialPermutation = CheckPartialPermutation.stringHaveAtMostOnePermutation(
                    elem.toLowerCase(Locale.ROOT),
                    query.toLowerCase(Locale.ROOT)
                )

                if(isPartialPermutation) {
                    Log.d("SearchOnList", "On isPartialPermutation, city name: $elem")
                    cityName = elem
                    break
                }
            }

            if(cityName.isEmpty()) {
                for (elem in capitalsNames) {
                    isTypo = CheckTypo.isZeroOrOneTypo(
                        query.toLowerCase(Locale.ROOT),
                        elem.toLowerCase(Locale.ROOT)
                    )

                    if(isTypo) {
                        Log.d("SearchOnList", "On isTypo, city name: $elem")
                        cityName = elem
                        break
                    }
                }
            } else {
                isTypo = CheckTypo.isZeroOrOneTypo(
                    query.toLowerCase(Locale.ROOT),
                    cityName.toLowerCase(Locale.ROOT)
                )
            }

            Log.d("SearchOnList", "isPartialPermutation: $isPartialPermutation")
            Log.d("SearchOnList", "isTypo: $isTypo")

            if((isPartialPermutation xor isTypo) || !isPartialPermutation && !isTypo) {
                val matchingName = ArrayList<String>()

                Log.d("SearchOnList", "On double check if, city name: $cityName")
                if(cityName.isNotEmpty()) {
                    matchingName.add(cityName)
                }

                _sortedCities.postValue(matchingName)
            }
        }

    }

    /**
     * Cancel all coroutines when the ViewModel is cleared
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}
