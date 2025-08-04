package io.sadwhy.party.screen.creator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import io.sadwhy.party.data.model.Post
import io.sadwhy.party.core.theme.AppTheme
import io.sadwhy.party.legacy.utils.Serializer.getSerialized
import io.sadwhy.party.legacy.utils.Serializer.putSerialized

class CreatorFragment : Fragment() {

    companion object {
        fun getBundle(post: Post) = Bundle().apply {
            putSerialized("post", post)
        }
    }

    private val args by lazy { requireArguments() }
    private val post by lazy { args.getSerialized<Post>("post")!! }

    private lateinit var composeView: ComposeView
    private val viewModel: CreatorViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).also {
            composeView = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchCreator(post.service, post.user)

        composeView.setContent {
            val creator by viewModel.creator.collectAsState()
            AppTheme {
                CreatorScreen(
                    post,
                    creator,
                    onBackClick = {}
                )
            }
        }

    }
}