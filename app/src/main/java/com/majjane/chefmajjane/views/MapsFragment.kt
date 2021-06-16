package com.majjane.chefmajjane.views

import android.annotation.SuppressLint
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import com.majjane.chefmajjane.R
import com.majjane.chefmajjane.responses.Address
import com.majjane.chefmajjane.utils.GoogleMapsUtils
import com.majjane.chefmajjane.viewmodel.SharedViewModel
import com.majjane.chefmajjane.views.activities.HomeActivity
import java.util.*


class MapsFragment : Fragment() {
    override fun onResume() {
        super.onResume()
        ((activity) as HomeActivity).setHeaderVisibility(false)
    }

    var map: GoogleMap? = null
    private val googleMapsUtils: GoogleMapsUtils = GoogleMapsUtils()
    private lateinit var navController: NavController
    private lateinit var sharedViewModel: SharedViewModel

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
            map.clear()
            val rabat = LatLng(33.966304, -6.8549541)
            val rabatMarker = map.addMarker(MarkerOptions().position(rabat).title(""))
            rabatMarker?.apply {
                setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_map))
                isDraggable = true
            }
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(rabat, 12F))
            map.mapType = GoogleMap.MAP_TYPE_NORMAL
            map.uiSettings.apply {
                isZoomControlsEnabled = true
            }
            setMapStyle(map)

            //SET MARKER TO CENTER OF SCREEN
            val polygon = map.addPolygon(
                PolygonOptions().apply {
                    add(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15,p16,p17,p18,p19,p20,p21,p22,p23,p24,p25,p26)
                        .strokeColor(Color.TRANSPARENT)

                }
            )


            map.setOnCameraMoveListener {
                rabatMarker?.let {
                    it.position = googleMap.cameraPosition.target
                    val isContains = PolyUtil.containsLocation(
                        LatLng(it.position.latitude, it.position.longitude),
                        polygon.points,
                        true
                    )
                    val drawable =
                        if (isContains) R.drawable.marker_map else R.drawable.map_marker_undifuned
                    rabatMarker.setIcon(BitmapDescriptorFactory.fromResource(drawable))

                }

            }

            map.setMinZoomPreference(10F)

            view?.findViewById<Button>(R.id.chooseLocationBtn)?.setOnClickListener {
                rabatMarker?.let {
                    val geocoder = Geocoder(requireContext(), Locale.getDefault())
                    val addresses = geocoder.getFromLocation(
                        rabatMarker.position.latitude,
                        rabatMarker.position.longitude,
                        1
                    )
                    val address: String? = addresses[0].getAddressLine(0)
                    val city: String? = addresses[0].locality
                    val state: String? = addresses[0].adminArea
                    val country: String? = addresses[0].countryName
                    val myAddress = Address(city, state, country, address)
                    sharedViewModel.address.value = myAddress
                    findNavController().popBackStack()
                }
            }


        }
    }

    private fun setMapStyle(googleMap: GoogleMap) {
        try {
            val success = map?.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireContext(),
                    R.raw.style
                )
            )
            if (!success!!) {
                Log.d(TAG, "setMapStyle: Error Map Style")
            }
        } catch (e: Exception) {
            Log.d(TAG, "setMapStyle: ${e.toString()}")
        }

    }

    private val TAG = "MapsFragment"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        }
    }

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
        navController = Navigation.findNavController(view)
    }


    val p0 = LatLng(33.830538628702556, -7.124905907903728  )
    val p1 = LatLng(33.770895898322344, -6.784246314083907  )
    val p2 = LatLng(33.76877056180024, -6.7795684568741486  )
    val p3 = LatLng(33.807327372348475, -6.739502223037827  )
    val p4 = LatLng(33.83710937716799, -6.752154717933507   )
    val p5 = LatLng(33.85112084883843, -6.761644089105267   )
    val p6 = LatLng(33.88176293137465, -6.7721878348516675  )
    val p7 = LatLng(33.89314147482519, -6.769024711127748   )
    val p8 = LatLng(33.934266620170405, -6.752154717933507  )
    val p9 = LatLng(33.946512826678735, -6.744774095911027  )
    val p10 = LatLng(33.96312986049791, -6.733175975589987   )
    val p11 = LatLng(33.98061796879199, -6.722632229843587   )
    val p12 = LatLng(34.028691716739075, -6.650934758768066  )
    val p13 = LatLng(34.00334713188005, -6.695218490902946   )
    val p14 = LatLng(34.01907914423934, -6.732121601015347   )
    val p15 = LatLng(34.042671696621646, -6.736339099313907  )
    val p16 = LatLng(34.05664937239112, -6.760589714530627   )
    val p17 = LatLng(34.08896392018733, -6.754263467082787   )
    val p18 = LatLng(34.10118783143923, -6.778514082299509   )
    val p19 = LatLng(34.107322163323, -6.783126217853086     )
    val p20 = LatLng(34.04640616272087, -6.834638468078731   )
    val p21 = LatLng(33.98518906163812, -6.9016664539789465  )
    val p22 = LatLng(33.951475105143345, -6.937973280945213  )
    val p23 = LatLng(33.928562578953205, -6.968694439784666  )
    val p24 = LatLng(33.88967441628073, -7.025482038531696   )
    val p25 = LatLng(33.85360332057257, -7.101198840068186   )
    val p26 = LatLng(33.8414903212148, -7.1123701714424215   )


}