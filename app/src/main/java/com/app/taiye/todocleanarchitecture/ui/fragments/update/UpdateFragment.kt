package com.app.taiye.todocleanarchitecture.ui.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.taiye.todocleanarchitecture.R
import com.app.taiye.todocleanarchitecture.data.model.ToDoData
import com.app.taiye.todocleanarchitecture.databinding.FragmentUpdateBinding
import com.app.taiye.todocleanarchitecture.ui.viewmodel.SharedViewModel
import com.app.taiye.todocleanarchitecture.ui.viewmodel.ToDoViewModel
import kotlinx.android.synthetic.main.fragment_update.*

class UpdateFragment : Fragment() {
    private val args by navArgs<UpdateFragmentArgs>()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val toDoViewModel: ToDoViewModel by viewModels()


    private var _binding : FragmentUpdateBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //data binding
        _binding = FragmentUpdateBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = this
        binding.args = args

        setHasOptionsMenu(true)

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_save -> updateItems()
            R.id.menu_delete -> confirmItemRemoval()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun updateItems() {
        val title = current_title_et.text.toString()
        val description = current_description_et.text.toString()
        val getPriority = current_priority_spinner.selectedItem.toString()


        val validation = sharedViewModel.verifyDataFromUser(title, description)
        if (validation) {
            //update note data item
            val updatedItem = ToDoData(
                args.currentitem.id,
                title,
                sharedViewModel.parsePriority(getPriority),
                description
            )
            toDoViewModel.updateData(updatedItem)
            Toast.makeText(requireContext(), "Successfully Updated", Toast.LENGTH_LONG).show()
            ///Navigate back
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_LONG).show()
        }
    }


    private fun confirmItemRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            toDoViewModel.deleteItem(args.currentitem)
            Toast.makeText(requireContext(), "Successfully Removed '${args.currentitem.title}'?", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete '${args.currentitem.title}'?")
        builder.setMessage("Are you sure you want to remove '${args.currentitem.title}'?")
        builder.create().show()

    }
}
