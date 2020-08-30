package com.example.retrohub.view.mobile

import androidx.annotation.StringRes
import com.example.retrohub.R
import com.example.retrohub.repository.local.entities.RetroEntity


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
        listOf("Seguir haciendo", "Hacer más", "Parar de hacer", "Hacer menos", "Empezar a hacer"),
        R.string.starfish
    ),
    SAILBOAT("SAILBOAT", listOf("Isla", "Viento", "Rocas", "Ancla"), R.string.sailboat),
    FAST_AND_FURIOUS(
        "FAST_AND_FURIOUS",
        listOf("Fast", "Fun zone", "Furious", "Boxes"),
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

fun createRetroFromEntity(entity: RetroEntity) = Retro(
    entity.username,
    entity.title?:"",
    entity.type?:RetroType.SPRINT.title,
    entity.subtype?:RetroSubTypes.STARFISH.title,
    entity.date?:"",
    entity.data
)