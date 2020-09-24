package com.it.partaker.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.it.partaker.R
import com.it.partaker.classes.User
import com.it.partaker.persistence.PartakerPrefs
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.nav_header_main.*

class EditProfileActivity : AppCompatActivity() {

    private var userReference : DatabaseReference? = null
    private var firebaseUser : FirebaseUser? = null
    private var bloodGroup: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val sharedPrefs = PartakerPrefs(this@EditProfileActivity)


        firebaseUser = FirebaseAuth.getInstance().currentUser
        userReference = FirebaseDatabase.getInstance().reference.child("users").child(firebaseUser?.uid.toString())

        userReference!!.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    val user = p0.getValue<User>(User::class.java)

                    etEditProfileName.setText(user!!.getFullName())
                    etEditProfilePhoneNum.setText(user.getPhoneNumber())
                    etEditProfileCity.setText(user.getCity())
                    tvEditProfileBloodGroupFB.text = user.getBloodGroup()
                }
            }
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@EditProfileActivity,"Value Event Listener Failed: ", Toast.LENGTH_LONG).show()
            }
        })

        val categories = ArrayList<String>()
        categories.add(0,"Not Known")
        categories.add(1,"A+")
        categories.add(2,"A-")
        categories.add(3,"B+")
        categories.add(4,"B-")
        categories.add(5,"AB+")
        categories.add(6,"AB-")
        categories.add(7,"O+")
        categories.add(8,"O-")

        val dataAdapter : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnEditProfileBloodGroup.adapter = dataAdapter

        spnEditProfileBloodGroup.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                bloodGroup = "Not Known"
            Toast.makeText(this@EditProfileActivity,"None Selected",Toast.LENGTH_LONG).show()
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) =
                if(parent?.getItemAtPosition(position).toString().equals("Not Known")){
                    bloodGroup = "Not Known"
                }
                else {
                    val item  = parent?.getItemAtPosition(position).toString()
                    bloodGroup = item
                }
        }
        btnEditProfileConfirm.setOnClickListener {


            val name = etEditProfileName.text.toString()
            val phone =  etEditProfilePhoneNum.text.toString()
            val city = etEditProfileCity.text.toString()
            val blood = bloodGroup

            val user = HashMap<String,Any>()
            user["fullName"] = name
            user["phoneNumber"] = phone
            user["city"] = city
            user["bloodGroup"] = blood

            sharedPrefs.saveNameUser(name)
            sharedPrefs.savePhoneUser(phone)
            sharedPrefs.saveCityUser(city)
            sharedPrefs.saveBloodUser(blood)

            userReference!!.updateChildren(user).addOnCompleteListener {
                if(it.isSuccessful){
                    val intent = Intent(this@EditProfileActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                }
                else {
                    Toast.makeText(this@EditProfileActivity,"Error: ${it.exception.toString()}",Toast.LENGTH_LONG).show()
                }
            }
        } // End Confirm Button Click Listener
    }
}