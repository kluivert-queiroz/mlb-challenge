package com.core

import java.time.LocalDate

class Date {
    companion object {
        private var now: LocalDate = LocalDate.now()
        fun now(): LocalDate {
            return this.now
        }

        fun setNow(date: LocalDate) {
            this.now = date
        }
    }
}