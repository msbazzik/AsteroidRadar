package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        val adapter = MainRecyclerViewAdapter(
            (MainRecyclerViewAdapter.OnClickListener {
                findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
            })
        )
        binding.asteroidRecycler.adapter = adapter

        viewModel.asteroidList.observe(viewLifecycleOwner, Observer {
            it.let {
                adapter.data = it
            }
        })

        viewModel.status.observe(viewLifecycleOwner, Observer {
            when (it) {
                MainApiStatus.LOADING -> {
                    binding.statusLoadingWheel.visibility = View.VISIBLE
                }
                MainApiStatus.ERROR -> {
                    binding.statusLoadingWheel.visibility = View.GONE
                    Snackbar.make(
                        requireActivity().findViewById(android.R.id.content),
                        getString(R.string.error_message, viewModel.errorMessage),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
                MainApiStatus.DONE -> {
                    binding.statusLoadingWheel.visibility = View.GONE
                }
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
