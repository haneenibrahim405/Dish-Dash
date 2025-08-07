package com.example.dishdash.fragments

import android.content.Intent
import android.os.*
import android.view.*
import android.view.animation.*
import androidx.fragment.app.Fragment
import com.example.dishdash.activity.AuthActivity
import com.example.dishdash.R

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_splash, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        animateDots(view)

        Handler(Looper.getMainLooper()).postDelayed({
            if (!isAdded) return@postDelayed
        }, 2000)
    }
    private fun animateDots(view: View) {
        val dots = listOf(R.id.dot1, R.id.dot2, R.id.dot3).map { view.findViewById<View>(it) }

        val anim = AlphaAnimation(1f, 0.3f).apply {
            duration = 500
            repeatMode = Animation.REVERSE
            repeatCount = Animation.INFINITE
        }

        dots.forEachIndexed { i, dot ->
            dot.postDelayed({ dot.startAnimation(anim) }, i * 200L)
        }
    }
}
