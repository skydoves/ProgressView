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
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.Px

@DslMarker
annotation class ProgressViewDSL

/** creates an instance of [ProgressView] by [ProgressView.Builder] using kotlin dsl. */
fun progressView(context: Context, block: ProgressView.Builder.() -> Unit): ProgressView =
  ProgressView.Builder(context).apply(block).build()

/** ProgressView is a progress bar with a flexible text and animations. */
class ProgressView : FrameLayout {

  val labelView = TextView(context)
  val highlightView = HighlightView(context)

  var duration = 1000L
  var autoAnimate = true
  var min = 0f
  var max = 100f
    set(value) {
      field = value
      updateProgressView()
    }
  var progress = 0f
    set(value) {
      field = when {
        value >= max -> max
        value <= min -> min
        else -> value
      }
      updateProgressView()
      onProgressChangeListener?.onChange(field)
    }
  var orientation = ProgressViewOrientation.HORIZONTAL
    set(value) {
      field = value
      updateProgressView()
    }
  @ColorInt var colorBackground = compatColor(R.color.white)
    set(value) {
      field = value
      updateProgressView()
    }
  @Px var radius = dp2Px(5).toFloat()
    set(value) {
      field = value
      updateProgressView()
    }
  var labelText: String? = ""
    set(value) {
      field = value
      updateProgressView()
    }
  @Px var labelSize = 12f
    set(value) {
      field = value
      updateProgressView()
    }
  @ColorInt var labelColorInner = compatColor(R.color.white)
    set(value) {
      field = value
      updateProgressView()
    }
  @ColorInt var labelColorOuter = compatColor(R.color.black)
    set(value) {
      field = value
      updateProgressView()
    }
  var labelTypeface = Typeface.NORMAL
    set(value) {
      field = value
      updateProgressView()
    }
  var labelTypefaceObject: Typeface? = null
    set(value) {
      field = value
      updateProgressView()
    }
  @Px var labelSpace = dp2Px(8).toFloat()
    set(value) {
      field = value
      updateProgressView()
    }
  private var onProgressChangeListener: OnProgressChangeListener? = null
  private var onProgressClickListener: OnProgressClickListener? = null

  private val path = Path()

  constructor(context: Context) : super(context)
  constructor(context: Context, attributeSet: AttributeSet) : this(context, attributeSet, 0)
  constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(context,
    attributeSet, defStyle) {
    getAttrs(attributeSet, defStyle)
  }

  private fun getAttrs(attributeSet: AttributeSet, defStyleAttr: Int) {
    val typedArray =
      context.obtainStyledAttributes(attributeSet, R.styleable.ProgressView, defStyleAttr, 0)
    try {
      setTypeArray(typedArray)
    } finally {
      typedArray.recycle()
    }
  }

  private fun setTypeArray(a: TypedArray) {
    this.labelText = a.getString(R.styleable.ProgressView_progressView_labelText)
    this.labelSize =
      px2Sp(a.getDimension(R.styleable.ProgressView_progressView_labelSize, labelSize))
    this.labelSpace = a.getDimension(R.styleable.ProgressView_progressView_labelSpace, labelSpace)
    this.labelColorInner =
      a.getColor(R.styleable.ProgressView_progressView_labelColorInner, labelColorInner)
    this.labelColorOuter =
      a.getColor(R.styleable.ProgressView_progressView_labelColorOuter, labelColorOuter)
    when (a.getInt(R.styleable.ProgressView_progressView_labelTypeface, Typeface.NORMAL)) {
      0 -> this.labelTypeface = Typeface.NORMAL
      1 -> this.labelTypeface = Typeface.BOLD
      2 -> this.labelTypeface = Typeface.ITALIC
    }
    when (a.getInt(R.styleable.ProgressView_progressView_orientation,
      ProgressViewOrientation.HORIZONTAL.value)) {
      0 -> {
        this.orientation = ProgressViewOrientation.HORIZONTAL
        this.highlightView.orientation = ProgressViewOrientation.HORIZONTAL
      }
      1 -> {
        this.orientation = ProgressViewOrientation.VERTICAL
        this.highlightView.orientation = ProgressViewOrientation.VERTICAL
      }
    }
    this.min = a.getFloat(R.styleable.ProgressView_progressView_min, min)
    this.max = a.getFloat(R.styleable.ProgressView_progressView_max, max)
    this.progress = a.getFloat(R.styleable.ProgressView_progressView_progress, progress)
    this.radius = a.getDimension(R.styleable.ProgressView_progressView_radius, radius)
    this.duration =
      a.getInteger(R.styleable.ProgressView_progressView_duration, duration.toInt()).toLong()
    this.colorBackground =
      a.getColor(R.styleable.ProgressView_progressView_colorBackground, colorBackground)
    this.autoAnimate = a.getBoolean(R.styleable.ProgressView_progressView_autoAnimate, autoAnimate)
    with(this.highlightView) {
      alpha = a.getFloat(R.styleable.ProgressView_progressView_highlightAlpha, highlightAlpha)
      color = a.getColor(R.styleable.ProgressView_progressView_colorProgress, color)
      colorGradientStart =
        a.getColor(R.styleable.ProgressView_progressView_colorGradientStart, 65555)
      colorGradientEnd = a.getColor(R.styleable.ProgressView_progressView_colorGradientEnd, 65555)
      radius = a.getDimension(R.styleable.ProgressView_progressView_radius, radius)
      padding = a.getDimension(R.styleable.ProgressView_progressView_padding, padding)
      highlightColor =
        a.getColor(R.styleable.ProgressView_progressView_highlightColor, highlightColor)
      highlightThickness = a.getDimension(R.styleable.ProgressView_progressView_highlightWidth,
        highlightThickness.toFloat()).toInt()
      if (!a.getBoolean(R.styleable.ProgressView_progressView_highlighting, !highlighting)) {
        highlightThickness = 0
      }
    }
  }

