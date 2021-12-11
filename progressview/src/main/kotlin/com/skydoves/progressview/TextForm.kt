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

@file:Suppress("unused")

package com.skydoves.progressview

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.Px
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

@DslMarker
internal annotation class TextFormDsl

/** creates an instance of [TextForm] from [TextForm.Builder] using kotlin dsl. */
@TextFormDsl
@JvmSynthetic
public inline fun textForm(
  context: Context,
  crossinline block: TextForm.Builder.() -> Unit
): TextForm =
  TextForm.Builder(context).apply(block).build()

/**
 * TextFrom is an attribute class what has some attributes about TextView
 * for customizing popup texts easily.
 */
public class TextForm(builder: Builder) {

  public val text: CharSequence? = builder.text
  @Px public val textSize: Float = builder.textSize
  @ColorInt public val textColor: Int = builder.textColor
  public val textStyle: Int = builder.textTypeface
  public val textStyleObject: Typeface? = builder.textTypefaceObject

  /** Builder class for [TextForm]. */
  @TextFormDsl
  public class Builder(private val context: Context) {
    @JvmField
    @set:JvmSynthetic
    public var text: CharSequence? = ""

    @JvmField @Px
    @set:JvmSynthetic
    public var textSize: Float = 12f

    @JvmField @ColorInt
    @set:JvmSynthetic
    public var textColor: Int = Color.WHITE

    @JvmField
    @set:JvmSynthetic
    public var textTypeface: Int = Typeface.NORMAL

    @JvmField
    @set:JvmSynthetic
    public var textTypefaceObject: Typeface? = null

    public fun setText(value: CharSequence): Builder = apply { this.text = value }
    public fun setTextResource(@StringRes value: Int): Builder = apply {
      this.text = context.getString(value)
    }

    public fun setTextSize(@Px value: Float): Builder = apply { this.textSize = value }
    public fun setTextColor(@ColorInt value: Int): Builder = apply { this.textColor = value }
    public fun setTextColorResource(@ColorRes value: Int): Builder = apply {
      this.textColor = ContextCompat.getColor(context, value)
    }

    public fun setTextTypeface(value: Int): Builder = apply { this.textTypeface = value }
    public fun setTextTypeface(value: Typeface): Builder = apply { this.textTypefaceObject = value }
    public fun build(): TextForm = TextForm(this)
  }
}
