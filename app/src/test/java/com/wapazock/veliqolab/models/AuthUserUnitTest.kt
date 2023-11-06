import com.wapazock.veliqolab.models.AuthUser
import org.junit.Test
import org.junit.Assert.*

class AuthUserUnitTest {

    @Test
    fun testConstructor() {
        // Create a new AuthUser object
        val authUser = AuthUser("email@example.com", "password")

        // Assert that the constructor set the values correctly
        assertEquals("email@example.com", authUser.getEmail())
        assertEquals("password", authUser.getPassword())
    }

    @Test
    fun testGettersAndSetters() {
        // Create a new AuthUser object
        val authUser = AuthUser("email@example.com", "password")

        // Set the email and password
        authUser.setEmail("new_email@example.com")
        authUser.setPassword("new_password")

        // Assert that the getters and setters are working correctly
        assertEquals("new_email@example.com", authUser.getEmail())
        assertEquals("new_password", authUser.getPassword())
    }

    @Test
    fun testAsJSONObject() {
        // Create a new AuthUser object
        val authUser = AuthUser("email@example.com", "password")

        // Convert the AuthUser object to JSON
        val jsonObject = authUser.asJSONObject()

        // Assert that the JSON object contains the correct values
        assertEquals("email@example.com", jsonObject.getString("email"))
        assertEquals("password", jsonObject.getString("password"))
    }
}