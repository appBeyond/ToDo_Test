package app.beyond.todo_list

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.text.SimpleDateFormat
import java.util.*

class DatePickerFragment: DialogFragment(), DatePickerDialog.OnDateSetListener {

   private var listener: OnDateSetListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnDateSetListener
        if (listener == null) {
            throw ClassCastException("$context must implement OnArticleSelectedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnDateSetListener {
        fun onDateListener(dateString: String)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {


        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)


        return DatePickerDialog(requireActivity(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        val dateString = getDateString(year, month, day)
        listener?.onDateListener(dateString)
        parentFragmentManager.beginTransaction().remove(this).commit()
    }

    private fun getDateString(year: Int, month: Int, day: Int): String {
        val calender = Calendar.getInstance()
        calender.set(year, month, day)
        val dateFormat = SimpleDateFormat("yyyy/MM/dd")

        return dateFormat.format(calender.time)
    }
}