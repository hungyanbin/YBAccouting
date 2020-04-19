package com.yanbin.ybaccouting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.yanbin.ybaccouting.data.AccountingDatabase
import com.yanbin.ybaccouting.data.TransactionModel
import com.yanbin.ybaccouting.utils.isNotNullOrEmpty
import kotlinx.android.synthetic.main.dialog_new_transaction.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.lang.RuntimeException

class AddTransactionDialog : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, android.R.style.Theme_Material_NoActionBar_Fullscreen)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_new_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnDone.setOnClickListener {
            if (isNameNotEmpty() && isAmountNotEmpty()) {
                val name = editName.text!!.toString()
                //TODO partial function here
                val amount = editAmount.text!!.toString().toInt()
                val transferMode = when(radioTransferMode.checkedRadioButtonId) {
                    R.id.radioDeposit -> "Deposit"
                    R.id.radioWithdraw -> "WithDraw"
                    else -> throw RuntimeException("Illegale button!!")
                }

                val transferModel = TransactionModel().apply {
                    this.name = name
                    this.deposit = if(transferMode == "Deposit") amount else 0
                    this.withDraw = if(transferMode == "WithDraw") amount else 0
                }

                MainScope().launch {
                    insertTransaction(transferModel)
                    dismiss()
                }
            }
        }
    }

    private suspend fun insertTransaction(transactionModel: TransactionModel) {
        val db = AccountingDatabase.getDatabase(this.context!!)
        val transactionDao = db.getTransactionDao()
        transactionDao.addTransaction(transactionModel)
    }

    private fun isNameNotEmpty(): Boolean {
        return editName.text?.toString().isNotNullOrEmpty()
    }

    private fun isAmountNotEmpty(): Boolean {
        return editAmount.text?.toString().isNotNullOrEmpty()
    }
}