package app.beyond.todo_list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
//import com.example.beyond_todo_list.R

class SettingsFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contain_settings, container, false)
        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.apply {
            findItem(R.id.menu_settings).isVisible = false
            findItem(R.id.menu_delete).isVisible = false
            findItem(R.id.menu_edit).isVisible = false
            findItem(R.id.menu_register).isVisible = false
            findItem(R.id.menu_calendar).isVisible = false
        }
    }

}