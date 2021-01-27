package app.beyond.todo_list

enum class IntentKey {
    TITLE, DEADLINE, TASK_DETAIL, IS_COMPLETED, MODE_IN_EDIT, SETTINGS
}

enum class ModeIntent {
    NEW_ENTRY, EDIT
}

enum class FragmentTag {
    MASTER, DETAIL, EDIT, DATE_PICKER, SETTINGS, CALENDAR
}
