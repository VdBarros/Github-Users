package com.vinib.githubusers

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.vinib.githubusers.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class UserListingTest {

    @get:Rule(order = 0)
    var hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
    }

    @OptIn(DelicateCoroutinesApi::class)
    @Test
    fun testFirstNavigationScreenIsUserListingScreen() {
        GlobalScope.launch {
            composeTestRule.awaitIdle()
            composeTestRule.onNodeWithText("Github Users").assertExists()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    @Test
    fun testFirstGithubUserClickNavigatesToUserDetailsScreen() {
        GlobalScope.launch {
            composeTestRule.awaitIdle()
            composeTestRule.onNodeWithText("defunkt").performClick()
            composeTestRule.awaitIdle()
            composeTestRule.onNodeWithText("Repositórios").assertExists()
        }
    }
}