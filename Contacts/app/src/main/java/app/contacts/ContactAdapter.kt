package app.contacts

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView

class ContactAdapter(
    private val context: Context,
    contacts: Set<Contact>
) :
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {
    private val contacts = contacts.toList()

    class ContactViewHolder(
        root: View,
        val name: Button = root.findViewById(R.id.name),
        val phone: Button = root.findViewById(R.id.phone),
        val sms: ImageButton = root.findViewById(R.id.sms)
    ) :
        RecyclerView.ViewHolder(root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.contact_list, parent, false)
        )
    }

    override fun getItemCount() = contacts.size

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val current = contacts[position]
        val defaultMessage = "Hello, ${current.name}!\n"
        with(holder) {
            name.text = current.name
            phone.text = current.phoneNumber
            name.setOnClickListener {
                context.startActivity(
                    Intent(
                        Intent.ACTION_DIAL,
                        Uri.parse("tel:${current.phoneNumber}")
                    )
                )
            }
            phone.setOnClickListener {
                context.startActivity(
                    Intent(
                        Intent.ACTION_DIAL,
                        Uri.parse("tel:${current.phoneNumber}")
                    )
                )
            }
            sms.setOnClickListener {
                val sms = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:${current.phoneNumber}"))
                sms.putExtra("sms_body", defaultMessage)
                context.startActivity(sms)
            }
        }
    }

}