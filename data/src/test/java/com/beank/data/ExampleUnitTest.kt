package com.beank.data

import com.beank.data.entity.WeekRecord
import com.beank.data.mapper.modelCasting
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        println(modelCasting(WeekRecord()))
    }
}