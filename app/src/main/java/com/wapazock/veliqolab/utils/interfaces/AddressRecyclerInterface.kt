package com.wapazock.veliqolab.utils.interfaces

import com.wapazock.veliqolab.models.Address

interface AddressRecyclerInterface {
    // When an address is clicked callback
    fun onAddressClicked(address: Address);
}