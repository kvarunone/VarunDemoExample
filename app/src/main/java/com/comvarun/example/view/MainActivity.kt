package com.comvarun.example.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.comvarun.example.databinding.ActivityMainBinding

import com.comvarun.example.viewmodel.ListViewModel

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: ListViewModel
    private val userListAdapter = UserListAdapter(arrayListOf())
    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        viewModel.refresh()

        binding.usersList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userListAdapter
        }

        observeViewModel()
    }
    fun observeViewModel() {
        viewModel.users.observe(this, Observer {countries ->
            countries?.let {
                usersList.visibility = View.VISIBLE
                userListAdapter.updateCountries(it) }
        })

        viewModel.usersLoadError.observe(this, Observer { isError ->
            listError.visibility = if(isError == "") View.GONE else View.VISIBLE
        })

        viewModel.loading.observe(this, Observer { isLoading ->
            isLoading?.let {
                loadingView.visibility = if(it) View.VISIBLE else View.GONE
                if(it) {
                    listError.visibility = View.GONE
                    usersList.visibility = View.GONE
                }
            }
        })
    }
}
