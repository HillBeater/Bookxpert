package com.hillbeater.bookxpert.database

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("CPU model") val cpuModel: String? = null,
    @SerializedName("Capacity") val capacityCaps: String? = null,
    @SerializedName("Case Size") val caseSize: String? = null,
    @SerializedName("Color") val colorCaps: String? = null,
    @SerializedName("Description") val description: String? = null,
    @SerializedName("Generation") val generationCaps: String? = null,
    @SerializedName("Hard disk size") val hardDiskSize: String? = null,
    @SerializedName("Price") val priceCaps: String? = null,
    @SerializedName("Screen size") val screenSize: Double? = null,
    @SerializedName("Strap Colour") val strapColour: String? = null,
    @SerializedName("capacity") val capacity: String? = null,
    @SerializedName("capacity GB") val capacityGb: Int? = null,
    @SerializedName("color") val color: String? = null,
    @SerializedName("generation") val generation: String? = null,
    @SerializedName("price") val price: Double? = null,
    @SerializedName("year") val year: Int? = null
)

