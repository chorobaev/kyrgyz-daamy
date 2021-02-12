package com.timelysoft.amore.bottomsheet.paytype

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.timelysoft.amore.R
import com.timelysoft.amore.service.AppPreferences
import kotlinx.android.synthetic.main.fragment_pay_type.*

class PayTypeBottomSheet(
    private val listener: PayTypeListener
) : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_pay_type, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pay_type_1.setOnClickListener {
            listener.onClickPay(1)
            dismiss()
        }

        pay_type_3.setOnClickListener {
            listener.onClickPay(2)
        }
        if (!AppPreferences.bankPay) {
            pay_type_2.visibility = View.GONE
            layoutCards.visibility =View.GONE
            textViewOnlinePay.visibility = View.GONE
            demirBankOnlinePayment.visibility = View.GONE
        }

        pay_type_2.setOnClickListener {
            listener.onClickPay(3)
            dismiss()
        }

    }
}