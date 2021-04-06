package com.timelysoft.amore.bottomsheet.paytype

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.timelysoft.amore.R
import com.timelysoft.amore.databinding.FragmentPayTypeBinding
import com.timelysoft.amore.service.AppPreferences
import org.koin.android.ext.android.bind

class PayTypeBottomSheet(
    private val listener: PayTypeListener
) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentPayTypeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPayTypeBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.payType1.setOnClickListener {
            listener.onClickPay(1)
            dismiss()
        }

        binding.payType3.setOnClickListener {
            listener.onClickPay(2)
        }
        if (!AppPreferences.bankPay) {
            binding.payType2.visibility = View.GONE
            binding.layoutCards.visibility =View.GONE
            binding.textViewOnlinePay.visibility = View.GONE
            binding.demirBankOnlinePayment.visibility = View.GONE
        }

        binding.payType2.setOnClickListener {
            listener.onClickPay(3)
            dismiss()
        }

    }
}