package com.example.retrohub.extensions

import java.util.*


fun Calendar.toFormattedString() = "${get(Calendar.DAY_OF_MONTH)}/${get(Calendar.MONTH)}/${get(Calendar.YEAR)}"