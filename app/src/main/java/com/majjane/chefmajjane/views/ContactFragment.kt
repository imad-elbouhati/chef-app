package com.majjane.chefmajjane.views

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.majjane.chefmajjane.R
import com.majjane.chefmajjane.utils.Constants.Companion.ADMIN_EMAIL
import com.majjane.chefmajjane.utils.Constants.Companion.ADMIN_PHONE
import com.majjane.chefmajjane.utils.snackbar
import com.majjane.chefmajjane.views.activities.HomeActivity


class ContactFragment : Fragment(R.layout.fragment_contact), View.OnClickListener {

    override fun onResume() {
        super.onResume()
        ((activity) as HomeActivity).apply {
            setToolbar(getString(R.string.contact))
            setToolbarHeight(170)

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sendEmailRow = view.findViewById<LinearLayout>(R.id.envoyerEmail)
        val makeCallRow = view.findViewById<LinearLayout>(R.id.appeler)
        sendEmailRow.setOnClickListener(this)
        makeCallRow.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.envoyerEmail -> {
                sendEmail()
            }

            R.id.appeler -> {
                makeACall()
            }
        }

    }

    private fun makeACall() {
        val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", ADMIN_PHONE, null))
        startActivity(intent)
    }

    private fun sendEmail() {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("mailto:$ADMIN_EMAIL"))
            startActivity(Intent.createChooser(intent, getString(R.string.send_email)))
        } catch (e: ActivityNotFoundException) {
            requireView().snackbar(getString(R.string.no_email_client))
        }
    }
}