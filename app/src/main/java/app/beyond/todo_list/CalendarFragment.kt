package app.beyond.todo_list

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class CalendarFragment: DialogFragment(), DatePickerDialog.OnDateSetListener {

    private var listener: OnCalendarListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnCalendarListener
        if (listener == null) {
            throw ClassCastException("$context must implement OnArticleSelectedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface OnCalendarListener {
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val calender = Calendar.getInstance()
        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH)
        val day = calender.get(Calendar.DAY_OF_MONTH)


        return DatePickerDialog(requireActivity()!!, this, year, month, day)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

    }
}