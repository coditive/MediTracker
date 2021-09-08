package com.syrous.meditracker.view.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.syrous.meditracker.databinding.ItemMedicineLayoutBinding
import com.syrous.meditracker.model.Medicine

class MedicineAdapter(
    private val clickHandler: FragmentDashboard.ClickHandler
    ): ListAdapter<Medicine, MedicineAdapter.MedicineVH>(CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineVH {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemMedicineLayoutBinding.inflate(layoutInflater, parent, false)
        return MedicineVH(binding)
    }

    override fun onBindViewHolder(holder: MedicineVH, position: Int) {
        holder.bind(getItem(position), clickHandler)
    }

    companion object{
        val CALLBACK = object: DiffUtil.ItemCallback<Medicine>() {
            override fun areItemsTheSame(oldItem: Medicine, newItem: Medicine): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Medicine, newItem: Medicine): Boolean {
                return oldItem.createdAt == newItem.createdAt
            }
        }
    }

    class MedicineVH(
        private val binding: ItemMedicineLayoutBinding
        ): RecyclerView.ViewHolder(binding.root) {

            fun bind(medicine: Medicine, clickHandler: FragmentDashboard.ClickHandler) {
                binding.medicineAmount.text = medicine.amount.toString()
                Glide.with(binding.transactionIconView).load(medicine.imageLocation).circleCrop().into(binding.transactionIconView)
                binding.medicineName.text = medicine.medicineName
                binding.directionOfUse.text = medicine.directionToUse
                binding.timeToTake.text = "${medicine.hours} : ${medicine.minutes}"

                binding.root.setOnClickListener {
                    clickHandler.onClick(medicine.id)
                }
                binding.deleteMedicine.setOnClickListener {
                    clickHandler.deleteItem(medicine.id)
                }
            }
    }
}
