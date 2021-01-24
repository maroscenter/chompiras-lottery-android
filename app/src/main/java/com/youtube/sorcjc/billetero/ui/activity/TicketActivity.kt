package com.youtube.sorcjc.billetero.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.youtube.sorcjc.billetero.Global
import com.youtube.sorcjc.billetero.R
import com.youtube.sorcjc.billetero.io.MyApiAdapter
import com.youtube.sorcjc.billetero.io.response.TicketResponse
import com.youtube.sorcjc.billetero.model.Ticket
import com.youtube.sorcjc.billetero.model.TicketPlay
import com.youtube.sorcjc.billetero.model.User
import com.youtube.sorcjc.billetero.ui.adapter.TicketPlayAdapter
import kotlinx.android.synthetic.main.activity_ticket.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TicketActivity : AppCompatActivity() {

    private lateinit var mAdapter: TicketPlayAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket)

        val bundle = intent.extras

        bundle?.getString("ticket_id")?.let {
            fetchTicketInfo(it)
        }

        mAdapter = TicketPlayAdapter()
    }

    private fun setupPDFLink(ticketId: Int) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://mrm-lotery.com/ticket/${ticketId}/pdf"))
        startActivity(browserIntent)
    }

    private fun fetchTicketInfo(ticketId: String) {
        val authHeader = User.getAuthHeader(this)

        MyApiAdapter.getApiService().getTicket(authHeader, ticketId).enqueue(object : Callback<TicketResponse> {
            override fun onResponse(call: Call<TicketResponse>, response: Response<TicketResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        displayTicket(it.ticket)
                        displayPlays(it.plays)
                    }
                } else {
                    showErrorMessage()
                }
            }

            override fun onFailure(call: Call<TicketResponse>, t: Throwable) {
                showErrorMessage()
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun displayTicket(ticket: Ticket) {
        title = "Ticket #${ticket.id}"

        tvWinnerId.text = ticket.id.toString()
        tvTicketCreatedAt.text = ticket.createdAt
        tvTicketTotal.text = "$ ${ticket.totalPoints}"

        setupPDFLink(ticket.id)
    }

    private fun displayPlays(plays: ArrayList<TicketPlay>) {
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        recyclerView.adapter = mAdapter
        mAdapter.setDataSet(plays)
    }

    private fun showErrorMessage() {
        Global.showMessageDialog(this, "Error inesperado", "No se pudo cargar la información del Ticket")
    }
}