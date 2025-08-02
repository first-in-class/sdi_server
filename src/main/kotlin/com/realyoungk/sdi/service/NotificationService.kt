package com.realyoungk.sdi.service

interface NotificationService {
    fun send(destination: String, message: String)
}