package com.example.uf1_proyecto.view

interface PagerAdapter {
    fun reload()
    fun search(date: Long): Int
}