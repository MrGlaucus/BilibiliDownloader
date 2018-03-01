package com.glaucus.bilibilidownloader

import com.glaucus.bilibilidownloader.util.parseAV
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
        val addr = "https://www.bilibili.com/video/?spm_id_from=333.334.chief_recommend.17"
        val av = addr.parseAV()
        println(av)
        assertEquals("19962860", av)
    }
}
