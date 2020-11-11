/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.navigationadvancedsample.homescreen

import android.app.ActivityManager
import android.app.Service
import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.android.navigationadvancedsample.MainActivity
import com.example.android.navigationadvancedsample.R
import java.io.File


class About : Fragment() {

    private lateinit var memoryInfo: ActivityManager.MemoryInfo
    private lateinit var activityManager: ActivityManager
    private lateinit var info: LinkedHashMap<String, Float>
    private val toMB = 1048576F
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityManager = activity?.getSystemService(Service.ACTIVITY_SERVICE) as ActivityManager
        memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        info = linkedMapOf()
        infoRam()
        infoInternal()
        infoExternal()
    }

    private fun infoRam() {
        info["Free RAM: "] = memoryInfo.availMem / toMB
        info["Total RAM: "] = memoryInfo.totalMem / toMB
    }

    private fun infoInternal() {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        info["Available Internal: "] = blockSize * stat.availableBlocksLong / toMB
        info["Total Internal: "] = blockSize * stat.blockCountLong / toMB
    }

    private fun infoExternal() {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val path = Environment.getExternalStorageDirectory()
            val stat = StatFs(path.path)
            val blockSize = stat.blockSizeLong
            info["Available External: "] = blockSize * stat.availableBlocksLong / toMB
            info["Total External: "] = blockSize * stat.blockCountLong / toMB
        } else {
            info["Available External: "] = 0F
            info["Total External: "] = 0F
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_about, container, false)
        var params = ""
        var values = ""
        for ((key, value) in info) {
            params += key + "\n"
            values += String.format("%.2f MB\n", value)
        }
        view.findViewById<TextView>(R.id.mem_params).text = params
        view.findViewById<TextView>(R.id.mem_values).text = values
        (activity as MainActivity).changeBottom(R.color.blue)
        return view
    }
}
