package com.example.favdish.view.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.favdish.R
import com.example.favdish.application.FavDishApplication
import com.example.favdish.databinding.FragmentAllDishesBinding
import com.example.favdish.view.activities.AddUpdateDishActivity
import com.example.favdish.view.adapters.FavDishAdapter
import com.example.favdish.viewmodel.FavDishViewModel
import com.example.favdish.viewmodel.FavDishViewModelFactory
import com.example.favdish.viewmodel.HomeViewModel

class AllDishesFragment : Fragment() {

    private lateinit var mBinding: FragmentAllDishesBinding

    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentAllDishesBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.rvDishesList.layoutManager = GridLayoutManager(requireActivity(), 2)
        val favDishAdapter = FavDishAdapter(this@AllDishesFragment)

        mBinding.rvDishesList.adapter = favDishAdapter

        mFavDishViewModel.allDishesList.observe(viewLifecycleOwner, { dishes ->
            dishes.let {
                for (item in it) {
                    if (it.isNotEmpty()) {
                        mBinding.rvDishesList.visibility = View.VISIBLE
                        mBinding.tvNoDishesYet.visibility = View.GONE

                        favDishAdapter.dishesList(it)
                    } else {
                        mBinding.rvDishesList.visibility = View.GONE
                        mBinding.tvNoDishesYet.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_all_dishes, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_dish -> {
                startActivity(Intent(requireActivity(), AddUpdateDishActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}