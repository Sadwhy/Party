package io.sadwhy.party.ui.utils

import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout

object AnimationUtils {

    fun showTitleOnCollapse(
        appBarLayout: AppBarLayout,
        collapsingToolbar: CollapsingToolbarLayout
    ) {
        var isTitleVisible = false
        var scrollRange = -1

        appBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(layout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = layout.totalScrollRange
                }

                if (scrollRange + verticalOffset == 0 && !isTitleVisible) {
                    collapsingToolbar.isTitleEnabled = true
                    isTitleVisible = true
                } else if (isTitleVisible) {
                    collapsingToolbar.isTitleEnabled = false
                    isTitleVisible = false
                }
            }
        })
    }

}