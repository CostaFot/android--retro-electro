package com.feelsokman.retroelectro

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.feelsokman.retroelectro.model.NetCat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    // Read the docs with detailed instructions to get your API key and endpoint!
    // https://docs.thecatapi.com/

    // public static fields in a companion object because im a horrible person
    companion object {
        // the server url endpoint
        const val serverUrl = "https://api.thecatapi.com/v1/"
        // this is where you declare your api key
        const val apiKey = "yourApiKeyHere"
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // setting up viewModel
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        // observing the stuff we are interested about.
        // any change observed will run the corresponding method
        viewModel.bunchOfCats.observe(this, Observer { onResult(it) })
        viewModel.errorObservedByActivityInCaseThingsGoWrong.observe(this, Observer { onError(it) })

        // click listener so you can perform the API call manually
        button.setOnClickListener {
            viewModel.getSomeCats()
        }
    }

    private fun onResult(bunchOfCats: List<NetCat>) {

        // Not doing anything yet with this list except a toast
        Toast.makeText(this@MainActivity, "Got ${bunchOfCats.size} cats", Toast.LENGTH_SHORT).show()
    }

    private fun onError(error: String) {
        // a simple toast in case things went wrong
        error.let {
            if (!it.isBlank()) {
                Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show()
            }

        }
    }

}
