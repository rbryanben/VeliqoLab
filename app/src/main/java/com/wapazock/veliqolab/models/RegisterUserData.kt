import com.wapazock.veliqolab.models.AuthUser
import org.json.JSONObject

class RegisterUserData(
    firstName: String? = null,
    lastName: String? = null,
    mobile: String? = null,
    email: String? = null,
    password: String? = null
) {

    private var firstName: String = firstName ?: ""
    private var lastName: String = lastName ?: ""
    private var mobile: String = mobile ?: ""
    private var email: String = email ?: ""
    private var password: String = password ?: ""

    // Getters
    fun getFirstName(): String {
        return firstName
    }

    fun getLastName(): String {
        return lastName
    }

    fun getMobile(): String {
        return mobile
    }

    fun getEmail(): String {
        return email
    }

    fun getPassword(): String {
        return password
    }

    // Setters
    fun setFirstName(firstName: String) {
        this.firstName = firstName
    }

    fun setLastName(lastName: String) {
        this.lastName = lastName
    }

    fun setMobile(mobile: String) {
        this.mobile = mobile
    }

    fun setEmail(email: String) {
        this.email = email
    }

    fun setPassword(password: String) {
        this.password = password
    }

    fun toJSONObject(): JSONObject {
        val jsonObject = JSONObject()

        jsonObject.put("firstName", firstName)
        jsonObject.put("lastName", lastName)
        jsonObject.put("phoneNumber", mobile)
        jsonObject.put("email", email)
        jsonObject.put("password", password)
        jsonObject.put("middleNames","This for now")

        return jsonObject
    }
}
