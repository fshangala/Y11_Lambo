package com.fshangala.y11

data class AutomationObject(val event_type:String, val event:String) {
    override fun toString(): String {
        return "{\"event_type\":\"$event_type\",\"event\":\"$event\",\"args\":[],\"kwargs\":{}}"
    }
}