package com.hillbeater.bookxpert.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hillbeater.bookxpert.R
import com.hillbeater.bookxpert.adapter.PhoneDataAdapter
import com.hillbeater.bookxpert.database.AppDatabase
import com.hillbeater.bookxpert.database.PhoneDataItem
import com.hillbeater.bookxpert.database.PhoneDatabase
import com.hillbeater.bookxpert.databinding.FragmentApiDataBinding
import com.hillbeater.bookxpert.viewModel.ApiDataFragmentViewModel
import com.hillbeater.bookxpert.viewModel.ApiDataViewModelFactory

class ApiDataFragment : Fragment() {

    private var _binding: FragmentApiDataBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ApiDataFragmentViewModel
    private lateinit var phoneDatabase: PhoneDatabase
    private lateinit var userDatabase: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentApiDataBinding.inflate(inflater, container, false)
        phoneDatabase = PhoneDatabase.getDatabase(requireContext())
        userDatabase = AppDatabase.getDatabase(requireContext())

        viewModel = ViewModelProvider(this, ApiDataViewModelFactory(phoneDatabase, userDatabase))[ApiDataFragmentViewModel::class.java]

        observeViewModel()

        //Start fetching data from API
        viewModel.fetchDataFromApi()

        return binding.root
    }

    private fun observeViewModel() {
        viewModel.loading.observe(viewLifecycleOwner) { showLoading(it) }

        viewModel.toastMessage.observe(viewLifecycleOwner) {
            it?.let { showToast(it) }
        }

        viewModel.phoneData.observe(viewLifecycleOwner) { phoneList ->
            binding.recyclerViewPhoneData.visibility = View.VISIBLE
            binding.recyclerViewPhoneData.adapter = PhoneDataAdapter(
                phoneList,
                onUpdateClick = { phoneItem ->

                    val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.edit_dialog, null)

                    val etName = dialogView.findViewById<EditText>(R.id.etName)
                    val etColor = dialogView.findViewById<EditText>(R.id.etColor)
                    val etCapacity = dialogView.findViewById<EditText>(R.id.etCapacity)
                    val etPrice = dialogView.findViewById<EditText>(R.id.etPrice)
                    val etGeneration = dialogView.findViewById<EditText>(R.id.etGeneration)
                    val etYear = dialogView.findViewById<EditText>(R.id.etYear)
                    val etCpuModel = dialogView.findViewById<EditText>(R.id.etCpuModel)
                    val etHardDiskSize = dialogView.findViewById<EditText>(R.id.etHardDiskSize)
                    val etStrapColour = dialogView.findViewById<EditText>(R.id.etStrapColour)
                    val etCaseSize = dialogView.findViewById<EditText>(R.id.etCaseSize)
                    val etScreenSize = dialogView.findViewById<EditText>(R.id.etScreenSize)
                    val etDescription = dialogView.findViewById<EditText>(R.id.etDescription)

                    //OLD data
                    with(phoneItem) {
                        etName.setText(name)
                        etColor.setText(color)
                        etCapacity.setText(capacity)
                        etPrice.setText(price)
                        etGeneration.setText(generation)
                        etYear.setText(year?.toString())
                        etCpuModel.setText(cpuModel)
                        etHardDiskSize.setText(hardDiskSize)
                        etStrapColour.setText(strapColour)
                        etCaseSize.setText(caseSize)
                        etScreenSize.setText(screenSize?.toString())
                        etDescription.setText(description)
                    }

                    val dialog = AlertDialog.Builder(requireContext())
                        .setTitle("Edit Phone Details")
                        .setView(dialogView)
                        .setPositiveButton("Update", null) // Null for now to customize later
                        .setNegativeButton("Cancel", null)
                        .create()

                    dialog.setOnShowListener {
                        val updateButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        val cancelButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        updateButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.googleBlue))
                        cancelButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))

                        updateButton.setOnClickListener {
                            val updatedItem = phoneItem.copy(
                                name = etName.text.toString(),
                                color = etColor.text.toString(),
                                capacity = etCapacity.text.toString(),
                                price = etPrice.text.toString(),
                                generation = etGeneration.text.toString(),
                                year = etYear.text.toString().toIntOrNull(),
                                cpuModel = etCpuModel.text.toString(),
                                hardDiskSize = etHardDiskSize.text.toString(),
                                strapColour = etStrapColour.text.toString(),
                                caseSize = etCaseSize.text.toString(),
                                screenSize = etScreenSize.text.toString().toDoubleOrNull(),
                                description = etDescription.text.toString()
                            )

                            viewModel.updatePhoneItem(updatedItem)
                            dialog.dismiss()
                        }
                    }

                    dialog.show()
                },
                onDeleteClick = { phoneItem ->
                    viewModel.deletePhone(phoneItem)
                }
            )
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(show: Boolean) {
        binding.loadingLayout.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
