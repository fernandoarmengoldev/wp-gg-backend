package com.fernandoarmengol.ggwp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fernandoarmengol.ggwp.R
import com.fernandoarmengol.ggwp.databinding.FragmentWikiBinding
import com.fernandoarmengol.ggwp.ui.activities.*

class WikiFragment : Fragment() {

    private lateinit var binding: FragmentWikiBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWikiBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.imgItem.setOnClickListener {
            val intent = Intent(context, ItemsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context?.startActivity(intent)
        }
        binding.imgSpell.setOnClickListener {
            val intent = Intent(context, SpellsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context?.startActivity(intent)
        }
        binding.imgRunes.setOnClickListener {
            val intent = Intent(context, RunesActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context?.startActivity(intent)
        }

        binding.imgChamp.setOnClickListener {
            openUrl(getString(R.string.url_champs_wiki))
        }
        binding.imgMap.setOnClickListener {
            openUrl(getString(R.string.url_map_wiki))
        }
        binding.imgRegions.setOnClickListener {
            openUrl(getString(R.string.url_region_wiki))
        }

        return view
    }

    private fun openUrl(url: String){
        val extras = Bundle()
        extras.putString("EXTRA_URL", url)

        val intent = Intent(context, WebActivity::class.java).apply {
            putExtras(extras)
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context?.startActivity(intent)
    }
}