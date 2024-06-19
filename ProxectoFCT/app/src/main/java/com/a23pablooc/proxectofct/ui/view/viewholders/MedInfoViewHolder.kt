package com.a23pablooc.proxectofct.ui.view.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.a23pablooc.proxectofct.databinding.PrincipioActivoRvItemBinding

class MedInfoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = PrincipioActivoRvItemBinding.bind(view)

    fun render(s: String) {
        binding.tv.text = s
    }
}