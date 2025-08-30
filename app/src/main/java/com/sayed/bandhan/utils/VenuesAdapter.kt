package com.sayed.bandhan.utils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sayed.bandhan.Data.Venue
import com.sayed.bandhan.R

class VenuesAdapter(private val venues: MutableList<Venue>) :
    RecyclerView.Adapter<VenuesAdapter.VenueViewHolder>(){

    class VenueViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val venueImage: ImageView = view.findViewById(R.id.ivVenueImage)
        val priceRange: TextView = view.findViewById(R.id.tvPriceRange)
        val venueName: TextView = view.findViewById(R.id.tvVenueName)
        val venueLocation: TextView = view.findViewById(R.id.tvVenueLocation)
        val guestRange: TextView = view.findViewById(R.id.tvGuestRange)
        val shortlistButton: Button = view.findViewById(R.id.btnShortlist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VenueViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_venue, parent, false)
        return VenueViewHolder(view)
    }

    override fun onBindViewHolder(holder: VenueViewHolder, position: Int) {
        val venue = venues[position]
        holder.venueImage.setImageResource(venue.imageResId)
        holder.priceRange.text = venue.priceRange
        holder.venueName.text = venue.venueName
        holder.venueLocation.text = venue.venueLocation
        holder.guestRange.text = venue.guestRange

        holder.shortlistButton.setOnClickListener {
            // TODO --- Handle karlo click
        }
    }

    fun submitList(newVenues: List<Venue>) {
        venues.clear()
        venues.addAll(newVenues)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = venues.size
}