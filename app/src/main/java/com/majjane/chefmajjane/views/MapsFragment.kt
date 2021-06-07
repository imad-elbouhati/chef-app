package com.majjane.chefmajjane.views

import android.annotation.SuppressLint
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.majjane.chefmajjane.R
import com.majjane.chefmajjane.utils.GoogleMapsUtils
import com.majjane.chefmajjane.views.activities.HomeActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


class MapsFragment : Fragment() {
    override fun onResume() {
        super.onResume()
        ((activity) as HomeActivity).setHeaderVisibility(false)
    }

    var map: GoogleMap? = null
    private val googleMapsUtils: GoogleMapsUtils = GoogleMapsUtils()

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        map = googleMap
        map?.let { map ->
            val rabat = LatLng(33.966304, -6.8549541)
            var rabatMarker =
                map.addMarker(MarkerOptions().position(rabat).title("Marker in Rabat"))
            rabatMarker?.apply {
                setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_map))
                isDraggable = true
            }
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(rabat, 10F))
            map.mapType = GoogleMap.MAP_TYPE_NORMAL
            map.uiSettings.apply {
                isZoomControlsEnabled = true
            }

            lifecycleScope.launch {
                delay(1000L)
                map.animateCamera(
                    CameraUpdateFactory.newLatLngBounds(
                        googleMapsUtils.rabatBoundaries,
                        0
                    ), 2000, null
                )
                map.setLatLngBoundsForCameraTarget(googleMapsUtils.rabatBoundaries)
            }

            map.setOnCameraMoveListener {
                rabatMarker?.let {
                    it.position = googleMap.cameraPosition.target
                    Log.d(TAG, "${it.position.latitude} ${it.position.longitude}")
                }
            }
            view?.findViewById<Button>(R.id.chooseLocationBtn)?.setOnClickListener {
                rabatMarker?.let {
                    val geocoder = Geocoder(requireContext(), Locale.getDefault())
                    val addresses = geocoder.getFromLocation(
                        rabatMarker.position.latitude,
                        rabatMarker.position.longitude,
                        1
                    )
                    val address: String = addresses[0].getAddressLine(0)
                    val city: String = addresses[0].locality
                    val state: String = addresses[0].adminArea
                    val country: String = addresses[0].countryName
                    Toast.makeText(requireContext(), "$address", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private val TAG = "MapsFragment"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

    }


}