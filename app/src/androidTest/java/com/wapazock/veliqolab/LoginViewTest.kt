package com.wapazock.veliqolab

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.wapazock.veliqolab.custom.CustomEmailEditText
import com.wapazock.veliqolab.views.login.loginActivity
import org.junit.Rule
import org.junit.Test

class LoginViewTest {

    @Rule
    val activityLoginRule = ActivityScenarioRule(loginActivity::class.java)

    @Test
    fun testUsernameInput(){}
}