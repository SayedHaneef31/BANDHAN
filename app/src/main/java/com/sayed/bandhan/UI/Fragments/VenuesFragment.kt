package com.sayed.bandhan.UI.Fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sayed.bandhan.Data.Venue
import com.sayed.bandhan.R
import com.sayed.bandhan.utils.VenuesAdapter

class VenuesFragment : Fragment() {

    private lateinit var originalVenueList: List<Venue>
    private lateinit var adapter: VenuesAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_venues, container, false)
        originalVenueList = listOf(
            Venue(R.drawable.venn, "₹10k - ₹30k", "The Grand Ballroom", "Mumbai, India", "100-200 guests"),
            Venue(R.drawable.ven2, "₹25k - ₹45k", "Skyline Terrace", "Delhi, India", "250-280 guests"),
            Venue(R.drawable.ven22, "₹18k - ₹30k", "Palace Gardens", "Jaipur, India", "200-500 guests"),
            Venue(R.drawable.ven1, "₹8k - ₹15k", "Lotus Courtyard", "Lucknow, India", "50-100 guests"),
            Venue(R.drawable.ven3, "₹40k - ₹70k", "Oceanview Pavilion", "Goa, India", "150-300 guests"),
            Venue(R.drawable.ven34, "₹12k - ₹25k", "Maple Banquet Hall", "Chandigarh, India", "80-150 guests"),
            Venue(R.drawable.ven89, "₹60k - ₹1L", "Royal Heritage Fort", "Udaipur, India", "400-800 guests"),
            Venue(R.drawable.ven67, "₹20k - ₹35k", "Skyline Deck", "Bangalore, India", "100-250 guests"),
            Venue(R.drawable.ven89, "₹5k - ₹12k", "Green Leaf Lawn", "Nagpur, India", "30-80 guests"),
            Venue(R.drawable.ven34, "₹25k - ₹50k", "Amber Heights", "Hyderabad, India", "200-400 guests"),
            Venue(R.drawable.ven1, "₹15k - ₹28k", "The Orchid Room", "Kolkata, India", "120-220 guests"),
            Venue(R.drawable.ven2, "₹70k - ₹1.2L", "Celestial Dome", "New Delhi, India", "500-1000 guests")
        )

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerVenues)
        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = VenuesAdapter(originalVenueList.toMutableList())
        recyclerView.adapter = adapter

        setupSearchBar(view)

        return view
    }

    private fun setupSearchBar(view: View) {
        val etSearch: EditText = view.findViewById(R.id.etSearch)

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterVenues(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

    }
    private fun filterVenues(query: String) {
        val filteredList = if (query.isEmpty()) {
            originalVenueList
        } else {
            originalVenueList.filter { venue ->
                venue.venueName.contains(query, ignoreCase = true) || venue.venueLocation.contains(query, ignoreCase = true)
            }
        }

        adapter.submitList(filteredList)

    }



}