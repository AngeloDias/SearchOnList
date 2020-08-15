package br.com.training.android.searchonlist

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import br.com.training.android.searchonlist.viewmodel.CitiesViewModel
import br.com.training.android.searchonlist.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var cityViewModel: CitiesViewModel
    private lateinit var citiesNames: ArrayList<String>
    private lateinit var listViewForNames: ListView
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        citiesNames = ArrayList()
        listViewForNames = listToSearch
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, citiesNames)

        listViewForNames.adapter = adapter

        setupViewModel()
        handleIntent(intent)

        // Create the observer which updates the UI.
        val citiesNamesObserver = Observer<List<String>> { cities ->
            // Update the UI, in this case, a TextView.
            Log.d("SearchOnList", "On observer, city name: ${cities[0]}")
            citiesNames.clear()
            citiesNames.addAll(cities)
            adapter.notifyDataSetChanged()
        }

        cityViewModel.sortedCities.observe(this, citiesNamesObserver)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                // verify partial-permutation
                // verify typo
                // do query if is one of that at most
                val allowed = cityViewModel.isQueryAllowed(query)

//                if(allowed) {
//                    val names = cityViewModel.sortedCities.value ?: ArrayList()
//
//                    if(names.isNotEmpty()) {
//                        citiesNames.add(names[0])
//                        adapter.notifyDataSetChanged()
//                    }
//
//                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater

        inflater.inflate(R.menu.search_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.search_dialog_button -> {
                citiesNames.clear()
                adapter.notifyDataSetChanged()
                onSearchRequested()

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupViewModel() {
        cityViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory()
        ).get(CitiesViewModel::class.java)
    }

}
