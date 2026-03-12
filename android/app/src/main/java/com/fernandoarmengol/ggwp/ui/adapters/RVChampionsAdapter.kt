package com.fernandoarmengol.ggwp.ui.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.fernandoarmengol.ggwp.databinding.ItemChampionBinding
import com.fernandoarmengol.ggwp.model.ChampionSummaryCD
import com.fernandoarmengol.ggwp.network.GlideHelper.loadImage
import com.fernandoarmengol.ggwp.ui.activities.ChampActivity


class RVChampionsAdapter : RecyclerView.Adapter<RVChampionsAdapter.ViewHolder>() {

    var champions: MutableList<ChampionSummaryCD> = ArrayList()
    lateinit var context: Context

    // Constructor de la clase. Se pasa la fuente de datos y el contexto sobre el que se mostrará.
    fun RecyclerAdapter(championsList: MutableList<ChampionSummaryCD>, contxt: Context) {
        this.champions = championsList
        this.context = contxt
    }

    // Este método se encarga de pasar los objetos, uno a uno al ViewHolder personalizado.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = champions.get(position)
        holder.bind(item, context)
    }

    // Es el encargado de devolver el ViewHolder ya configurado.
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            ItemChampionBinding.inflate(
                layoutInflater,
                parent,
                false
            ).root
        )
    }

    // Devuelve el tamaño del array.
    override fun getItemCount(): Int {
        return champions.size
    }

    // Esta clase se encarga de rellenar cada una de las vistas que se inflarán en el RecyclerView.
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Se usa View Binding para localizar los elementos en la vista.
        private val binding = ItemChampionBinding.bind(view)

        fun bind(champ: ChampionSummaryCD, context: Context) {
            binding.txtName.text = champ.name

            val url = "https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1/champion-icons/"
            loadImage(url + champ.id + ".png", binding.ivImg)

            binding.cvItem.setOnClickListener {
                val extras = Bundle()
                extras.putInt("EXTRA_CHAMP", champ.id)

                val intent = Intent(context, ChampActivity::class.java).apply {
                    putExtras(extras)
                }
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            }
        }
    }
}