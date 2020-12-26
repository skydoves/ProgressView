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

package com.skydoves.progressviewdemo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.skydoves.progressviewdemo.databinding.ActivityCustomBinding

class CustomActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val binding = ActivityCustomBinding.inflate(layoutInflater)
    setContentView(binding.root)

    with(binding) {
      button.setOnClickListener {
        progressView.progress += 35
        progressView1.progress += 20
        progressView2.progress += 15
        progressView3.progress += 25
        progressView4.progress += 15
        progressView5.progress += 15
      }

      progressView.setOnProgressChangeListener { progressView.labelText = "${it.toInt()}%" }
      progressView1.setOnProgressChangeListener { progressView1.labelText = "${it.toInt()}%" }
      progressView2.setOnProgressChangeListener { progressView2.labelText = "${it.toInt()}%" }
      progressView3.setOnProgressChangeListener { progressView3.labelText = "${it.toInt()}%" }
      progressView4.setOnProgressChangeListener { progressView4.labelText = "${it.toInt()}%" }
      progressView5.setOnProgressChangeListener { progressView5.labelText = "${it.toInt()}%" }

      progressView.progressFromPrevious = true
      progressView5.radiusArray = floatArrayOf(0f, 0f, 0f, 0f, 50f, 50f, 50f, 50f)

      progressView3.setOnProgressClickListener {
        if (it) {
          Toast.makeText(this@CustomActivity, "highlight on", Toast.LENGTH_SHORT)
            .show()
        } else {
          Toast.makeText(this@CustomActivity, "highlight off", Toast.LENGTH_SHORT)
            .show()
        }
      }
    }
  }
}
