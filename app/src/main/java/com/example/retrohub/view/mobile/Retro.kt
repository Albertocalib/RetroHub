package com.example.retrohub.view.mobile

import androidx.annotation.StringRes
import com.example.retrohub.R


data class Retro(
    val username: String,
    val title: String,
    val type: String,
    val subtype: String,
    val date: String,
    val data: Map<String, List<String>>
)

enum class RetroSubTypes(val title: String, val fields: List<String>, @StringRes val res: Int) {

    STARFISH(
        "STARFISH",
        listOf("Seguir haciendo", "Hacer más", "Empezar a hacer", "Parar de hacer", "Hacer menos"),
        R.string.starship
    ),
    SAILBOAT("SAILBOAT", listOf("Ancla", "Rocas", "Viento", "Isla"), R.string.sailboat),
    FAST_AND_FURIOUS(
        "FAST_AND_FURIOUS",
        listOf("Fast", "Furious", "FunZone", "Boxes"),
        R.string.fast_and_furious
    ),
    THREE_LITTLE_PIGS(
        "THREE_LITTLE_PIGS",
        listOf("Paja", "Madera", "Ladrillo"),
        R.string.three_little_pigs
    ),
    SEMAPHORE("SEMAPHORE", listOf("Rojo", "Ámbar", "Verde"), R.string.semaphore),
    FOUR_L("4L", listOf("Liked", "Learned", "Lacked", "Longed for"), R.string.fourl)
}

enum class RetroType(val title: String) {
    RELEASE("RELEASE"),
    SPRINT("SPRINT")
}


private fun create(
    username: String,
    title: String,
    type: RetroType,
    date: String,
    data: List<List<String>>,
    subtype: RetroSubTypes
) =
    Retro(
        username,
        title,
        type.title,
        subtype.title,
        date,
        data.mapIndexed { index, it -> subtype.fields[index] to it }.toMap()
    )
