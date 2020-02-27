package com.rooio.repairs

import androidx.arch.core.util.Function
import kotlin.collections.HashMap

class JsonRequest(val isTest: Boolean, val url: String, var params: HashMap<Any?, Any?>, val responseFunc: Function<Any, Void?>,
                  val errorFunc: Function<String, Void?>, val headersFlag: Boolean)