  override fun onFinishInflate() {
    super.onFinishInflate()
    updateProgressView()
  }

  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    super.onSizeChanged(w, h, oldw, oldh)
    this.path.apply {
      reset()
      addRoundRect(RectF(0f, 0f, w.toFloat(), h.toFloat()),
        floatArrayOf(radius, radius, radius, radius, radius, radius, radius, radius),
        Path.Direction.CCW)
    }
  }

  override fun dispatchDraw(canvas: Canvas) {
    canvas.clipPath(this.path)
    super.dispatchDraw(canvas)
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
    this.background = GradientDrawable().apply {
      cornerRadius = radius
      setColor(colorBackground)
    }
  }

  private fun updateOrientation() {
    if (this.orientation == ProgressViewOrientation.VERTICAL) {
      rotation = 180f
      labelView.rotation = 180f
    }
  }

  private fun updateHighlightView() {
    val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    if (max <= progress) {
      if (isVertical()) {
        params.height = getViewSize(this)
      } else {
        params.width = getViewSize(this)
      }
    } else {
      if (isVertical()) {
        params.height = getProgressSize().toInt()
      } else {
        params.width = getProgressSize().toInt()
      }
    }
    this.highlightView.layoutParams = params
    this.highlightView.updateHighlightView()
    removeView(highlightView)
    addView(highlightView)
  }

  private fun updateLabel() {
    var params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
      ViewGroup.LayoutParams.MATCH_PARENT)
    if (!isVertical()) {
      this.labelView.gravity = Gravity.CENTER_VERTICAL
    } else {
      params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT)
      this.labelView.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
    }
    this.labelView.layoutParams = params
    applyTextForm(textForm(context) {
      text = labelText
      textSize = labelSize
      textTypeface = labelTypeface
      textTypefaceObject = labelTypefaceObject
    })
    removeView(labelView)
    addView(labelView)

    post {
      when {
        this.labelView.width + labelSpace < getProgressSize() -> {
          setLabelViewPosition(getProgressSize() - this.labelView.width - this.labelSpace)
          this.labelView.setTextColor(labelColorInner)
        }
        else -> {
          setLabelViewPosition(getProgressSize() + this.labelSpace)
          this.labelView.setTextColor(labelColorOuter)
        }
      }
    }
  }

  private fun setLabelViewPosition(position: Float) {
    if (isVertical()) {
      labelView.y = position
    } else {
      labelView.x = position
    }
  }

  private fun getProgressSize(): Float {
    return (getViewSize(this) / max) * progress
  }

  private fun getLabelPosition(): Float {
    return when {
      this.labelView.width + labelSpace < getProgressSize() -> getProgressSize() - this.labelView.width - this.labelSpace
      else -> getProgressSize() + this.labelSpace
    }
  }

  private fun autoAnimate() {
    if (this.autoAnimate) {
      progressAnimate()
    }
  }

  private fun getViewSize(view: View): Int {
    return if (isVertical()) view.height
    else view.width
  }

  /** animates [ProgressView]'s progress bar. */
  fun progressAnimate() {
    ValueAnimator.ofFloat(0f, 1f).apply {
      duration = this@ProgressView.duration
      addUpdateListener {
        val value = it.animatedValue as Float
        setLabelViewPosition(getLabelPosition() * value)
        highlightView.updateLayoutParams {
          if (isVertical()) {
            height = (getProgressSize() * value).toInt()
          } else {
            width = (getProgressSize() * value).toInt()
          }
        }
      }
      start()
    }
  }

  private fun isVertical(): Boolean {
    return orientation == ProgressViewOrientation.VERTICAL
  }

  /** sets a progress change listener. */
  fun setOnProgressChangeListener(onProgressChangeListener: OnProgressChangeListener) {
    this.onProgressChangeListener = onProgressChangeListener
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
  fun setOnProgressClickListener(onProgressClickListener: OnProgressClickListener) {
    this.onProgressClickListener = onProgressClickListener
    this.highlightView.onProgressClickListener = this.onProgressClickListener
  }

  /** sets a progress click listener. */
  fun setOnProgressClickListener(block: (Boolean) -> Unit) {
    this.onProgressClickListener = object : OnProgressClickListener {
      override fun onClickProgress(highlighting: Boolean) {
        block(highlighting)
      }
    }
    this.highlightView.onProgressClickListener = this.onProgressClickListener
  }

  /** applies [TextForm] attributes to a TextView. */
  fun applyTextForm(textForm: TextForm) {
    this.labelView.applyTextForm(textForm)
  }

  /** Builder class for creating [ProgressView]. */
  @ProgressViewDSL
  class Builder(context: Context) {
    private val progressView = ProgressView(context)

    fun setSize(@Px width: Int, @Px height: Int): Builder = apply {
      this.progressView.layoutParams =
        LayoutParams(progressView.dp2Px(width), progressView.dp2Px(height))
    }

    fun setHeight(@Px value: Int): Builder = apply { this.progressView.layoutParams.height = value }
    fun setDuration(value: Long): Builder = apply { this.progressView.duration = value }
    fun setAutoAnimate(value: Boolean): Builder = apply { this.progressView.autoAnimate = value }
    fun setMin(value: Float): Builder = apply { this.progressView.min = value }
    fun setMax(value: Float): Builder = apply { this.progressView.max = value }
    fun setProgress(value: Float): Builder = apply { this.progressView.progress = value }
    fun setOrientation(value: ProgressViewOrientation): Builder = apply {
      this.progressView.orientation = value
    }

    fun setColorBackground(@ColorInt value: Int): Builder = apply {
      this.progressView.colorBackground = value
    }

    fun setRadius(@Px value: Float): Builder = apply { this.progressView.radius = value }
    fun setLabelText(value: String): Builder = apply { this.progressView.labelText = value }
    fun setLabelSize(value: Float): Builder = apply {
      this.progressView.labelSize = this.progressView.sp2Px(value)
    }

    fun setLabelSpace(@Px value: Float): Builder = apply { this.progressView.labelSpace = value }
    fun setLabelColorInner(@ColorInt value: Int): Builder = apply {
      this.progressView.labelColorInner = value
    }

    fun setLabelColorOuter(@ColorInt value: Int): Builder = apply {
      this.progressView.labelColorOuter = value
    }

    fun setLabelTypeface(value: Int): Builder = apply { this.progressView.labelTypeface = value }
    fun setLabelTypeface(value: Typeface): Builder = apply {
      this.progressView.labelTypefaceObject = value
    }

    fun setProgressbarAlpha(@FloatRange(from = 0.0, to = 1.0) value: Float): Builder = apply {
      this.progressView.highlightView.alpha = value
    }

    fun setProgressbarColor(@ColorInt value: Int): Builder = apply {
      this.progressView.highlightView.color = value
    }

    fun setProgressbarColorGradientStart(@ColorInt value: Int): Builder = apply {
      this.progressView.highlightView.colorGradientStart = value
    }

    fun setProgressbarColorGradientEnd(@ColorInt value: Int): Builder = apply {
      this.progressView.highlightView.colorGradientEnd = value
    }

    fun setProgressbarRadius(@Px value: Float): Builder = apply {
      this.progressView.highlightView.radius = value
    }

    fun setProgressbarPadding(@Px value: Float): Builder = apply {
      this.progressView.highlightView.padding = value
    }

    fun setHighlightColor(@ColorInt value: Int): Builder = apply {
      this.progressView.highlightView.highlightColor = value
    }

    fun setHighlighting(value: Boolean): Builder = apply {
      this.progressView.highlightView.highlighting = value
    }

    fun setHighlightThickness(@Px value: Int): Builder = apply {
      this.progressView.highlightView.highlightThickness = value
    }

    fun setOnProgressChangeListener(value: OnProgressChangeListener): Builder = apply {
      this.progressView.onProgressChangeListener = value
    }

    fun setOnProgressClickListener(value: OnProgressClickListener): Builder = apply {
      this.progressView.onProgressClickListener = value
    }

    fun setTextForm(value: TextForm): Builder = apply {
      this.progressView.labelView.applyTextForm(value)
    }

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
