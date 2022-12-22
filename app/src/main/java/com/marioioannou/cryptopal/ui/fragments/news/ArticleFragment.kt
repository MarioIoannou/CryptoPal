package com.marioioannou.cryptopal.ui.fragments.news

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.marioioannou.cryptopal.R
import com.marioioannou.cryptopal.databinding.FragmentArticleBinding
import com.marioioannou.cryptopal.databinding.FragmentCoinDetailBinding
import com.marioioannou.cryptopal.ui.fragments.coin_detail.CoinDetailFragmentArgs

class ArticleFragment : Fragment() {

    private lateinit var binding: FragmentArticleBinding

    private val args: ArticleFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cvBack.setOnClickListener {
            findNavController().popBackStack()
        }

        val source = args.article.source
        val link = args.article.link

        binding.author.text = source.toString()
        binding.articleWebview.loadUrl(link.toString())
    }
}