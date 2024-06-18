package com.a23pablooc.proxectofct.ui.view.diffutils

import androidx.recyclerview.widget.DiffUtil
import java.util.Date

class ActiveMedScheduleDiffUtil(
    private val oldList: List<Date>,
    private val newList: List<Date>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}