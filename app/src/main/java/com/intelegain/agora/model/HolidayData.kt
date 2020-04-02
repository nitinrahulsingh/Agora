package com.intelegain.agora.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by suraj.m on 24/7/17.
 */
class HolidayData(@field:Expose @field:SerializedName("HolidayDay") val holidayDay: String, @field:Expose @field:SerializedName("HolidayMonth") val holidayMonth: String, @field:Expose @field:SerializedName("HolidayName") val holidayName: String,
                  @field:Expose @field:SerializedName("HolidayImageUrl") val holidayImageUrl: String, @field:Expose @field:SerializedName("ImageId") val imageId: Int, @field:Expose @field:SerializedName("HolidayBgColor") val holidayBgColor: String)