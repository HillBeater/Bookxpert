package com.hillbeater.bookxpert.model
import com.google.gson.annotations.SerializedName

data class PhoneDataAPIModel(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String? = null,
    @SerializedName("data") val data: Data? = null,
)

data class Data(
    @SerializedName("color") val color: String? = null,
    @SerializedName("Color") val colorAlt: String? = null,

    @SerializedName("capacity") val capacity: String? = null,
    @SerializedName("capacity GB") val capacityGB: Int? = null,
    @SerializedName("Capacity") val capacityAlt: String? = null,

    @SerializedName("price") val price: Double? = null,
    @SerializedName("Price") val priceAlt: String? = null,

    @SerializedName("generation") val generation: String? = null,
    @SerializedName("Generation") val generationAlt: String? = null,

    @SerializedName("year") val year: Int? = null,
    @SerializedName("CPU model") val cpuModel: String? = null,
    @SerializedName("Hard disk size") val hardDiskSize: String? = null,
    @SerializedName("Strap Colour") val strapColour: String? = null,
    @SerializedName("Case Size") val caseSize: String? = null,
    @SerializedName("Screen size") val screenSize: Double? = null,
    @SerializedName("Description") val description: String? = null
) {
    val actualColor: String?
        get() = color ?: colorAlt

    val actualCapacity: String?
        get() = capacity ?: capacityAlt ?: capacityGB?.toString()

    val actualPrice: String?
        get() = price?.toString() ?: priceAlt

    val actualGeneration: String?
        get() = generation ?: generationAlt
}


