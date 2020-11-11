package app.contacts

import android.content.Context
import android.provider.ContactsContract
import java.util.*

data class Contact(val name: String, val phoneNumber: String)

fun Context.fetchAllContacts(): Set<Contact> {
    contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)
        .use { cursor ->
            if (cursor == null) return emptySet()
            val builder = TreeSet<Contact> { o1, o2 -> compareValues(o1.name, o2.name) }
            while (cursor.moveToNext()) {
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)) ?: "N/A"
                val phoneNumber =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)) ?: "N/A"


                builder.add(Contact(name, phoneNumber))
            }
            return builder
        }
}