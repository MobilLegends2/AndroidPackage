package tn.esprit.ccsdk

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val sam = User(5, "Sam", R.drawable.sam)
val steven = User(7, "Steven", R.drawable.steven)
val olivia = User(4, "Olivia", R.drawable.olivia)
val john = User(3, "John", R.drawable.john)
val greg = User(1, "Greg", R.drawable.greg)


val users: List<User> = listOf(sam, steven, olivia, john, greg)

const val currentUser= "participant2"


class MainActivity : AppCompatActivity() {
    private lateinit var crossChatSdk: CrossChatSdk
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize users
        val sam = User(5, "Sam", R.drawable.sam)
        val steven = User(7, "Steven", R.drawable.steven)
        val olivia = User(4, "Olivia", R.drawable.olivia)
        val john = User(3, "John", R.drawable.john)
        val greg = User(1, "Greg", R.drawable.greg)

        val users: List<User> = listOf(sam, steven, olivia, john, greg)

        // Initialize CrossChatSdk
        crossChatSdk = CrossChatSdk(this, users, currentUser)
        // Fetch conversations with the current user ID
        crossChatSdk.fetchConversations()

        // Assuming you have a RecyclerView in your layout with id "recyclerView"
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        crossChatSdk.initRecyclerView(recyclerView)
    }

}
