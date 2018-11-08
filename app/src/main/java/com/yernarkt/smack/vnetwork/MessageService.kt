package com.yernarkt.smack.vnetwork

import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.yernarkt.smack.BaseApplication
import com.yernarkt.smack.model.Channel
import com.yernarkt.smack.model.Message
import com.yernarkt.smack.util.URL_GET_ALL_CHANNELS
import com.yernarkt.smack.util.URL_GET_ALL_MESSAGE_BY_CHANNEL_ID
import org.json.JSONException

object MessageService {
    val channels = ArrayList<Channel>()
    val messages = ArrayList<Message>()

    fun getChannels(complete: (Boolean) -> Unit) {
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

    fun getMessages(channelId: String, complete: (Boolean) -> Unit) {
        val messageRequest =
            object : JsonArrayRequest(
                Method.GET,
                "$URL_GET_ALL_MESSAGE_BY_CHANNEL_ID$channelId",
                null,
                Response.Listener { response ->
                    clearMsgs()
                    try {
                        for (x in 0 until response.length()) {
                            val message = response.getJSONObject(x)

                            val msgBody = message.getString("messageBody")
                            val newChannelId = message.getString("channelId")
                            val id = message.getString("_id")
                            val userName = message.getString("userName")
                            val userAvatar = message.getString("userAvatar")
                            val userAvatarColor = message.getString("userAvatarColor")
                            val timeStamp = message.getString("timeStamp")

                            val newMessage =
                                Message(msgBody, userName, newChannelId, userAvatar, userAvatarColor, id, timeStamp)
                            this.messages.add(newMessage)
                        }
                        complete(true)
                    } catch (e: JSONException) {
                        Log.d("JSON", "EXC: ${e.localizedMessage}")
                        complete(false)
                    }
                },
                Response.ErrorListener { error ->
                    Log.d("ERROR", "Could not find messages $error")
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

        BaseApplication.prefs.requestQueue.add(messageRequest)
    }

    fun clearMsgs(){
        messages.clear()
    }

    fun clearChannels(){
        channels.clear()
    }
}