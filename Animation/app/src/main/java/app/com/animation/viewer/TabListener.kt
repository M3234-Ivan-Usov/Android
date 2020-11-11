package app.com.animation.viewer


import com.google.android.material.tabs.TabLayout

class TabListener(private val codeView : CodeViewFragment) : TabLayout.OnTabSelectedListener {
    override fun onTabReselected(p0: TabLayout.Tab?) {
    }

    override fun onTabUnselected(p0: TabLayout.Tab?) {
    }

    override fun onTabSelected(p0: TabLayout.Tab?) {
        p0?.position?.let { codeView.update(it) }
    }
}