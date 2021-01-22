package com.youtube.sorcjc.billetero.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.youtube.sorcjc.billetero.Global
import com.youtube.sorcjc.billetero.R
import com.youtube.sorcjc.billetero.io.MyApiAdapter
import com.youtube.sorcjc.billetero.model.Ticket
import com.youtube.sorcjc.billetero.model.User
import kotlinx.android.synthetic.main.activity_ticket.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TicketActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket)

        val bundle = intent.extras

        bundle?.getString("ticket_id")?.let {
            fetchTicketInfo(it)
        }
    }

    private fun fetchTicketInfo(ticketId: String) {
        val authHeader = User.getAuthHeader(this)

        MyApiAdapter.getApiService().getTicket(authHeader, ticketId).enqueue(object : Callback<Ticket> {
            override fun onResponse(call: Call<Ticket>, response: Response<Ticket>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        displayTicket(it)
                    }
                } else {
                    showErrorMessage()
                }
            }

            override fun onFailure(call: Call<Ticket>, t: Throwable) {
                showErrorMessage()
            }
        })
    }

    private fun displayTicket(ticket: Ticket) {
        tvTicketId.text = ticket.id.toString()
    }

    private fun showErrorMessage() {
        Global.showMessageDialog(this, "Error inesperado", "No se pudo cargar la información del Ticket")
    }
}