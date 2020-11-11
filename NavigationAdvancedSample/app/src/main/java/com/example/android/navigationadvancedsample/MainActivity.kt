package com.example.android.navigationadvancedsample

import android.app.Activity
import android.app.ActivityManager
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.MemoryFile
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private val request = 0
    private var hasPermission = false
    var bottomNavigationView: BottomNavigationView? = null

    var currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        askForPermission()
        if (!hasPermission) {
            setContentView(R.layout.access_denied)
            findViewById<Button>(R.id.error_retry).run {
                setOnClickListener {
                    recreate()
                }
            }
        } else {
            setContentView(R.layout.activity_main)
            if (savedInstanceState == null) {
                setupBottomNavigationBar()
            }
        }
    }

    fun changeBottom(color: Int) {
        bottomNavigationView?.setBackgroundColor(resources.getColor(color))
    }

    /*override fun onResume() {
        super.onResume()
        bottomNavigationView?.setOnNavigationItemSelectedListener {
            return@setOnNavigationItemSelectedListener when (it.itemId) {
                R.id.images -> {
                    bottomNavigationView!!.setBackgroundColor(resources.getColor(R.color.darkGrey))
                    true
                }
                R.id.audio -> {
                    bottomNavigationView!!.setBackgroundColor(resources.getColor(R.color.brightWhite))
                    true
                }
                R.id.home -> {
                    bottomNavigationView!!.setBackgroundColor(resources.getColor(R.color.brightWhite))
                    true
                }
                else -> false
            }
        }
    }*/
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }


    private fun setupBottomNavigationBar() {
        bottomNavigationView = findViewById(R.id.bottom_nav)
        val navGraphIds = listOf(R.navigation.home, R.navigation.images, R.navigation.audio)
        val controller = bottomNavigationView?.setupWithNavController(
                navGraphIds = navGraphIds,
                fragmentManager = supportFragmentManager,
                containerId = R.id.nav_host_container,
                intent = intent
        )
        controller?.observe(this, Observer { navController ->
            setupActionBarWithNavController(navController)
        })
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    private fun askForPermission() {
        val permission = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), request)
        } else {
            hasPermission = true
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            request -> {
                if (grantResults.size == 1 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    hasPermission = true
                    recreate()
                }
            }
        }
    }
}
