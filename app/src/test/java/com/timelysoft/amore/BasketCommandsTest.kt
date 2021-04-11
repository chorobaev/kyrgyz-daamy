package com.timelysoft.amore

import com.timelysoft.amore.extension.countPriceOfMod
import com.timelysoft.amore.service.response.BaseModifier
import com.timelysoft.amore.service.response.BaseModifierGroup
import org.junit.Test
import org.junit.Assert.*



internal class BasketCommandsTest{

    @Test
    fun checkForRightWorking(){

        val tasks = hashMapOf<Int,BaseModifier>(
            1 to BaseModifier(0,"Сырный соус",145,10,2,false,3),
            2 to BaseModifier(1,"Чесночный соус",144,10,2,false,3),
        )
        val baseModifierGroup = BaseModifierGroup(1,"147","",6,2,2,
            true, arrayListOf(),tasks)

        val result = countPriceOfMod(baseModifierGroup)

        assertEquals(40,result)
    }

}
