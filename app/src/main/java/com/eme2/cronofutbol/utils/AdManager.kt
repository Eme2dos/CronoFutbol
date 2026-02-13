package com.eme2.cronofutbol.utils

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

object AdManager {
    // Variable mágica: Si es true, desaparecen los anuncios de toda la app
    var isPremium by mutableStateOf(false)

    private var interstitialAd: InterstitialAd? = null

    // ID de prueba de Google para Interstitial
    private const val AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"

    fun loadInterstitial(context: Context) {
        if (isPremium) return

        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(context, AD_UNIT_ID, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(ad: InterstitialAd) {
                interstitialAd = ad
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                interstitialAd = null
            }
        })
    }

    // Muestra el anuncio y ejecuta una acción (ej: cambiar de pantalla) cuando se cierra
    fun showInterstitial(activity: Activity, onAdFinished: () -> Unit) {
        if (isPremium || interstitialAd == null) {
            // Si es premium o el anuncio no cargó, simplemente vamos a la siguiente pantalla
            onAdFinished()
            return
        }

        interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                interstitialAd = null
                loadInterstitial(activity) // Precargar el siguiente
                onAdFinished() // Navegar
            }

            override fun onAdFailedToShowFullScreenContent(e: AdError) {
                interstitialAd = null
                onAdFinished() // Navegar aunque falle
            }
        }
        interstitialAd?.show(activity)
    }
}