package uz.mobiledv.hr_desktop.utils

import kotlinx.datetime.*
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.char

object DateUtils {
    val datetimeInUtc: LocalDateTime
        get() = Clock.System.now().toLocalDateTime(TimeZone.UTC)

    val dateInUtc: LocalDate
        get() = Clock.System.todayIn(TimeZone.UTC)

    val firstDayOfCurrentMonth: LocalDate
        get() {
            val today = dateInUtc
            return LocalDate(today.year, today.month, 1)
        }

    fun formatDate(date: LocalDate): String {
        return date.format(LocalDate.Format {
            year()
            char('-')
            monthNumber()
            char('-')
            dayOfMonth()
        })
    }

    fun formatDateTime(dateTime: LocalDateTime): String {
        return dateTime.format(LocalDateTime.Format {
            date(LocalDate.Format {
                year()
                char('-')
                monthNumber()
                char('-')
                dayOfMonth()
            })
            char(' ')
            time(LocalTime.Format {
                hour()
                char(':')
                minute()
            })
        })
    }

    fun parseDate(dateString: String): LocalDate? {
        return try {
            LocalDate.parse(dateString)
        } catch (e: Exception) {
            null
        }
    }

    fun parseDateTime(dateTimeString: String): LocalDateTime? {
        return try {
            LocalDateTime.parse(dateTimeString)
        } catch (e: Exception) {
            null
        }
    }
}