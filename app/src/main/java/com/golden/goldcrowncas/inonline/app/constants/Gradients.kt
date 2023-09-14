package com.golden.goldcrowncas.inonline.app.constants

import android.graphics.LinearGradient
import android.graphics.Shader

object Gradients {
    fun getGold(width: Int, textSize: Float) = LinearGradient(width / 2f, 0f, width / 2f, textSize.toFloat(), intArrayOf((0xFFFFC609).toInt(), (0xFFFFA217).toInt()), null, Shader.TileMode.CLAMP)
}