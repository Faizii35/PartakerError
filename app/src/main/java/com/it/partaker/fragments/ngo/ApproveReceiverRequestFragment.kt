package com.it.partaker.fragments.ngo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.it.partaker.ItemClickListener.MyDonationsClickListener
import com.it.partaker.R
import com.it.partaker.adapter.ApproveDonationAdapter
import com.it.partaker.models.Donation
import kotlinx.android.synthetic.main.fragment_approve_receiver_request.*


class ApproveReceiverRequestFragment : AppCompatActivity(), MyDonationsClickListener {

    private lateinit var adapter : ApproveDonationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_approve_receiver_request)

        val donRef = FirebaseDatabase.getInstance().reference.child("donations")

        val manager = LinearLayoutManager(this)
        rv_Apv_Rec_Req_NGO.layoutManager = manager
        adapter = ApproveDonationAdapter(this,this)
        rv_Apv_Rec_Req_NGO.adapter = adapter

        donRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    val donationList = mutableListOf<Donation>()

                    for (data in snapshot.children) {
                        val donation = data.getValue(Donation::class.java)
                        if (donation!!.getStatus() == "Approved" && donation.getAssigned() == "Requested") {
                            donation.let {
                                donationList.add(it)
                            }
                        }
                    }
                    donationList.reverse()
                    adapter.setDonations(donationList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ApproveReceiverRequestFragment, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })

    }

    override fun OnMyDonationsItemClickListener(view: View, donation: Donation) {
        val intent = Intent(this, ApproveReceiveRequestDetailFragment::class.java)
        intent.putExtra("Approve Receiver Request", donation)
        startActivity(intent)
    }

}