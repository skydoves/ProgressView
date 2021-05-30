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

import android.animation.Animator
import android.animation.Animator.AnimatorListener

@JvmSynthetic
internal fun Animator.doStartAndFinish(
  start: () -> Unit,
  finish: () -> Unit
) {
  addListener(
    object : AnimatorListener {
      override fun onAnimationStart(animator: Animator?) = start()
      override fun onAnimationEnd(animator: Animator?) = finish()
      override fun onAnimationCancel(animator: Animator?) = Unit
      override fun onAnimationRepeat(animator: Animator?) = Unit
    }
  )
}
