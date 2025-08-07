package com.example.dishdash.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.dishdash.activity.AuthActivity
import com.example.dishdash.R


class LoginFragment : Fragment() {

    interface LoginListener {
        fun onLogin(email: String, password: String)
    }

    private var listener: LoginListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LoginListener) {
            listener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val emailField = view.findViewById<EditText>(R.id.et_email)
        val passwordField = view.findViewById<EditText>(R.id.et_password)
        val loginButton = view.findViewById<Button>(R.id.btn_login)
        val registerLink = view.findViewById<TextView>(R.id.tv_register_link)

        loginButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                listener?.onLogin(email, password)
            } else {
                Toast.makeText(requireContext(), "Enter both email and password", Toast.LENGTH_SHORT).show()
            }
        }

        registerLink.setOnClickListener {
            (activity as? AuthActivity)?.navigateToRegister()
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}

