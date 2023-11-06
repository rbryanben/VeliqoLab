package com.wapazock.veliqolab

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.wapazock.veliqolab.models.Address
import com.wapazock.veliqolab.repository.UserDataRepository
import com.wapazock.veliqolab.utils.Share.Share
import com.wapazock.veliqolab.utils.adapters.AddressesRecyclerViewAdapter
import com.wapazock.veliqolab.utils.errors.ServerError
import com.wapazock.veliqolab.utils.interfaces.AddressRecyclerInterface
import com.wapazock.veliqolab.utils.interfaces.GetUserDataInterface
import com.wapazock.veliqolab.utils.notifications.ClassicNotifications
import com.wapazock.veliqolab.utils.notifications.NotificationTypes
import com.wapazock.veliqolab.views.addresses.ViewAddress
import com.wapazock.veliqolab.views.login.loginActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), GetUserDataInterface, AddressRecyclerInterface {

    // View Variables
    private lateinit var customToolBarLayout : RelativeLayout
    private lateinit var collapsingToolBar: CollapsingToolbarLayout
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var addressesRecyclerView: RecyclerView
    private lateinit var fullName: TextView
    private lateinit var email : TextView
    private lateinit var mobile: TextView

    // Variables
    private lateinit var addressesRecyclerViewAdapter: AddressesRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set view variables
        customToolBarLayout = this.findViewById(R.id.activityMainCustomToolbarRelativeLayout)
        collapsingToolBar = this.findViewById(R.id.activityMainCollapsingToolbarLayout)
        appBarLayout = this.findViewById(R.id.activityMainAppBarLayout)
        addressesRecyclerView = this.findViewById(R.id.activityMainAddressesRecyclerView)
        fullName = this.findViewById(R.id.mainActivityMainFullNameTextView)
        email = this.findViewById(R.id.activityMainEmailTextView)
        mobile = this.findViewById(R.id.mainActivityMobileNumberTextView)

        // Get User Data
        UserDataRepository.getInstance().getUserData(this)

        // Get Passed Messages
        val passedMessage = this.intent.getStringExtra("toast")
        if (passedMessage != null){
            ClassicNotifications.alertNotification(this,passedMessage, NotificationTypes.SUCCESS)
        }

        // Bind collapsing tool bar
        bindCollapsingToolBar()
        // Bind recycler view
        bindRecyclerView()
    }

    // Method bind the recycler view to the addresses
    private fun bindRecyclerView() {
        // Create an adapter
        addressesRecyclerViewAdapter = AddressesRecyclerViewAdapter()
        addressesRecyclerViewAdapter.setAddressRecyclerInterface(this)
        addressesRecyclerView.adapter = addressesRecyclerViewAdapter
        addressesRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    // Android does not support this. This will make the custom layout visible when toolbar is collapsed
    private fun bindCollapsingToolBar() {
        appBarLayout.addOnOffsetChangedListener(object: OnOffsetChangedListener{
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                // Collapsed
                if (Math.abs(verticalOffset)- appBarLayout!!.totalScrollRange == 0){
                    customToolBarLayout.visibility = VISIBLE
                    return
                }
                customToolBarLayout.visibility = GONE
            }
        })
    }

    // When user data is received
    override fun onGetUserDataRequestResult(
        userData: com.wapazock.veliqolab.models.UserData?,
        wasSuccessful: Boolean,
        error: ServerError?
    ) {
        runOnUiThread{
            // Success
            if (wasSuccessful){
                // Set attributes
                email.text = userData?.getEmail();
                fullName.text = "${userData!!.getFirstName()} ${userData!!.getLastName()}"
                mobile.text = userData.getPhoneNumber()
                return@runOnUiThread
            }

            // Network Error
            if (error == ServerError.SERVER_UNREACHABLE){
                // Notify network error and retry after 3 seconds
                ClassicNotifications.alertNotification(baseContext,"We faced a network challenge. Retrying",NotificationTypes.NETWORK_ERROR)
                // Run after 3 seconds
                val runnable = Runnable {
                    UserDataRepository.getInstance().getUserData(this)
                }

                // Sleep the runnable for 3 seconds
                GlobalScope.launch {
                    delay(3000)
                    runnable.run()
                }

                return@runOnUiThread
            }

            // Invalid token
            if (error == ServerError.INVALID_AUTH_TOKEN){
                // Login
                val intent = Intent(baseContext,loginActivity::class.java)
                startActivity(intent)
                finish()
                return@runOnUiThread
            }

            // Unhandled
            ClassicNotifications.alertNotification(baseContext,"Something went wrong",NotificationTypes.SOMETHING_WENT_WRONG)
        }

    }

    // When addresses from user data are received
    override fun onGetUserDataRequestResultAddresses(addresses: ArrayList<Address>) {
        runOnUiThread {
            addressesRecyclerViewAdapter.setAddresses(addresses)
            addressesRecyclerViewAdapter.notifyDataSetChanged()
        }
    }

    // When address has been clicked view it
    override fun onAddressClicked(address: Address) {
        val intent = Intent(this,ViewAddress::class.java)
        intent.putExtra("address",address)
        startActivity(intent)
        return
    }

    //On resume check any shared events
    override fun onResume() {
        super.onResume()
        // Get the event from share
        val event = Share.getInstance().get("MainActivity")
        // If event is not null perform action
        if (event != null){
            // Since there is 1 event just refresh
            UserDataRepository.getInstance().getUserData(this);
        }
    }
}