package com.smitcoderx.volunteerconnect.Utils

import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.mappls.sdk.maps.Mappls
import com.mappls.sdk.services.account.MapplsAccountManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class VolunteerConnectApplication: MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        MapplsAccountManager.getInstance().restAPIKey = "ed6f2f40d476c2f466f934e604f1a04d"
        MapplsAccountManager.getInstance().mapSDKKey = "ed6f2f40d476c2f466f934e604f1a04d"
        MapplsAccountManager.getInstance().atlasClientId = "96dHZVzsAuty-iEPnRqgz8gu7gPTNuLLum-3Dr7ul1cS6vlMlCsnS6LIKIq3RNVTU32nTSa4qH6GbOgdSn_ZggyVyVJnK-Sq"
        MapplsAccountManager.getInstance().atlasClientSecret = "lrFxI-iSEg-uM9Ds0v350kEgZ859_exzJ4KiTmYnCEnaNXsg1nmWGSVdD8ogzuH70UZmgfwinOcP8YfmL_Iy-BgqBYSbYtQjvjkcvlT0ZIM="
        Mappls.getInstance(applicationContext)
    }
}