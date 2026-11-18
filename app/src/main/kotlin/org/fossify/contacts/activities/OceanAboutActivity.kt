package org.fossify.contacts.activities

import android.os.Bundle
import org.fossify.commons.extensions.getProperBackgroundColor
import org.fossify.commons.extensions.getProperPrimaryColor
import org.fossify.commons.extensions.getProperTextColor
import org.fossify.commons.extensions.viewBinding
import org.fossify.commons.helpers.NavigationIcon
import org.fossify.contacts.BuildConfig
import org.fossify.contacts.R
import org.fossify.contacts.databinding.ActivityAboutOceanBinding

class OceanAboutActivity : SimpleActivity() {
    private val binding by viewBinding(ActivityAboutOceanBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupEdgeToEdge(padBottomSystem = listOf(binding.aboutScrollview))
        setupMaterialScrollListener(binding.aboutScrollview, binding.aboutAppbar)

        binding.aboutVersion.text = getString(
            R.string.ocean_version_format,
            BuildConfig.VERSION_NAME,
            BuildConfig.VERSION_CODE
        )
    }

    override fun onResume() {
        super.onResume()
        setupTopAppBar(binding.aboutAppbar, NavigationIcon.Arrow)

        val textColor = getProperTextColor()
        val primaryColor = getProperPrimaryColor()
        binding.aboutRoot.setBackgroundColor(getProperBackgroundColor())
        binding.aboutAppName.setTextColor(textColor)
        binding.aboutTagline.setTextColor(primaryColor)
        binding.aboutVersion.setTextColor(textColor)
        binding.aboutDescription.setTextColor(textColor)
        binding.aboutFeaturesTitle.setTextColor(primaryColor)
        binding.aboutFeaturesBody.setTextColor(textColor)
        binding.aboutLicenseTitle.setTextColor(primaryColor)
        binding.aboutLicenseBody.setTextColor(textColor)
    }
}
