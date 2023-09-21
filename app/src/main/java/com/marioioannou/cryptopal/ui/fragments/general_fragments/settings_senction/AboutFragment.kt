package com.marioioannou.cryptopal.ui.fragments.general_fragments.settings_senction

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.marioioannou.cryptopal.R
import com.marioioannou.cryptopal.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            cvBack.setOnClickListener {
                findNavController().popBackStack()
            }
            imgCoinstats.setOnClickListener {
                val url: Uri = Uri.parse("https://coinstats.app/")
                val intent = Intent(Intent.ACTION_VIEW, url)
                startActivity(intent)
            }
            imgCoingecko.setOnClickListener {
                val url: Uri = Uri.parse("https://www.coingecko.com/en/all-cryptocurrencies")
                val intent = Intent(Intent.ACTION_VIEW, url)
                startActivity(intent)
            }
            aboutLinkedin.setOnClickListener{
                val url: Uri = Uri.parse("https://www.linkedin.com/in/marios-ioannou-8326b8174")
                val intent = Intent(Intent.ACTION_VIEW, url)
                startActivity(intent)

            }
            aboutGithub.setOnClickListener {
                val url: Uri = Uri.parse("https://github.com/MarioIoannou")
                val intent = Intent(Intent.ACTION_VIEW, url)
                startActivity(intent)
            }
        }
    }

}