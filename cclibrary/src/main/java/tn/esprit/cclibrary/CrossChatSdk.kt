package tn.esprit.cclibrary

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class Conversation(
    val _id: String,
    val participants: List<String>,
    val messages: List<Message>
)

data class Message(
    val _id: String,
    val sender: Sender,
    val content: String
)

data class User(
    val id: Int,
    val name: String,
    val imageResourceId: Int // Resource ID for the user's image
)

data class Sender(
    val name: String
)

interface ConversationService {
    @GET("conversation/{currentUser}")
    fun getConversations(@retrofit2.http.Path("currentUser") currentUser: String): Call<List<Conversation>>
}

class CrossChatSdk(private val context: Context, private val users: List<User>, private val currentUser: String) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ContactAdapter


    fun initRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        // Check if the adapter has been initialized before using it
        if (!::adapter.isInitialized) {
            adapter = ContactAdapter(context, emptyList(),currentUser) // Initialize with an empty list
        }
        recyclerView.adapter = adapter
    }


    fun fetchConversations() {
        // Initialize Retrofit and make network call
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:9090/") // Ensure the URL ends with "/"
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ConversationService::class.java)
        service.getConversations(currentUser).enqueue(object : Callback<List<Conversation>> {

            override fun onResponse(
                call: Call<List<Conversation>>,
                response: Response<List<Conversation>>
            ) {
                if (response.isSuccessful) {
                    val conversations = response.body()
                    conversations?.let {
                        Log.d("CrossChatSdk", "Number of conversations received: ${conversations.size}")
                        displayConversations(it)
                    }
                    Log.d("CrossChatSdk", "Response: $response")
                } else {
                    Log.e("CrossChatSdk", "Failed to fetch data: ${response.code()}")
                    Toast.makeText(context, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Conversation>>, t: Throwable) {
                Log.e("CrossChatSdk", "Network Error: ${t.message}")
                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayConversations(conversations: List<Conversation>) {
        adapter = ContactAdapter(context, conversations, currentUser)
        recyclerView.adapter = adapter
    }
}