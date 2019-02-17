package com.feelsokman.retroelectro

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.feelsokman.retroelectro.cats.CatsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    // Read the docs with detailed instructions to get your API key and endpoint!
    // https://docs.thecatapi.com/

    // the server url endpoint
    private val serverUrl = "https://api.thecatapi.com/v1/"
    // this is where you declare your api key
    private val apiKey = "yourApiKeyHere"

    private val compositeDisposableOnPause = CompositeDisposable()
    private var latestCatCall: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // click listener so you can perform the API call manually
        button.setOnClickListener {
            getSomeCats()
        }
    }

    // the API call
    private fun getSomeCats() {
        // initialising the repository class with the necessary information
        val catsRepository = CatsRepository(serverUrl, BuildConfig.DEBUG, apiKey)

        // stopping the last call if it's already running (optional)
        latestCatCall?.dispose()

        // perform the API call
        // asking for 10 cats. Don't care in what category so just passing null
        latestCatCall =
            catsRepository.getNumberOfRandomCats(10, null).subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    compositeDisposableOnPause.add(it)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result ->
                    when {
                        result.hasError() -> result.errorMessage?.let {
                            Toast.makeText(this@MainActivity, "Error getting cats$it", Toast.LENGTH_SHORT).show()
                        }
                            ?: run {
                                Toast.makeText(this@MainActivity, "Null error", Toast.LENGTH_SHORT).show()
                            }
                        result.hasCats() -> result.netCats?.let {
                            Toast.makeText(this@MainActivity, "Cats received!", Toast.LENGTH_SHORT).show()
                        }
                            ?: run {
                                Toast.makeText(this@MainActivity, "Null list of cats", Toast.LENGTH_SHORT).show()
                            }
                        else -> Toast.makeText(this@MainActivity, "No cats available :(", Toast.LENGTH_SHORT).show()
                    }
                }
    }

    // Killing all background threads (if any exist) cause they don't deserve to live when the activity is not running
    private fun clearAllJobsOnPause() {
        compositeDisposableOnPause.clear()
    }

    // onPause! Stop everything, the user is probably checking memes elsewhere
    override fun onPause() {
        clearAllJobsOnPause()
        super.onPause()
    }
}
