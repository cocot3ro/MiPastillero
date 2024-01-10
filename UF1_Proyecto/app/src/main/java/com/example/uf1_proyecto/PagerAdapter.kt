package com.example.uf1_proyecto

interface PagerAdapter {
    fun reload()
    fun search(date: Long): Int
}