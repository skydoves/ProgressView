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
public class HighlightView(
  context: Context,
  attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

  private val bodyView = LinearLayout(context)
  private val strokeView = View(context)

  public var highlighting: Boolean by highlightViewProperty(false)

  @get:Px
  public var highlightThickness: Int by highlightViewProperty(dp2Px(0))

  @get:ColorInt
  public var highlightColor: Int by highlightViewProperty(accentColor())

  @get:FloatRange(from = 0.0, to = 1.0)
  public var highlightAlpha: Float by highlightViewProperty(1.0f)

  public var radius: Float by highlightViewProperty(dp2Px(5).toFloat())

  public var radiusArray: FloatArray? by highlightViewProperty(null)

  @get:Px
  public var padding: Int by highlightViewProperty(dp2Px(0))

  @get:ColorInt
  public var color: Int by highlightViewProperty(accentColor())

  @get:ColorInt
  public var colorGradientStart: Int by highlightViewProperty(NO_COLOR)

  @get:ColorInt
  public var colorGradientCenter: Int by highlightViewProperty(NO_COLOR)

  @get:ColorInt
  public var colorGradientEnd: Int by highlightViewProperty(NO_COLOR)

  public var highlight: Drawable? by highlightViewProperty(null)

  public var orientation: ProgressViewOrientation by highlightViewProperty(ProgressViewOrientation.HORIZONTAL)

  public var onProgressClickListener: OnProgressClickListener? = null

  init {
    addView(bodyView)
    addView(strokeView)
    strokeView.setOnClickListener {
      highlighting = highlighting.not()
      onProgressClickListener?.onClickProgress(highlighting)
    }
  }

  public fun updateHighlightView() {
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
      GradientDrawable(
        gradientOrientation,
        intArrayOf(colorGradientStart, colorGradientCenter, colorGradientEnd)
          .filter { it != NO_COLOR }.toIntArray()
      ).apply {
        applyRadius(this)
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
