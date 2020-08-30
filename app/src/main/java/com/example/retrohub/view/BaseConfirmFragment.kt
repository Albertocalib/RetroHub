package com.example.retrohub.view

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrohub.MainActivity
import com.example.retrohub.R
import com.example.retrohub.extensions.inflate
import com.example.retrohub.model_view.PersistedRetroViewModel
import com.example.retrohub.view.mobile.Retro
import kotlinx.android.synthetic.main.retro_confirm_view.*
import kotlinx.android.synthetic.main.view_card_grid.view.*
import kotlinx.android.synthetic.main.view_grid_item.view.*
import org.koin.android.ext.android.inject

abstract class BaseConfirmFragment: MainActivity.RetroHubFragment(R.layout.retro_confirm_view) {

    protected lateinit var items: List<Card>

    protected val viewModel: PersistedRetroViewModel by inject()

    abstract fun getColors(): List<Int>
    abstract fun getDrawables(): List<Int>
    abstract fun getPositions(): List<Boolean>

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.retro.observe(viewLifecycleOwner) {
            setItems(it)
            with(retro_grid) {
                layoutManager = GridLayoutManager(requireContext(), 2)
                adapter = GridItemAdapter(items)
            }
        }
        viewModel.getRetro()
        next_step.setOnClickListener {
            findNavController().navigate(R.id.savedRetroFragment)
        }
    }

    private fun setItems(retro: Retro) {
        val colors = getColors()
        val rightImages = getPositions()
        val images = getDrawables()

        items = retro.data.keys.mapIndexed { index, s ->
            Card(images[index], colors[index], (retro.data[s] ?: emptyList()).sortedByDescending { it.length },s,rightImages[index])
        }
    }

    class GridItemAdapter(val cards: List<Card>) : RecyclerView.Adapter<GridItemAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(parent.inflate(R.layout.view_grid_item))

        override fun onBindViewHolder(holder: ViewHolder, position: Int) =
            holder.bind(cards[position])

        override fun getItemCount() = cards.size

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            fun bind(card: Card) = with(itemView) {
                title_grid_item.text = card.title
                right_icon.isInvisible = !card.right
                left_icon.isInvisible = card.right
                with (if(card.right) right_icon else left_icon) {
                    setImageDrawable(resources.getDrawable(card.res,null))
                }
                empty_message.isVisible = card.info.isEmpty()
                with (list_cards) {
                    layoutManager = GridLayoutManager(context,2)
                    adapter = CardAdapter(card.info, card.color)
                }
            }
        }
    }

    class CardAdapter(val sentences: List<String>, @ColorRes val colorRes: Int) : RecyclerView.Adapter<CardAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(parent.inflate(R.layout.view_card_grid))

        override fun onBindViewHolder(holder: ViewHolder, position: Int) =
            holder.bind(sentences[position], colorRes)

        override fun getItemCount() = sentences.size

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            fun bind(sentence: String, color: Int) = with(itemView) {
                text.text = sentence
                card_container.backgroundTintList = context.resources.getColorStateList(color)
            }
        }
    }

    data class Card(@DrawableRes val res: Int, @ColorRes val color: Int, val info: List<String>, val title: String, val right: Boolean)
}

class FourLsConfirmFragment: BaseConfirmFragment() {

    override fun getToolbarTitle() = getString(R.string.fourl)

    override fun getColors() = listOf(
        R.color.lime_green ,R.color.light_yellow, R.color.light_red,R.color.colorPrimaryExtraLight
    )

    override fun getPositions() = listOf(true, false, true, false)

    override fun getDrawables() = listOf(
        R.drawable.ic_like, R.drawable.ic_learned,
        R.drawable.ic_improve, R.drawable.ic_longed_for
    )
}

class SailboatConfirmFragment: BaseConfirmFragment() {

    override fun getToolbarTitle() = getString(R.string.sailboat)

    override fun getColors() = listOf(
        R.color.colorPrimaryExtraLight ,R.color.lime_green, R.color.light_yellow,R.color.light_red
    )

    override fun getPositions() = listOf(true, false, true, false)

    override fun getDrawables() = listOf(
        R.drawable.ic_island, R.drawable.ic_wind,
        R.drawable.ic_rocks, R.drawable.ic_anchor
    )
}

class StarfishConfirmFragment: BaseConfirmFragment() {

    override fun getToolbarTitle() = getString(R.string.starfish)

    override fun getColors() = listOf(
        R.color.lime_green ,R.color.green_light, R.color.light_red,R.color.light_yellow,R.color.light_purple
    )

    override fun getPositions() = listOf(true, false, true, false, true)

    override fun getDrawables() = listOf(
        R.drawable.ic_continue, R.drawable.ic_more,
        R.drawable.ic_dislike, R.drawable.ic_less,
        R.drawable.ic_start
    )
}

class SemaphoreConfirmFragment: BaseConfirmFragment() {

    override fun getColors() = listOf(R.color.light_red,R.color.light_yellow,R.color.lime_green)

    override fun getDrawables() = listOf(R.drawable.ic_semaphore_red,R.drawable.ic_semaphore_yellow, R.drawable.ic_semaphore_green)

    override fun getPositions() = List(3) { true }

    override fun getToolbarTitle() = getString(R.string.semaphore)

}

class ThreeLittlePigsConfirmFragment : BaseConfirmFragment() {

    override fun getColors() = listOf(R.color.light_red,R.color.light_yellow,R.color.lime_green)

    override fun getDrawables() = listOf(R.drawable.ic_straw,R.drawable.ic_wood, R.drawable.ic_bricks)

    override fun getPositions() = List(3) { true }

    override fun getToolbarTitle() = getString(R.string.semaphore)

}

class FastAndFuriousConfirmFragment : BaseConfirmFragment() {

    override fun getColors() = listOf(R.color.lime_green,R.color.colorPrimaryExtraLight,R.color.light_red,R.color.light_yellow)

    override fun getDrawables() = listOf(R.drawable.ic_fast,R.drawable.ic_fun_zone, R.drawable.ic_furious, R.drawable.ic_boxes)

    override fun getPositions() = listOf(true,false,true,false)

    override fun getToolbarTitle() = getString(R.string.fast_and_furious)

}