package com.fernandoarmengol.ggwp.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.fernandoarmengol.ggwp.databinding.ItemSummonerBinding
import com.fernandoarmengol.ggwp.ui.activities.SummonerActivity

class RVSumonerAdapter : RecyclerView.Adapter<RVSumonerAdapter.ViewHolder>() {

    var names: MutableList<String> = ArrayList()

    lateinit var context: Context

    // Constructor de la clase. Se pasa la fuente de datos y el contexto sobre el que se mostrará.
    fun RecyclerAdapter(names: MutableList<String>, contxt: Context) {
        this.names = names
        this.context = contxt
    }

    // Este método se encarga de pasar los objetos, uno a uno al ViewHolder personalizado.
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name = names.get(position)
        holder.bind(name, context)
    }

    // Es el encargado de devolver el ViewHolder ya configurado.
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            ItemSummonerBinding.inflate(
                layoutInflater,
                parent,
                false
            ).root
        )
    }

    // Devuelve el tamaño del array.
    override fun getItemCount(): Int {
        return names.size
    }

    // Esta clase se encarga de rellenar cada una de las vistas que se inflarán en el RecyclerView.
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Se usa View Binding para localizar los elementos en la vista.
        private val binding = ItemSummonerBinding.bind(view)

        @SuppressLint("MutatingSharedPrefs", "NotifyDataSetChanged", "CommitPrefEdits", "WrongConstant")
        @RequiresApi(Build.VERSION_CODES.Q)
        fun bind(item: String, context: Context) {
            binding.txtName.text = item
            binding.btnSearch.setOnClickListener {
                val extras = Bundle()
                extras.putString("EXTRA_SUMMONER", item)

                val intent = Intent(context, SummonerActivity::class.java).apply {
                    putExtras(extras)
                }
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            }
            binding.btnDelete.setOnClickListener {
                val prefs = context.getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE)
                val namesPrefs = prefs?.getStringSet("names", null)!!

                namesPrefs.remove(item)
                names.remove(item)
                notifyDataSetChanged()

                prefs.edit().clear().apply()
                prefs.edit()?.putStringSet("names", namesPrefs)?.apply()
            }
        }
    }
}