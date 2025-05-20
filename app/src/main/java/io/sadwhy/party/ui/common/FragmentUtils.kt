package io.sadwhy.party.ui.common

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import io.sadwhy.party.R

object FragmentUtils {
    inline fun <reified T : Fragment> Fragment.openFragment(
        view: View? = null, bundle: Bundle? = null,
    ) {
        parentFragmentManager.commit {
            add<T>(id, args = bundle)
            hide(this@openFragment)
            //addToBackStack(null)
        }
    }

    inline fun <reified T : Fragment> FragmentActivity.openFragment(
        view: View? = null, bundle: Bundle? = null, cont: Int = R.id.nav_host_fragment
    ) {
        val oldFragment = supportFragmentManager.findFragmentById(cont)!!
        oldFragment.openFragment<T>(view, bundle)
    }

    inline fun <reified F : Fragment> Fragment.addIfNull(
        id: Int, tag: String, args: Bundle? = null
    ) {
        childFragmentManager.run {
            if (findFragmentByTag(tag) == null) commit {
                add<F>(id, tag, args)
            }
        }
    }
}
