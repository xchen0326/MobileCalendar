package com.kizitonwose.mobilecalendar

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.mobilecalendar.databinding.CalendarDayBinding
import com.kizitonwose.mobilecalendar.databinding.CalendarFragmentBinding
import com.kizitonwose.mobilecalendar.databinding.CalendarHeaderBinding
import com.kizitonwose.mobilecalendar.databinding.EventItemViewBinding
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*


public interface ClosedRange<T: Comparable<T>> {
    public val start: T
    public val endInclusive: T
    public operator fun contains(value: T): Boolean =
        value >= start && value <= endInclusive
    public fun isEmpty(): Boolean = start > endInclusive
}

class DateIterator(val startDate: LocalDate,
                   val endDateInclusive: LocalDate,
                   val stepDays: Long): Iterator<LocalDate> {
    private var currentDate = startDate
    override fun hasNext() = currentDate <= endDateInclusive
    override fun next(): LocalDate {
        val next = currentDate
        currentDate = currentDate.plusDays(stepDays)
        return next
    }
}
class DateProgression(override val start: LocalDate,
                      override val endInclusive: LocalDate,
                      val stepDays: Long = 1) :
    Iterable<LocalDate>, ClosedRange<LocalDate> {
    override fun iterator(): Iterator<LocalDate> =
        DateIterator(start, endInclusive, stepDays)
    infix fun step(days: Long) = DateProgression(start, endInclusive, days)
}

operator fun LocalDate.rangeTo(other: LocalDate) = DateProgression(this, other)



data class Event(val id: String, val text: String, val date: LocalDate)

