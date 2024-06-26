package tn.esprit.cclibrary

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tn.esprit.cclibrary.databinding.ChatitemBinding

class ContactAdapter(private val context: Context, private val conversations: List<Conversation>, private val currentUser: String) :
    RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ChatitemBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(conversation: Conversation) {
            val lastMessage = conversation.messages.lastOrNull()
            val participants = conversation.participants
            val otherParticipantId = participants.firstOrNull {
                it != currentUser
            }
            val senderName = otherParticipantId ?: "Unknown"
            binding.senderName.text = senderName
            binding.messageContent.text = lastMessage?.content ?: ""
        }

        override fun onClick(view: View) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val conversation = conversations[position]
                val conversationId = conversation._id
                val intent = Intent(context, MessengerActivity::class.java).apply {
                    putExtra("conversationId", conversationId)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ChatitemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(conversations[position])
    }

    override fun getItemCount(): Int {
        return conversations.size
    }
}
