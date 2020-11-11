package app.com.animation

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.example.android.navigationadvancedsample.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File

class MainActivity : AppCompatActivity() {
    companion object {
        private const val REQUEST = 0
        private const val APP_DIR = "CodeView"
    }
    var bottomNavigationView: BottomNavigationView? = null

    var currentNavController: LiveData<NavController>? = null

    private var hasPermission = false

    lateinit var folder : File

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
            folder = File(Environment.getExternalStorageDirectory().absolutePath,
                APP_DIR).apply { if (!exists()) {mkdir()} }
            setContentView(R.layout.activity_main)
            if (savedInstanceState == null) {
                setupBottomNavigationBar()
            }
            openViewer()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }


    private fun setupBottomNavigationBar() {
        bottomNavigationView = findViewById(R.id.bottom_nav)
        val navGraphIds = listOf(R.navigation.git, R.navigation.code_viewer, R.navigation.storage)
        currentNavController = bottomNavigationView?.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )
    }

    fun changeBottom(colour : Int) {
        bottomNavigationView?.setBackgroundColor(resources.getColor(colour))
    }

    fun openViewer() {
        bottomNavigationView?.selectedItemId = R.id.code_viewer
    }

    fun updateViewer() {
        currentNavController?.value?.popBackStack()
        currentNavController?.value?.navigate(R.id.codeViewFragment)
    }

    fun updateStorage() {
        currentNavController?.value?.popBackStack()
        currentNavController?.value?.navigate(R.id.storageFragment)
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    private fun askForPermission() {
        val permissions = arrayOf(
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET),
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE),
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET)
        )
        if (permissions.contains(PackageManager.PERMISSION_DENIED)) {
            ActivityCompat.requestPermissions(this, arrayOf(
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST)
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
            REQUEST -> {
                if (grantResults.size == 3 &&
                    grantResults.all { res -> res == PackageManager.PERMISSION_GRANTED } ) {
                    hasPermission = true
                    recreate()
                }
            }
        }
    }
}