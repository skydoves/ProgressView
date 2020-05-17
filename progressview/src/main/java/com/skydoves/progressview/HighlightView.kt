/*
 * Copyright (C) 2019 skydoves
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skydoves.progressview

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.Px

/** HighlightView is a view with stroke highlighting via onClickListener. */
class HighlightView(
  context: Context,
  attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

  private val bodyView = LinearLayout(context)
  private val strokeView = View(context)

  var highlighting: Boolean = false
    set(value) {
      field = value
      updateHighlighting()
    }
  @Px var highlightThickness: Int = dp2Px(0)
    set(value) {
      field = value
      updateHighlightView()
    }
  @ColorInt var highlightColor: Int = accentColor()
    set(value) {
      field = value
      updateHighlightView()
    }
  @FloatRange(from = 0.0, to = 1.0) var highlightAlpha: Float = 1.0f
    set(value) {
      field = value
      updateHighlightView()
    }
  var radius: Float = dp2Px(5).toFloat()
    set(value) {
      field = value
      updateHighlightView()
    }
  @Px var padding = dp2Px(0).toFloat()
    set(value) {
      field = value
      updateHighlightView()
    }
  @ColorInt var color: Int = accentColor()
    set(value) {
      field = value
      updateHighlightView()
    }
  @ColorInt var colorGradientStart: Int = 65555
    set(value) {
      field = value
      updateHighlightView()
    }
  @ColorInt var colorGradientEnd: Int = 65555
    set(value) {
      field = value
      updateHighlightView()
    }
  var drawable: Drawable? = null
    set(value) {
      field = value
      updateHighlightView()
    }
  var orientation = ProgressViewOrientation.HORIZONTAL
    set(value) {
      field = value
      updateHighlightView()
    }
  var onProgressClickListener: OnProgressClickListener? = null

  override fun onFinishInflate() {
    super.onFinishInflate()
    updateHighlightView()
  }

  fun updateHighlightView() {
    updateBodyView()
    updateStrokeView()
    updateHighlighting()
    updateOnClickListener()
  }

  private fun updateBodyView() {
    if (colorGradientStart != 65555 && colorGradientEnd != 65555) {
      var gradientOrientation = GradientDrawable.Orientation.LEFT_RIGHT
      if (orientation == ProgressViewOrientation.VERTICAL) {
        gradientOrientation = GradientDrawable.Orientation.TOP_BOTTOM
      }
      val gradient =
        GradientDrawable(gradientOrientation, intArrayOf(colorGradientStart, colorGradientEnd))
      gradient.cornerRadius = radius
      this.bodyView.background = gradient
    } else if (this.drawable == null) {
      this.bodyView.background = GradientDrawable().apply {
        cornerRadius = radius
        setColor(this@HighlightView.color)
      }
    } else {
      this.bodyView.background = this.drawable
    }
    val params = LinearLayout.LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT,
      ViewGroup.LayoutParams.MATCH_PARENT
    ).apply {
      setMargins(padding.toInt(), padding.toInt(), padding.toInt(), padding.toInt())
    }
    this.bodyView.layoutParams = params
    removeView(bodyView)
    addView(bodyView)
  }

  private fun updateStrokeView() {
    this.strokeView.background = GradientDrawable().apply {
      setColor(Color.TRANSPARENT)
      cornerRadius = radius
      setStroke(highlightThickness, highlightColor)
    }
    this.strokeView.layoutParams =
      ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
      )
    removeView(strokeView)
    addView(strokeView)
  }

  private fun updateHighlighting() {
    if (this.highlighting) {
      this.strokeView.alpha = this.highlightAlpha
    } else {
      this.strokeView.alpha = 0f
    }
  }

  private fun updateOnClickListener() {
    this.strokeView.setOnClickListener {
      this.highlighting = !highlighting
      this.onProgressClickListener?.onClickProgress(highlighting)
    }
  }
}
