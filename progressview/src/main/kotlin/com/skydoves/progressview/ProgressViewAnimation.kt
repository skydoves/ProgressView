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

import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator

/** ProgressViewAnimation is a collection of progress animation. */
public enum class ProgressViewAnimation(public val value: Int) {
  NORMAL(0),
  BOUNCE(1),
  DECELERATE(2),
  ACCELERATEDECELERATE(3);

  public fun getInterpolator(): Interpolator {
    return when (value) {
      BOUNCE.value -> BounceInterpolator()
      DECELERATE.value -> DecelerateInterpolator()
      ACCELERATEDECELERATE.value -> AccelerateDecelerateInterpolator()
      else -> AccelerateInterpolator()
    }
  }
}
