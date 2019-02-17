package com.feelsokman.retroelectro.model

import com.google.gson.annotations.SerializedName

/**
 * The class representing the Json response. Use http://www.jsonschema2pojo.org/ to get this.
 * Or you can add this plugin for AS here https://plugins.jetbrains.com/plugin/9960-json-to-kotlin-class-jsontokotlinclass-
 * It will create your data class from JSON to kotlin.
 */
data class NetCat(
    @SerializedName("id") val id: String,
    @SerializedName("url") val url: String,
    @SerializedName("breeds") val breeds: List<Any>,
    @SerializedName("categories") val categories: List<Any>
) {
    override fun toString(): String {
        return "NetCat(id='$id', url='$url', breeds=$breeds, categories=$categories)"
    }
}