class EventsAdapter(val onClick: (Event) -> Unit) :
    RecyclerView.Adapter<EventsAdapter.EventsViewHolder>() {

    val events = mutableListOf<Event>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        return EventsViewHolder(
            EventItemViewBinding.inflate(parent.context.layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(viewHolder: EventsViewHolder, position: Int) {
        viewHolder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size

    inner class EventsViewHolder(private val binding: EventItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onClick(events[bindingAdapterPosition])
            }
        }

        fun bind(event: Event) {
            binding.itemEventText.text = event.text
        }
    }
}

class CalendarFragment : BaseFragment(R.layout.calendar_fragment), HasBackButton {


    private lateinit var stuViewModel: StudentViewModel
    private val eventsAdapter = EventsAdapter {
        AlertDialog.Builder(requireContext())
            .setMessage(R.string.dialog_delete_confirmation)
            .setPositiveButton(R.string.delete) { _, _ ->
                deleteEvent(it)
            }
            .setNegativeButton(R.string.close, null)
            .show()
    }

    private val inputDialog by lazy {
        val editText = AppCompatEditText(requireContext())
        val layout = FrameLayout(requireContext()).apply {
            // Setting the padding on the EditText only pads the input area
            // not the entire EditText so we wrap it in a FrameLayout.
            val padding = dpToPx(20, requireContext())
            setPadding(padding, padding, padding, padding)
            addView(editText, FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT))
        }
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.input_dialog_title))
            .setView(layout)
            .setPositiveButton(R.string.save) { _, _ ->
                saveEvent(editText.text.toString())
                // Prepare EditText for reuse.
                editText.setText("")
            }
            .setNegativeButton(R.string.close, null)
            .create()
            .apply {
                setOnShowListener {
                    // Show the keyboard
                    editText.requestFocus()
                    context.inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
                }
                setOnDismissListener {
                    // Hide the keyboard
                    context.inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
                }
            }
    }

    override val titleRes: Int = R.string.calendar_title

    private var selectedDate: LocalDate? = null
    private val today = LocalDate.now()

    private val titleSameYearFormatter = DateTimeFormatter.ofPattern("MMMM")
    private val titleFormatter = DateTimeFormatter.ofPattern("MMM yyyy")
    private val selectionFormatter = DateTimeFormatter.ofPattern("d MMM yyyy")
    private val events = mutableMapOf<LocalDate, List<Event>>()

    private lateinit var binding: CalendarFragmentBinding

    private val aTermStartDate:LocalDate = LocalDate.of(2020, 8, 31)
    private val aTermEndDate:LocalDate = LocalDate.of(2020, 10, 16)
    private val aTerm: DateProgression = aTermStartDate..aTermEndDate;

    private val bTermStartDate:LocalDate = LocalDate.of(2020, 10, 21)
    private val bTermEndDate:LocalDate = LocalDate.of(2020, 12, 11)
    private val bTerm: DateProgression = bTermStartDate..bTermEndDate;

    private val cTermStartDate:LocalDate = LocalDate.of(2021, 1, 21)
    private val cTermEndDate:LocalDate = LocalDate.of(2021, 3, 10)
    private val cTerm: DateProgression = cTermStartDate..cTermEndDate;

    private val dTermStartDate:LocalDate = LocalDate.of(2021, 3, 15)
    private val dTermEndDate:LocalDate = LocalDate.of(2021, 5, 4)
    private val dTerm: DateProgression = dTermStartDate..dTermEndDate;

    private var courseString = String()
    private var courseList = mutableListOf<Course>()

    var converter = Converter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stuViewModel = ViewModelProvider(this).get(StudentViewModel::class.java)
        courseString =
            stuViewModel.getStudentCourses(HomeActivity.instance?.user_name?: "invalid user").toString()
        //getStudentCourses(HomeActivity.instance?.user_name?: "invalid user")?: String?
        if(courseString != "invalid user"){
            courseList = converter.toListCourse(courseString)!!
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding = CalendarFragmentBinding.bind(view)
        binding.Rv.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = eventsAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
        }

        val daysOfWeek = daysOfWeekFromLocale()
        val currentMonth = YearMonth.now()
        binding.Calendar.apply {
            setup(currentMonth.minusMonths(10), currentMonth.plusMonths(10), daysOfWeek.first())
            scrollToMonth(currentMonth)
        }

        if (savedInstanceState == null) {
            binding.Calendar.post {
                // Show today's events initially.
                selectDate(today)
            }
        }

        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay // Will be set when this container is bound.
            val binding = CalendarDayBinding.bind(view)

            init {
                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        selectDate(day.date)
                    }
                }
            }
        }
        binding.Calendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.binding.DayText
                val dotView = container.binding.DotView

                textView.text = day.date.dayOfMonth.toString()

                if (day.owner == DayOwner.THIS_MONTH) {
                    textView.makeVisible()
                    when (day.date) {
                        today -> {
                            textView.setTextColorRes(R.color.example_white)
                            textView.setBackgroundResource(R.drawable.today_bg)
                            dotView.makeInVisible()
                        }
                        selectedDate -> {
                            textView.setTextColorRes(R.color.example_blue)
                            textView.setBackgroundResource(R.drawable.selected_bg)
                            dotView.makeInVisible()
                        }
                        else -> {
                            textView.setTextColorRes(R.color.example_black)
                            textView.background = null
                            dotView.isVisible = events[day.date].orEmpty().isNotEmpty()
                        }
                    }
                } else {
                    textView.makeInVisible()
                    dotView.makeInVisible()
                }
            }
        }

        binding.Calendar.monthScrollListener = {
            homeActivityToolbar.title = if (it.year == today.year) {
                titleSameYearFormatter.format(it.yearMonth)
            } else {
                titleFormatter.format(it.yearMonth)
            }

            // Select the first day of the month when
            // we scroll to a new month.
            selectDate(it.yearMonth.atDay(1))
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val legendLayout = CalendarHeaderBinding.bind(view).legendLayout.root
        }
        binding.Calendar.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                // Setup each header day text if we have not done that already.
                if (container.legendLayout.tag == null) {
                    container.legendLayout.tag = month.yearMonth
                    container.legendLayout.children.map { it as TextView }.forEachIndexed { index, tv ->
                        tv.text = daysOfWeek[index].name.first().toString()
                        tv.setTextColorRes(R.color.example_black)
                    }
                }
            }
        }

        binding.AddButton.setOnClickListener {

            // Start CourseEditActivity
            val intent = Intent(activity, CourseEditActivity::class.java)

            val todayTemp: String = selectionFormatter.format(today)
            intent.putExtra("DATE", todayTemp);

            startActivityForResult(intent,0)
        }

        binding.FreshButton.setOnClickListener{
            println("fresh is clicked")
            (activity as HomeActivity).freshCalendar(view)
        }

        if(!courseList.isNullOrEmpty()) {
            for (course in courseList) {
                val courseName: String = course.getCourseName()
                val location: String = course.getLocation()
                val startTime: String = course.getStartTime()
                val endTime: String = course.getEndTime()
                val term: String = course.getCourseTerm()
                val dayOfWeek: ArrayList<String> = ArrayList(course.getRecurrance())

                val courseDetail: String =
                    "$courseName\n$location\n$startTime - $endTime"

                var selectedTerm: DateProgression

                selectedTerm = if (term == "A") {
                    aTerm
                } else if (term == "B") {
                    bTerm
                } else if (term == "C") {
                    cTerm
                } else {
                    dTerm
                }
                addCourseToCalendar(selectedTerm, courseDetail, dayOfWeek)
            }
        }
    }

    private fun addCourseToCalendar(term: DateProgression, courseDetail: String, dayOfWeek: ArrayList<String>){
        val fixSelectedDate = selectedDate

        for(date in term step 1){
            for(i in dayOfWeek){
                //Check Monday
                if(date.dayOfWeek == DayOfWeek.of(1)){
                    if(i == "M"){
                        selectedDate = date;
                        saveEvent(courseDetail)
                    }
                }
                //Check Tuesday
                else if(date.dayOfWeek == DayOfWeek.of(2)){
                    if(i == "T"){
                        selectedDate = date;
                        saveEvent(courseDetail)
                    }
                }
                //Check Wednesday
                else if(date.dayOfWeek == DayOfWeek.of(3)){
                    if(i == "W"){
                        selectedDate = date;
                        saveEvent(courseDetail)
                    }
                }
                //Check Thursday
                else if(date.dayOfWeek == DayOfWeek.of(4)){
                    if(i == "Th"){
                        selectedDate = date;
                        saveEvent(courseDetail)
                    }
                }
                //Check Friday
                else if(date.dayOfWeek == DayOfWeek.of(5)){
                    if(i == "F"){
                        selectedDate = date;
                        saveEvent(courseDetail)
                    }
                }
                //Check Saturday
                else if(date.dayOfWeek == DayOfWeek.of(6)){
                    if(i == "Sa"){
                        selectedDate = date;
                        saveEvent(courseDetail)
                    }
                }
                //Check Sunday
                else if(date.dayOfWeek == DayOfWeek.of(7)){
                    if(i == "S"){
                        selectedDate = date;
                        saveEvent(courseDetail)
                    }
                }
            }
        }

        selectedDate = fixSelectedDate
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var courseName: String = data?.getStringExtra("COURSE_NAME") ?: "invalid name"
        var location: String = data?.getStringExtra("LOCATION") ?: "invalid location"
        val startTime: String = data?.getStringExtra("START_TIME") ?: "invalid start time"
        val endTime: String = data?.getStringExtra("END_TIME") ?: "invalid end time"
        val term: String = data?.getStringExtra("TERM") ?: "invalid term"
        val dayOfWeek: ArrayList<String> = data?.getStringArrayListExtra("DAY_IN_WEEK") ?: ArrayList<String>()

        if(courseName.contains(',') || courseName.contains(';') || courseName.contains('/')){
            courseName = "invalid name"
        }else if(location.contains(',') || location.contains(';') || location.contains('/')){
            location = "invalid location"
        }

        val courseDetail:String =
            "$courseName\n$location\n$startTime - $endTime"

        var selectedTerm: DateProgression

        selectedTerm = if(term == "A") {
            aTerm
        } else if (term == "B") {
            bTerm
        } else if (term == "C") {
            cTerm
        } else {
            dTerm
        }

        addCourseToCalendar(selectedTerm, courseDetail, dayOfWeek)

//        Toast.makeText(context, courseName, Toast.LENGTH_SHORT).show()
    }

    private fun selectDate(date: LocalDate) {
        if (selectedDate != date) {
            val oldDate = selectedDate
            selectedDate = date
            oldDate?.let { binding.Calendar.notifyDateChanged(it) }
            binding.Calendar.notifyDateChanged(date)
            updateAdapterForDate(date)
        }
    }

    private fun saveEvent(text: String) {
        if (text.isBlank()) {
            Toast.makeText(requireContext(), R.string.empty_input_text, Toast.LENGTH_LONG).show()
        } else {
            selectedDate?.let {
                events[it] = events[it].orEmpty().plus(Event(UUID.randomUUID().toString(), text, it))
                updateAdapterForDate(it)
            }
        }
    }

    private fun deleteEvent(event: Event) {
        val password = HomeActivity.instance?.pass_word
        val username = HomeActivity.instance?.user_name
        val str = HomeActivity.instance?.user_name?.let { stuViewModel.getStudentCourses(it) }
        val loc = converter.toListCourse(str)
        var term = String()
        if(loc == null){
            Toast.makeText(context, "Nothiing to be delete", Toast.LENGTH_SHORT).show()
        }else{
            for(it in loc){
                val lengthC = it.getCourseName().length
                val lengthL = it.getLocation().length
                println("-------------------------------")
                println(event.text.substring(0,lengthC))
                println(event.text.substring(lengthC,lengthC+lengthL+1))
                println(it.getCourseName() )
                println(it.getLocation() )
                if(it.getCourseName().trim() == event.text.substring(0,lengthC).trim() &&
                    it.getLocation().trim() == event.text.substring(lengthC,lengthC+lengthL+1).trim()){
                    println("--------inside condition---------")
                    term = it.getCourseTerm()
                    loc.remove(it)
                    break
                }
            }
            val strDeleted = converter.fromListCourse(loc)
            if(username == null || password == null || strDeleted == null){
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
            }else{
                val student = Student(username, password, strDeleted)
                stuViewModel.updateStudent(student)
            }
        }
        if(term == "A"){
            for(date in aTerm step 1){
                var loe = events[date]
                if (loe != null) {
                    for(e in loe){
                        if(e.text == event.text){
                            events[date] = events[date].orEmpty().minus(Event(e.id, event.text, date))
                            updateAdapterForDate(date)
                        }
                    }
                }
            }
        }else if(term == "B"){
            for(date in bTerm step 1){
                var loe = events[date]
                if (loe != null) {
                    for(e in loe){
                        if(e.text == event.text){
                            events[date] = events[date].orEmpty().minus(Event(e.id, event.text, date))
                            updateAdapterForDate(date)
                        }
                    }
                }
            }
        }else if(term == "C"){
            for(date in cTerm step 1){
                var loe = events[date]
                if (loe != null) {
                    for(e in loe){
                        if(e.text == event.text){
                            events[date] = events[date].orEmpty().minus(Event(e.id, event.text, date))
                            updateAdapterForDate(date)
                        }
                    }
                }
            }
        }else if(term == "D"){
            for(date in dTerm step 1){
                var loe = events[date]
                if (loe != null) {
                    for(e in loe){
                        if(e.text == event.text){
                            events[date] = events[date].orEmpty().minus(Event(e.id, event.text, date))
                            updateAdapterForDate(date)
                        }
                    }
                }
            }
        }
    }

    private fun updateAdapterForDate(date: LocalDate) {
        eventsAdapter.apply {
            events.clear()
            events.addAll(this@CalendarFragment.events[date].orEmpty())
            notifyDataSetChanged()
        }
        binding.SelectedDateText.text = selectionFormatter.format(date)
    }

    override fun onStart() {
        super.onStart()
        homeActivityToolbar.setBackgroundColor(requireContext().getColorCompat(R.color.example_toolbar_color))
        requireActivity().window.statusBarColor = requireContext().getColorCompat(R.color.example_statusbar_color)
    }

    override fun onStop() {
        super.onStop()
        homeActivityToolbar.setBackgroundColor(requireContext().getColorCompat(R.color.colorPrimary))
        requireActivity().window.statusBarColor = requireContext().getColorCompat(R.color.colorPrimaryDark)
    }
}
