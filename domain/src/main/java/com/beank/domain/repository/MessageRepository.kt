package com.beank.domain.repository

interface MessageRepository {
    fun insertToken()

    fun newToken(token : String)

    fun subscribeNotice()

    fun unsubscribeNotice()

}