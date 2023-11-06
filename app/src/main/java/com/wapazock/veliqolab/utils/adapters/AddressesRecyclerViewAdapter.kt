package com.wapazock.veliqolab.utils.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wapazock.veliqolab.R
import com.wapazock.veliqolab.models.Address
import com.wapazock.veliqolab.utils.interfaces.AddressRecyclerInterface

class AddressesRecyclerViewAdapter :
    RecyclerView.Adapter<AddressesRecyclerViewAdapter.ViewHolder>() {

    //Variables
    private var ADDRESSES: ArrayList<Address> = ArrayList()
    private lateinit var addressRecyclerInterface: AddressRecyclerInterface

    //Set address
    public fun setAddresses(addresses: ArrayList<Address>): AddressesRecyclerViewAdapter {
        ADDRESSES = addresses
        return this
    }

    // Set callback
    public fun setAddressRecyclerInterface(addressRecyclerInterface: AddressRecyclerInterface){
        this.addressRecyclerInterface = addressRecyclerInterface
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_address,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get the address
        val address = ADDRESSES[position]
        // Set city and country
        holder.cityContryText.text = "${address.city}, ${address.country}"
        holder.streetAddress.text = address.addressLine1
        // Set on click listener on root layout
        holder.rootLayout.setOnClickListener{
            addressRecyclerInterface.onAddressClicked(address)
        }
    }

    override fun getItemCount(): Int {
        return ADDRESSES.size
    }

    // View Holder
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // View Variables
        var rootLayout : RelativeLayout
        var cityContryText: TextView
        var streetAddress: TextView

        // Constructor
        init {
            // Set view variables
            rootLayout = view.findViewById(R.id.itemAddressRootLayout)
            cityContryText = view.findViewById(R.id.itemAddressCityCountryTextView)
            streetAddress = view.findViewById(R.id.itemAddressStreetAddress)
        }

    }
}