package com.wapazock.veliqolab.views.addresses

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.doOnTextChanged
import com.wapazock.veliqolab.R
import com.wapazock.veliqolab.custom.CustomButtonWhite
import com.wapazock.veliqolab.models.Address
import com.wapazock.veliqolab.repository.AddressesRepository
import com.wapazock.veliqolab.utils.Share.Share
import com.wapazock.veliqolab.utils.errors.ServerError
import com.wapazock.veliqolab.utils.interfaces.AddressManagerInterface
import com.wapazock.veliqolab.utils.notifications.ClassicNotifications
import com.wapazock.veliqolab.utils.notifications.NotificationTypes
import com.wapazock.veliqolab.utils.validators.AddressValidator
import com.wapazock.veliqolab.views.login.loginActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

class ViewAddress : AppCompatActivity(), AddressManagerInterface {

    // View Variables
    private lateinit var address1EditText: EditText
    private lateinit var address2EditText: EditText
    private lateinit var cityEditText: EditText
    private lateinit var postalCodeEditText: EditText
    private lateinit var provinceEditText: EditText
    private lateinit var countryEditText: EditText
    private lateinit var updateButton: CustomButtonWhite
    private lateinit var cancelButton: ImageView
    private lateinit var deleteAddressButton : ImageView

    // Variables
    private final lateinit var addressConst : Address // Two for comparing any changes
    private lateinit var addressMutable : Address
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_address)

        // Get address
        addressConst = (intent.getSerializableExtra("address") as? Address)!!

        // Used to detect if there is an update to the address object
        addressMutable = addressConst.copy()

        // Set view variables
        address1EditText = this.findViewById(R.id.viewAddressStreet1)
        address2EditText = this.findViewById(R.id.viewAddressStreet2)
        cityEditText = this.findViewById(R.id.viewAddressCity)
        postalCodeEditText = this.findViewById(R.id.viewAddressPostalCode)
        provinceEditText = this.findViewById(R.id.viewAddressProvince)
        countryEditText = this.findViewById(R.id.viewAddressCountry)
        updateButton = this.findViewById(R.id.viewAddressUpdateButton)
        cancelButton = this.findViewById(R.id.viewAddressCancelImageView)
        deleteAddressButton = this.findViewById(R.id.viewAddressDeleteAddress)

        // Check country code
        countryEditText.setText(Locale("",addressConst.country).displayCountry.toString())


        // Watch cancel clicked
        cancelButton.setOnClickListener {
            finish()
        }

        // Set View Variables
        address1EditText.setText(addressConst.addressLine1)
        address2EditText.setText(addressConst.addressLine2)
        cityEditText.setText(addressConst.city)
        postalCodeEditText.setText(addressConst.postalCode)
        provinceEditText.setText(addressConst.province)
        countryEditText.setText(addressConst.country)

        // Bind Address 1
        bindAddress1()
        // Bind Address 2
        bindAddress2()
        // Bind City
        bindCity()
        // Bind Postal Code
        bindPostalCode()
        // Bind Province
        bindProvince()
        // Bind Country
        bindCountry()
        // Bind update button
        bindUpdateButton()
        // Bind delete button
        bindDeleteButton()
    }

    // Delete the address
    private fun bindDeleteButton() {
        deleteAddressButton.setOnClickListener {
            ClassicNotifications.alertNotification(baseContext,"Deleting viewing address",NotificationTypes.INVALID_INPUT)
            AddressesRepository.getInstance()?.deleteAddress(addressMutable,this)
        }
    }

    // Update the address
    private fun bindUpdateButton() {
        // Update
        updateButton.setOnClickListener{
            if (AddressValidator.isValid(addressMutable)){

                // Start loading
                updateButton.setLoading(true)
                // Update
                AddressesRepository.getInstance()?.updateAddressRequest(addressMutable,this)
            }
        }
    }

    // Validates the address object, if there is a change and object is valid. Enable the update button
    private fun validate(){
        if (AddressValidator.isValid(addressMutable) && !addressMutable.equals(addressConst)){
            updateButton.enable()
            return
        }
        updateButton.disable()
    }

    // On text changed update address object and validate
    private fun bindCountry() {
        countryEditText.doOnTextChanged { text, start, before, count ->
            addressMutable.country = text.toString()
            validate()
        }
    }

    // On text changed update address object and validate
    private fun bindProvince() {
        provinceEditText.doOnTextChanged { text, start, before, count ->
            addressMutable.province = text.toString()
            validate()
        }
    }

    // On text changed update address object and validate
    private fun bindPostalCode() {
        postalCodeEditText.doOnTextChanged { text, start, before, count ->
            addressMutable.postalCode = text.toString()
            validate()
        }
    }

    // On text changed update address object and validate
    private fun bindCity() {
        cityEditText.doOnTextChanged { text, start, before, count ->
            addressMutable.city = text.toString()
            validate()
        }
    }

    // On text changed update address object and validate
    private fun bindAddress2() {
        address2EditText.doOnTextChanged { text, start, before, count ->
            addressMutable.addressLine2 = text.toString()
            validate()
        }
    }


    // On text changed update address object and validate
    private fun bindAddress1() {
        address1EditText.doOnTextChanged { text, start, before, count ->
            addressMutable.addressLine1 = text.toString()
            validate()
        }
    }

    override fun onAddressUpdateResult(wasSuccessful: Boolean, error: ServerError?) {
        runOnUiThread{
            // Stop loading
            updateButton.setLoading(false)

            // 200
            if (wasSuccessful){
                ClassicNotifications.alertNotification(baseContext,"Address Updated Successfully.",NotificationTypes.SUCCESS,dark = true)
                Share.getInstance().set("MainActivity","deleted-address")
                return@runOnUiThread
            }

            // 403 go back to login
            if (error == ServerError.INVALID_AUTH_TOKEN){
                ClassicNotifications.alertNotification(baseContext,"Your session seems to have expired",NotificationTypes.INVALID_INPUT, dark = true)

                // Run after 3 seconds
                val runnable = Runnable {
                    val intent = Intent(baseContext,loginActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                // Sleep the runnable for 3 seconds
                GlobalScope.launch {
                    delay(3000)
                    runnable.run()
                }
                return@runOnUiThread
            }

            // Network problem
            if (error == ServerError.SERVER_UNREACHABLE){
                ClassicNotifications.alertNotification(baseContext,"We faced a network challenge on updating",NotificationTypes.NETWORK_ERROR,dark = true)
                return@runOnUiThread
            }

            ClassicNotifications.alertNotification(baseContext,"Something went terribly wrong",NotificationTypes.SOMETHING_WENT_WRONG,dark = true)
        }
    }

    override fun onDeleteAddressResult(wasSuccessful: Boolean, error: ServerError?) {
        runOnUiThread{
            // Successful
            if (wasSuccessful){
                // Pass back message
                Share.getInstance().set("MainActivity","deleted-address")
                // Address deleted
                finish()
            }

            // 403 go back to login
            if (error == ServerError.INVALID_AUTH_TOKEN){
                ClassicNotifications.alertNotification(baseContext,"Your session seems to have expired",NotificationTypes.INVALID_INPUT, dark = true)

                // Run after 3 seconds
                val runnable = Runnable {
                    val intent = Intent(baseContext,loginActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                // Sleep the runnable for 3 seconds
                GlobalScope.launch {
                    delay(3000)
                    runnable.run()
                }
                return@runOnUiThread
            }


            // Network problem
            if (error == ServerError.SERVER_UNREACHABLE){
                ClassicNotifications.alertNotification(baseContext,"We faced a network challenge on updating",NotificationTypes.NETWORK_ERROR,dark = true)
                return@runOnUiThread
            }
            // Something went wrong
            ClassicNotifications.alertNotification(baseContext,"Something went terribly wrong",NotificationTypes.SOMETHING_WENT_WRONG,dark = true)
        }
    }
}