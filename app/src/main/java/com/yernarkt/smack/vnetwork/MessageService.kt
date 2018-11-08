package com.yernarkt.smack.vnetwork

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.yernarkt.smack.BaseApplication
import com.yernarkt.smack.model.Channel
import com.yernarkt.smack.util.URL_GET_ALL_CHANNELS
import org.json.JSONException

object MessageService {
    val channels = ArrayList<Channel>()

    fun getChannels(context: Context, complete: (Boolean) -> Unit) {
        val channelRequest =
            object : JsonArrayRequest(Method.GET, URL_GET_ALL_CHANNELS, null, Response.Listener { response ->
                try {
                    for (
                    x in 0 until response.length()
                    ) {
                        val channel = response.getJSONObject(x)
                        val name = channel.getString("name")
                        val desc = channel.getString("description")
                        val id = channel.getString("_id")

                        val newChannel = Channel(name, desc, id)
                        this.channels.add(newChannel)
                    }
                    complete(true)
                } catch (e: JSONException) {
                    Log.d("JSON", "EXC: ${e.localizedMessage}")
                    complete(false)
                }
            }, Response.ErrorListener { error ->
                Log.d("ERROR", "Could not find channels $error")
                complete(false)
            }) {
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }

                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers.put("Authorization", "Bearer ${BaseApplication.prefs.authToken}")
                    return headers
                }
            }

        BaseApplication.prefs.requestQueue.add(channelRequest)
    }
}