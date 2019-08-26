package com.contactapp.contact

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.contactapp.R
import com.contactapp.contact.model.Contact

class ContactListAdapter(contactListAdapterListener: ContactListAdapterListener) :
    RecyclerView.Adapter<ContactListAdapter.ViewHolder>() {

    private var contactListAdapterListener = contactListAdapterListener
    var contactList = listOf<Contact>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_contact_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(holder: ContactListAdapter.ViewHolder, position: Int) {
        val contact = contactList[position]
        holder.bind(contact, position)
        holder.itemView.setOnClickListener {
            contactListAdapterListener.onItemClick(position)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.name_txt_view)
        val numberTextView: TextView = itemView.findViewById(R.id.number_txt_view)

        fun bind(contact: Contact, position: Int) {
            nameTextView.text = (position + 1).toString()+" : "+contact.name
            numberTextView.text = contact.number
        }
    }

    interface ContactListAdapterListener {
        fun onItemClick(position: Int)
    }
}
