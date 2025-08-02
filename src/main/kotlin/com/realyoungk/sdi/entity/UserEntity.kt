package com.realyoungk.sdi.entity

import com.realyoungk.sdi.enums.CalendarType
import com.realyoungk.sdi.enums.LunarMonthType
import jakarta.persistence.*
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "users")
open class UserEntity protected constructor() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(nullable = false, unique = true, updatable = false)
    var publicId: String? = null
        protected set

    @Column(nullable = false, length = 50)
    var name: String? = null
        protected set

    @Column(length = 20)
    var phoneNumber: String? = null
        protected set

    var birthday: LocalDate? = null
        protected set

    @Enumerated(EnumType.STRING)
    var calendarType: CalendarType? = null
        protected set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var lunarMonthType: LunarMonthType = LunarMonthType.NORMAL
        protected set

    @Column(length = 100)
    var teamName: String? = null
        protected set

    @PrePersist
    protected fun onCreate() {
        if (publicId == null) {
            publicId = UUID.randomUUID().toString()
        }
    }

    companion object {
        @JvmStatic
        fun builder(): UserEntityBuilder = UserEntityBuilder()
    }

    class UserEntityBuilder {
        private var name: String? = null
        private var phoneNumber: String? = null
        private var birthday: LocalDate? = null
        private var calendarType: CalendarType? = null
        private var lunarMonthType: LunarMonthType? = null
        private var teamName: String? = null

        fun name(name: String) = apply { this.name = name }
        fun phoneNumber(phoneNumber: String?) = apply { this.phoneNumber = phoneNumber }
        fun birthday(birthday: LocalDate?) = apply { this.birthday = birthday }
        fun calendarType(calendarType: CalendarType?) = apply { this.calendarType = calendarType }
        fun lunarMonthType(lunarMonthType: LunarMonthType?) = apply {
            this.lunarMonthType = lunarMonthType
        }

        fun teamName(teamName: String?) = apply { this.teamName = teamName }

        fun build(): UserEntity {
            val entity = UserEntity()
            entity.name = name
            entity.phoneNumber = phoneNumber
            entity.birthday = birthday
            entity.calendarType = calendarType
            entity.lunarMonthType = if (calendarType == CalendarType.SOLAR) {
                LunarMonthType.NORMAL
            } else {
                lunarMonthType ?: LunarMonthType.NORMAL
            }
            entity.teamName = teamName
            return entity
        }
    }
}