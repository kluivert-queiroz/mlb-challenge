package com.util

import kotlin.math.floor

class CPF {
    companion object {
        private fun randomDigit(): Int {
            return (Math.random() * 9).toInt()
        }

        private fun modByEleven(dividend: Int): Int {
            return Math.round(dividend - (floor((dividend / 11).toDouble()) * 11)).toInt()
        }

        fun generate(): String {
            val n1: Int = randomDigit()
            val n2: Int = randomDigit()
            val n3: Int = randomDigit()
            val n4: Int = randomDigit()
            val n5: Int = randomDigit()
            val n6: Int = randomDigit()
            val n7: Int = randomDigit()
            val n8: Int = randomDigit()
            val n9: Int = randomDigit()
            var d1 = n9 * 2 + n8 * 3 + n7 * 4 + n6 * 5 + n5 * 6 + n4 * 7 + n3 * 8 + n2 * 9 + n1 * 10

            d1 = 11 - (modByEleven(d1))

            if (d1 >= 10) d1 = 0

            var d2 = d1 * 2 + n9 * 3 + n8 * 4 + n7 * 5 + n6 * 6 + n5 * 7 + n4 * 8 + n3 * 9 + n2 * 10 + n1 * 11

            d2 = 11 - (modByEleven(d2))

            if (d2 >= 10) d2 = 0
            return "" + n1 + n2 + n3 + n4 + n5 + n6 + n7 + n8 + n9 + d1 + d2
        }
    }
}