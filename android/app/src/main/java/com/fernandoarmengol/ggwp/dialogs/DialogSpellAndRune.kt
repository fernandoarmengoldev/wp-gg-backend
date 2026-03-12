package com.fernandoarmengol.ggwp.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.fernandoarmengol.ggwp.GlobalVar.Companion.fixText
import com.fernandoarmengol.ggwp.R
import com.fernandoarmengol.ggwp.databinding.DialogItemBinding
import com.fernandoarmengol.ggwp.model.ItemsCD
import com.fernandoarmengol.ggwp.model.SpellsCD
import com.fernandoarmengol.ggwp.network.GlideHelper.loadImage
import dev.bandb.graphview.AbstractGraphAdapter
import dev.bandb.graphview.graph.Graph
import dev.bandb.graphview.graph.Node
import dev.bandb.graphview.layouts.tree.BuchheimWalkerConfiguration
import dev.bandb.graphview.layouts.tree.BuchheimWalkerLayoutManager
import dev.bandb.graphview.layouts.tree.TreeEdgeDecoration
import retrofit2.http.Url

object DialogSpellAndRune {
    lateinit var binding: DialogItemBinding

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("SetTextI18n")
    fun dialogSpellAndRune(context: Context, name: String, description: String, url: String) {
        val builder = AlertDialog.Builder(context)

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DialogItemBinding.inflate(inflater)
        builder.setView(binding.root)

        //Nombre y descripcion
        binding.txtName.text = name
        binding.txtDesc.text = fixText(description)

        //Justificar texto (solo compatible con android 8 o superior)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.txtDesc.justificationMode = JUSTIFICATION_MODE_INTER_WORD
        }

        //Carga de imagen principal
        loadImage(url, binding.ivImg)

        binding.zoomLayout.visibility = View.GONE

        builder.show()
    }
}