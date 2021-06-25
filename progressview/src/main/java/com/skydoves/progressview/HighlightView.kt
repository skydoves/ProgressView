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

  var radiusArray: FloatArray? = null
    set(value) {
      field = value
      updateHighlightView()
    }

  @Px var padding = dp2Px(0)
    set(value) {
      field = value
      updateHighlightView()
    }

  @ColorInt var color: Int = accentColor()
    set(value) {
      field = value
      updateHighlightView()
    }

  @ColorInt var colorGradientStart: Int = NO_COLOR
    set(value) {
      field = value
      updateHighlightView()
    }

    @ColorInt
    var colorGradientCenter: Int = NO_COLOR
        set(value) {
            field = value
            updateHighlightView()
        }

    @ColorInt
    var colorGradientEnd: Int = NO_COLOR
        set(value) {
            field = value
            updateHighlightView()
        }

  var highlight: Drawable? = null
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

  init {
    addView(bodyView)
    addView(strokeView)
    strokeView.setOnClickListener {
      highlighting = highlighting.not()
      onProgressClickListener?.onClickProgress(highlighting)
    }
  }

  fun updateHighlightView() {
    updateBodyView()
    updateStrokeView()
    updateHighlighting()
  }

  private fun updateBodyView() {
    bodyView.background = if (colorGradientStart != NO_COLOR && colorGradientEnd != NO_COLOR) {
      var gradientOrientation = GradientDrawable.Orientation.LEFT_RIGHT
      if (orientation == ProgressViewOrientation.VERTICAL) {
        gradientOrientation = GradientDrawable.Orientation.TOP_BOTTOM
      }
            if (colorGradientCenter == NO_COLOR) {
                GradientDrawable(
                    gradientOrientation,
                    intArrayOf(colorGradientStart, colorGradientEnd)
                ).apply {
                    applyRadius(this)
                }
            } else {
                GradientDrawable(
                    gradientOrientation,
                    intArrayOf(colorGradientStart, colorGradientCenter, colorGradientEnd)
                ).apply {
                    applyRadius(this)
                }
            }
    } else if (highlight == null) {
      GradientDrawable().apply {
        setColor(this@HighlightView.color)
        applyRadius(this)
      }
    } else {
      highlight
    }
    bodyView.applyMargin()
  }

  private fun updateStrokeView() {
    strokeView.background = GradientDrawable().apply {
      setColor(Color.TRANSPARENT)
      setStroke(highlightThickness, highlightColor)
      applyRadius(this)
    }
    strokeView.applyMargin()
  }

  private fun updateHighlighting() {
    if (highlighting) {
      strokeView.alpha = highlightAlpha
    } else {
      strokeView.alpha = 0f
    }
  }

  private fun View.applyMargin() {
    (layoutParams as MarginLayoutParams).setMargins(padding, padding, padding, padding)
  }

  private fun applyRadius(gradientDrawable: GradientDrawable) {
    if (radiusArray != null) {
      gradientDrawable.cornerRadii = radiusArray
    } else {
      gradientDrawable.cornerRadius = radius
    }
  }
}
