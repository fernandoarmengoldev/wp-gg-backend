package com.fernandoarmengol.ggwp.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import com.fernandoarmengol.ggwp.GlobalVar.Companion.itemsCD
import com.fernandoarmengol.ggwp.databinding.ActivityItemsBinding
import com.fernandoarmengol.ggwp.model.ItemsCD
import com.fernandoarmengol.ggwp.ui.adapters.RVItemsAdapter

class ItemsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityItemsBinding
    private val adapter: RVItemsAdapter = RVItemsAdapter()

    private val itemsOnlyStore: MutableList<ItemsCD> = ArrayList()
    private var searchedItems: MutableList<ItemsCD> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.entryName.addTextChangedListener {
            filtrar()
        }

        for (item in itemsCD) {
            if (item.inStore){
                itemsOnlyStore.add(item)
            }
        }

        itemsOnlyStore.sortBy { it.name }
        searchedItems = itemsOnlyStore.toMutableList()

        setUpRecyclerView()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun filtrar() {
        searchedItems.clear()
        for (item in itemsOnlyStore) {
            if (item.name.contains(binding.entryName.text, ignoreCase = true)) {
                searchedItems.add(item)
            }
        }
        adapter.notifyDataSetChanged()
    }

    // Método encargado de configurar el RV.
    private fun setUpRecyclerView() {
        binding.rvItems.setHasFixedSize(true)
        binding.rvItems.layoutManager = GridLayoutManager(this.applicationContext, 5)
        adapter.RecyclerAdapter(searchedItems, itemsOnlyStore, this)
        binding.rvItems.adapter = adapter
    }
}