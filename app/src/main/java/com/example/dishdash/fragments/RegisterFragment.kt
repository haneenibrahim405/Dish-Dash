package com.example.dishdash.fragments


import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.dishdash.activity.AuthActivity
import com.example.dishdash.R

class RegisterFragment : Fragment() {

    interface RegisterListener {
        fun onRegister(name: String, email: String, password: String)
    }

    private var listener: RegisterListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is RegisterListener) {
            listener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val nameField = view.findViewById<EditText>(R.id.et_name)
        val emailField = view.findViewById<EditText>(R.id.et_email)
        val passwordField = view.findViewById<EditText>(R.id.et_password)
        val signUpButton = view.findViewById<Button>(R.id.btn_signup)
        val loginLink = view.findViewById<TextView>(R.id.tv_login_link)

        signUpButton.setOnClickListener {
            val name = nameField.text.toString().trim()
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                listener?.onRegister(name, email, password)
            } else {
                Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
            }
        }

        loginLink.setOnClickListener {
            (activity as? AuthActivity)?.navigateToLogin()
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
