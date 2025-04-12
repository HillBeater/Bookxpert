package com.hillbeater.bookxpert.utils

import com.hillbeater.bookxpert.database.PhoneDataItem
import com.hillbeater.bookxpert.model.PhoneDataAPIModel

fun PhoneDataAPIModel.toEntity(): PhoneDataItem {
    return PhoneDataItem(
        id = id.toString(),
        name = name ?: "Unknown",
        color = data?.color ?: data?.colorAlt,
        capacity = data?.capacity ?: data?.capacityAlt ?: data?.capacityGB?.toString(),
        price = data?.price?.toString() ?: data?.priceAlt,
        generation = data?.generation ?: data?.generationAlt,
        year = data?.year,
        cpuModel = data?.cpuModel,
        hardDiskSize = data?.hardDiskSize,
        strapColour = data?.strapColour,
        caseSize = data?.caseSize,
        screenSize = data?.screenSize,
        description = data?.description
    )
}