package com.example.myapplication13

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat


/**
 * A simple [Fragment] subclass.
 * Use the [MySettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MySettingFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        val idPreference:EditTextPreference? = findPreference("id")
        idPreference?.title = "ID 변경"
        //idPreference?.summary = "ID를 변경할 수 있습니다"
        //idPreference?.summaryProvider = EditTextPreference.SimpleSummaryProvider.getInstance()
        idPreference?.summaryProvider = Preference.SummaryProvider<EditTextPreference> {
            preference -> 
            val text = preference.text
            if(TextUtils.isEmpty(text)){
                "ID 설정이 되지 않았습니다"
            }
            else{
                "설정된 ID는 $text 입니다"
            }
        }
        val colorPreference:ListPreference? = findPreference("color")
        colorPreference?.summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()
    }
}