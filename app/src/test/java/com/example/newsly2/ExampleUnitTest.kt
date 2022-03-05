package com.example.newsly2

import com.example.newsly2.ui.login.AuthViewModel
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class PriceCalc() {
    fun finalPrice(basePrice: Double): Double {
        return basePrice * 1.5
    }
}

class ExampleUnitTest {

    val pc = PriceCalc()

    @Test
    fun `Assert product final price`() {
        val expectedPrice = 1.5
        val finalPrice = pc.finalPrice(1.0)

        assertEquals(expectedPrice, finalPrice, 0.001)
    }

}

@HiltAndroidTest
class AuthViewModelUnitTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @BindValue
    val mockMyViewModel= mockk<AuthViewModel>()

    @Before
    fun init() {
        hiltRule.inject()
    }

//    @Test
//    fun `User fetched from FirebaseAuth`() {
////        assertThat("null", mockMyViewModel.currentUser, `is`(null))
////        assertEquals(mockMyViewModel.currentUser.value, null)
//    }
}