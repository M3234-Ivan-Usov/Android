package app.contacts

import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var dataList: Set<Contact>
    private lateinit var mainViewer: RecyclerView
    private lateinit var mainAdapter: ContactAdapter
    private lateinit var restart: Button
    private lateinit var header: TextView
    private val request = 0
    private var hasPermission = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        askForPermission()
        if (!hasPermission) {
            setContentView(R.layout.access_denied)
            restart = findViewById(R.id.error_retry)
            restart.setOnClickListener {
                this.recreate()
            }
        } else {
            setContentView(R.layout.activity_main)
            dataList = fetchAllContacts()
            mainAdapter = ContactAdapter(this, dataList)
            header = findViewById(R.id.header)
            mainViewer = findViewById(R.id.main_viewer)
            header.text = resources.getQuantityString(R.plurals.amount, dataList.size, dataList.size)
            mainViewer.adapter = mainAdapter
        }
    }

    private fun askForPermission() {
        val hasReadPermission = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_CONTACTS
        )
        val hasSmsPermission = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.SEND_SMS
        )
        if (hasReadPermission != PackageManager.PERMISSION_GRANTED ||
            hasSmsPermission != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.READ_CONTACTS,
                    android.Manifest.permission.SEND_SMS
                ), request
            )
        }
        else {
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
                if (grantResults.size == 2 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {
                    hasPermission = true
                    recreate()
                }
            }
        }
    }
}