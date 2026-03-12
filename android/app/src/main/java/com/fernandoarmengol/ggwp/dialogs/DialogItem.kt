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
import com.fernandoarmengol.ggwp.GlobalVar.Companion.fixText
import com.fernandoarmengol.ggwp.R
import com.fernandoarmengol.ggwp.databinding.DialogItemBinding
import com.fernandoarmengol.ggwp.model.ItemsCD
import com.fernandoarmengol.ggwp.network.GlideHelper.loadImage
import dev.bandb.graphview.AbstractGraphAdapter
import dev.bandb.graphview.graph.Graph
import dev.bandb.graphview.graph.Node
import dev.bandb.graphview.layouts.tree.BuchheimWalkerConfiguration
import dev.bandb.graphview.layouts.tree.BuchheimWalkerLayoutManager
import dev.bandb.graphview.layouts.tree.TreeEdgeDecoration

object DialogItem {
    private lateinit var adapter: AbstractGraphAdapter<NodeViewHolder>
    lateinit var binding: DialogItemBinding
    private var itemsCD: MutableList<ItemsCD> = ArrayList()

    private const val url = "https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/assets/items/icons2d/"

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("SetTextI18n")
    fun dialogItem(context: Context, tempItemsCD: List<ItemsCD>, item: ItemsCD) {
        val builder = AlertDialog.Builder(context)
        itemsCD = tempItemsCD as MutableList<ItemsCD>

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DialogItemBinding.inflate(inflater)
        builder.setView(binding.root)

        //Configuracion del treeView
        val configuration = BuchheimWalkerConfiguration.Builder()
            .setSiblingSeparation(100)
            .setLevelSeparation(100)
            .setSubtreeSeparation(100)
            .setOrientation(BuchheimWalkerConfiguration.ORIENTATION_TOP_BOTTOM)
            .build()
        binding.rvFabric.layoutManager = BuchheimWalkerLayoutManager(context, configuration)

        binding.rvFabric.addItemDecoration(TreeEdgeDecoration())

        var indexNode = 0
        val graph = Graph()
        val node0 = Node("${indexNode++}_${item.id}")

        //Nombre y descripcion
        binding.txtName.text = "${item.name}\n${item.priceTotal}G"
        binding.txtDesc.text = fixText(item.description)

        //Justificar texto (solo compatible con android 8 o superior)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.txtDesc.justificationMode = JUSTIFICATION_MODE_INTER_WORD
        }

        //Carga de imagen principal
        loadImage(url + item.iconPath.split("/").last().lowercase(), binding.ivImg)

        //Algoritmo para calculo de nodos
        if (item.from.isNotEmpty()) {
            for (itemId in item.from) {
                val node = Node("${indexNode++}_${itemId}")
                graph.addEdge(node0, node)
                val itemCD = itemsCD.find { itemCD -> itemCD.id == itemId.toInt() }!!
                if (itemCD.from.isNotEmpty()){
                    for (itemMin in itemCD.from) {
                        graph.addEdge(node, Node("${indexNode++}_${itemMin}"))
                    }
                }
            }
        } else {
            binding.zoomLayout.visibility = View.GONE
        }
        setupGraphView(graph)

        builder.show()
    }

    private fun setupGraphView(graph: Graph) {
        adapter = object : AbstractGraphAdapter<NodeViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NodeViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_item, parent, false)
                return NodeViewHolder(view)
            }

            //Mostrar item en cada nodo junto a su imagen y nombre
            @SuppressLint("SetTextI18n")
            override fun onBindViewHolder(holder: NodeViewHolder, position: Int) {
                //Obtener el id del item
                val item: ItemsCD = itemsCD.find {
                        itemCD -> itemCD.id == getNodeData(position)
                    .toString()
                    .split("_")
                    .last()
                    .toInt()
                }!!

                //Mostrar datos en pantalla
                holder.txtName.text = "${item.name}\n${item.priceTotal}G"
                loadImage(url + item.iconPath.split("/").last().lowercase(), holder.ivImg)
            }
        }.apply {
            this.submitGraph(graph)
            binding.rvFabric.adapter = this
        }
    }

    class NodeViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtName: TextView = itemView.findViewById(R.id.txtName)
        var ivImg: ImageView = itemView.findViewById(R.id.ivImg)
    }
}