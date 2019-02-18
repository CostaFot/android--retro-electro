package com.feelsokman.retroelectro.cats

import com.feelsokman.retroelectro.model.NetCat
import com.feelsokman.retroelectro.repository.Repository
import io.reactivex.Single

/**
 * This guy extends Repository class so the retrofit variable will be available to use as it's instantiated in the init!
 */
class CatsRepository(
    baseUrl: String,
    isDebugEnabled: Boolean,
    apiKey: String
) : Repository(baseUrl, isDebugEnabled, apiKey) {

    private val catsDataSource: CatsDataSource = CatsDataSource(retrofit)

    // a class to wrap around the response to make things easier later
    inner class Result(val netCats: List<NetCat>? = null, val errorMessage: String? = null) {

        fun hasCats(): Boolean {
            return netCats != null && !netCats.isEmpty()
        }

        fun hasError(): Boolean {
            return errorMessage != null
        }
    }

    // the method that's gonna be called by our activity
    fun getNumberOfRandomCats(limit: Int, category_ids: Int?): Single<Result> {

        return catsDataSource.getNumberOfRandomCats(limit, category_ids)
            .map { netCats: List<NetCat> -> Result(netCats = netCats) }
            .onErrorReturn { t: Throwable -> Result(errorMessage = t.message) }
    }
}