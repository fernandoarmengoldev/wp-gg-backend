package com.fernandoarmengol.ggwp.ui.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.fernandoarmengol.ggwp.databinding.ItemItemBinding
import com.fernandoarmengol.ggwp.dialogs.DialogSpellAndRune
import com.fernandoarmengol.ggwp.model.SpellsCD

class RVSpellsAdapter : RecyclerView.Adapter<RVSpellsAdapter.ViewHolder>() {

    var spells: MutableList<SpellsCD> = ArrayList()

    lateinit var context: Context

    // Constructor de la clase. Se pasa la fuente de datos y el contexto sobre el que se mostrará.
    fun RecyclerAdapter(spells: MutableList<SpellsCD>, contxt: Context) {
        this.spells = spells
        this.context = contxt
    }

    // Este método se encarga de pasar los objetos, uno a uno al ViewHolder personalizado.
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val spell = spells.get(position)
        holder.bind(spell, context)
    }

    // Es el encargado de devolver el ViewHolder ya configurado.
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            ItemItemBinding.inflate(
                layoutInflater,
                parent,
                false
            ).root
        )
    }

    // Devuelve el tamaño del array.
    override fun getItemCount(): Int {
        return spells.size
    }

    // Esta clase se encarga de rellenar cada una de las vistas que se inflarán en el RecyclerView.
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Se usa View Binding para localizar los elementos en la vista.
        private val binding = ItemItemBinding.bind(view)

        @RequiresApi(Build.VERSION_CODES.Q)
        fun bind(spell: SpellsCD, context: Context) {
            binding.txtName.text = spell.name

            val shimmer = Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
                .setDuration(1800) // how long the shimmering animation takes to do one full sweep
                .setBaseAlpha(0.7f) //the alpha of the underlying children
                .setHighlightAlpha(0.6f) // the shimmer alpha amount
                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                .setAutoStart(true)
                .build()

            // This is the placeholder for the imageView
            val shimmerDrawable = ShimmerDrawable().apply {
                setShimmer(shimmer)
            }

            val url = "https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/data/spells/icons2d/"
            val urlRune = url + spell.iconPath.split("/").last().lowercase()
            Glide.with(binding.ivImg.context)
                .load(urlRune)
                .placeholder(shimmerDrawable)
                .into(binding.ivImg)

            binding.ivImg.setOnClickListener {
                DialogSpellAndRune.dialogSpellAndRune(context, spell.name, spell.description, urlRune)
            }
        }
    }
}