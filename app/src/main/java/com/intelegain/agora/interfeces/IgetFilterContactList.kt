package com.intelegain.agora.interfeces

import com.intelegain.agora.model.SearchContactsDetails
import java.util.*

/**
 * Created by meenal on 23/9/16.
 */
interface IgetFilterContactList {
    fun getFilterContactList(filterArrayList: ArrayList<SearchContactsDetails?>?): ArrayList<SearchContactsDetails?>?
}