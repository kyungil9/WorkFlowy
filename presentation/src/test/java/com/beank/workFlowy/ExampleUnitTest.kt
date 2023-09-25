package com.beank.workFlowy

import com.beank.workFlowy.utils.toWeekString
import org.junit.Test

import org.junit.Assert.*
import java.time.LocalDate

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        println(LocalDate.of(2023,9,24).toWeekString())
    }

    @Test
    fun test(){
        println()
    }
}