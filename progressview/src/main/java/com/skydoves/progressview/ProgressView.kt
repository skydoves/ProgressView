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

@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.skydoves.progressview

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView

@DslMarker
annotation class ProgressViewDSL

/** creates an instance of [ProgressView] by [ProgressView.Builder] using kotlin dsl. */
fun progressView(context: Context, block: ProgressView.Builder.() -> Unit): ProgressView =
  ProgressView.Builder(context).apply(block).build()

/** ProgressView is a progress bar with a flexible text and animations. */
class ProgressView : FrameLayout {

  val label = TextView(context)
  val highlight = HighlightView(context)

  var duration = 1000L
  var autoAnimate = true
  var max = 100f
    set(value) {
      field = value
      updateProgressView()
    }
  var progress = 0f
    set(value) {
      field = if (value >= max) max
      else value
      updateProgressView()
      onProgressChangeListener?.onChange(field)
    }
  var orientation = ProgressViewOrientation.HORIZONTAL
    set(value) {
      field = value
      updateProgressView()
    }
  var colorBackground = compatColor(R.color.white)
    set(value) {
      field = value
      updateProgressView()
    }
  var radius = dp2Px(5).toFloat()
    set(value) {
      field = value
      updateProgressView()
    }
  var labelText: String? = ""
    set(value) {
      field = value
      updateProgressView()
    }
  var labelSize = 12f
    set(value) {
      field = value
      updateProgressView()
    }
  var labelColorInner = compatColor(R.color.white)
    set(value) {
      field = value
      updateProgressView()
    }
  var labelColorOuter = compatColor(R.color.black)
    set(value) {
      field = value
      updateProgressView()
    }
  var labelTypeface = Typeface.NORMAL
    set(value) {
      field = value
      updateProgressView()
    }
  var labelSpace = dp2Px(8).toFloat()
    set(value) {
      field = value
      updateProgressView()
    }
  var onProgressChangeListener: OnProgressChangeListener? = null
  var onProgressClickListener: OnProgressClickListener? = null
    set(value) {
      field = value
      this.highlight.onProgressClickListener = value
    }

  constructor(context: Context) : super(context)

  constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
    getAttrs(attributeSet)
  }

  constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(context, attributeSet, defStyle) {
    getAttrs(attributeSet, defStyle)
  }

  private fun getAttrs(attributeSet: AttributeSet) {
    val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ProgressView)
    try {
      setTypeArray(typedArray)
    } finally {
      typedArray.recycle()
    }
  }

  private fun getAttrs(attributeSet: AttributeSet, defStyleAttr: Int) {
    val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ProgressView, defStyleAttr, 0)
    try {
      setTypeArray(typedArray)
    } finally {
      typedArray.recycle()
    }
  }

  private fun setTypeArray(a: TypedArray) {
    this.labelText = a.getString(R.styleable.ProgressView_progressView_labelText)
    this.labelSize = a.getDimension(R.styleable.ProgressView_progressView_labelSize, labelSize)
    this.labelSpace = a.getDimension(R.styleable.ProgressView_progressView_labelSpace, labelSpace)
    this.labelColorInner = a.getColor(R.styleable.ProgressView_progressView_labelColorInner, labelColorInner)
    this.labelColorOuter = a.getColor(R.styleable.ProgressView_progressView_labelColorOuter, labelColorOuter)
    when (a.getInt(R.styleable.ProgressView_progressView_labelTypeface, Typeface.NORMAL)) {
      0 -> this.labelTypeface = Typeface.NORMAL
      1 -> this.labelTypeface = Typeface.BOLD
      2 -> this.labelTypeface = Typeface.ITALIC
    }
    when (a.getInt(R.styleable.ProgressView_progressView_orientation, ProgressViewOrientation.HORIZONTAL.value)) {
      0 -> this.orientation = ProgressViewOrientation.HORIZONTAL
      1 -> this.orientation = ProgressViewOrientation.VERTICAL
    }
    this.max = a.getFloat(R.styleable.ProgressView_progressView_max, max)
    this.progress = a.getFloat(R.styleable.ProgressView_progressView_progress, progress)
    this.radius = a.getDimension(R.styleable.ProgressView_progressView_radius, radius)
    this.duration = a.getInteger(R.styleable.ProgressView_progressView_duration, duration.toInt()).toLong()
    this.colorBackground = a.getColor(R.styleable.ProgressView_progressView_colorBackground, colorBackground)
    this.autoAnimate = a.getBoolean(R.styleable.ProgressView_progressView_autoAnimate, autoAnimate)
    with(this.highlight) {
      alpha = a.getFloat(R.styleable.ProgressView_progressView_highlightAlpha, highlightAlpha)
      color = a.getColor(R.styleable.ProgressView_progressView_colorProgress, color)
      colorGradientStart = a.getColor(R.styleable.ProgressView_progressView_colorGradientStart, 65555)
      colorGradientEnd = a.getColor(R.styleable.ProgressView_progressView_colorGradientEnd, 65555)
      radius = a.getDimension(R.styleable.ProgressView_progressView_radius, radius)
      padding = a.getDimension(R.styleable.ProgressView_progressView_padding, padding)
      highlightColor = a.getColor(R.styleable.ProgressView_progressView_highlightColor, highlightColor)
      highlightThickness = a.getDimension(R.styleable.ProgressView_progressView_highlightWidth, highlightThickness.toFloat()).toInt()
      if (!a.getBoolean(R.styleable.ProgressView_progressView_highlighting, !highlighting)) {
        highlightThickness = 0
      }
    }
  }

  override fun onFinishInflate() {
    super.onFinishInflate()
    updateProgressView()
  }

  private fun updateProgressView() {
    post {
      updateHighlightView()
      updateLabel()
    }
    updateBackground()
    updateOrientation()
    autoAnimate()
  }

  private fun updateBackground() {
    val drawable = GradientDrawable()
    drawable.cornerRadius = radius
    drawable.setColor(colorBackground)
    this.background = drawable
  }

  private fun updateOrientation() {
    if (orientation == ProgressViewOrientation.VERTICAL) {
      rotation = -90f
    }
  }

  private fun updateHighlightView() {
    val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    if (max <= progress) {
      params.width = width
    } else {
      params.width = getProgressSize().toInt()
    }
    this.highlight.layoutParams = params
    this.highlight.updateHighlightView()
    removeView(highlight)
    addView(highlight)
  }

  private fun updateLabel() {
    val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
    this.label.layoutParams = params
    this.label.text = labelText
    this.label.setTextSize(TypedValue.COMPLEX_UNIT_PX, labelSize)
    this.label.setTypeface(label.typeface, labelTypeface)
    this.label.gravity = Gravity.CENTER_VERTICAL
    removeView(label)
    addView(label)

    post {
      when {
        max <= progress -> this.label.x = width.toFloat() - this.label.width - this.labelSpace
        this.label.width + labelSpace < getProgressSize() -> {
          this.label.x = getProgressSize() - this.label.width - this.labelSpace
          this.label.setTextColor(labelColorInner)
        }
        else -> {
          this.label.x = getProgressSize() + this.labelSpace
          this.label.setTextColor(labelColorOuter)
        }
      }
    }
  }

  private fun getProgressSize(): Float {
    return (width / max) * progress
  }

  private fun getLabelPosition(): Float {
    return when {
      max <= progress -> width.toFloat() - this.label.width - this.labelSpace
      this.label.width + labelSpace < getProgressSize() -> getProgressSize() - this.label.width - this.labelSpace
      else -> getProgressSize() + this.labelSpace
    }
  }

  private fun autoAnimate() {
    if (this.autoAnimate) {
      progressAnimate()
    }
  }

  /** animates [ProgressView]'s progress bar. */
  fun progressAnimate() {
    this.label.x = 0f
    this.highlight.updateLayoutParams { width = 0 }
    val animator = ValueAnimator.ofFloat(0f, 1f)
    animator.duration = duration
    animator.addUpdateListener {
      val value = it.animatedValue as Float
      this.label.x = getLabelPosition() * value
      this.highlight.updateLayoutParams {
        width = (getProgressSize() * value).toInt()
      }
    }
    animator.start()
  }

  /** sets a progress change listener. */
  fun setOnProgressChangeListener(block: (Float) -> Unit) {
    this.onProgressChangeListener = object : OnProgressChangeListener {
      override fun onChange(progress: Float) {
        block(progress)
      }
    }
  }

  /** sets a progress click listener. */
  fun setOnProgressClickListener(block: (Boolean) -> Unit) {
    this.onProgressClickListener = object : OnProgressClickListener {
      override fun onClickProgress(highlighting: Boolean) {
        block(highlighting)
      }
    }
  }

  /** applies [TextForm] attributes to a TextView. */
  fun applyTextForm(textForm: TextForm) {
    this.label.applyTextForm(textForm)
  }

  /** Builder class for creating [ProgressView]. */
  @ProgressViewDSL
  class Builder(context: Context) {
    private val progressView = ProgressView(context)

    fun setSize(width: Int, height: Int): Builder = apply { this.progressView.layoutParams = LayoutParams(progressView.dp2Px(width), progressView.dp2Px(height)) }
    fun setHeight(value: Int): Builder = apply { this.progressView.layoutParams.height = value }
    fun setDuration(value: Long): Builder = apply { this.progressView.duration = value }
    fun setAutoAnimate(value: Boolean): Builder = apply { this.progressView.autoAnimate = value }
    fun setMax(value: Float): Builder = apply { this.progressView.max = value }
    fun setProgress(value: Float): Builder = apply { this.progressView.progress = value }
    fun setOrientation(value: ProgressViewOrientation): Builder = apply { this.progressView.orientation = value }
    fun setColorBackground(value: Int): Builder = apply { this.progressView.colorBackground = value }
    fun setRadius(value: Float): Builder = apply { this.progressView.radius = value }
    fun setLabelText(value: String): Builder = apply { this.progressView.labelText = value }
    fun setLabelSize(value: Float): Builder = apply { this.progressView.labelSize = this.progressView.sp2Px(value) }
    fun setLabelSpace(value: Float): Builder = apply { this.progressView.labelSpace = value }
    fun setLabelColorInner(value: Int): Builder = apply { this.progressView.labelColorInner = value }
    fun setLabelColorOuter(value: Int): Builder = apply { this.progressView.labelColorOuter = value }
    fun setLabelTypeface(value: Int): Builder = apply { this.progressView.labelTypeface = value }
    fun setProgressbarAlpha(value: Float): Builder = apply { this.progressView.highlight.alpha = value }
    fun setProgressbarColor(value: Int): Builder = apply { this.progressView.highlight.color = value }
    fun setProgressbarColorGradientStart(value: Int): Builder = apply { this.progressView.highlight.colorGradientStart = value }
    fun setProgressbarColorGradientEnd(value: Int): Builder = apply { this.progressView.highlight.colorGradientEnd = value }
    fun setProgressbarRadius(value: Float): Builder = apply { this.progressView.highlight.radius = value }
    fun setProgressbarPadding(value: Float): Builder = apply { this.progressView.highlight.padding = value }
    fun setHighlightColor(value: Int): Builder = apply { this.progressView.highlight.highlightColor = value }
    fun setHighlighting(value: Boolean): Builder = apply { this.progressView.highlight.highlighting = value }
    fun setHighlightThickness(value: Int): Builder = apply { this.progressView.highlight.highlightThickness = value }
    fun setOnProgressChangeListener(value: OnProgressChangeListener): Builder = apply { this.progressView.onProgressChangeListener = value }
    fun setOnProgressClickListener(value: OnProgressClickListener): Builder = apply { this.progressView.onProgressClickListener = value }
    fun setTextForm(value: TextForm): Builder = apply { this.progressView.label.applyTextForm(value) }
    fun setOnProgressChangeListener(block: (Float) -> Unit): Builder = apply {
      this.progressView.onProgressChangeListener = object : OnProgressChangeListener {
        override fun onChange(progress: Float) {
          block(progress)
        }
      }
    }

    fun setOnProgressClickListener(block: (Boolean) -> Unit): Builder = apply {
      this.progressView.onProgressClickListener = object : OnProgressClickListener {
        override fun onClickProgress(highlighting: Boolean) {
          block(highlighting)
        }
      }
    }

    fun build() = progressView
  }
}
